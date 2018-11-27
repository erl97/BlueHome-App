package de.th_nuernberg.bluehome;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity {

    private boolean firstStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.dashboard);
        setContentView(R.layout.activity_start);

    }

    protected void onResume(){
        super.onResume();
        //if(firstStart)
            //showPopup();

    }

    private void showPopup() {

        LayoutInflater inflater = (LayoutInflater) StartActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Inflate the view from a predefined XML layout (no need for root id, using entire layout)
        View layout = inflater.inflate(R.layout.popup_first_start,null);

        float density = StartActivity.this.getResources().getDisplayMetrics().density;

        final PopupWindow pw = new PopupWindow(layout, (int)density*240, (int)density*285, true);

        pw.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.RED));

        pw.setOutsideTouchable(true);
        pw.showAtLocation(layout, Gravity.CENTER, 0, 0);

    }

    public void menu1_pressed(View view){

    }

    public void menu2_pressed(View view){
        Intent intent = new Intent(this, DeviceList.class);
        startActivity(intent);
    }

    public void menu3_pressed(View view){

    }

    public void menu4_pressed(View view){

    }

    public void menu5_pressed(View view){

    }
}
