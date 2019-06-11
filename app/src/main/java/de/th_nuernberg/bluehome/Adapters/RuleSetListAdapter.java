package de.th_nuernberg.bluehome.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.th_nuernberg.bluehome.BlueHomeDevice;
import de.th_nuernberg.bluehome.R;
import de.th_nuernberg.bluehome.RuleProcessObjects.RulesetObject;

public class RuleSetListAdapter extends BaseAdapter {
    private final Activity context;
    private ArrayList<RulesetObject> rulesets;
    private boolean deleteActive;

    public RuleSetListAdapter(Activity context, ArrayList<RulesetObject> rulesets) {
        this.context = context;
        this.rulesets = rulesets;
        deleteActive = false;
    }

    public void setNewList(ArrayList<RulesetObject> rulesets) {
        this.rulesets = rulesets;
    }

    public void setDeleteActive(boolean deleteActive) {
        this.deleteActive = deleteActive;
    }

    public boolean isDeleteActive() {
        return deleteActive;
    }

    @Override
    public int getCount() {
        return rulesets.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.ruleset_row_layout, null, true);

        TextView nameText = (TextView) rowView.findViewById(R.id.ruleset_list_name);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.ruleset_list_img);
        TextView macText = (TextView) rowView.findViewById(R.id.ruleset_list_mac);
        CheckBox deleteBox = (CheckBox) rowView.findViewById(R.id.ruleset_list_delete);
        ImageView imageView2 = (ImageView) rowView.findViewById(R.id.ruleset_list_img_2);

        nameText.setText(rulesets.get(position).getName());
        macText.setText(rulesets.get(position).getDev1().getShownName() + " -> " + rulesets.get(position).getDev2().getShownName());

        imageView.setImageResource(rulesets.get(position).getDev1().getImgID());
        imageView2.setImageResource(rulesets.get(position).getDev2().getImgID());

        if(deleteActive)
            deleteBox.setVisibility(View.VISIBLE);
        else
            deleteBox.setVisibility(View.GONE);

        return rowView;
    }
}
