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

import de.th_nuernberg.bluehome.BLEManagement.BLEBufferElement;
import de.th_nuernberg.bluehome.BLEManagement.ErrorObject;
import de.th_nuernberg.bluehome.BlueHomeDatabase.BlueHomeDeviceStorageManager;
import de.th_nuernberg.bluehome.RuleProcessObjects.ActionObject;
import de.th_nuernberg.bluehome.RuleProcessObjects.RPC;
import de.th_nuernberg.bluehome.RuleProcessObjects.RuleObject;

//TODO: implement Callback

/**
 * BLEConnectionManager manages Bluetooth Communication between BlueHome devices and App. Is used as an abstraction Layer for communication.
 *
 * @author Philipp Herrmann
 */

public class BLEconnectionManager {

    //private BluetoothGatt mGatt;
    private BluetoothAdapter mBluetoothAdapter;
    private Context mContext;
    private BlueHomeDeviceStorageManager storageManager;
    private ArrayList<ErrorObject> errors = new ArrayList<>();
    private ArrayList<BluetoothGatt> connected = new ArrayList<>();
    private int counter = 0;

    private final static String DEBUG_TAG = new String("BLEMAN");

    public final static UUID UUID_CMD_SERV =            UUID.fromString("02366E80-CF3A-11E1-9AB4-0002A5D5C51A");
    public final static UUID UUID_CMD_CMD_CHAR =        UUID.fromString("02366E80-CF3A-11E1-9AB4-0002A5D5C51B");

    public final static UUID UUID_INFO_SERV =           UUID.fromString("02366E80-CF3A-11E1-9AB4-0002A5D5C52A");
    public final static UUID UUID_INFO_ERROR_CHAR =     UUID.fromString("02366E80-CF3A-11E1-9AB4-0002A5D5C52B");
    public final static UUID UUID_INFO_HWV_CHAR =       UUID.fromString("02366E80-CF3A-11E1-9AB4-0002A5D5C52C");
    public final static UUID UUID_INFO_SWV_CHAR =       UUID.fromString("02366E80-CF3A-11E1-9AB4-0002A5D5C52D");
    public final static UUID UUID_INFO_NODE_CHAR =      UUID.fromString("02366E80-CF3A-11E1-9AB4-0002A5D5C52E");

    public final static UUID UUID_DIRECT_SERV =         UUID.fromString("02366E80-CF3A-11E1-9AB4-0002A5D5C53A");
    public final static UUID UUID_DIRECT_PARAM =        UUID.fromString("02366E80-CF3A-11E1-9AB4-0002A5D5C53B");
    public final static UUID UUID_DIRECT_PARAMCOMP =    UUID.fromString("02366E80-CF3A-11E1-9AB4-0002A5D5C53C");
    public final static UUID UUID_DIRECT_OPTIONS =      UUID.fromString("02366E80-CF3A-11E1-9AB4-0002A5D5C53D");

    private UUID tmp_write_to;
    private UUID tmp_write_to_service;

    public final static int BLE_FALSE = 1;
    public final static int BLE_OK = 0;
    public final static int BLE_FAIL = -1;
    public final static int BLE_BUSY = -2;

    private boolean FLAG_READ_ERROR = false;
    private boolean FLAG_WRITE_CMD = false;
    private boolean FLAG_WRITE_DIRECT = false;

    private boolean FLAG_MANAGER_BUSY = false;

    private int FLAG_WRITE_OPT_COMPLETE = BLE_FALSE;
    private int FLAG_WRITE_PARAM_COMPLETE = BLE_FALSE;
    private int FLAG_WRITE_PARAMCOMP_COMPLETE = BLE_FALSE;

    private byte[] tmpOptCmd;
    private byte[] tmpParam;
    private byte[] tmpParamComp;

    ArrayList<BLEBufferElement> buffer = new ArrayList<>();


