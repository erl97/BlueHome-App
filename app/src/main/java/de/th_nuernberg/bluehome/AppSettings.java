package de.th_nuernberg.bluehome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AppSettings extends AppCompatActivity {

    private TextView importView;
    private TextView exportView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);
        setTitle(R.string.settings);

        importView = (TextView)findViewById(R.id.app_settings_import);
        exportView = (TextView)findViewById(R.id.app_settings_export);

        exportView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AppSettings.this, ExportData.class);
                startActivity(i);
            }
        });
    }
}
