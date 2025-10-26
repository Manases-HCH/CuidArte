package com.example.cuidarte.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cuidarte.R;
import com.example.cuidarte.modelos.Mensaje;

import java.util.List;

public class MensajeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ENVIADO = 1;
    private static final int TYPE_RECIBIDO = 2;

    private Context context;
    private List<Mensaje> lista;
    private int usuarioId;

    public MensajeAdapter(Context context, List<Mensaje> lista, int usuarioId) {
        this.context = context;
        this.lista = lista;
        this.usuarioId = usuarioId;
    }

    @Override
    public int getItemViewType(int position) {
        if (lista.get(position).getRemitenteId() == usuarioId)
            return TYPE_ENVIADO;
        else
            return TYPE_RECIBIDO;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ENVIADO) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_message_sent, parent, false);
            return new SentViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_message_received, parent, false);
            return new ReceivedViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Mensaje mensaje = lista.get(position);

        if (holder instanceof SentViewHolder) {
            ((SentViewHolder) holder).txtMensaje.setText(mensaje.getContenido());
            ((SentViewHolder) holder).txtHora.setText(formatearHora(mensaje.getFechaEnvio()));
        } else if (holder instanceof ReceivedViewHolder) {
            ((ReceivedViewHolder) holder).txtMensaje.setText(mensaje.getContenido());
            ((ReceivedViewHolder) holder).txtHora.setText(formatearHora(mensaje.getFechaEnvio()));
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    // ====== VIEW HOLDERS ======
    static class SentViewHolder extends RecyclerView.ViewHolder {
        TextView txtMensaje, txtHora, TxtHora1;
        SentViewHolder(View itemView) {
            super(itemView);
            txtMensaje = itemView.findViewById(R.id.text_message_sent);
        }
    }

    static class ReceivedViewHolder extends RecyclerView.ViewHolder {
        TextView txtMensaje, txtHora;
        ReceivedViewHolder(View itemView) {
            super(itemView);
            txtMensaje = itemView.findViewById(R.id.text_message_received);
        }
    }

    // ====== FORMATEO ======
    private String formatearHora(String fechaHora) {
        // formato esperado: "2025-10-25 13:20:57"
        if (fechaHora == null || fechaHora.isEmpty()) return "";
        try {
            String[] partes = fechaHora.split(" ");
            if (partes.length == 2) return partes[1].substring(0, 5); // muestra solo hh:mm
        } catch (Exception ignored) {}
        return fechaHora;
    }
}
