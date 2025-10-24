package com.example.cuidarte.actividades;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cuidarte.R;
import com.example.cuidarte.fragmentos.Voluntario.BienvenidaFragment;
import com.example.cuidarte.fragmentos.Voluntario.ChatFragment;
import com.example.cuidarte.fragmentos.Voluntario.PerfilProfesionalFragment;
import com.example.cuidarte.fragmentos.Voluntario.PreferenciasFragment;
import com.example.cuidarte.fragmentos.Voluntario.ReputacionFragment;
import com.example.cuidarte.fragmentos.Voluntario.SolicitudesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeProfesionalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_profesional);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // Define el comportamiento de la barra de navegación
        // Dentro de tu HomeProfesionalActivity.java
// ...
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_chat) {
                switchFragment(new ChatFragment());
                return true;
            } else if (itemId == R.id.nav_reputacion) {
                switchFragment(new ReputacionFragment());
                return true;
            } else if (itemId == R.id.nav_perfil) {
                switchFragment(new PerfilProfesionalFragment());
                return true;
            } else if (itemId == R.id.nav_preferencias) {
                switchFragment(new PreferenciasFragment());
                return true;
            } else if (itemId == R.id.nav_solicitudes) {
                switchFragment(new SolicitudesFragment()); // Aquí se carga el nuevo fragmento
                return true;
            }
            return false;
        });
// ...

        // Carga el fragmento de bienvenida al iniciar la actividad
        if (savedInstanceState == null) {
            switchFragment(new BienvenidaFragment());
        }
    }

    /**
     * Reemplaza el fragmento actual en el contenedor con el nuevo.
     * @param fragment El nuevo fragmento a mostrar.
     */
    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}