package de.th_nuernberg.bluehome;

import android.net.MacAddress;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class DeviceList extends AppCompatActivity {

    private ListView list;
    private ArrayList<BlueHomeDevice> devices = new ArrayList<BlueHomeDevice>();
    private FloatingActionButton deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        for (int i = 0; i < 12; i++) {
            devices.add(new BlueHomeDevice("AB:CD:EF:GH:IJ:KL", "Abe9i3ls"));
            devices.get(i).setImgID(R.drawable.add);
            devices.get(i).setShownName("Gerät " + i);
        }

        final DeviceListAdapter list_adapter = new DeviceListAdapter(this, devices);
        list = findViewById(R.id.device_list);
        list.setAdapter(list_adapter);
        list.deferNotifyDataSetChanged();

        deleteButton = (FloatingActionButton) findViewById(R.id.device_list_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (devices.size() <= 0) {
                    Toast.makeText(getBaseContext(), getResources().getText(R.string.no_entries), Toast.LENGTH_LONG).show();
                }

                if ((devices.size() > 0) && (!devices.get(0).isDeleteActive())) {
                    for(BlueHomeDevice p : devices) {
                        p.setDeleteActive(true);
                    }
                } else if ((devices.size() > 0) && (devices.get(0).isDeleteActive())) {

                    //TODO: ask for sure
                    CheckBox cb;
                    for (int x = list.getChildCount() - 1; x >= 0;x--){
                        cb = (CheckBox)list.getChildAt(x).findViewById(R.id.device_list_delete);
                        if(cb.isChecked()){
                            devices.remove(x);
                        }
                    }

                    if(devices.size() > 0) {
                        for (BlueHomeDevice p : devices) {
                            p.setDeleteActive(false);
                        }
                    }

                }

                DeviceList.this.runOnUiThread(new Runnable() {
                    public void run() {
                        list_adapter.notifyDataSetChanged();
                        list.deferNotifyDataSetChanged();
                    }
                });
            }
        });

    }
}
