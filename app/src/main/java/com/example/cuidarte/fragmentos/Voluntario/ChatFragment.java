package com.example.cuidarte.fragmentos.Voluntario;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cuidarte.R;
import com.example.cuidarte.adapters.MensajeAdapter;
import com.example.cuidarte.modelos.Mensaje;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    RecyclerView recyclerView;
    EditText txtMensaje;
    ImageButton btnEnviar;
    List<Mensaje> listaMensajes;
    MensajeAdapter adaptador;
    int usuarioId;

    public ChatFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = v.findViewById(R.id.recycler_view_chat);
        txtMensaje = v.findViewById(R.id.edit_text_message);
        btnEnviar = v.findViewById(R.id.button_send);

        SharedPreferences prefs = getContext().getSharedPreferences("usuarioSesion", Context.MODE_PRIVATE);
        usuarioId = prefs.getInt("usuario_id", -1);

        listaMensajes = new ArrayList<>();
        adaptador = new MensajeAdapter(getContext(), listaMensajes, usuarioId);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adaptador);

        cargarMensajes();

        btnEnviar.setOnClickListener(view -> enviarMensaje());

        return v;
    }

    private void cargarMensajes() {
        String URL = "http://192.168.0.104:8012/api/chat_listar.php?usuario_id=" + usuarioId;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                try {
                    JSONObject json = new JSONObject(response);
                    if (json.getBoolean("success")) {
                        JSONArray mensajes = json.getJSONArray("mensajes");
                        listaMensajes.clear();
                        for (int i = 0; i < mensajes.length(); i++) {
                            JSONObject m = mensajes.getJSONObject(i);
                            listaMensajes.add(new Mensaje(
                                    m.getInt("id"),
                                    m.getInt("remitente_id"),
                                    m.getInt("destinatario_id"),
                                    m.getString("contenido"),
                                    m.getString("fecha_envio")
                            ));
                        }
                        adaptador.notifyDataSetChanged();
                        recyclerView.scrollToPosition(listaMensajes.size() - 1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getContext(), "Error cargando mensajes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enviarMensaje() {
        String mensaje = txtMensaje.getText().toString().trim();
        if (mensaje.isEmpty()) return;

        Toast.makeText(getContext(), "Enviando mensaje: " + mensaje, Toast.LENGTH_SHORT).show();
        txtMensaje.setText(""); // limpiar
    }
}
