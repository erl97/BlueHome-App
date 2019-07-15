package de.th_nuernberg.bluehome.SAMs;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import de.th_nuernberg.bluehome.R;

public class RelaisSAM implements SAM {

    private Context context;

    public RelaisSAM(Context context)
    {
        this.context = context;
    }

    @Override
    public String getName() {
        return context.getResources().getString(R.string.relais);
    }

    @Override
    public boolean hasInput() {
        return false;
    }

    @Override
    public boolean hasOutput() {
        return true;
    }

    @Override
    public List<String> getSources() {
        return null;
    }

    @Override
    public List<String> getSourceActions() {
        return null;
    }

    @Override
    public List<String> getActors() {
        return new ArrayList<>();
    }

    @Override
    public List<String> getActorActions() {
        ArrayList<String> actions = new ArrayList<>();
        actions.add(context.getResources().getString(R.string.switch_on));
        actions.add(context.getResources().getString(R.string.switch_off));
        actions.add(context.getResources().getString(R.string.toggle));
        return actions;
    }

    @Override
    public byte getSamId() {
        return 0x07;
    }

    @Override
    public byte getSourceID(String initiatorAction) {
        return 0;
    }

    @Override
    public byte getActionID(String actorAction) {
        if(actorAction.equals(context.getResources().getString(R.string.switch_on)))
            return 0x02;
        if(actorAction.equals(context.getResources().getString(R.string.toggle)))
            return 0x01;
        if(actorAction.equals(context.getResources().getString(R.string.switch_off)))
            return 0x03;

        return 0;
    }

    @Override
    public byte[] getSourceParams(byte toCompare) {
        return new byte[0];
    }

    @Override
    public byte[] getActionParams(byte value) {
        byte[] param = new byte[19];
        param[0] = value;
        return param;
    }

    @Override
    public byte getCompareCode(String compareSign) {
        return 0;
    }

    @Override
    public List<String> getPossibleCompareSigns() {
        return null;
    }

    @Override
    public String getInputNumberLabel() {
        return null;
    }

    @Override
    public String getOutputNumberLabel() {
        return context.getResources().getString(R.string.relay_number);
    }

    @Override
    public byte[] getCompareParams(String selection) {
        return new byte[0];
    }

    @Override
    public int getMaxNum() {
        return 3;
    }

    @Override
    public int getMinNum() {
        return 0;
    }
}
