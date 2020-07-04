package de.th_nuernberg.bluehome;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

import de.th_nuernberg.bluehome.RuleProcessObjects.NodeInfo;

public class WizardChoseRulesetActorAction extends Fragment {

    private Button btnContinue;
    private WizardCreateNewRuleSet fragmentView;
    private BlueHomeDevice device;
    public String actor;
    public Spinner spinnerActorAction;
    private TextView labelOut;
    public EditText valueOut;
    private ArrayAdapter<String> adapterActorActions;
    public List<String> actorActions = new ArrayList<>();
    private NodeInfo nodeInfo;

    public void Continue()
    {
        if ((byte)Integer.parseInt(valueOut.getText().toString()) >= nodeInfo.getMinNum(device.getNodeType(), actor) && (byte)Integer.parseInt(valueOut.getText().toString()) <= nodeInfo.getMaxNum(device.getNodeType(), actor))
            fragmentView.createRuleSet();
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(fragmentView);
            final AlertDialog dialog = builder.setTitle("Error!").setMessage(R.string.newRuleSetNotWithinBorders).setPositiveButton(android.R.string.ok, null).create();
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
                }
            });
            dialog.show();
        }

    }

    public void setActor(String actor)
    {
        this.actor = actor;
        if (device != null)
        {
            refillList(nodeInfo.getActorOperations(device.getNodeType(), actor), actorActions);
            labelOut.setText(nodeInfo.getOutputValueLabel(device.getNodeType(), actor));
            adapterActorActions.notifyDataSetChanged();
        }
    }

    public void setTargetDevice(BlueHomeDevice device)
    {
        this.device = device;
        if (actor != null)
        {
            refillList(nodeInfo.getActorOperations(device.getNodeType(), actor), actorActions);
            labelOut.setText(nodeInfo.getOutputValueLabel(device.getNodeType(), actor));
            adapterActorActions.notifyDataSetChanged();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.activity_wizard_chose_ruleset_actor_action, container, false);
        nodeInfo = new NodeInfo(fragmentView);
        btnContinue = (Button)rootView.findViewById(R.id.btnFinish);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Continue();
            }
        });
        spinnerActorAction = (Spinner)rootView.findViewById(R.id.spinnerActorAction);
        valueOut = (EditText)rootView.findViewById(R.id.editTextActorAction);
        labelOut = (TextView)rootView.findViewById(R.id.labelActorAction);

        adapterActorActions = new ArrayAdapter<String>(fragmentView, R.layout.text_spinner_layout, actorActions);
        spinnerActorAction.setAdapter(adapterActorActions);

        return rootView;
    }

    public WizardChoseRulesetActorAction(){}
    @SuppressLint("ValidFragment")
    public WizardChoseRulesetActorAction(WizardCreateNewRuleSet fragmentView)
    {
        this.fragmentView = fragmentView;
    }

    private void refillList(List<String> sourceList, List<String> toFill)
    {
        toFill.clear();
        for(int i = 0; i < sourceList.size(); i++)
            toFill.add(sourceList.get(i));
    }
}
