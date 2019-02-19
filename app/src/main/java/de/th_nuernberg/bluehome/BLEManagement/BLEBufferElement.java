package de.th_nuernberg.bluehome.BLEManagement;

import android.bluetooth.BluetoothGatt;

import java.util.UUID;

import de.th_nuernberg.bluehome.BlueHomeDevice;

public class BLEBufferElement {
    public final static int READ_DEVICE = 0;
    public final static int WRITE_DEVICE = 1;

    private BlueHomeDevice dev;
    private byte[] data;
    private UUID charUuid;
    private UUID servUuid;
    private int operation;
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

    public int getOperation() {
        return operation;
    }

    public String getBroadcastOnRead() {
        return broadcastOnRead;
    }

    public String getBroadcastOnConnectionError() {
        return broadcastOnConnectionError;
    }
}
