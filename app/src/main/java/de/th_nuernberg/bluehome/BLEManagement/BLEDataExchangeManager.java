package de.th_nuernberg.bluehome.BLEManagement;

import android.app.Application;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import de.th_nuernberg.bluehome.BlueHomeDevice;
import de.th_nuernberg.bluehome.RuleProcessObjects.ActionObject;
import de.th_nuernberg.bluehome.RuleProcessObjects.RPC;
import de.th_nuernberg.bluehome.RuleProcessObjects.RuleObject;


public class BLEDataExchangeManager extends Application {

    private String DEBUG_TAG = "BLEExMan";

    public BLEDataExchangeManager() {
    }

    public void addToBuffer(BLEBufferElement toBuffer, Context context) {
        Log.i(DEBUG_TAG, "building Intent");
        Intent intent = new Intent(context, BLEService.class);
        if (toBuffer.getOperation().equals(BLEBufferElement.READ_DEVICE)) {
            Log.i(DEBUG_TAG, BLEBufferElement.READ_DEVICE);
            intent.setAction(BLEBufferElement.READ_DEVICE);
            intent.putExtra(BLEBufferElement.TAG_DEV_MAC, toBuffer.getDev().getMacAddress());
            intent.putExtra(BLEBufferElement.TAG_CHAR_UUID, toBuffer.getCharUuid());
            intent.putExtra(BLEBufferElement.TAG_SERV_UUID, toBuffer.getServUuid());
            intent.putExtra(BLEBufferElement.TAG_BROAD_READ, toBuffer.getBroadcastOnRead());
            intent.putExtra(BLEBufferElement.TAG_BROAD_ERROR, toBuffer.getBroadcastOnConnectionError());
        }
        if (toBuffer.getOperation().equals(BLEBufferElement.WRITE_DEVICE)) {
            intent.setAction(BLEBufferElement.WRITE_DEVICE);
            intent.putExtra(BLEBufferElement.TAG_DEV_MAC, toBuffer.getDev().getMacAddress());
            intent.putExtra(BLEBufferElement.TAG_CHAR_UUID, toBuffer.getCharUuid());
            intent.putExtra(BLEBufferElement.TAG_SERV_UUID, toBuffer.getServUuid());
            intent.putExtra(BLEBufferElement.TAG_DATA, toBuffer.getData());
            intent.putExtra(BLEBufferElement.TAG_BROAD_ERROR, toBuffer.getBroadcastOnConnectionError());

        }
        Log.i(DEBUG_TAG, "issueing start");
        context.startService(intent);
        Log.i(DEBUG_TAG, "start issued");
    }

    public void programAction(BlueHomeDevice dev, ActionObject act, Context con){
        addToBuffer(new BLEBufferElement(dev, act.getParam(), BLEService.UUID_DIRECT_PARAM, BLEService.UUID_DIRECT_SERV, "WRITE_FAIL"), con);
        byte[] tmpData = new byte[]{RPC.SAM_PROG, RPC.ACT_ID_WRITE_ACTION, act.getActionMemID(), act.getActionSAM(), act.getActionID(), act.getMaskPart(0), act.getMaskPart(1), act.getMaskPart(2), act.getMaskPart(3)};
        addToBuffer(new BLEBufferElement(dev, tmpData, BLEService.UUID_DIRECT_OPTIONS, BLEService.UUID_DIRECT_SERV, "WRITE_FAIL"), con);
    }

    public void runAction(BlueHomeDevice dev, ActionObject act, Context con){
        addToBuffer(new BLEBufferElement(dev, act.getParam(), BLEService.UUID_DIRECT_PARAM, BLEService.UUID_DIRECT_SERV, "WRITE_FAIL"), con);
        byte[] tmpData = new byte[]{act.getActionSAM(), act.getActionID(), act.getMaskPart(0), act.getMaskPart(1), act.getMaskPart(2), act.getMaskPart(3)};
        addToBuffer(new BLEBufferElement(dev, tmpData, BLEService.UUID_DIRECT_OPTIONS, BLEService.UUID_DIRECT_SERV, "WRITE_FAIL"), con);
    }

    public void programRule(BlueHomeDevice dev, RuleObject rule, Context con){
        addToBuffer(new BLEBufferElement(dev, rule.getToComp().getParams(), BLEService.UUID_DIRECT_PARAM, BLEService.UUID_DIRECT_SERV, "WRITE_FAIL"), con);
        addToBuffer(new BLEBufferElement(dev, rule.getParamComp(), BLEService.UUID_DIRECT_PARAMCOMP, BLEService.UUID_DIRECT_SERV, "WRITE_FAIL"), con);
        byte[] tmpData = new byte[]{RPC.SAM_PROG, RPC.ACT_ID_WRITE_RULE, rule.getActionMemID(), rule.getRuleMemID(), rule.getToComp().getSourceSAM(), rule.getToComp().getSourceID()};
        addToBuffer(new BLEBufferElement(dev, tmpData, BLEService.UUID_DIRECT_OPTIONS, BLEService.UUID_DIRECT_SERV, "WRITE_FAIL"), con);
    }

    public void programMAC(BlueHomeDevice dev, byte macID, String mac, Context con){
        String[] macParts = mac.split(":");

        byte[] macBytes = new byte[6];
        for(int i=0; i<6; i++){
            Integer hex = Integer.parseInt(macParts[i], 16);
            macBytes[i] = hex.byteValue();
        }
        byte[] param = new byte[]{macID, macBytes[0], macBytes[1], macBytes[2], macBytes[3], macBytes[4], macBytes[5]};
        addToBuffer(new BLEBufferElement(dev, param, BLEService.UUID_DIRECT_PARAM, BLEService.UUID_DIRECT_SERV, "WRITE_FAIL"), con);
        byte[] tmpData = new byte[]{RPC.SAM_PROG, RPC.ACT_ID_WRITE_MAC};
        addToBuffer(new BLEBufferElement(dev, tmpData, BLEService.UUID_DIRECT_OPTIONS, BLEService.UUID_DIRECT_SERV, "WRITE_FAIL"), con);
    }
}