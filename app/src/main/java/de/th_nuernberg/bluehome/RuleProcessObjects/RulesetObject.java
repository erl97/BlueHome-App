package de.th_nuernberg.bluehome.RuleProcessObjects;

import de.th_nuernberg.bluehome.BlueHomeDevice;

public class RulesetObject {
    private byte rulesetID;
    private ActionObject action1;
    private ActionObject action2;
    private RuleObject rule1;
    private RuleObject rule2;
    private BlueHomeDevice dev1;
    private BlueHomeDevice dev2;

    public byte getRulesetID() {
        return rulesetID;
    }

    public void setRulesetID(byte rulesetID) {
        this.rulesetID = rulesetID;
    }

    public ActionObject getAction1() {
        return action1;
    }

    public void setAction1(ActionObject action1) {
        this.action1 = action1;
    }

    public ActionObject getAction2() {
        return action2;
    }

    public void setAction2(ActionObject action2) {
        this.action2 = action2;
    }

    public RuleObject getRule1() {
        return rule1;
    }

    public void setRule1(RuleObject rule1) {
        this.rule1 = rule1;
    }

    public RuleObject getRule2() {
        return rule2;
    }

    public void setRule2(RuleObject rule2) {
        this.rule2 = rule2;
    }

    public BlueHomeDevice getDev1() {
        return dev1;
    }

    public void setDev1(BlueHomeDevice dev1) {
        this.dev1 = dev1;
    }

    public BlueHomeDevice getDev2() {
        return dev2;
    }

    public void setDev2(BlueHomeDevice dev2) {
        this.dev2 = dev2;
    }
}
