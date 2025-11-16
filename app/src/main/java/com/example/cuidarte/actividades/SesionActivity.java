package com.example.cuidarte.actividades;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.example.cuidarte.ApiConfig;
import com.example.cuidarte.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.loopj.android.http.*;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

// GOOGLE + FIREBASE
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.FirebaseUser;


public class SesionActivity extends AppCompatActivity implements View.OnClickListener {

    private LoginButton btnLogin;
    private CallbackManager callbackManager;
    EditText txtCorreo, txtClave;
    Spinner cboTipoUsuario;
    Button btnIngresar, btnSalir;
    TextView lblRegistro;
    CheckBox chkRecordar;

    String URL_API = ApiConfig.BASE_URL + "login.php";

    // FIREBASE + GOOGLE
    private GoogleSignInClient googleClient;
    private FirebaseAuth firebaseAuth;
    private static final int RC_GOOGLE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion);

        txtCorreo = findViewById(R.id.sesTxtCorreo);
        txtClave = findViewById(R.id.sesTxtClave);
        cboTipoUsuario = findViewById(R.id.sesCboTipoUsuario);
        chkRecordar = findViewById(R.id.sesChkRecordar);
        btnIngresar = findViewById(R.id.sesBtnIngresar);
        btnSalir = findViewById(R.id.sesBtnSalir);
        lblRegistro = findViewById(R.id.sesLblRegistro);

        btnIngresar.setOnClickListener(this);
        btnSalir.setOnClickListener(this);
        lblRegistro.setOnClickListener(this);

        callbackManager = CallbackManager.Factory.create();


        //     GOOGLE + FIREBASE LOGIN
        // ==============================
        firebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("93917013998-pndfg96bipt24uumatc4o3897ihdsvci.apps.googleusercontent.com")   // EL CLIENTE WEB DE FIREBASE
                .requestEmail()
                .build();

        googleClient = GoogleSignIn.getClient(this, gso);

        SignInButton btnGoogle = findViewById(R.id.btnGoogle);
        btnGoogle.setOnClickListener(v -> iniciarGoogle());
    }

    // ====================================================
    //                  LOGIN NORMAL
    // ====================================================
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sesBtnIngresar) {
            loginUsuario();
        } else if (v.getId() == R.id.sesBtnSalir) {
            finish();
        } else if (v.getId() == R.id.sesLblRegistro) {
            startActivity(new Intent(this, RegistroActivity.class));
        }
    }

    private void loginUsuario() {
        String correo = txtCorreo.getText().toString().trim();
        String clave = txtClave.getText().toString().trim();
        String tipo = cboTipoUsuario.getSelectedItem().toString();

        if (correo.isEmpty() || clave.isEmpty() || tipo.equals("-- Seleccione usuario --")) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestParams params = new RequestParams();
        params.put("correo", correo);
        params.put("clave", clave);
        params.put("tipo_usuario", tipo);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(URL_API, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    JSONObject json = new JSONObject(new String(responseBody));

                    if (json.getBoolean("success")) {
                        JSONObject data = json.getJSONObject("data");

                        int idUsuario = data.getInt("id_usuario");
                        int perfilId = data.isNull("perfil_id") ? -1 : data.getInt("perfil_id");
                        String nombre = data.getString("nombres");
                        String tipoUsuario = data.getString("tipo_usuario");

                        SharedPreferences prefs = getSharedPreferences("usuarioSesion", MODE_PRIVATE);
                        prefs.edit()
                                .putInt("id_usuario", idUsuario)
                                .putInt("perfil_id", perfilId)
                                .putString("tipo_usuario", tipoUsuario)
                                .putString("nombre_usuario", nombre)
                                .apply();

                        navegarSegunTipo(tipoUsuario);

                    } else {
                        Toast.makeText(SesionActivity.this,
                                json.getString("message"),
                                Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(SesionActivity.this,
                            "Error procesando JSON", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(SesionActivity.this, "Error de conexión", Toast.LENGTH_LONG).show();
            }
        });
    }

    // ====================================================
    //                 GOOGLE LOGIN FIREBASE
    // ====================================================

    private void iniciarGoogle() {
        Intent intent = googleClient.getSignInIntent();
        startActivityForResult(intent, RC_GOOGLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GOOGLE) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                if (account == null) {
                    Toast.makeText(this, "No se obtuvo cuenta Google", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseConGoogle(account);

            } catch (ApiException e) {
                Toast.makeText(this, "Google Sign-In falló: " + e.getStatusCode(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseConGoogle(GoogleSignInAccount account) {

        AuthCredential credencial =
                GoogleAuthProvider.getCredential(account.getIdToken(), null);

        firebaseAuth.signInWithCredential(credencial)
                .addOnSuccessListener(authResult -> {

                    FirebaseUser user = firebaseAuth.getCurrentUser();

                    if (user != null) {
                        String correo = user.getEmail();
                        String nombre = user.getDisplayName();
                        String foto = (user.getPhotoUrl() != null)
                                ? user.getPhotoUrl().toString()
                                : "";

                        loginGoogleBackend(correo, nombre, foto);
                    }

                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error Firebase: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show());
    }

    private void loginGoogleBackend(String correo, String nombre, String foto) {

        String url = ApiConfig.BASE_URL + "login_google.php";

        RequestParams params = new RequestParams();
        params.put("correo", correo);
        params.put("nombre", nombre);
        params.put("foto", foto);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    JSONObject json = new JSONObject(new String(responseBody));

                    if (json.getBoolean("success")) {

                        String tipoUsuario = json.getString("tipo_usuario");
                        int idUsuario = json.getInt("id_usuario");

                        SharedPreferences prefs = getSharedPreferences("usuarioSesion", MODE_PRIVATE);
                        prefs.edit()
                                .putInt("id_usuario", idUsuario)
                                .putString("tipo_usuario", tipoUsuario)
                                .putString("nombre_usuario", nombre)
                                .putString("foto_google", foto)
                                .apply();

                        navegarSegunTipo(tipoUsuario);

                    } else {
                        Toast.makeText(SesionActivity.this,
                                json.getString("message"),
                                Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(SesionActivity.this,
                            "Error procesando respuesta servidor",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(SesionActivity.this,
                        "Error conexión Google→backend",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ====================================================
    //             REDIRECCIÓN SEGÚN TIPO
    // ====================================================

    private void navegarSegunTipo(String tipoUsuario) {

        if (tipoUsuario.equalsIgnoreCase("VOLUNTARIO")) {
            startActivity(new Intent(this, HomeProfesionalActivity.class));

        } else if (tipoUsuario.equalsIgnoreCase("ADULTO_MAYOR")) {
            startActivity(new Intent(this, HomeAdultoMayorActivity.class));

        } else {
            startActivity(new Intent(this, HomeAdminActivity.class));
        }

        finish();
    }
}
