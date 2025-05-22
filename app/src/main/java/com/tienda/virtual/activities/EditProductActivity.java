package com.tienda.virtual.activities;

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

public class EditProductActivity extends AppCompatActivity {

    private EditText etNombre, etDescripcion, etPrecio, etStock;
    private Spinner spinnerCategoria, spinnerImagen;
    private Button btnGuardar;

    private Producto productoOriginal;
    private int productoIndex;
    private List<Producto> productos;
    private PreferenceManager pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        pm = new PreferenceManager(this);

        etNombre = findViewById(R.id.etNombreProducto);
        etDescripcion = findViewById(R.id.etDescripcionProducto);
        etPrecio = findViewById(R.id.etPrecioProducto);
        etStock = findViewById(R.id.etStockProducto);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        spinnerImagen = findViewById(R.id.spinnerImagen);
        btnGuardar = findViewById(R.id.btnGuardarProducto);

        String[] categorias = {"Electrónica", "Ropa", "Deportes"};
        spinnerCategoria.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categorias));

        String[] imagenes = {"ic_audifonos", "ic_laptop", "ic_tablet", "ic_chaqueta", "ic_camiseta", "ic_pantalon", "ic_bici", "ic_balon", "ic_guantes"};
        spinnerImagen.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, imagenes));

        // Obtener el producto a editar
        productoIndex = getIntent().getIntExtra("producto_index", -1);
        productos = pm.obtenerProductosGuardados();

        if (productoIndex >= 0 && productoIndex < productos.size()) {
            productoOriginal = productos.get(productoIndex);
            cargarDatosEnFormulario();
        } else {
            Toast.makeText(this, "Error cargando producto", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnGuardar.setOnClickListener(v -> guardarCambios());
    }

    private void cargarDatosEnFormulario() {
        etNombre.setText(productoOriginal.getNombre());
        etDescripcion.setText(productoOriginal.getDescripcion());
        etPrecio.setText(String.valueOf(productoOriginal.getPrecio()));
        etStock.setText(String.valueOf(productoOriginal.getStock()));

        // Categoría
        for (int i = 0; i < spinnerCategoria.getCount(); i++) {
            if (spinnerCategoria.getItemAtPosition(i).toString().equalsIgnoreCase(productoOriginal.getCategoria())) {
                spinnerCategoria.setSelection(i);
                break;
            }
        }

        // Imagen
        for (int i = 0; i < spinnerImagen.getCount(); i++) {
            String nombreRes = spinnerImagen.getItemAtPosition(i).toString();
            int resId = getResources().getIdentifier(nombreRes, "drawable", getPackageName());
            if (resId == productoOriginal.getImagenResId()) {
                spinnerImagen.setSelection(i);
                break;
            }
        }
    }

    private void guardarCambios() {
        String nombre = etNombre.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String precioStr = etPrecio.getText().toString().trim();
        String stockStr = etStock.getText().toString().trim();
        String categoria = spinnerCategoria.getSelectedItem().toString();
        String imagenStr = spinnerImagen.getSelectedItem().toString();

        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(descripcion) || TextUtils.isEmpty(precioStr) || TextUtils.isEmpty(stockStr)) {
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

        // Actualizar objeto original
        productoOriginal.setNombre(nombre);
        productoOriginal.setDescripcion(descripcion);
        productoOriginal.setPrecio(precio);
        productoOriginal.setStock(stock);
        productoOriginal.setCategoria(categoria);
        productoOriginal.setImagenResId(imagenResId);

        // Guardar en SharedPreferences
        productos.set(productoIndex, productoOriginal);
        pm.guardarProductos(productos);

        Toast.makeText(this, "Producto actualizado", Toast.LENGTH_SHORT).show();
        finish();
    }
}