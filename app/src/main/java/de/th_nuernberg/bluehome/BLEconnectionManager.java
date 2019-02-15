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
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.th_nuernberg.bluehome.BlueHomeDatabase.BlueHomeDeviceStorageManager;

//TODO: implement Callback

public class BLEconnectionManager {

    private BluetoothGatt mGatt;
    private BluetoothAdapter mBluetoothAdapter;
    private Context mContext;
    private BlueHomeDeviceStorageManager storageManager;
    private ArrayList<ErrorObject> errors = new ArrayList<>();
    private ArrayList<BluetoothGatt> connected = new ArrayList<>();

    public final static UUID UUID_CMD_SERV =        UUID.fromString("02366E80-CF3A-11E1-9AB4-0002A5D5C51A");
    public final static UUID UUID_CMD_CMD_CHAR =    UUID.fromString("02366E80-CF3A-11E1-9AB4-0002A5D5C51B");
    public final static UUID UUID_CMD_POLL_CHAR =   UUID.fromString("02366E80-CF3A-11E1-9AB4-0002A5D5C51C");

    public final static UUID UUID_INFO_SERV =       UUID.fromString("02366E80-CF3A-11E1-9AB4-0002A5D5C52A");
    public final static UUID UUID_INFO_ERROR_CHAR = UUID.fromString("02366E80-CF3A-11E1-9AB4-0002A5D5C52B");
    public final static UUID UUID_INFO_HWV_CHAR =   UUID.fromString("02366E80-CF3A-11E1-9AB4-0002A5D5C52C");
    public final static UUID UUID_INFO_SWV_CHAR =   UUID.fromString("02366E80-CF3A-11E1-9AB4-0002A5D5C52D");
    public final static UUID UUID_INFO_NODE_CHAR =  UUID.fromString("02366E80-CF3A-11E1-9AB4-0002A5D5C52E");

    public final int BLE_OK = 0;
    public final int BLE_FAIL = -1;

    private boolean FLAG_READ_ERROR = false;

    BLEconnectionManager(Context mContext){
        this.mContext = mContext;
        final BluetoothManager bluetoothManager = (BluetoothManager) this.mContext.getSystemService(this.mContext.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        storageManager = new BlueHomeDeviceStorageManager(mContext);
    }

    public void openConnection(BlueHomeDevice device) {
        BluetoothDevice dev;
        if(mGatt == null) {
            dev = mBluetoothAdapter.getRemoteDevice(device.getMacAddress());
            mGatt = dev.connectGatt(mContext, false, gattCallback);
            Log.i("OPEN", "Connecting to " + dev.getAddress());
        }
    }

    public void closeConnection() {
        if(mGatt != null)
            mGatt.disconnect();
    }

    public int readErrors(){
        ArrayList<BlueHomeDevice> devs = storageManager.getAllDevices();
        if(devs.size() > 0)
        {
            FLAG_READ_ERROR = true;
            for(int i = 0; i < devs.size(); i++) {
                Log.i("OPEN_DEV", ""+i);
                openConnection(devs.get(i));
            }
        }

        return BLE_OK;
    }

    public ArrayList<ErrorObject> getErrors(){
        return errors;
    }

    public void wirteToDevice(BlueHomeDevice device, ArrayList<Byte> data) {

    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            ErrorObject tmp;
            BlueHomeDevice tmpDev;
            Log.i("onConnectionStateChange", "Status: " + status);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:  //Board Connected, Start app
                    Log.i("gattCallback", "STATE_CONNECTED");
                    connected.add(gatt);
                    gatt.discoverServices();
                    Log.i("gattCallback", "Start scan");

                    break;
                case BluetoothProfile.STATE_DISCONNECTED:  //Connection Lost
                    Log.e("gattCallback", "STATE_DISCONNECTED");
                    if(connected.contains(gatt)){
                        connected.remove(gatt);
                    } else {
                        tmpDev = storageManager.getDevice(gatt.getDevice().getAddress());
                        if(tmpDev != null) {
                            tmp = new ErrorObject(ErrorObject.ERROR_NOT_AVAILABLE, tmpDev);
                            errors.add(tmp);
                            Intent in = new Intent("ERROR_ACTION");
                            mContext.sendBroadcast(in);
                        }
                    }

                    break;
                default:
                    Log.e("gattCallback", "STATE_OTHER");
            }

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            List<BluetoothGattService> services = gatt.getServices();
            Log.i("onServicesDiscovered", services.toString());
            if(FLAG_READ_ERROR) {
                for (int i = 0; i < services.size(); i++) {
                    for (int j = 0; j < services.get(i).getCharacteristics().size(); j++) {
                        if (services.get(i).getCharacteristics().get(j).getUuid().equals(UUID_INFO_ERROR_CHAR)) {
                            mGatt.readCharacteristic(services.get(i).getCharacteristics().get(j));
                            Log.i("UUID", "" + services.get(i).getCharacteristics().get(j).getUuid());
                        }
                    }
                }
            }
            Log.i("onServicesDiscovered", ""+services.size());
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            ErrorObject tmp;
            BlueHomeDevice tmpDev;
            Log.i("onCharacteristicRead", characteristic.toString());
            Log.i("onCharacteristicRead", characteristic.getUuid().toString());
            Log.i("onCharacteristicRead", UUID_INFO_ERROR_CHAR.toString());
            Log.i("onCharacteristicRead", ""+characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8,0));
            Log.i("onCharacteristicRead", gatt.getDevice().getAddress().toString());
            if(characteristic.getUuid().equals(UUID_INFO_ERROR_CHAR)) {
                if(characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8,0) != ErrorObject.ERROR_NO_ERROR){
                    tmpDev = storageManager.getDevice(gatt.getDevice().getAddress());
                    if(tmpDev != null) {
                        tmp = new ErrorObject(characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8,0), tmpDev);
                        errors.add(tmp);
                        Intent in = new Intent("ERROR_ACTION");
                        mContext.sendBroadcast(in);
                    }
                }
                gatt.disconnect();
            }
        }

    };
}
