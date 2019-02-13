package de.th_nuernberg.bluehome;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.th_nuernberg.bluehome.BlueHomeDatabase.BlueHomeDeviceStorageManager;

public class FindNewDevice extends AppCompatActivity {

    private BluetoothAdapter mBluetoothAdapter;
    private final int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 10000;
    private boolean mScanning;
    private Handler mHandler = new Handler();
    private ArrayList<BlueHomeDevice> devices = new ArrayList<BlueHomeDevice>();
    private DeviceListAdapter list_adapter;
    private ListView list;
    private ProgressBar pb;
    private ImageView rescan;
    private final String deviceIdStart = "blhnt";
    private final String deviceIdEnd = "d";
    private final String deviceTypeSpacer = "ui";
    private BlueHomeDeviceStorageManager db = new BlueHomeDeviceStorageManager(this);
    private ArrayList<BlueHomeDevice> knownDevices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_new_device);
        setTitle(R.string.find_new_device);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        pb = (ProgressBar)findViewById(R.id.find_new_device_progress);
        pb.setVisibility(View.INVISIBLE);
        rescan = (ImageView)findViewById(R.id.find_new_device_rescan);
        rescan.setVisibility(View.INVISIBLE);

        rescan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanLeDevice(true);
                devices.clear();
                list_adapter.notifyDataSetChanged();
                list.deferNotifyDataSetChanged();
            }
        });

        knownDevices = db.getAllDevices();

        list = findViewById(R.id.find_new_device_list);

        list_adapter = new DeviceListAdapter(this, devices);
        list_adapter.setVersion(4);
        list.setAdapter(list_adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openDialog(devices.get(position));
                /*Intent i = new Intent(DeviceList.this, EditDevice.class);
                i.putExtra("macAddress", devices.get(position).getMacAddress());
                startActivity(i);*/
            }
        });

        scanLeDevice(true);

    }


    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    pb.setVisibility(View.INVISIBLE);
                    rescan.setVisibility(View.VISIBLE);
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);

            pb.setVisibility(View.VISIBLE);
            rescan.setVisibility(View.INVISIBLE);
            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
            Log.i("LE_SCAN", "Started Scan");
        } else {
            pb.setVisibility(View.INVISIBLE);
            rescan.setVisibility(View.VISIBLE);
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(deviceIsBlueHome(device))
                            {
                                BlueHomeDevice dev = new BlueHomeDevice(device.getAddress(), device.getName());
                                dev.setNodeType(Integer.parseInt(device.getName().substring(5, device.getName().length() - 9)));
                                Log.i("DEVICE", "Created Device, node type " + dev.getNodeType());
                                switch (dev.getNodeType()) {
                                    case 1:
                                        dev.setImgID(R.drawable.bluehome_node_1);
                                        break;
                                    case 2:
                                        dev.setImgID(R.drawable.bluehome_node_2);
                                        break;
                                    case 3:
                                        dev.setImgID(R.drawable.bluehome_node_3);
                                        break;
                                    case 4:
                                        dev.setImgID(R.drawable.bluehome_node_4);
                                        break;

                                        default:
                                            dev.setImgID(R.drawable.bluehome_device);
                                            break;
                                }

                                if((!devices.contains(dev)) && (!knownDevices.contains(dev)))
                                    devices.add(dev);
                                list_adapter.notifyDataSetChanged();
                                list.deferNotifyDataSetChanged();
                            }
                        }
                    });
                }
    };

    private boolean deviceIsBlueHome(BluetoothDevice device) {
        if((device.getName() != null) && (device.getName().startsWith(deviceIdStart)) && (device.getName().endsWith(deviceIdEnd)) && (device.getName().startsWith(deviceTypeSpacer, device.getName().length() - (6 + deviceIdEnd.length() + deviceTypeSpacer.length()))))

            return true;
        else
            return false;

    }


    private void openDialog(final BlueHomeDevice dev) {

        AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.AlertDialogStyle).create();

        alertDialog.setTitle(getResources().getString(R.string.add_device_question));
        alertDialog.setMessage(getResources().getString(R.string.like_to_add_question_1) + dev.getMacAddress() + getResources().getString(R.string.like_to_add_question_2));

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                openNameDialog(dev);
            }
        });

        new Dialog(getApplicationContext());
        alertDialog.show();
    }

    private void openNameDialog(BlueHomeDevice dev) {

        final BlueHomeDevice device = new BlueHomeDevice(dev);

        AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.AlertDialogStyle).create();

        alertDialog.setTitle(getResources().getString(R.string.name_device));
        alertDialog.setMessage(getResources().getString(R.string.give_device_name));

        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        lp.leftMargin = 50;
        lp.rightMargin= 50;
        input.setLayoutParams(lp);
        input.setPadding(50,10,50,10);
        input.setGravity(View.TEXT_ALIGNMENT_CENTER);
        input.setTextColor(getColor(R.color.colorAccent));
        alertDialog.setView(input);

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.save), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                device.setShownName(input.getText().toString());
                db.insertDevice(device);
                finish();
            }
        });

        new Dialog(getApplicationContext());
        alertDialog.show();

    }
}
