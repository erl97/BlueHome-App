package de.th_nuernberg.bluehome;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

import de.th_nuernberg.bluehome.BlueHomeDatabase.BlueHomeDeviceStorageManager;

public class WizardChoseRulesetSource extends Fragment {

    private Button btnContinue;
    private WizardCreateNewRuleSet fragmentView;
    private BlueHomeDeviceStorageManager deviceList;
    public ArrayList<BlueHomeDevice> devs;
    private ArrayList<String> deviceNames = new ArrayList<>();
    private ArrayAdapter<String> adapterDevices;
    public Spinner source_device;
    public Spinner target_device;


    public void Continue()
    {
            if (getSource_device().getSelectedItemPosition() >= 1 && getTarget_device().getSelectedItemPosition() >= 1) {
                fragmentView.rulesetInitiatorActor.setSourceDevice(devs.get(source_device.getSelectedItemPosition() - 1));
                fragmentView.rulesetInitiatorActor.setTargetDevice(devs.get(target_device.getSelectedItemPosition() - 1));
                fragmentView.mPager.setCurrentItem(fragmentView.mPager.getCurrentItem() + 1);
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(fragmentView);
                final AlertDialog dialog = builder.setTitle("Error!").setMessage(R.string.wrongDevice).setPositiveButton(android.R.string.ok, null).create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
                    }
                });
                dialog.show();
            }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_wizard_chose_rule_set_source, container, false);
        btnContinue = (Button) rootView.findViewById(R.id.btnSecondStepContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Continue();
            }
        });
        deviceList = new BlueHomeDeviceStorageManager(fragmentView);
        source_device = (Spinner)rootView.findViewById(R.id.spinnerSourceDevice);
        target_device = (Spinner)rootView.findViewById(R.id.spinnerTargetDevice);
        devs = deviceList.getAllDevices();
        for (int i = 0;i < devs.size(); i++)
        {
            deviceNames.add(devs.get(i).getShownName());
        }

        deviceNames.add(0,"-- " + getResources().getString(R.string.please_choose) + " --");

        if (devs.size() == 0)
        {
            adapterDevices = new ArrayAdapter<String>(fragmentView, R.layout.text_spinner_layout, new String[]{"no Devices"});
        }
        else
        {
            adapterDevices = new ArrayAdapter<String>(fragmentView, R.layout.text_spinner_layout, (String[]) deviceNames.toArray(new String[0]));
        }
        //adapterDevices.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        getSource_device().setAdapter(adapterDevices);
        getTarget_device().setAdapter(adapterDevices);
        return rootView;
    }
    public WizardChoseRulesetSource(){}

    @SuppressLint("ValidFragment")
    public WizardChoseRulesetSource(WizardCreateNewRuleSet fragmentView)
    {
        this.fragmentView = fragmentView;
    }

    public Spinner getSource_device() {
        return source_device;
    }

    public Spinner getTarget_device() {
        return target_device;
    }
}
