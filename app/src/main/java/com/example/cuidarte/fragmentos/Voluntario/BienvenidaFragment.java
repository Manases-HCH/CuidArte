package com.example.cuidarte.fragmentos.Voluntario;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cuidarte.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.cuidarte.actividades.HomeProfesionalActivity;

public class BienvenidaFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bienvenida, container, false);

        Button buttonEntrar = view.findViewById(R.id.button_entrar);
        buttonEntrar.setOnClickListener(v -> {
            // Encuentra la BottomNavigationView de la actividad y selecciona el ítem del chat
            BottomNavigationView bottomNav = ((HomeProfesionalActivity) requireActivity()).findViewById(R.id.bottom_navigation);
            bottomNav.setSelectedItemId(R.id.nav_chat);
        });

        return view;
    }
}