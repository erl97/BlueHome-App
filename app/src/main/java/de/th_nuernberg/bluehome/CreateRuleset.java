package de.th_nuernberg.bluehome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.th_nuernberg.bluehome.BlueHomeDatabase.ActionStorageManager;
import de.th_nuernberg.bluehome.BlueHomeDatabase.BlueHomeDeviceStorageManager;
import de.th_nuernberg.bluehome.BlueHomeDatabase.RuleSetStorageManager;
import de.th_nuernberg.bluehome.BlueHomeDatabase.RuleStorageManager;
import de.th_nuernberg.bluehome.RuleProcessObjects.ActionObject;
import de.th_nuernberg.bluehome.RuleProcessObjects.NodeInfo;
import de.th_nuernberg.bluehome.RuleProcessObjects.RuleObject;
import de.th_nuernberg.bluehome.RuleProcessObjects.RulesetObject;
import de.th_nuernberg.bluehome.RuleProcessObjects.SourceObject;

public class CreateRuleset extends AppCompatActivity {

    private Spinner source_device;
    private Spinner target_device;
    private Spinner initiator;
    private Spinner actor;
    private Spinner initiator_action;
    private Spinner actor_action;
    private Spinner operators;
    private TextView labelIn;
    private TextView labelOut;
    private EditText valueIn;
    private EditText valueOut;
    private BlueHomeDeviceStorageManager deviceStorageManager = new BlueHomeDeviceStorageManager(this);
    private RuleSetStorageManager ruleSetStorageManager = new RuleSetStorageManager(this);
    private ArrayAdapter<String> adapterDevices;
    private ArrayAdapter<String> adapterInitiators;
    private ArrayAdapter<String> adapterActors;
    private ArrayAdapter<String> adapterInitiatorActions;
    private ArrayAdapter<String> adapterActorActions;
    private ArrayAdapter<String> adapterOperators;
    private ArrayList<String> deviceNames = new ArrayList<>();
    private List<String> initiators = new ArrayList<>();
    private List<String> actors = new ArrayList<>();
    private List<String> initiatorAction = new ArrayList<>();
    private List<String> actorAction = new ArrayList<>();
    private List<String> operatorsList = new ArrayList<>();
    private ArrayList<BlueHomeDevice> devs;

