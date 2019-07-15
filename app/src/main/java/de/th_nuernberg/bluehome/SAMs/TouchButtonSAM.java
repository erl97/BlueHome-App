package de.th_nuernberg.bluehome.SAMs;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import de.th_nuernberg.bluehome.R;

public class TouchButtonSAM implements SAM {

    private Context context;

    public TouchButtonSAM(Context context)
    {
        this.context = context;
    }

    @Override
    public String getName() {
        return context.getResources().getString(R.string.touch_button);
    }

    @Override
    public boolean hasInput() {
        return true;
    }

    @Override
    public boolean hasOutput() {
        return false;
    }

    @Override
    public List<String> getSources() {
        return new ArrayList<String>();
    }

    @Override
    public List<String> getSourceActions() {
        ArrayList<String> actions = new ArrayList<>();
        actions.add(context.getResources().getString(R.string.touch_button_action_click));
        actions.add(context.getResources().getString(R.string.touch_button_action_long_click));
        //actions.add(context.getResources().getString(R.string.touch_button_action_pressed));
        //actions.add(context.getResources().getString(R.string.touch_button_action_released));
        return actions;
    }

    @Override
    public List<String> getActors() {
        return null;
    }

    @Override
    public List<String> getActorActions() {
        return null;
    }

    @Override
    public byte getSamId() {
        return 0x05;
    }

    @Override
    public byte getSourceID(String initiatorAction) {
        /*if(initiatorAction.equals(context.getResources().getString(R.string.touch_button_action_pressed)))
            return 0x00;
        if(initiatorAction.equals(context.getResources().getString(R.string.touch_button_action_released)))
            return 0x00;*/
        if(initiatorAction.equals(context.getResources().getString(R.string.touch_button_action_click)))
            return 0x04;
        if(initiatorAction.equals(context.getResources().getString(R.string.touch_button_action_long_click)))
            return 0x02;
        return 0;
    }

    @Override
    public byte getActionID(String actorAction) {
        return 0;
    }

    @Override
    public byte[] getSourceParams(byte toCompare) {
        byte[] params = new byte[19];
        switch (toCompare)
        {
            case 1:
                params[0] = 0x00;
                params[1] = 0x02;
                break;
            case 2:
                params[0] = 0x00;
                params[1] = 0x04;
                break;
            case 3:
                params[0] = 0x02;
                params[1] = 0x00;
                break;
            case 4:
                params[0] = 0x04;
                params[1] = 0x00;
                break;
        }


        return params;
    }

    @Override
    public byte[] getActionParams(byte value) {
        return new byte[0];
    }

    @Override
    public byte getCompareCode(String compareSign) {
        return 0;
    }

    @Override
    public List<String> getPossibleCompareSigns() {
        ArrayList<String> comps = new ArrayList<>();
        comps.add("=");
        comps.add("DC");
        return comps;
    }

    @Override
    public String getInputNumberLabel() {
        return context.getResources().getString(R.string.button_number);
    }

    @Override
    public String getOutputNumberLabel() {
        return null;
    }

    @Override
    public byte[] getCompareParams(String selection) {

        byte[] param = new byte[19];

        if(selection.equals("DC")) {
            param[0] = 0;
            param[1] = 0;
        }
        if(selection.equals("=")) {
            param[0] = 1;
            param[1] = 1;
        }

        return param;
    }

    @Override
    public int getMaxNum() {
        return 4;
    }

    @Override
    public int getMinNum() {
        return 1;
    }
}
