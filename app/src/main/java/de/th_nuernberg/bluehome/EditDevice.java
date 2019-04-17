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

import de.th_nuernberg.bluehome.BLEManagement.BLEBufferElement;
import de.th_nuernberg.bluehome.BLEManagement.BLEDataExchangeManager;
import de.th_nuernberg.bluehome.BLEManagement.BLEService;
import de.th_nuernberg.bluehome.BLEManagement.ErrorObject;
import de.th_nuernberg.bluehome.BlueHomeDatabase.BlueHomeDeviceStorageManager;
import de.th_nuernberg.bluehome.RuleProcessObjects.ActionObject;
import de.th_nuernberg.bluehome.RuleProcessObjects.RPC;
import de.th_nuernberg.bluehome.RuleProcessObjects.RuleObject;
import de.th_nuernberg.bluehome.RuleProcessObjects.SourceObject;

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
                new IntentFilter("FAIL"));

    }

    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onStop();
    }

    public void writeRule(View view){
        SourceObject tmpSource = new SourceObject();
        byte[] tmpBytes = {1,2,3,4,5,6};
        byte[] tmpCases = {RPC.COMP_EQUALS, RPC.COMP_SMALLER_EQUAL, RPC.COMP_DOESNT_MATTER, RPC.COMP_DOESNT_MATTER, RPC.COMP_DOESNT_MATTER, RPC.COMP_DOESNT_MATTER};
        RuleObject rule = new RuleObject();
        rule.setActionMemID((byte)1);
        rule.setParamComp(tmpCases);
        rule.setRuleMemID((byte)5);
        rule.setToComp(tmpSource);
        rule.getToComp().setParams(tmpBytes);
        rule.getToComp().setSourceID((byte)2);
        rule.getToComp().setSourceSAM(RPC.SAM_BUTTON);


        bleman.programMAC(toEdit, (byte)1, "AB:CD:EF:12:34:56", this);
        //Log.i("StartAct", ""+bleman.writeRule(toEdit, rule));
    }

    public void writeAction(View view) {
        ActionObject tmpAct = new ActionObject();
        byte[] tmpBytes = {0x01};
        tmpAct.setActionID((byte)1);
        tmpAct.setActionMemID((byte)5);
        tmpAct.setActionSAM((byte)3);
        tmpAct.setParam(tmpBytes);
        tmpAct.setParamMask(0b00000000000000000000101010101010);

        //bleman.writeAction(toEdit, tmpAct);
        Log.i("paramPart", "" + tmpAct.getMaskPart(0));

        BLEBufferElement tmpBuf = new BLEBufferElement(toEdit, tmpBytes, BLEService.UUID_DIRECT_PARAM, BLEService.UUID_DIRECT_SERV, "FAIL");

        byte[] tmpBytes2 = {0x0A,0x01};
        BLEBufferElement tmpBuf2 = new BLEBufferElement(toEdit, tmpBytes2, BLEService.UUID_DIRECT_OPTIONS, BLEService.UUID_DIRECT_SERV, "FAIL");
        Log.i("ActionButton", "created Buffer Element");
        if(bleman != null) {
            bleman.addToBuffer(tmpBuf, this);
            bleman.addToBuffer(tmpBuf2, this);
        }
        else
            Log.i("ActionButton", "NullPointer");

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
