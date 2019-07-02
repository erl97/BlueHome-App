package de.th_nuernberg.bluehome;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

import de.th_nuernberg.bluehome.Adapters.DeviceListAdapter;
import de.th_nuernberg.bluehome.Adapters.RuleSetListAdapter;
import de.th_nuernberg.bluehome.BlueHomeDatabase.BlueHomeDeviceStorageManager;
import de.th_nuernberg.bluehome.BlueHomeDatabase.RuleSetStorageManager;
import de.th_nuernberg.bluehome.RuleProcessObjects.RulesetObject;

/**
 * DeviceList Activity lists all known {@link de.th_nuernberg.bluehome.RuleProcessObjects.RulesetObject}s and offers the ability to edit and delete the Rulesets.
 *
 * @author Philipp Herrmann
 */
public class RulesetList extends AppCompatActivity {

    private ListView list;
    private ArrayList<RulesetObject> rulesets;
    private FloatingActionButton deleteButton, addButton;
    private RuleSetStorageManager storageManager = new RuleSetStorageManager(this);
    private RuleSetListAdapter list_adapter;
    private RulesetObject testObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle(R.string.rules);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruleset_list);

        rulesets = new ArrayList<>();

        testObject = new RulesetObject();
        testObject.setName("Test Set");
        testObject.setDev1(new BlueHomeDevice("AB:CD:EF:GH:IJ:KL", "Dev 1"));
        testObject.setDev2(new BlueHomeDevice("12:34:56:78:90:XY", "Dev 2"));
        testObject.getDev1().setImgID(R.drawable.bluehome_node_1);
        testObject.getDev2().setImgID(R.drawable.bluehome_node_2);
        testObject.getDev1().setShownName("Device 1");
        testObject.getDev2().setShownName("Device 2");

        //get Views
        deleteButton = (FloatingActionButton) findViewById(R.id.ruleset_list_delete);
        addButton = (FloatingActionButton) findViewById(R.id.ruleset_list_add);
        list = findViewById(R.id.ruleset_list);

        //rulesets.add(testObject);
        rulesets = storageManager.getAllRulessets();

        list_adapter = new RuleSetListAdapter(this, rulesets);
              list.setAdapter(list_adapter);


        /*list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(RulesetList.this, EditRuleset.class);
                i.putExtra("rulesetID", rulesets.get(position).getRulesetID());
                startActivity(i);
            }
        });*/



        //Delete Entries from list:
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rulesets.size() <= 0) {
                    Toast.makeText(getBaseContext(), getResources().getText(R.string.no_entries), Toast.LENGTH_LONG).show();
                }

                if ((rulesets.size() > 0) && (!list_adapter.isDeleteActive())) {
                    list_adapter.setDeleteActive(true);
                } else if ((rulesets.size() > 0) && (list_adapter.isDeleteActive())) {

                    Log.i("RulesetList", "deleting...");
                    //TODO: ask for sure
                    CheckBox cb;
                    for (int x = 0; x < list.getChildCount() ;x++){
                        cb = (CheckBox)list.getChildAt(x).findViewById(R.id.ruleset_list_delete);
                        if(cb.isChecked()){
                            //storageManager.deleteRuleSet(rulesets.get(x).getRulesetID());
                        }
                    }

                    list_adapter.setDeleteActive(false);

                }

                if(list_adapter.isDeleteActive())
                    deleteButton.setImageDrawable(getDrawable(R.drawable.delete_selected));
                else
                    deleteButton.setImageDrawable(getDrawable(R.drawable.delete));

                //rulesets = storageManager.getAllRulessets();
                list_adapter.setNewList(rulesets);
                list_adapter.notifyDataSetChanged();
                list.deferNotifyDataSetChanged();

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RulesetList.this, CreateRuleset.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //rulesets = storageManager.getAllRulessets();
        list_adapter.setNewList(rulesets);
        list_adapter.notifyDataSetChanged();
        list.deferNotifyDataSetChanged();
    }
}
