package de.th_nuernberg.bluehome.BlueHomeDatabase;

import java.util.ArrayList;

import de.th_nuernberg.bluehome.BlueHomeDevice;
import de.th_nuernberg.bluehome.RuleProcessObjects.RuleObject;

/**
 * RAStorageManager (RuleActionStorageManager
 */
public class RAStorageManager {

    public void RAStorageManager(){

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


}
