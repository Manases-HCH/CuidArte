package com.example.cuidarte.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cuidarte.R;
import com.example.cuidarte.modelos.AgendaItem;

import java.util.List;

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.ViewHolder> {

    private Context context;
    private List<AgendaItem> lista;

    public AgendaAdapter(Context context, List<AgendaItem> lista) {
        this.context = context;
        this.lista = lista;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_agenda, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AgendaItem item = lista.get(position);
        holder.txtTitulo.setText(item.titulo);
        holder.txtFecha.setText(item.fecha + " - " + item.hora);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitulo, txtFecha;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTituloAgenda);
            txtFecha = itemView.findViewById(R.id.txtFechaAgenda);
        }
    }
}
