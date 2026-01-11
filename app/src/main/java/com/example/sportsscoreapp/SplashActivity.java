package com.example.sportsscoreapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

/**
 * The initial activity shown when the app starts.
 * It waits for a set duration and then navigates to the MainActivity.
 */
public class SplashActivity extends AppCompatActivity {

    // Duration of the splash screen display in milliseconds (2 seconds)
    private static final int SPLASH_DURATION = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use the splash screen layout
        setContentView(R.layout.activity_splash);

        // Handler to start the main activity after the delay
        new Handler().postDelayed(() -> {
            // Start MainActivity
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);

            // Close the splash activity so the user cannot navigate back to it
            finish();
        }, SPLASH_DURATION);
    }
}