    BLEconnectionManager(Context mContext){
        this.mContext = mContext;
        final BluetoothManager bluetoothManager = (BluetoothManager) this.mContext.getSystemService(this.mContext.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        storageManager = new BlueHomeDeviceStorageManager(mContext);
    }

    /**
     * Opens an Connection to a BlueHomeDevice. Used in other methods in this manager.
     *
     * @param device BlueHomeDevice to connect to
     * @return BLE_OK in case of success or BLE_FAIL when the manager already handles an active connection.
     */
    private int openConnection(BlueHomeDevice device) {
        BluetoothDevice dev;
        if(connected.size() == 0) {
            dev = mBluetoothAdapter.getRemoteDevice(device.getMacAddress());
            dev.connectGatt(mContext, false, gattCallback, BluetoothDevice.TRANSPORT_LE);
            Log.i(DEBUG_TAG, "Connecting to " + dev.getAddress());
            return BLE_OK;
        } else {
            Log.i(DEBUG_TAG, "Can't Connect!");
            return BLE_FAIL;
        }
    }

    /**
     * closes given gatt connection
     *
     * @param gatt connection handle to close
     */
    private void closeConnection(BluetoothGatt gatt) {
        if(gatt != null)
            gatt.disconnect();
    }

    /**
     * Starts error readout for all known devices given by {@link BlueHomeDeviceStorageManager}. Scan results are indicated by 'ERROR_ACTION' Broadcast.
     *
     * @return BLE_OK in case of success
     */
    public int readErrors(){
        if(FLAG_MANAGER_BUSY)
            return BLE_BUSY;
        ArrayList<BlueHomeDevice> devs = storageManager.getAllDevices();
        FLAG_MANAGER_BUSY = true;
        if(devs.size() > 0)
        {
            FLAG_READ_ERROR = true;
            for(int i = 0; i < devs.size(); i++) {
                Log.i(DEBUG_TAG, "Open dev "+i);
                if(openConnection(devs.get(i)) == BLE_FAIL)
                    return BLE_FAIL;
            }
        }

        return BLE_OK;
    }

    /**
     * searches for Service UUID belonging to given Characteristic UUID
     *
     * @param charUuid
     * @return Service UUID for charUuid
     */
    private UUID getServUUID(UUID charUuid){
        if(charUuid.toString().substring(charUuid.toString().length()-2, charUuid.toString().length()-1).equals("1"))
            return UUID_CMD_SERV;
        if(charUuid.toString().substring(charUuid.toString().length()-2, charUuid.toString().length()-1).equals("2"))
            return UUID_INFO_SERV;
        if(charUuid.toString().substring(charUuid.toString().length()-2, charUuid.toString().length()-1).equals("3"))
            return UUID_DIRECT_SERV;

        return null;
    }

    /**
     * Starts asyncron writing a value to given characteristic on given {@link BlueHomeDevice}
     *
     * @param uuid UUID to write to
     * @param values values to write. Up to 20 bytes
     * @param dev BlueHome Device to write to
     */
    public int writeValue(UUID uuid, byte[] values, BlueHomeDevice dev){
        if(FLAG_MANAGER_BUSY)
            return BLE_BUSY;
        tmp_write_to = uuid;
        FLAG_WRITE_CMD = true;
        tmpOptCmd = values;
        FLAG_MANAGER_BUSY = true;
        tmp_write_to_service = getServUUID(uuid);
        if(tmp_write_to_service == null)
            return BLE_FAIL;
        Log.i(DEBUG_TAG, "Open Connection");
        return openConnection(dev);
    }

    public ArrayList<ErrorObject> getErrors(){
        return errors;
    }

    public int writeRule(BlueHomeDevice dev, RuleObject rule){
        if(FLAG_MANAGER_BUSY)
            return BLE_BUSY;
        FLAG_MANAGER_BUSY = true;
        FLAG_WRITE_DIRECT = true;
        tmpParam = rule.getToComp().getParams();
        tmpParamComp = rule.getParamComp();
        tmpOptCmd = new byte[20];
        tmpOptCmd[0] = RPC.SAM_PROG;
        tmpOptCmd[1] = RPC.PROG_ACT_ID_WRITE_RULE;
        tmpOptCmd[2] = rule.getActionMemID();
        tmpOptCmd[3] = rule.getRuleMemID();
        tmpOptCmd[4] = rule.getToComp().getSourceSAM();
        tmpOptCmd[5] = rule.getToComp().getSourceID();
        tmpOptCmd[6] = rule.getToComp().getParamNum();
        return openConnection(dev);
    }

    public int writeAction(BlueHomeDevice dev, ActionObject act){
        if(FLAG_MANAGER_BUSY)
            return BLE_BUSY;
        FLAG_MANAGER_BUSY = true;
        FLAG_WRITE_DIRECT = true;
        tmpParam = act.getParam();
        tmpParamComp = new byte[1];
        tmpParamComp[0] = 0;
        tmpOptCmd = new byte[20];
        tmpOptCmd[0] = RPC.SAM_PROG;
        tmpOptCmd[1] = RPC.PROG_ACT_ID_WRITE_ACTION;
        tmpOptCmd[2] = act.getActionMemID();
        tmpOptCmd[3] = act.getActionSAM();
        tmpOptCmd[4] = act.getActionID();
        tmpOptCmd[5] = act.getMaskPart(0);
        tmpOptCmd[6] = act.getMaskPart(1);
        tmpOptCmd[7] = act.getMaskPart(2);
        tmpOptCmd[8] = act.getMaskPart(3);
        tmpOptCmd[9] = act.getParamNum();
        return openConnection(dev);
    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            ErrorObject tmp;
            BlueHomeDevice tmpDev;

            Log.i(DEBUG_TAG, "Connection State Changed: State: " + status);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:  //Board Connected, Start app
                    Log.i(DEBUG_TAG, "STATE_CONNECTED");
                    connected.add(gatt);
                    gatt.discoverServices();
                    Log.i(DEBUG_TAG, "Start scan");

                    break;
                case BluetoothProfile.STATE_DISCONNECTED:  //Connection Lost
                    Log.e(DEBUG_TAG, "STATE_DISCONNECTED");
                    if(status == 133 && counter < 2) {
                        try {
                            //set time in mili
                            Thread.sleep(60);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        counter++;
                        gatt.connect();
                        Log.i(DEBUG_TAG, "retrying");
                        break;
                    } else {
                        counter = 0;
                    }
                    if(connected.contains(gatt)){
                        connected.remove(gatt);
                        FLAG_MANAGER_BUSY = false;
                    } else {
                        tmpDev = storageManager.getDevice(gatt.getDevice().getAddress());
                        if(tmpDev != null) {
                            tmp = new ErrorObject(ErrorObject.ERROR_NOT_AVAILABLE, tmpDev);
                            errors.add(tmp);
                            Intent in = new Intent("ERROR_ACTION");
                            mContext.sendBroadcast(in);
                        }
                        connected.remove(gatt);

                        FLAG_MANAGER_BUSY = false;
                    }

                    break;
                default:
                    Log.e(DEBUG_TAG, "STATE_OTHER");
            }

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            List<BluetoothGattService> services = gatt.getServices();
            Log.i(DEBUG_TAG, "Services Discovered " + services.toString());
            if(FLAG_READ_ERROR) {
                for (int i = 0; i < services.size(); i++) {
                    for (int j = 0; j < services.get(i).getCharacteristics().size(); j++) {
                        if (services.get(i).getCharacteristics().get(j).getUuid().equals(UUID_INFO_ERROR_CHAR)) {
                            gatt.readCharacteristic(services.get(i).getCharacteristics().get(j));
                            Log.i(DEBUG_TAG, "UUID " + services.get(i).getCharacteristics().get(j).getUuid());
                        }
                    }
                }
            }
            if(FLAG_WRITE_CMD){
                Log.i(DEBUG_TAG, "starting write...");
                if(tmpOptCmd.length > 0) {
                    gatt.getService(tmp_write_to_service).getCharacteristic(tmp_write_to).setValue(tmpOptCmd);
                    gatt.getService(tmp_write_to_service).getCharacteristic(tmp_write_to).setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
                    boolean state = gatt.writeCharacteristic(gatt.getService(tmp_write_to_service).getCharacteristic(tmp_write_to));
                    Log.i(DEBUG_TAG, "write state "+state);
                }
            }

            if(FLAG_WRITE_DIRECT){
                Log.i(DEBUG_TAG, "starting direct command write");
                FLAG_WRITE_OPT_COMPLETE = BLE_FALSE;
                FLAG_WRITE_PARAM_COMPLETE = BLE_FALSE;
                FLAG_WRITE_PARAMCOMP_COMPLETE = BLE_FALSE;
                gatt.getService(UUID_DIRECT_SERV).getCharacteristic(UUID_DIRECT_PARAMCOMP).setValue(tmpParamComp);
                gatt.getService(UUID_DIRECT_SERV).getCharacteristic(UUID_DIRECT_PARAM).setValue(tmpParam);
                gatt.getService(UUID_DIRECT_SERV).getCharacteristic(UUID_DIRECT_OPTIONS).setValue(tmpOptCmd);
                boolean state1 = gatt.writeCharacteristic(gatt.getService(UUID_DIRECT_SERV).getCharacteristic(UUID_DIRECT_PARAMCOMP));
                boolean state2 = gatt.writeCharacteristic(gatt.getService(UUID_DIRECT_SERV).getCharacteristic(UUID_DIRECT_PARAM));
                boolean state3 = gatt.writeCharacteristic(gatt.getService(UUID_DIRECT_SERV).getCharacteristic(UUID_DIRECT_OPTIONS));
            }
            Log.i(DEBUG_TAG, ""+services.size());
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            //super.onCharacteristicRead(gatt, characteristic, status);
            ErrorObject tmp;
            BlueHomeDevice tmpDev;
            Log.i(DEBUG_TAG, characteristic.toString());
            Log.i(DEBUG_TAG, characteristic.getUuid().toString());
            Log.i(DEBUG_TAG, UUID_INFO_ERROR_CHAR.toString());
            Log.i(DEBUG_TAG, ""+characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8,0));
            Log.i(DEBUG_TAG, gatt.getDevice().getAddress().toString());
            if(characteristic.getUuid().equals(UUID_INFO_ERROR_CHAR)) {
                Log.i(DEBUG_TAG, "read error Characteristik");
                if(characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8,0) != ErrorObject.ERROR_NO_ERROR){
                    tmpDev = storageManager.getDevice(gatt.getDevice().getAddress());
                    if(tmpDev != null) {
                        tmp = new ErrorObject(characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8,0), tmpDev);
                        errors.add(tmp);
                        Intent in = new Intent("ERROR_ACTION");
                        mContext.sendBroadcast(in);
                        Log.i(DEBUG_TAG, "Error read: " + tmp.getErrorID());
                    }
                }
                closeConnection(gatt);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if(FLAG_WRITE_DIRECT){
                if(characteristic.getUuid().equals(UUID_DIRECT_OPTIONS)) {
                    if (status == BluetoothGatt.GATT_SUCCESS)
                        FLAG_WRITE_OPT_COMPLETE = BLE_OK;
                    else
                        FLAG_WRITE_OPT_COMPLETE = BLE_FAIL;
                }

                if(characteristic.getUuid().equals(UUID_DIRECT_PARAM)) {
                    if (status == BluetoothGatt.GATT_SUCCESS)
                        FLAG_WRITE_PARAM_COMPLETE = BLE_OK;
                    else
                        FLAG_WRITE_PARAM_COMPLETE = BLE_FAIL;
                }

                if(characteristic.getUuid().equals(UUID_DIRECT_PARAMCOMP)) {
                    if (status == BluetoothGatt.GATT_SUCCESS)
                        FLAG_WRITE_PARAMCOMP_COMPLETE = BLE_OK;
                    else
                        FLAG_WRITE_PARAMCOMP_COMPLETE = BLE_FAIL;
                }

                if(FLAG_WRITE_OPT_COMPLETE == BLE_OK && FLAG_WRITE_PARAMCOMP_COMPLETE == BLE_OK && FLAG_WRITE_PARAM_COMPLETE == BLE_OK){
                    Log.i(DEBUG_TAG, "direct write complete, disconnect...");
                    FLAG_WRITE_DIRECT = false;
                    closeConnection(gatt);
                }

                if(FLAG_WRITE_OPT_COMPLETE == BLE_FAIL || FLAG_WRITE_PARAMCOMP_COMPLETE == BLE_FAIL || FLAG_WRITE_PARAM_COMPLETE == BLE_FAIL) {
                    Log.i(DEBUG_TAG, "direct write failed");
                    FLAG_WRITE_DIRECT = false;
                    closeConnection(gatt);
                }
            }
            if(FLAG_WRITE_CMD){
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    Log.i(DEBUG_TAG, "write complete, disconnect...");
                    closeConnection(gatt);
                } else {
                    Log.e(DEBUG_TAG, "write failed, code: " + status);
                    closeConnection(gatt);
                }
                FLAG_WRITE_CMD = false;
            }

        }
    };
}
