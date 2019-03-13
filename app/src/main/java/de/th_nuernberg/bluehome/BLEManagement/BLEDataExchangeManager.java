package de.th_nuernberg.bluehome.BLEManagement;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.util.Log;



public class BLEDataExchangeManager extends Application {

    private String DEBUG_TAG = "BLEExMan";

    public BLEDataExchangeManager() {
    }

    public void addToBuffer(BLEBufferElement toBuffer, Context context) {
        Log.i(DEBUG_TAG, "building Intent");
        Intent intent = new Intent(context, BLEService.class);
        if (toBuffer.getOperation().equals(BLEBufferElement.READ_DEVICE)) {
            Log.i(DEBUG_TAG, BLEBufferElement.READ_DEVICE);
            intent.setAction(BLEBufferElement.READ_DEVICE);
            intent.putExtra(BLEBufferElement.TAG_DEV_MAC, toBuffer.getDev().getMacAddress());
            intent.putExtra(BLEBufferElement.TAG_CHAR_UUID, toBuffer.getCharUuid());
            intent.putExtra(BLEBufferElement.TAG_SERV_UUID, toBuffer.getServUuid());
            intent.putExtra(BLEBufferElement.TAG_BROAD_READ, toBuffer.getBroadcastOnRead());
            intent.putExtra(BLEBufferElement.TAG_BROAD_ERROR, toBuffer.getBroadcastOnConnectionError());
        }
        if (toBuffer.getOperation().equals(BLEBufferElement.WRITE_DEVICE)) {
            intent.setAction(BLEBufferElement.WRITE_DEVICE);
            intent.putExtra(BLEBufferElement.TAG_DEV_MAC, toBuffer.getDev().getMacAddress());
            intent.putExtra(BLEBufferElement.TAG_CHAR_UUID, toBuffer.getCharUuid());
            intent.putExtra(BLEBufferElement.TAG_SERV_UUID, toBuffer.getServUuid());
            intent.putExtra(BLEBufferElement.TAG_DATA, toBuffer.getData());
            intent.putExtra(BLEBufferElement.TAG_BROAD_ERROR, toBuffer.getBroadcastOnConnectionError());

        }
        Log.i(DEBUG_TAG, "issueing start");
        context.startService(intent);
        Log.i(DEBUG_TAG, "start issued");

        /*
        buffer.add(toBuffer);
        kickOffAction();
        */
    }
}