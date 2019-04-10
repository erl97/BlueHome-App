package de.th_nuernberg.bluehome.BlueHomeDatabase;

/**
 * DBConstants contains all constants for Database use.
 *
 * @author Philipp Herrmann
 */

public class DBConstants {
    public static final String DATABASE_NAME = "BlueHome.db";
    public static final String DEVICES_TABLE_NAME = "devices";
    public static final String DEVICES_COLUMN_SHOWN_NAME = "shown_name";
    public static final String DEVICES_COLUMN_REAL_NAME = "real_name";
    public static final String DEVICES_COLUMN_MAC = "mac";
    public static final String DEVICES_COLUMN_IMG_ID = "img_id";
    public static final String DEVICES_COLUMN_NODE_TYPE = "node_type";

    public static final String RULES_TABLE_NAME = "rules";
    public static final String RULES_COLUMN_RULE_MEM_ID = "rule_mem_id";
    public static final String RULES_COLUMN_ACTION_MEM_ID = "action_mem_id";
    public static final String RULES_COLUMN_PARAM_COMP_BASE = "param_comp_";
}
