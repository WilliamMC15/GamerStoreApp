package com.tienda.virtual.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tienda.virtual.R;
import com.tienda.virtual.adapters.CarritoAdapter;
import com.tienda.virtual.models.Producto;
import com.tienda.virtual.utils.PreferenceManager;

import java.util.List;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView tvResumen;
    private Button btnVaciar, btnPagar;
    private PreferenceManager pm;
    private CarritoAdapter adapter;
    private List<Producto> productos;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cart, container, false);

        pm = new PreferenceManager(requireContext());

        recyclerView = root.findViewById(R.id.recyclerCarrito);
        tvResumen = root.findViewById(R.id.tvResumenCarrito);
        btnVaciar = root.findViewById(R.id.btnVaciarCarrito);
        btnPagar = root.findViewById(R.id.btnPagarCarrito);

        productos = pm.obtenerCarrito();

        adapter = new CarritoAdapter(productos, requireContext(), pm, this::actualizarTotales);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        btnVaciar.setOnClickListener(v -> mostrarDialogoConfirmacion());

        btnPagar.setOnClickListener(v ->
                Toast.makeText(getContext(), "Pago simulado", Toast.LENGTH_SHORT).show()
        );

        actualizarTotales();
        return root;
    }

    private void mostrarDialogoConfirmacion() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Vaciar carrito")
                .setMessage("¿Estás seguro de que deseas vaciar el carrito?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    pm.limpiarCarrito();
                    productos.clear();
                    adapter.notifyDataSetChanged();
                    actualizarTotales();
                    Toast.makeText(getContext(), "Carrito vaciado", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void actualizarTotales() {
        double total = 0;
        int totalProductos = 0;

        for (Producto p : productos) {
            total += p.getCantidad() * p.getPrecio();
            totalProductos += p.getCantidad();
        }

        tvResumen.setText("Productos: " + totalProductos + " | Total: $" + String.format("%.2f", total));
    }
}