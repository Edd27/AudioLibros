package com.example.audiolibros;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Vector;

public class MiAdaptadorPersonalizado extends RecyclerView.Adapter<MiAdaptadorPersonalizado.ViewHolder> {
    private final Vector<Libro> libros;

    private final Context contexto;

    private View.OnClickListener onClickLister;

    private View.OnLongClickListener onLongClickItemListener;

    public void setOnLongClickItemListener(View.OnLongClickListener onLongClickItemListener) {
        this.onLongClickItemListener = onLongClickItemListener;
    }

    public MiAdaptadorPersonalizado(Context ctx, Vector<Libro> libros){
        this.libros = libros;
        this.contexto = ctx;
    }

    public void setOnClickLister(View.OnClickListener onClickLister){
        this.onClickLister = onClickLister;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selector_layout, parent, false);
        v.setOnClickListener(this.onClickLister);
        v.setOnLongClickListener(this.onLongClickItemListener);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Libro libro = libros.elementAt(position);
        holder.getTitulo().setText(libro.getTitulo());
        holder.getPortada().setImageResource(libro.getRecursoImagen());
    }

    @Override
    public int getItemCount() {
        return libros.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView portada;
        private final TextView titulo;
        public ImageView getPortada() {
            return portada;
        }
        public TextView getTitulo() {
            return titulo;
        }
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            portada = itemView.findViewById(R.id.portada);
            titulo = itemView.findViewById(R.id.titulo);
        }
    }
}
