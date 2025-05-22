package com.tienda.virtual.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tienda.virtual.R;
import com.tienda.virtual.activities.AddProductActivity;
import com.tienda.virtual.adapters.ProductoAdapter;
import com.tienda.virtual.models.Producto;
import com.tienda.virtual.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class ProductsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductoAdapter adapter;
    private List<Producto> productos;
    private Button btnAgregarProducto; // Solo visible para admins

    private PreferenceManager pm;

    public ProductsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_products, container, false);

        pm = new PreferenceManager(requireContext());

        recyclerView = view.findViewById(R.id.recyclerProductos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        btnAgregarProducto = view.findViewById(R.id.btnAgregarProducto); // Asegúrate de tenerlo en el XML

        String rol = pm.obtenerRolActivo();
        if (!rol.equalsIgnoreCase("admin")) {
            btnAgregarProducto.setVisibility(View.GONE);
        } else {
            btnAgregarProducto.setVisibility(View.VISIBLE);
            btnAgregarProducto.setOnClickListener(v -> {
                startActivity(new Intent(getActivity(), AddProductActivity.class));
            });
        }

        productos = new ArrayList<>();
        cargarProductos();

        adapter = new ProductoAdapter(productos, getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void cargarProductos() {
        productos.add(new Producto("Audífonos", "Bluetooth", 40.0, R.drawable.ic_audifonos, "Electrónica", 10));
        productos.add(new Producto("Laptop", "i5 8GB RAM", 850.0, R.drawable.ic_laptop, "Electrónica", 3));
        productos.add(new Producto("Tablet", "10 pulgadas", 250.0, R.drawable.ic_tablet, "Electrónica", 0)); // Agotado

        productos.add(new Producto("Chaqueta", "Impermeable", 55.0, R.drawable.ic_chaqueta, "Ropa", 7));
        productos.add(new Producto("Camiseta", "Negra", 18.0, R.drawable.ic_camiseta, "Ropa", 0)); // Agotado
        productos.add(new Producto("Pantalón", "Jeans", 30.0, R.drawable.ic_pantalon, "Ropa", 4));

        productos.add(new Producto("Bicicleta", "Montañera", 200.0, R.drawable.ic_bici, "Deportes", 5));
        productos.add(new Producto("Balón", "Fútbol", 35.0, R.drawable.ic_balon, "Deportes", 15));
        productos.add(new Producto("Guantes", "Boxeo", 45.0, R.drawable.ic_guantes, "Deportes", 2));
    }
}