package de.th_nuernberg.bluehome;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class WizardChoseRuleSetName extends Fragment {

    private Button btnContinue;
    public EditText name;
    private WizardCreateNewRuleSet fragmentView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_wizard_chose_rule_set_name, container, false);
        btnContinue = (Button) rootView.findViewById(R.id.btnFirstStepContinue);
        name = (EditText)rootView.findViewById(R.id.editTextName);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentView.mPager.setCurrentItem(fragmentView.mPager.getCurrentItem() + 1);
            }
        });
        return rootView;
    }
    public WizardChoseRuleSetName(){}

    @SuppressLint("ValidFragment")
    public WizardChoseRuleSetName(WizardCreateNewRuleSet fragmentView)
    {
        this.fragmentView = fragmentView;
    }


}
