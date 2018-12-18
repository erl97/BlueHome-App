package de.th_nuernberg.bluehome.BlueHomeDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import de.th_nuernberg.bluehome.BlueHomeDevice;

public class BlueHomeDeviceStorageManager extends SQLiteOpenHelper {


    public BlueHomeDeviceStorageManager(Context context) {
        super(context, DBConstants.DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
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
    }

    /**
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.DEVICES_TABLE_NAME);
        onCreate(db);
    }

    /**
     * This Method inserts a new Device into the Database
     *
     * @param dev BlueHomeDevice to insert into the Database
     * @return true on success
     */
    public boolean insertDevice (BlueHomeDevice dev) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.DEVICES_COLUMN_REAL_NAME, dev.getDeviceName());
        contentValues.put(DBConstants.DEVICES_COLUMN_SHOWN_NAME, dev.getShownName());
        contentValues.put(DBConstants.DEVICES_COLUMN_MAC, dev.getMacAdress());
        contentValues.put(DBConstants.DEVICES_COLUMN_IMG_ID, dev.getImgID());
        contentValues.put(DBConstants.DEVICES_COLUMN_NODE_TYPE, dev.getNodeType());
        db.insert(DBConstants.DEVICES_TABLE_NAME, null, contentValues);
        return true;
    }

    /**
     * Gets Data from the Database at specified MAC Address
     * @param MAC MAC to Read data from
     * @return Cursor on selected data
     */
    public Cursor getData(String MAC) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + DBConstants.DEVICES_TABLE_NAME + " where " + DBConstants.DEVICES_COLUMN_MAC + "=" + MAC, null );
        return res;
    }

    /**
     * Reads Number of Rows in Device Table
     * @return Number of Rows
     */
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, DBConstants.DEVICES_TABLE_NAME);
        return numRows;
    }

    /**
     * Overwrites/Updates Device
     * @param dev Device to update, orientation on MAC Address
     * @return true on success
     */
    public boolean updateDevice (BlueHomeDevice dev) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.DEVICES_COLUMN_REAL_NAME, dev.getDeviceName());
        contentValues.put(DBConstants.DEVICES_COLUMN_SHOWN_NAME, dev.getShownName());
        contentValues.put(DBConstants.DEVICES_COLUMN_MAC, dev.getMacAdress());
        contentValues.put(DBConstants.DEVICES_COLUMN_IMG_ID, dev.getImgID());
        contentValues.put(DBConstants.DEVICES_COLUMN_NODE_TYPE, dev.getNodeType());
        db.update(DBConstants.DEVICES_TABLE_NAME, contentValues, DBConstants.DEVICES_COLUMN_MAC + " = ? ", new String[] { dev.getMacAdress() } );
        return true;
    }

    /**
     * Deletes Device at MAC
     * @param MAC MAC to Delete
     * @return delete return value
     */
    public Integer deleteDevice (String MAC) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(DBConstants.DEVICES_TABLE_NAME,
                DBConstants.DEVICES_COLUMN_MAC + " = ? ",
                new String[] { MAC });
    }

    /**
     *  Reads all Devices in Memory and Returns them in an ArrayList
     * @return Arraylist<BlueHomeDevice>
     */
    public ArrayList<BlueHomeDevice> getAllDevices() {
        ArrayList<BlueHomeDevice> array_list = new ArrayList<BlueHomeDevice>();
        BlueHomeDevice tmp;
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + DBConstants.DEVICES_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            tmp = new BlueHomeDevice(res.getString(res.getColumnIndex(DBConstants.DEVICES_COLUMN_MAC)), res.getString(res.getColumnIndex(DBConstants.DEVICES_COLUMN_REAL_NAME)));
            tmp.setShownName(res.getString(res.getColumnIndex(DBConstants.DEVICES_COLUMN_SHOWN_NAME)));
            tmp.setImgID(res.getInt(res.getColumnIndex(DBConstants.DEVICES_COLUMN_IMG_ID)));
            tmp.setNodeType(res.getInt(res.getColumnIndex(DBConstants.DEVICES_COLUMN_NODE_TYPE)));
            array_list.add(tmp);
            res.moveToNext();
        }
        return array_list;
    }
}

