package de.th_nuernberg.bluehome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

import javax.xml.XMLConstants;

import de.th_nuernberg.bluehome.BlueHomeDatabase.BlueHomeDeviceStorageManager;
import lib.folderpicker.FolderPicker;

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
                Log.i("DIRBROWSER", "result");
                Log.i("DIRBROWSER", data.getExtras().getString("data"));
                pathView.setText(data.getExtras().getString("data"));
                break;
        }
    }

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
