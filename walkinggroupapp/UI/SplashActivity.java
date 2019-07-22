package com.example.homepc.walkinggroupapp.UI;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.homepc.walkinggroupapp.Model.App;
import com.example.homepc.walkinggroupapp.R;

/**
 * Introduction splash screen for the application. The Walking Group App splash screen.
 */
public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIMER = 5000;
    private App instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        instance = App.getInstance();
        setSplashScreen();
    }

    private void setSplashScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(instance.getisUserLoggedIn()) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                }

                else if(!instance.getisUserLoggedIn()) {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        }, SPLASH_TIMER);
    }
}
