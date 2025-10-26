package com.example.cuidarte.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cuidarte.R;
import com.example.cuidarte.modelos.Calificacion;

import java.util.List;

public class CalificacionAdapter extends RecyclerView.Adapter<CalificacionAdapter.ViewHolder> {

    private List<Calificacion> lista;
    private Context context;

    public CalificacionAdapter(List<Calificacion> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_calificacion, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Calificacion c = lista.get(position);
        holder.txtEvaluador.setText(c.getEvaluadorNombre());
        holder.ratingBar.setRating(c.getPuntuacion());
        holder.txtComentario.setText(c.getComentario());
        holder.txtFecha.setText(c.getFecha());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtEvaluador, txtComentario, txtFecha;
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtEvaluador = itemView.findViewById(R.id.txtEvaluador);
            ratingBar = itemView.findViewById(R.id.ratingCalificacion);
            txtComentario = itemView.findViewById(R.id.txtComentario);
            txtFecha = itemView.findViewById(R.id.txtFecha);
        }
    }
}
