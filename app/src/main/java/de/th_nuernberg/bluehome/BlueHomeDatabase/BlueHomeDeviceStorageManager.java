package de.th_nuernberg.bluehome.BlueHomeDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import de.th_nuernberg.bluehome.BlueHomeDevice;

/**
 * BlueHomeStorageManager manages the databaseconnection for the BlueHome Device table, which contains all devices known to the System.
 *
 * @author Philipp Herrmann
 */
public class BlueHomeDeviceStorageManager extends DatabaseInitiator {


    public BlueHomeDeviceStorageManager(Context context) {
        super(context);
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
        contentValues.put(DBConstants.DEVICES_COLUMN_MAC, dev.getMacAddress());
        contentValues.put(DBConstants.DEVICES_COLUMN_IMG_ID, dev.getImgID());
        contentValues.put(DBConstants.DEVICES_COLUMN_NODE_TYPE, dev.getNodeType());
        contentValues.put(DBConstants.DEVICES_COLUMN_MAC_ID, getNextFreeMacId());
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
        Cursor res =  db.rawQuery( "select * from " + DBConstants.DEVICES_TABLE_NAME + " where " + DBConstants.DEVICES_COLUMN_MAC + "='" + MAC.trim() + "'", null );
        return res;
    }

    /**
     * Get a specific device from database, identified by Mac Address
     * @param MAC Mac addres to look for
     * @return BlueHomeDevice which is found
     */
    public BlueHomeDevice getDevice(String MAC) {
        BlueHomeDevice tmp;
        Cursor res = getData(MAC);
        if(res.moveToFirst()) {
            tmp = new BlueHomeDevice(res.getString(res.getColumnIndex(DBConstants.DEVICES_COLUMN_MAC)), res.getString(res.getColumnIndex(DBConstants.DEVICES_COLUMN_REAL_NAME)));
            tmp.setShownName(res.getString(res.getColumnIndex(DBConstants.DEVICES_COLUMN_SHOWN_NAME)));
            tmp.setImgID(res.getInt(res.getColumnIndex(DBConstants.DEVICES_COLUMN_IMG_ID)));
            tmp.setNodeType(res.getInt(res.getColumnIndex(DBConstants.DEVICES_COLUMN_NODE_TYPE)));
            tmp.setMacId((byte)res.getInt(res.getColumnIndex(DBConstants.DEVICES_COLUMN_MAC_ID)));
            return tmp;
        } else {
            Log.e("StorageManager", "didn't move");
            return null;
        }
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
        contentValues.put(DBConstants.DEVICES_COLUMN_MAC, dev.getMacAddress());
        contentValues.put(DBConstants.DEVICES_COLUMN_IMG_ID, dev.getImgID());
        contentValues.put(DBConstants.DEVICES_COLUMN_NODE_TYPE, dev.getNodeType());
        contentValues.put(DBConstants.DEVICES_COLUMN_MAC_ID, dev.getMacId());
        db.update(DBConstants.DEVICES_TABLE_NAME, contentValues, DBConstants.DEVICES_COLUMN_MAC + " = ? ", new String[] { dev.getMacAddress() } );
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
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + DBConstants.DEVICES_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            tmp = new BlueHomeDevice(res.getString(res.getColumnIndex(DBConstants.DEVICES_COLUMN_MAC)), res.getString(res.getColumnIndex(DBConstants.DEVICES_COLUMN_REAL_NAME)));
            tmp.setShownName(res.getString(res.getColumnIndex(DBConstants.DEVICES_COLUMN_SHOWN_NAME)));
            tmp.setImgID(res.getInt(res.getColumnIndex(DBConstants.DEVICES_COLUMN_IMG_ID)));
            tmp.setNodeType(res.getInt(res.getColumnIndex(DBConstants.DEVICES_COLUMN_NODE_TYPE)));
            tmp.setMacId((byte)res.getInt(res.getColumnIndex(DBConstants.DEVICES_COLUMN_MAC_ID)));
            array_list.add(tmp);
            res.moveToNext();
        }
        return array_list;
    }

    public byte getNextFreeMacId(){
        ArrayList<BlueHomeDevice> devs = this.getAllDevices();
        boolean foundFree;
        for(byte i = 1; i < 32; i++)
        {
            foundFree = true;
            for (BlueHomeDevice s : devs) {
                if (s.getMacId() == i) {
                    foundFree = false;
                    break;
                }
            }
            if(foundFree) {
                Log.i("BlueHomeDeviceStorageManager", "found " + i);
                return i;
            }
        }
        return 0;
    }

}

