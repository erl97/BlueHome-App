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
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

import javax.xml.XMLConstants;

import de.th_nuernberg.bluehome.BlueHomeDatabase.BlueHomeDeviceStorageManager;
import de.th_nuernberg.bluehome.BlueHomeDatabase.RuleSetStorageManager;
import lib.folderpicker.FolderPicker;

/**
 * Export Data offers an export function for configuration information
 *
 * @author Philipp Herrmann
 */
public class ExportData extends AppCompatActivity {

    private Button selectButtonView;
    private TextView pathView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_data);
        setTitle(R.string.export_string);

        selectButtonView = (Button)findViewById(R.id.export_data_select_button);
        pathView = (TextView)findViewById(R.id.export_data_path);

        selectButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ExportData.this, FolderPicker.class);
                i.putExtra("title", getResources().getString(R.string.choose_directory));
                startActivityForResult(i, 9999);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 9999:
                if (data.getExtras().getString("data") == null)
                    break;
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Log.i("DIRBROWSER", "result");
                    Log.i("DIRBROWSER", data.getExtras().getString("data"));
                    pathView.setText(data.getExtras().getString("data"));

                    try {
                        String fullPath = data.getExtras().getString("data");
                        File dir = new File(fullPath);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        OutputStream fOut = null;
                        File file = new File(fullPath, "ruleExport.json");
                        if(file.exists())
                            file.delete();
                        file.createNewFile();
                        fOut = new FileOutputStream(file);
                        fOut.write(new RuleSetStorageManager(this).toJSON().getBytes());
                        fOut.flush();
                        fOut.close();
                        final AlertDialog alertDialog = new AlertDialog.Builder(ExportData.this).create();
                        alertDialog.setTitle("Export successfully");
                        alertDialog.setMessage("The Settings of this App have succesfully been exported.");
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

    /**
     * creates XML String to write into file
     *
     * @return XML String
     * @throws Exception
     */
    private String getExportData() throws Exception{
        XmlSerializer xml = Xml.newSerializer();
        BlueHomeDeviceStorageManager db = new BlueHomeDeviceStorageManager(this);
        ArrayList<BlueHomeDevice> devs;
        StringWriter writer = new StringWriter();

        xml.setOutput(writer);

        devs = db.getAllDevices();

        xml.startDocument("UTF-8", true);
        xml.startTag("", XmlConstants.DEVICES_TAG);

        for(int i = 0; i < devs.size(); i++) {
            xml.startTag("", XmlConstants.DEVICE_TAG);

            xml.startTag("", XmlConstants.MAC_TAG);
            xml.text(devs.get(i).getMacAddress());
            xml.endTag("", XmlConstants.MAC_TAG);

            xml.startTag("", XmlConstants.REAL_NAME_TAG);
            xml.text(devs.get(i).getDeviceName());
            xml.endTag("", XmlConstants.REAL_NAME_TAG);

            xml.startTag("", XmlConstants.SHOWN_NAME_TAG);
            xml.text(devs.get(i).getShownName());
            xml.endTag("", XmlConstants.SHOWN_NAME_TAG);

            xml.startTag("", XmlConstants.TYPE_TAG);
            xml.text("" + devs.get(i).getNodeType());
            xml.endTag("", XmlConstants.TYPE_TAG);

            xml.startTag("", XmlConstants.IMG_TAG);
            xml.text("" + devs.get(i).getImgID());
            xml.endTag("", XmlConstants.IMG_TAG);

            xml.endTag("", XmlConstants.DEVICE_TAG);
        }

        xml.endTag("", XmlConstants.DEVICES_TAG);

        return writer.toString();
    }
}
