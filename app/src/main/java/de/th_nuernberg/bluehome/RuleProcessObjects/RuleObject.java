package de.th_nuernberg.bluehome.RuleProcessObjects;

/**
 * RuleObject is used to descripe dependencies between {@link SourceObject} and {@link ActionObject}.
 * It contains a SourceObjcect to compare Against as well as all compare cases.
 *
 * @author Philipp Herrmann
 */

public class RuleObject {

    private byte ruleMemID;
    private byte actionMemID;
    private SourceObject toComp;

    public byte getAppRuleID() {
        return appRuleID;
    }

    public void setAppRuleID(byte appRuleID) {
        this.appRuleID = appRuleID;
    }

    private byte appRuleID;
    private byte[] paramComp = new byte[20];


    /**
     * gets Rule ID. This ID identifies the position of the Rule in devices memory
     *
     * @return rule ID
     */
    public byte getRuleMemID() {
        return ruleMemID;
    }

    /**
     * sets Rule ID. This ID identifies the position of the Rule in devices memory
     *
     * @param ruleMemID rule ID
     */
    public void setRuleMemID(byte ruleMemID) {
        this.ruleMemID = ruleMemID;
    }

    /**
     * gets Action Memory ID. Action Memory ID descripes where to put the action packet in BlueHome devices memory.
     *
     * @return Action Memory ID
     */
    public byte getActionMemID() {
        return actionMemID;
    }

    /**
     * sets Action Memory ID. Action Memory ID descripes where to put the action packet in BlueHome devices memory.
     * @param actionMemID Action Memory ID
     */
    public void setActionMemID(byte actionMemID) {
        this.actionMemID = actionMemID;
    }

    /**
     * gets the {@link SourceObject} to compare against.
     *
     * @return SourceObject to compare against
     */
    public SourceObject getToComp() {
        return toComp;
    }

    /**
     * sets the {@link SourceObject} to compare against.
     *
     * @param toComp SourceObject to compare against
     */
    public void setToComp(SourceObject toComp) {
        this.toComp = toComp;
    }

    /**
     * gets the whole bytearray of compare cases. For Cases see {@link RPC}.
     *
     * @return bytearray of compare cases
     */
    public byte[] getParamComp() {
        return paramComp;
    }

    /**
     * sets the whole bytearray of compare cases. For Cases see {@link RPC}.
     *
     * @param paramComp bytearray of compare cases
     */
    public void setParamComp(byte[] paramComp) {
        this.paramComp = paramComp;
    }

    public RuleObject(){

    }


}
