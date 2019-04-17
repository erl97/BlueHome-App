package de.th_nuernberg.bluehome.BlueHomeDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import de.th_nuernberg.bluehome.BlueHomeDevice;
import de.th_nuernberg.bluehome.RuleProcessObjects.RuleObject;

/**
 * RuleSetStorageManager (RuleActionStorageManager
 */
public class RuleSetStorageManager extends SQLiteOpenHelper {

    public RuleSetStorageManager(Context context){
        super(context, DBConstants.DATABASE_NAME , null, 1);
    }

    //rule section
    public RuleObject getRuleSet(BlueHomeDevice dev, int ruleNum){
        RuleObject tmp = new RuleObject();

        return tmp;
    }

    public ArrayList<RuleObject> getAllRulesFor(BlueHomeDevice dev){
        ArrayList<RuleObject> rules = new ArrayList<>();

        return rules;
    }

    public ArrayList<RuleObject> getAllRules(){
        ArrayList<RuleObject> rules = new ArrayList<>();

        return rules;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " +
                        DBConstants.RULESET_TABLE_NAME +
                        " (" +
                        DBConstants.RULESET_COLUMN_RULESET_ID +
                        " integer primary key," +
                        DBConstants.RULESET_COLUMN_APP_RULE_1_ID +
                        " integer," +
                        DBConstants.RULESET_COLUMN_APP_RULE_2_ID +
                        " integer," +
                        DBConstants.RULESET_COLUMN_APP_ACTION_1_ID +
                        " integer," +
                        DBConstants.RULESET_COLUMN_APP_ACITON_2_ID +
                        " integer," +
                        DBConstants.RULESET_COLUMN_DEV_1_MAC +
                        " text," +
                        DBConstants.RULESET_COLUMN_DEV_2_MAC +
                        " text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
