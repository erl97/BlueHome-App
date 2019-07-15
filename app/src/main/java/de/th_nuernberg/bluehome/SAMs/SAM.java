package de.th_nuernberg.bluehome.SAMs;

import android.content.Context;

import java.util.List;

public interface SAM {
    public String getName();
    public boolean hasInput();
    public boolean hasOutput();
    public List<String> getSources();
    public List<String> getSourceActions();
    public List<String> getActors();
    public List<String> getActorActions();
    public byte getSamId();
    public byte getSourceID(String initiatorAction);
    public byte getActionID(String actorAction);
    public byte[] getSourceParams(byte toCompare);
    public byte[] getActionParams(byte value);
    public byte getCompareCode(String compareSign);
    public List<String> getPossibleCompareSigns();
    public String getInputNumberLabel();
    public String getOutputNumberLabel();
    public byte[] getCompareParams(String selection);
    public int getMaxNum();
    public int getMinNum();
}
