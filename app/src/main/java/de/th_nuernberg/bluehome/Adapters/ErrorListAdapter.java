package de.th_nuernberg.bluehome.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import de.th_nuernberg.bluehome.BLEManagement.ErrorObject;
import de.th_nuernberg.bluehome.R;
import de.th_nuernberg.bluehome.StartActivity;

/**
 * ErrorListAdapter is used for ListView on {@link StartActivity} to show error states
 *
 * @author Philipp Herrmann
 */
public class ErrorListAdapter extends BaseAdapter {
    private final Activity context;
    private ArrayList<ErrorObject> errors;
    private String names[];
    private View rowView;
    private HashMap<Integer, String> errorCodes = new HashMap<>();

    public ErrorListAdapter(Activity context, ArrayList<ErrorObject> errors) {

        this.context = context;
        if(errors != null)
            this.errors = errors;

        //Insert all error Codes in Hash Map
        errorCodes.put(1, this.context.getString(R.string.error_not_available));

    }

    /**
     * exchanges ArrayList of {@link ErrorObject}s to display. ListView needs to be notified after change
     *
     * @param errors new ArrayList of ErrorObjects
     */
    public void setNewErrorlist(ArrayList<ErrorObject> errors) {
        if(errors != null)
            this.errors = errors;
        else
            this.errors.clear();
    }

    /**
     * gets amount of entries
     *
     * @return amount of entries
     */
    public int getCount() {
        return errors.size();
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    /**
     * gets view of one single entry at position. Called by ListView
     *
     * @param position
     * @param view
     * @param parent
     * @return
     */
    public View getView(int position, View view, ViewGroup parent) {
        String tmpName;
        LayoutInflater inflater=context.getLayoutInflater();
        rowView = inflater.inflate(R.layout.error_row_layout, null, true);

        TextView DeviceNameText = (TextView) rowView.findViewById(R.id.error_list_device_name);
        TextView ErrorDescText = (TextView) rowView.findViewById(R.id.error_list_error_desc);

        if(errorCodes.get(errors.get(position).getErrorID()) == null) {
            tmpName = new String("" + errors.get(position).getErrorID());
        } else {
            tmpName = new String(errorCodes.get(errors.get(position).getErrorID()));
        }

        DeviceNameText.setText(errors.get(position).getDevice().getShownName());
        ErrorDescText.setText(tmpName);

        return rowView;

    }
}
