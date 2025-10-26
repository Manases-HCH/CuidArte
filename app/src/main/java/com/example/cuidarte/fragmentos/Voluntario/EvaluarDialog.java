package com.example.cuidarte.dialogos;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.cuidarte.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class EvaluarDialog extends Dialog {

    private int adultoMayorId; // ID de la persona a evaluar
    private Context context;

    public EvaluarDialog(@NonNull Context context, int adultoMayorId) {
        super(context);
        this.context = context;
        this.adultoMayorId = adultoMayorId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_evaluar);

        RatingBar ratingBar = findViewById(R.id.ratingEvaluacion);
        EditText edtComentario = findViewById(R.id.edtComentario);
        Button btnEnviar = findViewById(R.id.btnEnviarEvaluacion);

        SharedPreferences prefs = context.getSharedPreferences("usuarioSesion", Context.MODE_PRIVATE);
        int voluntarioId = prefs.getInt("id_usuario", -1);

        btnEnviar.setOnClickListener(v -> {
            float puntuacion = ratingBar.getRating();
            String comentario = edtComentario.getText().toString().trim();

            if (puntuacion == 0) {
                Toast.makeText(context, "Debes seleccionar una puntuación", Toast.LENGTH_SHORT).show();
                return;
            }

            if (voluntarioId == -1) {
                Toast.makeText(context, "Error: sesión no encontrada", Toast.LENGTH_SHORT).show();
                return;
            }

            enviarCalificacion(voluntarioId, adultoMayorId, (int) puntuacion, comentario);
        });
    }

    private void enviarCalificacion(int voluntarioId, int adultoMayorId, int puntuacion, String comentario) {
        String url = "http://192.168.0.104:8012/api/reputacion_insertar.php";

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("evaluador_id", voluntarioId);
        params.put("voluntario_id", adultoMayorId); // puede cambiarse si el rol es inverso
        params.put("puntuacion", puntuacion);
        params.put("comentario", comentario);

        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(context, "Evaluación enviada correctamente", Toast.LENGTH_SHORT).show();
                dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(context, "Error al enviar evaluación", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
