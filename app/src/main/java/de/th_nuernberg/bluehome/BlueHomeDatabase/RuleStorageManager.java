package de.th_nuernberg.bluehome.BlueHomeDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import de.th_nuernberg.bluehome.BlueHomeDevice;
import de.th_nuernberg.bluehome.RuleProcessObjects.RuleObject;

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
                        DBConstants.RULES_COLUMN_SOURCE_ID+
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

    private String byteArrayToString(byte[] data) {
        String tmp = new String();
        for(int i = 0; i < 20; i++) {
            tmp += ("" + data[i]);
        }
        return tmp;
    }
}
