package de.th_nuernberg.bluehome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * App Settings Activity contains options to import and export configuration files.
 *
 * @author Philipp Herrmann
 */
public class AppSettings extends AppCompatActivity {

    private TextView importView;
    private TextView exportView;
    private TextView manAddView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);
        setTitle(R.string.settings);

        importView = (TextView)findViewById(R.id.app_settings_import);
        exportView = (TextView)findViewById(R.id.app_settings_export);
        manAddView = (TextView)findViewById(R.id.app_settings_manual_add);

        exportView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AppSettings.this, ExportData.class);
                startActivity(i);
            }
        });

        importView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AppSettings.this, ImportData.class);
                startActivity(i);
            }
        });

        manAddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AppSettings.this, ManAddDev.class);
                startActivity(i);
            }
        });
    }
}
