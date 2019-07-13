package de.th_nuernberg.bluehome.BlueHomeDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

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

    public byte getNextFreeActionMemId(BlueHomeDevice dev){
        ArrayList<ActionObject> actions = asm.getAllActions();
        boolean foundFree;
        for(byte i = 0; i < 32; i++)
        {
            foundFree = true;

            for (ActionObject s : actions) {
                Log.i("LOL", "dev for action: " + getDevForAction(s).getShownName());
                if (s.getActionMemID() == i && getDevForAction(s).getMacAddress().equals(dev.getMacAddress())) {
                    foundFree = false;
                    break;
                }
            }

            if(foundFree)
                return i;
        }
        return 0;
    }

    public byte getNextFreeRuleMemId(BlueHomeDevice dev){
        ArrayList<RuleObject> rules = rsm.getAllRules();
        boolean foundFree;
        for(byte i = 0; i < 32; i++)
        {
            foundFree = true;



            for (RuleObject s : rules) {
                if (s.getRuleMemID() == i && getDevForRule(s).getMacAddress().equals(dev.getMacAddress())) {
                    foundFree = false;
                    break;
                }
            }

            if(foundFree)
                return i;
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

}
