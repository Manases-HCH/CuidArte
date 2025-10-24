package com.example.cuidarte.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cuidarte.R;

public class SesionActivity extends AppCompatActivity implements View.OnClickListener {
    EditText txtCorreo, txtClave;
    CheckBox chkRecordar;
    Button btnIngresar, btnSalir;
    TextView lblRegistrate;
    Spinner cboTipoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sesion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtCorreo = findViewById(R.id.sesTxtCorreo);
        txtClave = findViewById(R.id.sesTxtClave);
        chkRecordar = findViewById(R.id.sesChkRecordar);
        btnIngresar = findViewById(R.id.sesBtnIngresar);
        btnSalir = findViewById(R.id.sesBtnSalir);
        lblRegistrate = findViewById(R.id.sesLblRegistro);
        cboTipoUsuario = findViewById(R.id.sesCboTipoUsuario);

        btnIngresar.setOnClickListener(this);
        btnSalir.setOnClickListener(this);
        lblRegistrate.setOnClickListener(this);

        // llenar spinner de tipo de usuario
        llenarTiposUsuario();
    }

    private void llenarTiposUsuario() {
        String[] tipos = {"-- Seleccione usuario --", "Administrador", "Voluntario", "Adulto Mayor"};
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                tipos);
        cboTipoUsuario.setAdapter(adaptador);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sesBtnIngresar)
            ingresar(txtCorreo.getText().toString(), txtClave.getText().toString());
        else if (v.getId() == R.id.sesBtnSalir)
            salir();
        else if (v.getId() == R.id.sesLblRegistro)
            registrarse();
    }

    private void registrarse() {
        Intent registrate = new Intent(this, RegistroActivity.class);
        startActivity(registrate);
    }

    private void salir() {
        finishAffinity();
    }

    private void ingresar(String correo, String clave) {
        int pos = cboTipoUsuario.getSelectedItemPosition();
        Intent intent = null;

        if (pos == 1) { // Administrador
            intent = new Intent(this, HomeAdminActivity.class);
        } else if (pos == 2) { // Profesional
            intent = new Intent(this, HomeProfesionalActivity.class);
        } else if (pos == 3) { // Paciente
            intent = new Intent(this, HomePacienteActivity.class);
        }

        if (intent != null) {
            intent.putExtra("correo", correo);
            startActivity(intent);
        }
    }
}
