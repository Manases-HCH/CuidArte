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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cuidarte.R;
import com.example.cuidarte.adapters.MensajeAdapter;
import com.example.cuidarte.modelos.Mensaje;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private EditText txtMensaje;
    private ImageButton btnEnviar;
    private List<Mensaje> listaMensajes;
    private MensajeAdapter adaptador;

    private int yoId, otroId;
    private String otroNombre;

    private final String URL_BASE = "http://192.168.0.104:8012/api/";

    public ChatFragment() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = v.findViewById(R.id.recycler_view_chat);
        txtMensaje = v.findViewById(R.id.edit_text_message);
        btnEnviar = v.findViewById(R.id.button_send);

        SharedPreferences prefs = requireContext().getSharedPreferences("usuarioSesion", Context.MODE_PRIVATE);
        yoId = prefs.getInt("id_usuario", -1);

        if (getArguments() != null) {
            otroId = getArguments().getInt("otro_id", -1);
            otroNombre = getArguments().getString("otro_nombre", "Chat");
        }

        if (yoId == -1 || otroId == -1) {
            Toast.makeText(getContext(), "Error: sesión o chat inválido", Toast.LENGTH_SHORT).show();
            return v;
        }

        requireActivity().setTitle(otroNombre);

        listaMensajes = new ArrayList<>();
        adaptador = new MensajeAdapter(getContext(), listaMensajes, yoId);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adaptador);

        cargarMensajes();

        btnEnviar.setOnClickListener(view -> enviarMensaje());

        return v;
    }

    private void cargarMensajes() {
        String URL = URL_BASE + "chat_listar.php?yo_id=" + yoId + "&otro_id=" + otroId;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject json = new JSONObject(new String(responseBody));
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
                    Toast.makeText(getContext(), "Error al procesar mensajes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getContext(), "Error de conexión con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enviarMensaje() {
        String mensaje = txtMensaje.getText().toString().trim();
        if (mensaje.isEmpty()) return;

        int destinatarioId = getArguments().getInt("otro_id", -1); // Asegúrate de pasar este argumento
        if (destinatarioId == -1) {
            Toast.makeText(getContext(), "Error: destinatario no definido", Toast.LENGTH_SHORT).show();
            return;
        }

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("remitente_id", yoId);
        params.put("destinatario_id", destinatarioId);
        params.put("contenido", mensaje);

        client.post("http://192.168.0.104:8012/api/chat_enviar.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                try {
                    JSONObject json = new JSONObject(new String(responseBody));
                    if (json.getBoolean("success")) {
                        // Agrega el mensaje localmente sin recargar todo
                        listaMensajes.add(new Mensaje(
                                json.getInt("id"),
                                yoId,
                                destinatarioId,
                                mensaje,
                                json.getString("fecha_envio")
                        ));
                        adaptador.notifyItemInserted(listaMensajes.size() - 1);
                        recyclerView.scrollToPosition(listaMensajes.size() - 1);
                        txtMensaje.setText("");
                    } else {
                        Toast.makeText(getContext(), json.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "Error al procesar respuesta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
