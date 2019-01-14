package de.th_nuernberg.bluehome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import lib.folderpicker.FolderPicker;

public class ExportData extends AppCompatActivity {

    private Button selectButtonView;
    private TextView pathView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_data);
        setTitle(R.string.export_string);

        selectButtonView = (Button)findViewById(R.id.export_data_select_button);
        pathView = (TextView)findViewById(R.id.export_data_path);

        selectButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ExportData.this, FolderPicker.class);
                i.putExtra("title", getResources().getString(R.string.choose_directory));
                startActivityForResult(i, 9999);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 9999:
                pathView.setText(data.getExtras().getString("data"));
                break;
        }
    }
}
