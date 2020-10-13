package de.th_nuernberg.bluehome;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import javax.xml.transform.Source;

import de.th_nuernberg.bluehome.BlueHomeDatabase.RuleSetStorageManager;
import de.th_nuernberg.bluehome.RuleProcessObjects.ActionObject;
import de.th_nuernberg.bluehome.RuleProcessObjects.NodeInfo;
import de.th_nuernberg.bluehome.RuleProcessObjects.RuleObject;
import de.th_nuernberg.bluehome.RuleProcessObjects.RulesetObject;
import de.th_nuernberg.bluehome.RuleProcessObjects.SourceObject;
import de.th_nuernberg.bluehome.SAMs.NonSwipeableViewPager;

public class WizardCreateNewRuleSet extends FragmentActivity {

    private NodeInfo nodeInfo;
    public WizardChoseRuleSetName rulesetName = new WizardChoseRuleSetName(this);
    public WizardChoseRulesetSource rulesetSource = new WizardChoseRulesetSource(this);
    public WizardChoseRulesetInitiatorActor rulesetInitiatorActor = new WizardChoseRulesetInitiatorActor(this);
    public WizardChoseRulesetInitiatorAction rulesetInitiatorAction = new WizardChoseRulesetInitiatorAction(this);
    public WizardChoseRulesetActorAction rulesetActorAction = new WizardChoseRulesetActorAction(this);
    private static final int NUM_PAGES = 5;
    public NonSwipeableViewPager mPager;
    private PagerAdapter pagerAdapter;

