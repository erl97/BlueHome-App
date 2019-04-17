package de.th_nuernberg.bluehome.BlueHomeDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ActionStorageManager extends SQLiteOpenHelper {

    public ActionStorageManager(Context context) {
        super(context, DBConstants.DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
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

    }
}
