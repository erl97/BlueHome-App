package de.th_nuernberg.bluehome;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import de.th_nuernberg.bluehome.RuleProcessObjects.NodeInfo;

public class WizardChoseRulesetInitiatorActor extends Fragment {

    private Button btnContinue;
    private WizardCreateNewRuleSet fragmentView;
    private BlueHomeDevice sourceDevice;
    private BlueHomeDevice targetDevice;
    private ArrayAdapter<String> adapterActors;
    private ArrayAdapter<String> adapterInitiators;
    private Spinner actor;
    private Spinner initiator;
    private List<String> initiators = new ArrayList<>();
    private List<String> actors = new ArrayList<>();
    private NodeInfo nodeInfo;

    public void setTargetDevice(BlueHomeDevice dev)
    {
        targetDevice = dev;
        refillList(nodeInfo.getNodeActors(dev.getNodeType()), actors);
        adapterActors.notifyDataSetChanged();
    }
    public void setSourceDevice(BlueHomeDevice dev)
    {
        sourceDevice = dev;
        refillList(nodeInfo.getNodeInitiators(dev.getNodeType()), initiators);
        adapterInitiators.notifyDataSetChanged();
    }

    private void Continue()
    {
        fragmentView.rulesetInitiatorAction.setDevice(sourceDevice);
        fragmentView.rulesetInitiatorAction.setInitiator(initiators.get(initiator.getSelectedItemPosition()));
        fragmentView.rulesetActorAction.setTargetDevice(targetDevice);
        fragmentView.rulesetActorAction.setActor(nodeInfo.getNodeActors(targetDevice.getNodeType()).get(actor.getSelectedItemPosition()));

        fragmentView.mPager.setCurrentItem(fragmentView.mPager.getCurrentItem() + 1);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_wizard_chose_ruleset_initiator_actor, container, false);
        nodeInfo = new NodeInfo(fragmentView);
        btnContinue = (Button) rootView.findViewById(R.id.btnThirdStepContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Continue();
            }
        });
        adapterInitiators = new ArrayAdapter<String>(fragmentView, R.layout.text_spinner_layout, initiators);
        adapterActors = new ArrayAdapter<String>(fragmentView, R.layout.text_spinner_layout, actors);
        actor = (Spinner)rootView.findViewById(R.id.spinnerActors);
        initiator = (Spinner)rootView.findViewById(R.id.spinnerInitiators);
        actor.setAdapter(adapterActors);
        initiator.setAdapter(adapterInitiators);
        return rootView;
    }


    public WizardChoseRulesetInitiatorActor(){}
    @SuppressLint("ValidFragment")
    public WizardChoseRulesetInitiatorActor(WizardCreateNewRuleSet fragmentView)
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
