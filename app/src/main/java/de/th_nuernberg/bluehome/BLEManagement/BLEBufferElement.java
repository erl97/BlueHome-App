package de.th_nuernberg.bluehome.BLEManagement;

import android.bluetooth.BluetoothGatt;

import java.util.UUID;

import de.th_nuernberg.bluehome.BlueHomeDevice;

public class BLEBufferElement {
    public final static String READ_DEVICE = "READ_DEVICE";
    public final static String WRITE_DEVICE = "WRITE_DEVICE";

    public final static String TAG_DEV_MAC = "DEV_MAC";
    public final static String TAG_DATA = "DATA";
    public final static String TAG_CHAR_UUID = "CHAR_UUID";
    public final static String TAG_SERV_UUID = "SERV_UUID";
    public final static String TAG_BROAD_READ = "BROAD_READ";
    public final static String TAG_BROAD_ERROR = "BROAD_ERROR";

    private BlueHomeDevice dev;
    private byte[] data;
    private UUID charUuid;
    private UUID servUuid;
    private String operation;
    private String broadcastOnRead;
    private String broadcastOnConnectionError;

    public BLEBufferElement(BlueHomeDevice dev, byte[] data, UUID charUuid, UUID servUuid, String broadcastOnConnectionError){
        this.dev = dev;
        this.data = data;
        this.charUuid = charUuid;
        this.servUuid = servUuid;
        this.broadcastOnConnectionError = broadcastOnConnectionError;
        this.operation = WRITE_DEVICE;
    }

    public BLEBufferElement(BlueHomeDevice dev, UUID charUuid, UUID servUuid, String broadcastOnRead, String broadcastOnConnectionError){
        this.dev = dev;
        this.charUuid = charUuid;
        this.servUuid = servUuid;
        this.broadcastOnRead = broadcastOnRead;
        this.broadcastOnConnectionError = broadcastOnConnectionError;
        this.operation = READ_DEVICE;
    }


    public byte[] getData() {
        return data;
    }

    public UUID getCharUuid() {
        return charUuid;
    }

    public UUID getServUuid() {
        return servUuid;
    }

    public BlueHomeDevice getDev() {
        return dev;
    }

    public String getOperation() {
        return operation;
    }

    public String getBroadcastOnRead() {
        return broadcastOnRead;
    }

    public String getBroadcastOnConnectionError() {
        return broadcastOnConnectionError;
    }
}
