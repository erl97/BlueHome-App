package de.th_nuernberg.bluehome;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

import de.th_nuernberg.bluehome.BlueHomeDatabase.RuleSetStorageManager;
import de.th_nuernberg.bluehome.RuleProcessObjects.RulesetObject;
import lib.folderpicker.FolderPicker;

public class ImportData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_data);
        Intent i = new Intent(ImportData.this, FolderPicker.class);
        i.putExtra("title", getResources().getString(R.string.choose_directory));
        i.putExtra("pickFiles", true);
        startActivityForResult(i, 9999);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 9999:
                if (data.getExtras().getString("data") == null)
                    break;
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    RuleSetStorageManager rs = new RuleSetStorageManager(ImportData.this);
                    rs.deleteAll();
                    try {
                        String fullPath = data.getExtras().getString("data");
                        File f = new File(fullPath);
                        if (!f.exists())
                            break;
                        StringBuilder text = new StringBuilder();
                        FileReader fIn = new FileReader(f);
                        BufferedReader br = new BufferedReader(new FileReader(f));
                        String line;

                        while ((line = br.readLine()) != null) {
                            text.append(line);
                        }
                        br.close();

                        rs.readFromJSON(text.toString());
                        final AlertDialog alertDialog = new AlertDialog.Builder(ImportData.this).create();
                        alertDialog.setTitle("Import successfully");
                        alertDialog.setMessage("The Settings of this App have succesfully been imported.");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                });
                        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialogInterface) {
                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor((Color.RED));
                            }
                        });
                        alertDialog.show();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //File write logic here
                }
                else
                {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 9998);

                }


                break;
        }
    }
}
