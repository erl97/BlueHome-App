package de.th_nuernberg.bluehome.SAMs;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.th_nuernberg.bluehome.R;

public class LightSAM implements SAM {

    private Context context;

    public LightSAM(Context context)
    {
        this.context = context;
    }

    @Override
    public String getName() {
        return new String(context.getResources().getString(R.string.initiator_light));
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
        ArrayList<String> tmp = new ArrayList<>();

        return tmp;
    }

    @Override
    public List<String> getSourceActions() {
        ArrayList<String> tmp = new ArrayList<>();
        tmp.add(context.getResources().getString(R.string.threshold_value));
        return tmp;
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
        return 0x0B;
    }

    @Override
    public byte getSourceID(String initiatorAction) {
        return (byte)1;
    }

    @Override
    public byte getActionID(String actorAction) {
        return 0;
    }

    @Override
    public byte[] getSourceParams(byte toCompare) {
        Log.i("LightSAM", "got " + toCompare);
        byte[] tmp = new byte[19];
        for(int i = 0; i < 19; i++) {
            tmp[i] = 0;
        }
        long value = toCompare * 255;
        tmp[0] = (byte) (0x000000FF & value);
        tmp[1] = (byte) (0x000000FF & ( value >> 8));
        Log.i("LightSAM", "" + tmp[0]);


        return tmp;
    }

    @Override
    public byte[] getActionParams(byte value) {
        return new byte[0];
    }

    @Override
    public byte getCompareCode(String compareSign) {
        if(compareSign.equals("="))
            return 1;
        if(compareSign.equals("DC"))
            return 0;
        if(compareSign.equals("<"))
            return 3;
        if(compareSign.equals(">"))
            return 2;
        return 1;
    }

    @Override
    public List<String> getPossibleCompareSigns() {
        ArrayList<String> comps = new ArrayList<>();
        comps.add("=");
        comps.add("DC");
        comps.add("<");
        comps.add(">");
        return comps;
    }

    @Override
    public String getInputNumberLabel() {
        return new String(context.getResources().getString(R.string.threshold_value));
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
        if(selection.equals("<")) {
            param[0] = 3;
            param[1] = 3;
        }
        if(selection.equals(">")) {
            param[0] = 2;
            param[1] = 2;
        }

        return param;
    }

    @Override
    public int getMaxNum() {
        return 255;
    }

    @Override
    public int getMinNum() {
        return 0;
    }
}
