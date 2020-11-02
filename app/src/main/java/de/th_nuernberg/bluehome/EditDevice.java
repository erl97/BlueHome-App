package de.th_nuernberg.bluehome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
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
import android.widget.Toast;

import java.util.Arrays;

import de.th_nuernberg.bluehome.BLEManagement.BLEDataExchangeManager;
import de.th_nuernberg.bluehome.BLEManagement.BLEService;
import de.th_nuernberg.bluehome.BlueHomeDatabase.BlueHomeDeviceStorageManager;
import de.th_nuernberg.bluehome.RuleProcessObjects.ActionObject;

/**
 * EditDevice Activity offers tools to edit device data like shown name and image
 *
 * @author Philipp Herrmann
 */
public class EditDevice extends AppCompatActivity {

    private EditText shownNameView;
    private TextView realNameView;
    private TextView macAddressView;
    private Spinner imgSpinnerView;
    private Button submitButton;
    Integer[] spinnerImages;
    private BlueHomeDevice toEdit;
    private BlueHomeDeviceStorageManager db = new BlueHomeDeviceStorageManager(this);
    private BLEDataExchangeManager bleman = new BLEDataExchangeManager();

    private Button btnDirectInterface;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("EditDevice", "Received Broadcast");
            Toast.makeText(context,"Failed to Connect to " + intent.getStringExtra(BLEService.TAG_MAC), Toast.LENGTH_SHORT).show();
        }
    };

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
        btnDirectInterface = (Button)findViewById(R.id.btnDirectInterface);


        spinnerImages = new Integer[]{R.drawable.bluehome_node, R.drawable.bluehome_node_1, R.drawable.bluehome_node_2, R.drawable.bluehome_node_3, R.drawable.bluehome_node_4};

        toEdit = db.getDevice(getIntent().getExtras().getString("macAddress"));
        if(toEdit == null)
            finish();

        shownNameView.setText(toEdit.getShownName());
        realNameView.setText(toEdit.getDeviceName());
        macAddressView.setText(toEdit.getMacAddress());


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

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("WRITE_FAIL"));

        btnDirectInterface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditDevice.this, DirectInterface.class);
                intent.putExtra("macAddress", toEdit.getMacAddress());
                startActivity(intent);
                //writeAction(view);
            }
        });

    }

    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onStop();
    }


    public void writeAction(View view) {
        byte[] test = new byte[19];
        for(int i = 0; i < 19; i++)
            test[i] = 0;
        ActionObject act = null;
        switch (toEdit.getNodeType())
        {
            case 1:
                act = new ActionObject();
                act.setActionMemID((byte)0);
                act.setActionSAM((byte)0x08);
                act.setParamMask(0x00);
                test[0] = 1;
                act.setParam(test);
                act.setActionID((byte)0x01);


                break;

            case 2:
                act = new ActionObject();
                act.setActionMemID((byte)0);
                act.setActionSAM((byte)0x07);
                act.setParamMask(0x00);
                act.setParam(test);
                act.setActionID((byte)0x01);

                break;

            case 3:

                act = new ActionObject();
                act.setActionMemID((byte)0);
                act.setActionSAM((byte)0x0A);
                act.setParamMask(0x00);
                test[0] = 20;
                act.setParam(test);
                act.setActionID((byte)0x01);

                break;
        }

        if(act != null)
            bleman.runAction(toEdit, act, this);

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
