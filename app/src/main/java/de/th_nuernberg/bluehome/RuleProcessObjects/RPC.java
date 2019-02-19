package de.th_nuernberg.bluehome.RuleProcessObjects;

/**
 * RPC (RuleProcessConstants) Contains all defines for Rule Process
 *
 * @author Philipp Herrmann
 */

public class RPC {
    public final static int RP_OK = 0;
    public final static int RP_FAIL = -1;

    public final static byte COMP_DOESNT_MATTER =    0;
    public final static byte COMP_EQUALS =           1;
    public final static byte COMP_GREATER_THAN =     2;
    public final static byte COMP_GREATER_EQUAL =    3;
    public final static byte COMP_SMALLER_THAN =     4;
    public final static byte COMP_SMALLER_EQUAL =    5;

    public final static byte SAM_UNKNOWN =       0;
    public final static byte SAM_BLUETOOTH =     1;
    public final static byte SAM_RTC_ =          2;
    public final static byte SAM_ENVIRONMENT =   3;
    public final static byte SAM_DISPLAY =       4;
    public final static byte SAM_BUTTON =        5;
    public final static byte SAM_PWM =           6;
    public final static byte SAM_RELAY =         7;
    public final static byte SAM_DIO =           8;
    public final static byte SAM_PROG =          9;

    public final static byte PROG_ACT_ID_WRITE_RULE =    1;
    public final static byte PROG_ACT_ID_WRITE_ACTION =  2;

}
