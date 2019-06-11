package de.th_nuernberg.bluehome.BlueHomeDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseInitiator extends SQLiteOpenHelper {
    public DatabaseInitiator(Context context)
    {
        super(context, DBConstants.DATABASE_NAME, null, 1);
        Log.i("Initiator", "created Object");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("Initiator", "creating tables");
        db.execSQL(
                "create table " +
                        DBConstants.RULESET_TABLE_NAME +
                        " (" +
                        DBConstants.RULESET_COLUMN_RULESET_ID +
                        " integer primary key," +
                        DBConstants.RULESET_COLUMN_APP_RULE_1_ID +
                        " integer," +
                        DBConstants.RULESET_COLUMN_APP_RULE_2_ID +
                        " integer," +
                        DBConstants.RULESET_COLUMN_APP_ACTION_1_ID +
                        " integer," +
                        DBConstants.RULESET_COLUMN_APP_ACITON_2_ID +
                        " integer," +
                        DBConstants.RULESET_COLUMN_DEV_1_MAC +
                        " text," +
                        DBConstants.RULESET_COLUMN_DEV_2_MAC +
                        " text," +
                        DBConstants.RULESET_COLUMN_NAME +
                        " text)"
        );

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

        db.execSQL(
                "create table " +
                        DBConstants.DEVICES_TABLE_NAME +
                        " (" +
                        DBConstants.DEVICES_COLUMN_REAL_NAME +
                        " text," +
                        DBConstants.DEVICES_COLUMN_SHOWN_NAME +
                        " text," +
                        DBConstants.DEVICES_COLUMN_MAC +
                        " text primary key," +
                        DBConstants.DEVICES_COLUMN_IMG_ID +
                        " integer," +
                        DBConstants.DEVICES_COLUMN_NODE_TYPE +
                        " integer)"
        );

        db.execSQL(
                "create table " +
                        DBConstants.ACTIONS_TABLE_NAME +
                        " (" +
                        DBConstants.ACTIONS_COLUMN_APP_ACTION_ID +
                        " integer primary key," +
                        DBConstants.ACTIONS_COLUMN_ID +
                        " integer," +
                        DBConstants.ACTIONS_COLUMN_MEM_ID +
                        " integer," +
                        DBConstants.ACTIONS_COLUMN_PARAM_MASK +
                        " integer," +
                        DBConstants.ACTIONS_COLUMN_PARAMS +
                        " text," +
                        DBConstants.ACTIONS_COLUMN_SAM +
                        " integer)"
        );


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.DEVICES_TABLE_NAME);
        onCreate(db);
    }
}
