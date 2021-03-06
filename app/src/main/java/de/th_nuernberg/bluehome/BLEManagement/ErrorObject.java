package de.th_nuernberg.bluehome.BLEManagement;

import de.th_nuernberg.bluehome.BlueHomeDevice;

/**
 * ErrorObject contains error informations of one {@link BlueHomeDevice}
 *
 * @author Philipp Herrmann
 */
public class ErrorObject {
    private int errorID;
    private BlueHomeDevice device;

    public static final int ERROR_NO_ERROR = 0;
    public static final int ERROR_NOT_AVAILABLE = 1;
    public static final int ERROR_CHAR_WRITE = 2;
    public static final int ERROR_CHAR_READ = 3;

    public ErrorObject(int errorID, BlueHomeDevice device) {
        this.device = device;
        this.errorID = errorID;
    }

    public BlueHomeDevice getDevice(){
        return device;
    }

    public int getErrorID(){
        return errorID;
    }
}
