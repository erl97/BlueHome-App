package de.th_nuernberg.bluehome.RuleProcessObjects;

/**
 * RPC (RuleProcessConstants) Contains all defines for Rule Process
 *
 * @author Philipp Herrmann
 */

public class RPC {
    public final static int RP_OK = 0;
    public final static int RP_FAIL = -1;

    public final static int COMP_DOESNT_MATTER = 0;
    public final static int COMP_EQUALS = 1;
    public final static int COMP_GREATER_THAN = 2;
    public final static int COMP_GREATER_EQUAL = 3;
    public final static int COMP_SMALLER_THAN = 4;
    public final static int COMP_SMALLER_EQUAL = 5;

    public final static int SAM_UNKNOWN = 0;
    public final static int SAM_BLUETOOTH = 1;
    public final static int SAM_RTC_ = 2;
    public final static int SAM_ENVIRONMENT = 3;
    public final static int SAM_DISPLAY = 4;
    public final static int SAM_BUTTON = 5;
    public final static int SAM_PWM = 6;
    public final static int SAM_RELAY = 7;
    public final static int SAM_DIO = 8;
}
