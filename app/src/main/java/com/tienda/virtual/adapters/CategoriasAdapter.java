package com.tienda.virtual.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tienda.virtual.R;
import com.tienda.virtual.models.Producto;
import com.tienda.virtual.utils.PreferenceManager;

import java.util.List;
import java.util.Map;

public class CategoriasAdapter extends RecyclerView.Adapter<CategoriasAdapter.ViewHolder> {

    private final Context context;
    private final List<String> categorias;
    private final Map<String, List<Producto>> productosPorCategoria;
    private final PreferenceManager pm;

    public CategoriasAdapter(Context context, Map<String, List<Producto>> mapa) {
        this.context = context;
        this.categorias = new java.util.ArrayList<>(mapa.keySet());
        this.productosPorCategoria = mapa;
        this.pm = new PreferenceManager(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_categoria, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String categoria = categorias.get(position);
        holder.tvCategoria.setText(categoria);
        holder.container.removeAllViews();

        for (Producto p : productosPorCategoria.get(categoria)) {
            View item = LayoutInflater.from(context).inflate(R.layout.item_producto_categoria, holder.container, false);

            TextView nombre = item.findViewById(R.id.tvNombreProductoCat);
            TextView desc = item.findViewById(R.id.tvDescripcionProductoCat);
            TextView precio = item.findViewById(R.id.tvPrecioProductoCat);
            ImageView imagen = item.findViewById(R.id.ivImagenProductoCat);
            imagen.setImageResource(p.getImagenResId());
            Button btnAgregar = item.findViewById(R.id.btnAgregarProductoCat);

            nombre.setText(p.getNombre());
            desc.setText(p.getDescripcion());
            precio.setText("$" + p.getPrecio());

            btnAgregar.setOnClickListener(v -> {
                pm.agregarAlCarrito(new Producto(
                        p.getNombre(),
                        p.getDescripcion(),
                        p.getPrecio(),
                        p.getImagenResId(),
                        p.getCategoria()
                ));
            });

            holder.container.addView(item);
        }
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoria;
        LinearLayout container;

        ViewHolder(View itemView) {
            super(itemView);
            tvCategoria = itemView.findViewById(R.id.tvTituloCategoria);
            container = itemView.findViewById(R.id.layoutProductosCategoria);
        }
    }
}