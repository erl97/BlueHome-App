package de.th_nuernberg.bluehome.BlueHomeDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.nio.charset.Charset;
import java.util.ArrayList;

import de.th_nuernberg.bluehome.RuleProcessObjects.ActionObject;
import de.th_nuernberg.bluehome.RuleProcessObjects.RuleObject;
import de.th_nuernberg.bluehome.RuleProcessObjects.SourceObject;

public class ActionStorageManager extends DatabaseInitiator {

    public ActionStorageManager(Context context) {
        super(context);
    }

    public void insertAction(ActionObject action) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.ACTIONS_COLUMN_APP_ACTION_ID, action.getAppActionID());
        contentValues.put(DBConstants.ACTIONS_COLUMN_ID, action.getActionID());
        contentValues.put(DBConstants.ACTIONS_COLUMN_MEM_ID, action.getActionMemID());
        contentValues.put(DBConstants.ACTIONS_COLUMN_PARAM_MASK, action.getParamMask());
        contentValues.put(DBConstants.ACTIONS_COLUMN_PARAMS, action.getParam());
        contentValues.put(DBConstants.ACTIONS_COLUMN_SAM, action.getActionSAM());
        db.insert(DBConstants.ACTIONS_TABLE_NAME, null, contentValues);
    }

    private Cursor getData(byte appActionID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + DBConstants.ACTIONS_TABLE_NAME + " where " + DBConstants.ACTIONS_COLUMN_APP_ACTION_ID + "='" + appActionID + "'", null );
        return res;
    }

    public ActionObject getAction(byte appActionID) {
        ActionObject tmp;
        Cursor res = getData(appActionID);
        if(res.moveToFirst()) {
            tmp = new ActionObject();
            tmp.setAppActionID((byte)res.getInt(res.getColumnIndex(DBConstants.ACTIONS_COLUMN_APP_ACTION_ID)));
            tmp.setActionID((byte)res.getInt(res.getColumnIndex(DBConstants.ACTIONS_COLUMN_ID)));
            tmp.setActionMemID((byte)res.getInt(res.getColumnIndex(DBConstants.ACTIONS_COLUMN_MEM_ID)));
            tmp.setParamMask(res.getInt(res.getColumnIndex(DBConstants.ACTIONS_COLUMN_PARAM_MASK)));
            tmp.setParam((res.getBlob(res.getColumnIndex(DBConstants.ACTIONS_COLUMN_PARAMS))));
            tmp.setActionSAM((byte)res.getInt(res.getColumnIndex(DBConstants.ACTIONS_COLUMN_SAM)));
            return tmp;
        } else {
            Log.e("ActionStorageManager", "didn't move");
            return null;
        }
    }


    /**
     * Overwrites/Updates Action
     * @param action Action to update, orientation on App Action ID
     * @return true on success
     */
    public boolean updateAction (ActionObject action) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.ACTIONS_COLUMN_APP_ACTION_ID, action.getAppActionID());
        contentValues.put(DBConstants.ACTIONS_COLUMN_ID, action.getActionID());
        contentValues.put(DBConstants.ACTIONS_COLUMN_MEM_ID, action.getActionMemID());
        contentValues.put(DBConstants.ACTIONS_COLUMN_PARAM_MASK, action.getParamMask());
        contentValues.put(DBConstants.ACTIONS_COLUMN_PARAMS, action.getParam());
        contentValues.put(DBConstants.ACTIONS_COLUMN_SAM, action.getActionSAM());

        db.update(DBConstants.ACTIONS_TABLE_NAME, contentValues, DBConstants.ACTIONS_COLUMN_APP_ACTION_ID + " = ? ", new String[] { "" + action.getAppActionID() } );
        return true;
    }

    /**
     * Deletes Action at Action App ID
     * @param actionAppID Action to Delete
     * @return delete return value
     */
    public Integer deleteAction (byte actionAppID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(DBConstants.ACTIONS_TABLE_NAME,
                DBConstants.ACTIONS_COLUMN_APP_ACTION_ID + " = ? ",
                new String[] { "" + actionAppID });
    }

    /**
     *  Reads all Actions in Memory and Returns them in an ArrayList
     * @return Arraylist<ActionObject>
     */
    public ArrayList<ActionObject> getAllActions() {
        ArrayList<ActionObject> array_list = new ArrayList<ActionObject>();
        ActionObject tmp;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + DBConstants.ACTIONS_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            tmp = new ActionObject();
            tmp.setAppActionID((byte)res.getInt(res.getColumnIndex(DBConstants.ACTIONS_COLUMN_APP_ACTION_ID)));
            tmp.setActionID((byte)res.getInt(res.getColumnIndex(DBConstants.ACTIONS_COLUMN_ID)));
            tmp.setActionMemID((byte)res.getInt(res.getColumnIndex(DBConstants.ACTIONS_COLUMN_MEM_ID)));
            tmp.setActionSAM((byte)(res.getInt(res.getColumnIndex(DBConstants.ACTIONS_COLUMN_SAM))));
            tmp.setParam((res.getBlob(res.getColumnIndex(DBConstants.ACTIONS_COLUMN_PARAMS))));
            tmp.setParamMask(res.getInt(res.getColumnIndex(DBConstants.ACTIONS_COLUMN_PARAM_MASK)));
            array_list.add(tmp);
            res.moveToNext();
        }
        return array_list;
    }

    public byte getNextFreeAppId(byte startValue){
        ArrayList<ActionObject> rules = this.getAllActions();
        boolean foundFree;
        for(byte i = startValue; i < 32; i++)
        {
            foundFree = true;
            for (ActionObject s : rules) {
                if (s.getAppActionID() == i) {
                    foundFree = false;
                    break;
                }
            }
            if(foundFree)
                return i;
        }
        return 0;
    }
}
