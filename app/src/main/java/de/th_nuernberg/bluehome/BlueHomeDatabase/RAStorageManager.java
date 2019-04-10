package de.th_nuernberg.bluehome.BlueHomeDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import de.th_nuernberg.bluehome.BlueHomeDevice;
import de.th_nuernberg.bluehome.RuleProcessObjects.RuleObject;

/**
 * RAStorageManager (RuleActionStorageManager
 */
public class RAStorageManager extends SQLiteOpenHelper {

    public RAStorageManager(Context context){
        super(context, DBConstants.DATABASE_NAME , null, 1);
    }

    //rule section
    public RuleObject getRule(BlueHomeDevice dev, int ruleNum){
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

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
