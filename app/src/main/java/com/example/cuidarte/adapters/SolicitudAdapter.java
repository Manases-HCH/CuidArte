package com.example.cuidarte.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.cuidarte.R;
import com.example.cuidarte.modelos.Solicitud;
import java.util.List;

public class SolicitudAdapter extends RecyclerView.Adapter<SolicitudAdapter.ViewHolder> {

    private List<Solicitud> lista;
    private Context context;

    public SolicitudAdapter(List<Solicitud> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_solicitud, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Solicitud s = lista.get(position);
        holder.txtNombre.setText(s.getNombres() + " " + s.getApellidos());
        holder.txtEstado.setText("Estado: " + s.getEstado());
        holder.txtAfinidad.setText("Afinidad: " + s.getAfinidad() + "%");
        holder.txtUbicacion.setText("Ubicación: " + s.getUbicacion());

        Glide.with(context)
                .load(s.getFotoUrl())
                .placeholder(R.drawable.ic_person)
                .into(holder.imgPerfil);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtEstado, txtAfinidad, txtUbicacion;
        ImageView imgPerfil;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombreSolicitud);
            txtEstado = itemView.findViewById(R.id.txtEstadoSolicitud);
            txtAfinidad = itemView.findViewById(R.id.txtAfinidadSolicitud);
            txtUbicacion = itemView.findViewById(R.id.txtUbicacionSolicitud);
            imgPerfil = itemView.findViewById(R.id.imgSolicitud);
        }
    }
}