    public void createRuleSet()
    {
        RuleSetStorageManager ruleSetStorageManager = new RuleSetStorageManager(this);
        RulesetObject newRuleSet = new RulesetObject();
        newRuleSet.setName(rulesetName.name.getText().toString());
        newRuleSet.setRule1(new RuleObject());
        newRuleSet.setRulesetID(ruleSetStorageManager.getNextFreeRulesetId());
        if (rulesetSource.devs.get(rulesetSource.source_device.getSelectedItemPosition() - 1).getMacAddress().equals(rulesetSource.devs.get(rulesetSource.target_device.getSelectedItemPosition() - 1).getMacAddress()))
        {
            newRuleSet.setDev1(rulesetSource.devs.get(rulesetSource.source_device.getSelectedItemPosition() - 1));
            newRuleSet.setAction1(new ActionObject());
            newRuleSet.getRule1().setToComp(new SourceObject());

            newRuleSet.getAction1().setActionID(nodeInfo.getActionID(rulesetSource.devs.get(rulesetSource.source_device.getSelectedItemPosition() - 1).getNodeType(), rulesetActorAction.actor, rulesetActorAction.actorActions.get(rulesetActorAction.spinnerActorAction.getSelectedItemPosition())));
            newRuleSet.getAction1().setActionMemID(ruleSetStorageManager.getNextFreeActionMemId(newRuleSet.getDev1(), (byte) 0));
            newRuleSet.getAction1().setAppActionID(ruleSetStorageManager.getNextAppActionId((byte) 0));
            newRuleSet.getAction1().setParamMask(0);
            newRuleSet.getAction1().setActionSAM(nodeInfo.getSamID(rulesetSource.devs.get(rulesetSource.source_device.getSelectedItemPosition() - 1).getNodeType(), rulesetActorAction.actor));



            newRuleSet.getRule1().setParamComp(nodeInfo.getParamCompID(rulesetSource.devs.get(rulesetSource.source_device.getSelectedItemPosition() - 1).getNodeType(), rulesetInitiatorAction.initiator, rulesetInitiatorAction.operators.get(rulesetInitiatorAction.spinnerOperators.getSelectedItemPosition())));
            newRuleSet.getRule1().setRuleMemID(ruleSetStorageManager.getNextFreeRuleMemId(newRuleSet.getDev1(), (byte) 0));
            newRuleSet.getRule1().setActionMemID(newRuleSet.getAction1().getActionMemID());
            newRuleSet.getRule1().setAppRuleID(ruleSetStorageManager.getNextAppRuleId((byte) 0));
            newRuleSet.getRule1().getToComp().setSourceSAM(nodeInfo.getSamID(rulesetSource.devs.get(rulesetSource.source_device.getSelectedItemPosition() - 1).getNodeType(), rulesetInitiatorAction.initiator));
            newRuleSet.getRule1().getToComp().setSourceID(nodeInfo.getSourceID(rulesetSource.devs.get(rulesetSource.source_device.getSelectedItemPosition() - 1).getNodeType(), rulesetInitiatorAction.initiator, rulesetInitiatorAction.actions.get(rulesetInitiatorAction.spinnerActions.getSelectedItemPosition())));
            newRuleSet.getRule1().getToComp().setParams(nodeInfo.getParam(rulesetSource.devs.get(rulesetSource.source_device.getSelectedItemPosition() - 1).getNodeType(), rulesetInitiatorAction.initiator, (byte) Integer.parseInt(rulesetInitiatorAction.valueIn.getText().toString())));
        } else {
            newRuleSet.setRule2(new RuleObject());
            newRuleSet.setAction1(new ActionObject());
            newRuleSet.setAction2(new ActionObject());
            newRuleSet.setRule1(new RuleObject());
            newRuleSet.setRule2(new RuleObject());
            newRuleSet.getRule1().setToComp(new SourceObject());
            newRuleSet.getRule2().setToComp(new SourceObject());

            newRuleSet.setDev1(rulesetSource.devs.get(rulesetSource.source_device.getSelectedItemPosition() - 1));
            newRuleSet.setDev2(rulesetSource.devs.get(rulesetSource.target_device.getSelectedItemPosition() - 1));

            newRuleSet.getAction1().setActionID(newRuleSet.getDev2().getMacId());
            newRuleSet.getAction1().setActionMemID(ruleSetStorageManager.getNextFreeActionMemId(newRuleSet.getDev1(), (byte) 0));
            newRuleSet.getAction1().setAppActionID(ruleSetStorageManager.getNextAppActionId((byte) 0));
            newRuleSet.getAction1().setParam(newRuleSet.getAction1().getAppActionID(), 0);
            newRuleSet.getAction1().setParamMask(0);
            newRuleSet.getAction1().setActionSAM((byte) 0x01);
            newRuleSet.getAction2().setActionID(nodeInfo.getActionID((rulesetSource.devs.get(rulesetSource.target_device.getSelectedItemPosition() - 1)).getNodeType(), rulesetActorAction.actor, rulesetActorAction.actorActions.get(rulesetActorAction.spinnerActorAction.getSelectedItemPosition())));
            newRuleSet.getAction2().setActionMemID(ruleSetStorageManager.getNextFreeActionMemId(newRuleSet.getDev2(), (byte) 0));
            newRuleSet.getAction2().setAppActionID(ruleSetStorageManager.getNextAppActionId((byte) (newRuleSet.getAction1().getAppActionID() + 1)));
            newRuleSet.getAction2().setParam((byte) Integer.parseInt(rulesetActorAction.valueOut.getText().toString()), 0);
            newRuleSet.getAction2().setParamMask(0);
            newRuleSet.getAction2().setActionSAM(nodeInfo.getSamID(rulesetSource.devs.get(rulesetSource.target_device.getSelectedItemPosition() - 1).getNodeType(), rulesetActorAction.actor));

            newRuleSet.getRule1().setParamComp(nodeInfo.getParamCompID(rulesetSource.devs.get(rulesetSource.source_device.getSelectedItemPosition() - 1).getNodeType(), rulesetInitiatorAction.initiator, rulesetInitiatorAction.operators.get(rulesetInitiatorAction.spinnerOperators.getSelectedItemPosition())));
            newRuleSet.getRule1().setRuleMemID(ruleSetStorageManager.getNextFreeRuleMemId(newRuleSet.getDev1(), (byte) 0));
            newRuleSet.getRule1().setActionMemID(newRuleSet.getAction1().getActionMemID());
            newRuleSet.getRule1().setAppRuleID(ruleSetStorageManager.getNextAppRuleId((byte) 0));
            newRuleSet.getRule1().getToComp().setSourceSAM(nodeInfo.getSamID(rulesetSource.devs.get(rulesetSource.source_device.getSelectedItemPosition() - 1).getNodeType(), rulesetInitiatorAction.initiator));
            newRuleSet.getRule1().getToComp().setSourceID(nodeInfo.getSourceID(rulesetSource.devs.get(rulesetSource.source_device.getSelectedItemPosition() - 1).getNodeType(), rulesetInitiatorAction.initiator, rulesetInitiatorAction.actions.get(rulesetInitiatorAction.spinnerActions.getSelectedItemPosition())));
            newRuleSet.getRule1().getToComp().setParams(nodeInfo.getParam(rulesetSource.devs.get(rulesetSource.source_device.getSelectedItemPosition() - 1).getNodeType(), rulesetInitiatorAction.initiator, (byte) Integer.parseInt(rulesetInitiatorAction.valueIn.getText().toString())));


            newRuleSet.getRule2().setParamComp((byte) 0x01, 0x00);
            newRuleSet.getRule2().setRuleMemID(ruleSetStorageManager.getNextFreeRuleMemId(newRuleSet.getDev2(), (byte) 0));
            newRuleSet.getRule2().setActionMemID(newRuleSet.getAction2().getActionMemID());
            newRuleSet.getRule2().setAppRuleID(ruleSetStorageManager.getNextAppRuleId((byte) (newRuleSet.getRule1().getAppRuleID() + 1)));
            newRuleSet.getRule2().getToComp().setSourceID(newRuleSet.getDev1().getMacId());
            newRuleSet.getRule2().getToComp().setSourceSAM((byte) 0x01);
            newRuleSet.getRule2().getToComp().setParam(0, newRuleSet.getAction1().getAppActionID());
        }
        ruleSetStorageManager.insertRuleset(newRuleSet);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard_ruleset);
        nodeInfo = new NodeInfo(this);
        mPager = (NonSwipeableViewPager) findViewById(R.id.pager);
        mPager.setOffscreenPageLimit(5);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), this);
        mPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onBackPressed()
    {
        if (mPager.getCurrentItem() == 0)
        {
            super.onBackPressed();
        }
        else
        {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private WizardCreateNewRuleSet fragmentActivity;
        public ScreenSlidePagerAdapter(FragmentManager fm, WizardCreateNewRuleSet fragmentActivity) {
            super(fm);

            this.fragmentActivity = fragmentActivity;
        }
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return fragmentActivity.rulesetName;
            }
            else if (position == 1)
            {
                return fragmentActivity.rulesetSource;
            }
            else if (position == 2)
            {
                return fragmentActivity.rulesetInitiatorActor;
            }
            else if (position == 3)
            {
                return fragmentActivity.rulesetInitiatorAction;
            }
            else if (position == 4)
            {
                return fragmentActivity.rulesetActorAction;
            }
            else
            {
                return fragmentActivity.rulesetSource;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}