    private NodeInfo nodeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.create_ruleset);

        nodeInfo = new NodeInfo(this);



        setContentView(R.layout.activity_create_rule);
        source_device = (Spinner)findViewById(R.id.create_ruleset_spin_source_device);
        target_device = (Spinner)findViewById(R.id.create_ruleset_spin_target_device);
        initiator = (Spinner)findViewById(R.id.create_ruleset_spin_initiator);
        actor = (Spinner)findViewById(R.id.create_ruleset_spin_actor);
        initiator_action = (Spinner)findViewById(R.id.create_ruleset_spin_initiator_action);
        actor_action = (Spinner)findViewById(R.id.create_ruleset_spin_actor_action);
        operators = (Spinner)findViewById(R.id.create_ruleset_spin_compare);
        valueIn = (EditText)findViewById(R.id.create_ruleset_tf_source_number_input);
        valueOut = (EditText)findViewById(R.id.create_ruleset_tf_target_number_input);
        labelIn = (TextView)findViewById(R.id.create_ruleset_tf_source_number);
        labelOut = (TextView)findViewById(R.id.create_ruleset_tf_target_number);

        valueIn.setEnabled(false);
        valueOut.setEnabled(false);
        valueIn.setVisibility(View.INVISIBLE);
        valueOut.setVisibility(View.INVISIBLE);

        adapterInitiators = new ArrayAdapter<String>(CreateRuleset.this, R.layout.text_spinner_layout, initiators);
        adapterInitiators.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        initiator.setAdapter(adapterInitiators);

        adapterActors = new ArrayAdapter<String>(CreateRuleset.this, R.layout.text_spinner_layout, actors);
        adapterActors.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actor.setAdapter(adapterActors);

        adapterInitiatorActions = new ArrayAdapter<String>(CreateRuleset.this, R.layout.text_spinner_layout, initiatorAction);
        adapterInitiatorActions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        initiator_action.setAdapter(adapterInitiatorActions);

        adapterActorActions = new ArrayAdapter<String>(CreateRuleset.this, R.layout.text_spinner_layout, actorAction);
        adapterActorActions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actor_action.setAdapter(adapterActorActions);

        adapterOperators = new ArrayAdapter<String>(CreateRuleset.this, R.layout.text_spinner_layout, operatorsList);
        adapterOperators.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        operators.setAdapter(adapterOperators);

        devs = deviceStorageManager.getAllDevices();
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

        target_device.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position >= 1) {
                    refillList(nodeInfo.getNodeActors(devs.get(position - 1).getNodeType()), actors);
                } else {
                    actors.clear();
                    actorAction.clear();
                    labelOut.setText("");
                    valueOut.setEnabled(false);
                    valueOut.setVisibility(View.INVISIBLE);
                }
                adapterActors.notifyDataSetChanged();
                adapterActorActions.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        source_device.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("CreateRuleset", "selected");
                if(position >= 1) {
                    refillList(nodeInfo.getNodeInitiators(devs.get(position - 1).getNodeType()), initiators);
                } else {
                    initiators.clear();
                    initiatorAction.clear();
                    valueIn.setVisibility(View.INVISIBLE);
                    valueIn.setEnabled(false);
                    labelIn.setText("");
                    operatorsList.clear();
                }
                adapterInitiators.notifyDataSetChanged();
                adapterInitiatorActions.notifyDataSetChanged();
                adapterOperators.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        initiator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position >= 0) {
                    refillList(nodeInfo.getInitiatorOperations(devs.get(source_device.getSelectedItemPosition() - 1).getNodeType(), nodeInfo.getNodeInitiators(devs.get(source_device.getSelectedItemPosition() - 1).getNodeType()).get(position)), initiatorAction);
                    refillList(nodeInfo.getOperators(devs.get(source_device.getSelectedItemPosition() - 1).getNodeType(), nodeInfo.getNodeInitiators(devs.get(source_device.getSelectedItemPosition() - 1).getNodeType()).get(position)), operatorsList);
                    valueIn.setVisibility(View.VISIBLE);
                    valueIn.setEnabled(true);
                    labelIn.setText(nodeInfo.getInputValueLabel(devs.get(source_device.getSelectedItemPosition() - 1).getNodeType(), nodeInfo.getNodeInitiators(devs.get(source_device.getSelectedItemPosition() - 1).getNodeType()).get(position)));

                } else {
                    valueIn.setVisibility(View.INVISIBLE);
                    valueIn.setEnabled(false);
                    labelIn.setText("");
                    initiatorAction.clear();
                    operatorsList.clear();
                }
                adapterInitiatorActions.notifyDataSetChanged();
                adapterOperators.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        actor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position >= 0) {
                    refillList(nodeInfo.getActorOperations(devs.get(target_device.getSelectedItemPosition() - 1).getNodeType(), nodeInfo.getNodeActors(devs.get(target_device.getSelectedItemPosition() - 1).getNodeType()).get(position)), actorAction);
                    labelOut.setText(nodeInfo.getOutputValueLabel(devs.get(target_device.getSelectedItemPosition() - 1).getNodeType(), nodeInfo.getNodeActors(devs.get(target_device.getSelectedItemPosition() - 1).getNodeType()).get(position)));
                    valueOut.setEnabled(true);
                    valueOut.setVisibility(View.VISIBLE);
                } else {
                    labelOut.setText("");
                    actorAction.clear();
                    valueOut.setEnabled(false);
                    valueOut.setVisibility(View.INVISIBLE);
                }
                adapterActorActions.notifyDataSetChanged();
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

    public void createNewRuleset(View view)
    {
        RulesetObject ruleSet = new RulesetObject();

        ruleSet.setName(((EditText)(findViewById(R.id.create_ruleset_tf_rule_name))).getText().toString());

        if(devs.get(source_device.getSelectedItemPosition() - 1).getMacAddress().equals(devs.get(target_device.getSelectedItemPosition() - 1).getMacAddress()))
        {
            ruleSet.setDev1(devs.get(source_device.getSelectedItemPosition() - 1));

            ruleSet.setAction1(new ActionObject());
            ruleSet.setRule1(new RuleObject());
            ruleSet.getRule1().setToComp(new SourceObject());

            ruleSet.getAction1().setActionID(nodeInfo.getActionID((devs.get(target_device.getSelectedItemPosition() - 1)).getNodeType(), actors.get(actor.getSelectedItemPosition()), actorAction.get(actor_action.getSelectedItemPosition())));
            ruleSet.getAction1().setActionMemID(ruleSetStorageManager.getNextFreeActionMemId(ruleSet.getDev2()));
            ruleSet.getAction1().setAppActionID(ruleSetStorageManager.getNextAppActionId((byte)0));
            ruleSet.getAction1().setParam((byte) Integer.parseInt(valueOut.getText().toString()), 0);
            ruleSet.getAction1().setParamMask(0);
            ruleSet.getAction1().setActionSAM(nodeInfo.getSamID(devs.get(target_device.getSelectedItemPosition() - 1).getNodeType(), actors.get(actor.getSelectedItemPosition())));

            Log.i("ERRORLOGGERLOL", "dev size " + devs.size() + ", actors size " + actors.size());

            ruleSet.getRule1().setParamComp(nodeInfo.getParamCompID(devs.get(source_device.getSelectedItemPosition() - 1).getNodeType(), initiators.get(initiator.getSelectedItemPosition()), operatorsList.get(operators.getSelectedItemPosition())));
            ruleSet.getRule1().setRuleMemID(ruleSetStorageManager.getNextFreeRuleMemId(ruleSet.getDev1()));
            ruleSet.getRule1().setActionMemID(ruleSet.getAction1().getActionMemID());
            ruleSet.getRule1().setAppRuleID(ruleSetStorageManager.getNextAppRuleId((byte)0));
            ruleSet.getRule1().getToComp().setSourceSAM(nodeInfo.getSamID(devs.get(source_device.getSelectedItemPosition() - 1).getNodeType(), initiators.get(initiator.getSelectedItemPosition())));
            ruleSet.getRule1().getToComp().setSourceID(nodeInfo.getSourceID(devs.get(source_device.getSelectedItemPosition() - 1).getNodeType(), initiators.get(initiator.getSelectedItemPosition()), initiatorAction.get(initiator_action.getSelectedItemPosition())));
            ruleSet.getRule1().getToComp().setParams(nodeInfo.getParam(devs.get(source_device.getSelectedItemPosition() - 1).getNodeType(), initiators.get(initiator.getSelectedItemPosition()),(byte)Integer.parseInt(valueIn.getText().toString())));

        } else {
            ruleSet.setAction1(new ActionObject());
            ruleSet.setAction2(new ActionObject());
            ruleSet.setRule1(new RuleObject());
            ruleSet.setRule2(new RuleObject());
            ruleSet.getRule1().setToComp(new SourceObject());
            ruleSet.getRule2().setToComp(new SourceObject());

            ruleSet.setDev1(devs.get(source_device.getSelectedItemPosition() - 1));
            ruleSet.setDev2(devs.get(target_device.getSelectedItemPosition() - 1));

            ruleSet.getAction1().setActionID(ruleSet.getDev2().getMacId());
            ruleSet.getAction1().setActionMemID(ruleSetStorageManager.getNextFreeActionMemId(ruleSet.getDev1()));
            ruleSet.getAction1().setAppActionID(ruleSetStorageManager.getNextAppActionId((byte)0));
            ruleSet.getAction1().setParam(ruleSet.getAction1().getAppActionID(), 0);
            ruleSet.getAction1().setParamMask(0);
            ruleSet.getAction1().setActionSAM((byte) 0x01);
            ruleSet.getAction2().setActionID(nodeInfo.getActionID((devs.get(target_device.getSelectedItemPosition() - 1)).getNodeType(), actors.get(actor.getSelectedItemPosition()), actorAction.get(actor_action.getSelectedItemPosition())));
            ruleSet.getAction2().setActionMemID(ruleSetStorageManager.getNextFreeActionMemId(ruleSet.getDev2()));
            ruleSet.getAction2().setAppActionID(ruleSetStorageManager.getNextAppActionId((byte)(ruleSet.getAction1().getAppActionID() + 1)));
            ruleSet.getAction2().setParam((byte) Integer.parseInt(valueOut.getText().toString()), 0);
            ruleSet.getAction2().setParamMask(0);
            ruleSet.getAction2().setActionSAM(nodeInfo.getSamID(devs.get(target_device.getSelectedItemPosition() - 1).getNodeType(), actors.get(actor.getSelectedItemPosition())));

            ruleSet.getRule1().setParamComp(nodeInfo.getParamCompID(devs.get(source_device.getSelectedItemPosition() - 1).getNodeType(), initiators.get(initiator.getSelectedItemPosition()), operatorsList.get(operators.getSelectedItemPosition())));
            ruleSet.getRule1().setRuleMemID(ruleSetStorageManager.getNextFreeRuleMemId(ruleSet.getDev1()));
            ruleSet.getRule1().setActionMemID(ruleSet.getAction1().getActionMemID());
            ruleSet.getRule1().setAppRuleID(ruleSetStorageManager.getNextAppRuleId((byte)0));
            ruleSet.getRule1().getToComp().setSourceSAM(nodeInfo.getSamID(devs.get(source_device.getSelectedItemPosition() - 1).getNodeType(), initiators.get(initiator.getSelectedItemPosition())));
            ruleSet.getRule1().getToComp().setSourceID(nodeInfo.getSourceID(devs.get(source_device.getSelectedItemPosition() - 1).getNodeType(), initiators.get(initiator.getSelectedItemPosition()), initiatorAction.get(initiator_action.getSelectedItemPosition())));
            ruleSet.getRule1().getToComp().setParams(nodeInfo.getParam(devs.get(source_device.getSelectedItemPosition() - 1).getNodeType(), initiators.get(initiator.getSelectedItemPosition()),(byte)Integer.parseInt(valueIn.getText().toString())));

            ruleSet.getRule2().setParamComp((byte) 0x01, 0x00);
            ruleSet.getRule2().setRuleMemID(ruleSetStorageManager.getNextFreeRuleMemId(ruleSet.getDev2()));
            ruleSet.getRule2().setActionMemID(ruleSet.getAction2().getActionMemID());
            ruleSet.getRule2().setAppRuleID(ruleSetStorageManager.getNextAppRuleId((byte)(ruleSet.getRule1().getAppRuleID() + 1)));
            ruleSet.getRule2().getToComp().setSourceID(ruleSet.getDev1().getMacId());
            ruleSet.getRule2().getToComp().setSourceSAM((byte) 0x01);
            ruleSet.getRule2().getToComp().setParam(0, ruleSet.getAction1().getAppActionID());


        }

        ruleSetStorageManager.insertRuleset(ruleSet);

        this.finish();

    }

}

