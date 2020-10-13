package de.th_nuernberg.bluehome;

/**
 * BlueHomeDevice is the logical clone to a physical BlueHome device. It contains information that is needed to connect to the device.
 *
 * @author Philipp Herrmann
 */
public class BlueHomeDevice {
    private String address;
    private String deviceName;
    private String shownName;
    private int nodeType;
    private int imgID = R.drawable.bluehome_device;
    private boolean deleteActive = false;
    boolean toRemove = false;
    private byte macId;

    public BlueHomeDevice(String address, String deviceName) {
        this.setAddress(address);
        this.setDeviceName(deviceName);
    }

    public BlueHomeDevice()
    {

    }

    public BlueHomeDevice(BlueHomeDevice dev) {
        this.setAddress(dev.getMacAddress());
        this.setDeviceName(dev.getDeviceName());
        this.shownName = dev.getShownName();
        this.nodeType = dev.getNodeType();
        this.imgID = dev.getImgID();
        this.deleteActive = dev.isDeleteActive();
        this.toRemove = dev.isToRemove();
        this.macId = dev.getMacId();
    }

    public byte getMacId()
    {
        return macId;
    }

    public void setMacId(byte macId)
    {
        this.macId = macId;
    }

    /**
     * sets the name that is shown in device list and configuration parts of the app.
     *
     * @param shownName shown name in devicelist
     */
    public void setShownName(String shownName) {
        this.shownName = shownName;
    }

    /**
     * sets the node type. Needed to differ between hardware versions and types
     *
     * @param nodeType hardware node type
     */
    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }

    /**
     * gets devices' mac address
     *
     * @return mac address
     */
    public String getMacAddress() {
        return getAddress();
    }

    /**
     * gets real device name. Only for further information. Configuration is based on shown name.
     *
     * @return device name
     */
    public String getDeviceName() {
        return deviceName;
    }

    /**
     * gets the name which is used in device list and configuration parts
     *
     * @return shown name
     */
    public String getShownName() {
        return shownName;
    }

    /**
     * gets the node type. Needed to differ between hardware versions and types
     *
     * @return hardware node type
     */
    public int getNodeType() {
        return nodeType;
    }

    /**
     * gets image ressource id. Used for custom images in {@link DeviceList}
     *
     * @return image ressource id
     */
    public int getImgID() {
        return imgID;
    }

    /**
     * sets image ressource id. Used for custom images in {@link DeviceList}
     *
     * @param imgID image ressource id
     */
    public void setImgID(int imgID) {
        this.imgID = imgID;
    }

    /**
     * gets delete active status. used for delete function in {@link DeviceList}
     * @return
     */
    public boolean isDeleteActive() {
        return deleteActive;
    }
    public boolean isToRemove() {
        return toRemove;
    }

    /**
     * equals function for comparing BlueHomeDevice objects
     *
     * @param object object to compare against
     * @return true if object matches, false if not
     */
    @Override
    public boolean equals(Object object) {
        if(!(object instanceof BlueHomeDevice))
            return false;

        if(this.getMacAddress().trim().equalsIgnoreCase(((BlueHomeDevice) object).getMacAddress().trim()))
            return true;
        else
            return false;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
