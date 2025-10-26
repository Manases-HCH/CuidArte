package com.example.cuidarte.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.cuidarte.R;
import com.example.cuidarte.fragmentos.Voluntario.ChatFragment;
import com.example.cuidarte.modelos.ChatItem;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private List<ChatItem> lista;
    private Context context;
    private FragmentManager fragmentManager;

    public ChatListAdapter(List<ChatItem> lista, Context context, FragmentManager fragmentManager) {
        this.lista = lista;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_chat_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatItem c = lista.get(position);
        holder.txtNombre.setText(c.getNombre());
        holder.txtUltimoMensaje.setText(c.getUltimoMensaje());

        Glide.with(context)
                .load(c.getFotoUrl())
                .placeholder(R.drawable.ic_person)
                .into(holder.imgPerfil);

        holder.itemView.setOnClickListener(v -> {
            ChatFragment chatFragment = new ChatFragment();
            Bundle args = new Bundle();
            args.putInt("otro_id", c.getIdUsuario());
            args.putString("nombre_otro", c.getNombre());
            chatFragment.setArguments(args);

            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, chatFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() { return lista.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtUltimoMensaje;
        ImageView imgPerfil;
        ViewHolder(View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombreChat);
            txtUltimoMensaje = itemView.findViewById(R.id.txtUltimoMensaje);
            imgPerfil = itemView.findViewById(R.id.imgPerfilChat);
        }
    }
}
