package com.tienda.virtual.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tienda.virtual.R;
import com.tienda.virtual.fragments.CartFragment;
import com.tienda.virtual.fragments.CategoriesFragment;
import com.tienda.virtual.fragments.HomeFragment;
import com.tienda.virtual.fragments.ProductsFragment;
import com.tienda.virtual.fragments.ProfileFragment;
import com.tienda.virtual.fragments.StoresFragment;
import com.tienda.virtual.services.MediaPlayerService;
import com.tienda.virtual.utils.PreferenceManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private BottomNavigationView bottomNav;
    private PreferenceManager pm;
    private String rol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");

        pm = new PreferenceManager(this);
        rol = pm.obtenerRolActivo();

        // Iniciar música de fondo
        Intent musicIntent = new Intent(this, MediaPlayerService.class);
        musicIntent.setAction(MediaPlayerService.ACTION_START_MUSIC);
        startService(musicIntent);

        bottomNav = findViewById(R.id.bottomNav);

        // Ocultar sección Productos para usuarios no admin
        if (!rol.equalsIgnoreCase("admin")) {
            MenuItem prod = bottomNav.getMenu().findItem(R.id.nav_products);
            if (prod != null) prod.setVisible(false);
        }

        Toast.makeText(this, "Rol activo: " + rol, Toast.LENGTH_SHORT).show();
        loadFragment(new HomeFragment());

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();

            // Efecto de sonido en cada tab
            MediaPlayerService.playEffect(this, R.raw.game_on);

            if (id == R.id.nav_home) {
                selectedFragment = new HomeFragment();
                setTitle("Inicio");

            } else if (id == R.id.nav_products) {
                selectedFragment = new ProductsFragment();
                setTitle("Productos");

            } else if (id == R.id.nav_categories) {
                selectedFragment = new CategoriesFragment();
                setTitle("Categorías");

            } else if (id == R.id.nav_stores) {
                selectedFragment = new StoresFragment();
                setTitle("Tiendas");

            } else if (id == R.id.nav_cart) {
                selectedFragment = new CartFragment();
                setTitle("Carrito");

            } else if (id == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
                setTitle("Perfil");
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        // Parar música de fondo
        Intent stopIntent = new Intent(this, MediaPlayerService.class);
        stopIntent.setAction(MediaPlayerService.ACTION_STOP_MUSIC);
        startService(stopIntent);
    }
}