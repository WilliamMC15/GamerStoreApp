package com.tienda.virtual.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.tienda.virtual.R;

/**
 * Pantalla intermedia que muestra el mensaje "Tu usuario es tu correo"
 * tras un registro exitoso.
 */
public class RegisterSuccessActivity extends AppCompatActivity {

    private Button btnOkSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_success);

        btnOkSuccess = findViewById(R.id.btnOkSuccess);

        btnOkSuccess.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}