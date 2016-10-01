package com.avinashbehera.sabera.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.avinashbehera.sabera.R;
import com.avinashbehera.sabera.model.User;
import com.avinashbehera.sabera.util.PrefUtilsUser;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends Activity {

    public static final String TAG = SplashActivity.class.getSimpleName();
    protected int _splashTime = 1000;
    private Thread splashTread;
    protected boolean _active = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);



        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (_active && (waited < _splashTime)) {
                        sleep(100);
                        if (_active) {
                            waited += 100;
                        }
                    }
                } catch (InterruptedException e) {
                    finish();
                } finally {
                    try {

                        User user = PrefUtilsUser.getCurrentUser(SplashActivity.this);

                        if (user != null && user.getName()!=null) {

                            Log.d("LoginActivity","user!=null && user.getName() != null");

                            Intent homeIntent = new Intent(SplashActivity.this, BaseActivity.class);

                            startActivity(homeIntent);

                            finish();
                        }else{

                            Intent homeIntent = new Intent(SplashActivity.this, IntroActivity.class);

                            startActivity(homeIntent);

                            finish();

                        }




                    } catch (Exception e2) {
                        Log.d(TAG, "onCreate  Exception " + e2);
                    }
                }
            }
        };
        splashTread.start();

    }

    @Override
    public void onBackPressed() {
        splashTread.interrupt();
        super.onBackPressed();
    }
}
