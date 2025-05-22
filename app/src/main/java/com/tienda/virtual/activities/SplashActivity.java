package com.tienda.virtual.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.tienda.virtual.R;
import com.tienda.virtual.utils.PreferenceManager;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_splash);

        // Transición según sesión activa después de 3 segundos
        new Handler().postDelayed(() -> {
            PreferenceManager pm = new PreferenceManager(SplashActivity.this);

            if (pm.haySesionActiva()) {
                Log.d(TAG, "Sesión activa detectada. Ingresando a MainActivity");
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                Log.d(TAG, "No hay sesión activa. Ingresando a WelcomeActivity");
                startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
            }

            finish();
        }, 3000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}