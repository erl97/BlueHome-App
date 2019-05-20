package de.th_nuernberg.bluehome.BlueHomeDatabase;

/**
 * DBConstants contains all constants for Database use.
 *
 * @author Philipp Herrmann
 */

public class DBConstants {
    public static final String DATABASE_NAME                    = "BlueHome.db";
    public static final String DEVICES_TABLE_NAME               = "devices";
    public static final String DEVICES_COLUMN_SHOWN_NAME        = "shown_name";
    public static final String DEVICES_COLUMN_REAL_NAME         = "real_name";
    public static final String DEVICES_COLUMN_MAC               = "mac";
    public static final String DEVICES_COLUMN_IMG_ID            = "img_id";
    public static final String DEVICES_COLUMN_NODE_TYPE         = "node_type";

    public static final String RULES_TABLE_NAME                 = "rules";
    public static final String RULES_COLUMN_APP_RULE_ID         = "app_rule_id";
    public static final String RULES_COLUMN_RULE_MEM_ID         = "rule_mem_id";
    public static final String RULES_COLUMN_ACTION_MEM_ID       = "action_mem_id";
    public static final String RULES_COLUMN_PARAM_COMP          = "param_comp";
    public static final String RULES_COLUMN_SOURCE_SAM          = "source_sam";
    public static final String RULES_COLUMN_SOURCE_ID           = "source_id";
    public static final String RULES_COLUMN_SOURCE_PARAM        = "source_param";

    public static final String RULESET_TABLE_NAME               = "rulesets";
    public static final String RULESET_COLUMN_RULESET_ID        = "ruleset_id";
    public static final String RULESET_COLUMN_APP_ACTION_1_ID   = "app_action_1_id";
    public static final String RULESET_COLUMN_APP_ACITON_2_ID   = "app_action_2_id";
    public static final String RULESET_COLUMN_APP_RULE_1_ID     = "app_rule_1_id";
    public static final String RULESET_COLUMN_APP_RULE_2_ID     = "app_rule_2_id";
    public static final String RULESET_COLUMN_DEV_1_MAC         = "dev_1_mac";
    public static final String RULESET_COLUMN_DEV_2_MAC         = "dev_2_mac";
    public static final String RULESET_COLUMN_NAME              = "name";

    public static final String ACTIONS_TABLE_NAME               = "actions";
    public static final String ACTIONS_COLUMN_APP_ACTION_ID     = "app_action_id";
    public static final String ACTIONS_COLUMN_MEM_ID            = "action_mem_id";
    public static final String ACTIONS_COLUMN_SAM               = "action_sam";
    public static final String ACTIONS_COLUMN_ID                = "action_id";
    public static final String ACTIONS_COLUMN_PARAM_MASK        = "param_mask";
    public static final String ACTIONS_COLUMN_PARAMS            = "params";

}

