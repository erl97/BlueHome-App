package de.th_nuernberg.bluehome;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.th_nuernberg.bluehome.RuleProcessObjects.NodeInfo;

public class WizardChoseRulesetInitiatorAction extends Fragment {

    private Button btnContinue;
    private WizardCreateNewRuleSet fragmentView;
    public Spinner spinnerOperators;
    public Spinner spinnerActions;
    private TextView labelIn;
    public EditText valueIn;
    private ArrayAdapter<String> adapterInitiatorActions;
    private ArrayAdapter<String> adapterOperators;
    public List<String> actions = new ArrayList<>();
    public List<String> operators = new ArrayList<>();
    private BlueHomeDevice device;
    public String initiator;
    private NodeInfo nodeInfo;

    private void Continue()
    {
        if ((byte)Integer.parseInt(valueIn.getText().toString()) >= nodeInfo.getMinNum(device.getNodeType(), initiator) && (byte)Integer.parseInt(valueIn.getText().toString()) <= nodeInfo.getMaxNum(device.getNodeType(), initiator))
            fragmentView.mPager.setCurrentItem(fragmentView.mPager.getCurrentItem() + 1);
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

    public void setInitiator(String initiator)
    {
        this.initiator = initiator;
        if (device != null)
        {
            refillList(nodeInfo.getInitiatorOperations(device.getNodeType(), initiator), actions);
            refillList(nodeInfo.getOperators(device.getNodeType(), initiator), operators);
            labelIn.setText(nodeInfo.getInputValueLabel(device.getNodeType(), initiator));
            adapterOperators.notifyDataSetChanged();
            adapterInitiatorActions.notifyDataSetChanged();
        }
    }

    public void setDevice(BlueHomeDevice device)
    {
        this.device = device;
        if (initiator != null)
        {
            refillList(nodeInfo.getInitiatorOperations(device.getNodeType(), initiator), actions);
            refillList(nodeInfo.getOperators(device.getNodeType(), initiator), operators);
            adapterInitiatorActions.notifyDataSetChanged();
            adapterOperators.notifyDataSetChanged();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_wizard_chose_ruleset_initiator_action, container, false);
        btnContinue = (Button)rootView.findViewById(R.id.btnFourthStepContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Continue();
            }
        });
        nodeInfo = new NodeInfo(fragmentView);
        valueIn = (EditText)rootView.findViewById(R.id.editTextComparismValue);
        spinnerActions = (Spinner)rootView.findViewById(R.id.spinnerAction);
        spinnerOperators = (Spinner)rootView.findViewById(R.id.spinnerComparism);
        labelIn = (TextView)rootView.findViewById(R.id.lblIn);

        adapterInitiatorActions = new ArrayAdapter<String>(fragmentView, R.layout.text_spinner_layout, actions);
        spinnerActions.setAdapter(adapterInitiatorActions);

        adapterOperators = new ArrayAdapter<String>(fragmentView, R.layout.text_spinner_layout, operators);
        spinnerOperators.setAdapter(adapterOperators);


        return rootView;
    }

    public WizardChoseRulesetInitiatorAction(){}

    @SuppressLint("ValidFragment")
    public WizardChoseRulesetInitiatorAction(WizardCreateNewRuleSet fragmentView)
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
