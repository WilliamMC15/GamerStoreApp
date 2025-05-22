package com.tienda.virtual.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tienda.virtual.R;
import com.tienda.virtual.adapters.CategoriasAdapter;
import com.tienda.virtual.models.Producto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoriesFragment extends Fragment {

    private RecyclerView rvCategorias;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_categories, container, false);

        rvCategorias = root.findViewById(R.id.rvCategorias);
        rvCategorias.setLayoutManager(new LinearLayoutManager(getContext()));

        Map<String, List<Producto>> mapa = obtenerProductosAgrupados();
        CategoriasAdapter adapter = new CategoriasAdapter(getContext(), mapa);
        rvCategorias.setAdapter(adapter);

        return root;
    }

    private Map<String, List<Producto>> obtenerProductosAgrupados() {
        List<Producto> lista = new ArrayList<>();

        lista.add(new Producto("Audífonos", "Bluetooth", 40.0, R.drawable.ic_audifonos, "Electrónica"));
        lista.add(new Producto("Laptop", "i5 8GB RAM", 850.0, R.drawable.ic_laptop, "Electrónica"));
        lista.add(new Producto("Tablet", "10 pulgadas", 250.0, R.drawable.ic_tablet, "Electrónica"));

        lista.add(new Producto("Bicicleta", "Montañera", 200.0, R.drawable.ic_bici, "Deportes"));
        lista.add(new Producto("Balón", "Fútbol", 35.0, R.drawable.ic_balon, "Deportes"));
        lista.add(new Producto("Guantes", "Boxeo", 45.0, R.drawable.ic_guantes, "Deportes"));

        lista.add(new Producto("Chaqueta", "Impermeable", 55.0, R.drawable.ic_chaqueta, "Ropa"));
        lista.add(new Producto("Camiseta", "Negra", 18.0, R.drawable.ic_camiseta, "Ropa"));
        lista.add(new Producto("Pantalón", "Jeans", 30.0, R.drawable.ic_pantalon, "Ropa"));

        Map<String, List<Producto>> mapa = new HashMap<>();
        for (Producto p : lista) {
            if (!mapa.containsKey(p.getCategoria())) {
                mapa.put(p.getCategoria(), new ArrayList<>());
            }
            mapa.get(p.getCategoria()).add(p);
        }
        return mapa;
    }
}