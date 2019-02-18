package de.th_nuernberg.bluehome.RuleProcessObjects;

/**
 * ActionObject is used in BlueHome Devices to trigger actions on the hardware. ActionObjects are created by the app (RuleCreator) and pushed to the Device while configuration.
 *
 * @author Philipp Herrmann
 */
public class ActionObject {
    private byte actionSAM;
    private byte actionID;
    private int paramMask;
    private byte paramNum;
    private byte[] param = new byte[20];
}
