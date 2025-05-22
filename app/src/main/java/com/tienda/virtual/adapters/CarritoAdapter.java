package com.tienda.virtual.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tienda.virtual.R;
import com.tienda.virtual.models.Producto;
import com.tienda.virtual.utils.PreferenceManager;

import java.util.List;

/**
 * Adapter para el RecyclerView del carrito.
 * Gestiona:
 * - Aumento y disminución de cantidad.
 * - Eliminación de ítem.
 * - Notificación a la UI para recalcular totales.
 */
public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.ViewHolder> {

    private final List<Producto> productos;
    private final PreferenceManager pm;
    private final Runnable callbackActualizar;

    public CarritoAdapter(List<Producto> productos, PreferenceManager pm, Runnable callbackActualizar) {
        this.productos = productos;
        this.pm = pm;
        this.callbackActualizar = callbackActualizar;
    }

    @NonNull
    @Override
    public CarritoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_carrito, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CarritoAdapter.ViewHolder holder, int position) {
        Producto producto = productos.get(position);

        // Carga de datos
        holder.tvNombre.setText(producto.getNombre());
        holder.tvCantidad.setText(String.valueOf(producto.getCantidad()));
        double subtotal = producto.getCantidad() * producto.getPrecio();
        holder.tvPrecio.setText("$" + String.format("%.2f", subtotal));
        holder.ivImagen.setImageResource(producto.getImagenResId());

        // Aumentar cantidad
        holder.btnAumentar.setOnClickListener(v -> {
            int nuevaCant = producto.getCantidad() + 1;
            producto.setCantidad(nuevaCant);
            pm.actualizarCantidad(producto.getId(), nuevaCant); // Ajustar según tu método real
            holder.tvCantidad.setText(String.valueOf(nuevaCant));
            callbackActualizar.run();
        });

        // Disminuir cantidad
        holder.btnDisminuir.setOnClickListener(v -> {
            int nuevaCant = producto.getCantidad() - 1;
            if (nuevaCant < 1) return;
            producto.setCantidad(nuevaCant);
            pm.actualizarCantidad(producto.getId(), nuevaCant); // Ajustar según tu método real
            holder.tvCantidad.setText(String.valueOf(nuevaCant));
            callbackActualizar.run();
        });

        // Eliminar del carrito
        holder.btnEliminar.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            pm.eliminarDelCarrito(producto.getId());
            productos.remove(pos);
            notifyItemRemoved(pos);
            callbackActualizar.run();
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImagen;
        TextView tvNombre, tvPrecio, tvCantidad;
        ImageButton btnAumentar, btnDisminuir, btnEliminar;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImagen     = itemView.findViewById(R.id.ivCarritoImagen);
            tvNombre     = itemView.findViewById(R.id.tvCarritoNombre);
            tvPrecio     = itemView.findViewById(R.id.tvCarritoPrecio);
            tvCantidad   = itemView.findViewById(R.id.tvCantidad);
            btnAumentar  = itemView.findViewById(R.id.btnAumentar);
            btnDisminuir = itemView.findViewById(R.id.btnDisminuir);
            btnEliminar  = itemView.findViewById(R.id.btnEliminarCarrito);
        }
    }
}