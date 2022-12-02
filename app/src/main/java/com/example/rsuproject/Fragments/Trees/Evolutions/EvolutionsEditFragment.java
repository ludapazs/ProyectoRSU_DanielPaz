package com.example.rsuproject.Fragments.Trees.Evolutions;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.rsuproject.R;

public class EvolutionsEditFragment extends Fragment {

    private Evolutions evolutions;

    public EvolutionsEditFragment(Evolutions evolutions) {
        this.evolutions = evolutions;
    }

    private Button btnActualizar, btnEliminar, btnCancelar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_evolutions_edit, container, false);
    }
}