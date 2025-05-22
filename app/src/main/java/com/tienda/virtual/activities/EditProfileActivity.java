package com.tienda.virtual.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tienda.virtual.R;
import com.tienda.virtual.models.Usuario;
import com.tienda.virtual.utils.PreferenceManager;

import java.util.List;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etNombre, etApellidos, etCorreo, etTelefono;
    private Button btnGuardar;
    private PreferenceManager pm;
    private Usuario usuarioActivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etNombre    = findViewById(R.id.etEditarNombre);
        etApellidos = findViewById(R.id.etEditarApellidos);
        etCorreo    = findViewById(R.id.etEditarCorreo);
        etTelefono  = findViewById(R.id.etEditarTelefono);
        btnGuardar  = findViewById(R.id.btnGuardarPerfil);

        pm = new PreferenceManager(this);
        usuarioActivo = pm.obtenerUsuarioActivo();

        if (usuarioActivo != null) {
            etNombre.setText(usuarioActivo.getNombres());
            etApellidos.setText(usuarioActivo.getApellidos());
            etCorreo.setText(usuarioActivo.getCorreo());
            etTelefono.setText(usuarioActivo.getTelefono());
        }

        btnGuardar.setOnClickListener(v -> guardarCambios());
    }

    private void guardarCambios() {
        String nombre    = etNombre.getText().toString().trim();
        String apellidos = etApellidos.getText().toString().trim();
        String correo    = etCorreo.getText().toString().trim();
        String telefono  = etTelefono.getText().toString().trim();

        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(apellidos)
                || TextUtils.isEmpty(correo) || TextUtils.isEmpty(telefono)) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Correo inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Usuario> usuarios = pm.obtenerUsuarios();
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario u = usuarios.get(i);
            if (u.getCorreo().equalsIgnoreCase(usuarioActivo.getCorreo())) {
                usuarios.set(i, new Usuario(
                        nombre, apellidos, correo, telefono, usuarioActivo.getContrasena()
                ));
                break;
            }
        }

        pm.guardarUsuarios(usuarios);
        pm.guardarSesionActiva(correo);

        Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show();
        finish();
    }
}