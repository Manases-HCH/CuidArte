package com.example.cuidarte.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cuidarte.R;
import com.example.cuidarte.modelos.Solicitud;

import java.util.List;

public class SolicitudAdapter extends RecyclerView.Adapter<SolicitudAdapter.ViewHolder> {

    private final List<Solicitud> lista;
    private final Context context;

    public SolicitudAdapter(List<Solicitud> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_solicitud, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Solicitud s = lista.get(position);

        // 📋 Mostrar los datos
        holder.txtNombre.setText(s.getNombres() + " " + s.getApellidos());
        holder.txtEstado.setText("Estado: " + s.getEstado());
        holder.txtAfinidad.setText("Afinidad: " + s.getAfinidad() + "%");
        holder.txtUbicacion.setText("Ubicación: " + s.getUbicacion());

        // 🖼️ Cargar imagen con Glide
        Glide.with(context)
                .load(s.getFotoUrl())
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .into(holder.imgPerfil);

        // 📍 Botón Ver ubicación
        holder.btnVerUbicacion.setOnClickListener(v -> {
            if (s.getLatitud() != 0 && s.getLongitud() != 0) {
                String uri = "geo:" + s.getLatitud() + "," + s.getLongitud() +
                        "?q=" + s.getLatitud() + "," + s.getLongitud() +
                        "(" + s.getNombres() + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Ubicación no disponible", Toast.LENGTH_SHORT).show();
            }
        });

        // 🤝 Botón Aceptar solicitud
        holder.btnAceptar.setOnClickListener(v -> {
            Toast.makeText(context,
                    "Solicitud aceptada de " + s.getNombres(),
                    Toast.LENGTH_SHORT).show();

            // TODO: Puedes hacer aquí una llamada a la API
            // para actualizar el estado del emparejamiento.
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    // 🧩 ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtEstado, txtAfinidad, txtUbicacion;
        ImageView imgPerfil;
        Button btnVerUbicacion, btnAceptar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombreSolicitud);
            txtEstado = itemView.findViewById(R.id.txtEstadoSolicitud);
            txtAfinidad = itemView.findViewById(R.id.txtAfinidadSolicitud);
            txtUbicacion = itemView.findViewById(R.id.txtUbicacionSolicitud);
            imgPerfil = itemView.findViewById(R.id.imgSolicitud);
            btnVerUbicacion = itemView.findViewById(R.id.btnVerUbicacion);
            btnAceptar = itemView.findViewById(R.id.btnAceptar);
        }
    }
}
