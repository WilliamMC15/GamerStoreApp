package com.tienda.virtual.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.tienda.virtual.R;

/**
 * Activity que gestiona el flujo "Olvidé mi contraseña":
 * 1) El usuario introduce su email y pulsa ENVIAR CÓDIGO → validación + Toast simulado.
 * 2) Introduce el código recibido y la nueva contraseña → validación + Toast éxito.
 * 3) Redirige a LoginActivity.
 */
public class RecoveryActivity extends AppCompatActivity {
    private EditText etRecoveryEmail;
    private EditText etRecoveryCode;
    private EditText etNewPassword;
    private Button btnSendCode;
    private Button btnResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery);

        // 1) Referencias a vistas
        etRecoveryEmail  = findViewById(R.id.etRecoveryEmail);
        etRecoveryCode   = findViewById(R.id.etRecoveryCode);
        etNewPassword    = findViewById(R.id.etNewPassword);
        btnSendCode      = findViewById(R.id.btnSendCode);
        btnResetPassword = findViewById(R.id.btnResetPassword);

        // 2) Envío de código simulado
        btnSendCode.setOnClickListener(v -> {
            String email = etRecoveryEmail.getText().toString().trim();
            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etRecoveryEmail.setError("Ingrese un correo válido");
                return;
            }
            Toast.makeText(this, "Código enviado a " + email, Toast.LENGTH_SHORT).show();
            etRecoveryCode.requestFocus();
        });

        // 3) Restablecer contraseña simulado
        btnResetPassword.setOnClickListener(v -> {
            String code = etRecoveryCode.getText().toString().trim();
            String pwd  = etNewPassword.getText().toString();
            if (code.isEmpty()) {
                etRecoveryCode.setError("Ingrese el código recibido");
                return;
            }
            if (pwd.length() < 6) {
                etNewPassword.setError("La contraseña debe tener al menos 6 caracteres");
                return;
            }
            Toast.makeText(this,
                    "Contraseña restablecida con éxito",
                    Toast.LENGTH_LONG
            ).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}