package de.th_nuernberg.bluehome;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity {

    private boolean firstStart = false;

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_WRITE_STORAGE = 2;
    private static final int PERMISSION_REQUEST_BLUETOOTH = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.dashboard);
        setContentView(R.layout.activity_start);

        getPermissions();

    }

    protected void onResume(){
        super.onResume();
        //if(firstStart)
            //showPopup();

        //TODO: Error Check


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

    }

    public void menu5_pressed(View view){

    }

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
