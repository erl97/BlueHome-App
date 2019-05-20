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
import de.th_nuernberg.bluehome.RuleProcessObjects.SourceObject;
import de.th_nuernberg.bluehome.SplashScreen;

public class RuleStorageManager extends SQLiteOpenHelper {

    public RuleStorageManager(Context context) {
        super(context, DBConstants.DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " +
                        DBConstants.RULES_TABLE_NAME +
                        " (" +
                        DBConstants.RULES_COLUMN_APP_RULE_ID +
                        " integer primary key," +
                        DBConstants.RULES_COLUMN_ACTION_MEM_ID +
                        " integer," +
                        DBConstants.RULES_COLUMN_RULE_MEM_ID +
                        " integer," +
                        DBConstants.RULES_COLUMN_PARAM_COMP +
                        " text," +
                        DBConstants.RULES_COLUMN_SOURCE_ID +
                        " integer," +
                        DBConstants.RULES_COLUMN_SOURCE_SAM +
                        " integer," +
                        DBConstants.RULES_COLUMN_SOURCE_PARAM +
                        " text)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertRule(RuleObject rule) {

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBConstants.RULES_COLUMN_ACTION_MEM_ID, rule.getActionMemID());
            contentValues.put(DBConstants.RULES_COLUMN_APP_RULE_ID, rule.getAppRuleID());
            contentValues.put(DBConstants.RULES_COLUMN_RULE_MEM_ID, rule.getRuleMemID());
            contentValues.put(DBConstants.RULES_COLUMN_SOURCE_ID, rule.getToComp().getSourceID());
            contentValues.put(DBConstants.RULES_COLUMN_SOURCE_SAM, rule.getToComp().getSourceSAM());

            contentValues.put(DBConstants.RULES_COLUMN_PARAM_COMP, byteArrayToString(rule.getParamComp()));
            contentValues.put(DBConstants.RULES_COLUMN_SOURCE_PARAM, byteArrayToString(rule.getToComp().getParams()));
            db.insert(DBConstants.RULES_TABLE_NAME, null, contentValues);
    }

    private Cursor getData(byte appRuleID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + DBConstants.RULES_TABLE_NAME + " where " + DBConstants.RULES_COLUMN_APP_RULE_ID + "='" + appRuleID + "'", null );
        return res;
    }

    public RuleObject getRule(byte appRuleID) {
        RuleObject tmp;
        SourceObject tmpSource;
        Cursor res = getData(appRuleID);
        if(res.moveToFirst()) {
            tmp = new RuleObject();
            tmpSource = new SourceObject();
            tmp.setActionMemID((byte)res.getInt(res.getColumnIndex(DBConstants.RULES_COLUMN_ACTION_MEM_ID)));
            tmp.setAppRuleID((byte)res.getInt(res.getColumnIndex(DBConstants.RULES_COLUMN_APP_RULE_ID)));
            tmp.setRuleMemID((byte)res.getInt(res.getColumnIndex(DBConstants.RULES_COLUMN_RULE_MEM_ID)));
            tmpSource.setSourceID((byte)res.getInt(res.getColumnIndex(DBConstants.RULES_COLUMN_SOURCE_ID)));
            tmpSource.setSourceSAM((byte)res.getInt(res.getColumnIndex(DBConstants.RULES_COLUMN_SOURCE_SAM)));
            tmpSource.setParams(toByteArray(res.getString(res.getColumnIndex(DBConstants.RULES_COLUMN_SOURCE_PARAM))));
            tmp.setToComp(tmpSource);
            tmp.setParamComp(toByteArray(res.getString(res.getColumnIndex(DBConstants.RULES_COLUMN_PARAM_COMP))));
            return tmp;
        } else {
            Log.e("RuleStorageManager", "didn't move");
            return null;
        }
    }

    private byte[] toByteArray(String data){
        return data.getBytes();
    }

    private String byteArrayToString(byte[] data) {
        return data.toString();
    }

    /**
     * Overwrites/Updates Rule
     * @param rule Rule to update, orientation on App Rule ID
     * @return true on success
     */
    public boolean updateRule (RuleObject rule) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.RULES_COLUMN_ACTION_MEM_ID, rule.getActionMemID());
        contentValues.put(DBConstants.RULES_COLUMN_APP_RULE_ID, rule.getAppRuleID());
        contentValues.put(DBConstants.RULES_COLUMN_RULE_MEM_ID, rule.getRuleMemID());
        contentValues.put(DBConstants.RULES_COLUMN_SOURCE_ID, rule.getToComp().getSourceID());
        contentValues.put(DBConstants.RULES_COLUMN_SOURCE_SAM, rule.getToComp().getSourceSAM());

        contentValues.put(DBConstants.RULES_COLUMN_PARAM_COMP, byteArrayToString(rule.getParamComp()));
        contentValues.put(DBConstants.RULES_COLUMN_SOURCE_PARAM, byteArrayToString(rule.getToComp().getParams()));

        db.update(DBConstants.RULES_TABLE_NAME, contentValues, DBConstants.RULES_COLUMN_APP_RULE_ID + " = ? ", new String[] { "" + rule.getAppRuleID() } );
        return true;
    }

    /**
     * Deletes Device at Rule App ID
     * @param ruleAppID Rule to Delete
     * @return delete return value
     */
    public Integer deleteRule (byte ruleAppID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(DBConstants.RULES_TABLE_NAME,
                DBConstants.RULES_COLUMN_APP_RULE_ID + " = ? ",
                new String[] { ""+ruleAppID });
    }

    /**
     *  Reads all Rules in Memory and Returns them in an ArrayList
     * @return Arraylist<RuleObject>
     */
    public ArrayList<RuleObject> getAllRules() {
        ArrayList<RuleObject> array_list = new ArrayList<RuleObject>();
        RuleObject tmp;
        SourceObject tmpSource;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + DBConstants.RULES_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            tmp = new RuleObject();
            tmpSource = new SourceObject();
            tmp.setActionMemID((byte)res.getInt(res.getColumnIndex(DBConstants.RULES_COLUMN_ACTION_MEM_ID)));
            tmp.setAppRuleID((byte)res.getInt(res.getColumnIndex(DBConstants.RULES_COLUMN_APP_RULE_ID)));
            tmp.setRuleMemID((byte)res.getInt(res.getColumnIndex(DBConstants.RULES_COLUMN_RULE_MEM_ID)));
            tmpSource.setSourceID((byte)res.getInt(res.getColumnIndex(DBConstants.RULES_COLUMN_SOURCE_ID)));
            tmpSource.setSourceSAM((byte)res.getInt(res.getColumnIndex(DBConstants.RULES_COLUMN_SOURCE_SAM)));
            tmpSource.setParams(toByteArray(res.getString(res.getColumnIndex(DBConstants.RULES_COLUMN_SOURCE_PARAM))));
            tmp.setToComp(tmpSource);
            tmp.setParamComp(toByteArray(res.getString(res.getColumnIndex(DBConstants.RULES_COLUMN_PARAM_COMP))));
            array_list.add(tmp);
            res.moveToNext();
        }
        return array_list;
    }
}
