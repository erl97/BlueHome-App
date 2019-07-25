package de.th_nuernberg.bluehome.BlueHomeDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.nio.charset.Charset;
import java.util.ArrayList;

import de.th_nuernberg.bluehome.BlueHomeDevice;
import de.th_nuernberg.bluehome.RuleProcessObjects.RuleObject;
import de.th_nuernberg.bluehome.RuleProcessObjects.SourceObject;
import de.th_nuernberg.bluehome.SplashScreen;

public class RuleStorageManager extends DatabaseInitiator {

    public RuleStorageManager(Context context) {
        super(context);
    }


    public void insertRule(RuleObject rule) {

            //Log.i("StorageManager", "" + new String(rule.getParamComp(), Charset.forName("UTF-8")).getBytes(Charset.forName("UTF-8"))[0] + " " + rule.getParamComp()[0]);


            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBConstants.RULES_COLUMN_ACTION_MEM_ID, rule.getActionMemID());
            contentValues.put(DBConstants.RULES_COLUMN_APP_RULE_ID, rule.getAppRuleID());
            contentValues.put(DBConstants.RULES_COLUMN_RULE_MEM_ID, rule.getRuleMemID());
            contentValues.put(DBConstants.RULES_COLUMN_SOURCE_ID, rule.getToComp().getSourceID());
            contentValues.put(DBConstants.RULES_COLUMN_SOURCE_SAM, rule.getToComp().getSourceSAM());

            Log.i("RuleStorageManager", "source sam: " + rule.getToComp().getSourceSAM());

            contentValues.put(DBConstants.RULES_COLUMN_PARAM_COMP, rule.getParamComp());
            contentValues.put(DBConstants.RULES_COLUMN_SOURCE_PARAM, rule.getToComp().getParams());

            for(int i = 0; i < rule.getToComp().getParams().length; i++)
                Log.i("RuleStorageManager", "Writing param " + i + ": " + rule.getToComp().getParams()[i]);
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
            tmpSource.setParams((res.getBlob(res.getColumnIndex(DBConstants.RULES_COLUMN_SOURCE_PARAM))));
            tmp.setToComp(tmpSource);
            tmp.setParamComp((res.getBlob(res.getColumnIndex(DBConstants.RULES_COLUMN_PARAM_COMP))));
            Log.i("RuleStorageManager", "Rule Param for Rule ID " + tmp.getAppRuleID() + ":");
            for(int i = 0; i < tmp.getToComp().getParams().length; i++)
                Log.i("RuleStorageManager", "data byte " + i + ": " + tmp.getToComp().getParams()[i]);
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

        contentValues.put(DBConstants.RULES_COLUMN_PARAM_COMP, rule.getParamComp());
        contentValues.put(DBConstants.RULES_COLUMN_SOURCE_PARAM, rule.getToComp().getParams());

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
            tmpSource.setParams((res.getBlob(res.getColumnIndex(DBConstants.RULES_COLUMN_SOURCE_PARAM))));
            tmp.setToComp(tmpSource);
            tmp.setParamComp((res.getBlob(res.getColumnIndex(DBConstants.RULES_COLUMN_PARAM_COMP))));
            array_list.add(tmp);
            res.moveToNext();
        }
        return array_list;
    }


    public byte getNextFreeAppId(byte startValue){
        ArrayList<RuleObject> rules = this.getAllRules();
        boolean foundFree;
        for(byte i = startValue; i < 32; i++)
        {
            foundFree = true;
            for (RuleObject s : rules) {
                if (s.getAppRuleID() == i) {
                    foundFree = false;
                    break;
                }
            }
            if(foundFree)
            {
                Log.i("RuleStorageManager", "found free App id: " + i);
                return i;

            }

        }
        return 0;
    }
}
