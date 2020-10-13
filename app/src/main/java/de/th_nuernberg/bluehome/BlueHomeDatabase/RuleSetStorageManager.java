package de.th_nuernberg.bluehome.BlueHomeDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.drm.DrmStore;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import de.th_nuernberg.bluehome.BlueHomeDevice;
import de.th_nuernberg.bluehome.RuleProcessObjects.ActionObject;
import de.th_nuernberg.bluehome.RuleProcessObjects.RuleObject;
import de.th_nuernberg.bluehome.RuleProcessObjects.RulesetObject;
import de.th_nuernberg.bluehome.RuleProcessObjects.SourceObject;

/**
 * RuleSetStorageManager (RuleActionStorageManager
 */
public class RuleSetStorageManager extends DatabaseInitiator {

    private ActionStorageManager asm;
    private RuleStorageManager rsm;
    private BlueHomeDeviceStorageManager dsm;

    public RuleSetStorageManager(Context context){
        super(context);
        asm = new ActionStorageManager(context);
        rsm = new RuleStorageManager(context);
        dsm = new BlueHomeDeviceStorageManager(context);
    }


    /**
     * This Method inserts a new Ruleset into the Database
     *
     * @param rso Ruleset to insert into the Database
     * @return true on success
     */
    public boolean insertRuleset (RulesetObject rso) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.RULESET_COLUMN_RULESET_ID, rso.getRulesetID());
        contentValues.put(DBConstants.RULESET_COLUMN_APP_ACTION_1_ID, rso.getAction1().getAppActionID());
        contentValues.put(DBConstants.RULESET_COLUMN_APP_RULE_1_ID, rso.getRule1().getAppRuleID());
        contentValues.put(DBConstants.RULESET_COLUMN_DEV_1_MAC, rso.getDev1().getMacAddress());
        if(rso.getRule2() != null) {
            contentValues.put(DBConstants.RULESET_COLUMN_DEV_2_MAC, rso.getDev2().getMacAddress());
            contentValues.put(DBConstants.RULESET_COLUMN_APP_ACITON_2_ID, rso.getAction2().getAppActionID());
            contentValues.put(DBConstants.RULESET_COLUMN_APP_RULE_2_ID, rso.getRule2().getAppRuleID());
        } else
        {
            contentValues.put(DBConstants.RULESET_COLUMN_DEV_2_MAC, "");
            contentValues.put(DBConstants.RULESET_COLUMN_APP_ACITON_2_ID, -1);
            contentValues.put(DBConstants.RULESET_COLUMN_APP_RULE_2_ID, -1);
        }
        contentValues.put(DBConstants.RULESET_COLUMN_NAME, rso.getName());
        db.insert(DBConstants.RULESET_TABLE_NAME, null, contentValues);
        rsm.insertRule(rso.getRule1());
        asm.insertAction(rso.getAction1());
        if(rso.getRule2() != null) {
            Log.i("RuleSetStorageManagerLogger", "write");
            rsm.insertRule(rso.getRule2());
            asm.insertAction(rso.getAction2());
        }
        return true;
    }

    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ DBConstants.DEVICES_TABLE_NAME);
        db.execSQL("delete from "+ DBConstants.RULESET_TABLE_NAME);
        db.execSQL("delete from "+ DBConstants.ACTIONS_TABLE_NAME);
        db.execSQL("delete from "+ DBConstants.RULES_TABLE_NAME);
    }

    public void readFromJSON(String file) throws IOException
    {
        JsonReader reader = new JsonReader(new StringReader(file));
        reader.beginObject();
        while(reader.hasNext())
        {
            String s = reader.nextName();
            if (s.equals("Rules"))
            {
                reader.beginArray();
                while (reader.hasNext())
                {
                    reader.beginObject();
                    RuleObject newRule = new RuleObject();
                    newRule.setToComp(new SourceObject());
                    while (reader.hasNext())
                    {
                        s = reader.nextName();
                        if (s.equals(DBConstants.RULES_COLUMN_APP_RULE_ID))
                            newRule.setAppRuleID((byte)reader.nextInt());
                        else if (s.equals(DBConstants.RULES_COLUMN_RULE_MEM_ID))
                            newRule.setRuleMemID((byte)reader.nextInt());
                        else if (s.equals(DBConstants.RULES_COLUMN_ACTION_MEM_ID))
                            newRule.setActionMemID((byte)reader.nextInt());
                        else if (s.equals(DBConstants.RULES_COLUMN_PARAM_COMP))
                            newRule.setParamComp(reader.nextString().getBytes());
                        else if (s.equals(DBConstants.RULES_COLUMN_SOURCE_SAM))
                            newRule.getToComp().setSourceSAM((byte)reader.nextInt());
                        else if (s.equals(DBConstants.RULES_COLUMN_SOURCE_ID))
                            newRule.getToComp().setSourceID((byte)reader.nextInt());
                        else if (s.equals(DBConstants.RULES_COLUMN_SOURCE_PARAM))
                            newRule.getToComp().setParams(reader.nextString().getBytes());
                    }
                    rsm.insertRule(newRule);
                    reader.endObject();
                }
                reader.endArray();
            }
            else if (s.equals("Actions"))
            {
                reader.beginArray();
                while (reader.hasNext())
                {
                    reader.beginObject();
                    ActionObject newAction = new ActionObject();
                    while (reader.hasNext())
                    {
                        s = reader.nextName();
                        if (s.equals(DBConstants.ACTIONS_COLUMN_APP_ACTION_ID))
                            newAction.setAppActionID((byte)reader.nextInt());
                        else if (s.equals(DBConstants.ACTIONS_COLUMN_MEM_ID))
                            newAction.setActionMemID((byte)reader.nextInt());
                        else if (s.equals(DBConstants.ACTIONS_COLUMN_SAM))
                            newAction.setActionSAM((byte)reader.nextInt());
                        else if (s.equals(DBConstants.ACTIONS_COLUMN_PARAM_MASK))
                            newAction.setParamMask(reader.nextInt());
                        else if (s.equals(DBConstants.ACTIONS_COLUMN_PARAMS))
                            newAction.setParam(reader.nextString().getBytes());
                        else if (s.equals(DBConstants.ACTIONS_COLUMN_ID))
                            newAction.setActionID((byte)reader.nextInt());
                    }
                    reader.endObject();
                    asm.insertAction(newAction);
                }
                reader.endArray();
            }
            else if (s.equals("Devices"))
            {
                reader.beginArray();
                while (reader.hasNext())
                {
                    reader.beginObject();
                    BlueHomeDevice newDevice = new BlueHomeDevice();
                    while (reader.hasNext())
                    {
                        s = reader.nextName();
                        if (s.equals(DBConstants.DEVICES_COLUMN_SHOWN_NAME))
                            newDevice.setShownName(reader.nextString());
                        else if (s.equals(DBConstants.DEVICES_COLUMN_REAL_NAME))
                            newDevice.setDeviceName(reader.nextString());
                        else if (s.equals(DBConstants.DEVICES_COLUMN_MAC))
                            newDevice.setAddress(reader.nextString());
                        else if (s.equals(DBConstants.DEVICES_COLUMN_MAC_ID))
                            newDevice.setMacId((byte)reader.nextInt());
                        else if (s.equals(DBConstants.DEVICES_COLUMN_IMG_ID))
                            newDevice.setImgID(reader.nextInt());
                        else if (s.equals(DBConstants.DEVICES_COLUMN_NODE_TYPE))
                            newDevice.setNodeType(reader.nextInt());
                    }
                    reader.endObject();
                    dsm.insertDevice(newDevice);
                }
                reader.endArray();
            } else {
                reader.beginArray();
                while (reader.hasNext())
                {
                    reader.beginObject();
                    while (reader.hasNext())
                    {
                        reader.nextName();
                        reader.skipValue();
                    }
                    reader.endObject();
                }
                reader.endArray();
            }
        }
        reader.endObject();
        reader.close();
        reader = new JsonReader(new StringReader(file));
        reader.beginObject();
        while (reader.hasNext())
        {
            if (reader.nextName().equals("Rulesets"))
            {
                reader.beginArray();
                while (reader.hasNext())
                {
                    reader.beginObject();
                    RulesetObject newRS = new RulesetObject();
                    while (reader.hasNext())
                    {
                        String s = reader.nextName();
                        if (s.equals(DBConstants.RULESET_COLUMN_RULESET_ID))
                            newRS.setRulesetID((byte)reader.nextInt());
                        else if (s.equals(DBConstants.RULESET_COLUMN_APP_ACTION_1_ID))
                            newRS.setAction1(asm.getAction((byte)reader.nextInt()));
                        else if (s.equals(DBConstants.RULESET_COLUMN_APP_ACITON_2_ID)) {
                            int value = reader.nextInt();
                            if(value >= 0) {
                                newRS.setAction2(asm.getAction((byte) value));
                            }
                        }
                        else if (s.equals(DBConstants.RULESET_COLUMN_APP_RULE_1_ID))
                            newRS.setRule1(rsm.getRule((byte)reader.nextInt()));
                        else if (s.equals(DBConstants.RULESET_COLUMN_APP_RULE_2_ID)) {
                            int value = reader.nextInt();
                            if(value >= 0) {
                                newRS.setRule2(rsm.getRule((byte) value));
                            }
                        }
                        else if (s.equals(DBConstants.RULESET_COLUMN_NAME))
                            newRS.setName(reader.nextString());
                        else if (s.equals(DBConstants.RULESET_COLUMN_DEV_1_MAC))
                            newRS.setDev1(dsm.getDevice(reader.nextString()));
                        else if (s.equals(DBConstants.RULESET_COLUMN_DEV_2_MAC)) {
                            String value = reader.nextString();
                            if(!value.isEmpty()) {
                                newRS.setDev2(dsm.getDevice(value));
                            }
                        }
                    }
                    reader.endObject();
                    insertRuleset(newRS);
                }
                reader.endArray();
            } else {
                reader.beginArray();
                while (reader.hasNext())
                {
                    reader.beginObject();
                    while (reader.hasNext())
                    {
                        reader.nextName();
                        reader.skipValue();
                    }
                    reader.endObject();
                }
                reader.endArray();
            }
        }
        reader.endObject();
        reader.close();
    }

    public String toJSON() throws IOException
    {
        StringWriter sw = new StringWriter();
        JsonWriter writer = new JsonWriter(sw);
        writer.beginObject();
        writer.name("Rulesets").beginArray();
        for (RulesetObject rs : getAllRulessets())
        {
            writer.beginObject();
            writer.name(DBConstants.RULESET_COLUMN_APP_RULE_1_ID).value(rs.getRule1().getAppRuleID());
            if(rs.getRule2() != null){
                writer.name(DBConstants.RULESET_COLUMN_APP_RULE_2_ID).value(rs.getRule2().getAppRuleID());
                writer.name(DBConstants.RULESET_COLUMN_APP_ACITON_2_ID).value(rs.getAction2().getAppActionID());
                writer.name(DBConstants.RULESET_COLUMN_DEV_2_MAC).value(rs.getDev2().getMacAddress());
            } else {
                writer.name(DBConstants.RULESET_COLUMN_APP_RULE_2_ID).value(-1);
                writer.name(DBConstants.RULESET_COLUMN_APP_ACITON_2_ID).value(-1);
                writer.name(DBConstants.RULESET_COLUMN_DEV_2_MAC).value("");
            }
            writer.name(DBConstants.RULESET_COLUMN_APP_ACTION_1_ID).value(rs.getAction1().getAppActionID());
            writer.name(DBConstants.RULESET_COLUMN_RULESET_ID).value(rs.getRulesetID());
            writer.name(DBConstants.RULESET_COLUMN_DEV_1_MAC).value(rs.getDev1().getMacAddress());
            writer.name(DBConstants.RULESET_COLUMN_NAME).value(rs.getName());
            writer.endObject();
        }
        writer.endArray();
        writer.name("Rules").beginArray();
        for (RuleObject ruleObject : rsm.getAllRules())
        {
            writer.beginObject();
            writer.name(DBConstants.RULES_COLUMN_APP_RULE_ID).value(ruleObject.getAppRuleID());
            writer.name(DBConstants.RULES_COLUMN_RULE_MEM_ID).value(ruleObject.getRuleMemID());
            writer.name(DBConstants.RULES_COLUMN_ACTION_MEM_ID).value(ruleObject.getActionMemID());
            writer.name(DBConstants.RULES_COLUMN_PARAM_COMP).value(ruleObject.getParamComp().toString());
            writer.name(DBConstants.RULES_COLUMN_SOURCE_SAM).value(ruleObject.getToComp().getSourceSAM());
            writer.name(DBConstants.RULES_COLUMN_SOURCE_ID).value(ruleObject.getToComp().getSourceID());
            writer.name(DBConstants.RULES_COLUMN_SOURCE_PARAM).value(ruleObject.getToComp().getParams().toString());
            writer.endObject();
        }
        writer.endArray();
        writer.name("Actions").beginArray();
        for (ActionObject actionObject : asm.getAllActions())
        {
            writer.beginObject();
            writer.name(DBConstants.ACTIONS_COLUMN_APP_ACTION_ID).value(actionObject.getAppActionID());
            writer.name(DBConstants.ACTIONS_COLUMN_MEM_ID).value(actionObject.getActionMemID());
            writer.name(DBConstants.ACTIONS_COLUMN_SAM).value(actionObject.getActionSAM());
            writer.name(DBConstants.ACTIONS_COLUMN_ID).value(actionObject.getActionID());
            writer.name(DBConstants.ACTIONS_COLUMN_PARAM_MASK).value(actionObject.getParamMask());
            writer.name(DBConstants.ACTIONS_COLUMN_PARAMS).value(actionObject.getParam().toString());
            writer.endObject();
        }
        writer.endArray();
        writer.name("Devices").beginArray();
        for (BlueHomeDevice blueHomeDevice : dsm.getAllDevices())
        {
            writer.beginObject();
            writer.name(DBConstants.DEVICES_COLUMN_SHOWN_NAME).value(blueHomeDevice.getShownName());
            writer.name(DBConstants.DEVICES_COLUMN_REAL_NAME).value(blueHomeDevice.getDeviceName());
            writer.name(DBConstants.DEVICES_COLUMN_MAC).value(blueHomeDevice.getMacAddress());
            writer.name(DBConstants.DEVICES_COLUMN_MAC_ID).value(blueHomeDevice.getMacId());
            writer.name(DBConstants.DEVICES_COLUMN_IMG_ID).value(blueHomeDevice.getImgID());
            writer.name(DBConstants.DEVICES_COLUMN_NODE_TYPE).value(blueHomeDevice.getNodeType());
            writer.endObject();
        }
        writer.endArray();
        writer.endObject();
        writer.close();
        return sw.toString();
    }

    private Cursor getData(byte rulesetID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + DBConstants.RULESET_TABLE_NAME + " where " + DBConstants.RULESET_COLUMN_RULESET_ID + "='" + rulesetID + "'", null );
        return res;
    }

    public RulesetObject getRuleset(byte rulesetID) {
        RulesetObject tmp;
        Cursor res = getData(rulesetID);
        if(res.moveToFirst()) {
            tmp = new RulesetObject();
            tmp.setRulesetID((byte)res.getInt(res.getColumnIndex(DBConstants.RULESET_COLUMN_RULESET_ID)));
            tmp.setAction1(asm.getAction((byte)res.getInt(res.getColumnIndex(DBConstants.RULESET_COLUMN_APP_ACTION_1_ID))));
            tmp.setRule1(rsm.getRule((byte)res.getInt(res.getColumnIndex(DBConstants.RULESET_COLUMN_APP_RULE_1_ID))));
            tmp.setDev1(dsm.getDevice(res.getString(res.getColumnIndex(DBConstants.RULESET_COLUMN_DEV_1_MAC))));
            tmp.setName(res.getString(res.getColumnIndex(DBConstants.RULESET_COLUMN_NAME)));
            if(res.getInt(res.getColumnIndex(DBConstants.RULESET_COLUMN_APP_RULE_2_ID)) != -1) {
                tmp.setRule2(rsm.getRule((byte) res.getInt(res.getColumnIndex(DBConstants.RULESET_COLUMN_APP_RULE_2_ID))));
                tmp.setAction2(asm.getAction((byte) res.getInt(res.getColumnIndex(DBConstants.RULESET_COLUMN_APP_ACITON_2_ID))));
                tmp.setDev2(dsm.getDevice(res.getString(res.getColumnIndex(DBConstants.RULESET_COLUMN_DEV_2_MAC))));
            }
            else
            {
                tmp.setRule2(null);
                tmp.setAction2(null);
                tmp.setDev2(null);
            }
            return tmp;
        } else {
            Log.e("RulesetStorageManager", "didn't move");
            return null;
        }
    }

    /**
     * Overwrites/Updates Ruleset
     * @param ruleset Ruleset to update, orientation on App Rule ID
     * @return true on success
     */
    public boolean updateRuleset (RulesetObject ruleset) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.RULESET_COLUMN_RULESET_ID, ruleset.getRulesetID());
        contentValues.put(DBConstants.RULESET_COLUMN_APP_ACTION_1_ID, ruleset.getAction1().getAppActionID());
        contentValues.put(DBConstants.RULESET_COLUMN_APP_RULE_1_ID, ruleset.getRule1().getAppRuleID());
        contentValues.put(DBConstants.RULESET_COLUMN_DEV_1_MAC, ruleset.getDev1().getMacAddress());
        if(ruleset.getRule2() != null) {
            contentValues.put(DBConstants.RULESET_COLUMN_DEV_2_MAC, ruleset.getDev2().getMacAddress());
            contentValues.put(DBConstants.RULESET_COLUMN_APP_ACITON_2_ID, ruleset.getAction2().getAppActionID());
            contentValues.put(DBConstants.RULESET_COLUMN_APP_RULE_2_ID, ruleset.getRule2().getAppRuleID());
        } else {
            contentValues.put(DBConstants.RULESET_COLUMN_DEV_2_MAC, "");
            contentValues.put(DBConstants.RULESET_COLUMN_APP_ACITON_2_ID, -1);
            contentValues.put(DBConstants.RULESET_COLUMN_APP_RULE_2_ID, -1);
        }
        contentValues.put(DBConstants.RULESET_COLUMN_NAME, ruleset.getName());

        db.update(DBConstants.RULESET_TABLE_NAME, contentValues, DBConstants.RULESET_COLUMN_RULESET_ID + " = ? ", new String[] { "" + ruleset.getRulesetID() } );
        return true;
    }

    /**
     * Deletes Device at RulesetID
     * @param rulesetID Ruleset to Delete
     * @return delete return value
     */
    public Integer deleteRuleSet (byte rulesetID) {
        SQLiteDatabase db = this.getWritableDatabase();
        RulesetObject rso = getRuleset(rulesetID);

        asm.deleteAction(rso.getAction1().getAppActionID());
        rsm.deleteRule(rso.getRule1().getAppRuleID());
        if(rso.getRule2() != null) {
            rsm.deleteRule(rso.getRule2().getAppRuleID());
            asm.deleteAction(rso.getAction2().getAppActionID());
        }

        return db.delete(DBConstants.RULESET_TABLE_NAME,
                DBConstants.RULESET_COLUMN_RULESET_ID + " = ? ",
                new String[] { "" + rulesetID });
    }

    /**
     *  Reads all Rulesets in Memory and Returns them in an ArrayList
     * @return Arraylist<RulesetObject>
     */
    public ArrayList<RulesetObject> getAllRulessets() {
        ArrayList<RulesetObject> array_list = new ArrayList<RulesetObject>();
        RulesetObject tmp;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + DBConstants.RULESET_TABLE_NAME, null );
        res.moveToFirst();

        Log.i("RuleSetStorageManager", "preparing ArrayList<>");

        while(res.isAfterLast() == false){
            tmp = new RulesetObject();
            tmp.setRulesetID((byte)res.getInt(res.getColumnIndex(DBConstants.RULESET_COLUMN_RULESET_ID)));
            tmp.setAction1(asm.getAction((byte)res.getInt(res.getColumnIndex(DBConstants.RULESET_COLUMN_APP_ACTION_1_ID))));
            tmp.setRule1(rsm.getRule((byte)res.getInt(res.getColumnIndex(DBConstants.RULESET_COLUMN_APP_RULE_1_ID))));
            tmp.setDev1(dsm.getDevice(res.getString(res.getColumnIndex(DBConstants.RULESET_COLUMN_DEV_1_MAC))));
            if(res.getInt(res.getColumnIndex(DBConstants.RULESET_COLUMN_APP_RULE_2_ID)) != -1) {
                tmp.setDev2(dsm.getDevice(res.getString(res.getColumnIndex(DBConstants.RULESET_COLUMN_DEV_2_MAC))));
                tmp.setRule2(rsm.getRule((byte) res.getInt(res.getColumnIndex(DBConstants.RULESET_COLUMN_APP_RULE_2_ID))));
                tmp.setAction2(asm.getAction((byte) res.getInt(res.getColumnIndex(DBConstants.RULESET_COLUMN_APP_ACITON_2_ID))));
            }
            else
            {

                tmp.setDev2(null);
                tmp.setRule2(null);
                tmp.setAction2(null);
            }
            tmp.setName(res.getString(res.getColumnIndex(DBConstants.RULESET_COLUMN_NAME)));
            array_list.add(tmp);
            res.moveToNext();
        }
        return array_list;
    }

    public BlueHomeDevice getDevForRule(RuleObject rule)
    {
        ArrayList<RulesetObject> rulesets = this.getAllRulessets();
        for(RulesetObject o : rulesets)
        {
            if(o.getRule1().getAppRuleID() == rule.getAppRuleID())
                return o.getDev1();
            if(o.getRule2() != null && o.getRule2().getAppRuleID() == rule.getAppRuleID())
                return o.getDev2();
        }

        return null;
    }

    public BlueHomeDevice getDevForAction(ActionObject action)
    {
        ArrayList<RulesetObject> rulesets = this.getAllRulessets();
        for(RulesetObject o : rulesets)
        {
            Log.i("LOL", "dev name " + o.getDev1().getShownName());
            Log.i("LOL", "id 1 und 2 " + o.getAction1().getAppActionID() + " " + action.getAppActionID());

            if(o.getAction1().getAppActionID() == action.getAppActionID())
                return o.getDev1();
            if(o.getAction2() != null && o.getAction2().getAppActionID() == action.getAppActionID())
                return o.getDev2();
        }

        return null;
    }

    public byte getNextFreeActionMemId(BlueHomeDevice dev, byte startValue){
        ArrayList<ActionObject> actions = asm.getAllActions();
        boolean foundFree;
        for(byte i = startValue; i < 32; i++)
        {
            foundFree = true;

            for (ActionObject s : actions) {
                Log.i("LOL", "dev for action: " + getDevForAction(s).getShownName());
                if (s.getActionMemID() == i && getDevForAction(s).getMacAddress().equals(dev.getMacAddress())) {
                    foundFree = false;
                    break;
                }
            }

            if(foundFree) {
                Log.i("RuleSetStorageManager", "found free action mem id: " + i);
                return i;
            }
        }
        return 0;
    }

    public byte getNextFreeRuleMemId(BlueHomeDevice dev, byte startValue){
        ArrayList<RuleObject> rules = rsm.getAllRules();
        boolean foundFree;
        for(byte i = startValue; i < 32; i++)
        {
            foundFree = true;



            for (RuleObject s : rules) {
                if (s.getRuleMemID() == i && getDevForRule(s).getMacAddress().equals(dev.getMacAddress())) {
                    foundFree = false;
                    break;
                }
            }

            if(foundFree) {
                Log.i("RuleSetStorageManager", "found free rule mem id: " + i);
                return i;
            }
        }
        return 0;
    }

    public byte getNextAppRuleId(byte startValue)
    {
        return rsm.getNextFreeAppId(startValue);
    }

    public byte getNextAppActionId(byte startValue)
    {
        return asm.getNextFreeAppId(startValue);
    }

    public byte getNextFreeRulesetId(){
        ArrayList<RulesetObject> rulesets = this.getAllRulessets();
        boolean foundFree;
        for(byte i = 0; i < 32; i++)
        {
            foundFree = true;
            for (RulesetObject s : rulesets) {
                if (s.getRulesetID() == i) {
                    foundFree = false;
                    break;
                }
            }
            if(foundFree)
            {
                Log.i("RuleStorageManager", "found free rule id: " + i);
                return i;

            }

        }
        return 0;
    }

    public void deleteRelatedRules(BlueHomeDevice dev) {
        ArrayList<RulesetObject> sets = getAllRulessets();
        for(RulesetObject set : sets) {
            if(set.getDev1().equals(dev) || (set.getDev2() != null && set.getDev2().equals(dev))) {
                deleteRuleSet(set.getRulesetID());
            }
        }
    }
}
