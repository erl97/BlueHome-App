package de.th_nuernberg.bluehome.RuleProcessObjects;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import de.th_nuernberg.bluehome.SAMs.BuzzerSAM;
import de.th_nuernberg.bluehome.SAMs.GpioSAM;
import de.th_nuernberg.bluehome.SAMs.LightSAM;
import de.th_nuernberg.bluehome.SAMs.RelaisSAM;
import de.th_nuernberg.bluehome.SAMs.SAM;
import de.th_nuernberg.bluehome.SAMs.TouchButtonSAM;

public class NodeInfo {

    private ArrayList<SAM> node1SAMs;
    private ArrayList<SAM> node2SAMs;
    private ArrayList<SAM> node3SAMs;

    private ArrayList<ArrayList<SAM>> SAMs;

    private final static int MAX_NODES = 3;

    public NodeInfo(Context context)
    {
        node1SAMs = new ArrayList<>();
        node2SAMs = new ArrayList<>();
        node3SAMs = new ArrayList<>();
        SAMs = new ArrayList<>();

        node1SAMs.add(new GpioSAM(context));
        node1SAMs.add(new RelaisSAM(context));

        node2SAMs.add(new GpioSAM(context));
        node2SAMs.add(new RelaisSAM(context));

        node3SAMs.add(new TouchButtonSAM(context));
        //node3SAMs.add(new LightSAM(context));
        node3SAMs.add(new BuzzerSAM(context));

        SAMs.add(node1SAMs);
        SAMs.add(node2SAMs);
        SAMs.add(node3SAMs);

    }



    public List<String> getNodeInitiators(int nodeType)
    {
        ArrayList<String> inits = new ArrayList<>();

        if(nodeType > 0 && nodeType <= MAX_NODES)
        {
            for(SAM s : SAMs.get(nodeType - 1)) {
                if (s.hasInput())
                    inits.add(s.getName());
            }
        }

        return inits;
    }

    public List<String> getNodeActors(int nodeType)
    {
        ArrayList<String> acts = new ArrayList<>();

        if(nodeType > 0 && nodeType <= MAX_NODES)
        {
            for(SAM s : SAMs.get(nodeType - 1)) {
                if (s.hasOutput())
                    acts.add(s.getName());
            }
        }

        return acts;
    }

    public List<String> getActorOperations(int nodeType, String actor)
    {
        ArrayList<String> ops = new ArrayList<>();

        if(nodeType > 0 && nodeType <= MAX_NODES)
        {
            for(SAM s : SAMs.get(nodeType - 1)) {
                if (s.hasOutput() && s.getName().equals(actor))
                    ops.addAll(s.getActorActions());
            }
        }

        return ops;
    }

    public List<String> getInitiatorOperations(int nodeType, String initiator)
    {
        ArrayList<String> ops = new ArrayList<>();

        if(nodeType > 0 && nodeType <= MAX_NODES)
        {
            for(SAM s : SAMs.get(nodeType - 1)) {
                if (s.hasInput() && s.getName().equals(initiator))
                    ops.addAll(s.getSourceActions());
            }
        }

        return ops;
    }

    public List<String> getOperators(int nodeType, String initiator)
    {
        ArrayList<String> ops = new ArrayList<>();

        if(nodeType > 0 && nodeType <= MAX_NODES)
        {
            for(SAM s : SAMs.get(nodeType - 1)) {
                if (s.hasInput() && s.getName().equals(initiator))
                    ops.addAll(s.getPossibleCompareSigns());
            }
        }

        return ops;
    }

    public String getInputValueLabel(int nodeType, String initiator)
    {
        if(nodeType > 0 && nodeType <= MAX_NODES)
        {
            for(SAM s : SAMs.get(nodeType - 1)) {
                if (s.hasInput() && s.getName().equals(initiator))
                    return s.getInputNumberLabel();
            }
        }

        return new String("DEFAULT");
    }

    public String getOutputValueLabel(int nodeType, String initiator)
    {
        if(nodeType > 0 && nodeType <= MAX_NODES)
        {
            for(SAM s : SAMs.get(nodeType - 1)) {
                if (s.hasOutput() && s.getName().equals(initiator))
                    return s.getOutputNumberLabel();
            }
        }

        return new String("DEFAULT");
    }

    public byte getActionID(int nodeType, String actor, String actorAction)
    {
        if(nodeType > 0 && nodeType <= MAX_NODES)
        {
            for(SAM s : SAMs.get(nodeType - 1)) {
                if (s.hasOutput() && s.getName().equals(actor))
                    return s.getActionID(actorAction);
            }
        }

        return 0;
    }

    public byte getSourceID(int nodeType, String initiator, String initiatorAction)
    {
        if(nodeType > 0 && nodeType <= MAX_NODES)
        {
            for(SAM s : SAMs.get(nodeType - 1)) {
                if (s.hasInput() && s.getName().equals(initiator))
                    return s.getSourceID(initiatorAction);
            }
        }

        return 0;
    }

    public byte getSamID(int nodeType, String initiator)
    {
        if(nodeType > 0 && nodeType <= MAX_NODES)
        {
            for(SAM s : SAMs.get(nodeType - 1)) {
                if (s.getName().equals(initiator))
                    return s.getSamId();
            }
        }

        return 0;
    }

    public byte[] getParamCompID(int nodeType, String initiator, String param)
    {
        if(nodeType > 0 && nodeType <= MAX_NODES)
        {
            for(SAM s : SAMs.get(nodeType - 1)) {
                if (s.getName().equals(initiator))
                    return s.getCompareParams(param);
            }
        }
        return new byte[19];
    }

    public byte[] getParam(int nodeType, String initiator, byte number)
    {
        if(nodeType > 0 && nodeType <= MAX_NODES)
        {
            for(SAM s : SAMs.get(nodeType - 1)) {
                if (s.getName().equals(initiator))
                    return s.getSourceParams(number);
            }
        }
        return new byte[19];
    }

}


