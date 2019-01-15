package de.th_nuernberg.bluehome;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DeviceListAdapter extends BaseAdapter {

    private final Activity context;
    private ArrayList<BlueHomeDevice> devices;
    private String names[];
    private View rowView;
    private boolean deleteActive;
    private int version = 0;

    private final int VERSION_BIG_SHOWN_NAME = 1;
    private final int VERSION_BIG_MAC_ADDRESS = 2;
    private final int VERSION_BIG_REAL_NAME = 3;

    public DeviceListAdapter(Activity context, ArrayList<BlueHomeDevice> devices) {

        this.context = context;
        this.devices = devices;
    }

    public void setVersion(int ver) {
        version = ver;
    }

    public void setNewDevicelist(ArrayList<BlueHomeDevice> devices) {
        this.devices = devices;
    }


    public void setDeleteActive(boolean deleteActive) {
        this.deleteActive = deleteActive;
    }

    public boolean isDeleteActive() {
        return deleteActive;
    }

    public int getCount() {
        return devices.size();
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

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

    public boolean isChecked() {
        CheckBox deleteBox = (CheckBox) rowView.findViewById(R.id.device_list_delete);
        return deleteBox.isChecked();
    }

}
