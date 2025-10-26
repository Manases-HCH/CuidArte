package com.example.cuidarte.fragmentos.Voluntario;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cuidarte.R;
import com.example.cuidarte.adapters.ChatListAdapter;
import com.example.cuidarte.modelos.ChatItem;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import cz.msebera.android.httpclient.Header;

public class ChatListFragment extends Fragment {

    private RecyclerView recyclerChatList;
    private ChatListAdapter adapter;
    private List<ChatItem> listaChats = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat_list, container, false);
        recyclerChatList = v.findViewById(R.id.recyclerChatList);

        recyclerChatList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ChatListAdapter(listaChats, getContext(), getParentFragmentManager());
        recyclerChatList.setAdapter(adapter);

        cargarChats();
        return v;
    }

    private void cargarChats() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("usuarioSesion", Context.MODE_PRIVATE);
        int idVoluntario = prefs.getInt("id_usuario", -1);
        if (idVoluntario == -1) {
            Toast.makeText(getContext(), "Sesión no encontrada", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://192.168.0.104:8012/api/chat_list.php?voluntario_id=" + idVoluntario;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject json = new JSONObject(new String(responseBody));
                    if (json.getBoolean("success")) {
                        JSONArray data = json.getJSONArray("data");
                        listaChats.clear();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj = data.getJSONObject(i);
                            listaChats.add(new ChatItem(
                                    obj.getInt("id_adulto"),
                                    obj.getString("nombre"),
                                    obj.optString("foto_url", ""),
                                    obj.optString("ultimo_mensaje", "")
                            ));
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), json.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "Error al procesar datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getContext(), "Error al conectar con servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
