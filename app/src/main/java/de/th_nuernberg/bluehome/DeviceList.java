package de.th_nuernberg.bluehome;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

import de.th_nuernberg.bluehome.BlueHomeDatabase.BlueHomeDeviceStorageManager;

/**
 * DeviceList Activity lists all known {@link BlueHomeDevice}s and offers the ability to edit and delete the devices.
 *
 * @author Philipp Herrmann
 */
public class DeviceList extends AppCompatActivity {

    private ListView list;
    private ArrayList<BlueHomeDevice> devices;
    private FloatingActionButton deleteButton, addButton;
    private BlueHomeDeviceStorageManager storageManager = new BlueHomeDeviceStorageManager(this);
    private DeviceListAdapter list_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        //get Views
        deleteButton = (FloatingActionButton) findViewById(R.id.device_list_delete);
        addButton = (FloatingActionButton) findViewById(R.id.device_list_add);
        list = findViewById(R.id.device_list);

        DecimalFormat df = new DecimalFormat("00");
        BlueHomeDevice tmp;

        devices = storageManager.getAllDevices();

        list_adapter = new DeviceListAdapter(this, devices);
              list.setAdapter(list_adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(DeviceList.this, EditDevice.class);
                i.putExtra("macAddress", devices.get(position).getMacAddress());
                startActivity(i);
            }
        });



        //Delete Entries from list:
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (devices.size() <= 0) {
                    Toast.makeText(getBaseContext(), getResources().getText(R.string.no_entries), Toast.LENGTH_LONG).show();
                }

                if ((devices.size() > 0) && (!list_adapter.isDeleteActive())) {
                    list_adapter.setDeleteActive(true);
                } else if ((devices.size() > 0) && (list_adapter.isDeleteActive())) {

                    //TODO: ask for sure
                    CheckBox cb;
                    for (int x = 0; x < list.getChildCount() ;x++){
                        cb = (CheckBox)list.getChildAt(x).findViewById(R.id.device_list_delete);
                        if(cb.isChecked()){
                            storageManager.deleteDevice(devices.get(x).getMacAddress());
                        }
                    }

                    list_adapter.setDeleteActive(false);

                }

                if(list_adapter.isDeleteActive())
                    deleteButton.setImageDrawable(getDrawable(R.drawable.delete_selected));
                else
                    deleteButton.setImageDrawable(getDrawable(R.drawable.delete));

                devices = storageManager.getAllDevices();
                list_adapter.setNewDevicelist(devices);
                list_adapter.notifyDataSetChanged();
                list.deferNotifyDataSetChanged();

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DeviceList.this, FindNewDevice.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        devices = storageManager.getAllDevices();
        list_adapter.setNewDevicelist(devices);
        list_adapter.notifyDataSetChanged();
        list.deferNotifyDataSetChanged();
    }
}
