package de.th_nuernberg.bluehome.SAMs;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import de.th_nuernberg.bluehome.R;

public class BuzzerSAM implements SAM {

    private Context context;

    public BuzzerSAM(Context context)
    {
        this.context = context;
    }

    @Override
    public String getName() {
        return context.getResources().getString(R.string.buzzer);
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
        ArrayList<String> actors = new ArrayList<>();
        return actors;
    }

    @Override
    public List<String> getActorActions() {
        ArrayList<String> actions = new ArrayList<>();
        actions.add(context.getResources().getString(R.string.short_beep));
        actions.add(context.getResources().getString(R.string.start));
        actions.add(context.getResources().getString(R.string.stop));
        return actions;
    }

    @Override
    public byte getSamId() {
        return 0x0A;
    }

    @Override
    public byte getSourceID(String initiatorAction) {
        return 0;
    }

    @Override
    public byte getActionID(String actorAction) {
        if(actorAction.equals(context.getResources().getString(R.string.short_beep)))
            return 0x01;
        if(actorAction.equals(context.getResources().getString(R.string.start)))
            return 0x02;
        if(actorAction.equals(context.getResources().getString(R.string.stop)))
            return 0x03;
        
        return 0;

    }

    @Override
    public byte[] getSourceParams(byte toCompare) {
        return new byte[0];
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
        return null;
    }

    @Override
    public String getInputNumberLabel() {
        return null;
    }

    @Override
    public String getOutputNumberLabel() {
        return context.getResources().getString(R.string.tone_height);
    }

    @Override
    public byte[] getCompareParams(String selection) {
        return new byte[0];
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
