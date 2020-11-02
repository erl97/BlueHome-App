package de.th_nuernberg.bluehome;

import android.drm.DrmStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Array;

import de.th_nuernberg.bluehome.BLEManagement.BLEDataExchangeManager;
import de.th_nuernberg.bluehome.BlueHomeDatabase.BlueHomeDeviceStorageManager;
import de.th_nuernberg.bluehome.RuleProcessObjects.ActionObject;

public class DirectInterface extends AppCompatActivity {

    private BlueHomeDevice toEdit;
    private BLEDataExchangeManager bleman = new BLEDataExchangeManager();
    private Button btn1, btn2, btn3, btn4, btnPeep;
    private BlueHomeDeviceStorageManager db = new BlueHomeDeviceStorageManager(this);
    private TextView lblToneHeight;
    private EditText tfToneHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_interface);
        btn1 = findViewById(R.id.btnAction1);
        btn2 = findViewById(R.id.btnAction2);
        btn3 = findViewById(R.id.btnAction3);
        btn4 = findViewById(R.id.btnAction4);
        btnPeep = findViewById(R.id.btnPeep);
        lblToneHeight = findViewById(R.id.lblToneHeigth);
        tfToneHeight = findViewById(R.id.toneHight);

        toEdit = db.getDevice(getIntent().getExtras().getString("macAddress"));

        if (toEdit.getNodeType() == 3) {
            btnPeep.setVisibility(View.VISIBLE);
            tfToneHeight.setVisibility(View.VISIBLE);
            lblToneHeight.setVisibility(View.VISIBLE);
        }

        if (toEdit.getNodeType() == 1)
        {
            btn1.setVisibility(View.VISIBLE);
        }

        if (toEdit.getNodeType() == 2)
        {
            btn1.setVisibility(View.VISIBLE);
            btn2.setVisibility(View.VISIBLE);
            btn3.setVisibility(View.VISIBLE);
            btn4.setVisibility(View.VISIBLE);
            btnPeep.setVisibility(View.GONE);
            tfToneHeight.setVisibility(View.GONE);
            lblToneHeight.setVisibility(View.GONE);
        }

        btnPeep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionObject act;
                byte[] test = new byte[19];
                for(int i = 0; i < 19; i++)
                    test[i] = 0;
                Integer tone = Integer.parseInt(tfToneHeight.getText().toString());
                if(tone != null)
                    test[0] = tone.byteValue();
                else
                    return;
                act = new ActionObject();
                act.setActionMemID((byte)0);
                act.setActionSAM((byte)0x0A);
                act.setParamMask(0x00);
                act.setParam(test);
                act.setActionID((byte)0x01);
                if(act != null)
                    bleman.runAction(toEdit, act, DirectInterface.this);
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActionObject act;
                byte[] test = new byte[19];
                for(int i = 0; i < 19; i++)
                    test[i] = 0;
                test[0] = 0;
                act = new ActionObject();
                act.setActionMemID((byte)0);
                act.setActionSAM((byte)0x07);
                act.setParamMask(0x00);
                act.setParam(test);
                act.setActionID((byte)0x01);
                if(act != null)
                    bleman.runAction(toEdit, act, DirectInterface.this);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActionObject act;
                byte[] test = new byte[19];
                for(int i = 0; i < 19; i++)
                    test[i] = 0;
                test[0] = 1;
                act = new ActionObject();
                act.setActionMemID((byte)0);
                act.setActionSAM((byte)0x07);
                act.setParamMask(0x00);
                act.setParam(test);
                act.setActionID((byte)0x01);
                if(act != null)
                    bleman.runAction(toEdit, act, DirectInterface.this);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActionObject act;
                byte[] test = new byte[19];
                for(int i = 0; i < 19; i++)
                    test[i] = 0;
                test[0] = 2;
                act = new ActionObject();
                act.setActionMemID((byte)0);
                act.setActionSAM((byte)0x07);
                act.setParamMask(0x00);
                act.setParam(test);
                act.setActionID((byte)0x01);
                if(act != null)
                    bleman.runAction(toEdit, act, DirectInterface.this);
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActionObject act;
                byte[] test = new byte[19];
                for(int i = 0; i < 19; i++)
                    test[i] = 0;
                test[0] = 3;
                act = new ActionObject();
                act.setActionMemID((byte)0);
                act.setActionSAM((byte)0x07);
                act.setParamMask(0x00);
                act.setParam(test);
                act.setActionID((byte)0x01);
                if(act != null)
                    bleman.runAction(toEdit, act, DirectInterface.this);
            }
        });
    }

}
