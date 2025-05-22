package com.tienda.virtual.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tienda.virtual.R;
import com.tienda.virtual.models.Usuario;
import com.tienda.virtual.services.MediaPlayerService;
import com.tienda.virtual.utils.PreferenceManager;

/**
 * Activity de bienvenida que dirige al usuario a Login o Registro.
 * Pantalla mostrada después del Splash.
 * Ahora con saludo dinámico y efecto de sonido gamer.
 */
public class WelcomeActivity extends AppCompatActivity {

    private static final String TAG = "WelcomeActivity";

    private Button btnComienza;
    private TextView tvRegistrate;
    private TextView tvGreeting;             // NUEVO
    private TextView tvMensajeBienvenida;    // EXISTENTE CON ID CAMBIADO A tvMensajeBienvenida
    private PreferenceManager pm;            // NUEVO

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_welcome);

        // === INICIO: referencias originales ===
        btnComienza      = findViewById(R.id.btnComienza);
        tvRegistrate     = findViewById(R.id.tvRegistrate);
        // === FIN: referencias originales ===

        // === INICIO: nuevas referencias ===
        pm                     = new PreferenceManager(this);
        tvGreeting             = findViewById(R.id.tvGreeting);
        tvMensajeBienvenida    = findViewById(R.id.tvMensajeBienvenida);
        // === FIN: nuevas referencias ===

        // Acción del botón “Comienza” → LoginActivity (con efecto de sonido)
        btnComienza.setOnClickListener(v -> {
            Log.d(TAG, "Click en Comienza");
            MediaPlayerService.playEffect(this, R.raw.game_on);
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        });

        // Acción del texto “Regístrate” → RegisterActivity
        tvRegistrate.setOnClickListener(v -> {
            Log.d(TAG, "Click en Registrate");
            startActivity(new Intent(WelcomeActivity.this, RegisterActivity.class));
        });

        // === INICIO: saludo dinámico ===
        Usuario user = pm.obtenerUsuarioActivo();
        String nick = (user != null && user.getNombres() != null)
                ? user.getNombres().split(" ")[0]
                : "Gamer";
        tvGreeting.setText("¡Welcome, " + nick + "!");
        // tvMensajeBienvenida queda con el texto estático de @string/mensaje_bienvenida
        // === FIN: saludo dinámico ===
    }

    @Override protected void onStart()   { super.onStart();   Log.d(TAG, "onStart"); }
    @Override protected void onResume()  { super.onResume();  Log.d(TAG, "onResume"); }
    @Override protected void onPause()   { super.onPause();   Log.d(TAG, "onPause"); }
    @Override protected void onStop()    { super.onStop();    Log.d(TAG, "onStop"); }
    @Override protected void onDestroy() { super.onDestroy(); Log.d(TAG, "onDestroy"); }
}