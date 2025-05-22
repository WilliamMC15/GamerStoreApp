package com.tienda.virtual.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tienda.virtual.R;
import com.tienda.virtual.utils.PreferenceManager;

public class RegisterActivity extends AppCompatActivity {

    private EditText etNombres, etApellidos, etCorreo, etTelefono, etContrasena, etRepetirContrasena;
    private CheckBox cbTerminos;
    private Button btnRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etNombres = findViewById(R.id.etNombres);
        etApellidos = findViewById(R.id.etApellidos);
        etCorreo = findViewById(R.id.etCorreo);
        etTelefono = findViewById(R.id.etTelefono);
        etContrasena = findViewById(R.id.etContrasena);
        etRepetirContrasena = findViewById(R.id.etRepetirContrasena);
        cbTerminos = findViewById(R.id.cbTerminos);
        btnRegistro = findViewById(R.id.btnRegistro);

        btnRegistro.setOnClickListener(v -> registrarUsuario());
    }

    private void registrarUsuario() {
        String nombres = etNombres.getText().toString().trim();
        String apellidos = etApellidos.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String contrasena = etContrasena.getText().toString();
        String repetir = etRepetirContrasena.getText().toString();

        if (TextUtils.isEmpty(nombres) || TextUtils.isEmpty(apellidos) || TextUtils.isEmpty(correo)
                || TextUtils.isEmpty(telefono) || TextUtils.isEmpty(contrasena) || TextUtils.isEmpty(repetir)) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Correo inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!contrasena.equals(repetir)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!cbTerminos.isChecked()) {
            Toast.makeText(this, "Debes aceptar los términos y condiciones", Toast.LENGTH_SHORT).show();
            return;
        }

        PreferenceManager pm = new PreferenceManager(this);

        boolean registrado = pm.registrarUsuario(nombres, apellidos, correo, telefono, contrasena);
        if (!registrado) {
            Toast.makeText(this, "Ese correo ya está registrado", Toast.LENGTH_SHORT).show();
            return;
        }

        pm.guardarSesionActiva(correo);

        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(RegisterActivity.this, RegisterSuccessActivity.class));
        finish();
    }
}