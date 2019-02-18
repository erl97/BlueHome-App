package de.th_nuernberg.bluehome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import de.th_nuernberg.bluehome.BlueHomeDatabase.BlueHomeDeviceStorageManager;

/**
 * ManAddDev Activity allows adding a device to known devices manually. Only for debug use, shoud be hidden in final App
 *
 * @author Philipp Herrmann
 */
public class ManAddDev extends AppCompatActivity {

    private EditText shownNameView;
    private EditText realNameView;
    private EditText macAddressView;
    private Button addButton;
    private CheckBox devImg;
    private BlueHomeDevice newDevice;
    private BlueHomeDeviceStorageManager db = new BlueHomeDeviceStorageManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_add_dev);
        setTitle(getResources().getText(R.string.add_new_device));

        shownNameView = (EditText)findViewById(R.id.add_device_shown_name);
        realNameView = (EditText)findViewById(R.id.add_device_real_name);
        macAddressView = (EditText)findViewById(R.id.add_device_mac);
        addButton = (Button)findViewById(R.id.add_device_add_button);
        devImg = (CheckBox)findViewById(R.id.add_device_devimg_check);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newDevice = new BlueHomeDevice(macAddressView.getText().toString(), realNameView.getText().toString());
                newDevice.setNodeType(1);
                newDevice.setShownName(shownNameView.getText().toString());
                if(devImg.isChecked())
                    newDevice.setImgID(R.drawable.bluehome_device);
                else
                    newDevice.setImgID(R.drawable.unknown_device);
                db.insertDevice(newDevice);
                finish();
            }
        });
    }
}
