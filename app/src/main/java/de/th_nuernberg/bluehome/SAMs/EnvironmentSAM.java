package de.th_nuernberg.bluehome.SAMs;

import android.content.Context;

import java.util.List;

public class EnvironmentSAM implements SAM {

    private Context context;

    public EnvironmentSAM(Context context)
    {
        this.context = context;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasInput() {
        return false;
    }

    @Override
    public boolean hasOutput() {
        return false;
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
        return null;
    }

    @Override
    public List<String> getActorActions() {
        return null;
    }

    @Override
    public byte getSamId() {
        return 0x03;
    }

    @Override
    public byte getSourceID(String initiatorAction) {
        return 0;
    }

    @Override
    public byte getActionID(String actorAction) {
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
        return null;
    }

    @Override
    public byte[] getCompareParams(String selection) {
        return new byte[0];
    }

    @Override
    public int getMaxNum() {
        return 0;
    }

    @Override
    public int getMinNum() {
        return 0;
    }
}
