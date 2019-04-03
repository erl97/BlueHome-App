package de.th_nuernberg.bluehome.RuleProcessObjects;

/**
 * Source Object is the Object that is generated when an interrupt on a BlueHome Device occours. In the App, it is needed for comparing in {@link RuleObject}
 *
 * @author Philipp Herrmann
 */

public class SourceObject {
    private byte sourceSAM;
    private byte sourceID;
    private byte[] param = new byte[20];

    public byte getSourceSAM() {
        return sourceSAM;
    }

    public void setSourceSAM(byte sourceSAM) {
        this.sourceSAM = sourceSAM;
    }

    public byte getSourceID() {
        return sourceID;
    }

    public void setSourceID(byte sourceID) {
        this.sourceID = sourceID;
    }

    public byte[] getParams() {
        return param;
    }

    public void setParams(byte[] param) {
        this.param = param;
    }

    public int setParam(int paramNum, byte paramValue) {
        if(this.param != null && paramNum < this.param.length) {
            this.param[paramNum] = paramValue;
            return RPC.RP_OK;
        } else {
            return RPC.RP_FAIL;
        }
    }

    public byte getParam(int paramNum) {
        if(paramNum < this.param.length) {
            return this.param[paramNum];
        } else {
            return 0;
        }
    }

    public SourceObject(){

    }


}
