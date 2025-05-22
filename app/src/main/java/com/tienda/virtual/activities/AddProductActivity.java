package com.tienda.virtual.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tienda.virtual.R;
import com.tienda.virtual.models.Producto;
import com.tienda.virtual.utils.PreferenceManager;

import java.util.List;

public class AddProductActivity extends AppCompatActivity {

    private EditText etNombre, etDescripcion, etPrecio, etStock;
    private Spinner spinnerCategoria, spinnerImagen;
    private Button btnGuardar;

    private PreferenceManager pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        pm = new PreferenceManager(this);

        etNombre = findViewById(R.id.etNombreProducto);
        etDescripcion = findViewById(R.id.etDescripcionProducto);
        etPrecio = findViewById(R.id.etPrecioProducto);
        etStock = findViewById(R.id.etStockProducto);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        spinnerImagen = findViewById(R.id.spinnerImagen);
        btnGuardar = findViewById(R.id.btnGuardarProducto);

        // CATEGORÍAS
        String[] categorias = {"Electrónica", "Ropa", "Deportes"};
        spinnerCategoria.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categorias));

        // IMÁGENES - referenciamos nombres que deberías tener en /res/drawable
        String[] imagenes = {"ic_audifonos", "ic_laptop", "ic_tablet", "ic_chaqueta", "ic_camiseta", "ic_pantalon", "ic_bici", "ic_balon", "ic_guantes"};
        spinnerImagen.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, imagenes));

        btnGuardar.setOnClickListener(v -> guardarProducto());
    }

    private void guardarProducto() {
        String nombre = etNombre.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String precioStr = etPrecio.getText().toString().trim();
        String stockStr = etStock.getText().toString().trim();
        String categoria = spinnerCategoria.getSelectedItem().toString();
        String imagenStr = spinnerImagen.getSelectedItem().toString();

        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(descripcion) ||
                TextUtils.isEmpty(precioStr) || TextUtils.isEmpty(stockStr)) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        double precio;
        int stock;
        try {
            precio = Double.parseDouble(precioStr);
            stock = Integer.parseInt(stockStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Precio o stock inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        int imagenResId = getResources().getIdentifier(imagenStr, "drawable", getPackageName());
        if (imagenResId == 0) imagenResId = R.drawable.ic_launcher_background; // fallback

        Producto nuevo = new Producto(nombre, descripcion, precio, imagenResId, categoria, stock);

        List<Producto> productos = pm.obtenerProductosGuardados(); // 🔥 Asumimos este método
        productos.add(nuevo);
        pm.guardarProductos(productos); // 🔥 Asumimos este método

        Toast.makeText(this, "Producto guardado", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}