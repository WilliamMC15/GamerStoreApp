package com.tienda.virtual.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tienda.virtual.R;
import com.tienda.virtual.utils.PreferenceManager;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsuario, etContrasena;
    private Button btnIngresar, btnGoogle;
    private TextView tvRecuperar, tvIrRegistrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsuario = findViewById(R.id.etUsuario);
        etContrasena = findViewById(R.id.etContrasena);
        btnIngresar = findViewById(R.id.btnIngresar);
        btnGoogle = findViewById(R.id.btnGoogle);
        tvRecuperar = findViewById(R.id.tvRecuperar);
        tvIrRegistrate = findViewById(R.id.tvIrRegistrate);

        btnIngresar.setOnClickListener(v -> intentarLogin());

        tvRecuperar.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RecoveryActivity.class)));

        tvIrRegistrate.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        btnGoogle.setOnClickListener(v ->
                Toast.makeText(this, "Login con Google no implementado aún", Toast.LENGTH_SHORT).show());
    }

    private void intentarLogin() {
        String correo = etUsuario.getText().toString().trim();
        String pass = etContrasena.getText().toString();

        if (TextUtils.isEmpty(correo) || TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Correo inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        PreferenceManager pm = new PreferenceManager(this);

        if (pm.validarLogin(correo, pass)) {
            pm.guardarSesionActiva(correo);

            // NUEVO: Obtener y guardar el rol
            String rol = pm.obtenerRol(correo); // Asumimos que este método existe
            pm.guardarRol(rol);

            Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
        }
    }
}