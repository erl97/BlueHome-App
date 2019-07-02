package de.th_nuernberg.bluehome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import de.th_nuernberg.bluehome.BlueHomeDatabase.BlueHomeDeviceStorageManager;

public class CreateRuleset extends AppCompatActivity {

    private Spinner source_device;
    private Spinner target_device;
    private Spinner initiator;
    private Spinner actor;
    private Spinner initiator_action;
    private Spinner actor_action;
    private BlueHomeDeviceStorageManager storageManager = new BlueHomeDeviceStorageManager(this);
    private ArrayAdapter<String> adapterDevices;
    private ArrayAdapter<String> adapterInitiators;
    private ArrayAdapter<String> adapterActions;
    private ArrayList<String> deviceNames = new ArrayList<>();
    private List<String> initiators = new ArrayList<>();
    private List<String> initiatorsNode1 = new ArrayList<>();
    private List<String> actorsNode2 = new ArrayList<>();
    private List<String> actionsNode1 = new ArrayList<>();
    private List<String> actionsNode2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.create_ruleset);

        initiatorsNode1.add(getResources().getString(R.string.initiator_current_sens));
        initiatorsNode1.add(getResources().getString(R.string.initiator_input_1));
        initiatorsNode1.add(getResources().getString(R.string.initiator_input_2));
        initiatorsNode1.add(getResources().getString(R.string.initiator_input_3));
        initiatorsNode1.add(getResources().getString(R.string.initiator_input_4));
        initiatorsNode1.add(getResources().getString(R.string.initiator_input_5));
        initiatorsNode1.add(getResources().getString(R.string.initiator_input_6));
        initiatorsNode1.add(getResources().getString(R.string.initiator_input_7));
        initiatorsNode1.add(getResources().getString(R.string.initiator_input_8));
        initiatorsNode1.add(getResources().getString(R.string.initiator_temp_Sens));

        actorsNode2.add(getResources().getString(R.string.initiator_button_1));
        actorsNode2.add(getResources().getString(R.string.initiator_button_2));
        actorsNode2.add(getResources().getString(R.string.initiator_button_3));
        actorsNode2.add(getResources().getString(R.string.initiator_button_4));
        actorsNode2.add(getResources().getString(R.string.initiator_wheel));
        actorsNode2.add(getResources().getString(R.string.initiator_humidity));
        actorsNode2.add(getResources().getString(R.string.initiator_light));
        actorsNode2.add(getResources().getString(R.string.initiator_temp_Sens));
        actorsNode2.add(getResources().getString(R.string.initiator_microfone));

        setContentView(R.layout.activity_create_rule);
        source_device = (Spinner)findViewById(R.id.create_ruleset_spin_source_device);
        target_device = (Spinner)findViewById(R.id.create_ruleset_spin_target_device);
        initiator = (Spinner)findViewById(R.id.create_ruleset_spin_initiator);
        actor = (Spinner)findViewById(R.id.create_ruleset_spin_actor);
        initiator_action = (Spinner)findViewById(R.id.create_ruleset_spin_initiator_action);
        actor_action = (Spinner)findViewById(R.id.create_ruleset_spin_actor_action);

        adapterInitiators = new ArrayAdapter<String>(CreateRuleset.this, R.layout.text_spinner_layout, initiators);
        adapterInitiators.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        initiator.setAdapter(adapterInitiators);

        final ArrayList<BlueHomeDevice> devs = storageManager.getAllDevices();
        for(int i = 0; i < devs.size(); i++)
        {
            deviceNames.add(devs.get(i).getShownName());
        }

        deviceNames.add(0, "-- " + getResources().getString(R.string.please_choose) + " --");

        if(devs.size() == 0)
        {
            adapterDevices = new ArrayAdapter<String>(this, R.layout.text_spinner_layout, new String[]{getResources().getString(R.string.no_devs)});
        } else {
            adapterDevices = new ArrayAdapter<String>(this, R.layout.text_spinner_layout, (String[]) deviceNames.toArray(new String[0]));
        }
        adapterDevices.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        source_device.setAdapter(adapterDevices);
        target_device.setAdapter(adapterDevices);

        source_device.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("CreateRuleset", "selected");
                if(position >= 1) {
                    switch (devs.get(position - 1).getNodeType()) {
                        case 1:
                            refillList(initiatorsNode1, initiators);
                            break;
                        case 2:
                            refillList(actorsNode2, initiators);
                            break;
                    }
                } else {
                    initiators.clear();
                }
                adapterInitiators.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void refillList(List<String> sourceList, List<String> toFill)
    {
        toFill.clear();
        for(int i = 0; i < sourceList.size(); i++)
            toFill.add(sourceList.get(i));
    }
}
