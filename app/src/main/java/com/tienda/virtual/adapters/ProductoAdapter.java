package com.tienda.virtual.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tienda.virtual.R;
import com.tienda.virtual.activities.EditProductActivity;
import com.tienda.virtual.models.Producto;
import com.tienda.virtual.utils.PreferenceManager;

import java.util.List;

/**
 * Adaptador para mostrar productos en el RecyclerView con badge “Sin stock”
 * y lógica de añadir al carrito o gestionar como admin.
 */
public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder> {

    private final List<Producto> productos;
    private final Context context;
    private final boolean esAdmin;
    private final PreferenceManager pm;

    public ProductoAdapter(List<Producto> productos, Context context) {
        this.productos = productos;
        this.context = context;
        pm = new PreferenceManager(context);
        esAdmin = pm.obtenerRolActivo().equalsIgnoreCase("admin");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_producto, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto producto = productos.get(position);

        // Datos básicos
        holder.nombre.setText(producto.getNombre());
        holder.descripcion.setText(producto.getDescripcion());
        holder.precio.setText("$" + producto.getPrecio());
        holder.imagen.setImageResource(producto.getImagenResId());

        // STOCK
        if (producto.getStock() <= 0) {
            holder.badgeSinStock.setVisibility(View.VISIBLE);
            holder.btnAgregar.setText("Agotado");
            holder.btnAgregar.setEnabled(false);
            holder.btnAgregar.setAlpha(0.5f);
        } else {
            holder.badgeSinStock.setVisibility(View.GONE);
            holder.btnAgregar.setText("Agregar");
            holder.btnAgregar.setEnabled(true);
            holder.btnAgregar.setAlpha(1.0f);
            holder.btnAgregar.setOnClickListener(v -> {
                pm.agregarAlCarrito(producto);
                Toast.makeText(context,
                        producto.getNombre() + " añadido al carrito",
                        Toast.LENGTH_SHORT).show();
            });
        }

        // Opciones de admin
        if (esAdmin) {
            holder.itemView.setOnLongClickListener(v -> {
                mostrarDialogoOpciones(producto, position);
                return true;
            });
        }
    }

    private void mostrarDialogoOpciones(Producto producto, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Opciones para: " + producto.getNombre())
                .setItems(new String[]{"✏️ Editar", "🗑️ Eliminar"}, (dialog, which) -> {
                    if (which == 0) {
                        Intent intent = new Intent(context, EditProductActivity.class);
                        intent.putExtra("producto_index", position);
                        context.startActivity(intent);
                    } else {
                        eliminarProducto(position);
                    }
                })
                .show();
    }

    private void eliminarProducto(int position) {
        Producto eliminado = productos.remove(position);
        notifyItemRemoved(position);
        pm.guardarProductos(productos);
        Toast.makeText(context,
                "Producto eliminado: " + eliminado.getNombre(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, descripcion, precio, badgeSinStock;
        ImageView imagen;
        Button btnAgregar;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre        = itemView.findViewById(R.id.tvNombreProducto);
            descripcion   = itemView.findViewById(R.id.tvDescripcionProducto);
            precio        = itemView.findViewById(R.id.tvPrecioProducto);
            imagen        = itemView.findViewById(R.id.ivImagenProducto);
            btnAgregar    = itemView.findViewById(R.id.btnAgregarCarrito);
            badgeSinStock = itemView.findViewById(R.id.tvSinStock);
        }
    }
}