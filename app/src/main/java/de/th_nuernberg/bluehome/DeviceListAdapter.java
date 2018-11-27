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

    public DeviceListAdapter(Activity context, ArrayList<BlueHomeDevice> devices) {

        this.context = context;
        this.devices = devices;
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
        rowView=inflater.inflate(R.layout.device_row_layout, null,true);

        TextView nameText = (TextView) rowView.findViewById(R.id.device_list_name);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.device_list_img);
        TextView macText = (TextView) rowView.findViewById(R.id.device_list_mac);
        CheckBox deleteBox = (CheckBox) rowView.findViewById(R.id.device_list_delete);

        nameText.setText(devices.get(position).getShownName());
        imageView.setImageResource(devices.get(position).getImgID());
        macText.setText("" + devices.get(position).getMacAdress() + " / " + devices.get(position).getDeviceName());
        if(devices.get(position).isDeleteActive())
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
