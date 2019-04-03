package de.th_nuernberg.bluehome.BLEManagement;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.th_nuernberg.bluehome.BlueHomeDevice;

public class BLEService extends Service {


    ArrayList<BLEBufferElement> buffer = new ArrayList<>();
    ArrayList<ErrorObject> errorBuffer = new ArrayList<>();

    private BluetoothAdapter mBluetoothAdapter;

    private boolean CONNECTION_STATE = false;
    private boolean RUNNING = false;

    BlueHomeDevice actDev;

    private int retryCounter = 0;

    public final static int BLE_OK = 0;
    public final static int BLE_FAIL = -1;

    private final static int MAX_RETRY = 2;

    public final static String TAG_SOURCE = "SOURCE";
    public final static String TAG_MAC = "MAC";

    public final static String ERROR_FOUND_BC = "Error_Found";
    public final static String READ_RES_NAME = "Read_Res_Name";

    private LocalBroadcastManager broadcaster;

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




    private final static String DEBUG_TAG = "BLEService";

    public BLEService() {

    }

    @Override
    public void onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this);
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        BLEBufferElement toBuffer = null;
        BlueHomeDevice dev = new BlueHomeDevice(intent.getStringExtra(BLEBufferElement.TAG_DEV_MAC), "DEVICE");
        Log.i(DEBUG_TAG,"starting " + intent.getAction());
        if(intent.getAction().equals(BLEBufferElement.WRITE_DEVICE)) {
            toBuffer = new BLEBufferElement(dev, intent.getByteArrayExtra(BLEBufferElement.TAG_DATA), (UUID)intent.getSerializableExtra(BLEBufferElement.TAG_CHAR_UUID), (UUID)intent.getSerializableExtra(BLEBufferElement.TAG_SERV_UUID), intent.getStringExtra(BLEBufferElement.TAG_BROAD_ERROR));
        }
        if(intent.getAction().equals(BLEBufferElement.READ_DEVICE)){
            toBuffer = new BLEBufferElement(dev, (UUID)intent.getSerializableExtra(BLEBufferElement.TAG_CHAR_UUID), (UUID)intent.getSerializableExtra(BLEBufferElement.TAG_SERV_UUID), intent.getStringExtra(BLEBufferElement.TAG_BROAD_READ), intent.getStringExtra(BLEBufferElement.TAG_BROAD_ERROR));
        }
        if(toBuffer == null) {
            Log.e(DEBUG_TAG, "toBuffer Null Pointer");
            stopSelf();
        } else {
            buffer.add(toBuffer);
            Log.i(DEBUG_TAG, "added " + toBuffer.getDev().getMacAddress() + " to Buffer");
        }
        kickOffAction();
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(DEBUG_TAG, "Service onBind");
        return null;
    }

    private void kickOffAction(){
        if(!RUNNING && !buffer.isEmpty()){
            openConnetionTo(buffer.get(0).getDev());
            RUNNING = true;
        }
        if(buffer.isEmpty())
            stopSelf();
    }

    private int openConnetionTo(BlueHomeDevice dev){
        if(actDev == null) {
            actDev = dev;
            BluetoothDevice devTmp;
            devTmp = mBluetoothAdapter.getRemoteDevice(dev.getMacAddress());
            devTmp.connectGatt(getApplicationContext(), false, gattCallback);
            Log.i(DEBUG_TAG, "Connecting to " + dev.getShownName() + "/" + devTmp.getAddress());
            return BLE_OK;
        } else {
            Log.i(DEBUG_TAG, "Cannot open connection to " + dev.getShownName());
            return BLE_FAIL;
        }
    }

    protected void retryConnection(){
        if(retryCounter < MAX_RETRY) {
            retryCounter ++;
            BluetoothDevice devTmp;
            devTmp = mBluetoothAdapter.getRemoteDevice(actDev.getMacAddress());
            devTmp.connectGatt(getApplicationContext(), false, gattCallback);
            Log.i(DEBUG_TAG, "Retry connecting to " + actDev.getShownName() + "/" + devTmp.getAddress());
        } else {
            retryCounter = 0;
            Log.i(DEBUG_TAG, "Retry Counter expired, cancel Connection");
            ErrorObject tmp = new ErrorObject(ErrorObject.ERROR_NOT_AVAILABLE, actDev);
            errorBuffer.add(tmp);
            notifyErrorChanged();
        }
    }

    protected void notifyErrorChanged(){
        Intent in = new Intent(ERROR_FOUND_BC);
        sendBroadcast(in);
    }

    protected void connectionFailed(BluetoothGatt gatt, int source){
        notifyFailed(gatt, source);
        Log.i(DEBUG_TAG, "Connection failed, creating error");
        ErrorObject tmp = new ErrorObject(ErrorObject.ERROR_NOT_AVAILABLE, actDev);
        errorBuffer.add(tmp);
        notifyErrorChanged();
        disconnected();
    }

    protected void disconnected(){
        buffer.remove(0);
        actDev = null;
        Log.i(DEBUG_TAG, "Disconnection Completed");
        runNext();
    }

    private void runNext(){
        if(buffer.isEmpty()){
            RUNNING = false;
            Log.i(DEBUG_TAG,"Nothing to Do, stopping");
            stopSelf();
        } else {
            openConnetionTo(buffer.get(0).getDev());
        }
    }

    private void notifyFailed(BluetoothGatt gatt, int source){
        Intent in = new Intent(buffer.get(0).getBroadcastOnConnectionError());
        in.putExtra(TAG_SOURCE, source);
        in.putExtra(TAG_MAC, gatt.getDevice().getAddress());
        Log.i(DEBUG_TAG, "Sending Broadcast " + buffer.get(0).getBroadcastOnConnectionError());
        broadcaster.sendBroadcast(in);
    }

    protected void notifyRead(byte[] res){
        Intent in = new Intent(buffer.get(0).getBroadcastOnRead());
        in.putExtra(READ_RES_NAME, res);
        broadcaster.sendBroadcast(in);
    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState){

            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:  //Connected
                    Log.i(DEBUG_TAG, "BLE Connected");
                    CONNECTION_STATE = true;
                    gatt.discoverServices();
                    Log.i(DEBUG_TAG, "Start scan");
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:  //Connection Lost
                    Log.i(DEBUG_TAG, "Connection failed/ended!");
                    if(status == 0 || status == 19) //normal disconnect
                        disconnected();
                    else
                        //retryConnection();
                        connectionFailed(gatt, ErrorObject.ERROR_NOT_AVAILABLE);
                    break;
                default:
                    Log.e(DEBUG_TAG, "STATE_OTHER");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status){
            List<BluetoothGattService> services = gatt.getServices();
            Log.i(DEBUG_TAG, "Services Discovered " + services.toString());

            for(int i = 0; i < services.size(); i++)
                Log.i(DEBUG_TAG, "Service found: "+services.get(i).getUuid());

            if(buffer.get(0).getOperation() == BLEBufferElement.READ_DEVICE){
                for (int i = 0; i < services.size(); i++) {
                    for (int j = 0; j < services.get(i).getCharacteristics().size(); j++) {
                        if (services.get(i).getCharacteristics().get(j).getUuid().equals(buffer.get(0).getCharUuid())) {
                            gatt.readCharacteristic(services.get(i).getCharacteristics().get(j));
                            Log.i(DEBUG_TAG, "Reading UUID " + services.get(i).getCharacteristics().get(j).getUuid());
                        }
                    }
                }
            }

            if(buffer.get(0).getOperation().equals(BLEBufferElement.WRITE_DEVICE)){
                Log.i(DEBUG_TAG, "starting write...");
                if(gatt == null){
                    Log.e(DEBUG_TAG, "Gatt is null!");
                }
                if(buffer.isEmpty()){
                    Log.e(DEBUG_TAG, "buffer empty!");
                }
                Log.i(DEBUG_TAG,""+buffer.get(0).getServUuid());
                Log.i(DEBUG_TAG,""+gatt.getDevice().getAddress());
               /* gatt.getService(buffer.get(0).getServUuid()).getCharacteristic(buffer.get(0).getCharUuid()).setValue(buffer.get(0).getData());
                gatt.getService(buffer.get(0).getServUuid()).getCharacteristic(buffer.get(0).getCharUuid()).setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
                boolean state = gatt.writeCharacteristic(gatt.getService(buffer.get(0).getServUuid()).getCharacteristic(buffer.get(0).getCharUuid()));*/
                boolean state = false;
                for (int i = 0; i < services.size(); i++) {
                    for (int j = 0; j < services.get(i).getCharacteristics().size(); j++) {
                        if (services.get(i).getCharacteristics().get(j).getUuid().equals(buffer.get(0).getCharUuid())) {
                            gatt.getService(services.get(i).getUuid()).getCharacteristic(services.get(i).getCharacteristics().get(j).getUuid()).setValue(buffer.get(0).getData());
                            gatt.getService(services.get(i).getUuid()).getCharacteristic(services.get(i).getCharacteristics().get(j).getUuid()).setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
                            state = gatt.writeCharacteristic(gatt.getService(services.get(i).getUuid()).getCharacteristic(services.get(i).getCharacteristics().get(j).getUuid()));

                            Log.i(DEBUG_TAG, "Writing UUID " + services.get(i).getCharacteristics().get(j).getUuid());
                        }
                    }
                }

                Log.i(DEBUG_TAG, "write state " + state);

                if(!state){
                    if(buffer.size() > 1){
                        if(buffer.get(1).getDev().equals(actDev)){
                            buffer.remove(0);
                            gatt.discoverServices();
                        }
                    } else
                        gatt.disconnect();
                }



            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status){
            Log.i(DEBUG_TAG, "Characteristic read");
            if(status == BluetoothGatt.GATT_SUCCESS)
                notifyRead(characteristic.getValue());
            else
                notifyFailed(gatt, ErrorObject.ERROR_CHAR_READ);

            if(buffer.size() > 1){
                if(buffer.get(1).getDev().equals(actDev)){
                    buffer.remove(0);
                    gatt.discoverServices();
                }
            } else
                gatt.disconnect();
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.i(DEBUG_TAG, "Characteristic written");
            if(status != BluetoothGatt.GATT_SUCCESS)
                notifyFailed(gatt, ErrorObject.ERROR_CHAR_WRITE);

            if(buffer.size() > 1){
                if(buffer.get(1).getDev().equals(actDev)){
                    buffer.remove(0);
                    gatt.discoverServices();
                }
            } else
                gatt.disconnect();
        }
    };
}
