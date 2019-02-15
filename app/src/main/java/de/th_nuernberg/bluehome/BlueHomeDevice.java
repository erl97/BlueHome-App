package de.th_nuernberg.bluehome;

import android.bluetooth.BluetoothDevice;
import android.net.MacAddress;

import java.util.ArrayList;
import java.util.Objects;

public class BlueHomeDevice {
    private String address;
    private String deviceName;
    private String shownName;
    private int nodeType;
    private int imgID = R.drawable.bluehome_device;
    private boolean deleteActive = false;
    boolean toRemove = false;

    public BlueHomeDevice(String address, String deviceName) {
        this.address = address;
        this.deviceName = deviceName;
    }

    public BlueHomeDevice(BlueHomeDevice dev) {
        this.address = dev.getMacAddress();
        this.deviceName = dev.getDeviceName();
        this.shownName = dev.getShownName();
        this.nodeType = dev.getNodeType();
        this.imgID = dev.getImgID();
        this.deleteActive = dev.isDeleteActive();
        this.toRemove = dev.isToRemove();
    }



    public void setShownName(String shownName) {
        this.shownName = shownName;
    }

    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }

    public String getMacAddress() {
        return address;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getShownName() {
        return shownName;
    }

    public int getNodeType() {
        return nodeType;
    }


    public int getImgID() {
        return imgID;
    }

    public void setImgID(int imgID) {
        this.imgID = imgID;
    }

    public void setDeleteActive(boolean deleteActive) {
        this.deleteActive = deleteActive;
    }

    public boolean isDeleteActive() {
        return deleteActive;
    }
    public boolean isToRemove() {
        return toRemove;
    }

    public void setToRemove(boolean toRemove) {
        this.toRemove = toRemove;
    }

    @Override
    public boolean equals(Object object) {
        if(!(object instanceof BlueHomeDevice))
            return false;

        if(this.getMacAddress().trim().equalsIgnoreCase(((BlueHomeDevice) object).getMacAddress().trim()))
            return true;
        else
            return false;
    }
}
