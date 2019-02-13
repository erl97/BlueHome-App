package de.th_nuernberg.bluehome;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//TODO: implement Callback

public class BLEconnectionManager {

    private BluetoothGatt mGatt;
    private BluetoothAdapter mBluetoothAdapter;
    Context mContext;

    public final static UUID UUID_SERV = UUID.fromString("1BC5D5A5-0200-B49A-E111-3ACF806E3602");
    public final static UUID UUID_CHAR = UUID.fromString("1BC5D5A5-0200-FC8F-E111-4ACFA0783EE2");

    BLEconnectionManager(Context mContext){
        this.mContext = mContext;
        final BluetoothManager bluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
    }

    private void openConnection(BluetoothDevice device) {
        BluetoothDevice dev;
        if(mGatt == null) {
            dev = mBluetoothAdapter.getRemoteDevice(device.getAddress());
            mGatt = dev.connectGatt(mContext, false, gattCallback);
        }
    }

    ArrayList<ErrorObject> readErrors(BluetoothDevice device){

    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.i("onConnectionStateChange", "Status: " + status);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:  //Board Connected, Start app
                    Log.i("gattCallback", "STATE_CONNECTED");
                    gatt.discoverServices();


                    break;
                case BluetoothProfile.STATE_DISCONNECTED:  //Connection Lost
                    Log.e("gattCallback", "STATE_DISCONNECTED");

                    break;
                default:
                    Log.e("gattCallback", "STATE_OTHER");
            }

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            List<BluetoothGattService> services = gatt.getServices();
            Log.i("onServicesDiscovered", services.toString());
            mGatt.readCharacteristic(services.get(2).getCharacteristics().get(0));
            Log.i("onServicesDiscovered", ""+services.size());
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.i("onCharacteristicRead", characteristic.toString());
            Log.i("onCharacteristicRead", characteristic.getUuid().toString());
            Log.i("onCharacteristicRead", UUID_CHAR.toString());
            Log.i("onCharacteristicRead", characteristic.getStringValue(0));
            mGatt.disconnect();
        }

    };
}
