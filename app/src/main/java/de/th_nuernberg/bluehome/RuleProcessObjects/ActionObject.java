package de.th_nuernberg.bluehome.RuleProcessObjects;

/**
 * ActionObject is used in BlueHome Devices to trigger actions on the hardware. ActionObjects are created by the app (RuleCreator) and pushed to the Device while configuration.
 *
 * @author Philipp Herrmann
 */
public class ActionObject {
    private byte actionMemID;
    private byte actionSAM;
    private byte actionID;
    private int paramMask;
    private byte appActionID;
    private byte[] param = new byte[20];

    public ActionObject(){

    }

    public byte getActionMemID() {
        return actionMemID;
    }

    public void setActionMemID(byte actionMemID) {
        this.actionMemID = actionMemID;
    }

    public byte getActionSAM() {
        return actionSAM;
    }

    public void setActionSAM(byte actionSAM) {
        this.actionSAM = actionSAM;
    }

    public byte getActionID() {
        return actionID;
    }

    public void setActionID(byte actionID) {
        this.actionID = actionID;
    }

    public int getParamMask() {
        return paramMask;
    }

    public void setParamMask(int paramMask) {
        this.paramMask = paramMask;
    }

    public byte[] getParam() {
        return param;
    }

    public void setParam(byte[] param) {
        this.param = param;
    }

    public byte getMaskPart(int part){
        int msk = 0b11111111;
        return ((byte)((paramMask & (msk << (part * 8))) >> (part * 8)));
    }
}
