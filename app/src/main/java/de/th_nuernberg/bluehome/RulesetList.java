package de.th_nuernberg.bluehome;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

import de.th_nuernberg.bluehome.Adapters.DeviceListAdapter;
import de.th_nuernberg.bluehome.Adapters.RuleSetListAdapter;
import de.th_nuernberg.bluehome.BLEManagement.BLEDataExchangeManager;
import de.th_nuernberg.bluehome.BLEManagement.BLEService;
import de.th_nuernberg.bluehome.BlueHomeDatabase.ActionStorageManager;
import de.th_nuernberg.bluehome.BlueHomeDatabase.BlueHomeDeviceStorageManager;
import de.th_nuernberg.bluehome.BlueHomeDatabase.RuleSetStorageManager;
import de.th_nuernberg.bluehome.BlueHomeDatabase.RuleStorageManager;
import de.th_nuernberg.bluehome.RuleProcessObjects.ActionObject;
import de.th_nuernberg.bluehome.RuleProcessObjects.NodeInfo;
import de.th_nuernberg.bluehome.RuleProcessObjects.RuleObject;
import de.th_nuernberg.bluehome.RuleProcessObjects.RulesetObject;

/**
 * DeviceList Activity lists all known {@link de.th_nuernberg.bluehome.RuleProcessObjects.RulesetObject}s and offers the ability to edit and delete the Rulesets.
 *
 * @author Philipp Herrmann
 */
public class RulesetList extends AppCompatActivity {

    private ListView list;
    private ArrayList<RulesetObject> rulesets;
    private FloatingActionButton deleteButton, addButton, downloadButton;
    private RuleSetStorageManager storageManager = new RuleSetStorageManager(this);
    private RuleStorageManager rsm = new RuleStorageManager(this);
    private ActionStorageManager asm = new ActionStorageManager(this);
    private BLEDataExchangeManager bleman = new BLEDataExchangeManager();
    private BlueHomeDeviceStorageManager bhsm = new BlueHomeDeviceStorageManager(this);
    private NodeInfo nodeInfo = new NodeInfo(this);
    private RuleSetListAdapter list_adapter;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("EditDevice", "Received Broadcast");
            Toast.makeText(context,"Failed to Connect to " + intent.getStringExtra(BLEService.TAG_MAC), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle(R.string.rules);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruleset_list);

        rulesets = new ArrayList<>();

        //get Views
        deleteButton = (FloatingActionButton) findViewById(R.id.ruleset_list_delete);
        addButton = (FloatingActionButton) findViewById(R.id.ruleset_list_add);
        downloadButton = (FloatingActionButton) findViewById(R.id.ruleset_list_download);
        list = findViewById(R.id.ruleset_list);

        //rulesets.add(testObject);
        rulesets = storageManager.getAllRulessets();

        list_adapter = new RuleSetListAdapter(this, rulesets);
              list.setAdapter(list_adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showRulsetInfo(rulesets.get(position));
                /*Intent i = new Intent(RulesetList.this, EditRuleset.class);
                i.putExtra("rulesetID", rulesets.get(position).getRulesetID());
                startActivity(i);*/
            }
        });

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeAll();
            }
        });

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
                            storageManager.deleteRuleSet(rulesets.get(x).getRulesetID());
                        }
                    }

                    list_adapter.setDeleteActive(false);

                }

                if(list_adapter.isDeleteActive())
                    deleteButton.setImageDrawable(getDrawable(R.drawable.delete_selected));
                else
                    deleteButton.setImageDrawable(getDrawable(R.drawable.delete));

                rulesets = storageManager.getAllRulessets();
                list_adapter.setNewList(rulesets);
                list_adapter.notifyDataSetChanged();
                list.deferNotifyDataSetChanged();

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RulesetList.this, /*CreateRuleset.class*/WizardCreateNewRuleSet.class);
                startActivity(i);
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("WRITE_FAIL"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        rulesets = storageManager.getAllRulessets();
        list_adapter.setNewList(rulesets);
        list_adapter.notifyDataSetChanged();
        list.deferNotifyDataSetChanged();
    }

    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onStop();
    }

    private void showRulsetInfo(RulesetObject rule)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.AlertDialogStyle).create();

        alertDialog.setTitle(getResources().getString(R.string.rule_info));

        StringBuilder message = new StringBuilder();
        message.append(getResources().getString(R.string.rule_info_text_1) + rule.getName() + getResources().getString(R.string.rule_info_text_2) + '\n');
        message.append(getResources().getString(R.string.source_device) + ": " + rule.getDev1().getShownName() + " (" + rule.getDev1().getMacAddress() + ")\n");
        if(rule.getDev2() != null)
            message.append(getResources().getString(R.string.target_device) + ": " + rule.getDev2().getShownName() + " (" + rule.getDev2().getMacAddress() + ")\n");
        else
            message.append(getResources().getString(R.string.target_device) + ": " + rule.getDev1().getShownName() + " (" + rule.getDev1().getMacAddress() + ")\n");

        message.append(getResources().getString(R.string.initiator) + ": " + nodeInfo.getSAMname(rule.getRule1().getToComp().getSourceSAM()) + "\n");
        if(rule.getDev2() != null)
            message.append(getResources().getString(R.string.actor) + ": " + nodeInfo.getSAMname(rule.getAction2().getActionSAM()) + "\n");
        else
            message.append(getResources().getString(R.string.actor) + ": " + nodeInfo.getSAMname(rule.getAction1().getActionSAM()) + "\n");

        alertDialog.setMessage(message);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        lp.leftMargin = 50;
        lp.rightMargin= 50;


        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        new Dialog(getApplicationContext());
        alertDialog.show();
    }


    private void writeAll()
    {
        if(storageManager.getAllRulessets().size() > 0)
        {
            ArrayList<BlueHomeDevice> devs = bhsm.getAllDevices();

            for(BlueHomeDevice dev : devs)
            {
                bleman.clearActions(dev, this);
                bleman.clearRules(dev, this);
                bleman.clearMACs(dev, this);
            }

            ArrayList<RuleObject> rules = rsm.getAllRules();
            ArrayList<ActionObject> actions = asm.getAllActions();
            ArrayList<BlueHomeDevice> toUpdate = new ArrayList<>();
            Log.i("RulesetList", "Rules size: " + rules.size());
            for(RuleObject ro : rules) {
                Log.i("RulesetList", "" + ro.getParamComp()[0]);
                bleman.programRule(storageManager.getDevForRule(ro), ro, this);
                if(!toUpdate.contains(storageManager.getDevForRule(ro)))
                    toUpdate.add(storageManager.getDevForRule(ro));
            }
            for(ActionObject ao : actions) {
                bleman.programAction(storageManager.getDevForAction(ao), ao, this);
                if(!toUpdate.contains(storageManager.getDevForAction(ao)))
                    toUpdate.add(storageManager.getDevForAction(ao));
            }



            for(BlueHomeDevice dev : toUpdate) {
                for (BlueHomeDevice d : devs)
                    if(d.getMacId() != 0) {
                        Log.i("RulesetList", "mac ID: " + d.getMacId() + " device: " + dev.getShownName());
                        bleman.programMAC(dev, d.getMacId(), d.getMacAddress(), this);
                    }

            }
        }
    }
}
