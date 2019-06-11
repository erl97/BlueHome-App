package de.th_nuernberg.bluehome.BlueHomeDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import de.th_nuernberg.bluehome.BlueHomeDevice;
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
        contentValues.put(DBConstants.RULESET_COLUMN_APP_ACITON_2_ID, rso.getAction2().getAppActionID());
        contentValues.put(DBConstants.RULESET_COLUMN_APP_RULE_1_ID, rso.getRule1().getAppRuleID());
        contentValues.put(DBConstants.RULESET_COLUMN_APP_RULE_2_ID, rso.getRule2().getAppRuleID());
        contentValues.put(DBConstants.RULESET_COLUMN_DEV_1_MAC, rso.getDev1().getMacAddress());
        contentValues.put(DBConstants.RULESET_COLUMN_DEV_2_MAC, rso.getDev2().getMacAddress());
        contentValues.put(DBConstants.RULESET_COLUMN_NAME, rso.getName());
        db.insert(DBConstants.RULESET_TABLE_NAME, null, contentValues);
        rsm.insertRule(rso.getRule1());
        rsm.insertRule(rso.getRule2());
        asm.insertAction(rso.getAction1());
        asm.insertAction(rso.getAction2());
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
            tmp.setAction2(asm.getAction((byte)res.getInt(res.getColumnIndex(DBConstants.RULESET_COLUMN_APP_ACITON_2_ID))));
            tmp.setRule1(rsm.getRule((byte)res.getInt(res.getColumnIndex(DBConstants.RULESET_COLUMN_APP_RULE_1_ID))));
            tmp.setRule2(rsm.getRule((byte)res.getInt(res.getColumnIndex(DBConstants.RULESET_COLUMN_APP_RULE_2_ID))));
            tmp.setDev1(dsm.getDevice(res.getString(res.getColumnIndex(DBConstants.RULESET_COLUMN_DEV_1_MAC))));
            tmp.setDev2(dsm.getDevice(res.getString(res.getColumnIndex(DBConstants.RULESET_COLUMN_DEV_2_MAC))));
            tmp.setName(res.getString(res.getColumnIndex(DBConstants.RULESET_COLUMN_NAME)));
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
        contentValues.put(DBConstants.RULESET_COLUMN_APP_ACITON_2_ID, ruleset.getAction2().getAppActionID());
        contentValues.put(DBConstants.RULESET_COLUMN_APP_RULE_1_ID, ruleset.getRule1().getAppRuleID());
        contentValues.put(DBConstants.RULESET_COLUMN_APP_RULE_2_ID, ruleset.getRule2().getAppRuleID());
        contentValues.put(DBConstants.RULESET_COLUMN_DEV_1_MAC, ruleset.getDev1().getMacAddress());
        contentValues.put(DBConstants.RULESET_COLUMN_DEV_2_MAC, ruleset.getDev2().getMacAddress());
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
        asm.deleteAction(rso.getAction2().getAppActionID());
        rsm.deleteRule(rso.getRule1().getAppRuleID());
        rsm.deleteRule(rso.getRule2().getAppRuleID());

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
            tmp.setAction2(asm.getAction((byte)res.getInt(res.getColumnIndex(DBConstants.RULESET_COLUMN_APP_ACITON_2_ID))));
            tmp.setRule1(rsm.getRule((byte)res.getInt(res.getColumnIndex(DBConstants.RULESET_COLUMN_APP_RULE_1_ID))));
            tmp.setRule2(rsm.getRule((byte)res.getInt(res.getColumnIndex(DBConstants.RULESET_COLUMN_APP_RULE_2_ID))));
            tmp.setDev1(dsm.getDevice(res.getString(res.getColumnIndex(DBConstants.RULESET_COLUMN_DEV_1_MAC))));
            tmp.setDev2(dsm.getDevice(res.getString(res.getColumnIndex(DBConstants.RULESET_COLUMN_DEV_2_MAC))));
            tmp.setName(res.getString(res.getColumnIndex(DBConstants.RULESET_COLUMN_NAME)));
            array_list.add(tmp);
            res.moveToNext();
        }
        return array_list;
    }
}
