package de.th_nuernberg.bluehome;

import android.Manifest;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;

import de.th_nuernberg.bluehome.BLEManagement.BLEDataExchangeManager;
import de.th_nuernberg.bluehome.BLEManagement.ErrorObject;
import de.th_nuernberg.bluehome.BlueHomeDatabase.BlueHomeDeviceStorageManager;

/**
 * StartActivity is the first shown Activity. It contains a dashboard and the main menu. Checks if devices are in range in background.
 *
 * @author Philipp Herrmann
 */
public class StartActivity extends AppCompatActivity {

    private boolean firstStart = false;
    private BlueHomeDeviceStorageManager storageManager = new BlueHomeDeviceStorageManager(this);
    private ArrayList<BlueHomeDevice> devices;
    private ArrayList<ErrorObject> errors = new ArrayList<>();
    private ListView list;
    private ErrorListAdapter errorListAdapter;
    private BLEDataExchangeManager bleman = (BLEDataExchangeManager) this.getApplication();

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_WRITE_STORAGE = 2;
    private static final int PERMISSION_REQUEST_BLUETOOTH = 3;

    public static String REFRESH_ACTIVITY = "de.th_nuernberg.bluehome.action.REFRESH_ERROR";

    private class ErrorReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //errors = bleman.getErrors();
            errorListAdapter.setNewErrorlist(errors);
            errorListAdapter.notifyDataSetChanged();
            list.deferNotifyDataSetChanged();
        }
    }

    private BroadcastReceiver errorRec = new ErrorReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.dashboard);
        setContentView(R.layout.activity_start);
        list = findViewById(R.id.error_list);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        errorListAdapter = new ErrorListAdapter(this, errors);
        list.setAdapter(errorListAdapter);

        getPermissions();


    }

    protected void onResume(){
        super.onResume();
        //if(firstStart)
            //showPopup();

        IntentFilter filter = new IntentFilter("ERROR_ACTION");
        this.registerReceiver(errorRec, filter);

        devices = storageManager.getAllDevices();
        //bleman.readErrors();
        //errors = bleman.getErrors();
        //TODO: Show List

    }

    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.unregisterReceiver(this.errorRec);
    }

    private void showPopup() {

        LayoutInflater inflater = (LayoutInflater) StartActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Inflate the view from a predefined XML layout (no need for root id, using entire layout)
        View layout = inflater.inflate(R.layout.popup_first_start,null);

        float density = StartActivity.this.getResources().getDisplayMetrics().density;

        final PopupWindow pw = new PopupWindow(layout, (int)density*240, (int)density*285, true);

        pw.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.RED));

        pw.setOutsideTouchable(true);
        pw.showAtLocation(layout, Gravity.CENTER, 0, 0);

    }

    public void menu1_pressed(View view){
        Intent intent = new Intent(this, ManAddDev.class);
        startActivity(intent);

    }

    public void menu2_pressed(View view){
        Intent intent = new Intent(this, DeviceList.class);
        startActivity(intent);
    }

    public void menu3_pressed(View view){
        Intent intent = new Intent(this, AppSettings.class);
        startActivity(intent);
    }

    public void menu4_pressed(View view){


        /*byte[] dummy = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
        BlueHomeDevice test = new BlueHomeDevice("10:80:E1:00:34:12", "test");
        bleman2.writeValue(bleman2.UUID_CMD_CMD_CHAR, dummy, test);*/
    }

    public void menu5_pressed(View view){

    }

    /**
     * asks for needed permissions if not already granted
     */
    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check 
            if (this.checkSelfPermission(Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
                //TODO: Language
                builder.setTitle(R.string.acces_bluetooth_title);
                builder.setMessage(R.string.acces_bluetooth_text);
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.BLUETOOTH_ADMIN}, PERMISSION_REQUEST_BLUETOOTH);
                    }
                });
                builder.show();
            }

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check 
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
                //TODO: Language
                //if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                builder.setTitle(R.string.acces_location_title);
                builder.setMessage(R.string.acces_location_text);
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
                /*} else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                }*/
            }

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check 
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
                //TODO: Language
                builder.setTitle(R.string.acces_storage_title);
                builder.setMessage(R.string.acces_storage_text);
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_WRITE_STORAGE);
                    }
                });
                builder.show();
            }

        }
    }
}
