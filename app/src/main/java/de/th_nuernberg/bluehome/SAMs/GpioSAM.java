package de.th_nuernberg.bluehome.SAMs;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import de.th_nuernberg.bluehome.R;

public class GpioSAM implements SAM {

    private Context context;

    public GpioSAM(Context context)
    {
        this.context = context;
    }

    @Override
    public String getName() {
        return context.getResources().getString(R.string.gpio);
    }

    @Override
    public boolean hasInput() {
        return true;
    }

    @Override
    public boolean hasOutput() {
        return true;
    }

    @Override
    public List<String> getSources() {
        ArrayList<String> sources = new ArrayList<>();
        sources.add(Resources.getSystem().getString(R.string.initiator_input_1));
        sources.add(Resources.getSystem().getString(R.string.initiator_input_2));
        sources.add(Resources.getSystem().getString(R.string.initiator_input_3));
        sources.add(Resources.getSystem().getString(R.string.initiator_input_4));
        sources.add(Resources.getSystem().getString(R.string.initiator_input_5));
        sources.add(Resources.getSystem().getString(R.string.initiator_input_6));
        sources.add(Resources.getSystem().getString(R.string.initiator_input_7));
        sources.add(Resources.getSystem().getString(R.string.initiator_input_8));
        return sources;
    }

    @Override
    public List<String> getSourceActions() {
        ArrayList<String> actions = new ArrayList<>();
        actions.add(context.getResources().getString(R.string.falling_edge));
        actions.add(context.getResources().getString(R.string.rising_edge));
        return actions;
    }

    @Override
    public List<String> getActors() {
        ArrayList<String> actors = new ArrayList<>();
        actors.add(Resources.getSystem().getString(R.string.output_1));
        actors.add(Resources.getSystem().getString(R.string.output_2));
        actors.add(Resources.getSystem().getString(R.string.output_3));
        actors.add(Resources.getSystem().getString(R.string.output_4));
        actors.add(Resources.getSystem().getString(R.string.output_5));
        actors.add(Resources.getSystem().getString(R.string.output_6));
        actors.add(Resources.getSystem().getString(R.string.output_7));
        actors.add(Resources.getSystem().getString(R.string.output_8));
        return actors;
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
        return 0x08;
    }

    @Override
    public byte getSourceID(String initiatorAction) {
        if(initiatorAction.equals(Resources.getSystem().getString(R.string.falling_edge)))
            return 0x02;
        if(initiatorAction.equals(Resources.getSystem().getString(R.string.rising_edge)))
            return 0x01;
        return 0;
    }

    @Override
    public byte getActionID(String actorAction) {
        if(actorAction.equals(context.getResources().getString(R.string.switch_on)))
            return 0x02;
        if(actorAction.equals(context.getResources().getString(R.string.switch_off)))
            return 0x03;
        if(actorAction.equals(context.getResources().getString(R.string.toggle)))
            return 0x01;
        return 0;
    }

    @Override
    public byte[] getSourceParams(byte toCompare) {
        byte[] params = new byte[19];
        params[0] = toCompare;
        return params;
    }

    @Override
    public byte[] getActionParams(byte value) {
        byte[] params = new byte[19];
        params[0] = value;
        return params;
    }

    @Override
    public byte getCompareCode(String compareSign) {
        if(compareSign.equals("="))
            return 1;
        if(compareSign.equals("DC"))
            return 0;
        return 1;
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
        return context.getResources().getString(R.string.comp_lbl_gpi);
    }

    @Override
    public String getOutputNumberLabel() {
        return context.getResources().getString(R.string.comp_lbl_gpo);
    }

    @Override
    public byte[] getCompareParams(String selection) {
        byte[] param = new byte[19];

        if(selection.equals("DC")) {
            param[0] = 0;
        }
        if(selection.equals("=")) {
            param[0] = 1;
        }

        return param;
    }

    @Override
    public int getMaxNum() {
        return 7;
    }

    @Override
    public int getMinNum() {
        return 0;
    }

}
