package de.th_nuernberg.bluehome;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * DeviceListAdapter ist used to convert an ArrayList of {@link BlueHomeDevice}s to a showable list with images and formated text.
 *
 * @author Philipp Herrmann
 */
public class DeviceListAdapter extends BaseAdapter {

    private final Activity context;
    private ArrayList<BlueHomeDevice> devices;
    private String names[];
    private View rowView;
    private boolean deleteActive;
    private int version = 0;

    public final static int VERSION_BIG_SHOWN_NAME = 1;
    public final static int VERSION_BIG_MAC_ADDRESS = 2;
    public final static int VERSION_BIG_REAL_NAME = 3;
    public final static int VERSION_BIG_REAL_NAME_AND_TYPE = 4;

    public DeviceListAdapter(Activity context, ArrayList<BlueHomeDevice> devices) {

        this.context = context;
        this.devices = devices;
    }

    /**
     * sets the display version option defined by VERSION tags in this class
     *
     * @param ver version to show
     */
    public void setVersion(int ver) {
        version = ver;
    }

    /**
     * exchanges the ArrayList which is shown. List has to be notified about dataset changed
     *
     * @param devices ArrayList of BlueHome Devices
     */
    public void setNewDevicelist(ArrayList<BlueHomeDevice> devices) {
        this.devices = devices;
    }


    /**
     * activates the delete checkbox on the list entries
     *
     * @param deleteActive true for activate, false for deactivate
     */
    public void setDeleteActive(boolean deleteActive) {
        this.deleteActive = deleteActive;
    }

    /**
     * used to determine wether delete checkbox should be active or not
     *
     * @return true if delete is active, false if not
     */
    public boolean isDeleteActive() {
        return deleteActive;
    }

    /**
     * gets amount of entries
     *
     * @return amount of entries
     */
    public int getCount() {
        return devices.size();
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    /**
     * returns view for single entry, depending on display version. Called by ListView
     *
     * @param position
     * @param view
     * @param parent
     * @return view for single entry
     */
    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        rowView = inflater.inflate(R.layout.device_row_layout, null, true);

        TextView nameText = (TextView) rowView.findViewById(R.id.device_list_name);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.device_list_img);
        TextView macText = (TextView) rowView.findViewById(R.id.device_list_mac);
        CheckBox deleteBox = (CheckBox) rowView.findViewById(R.id.device_list_delete);

        switch (version) {
            case VERSION_BIG_MAC_ADDRESS:
                nameText.setText(devices.get(position).getMacAddress());
                macText.setText(devices.get(position).getDeviceName());
                break;

            case VERSION_BIG_REAL_NAME:
                nameText.setText(devices.get(position).getDeviceName());
                macText.setText(devices.get(position).getMacAddress());
                break;

            case VERSION_BIG_SHOWN_NAME:
                nameText.setText(devices.get(position).getShownName());
                macText.setText("" + devices.get(position).getMacAddress() + " / " + devices.get(position).getDeviceName());
                break;

            case VERSION_BIG_REAL_NAME_AND_TYPE:
                nameText.setText(context.getString(R.string.type) + ": " + devices.get(position).getNodeType() + ", " + devices.get(position).getDeviceName());
                macText.setText(devices.get(position).getMacAddress());
                break;


            default:
                    nameText.setText(devices.get(position).getShownName());
                    macText.setText("" + devices.get(position).getMacAddress() + " / " + devices.get(position).getDeviceName());
                    break;
        }


        imageView.setImageResource(devices.get(position).getImgID());
        if(deleteActive)
            deleteBox.setVisibility(View.VISIBLE);
        else
            deleteBox.setVisibility(View.GONE);

        return rowView;

    }

}
