package de.th_nuernberg.bluehome;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;

import de.th_nuernberg.bluehome.BlueHomeDatabase.DatabaseInitiator;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

/**
 * Shows a Splashscreen on App start. Duration can be edited.
 *
 * @author Philipp Herrmann
 */
public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        this.scheduleSplashScreen();

    }


    private final void scheduleSplashScreen() {
        long splashScreenDuration = this.getSplashScreenDuration();
        (new Handler()).postDelayed((Runnable)(new Runnable() {
            public final void run() {
                SplashScreen.this.finish();
                routeToAppropriatePage();
            }
        }), splashScreenDuration);
    }

    /**
     * Change returnvalue for Splashscreen duration
     * @return Splashscreen duration
     */
    private final long getSplashScreenDuration() {
        return 300L;
    }

    private final void routeToAppropriatePage() {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


}
