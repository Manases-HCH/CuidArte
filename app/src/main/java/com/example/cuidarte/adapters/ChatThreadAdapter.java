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
import com.example.cuidarte.fragmentos.Voluntario.ChatThread;

import java.util.List;

// ChatThreadAdapter.java
public class ChatThreadAdapter extends RecyclerView.Adapter<ChatThreadAdapter.VH> {

    public interface OnThreadClick {
        void onOpen(ChatThread t);
    }

    private final List<ChatThread> data;
    private final Context ctx;
    private final OnThreadClick listener;

    public ChatThreadAdapter(Context ctx, List<ChatThread> data, OnThreadClick l) {
        this.ctx = ctx; this.data = data; this.listener = l;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_chat_thread, parent, false);
        return new VH(v);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        ChatThread t = data.get(pos);
        h.txtNombre.setText(t.otroNombre);
        h.txtUltimo.setText(t.ultimoMensaje == null ? "Empieza la conversación" : t.ultimoMensaje);
        h.txtHora.setText(t.ultimoFecha == null ? "" : t.ultimoFecha.substring(11,16));
        if (t.noLeidos > 0) {
            h.badge.setVisibility(View.VISIBLE);
            h.badge.setText(String.valueOf(t.noLeidos));
        } else h.badge.setVisibility(View.GONE);

        if (t.otroFoto != null && !t.otroFoto.isEmpty()) {
            Glide.with(ctx).load(t.otroFoto).placeholder(R.drawable.ic_person).into(h.img);
        } else h.img.setImageResource(R.drawable.ic_person);

        h.itemView.setOnClickListener(v -> { if (listener!=null) listener.onOpen(t); });
    }

    @Override public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        ImageView img; TextView txtNombre, txtUltimo, txtHora, badge;
        VH(View v){ super(v);
            img=v.findViewById(R.id.imgAvatar);
            txtNombre=v.findViewById(R.id.txtNombre);
            txtUltimo=v.findViewById(R.id.txtUltimo);
            txtHora=v.findViewById(R.id.txtHora);
            badge=v.findViewById(R.id.badgeNoLeidos);
        }
    }
}

