package de.th_nuernberg.bluehome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FindNewDevice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_new_device);
        setTitle(getResources().getText(R.string.add_new_device));
    }
}
