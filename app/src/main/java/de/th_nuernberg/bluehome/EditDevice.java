package de.th_nuernberg.bluehome;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;

import de.th_nuernberg.bluehome.BlueHomeDatabase.BlueHomeDeviceStorageManager;

public class EditDevice extends AppCompatActivity {

    private EditText shownNameView;
    private TextView realNameView;
    private TextView macAddressView;
    private Spinner imgSpinnerView;
    private Button submitButton;
    Integer[] spinnerImages;
    private BlueHomeDevice toEdit;
    private BlueHomeDeviceStorageManager db = new BlueHomeDeviceStorageManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_device);
        setTitle(R.string.edit_device);

        shownNameView = (EditText)findViewById(R.id.edit_device_shown_name);
        realNameView = (TextView)findViewById(R.id.edit_device_real_name);
        macAddressView = (TextView)findViewById(R.id.edit_device_mac);
        imgSpinnerView = (Spinner)findViewById(R.id.edit_device_img_spinner);
        submitButton = (Button)findViewById(R.id.edit_device_submit_button);

        spinnerImages = new Integer[]{R.drawable.add, R.drawable.bluehome_device, R.drawable.delete, R.drawable.dashboard_menu_button_1, R.drawable.dashboard_menu_button_2, R.drawable.unknown_device};

        toEdit = db.getDevice(getIntent().getExtras().getString("macAddress"));
        if(toEdit == null)
            finish();

        shownNameView.setText(toEdit.getShownName());
        realNameView.setText(toEdit.getDeviceName());
        macAddressView.setText(toEdit.getMacAdress());

        ImageSpinnerAdapter adapter = new ImageSpinnerAdapter(getApplicationContext(), R.layout.image_spinner_layout, spinnerImages);
        imgSpinnerView.setAdapter(adapter);

        imgSpinnerView.setSelection(Arrays.asList(spinnerImages).indexOf(toEdit.getImgID()));


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toEdit.setShownName(shownNameView.getText().toString());
                toEdit.setImgID(spinnerImages[imgSpinnerView.getSelectedItemPosition()]);
                db.updateDevice(toEdit);
                finish();
            }
        });


    }


    protected class ImageSpinnerAdapter extends ArrayAdapter {

        private Integer[] images;

        public ImageSpinnerAdapter(Context context, int resource, Integer[] image) {
            super(context, resource, image);
            images = image;
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.image_spinner_layout, parent, false);

            ImageView img = (ImageView) view.findViewById(R.id.image_spinner_imageView);

            img.setImageResource(images[position]);

            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

    }
}
