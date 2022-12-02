package com.example.rsuproject.Fragments.Trees;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.rsuproject.Fragments.Species.Species;
import com.example.rsuproject.Fragments.Trees.Evolutions.EvolutionsFragment;
import com.example.rsuproject.Fragments.Trees.Procedures.ProceduresFragment;
import com.example.rsuproject.Fragments.Zones.Zones;
import com.example.rsuproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;


public class TreesShowFragment extends Fragment {

    private Trees trees;

    public TreesShowFragment(Trees trees) {
        this.trees = trees;
    }

    private Button btnEditarArbol, btnVolver, btnProcedimiento, btnEvolucion;
    private TextView tree_name_show;
    private TextView tree_specie_show;
    private TextView tree_birth_date_show;
    private TextView tree_planting_date_show;
    private TextView tree_coordinates_show;
    private TextView tree_zone_show;
    private TextView tree_description_show;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;


    private Integer tree_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trees_show, container, false);
        getActivity().setTitle("Árbol N°"+trees.getId());

        tree_name_show = (TextView) view.findViewById(R.id.tree_name_show);
        tree_specie_show = (TextView) view.findViewById(R.id.tree_specie_show);
        tree_birth_date_show = (TextView) view.findViewById(R.id.tree_birth_date_show);
        tree_planting_date_show = (TextView) view.findViewById(R.id.tree_planting_date_show);
        tree_coordinates_show = (TextView) view.findViewById(R.id.tree_coordinates_show);
        tree_zone_show = (TextView) view.findViewById(R.id.tree_zone_show);
        tree_description_show = (TextView) view.findViewById(R.id.tree_description_show);

        btnEditarArbol = (Button) view.findViewById(R.id.btnEditarArbol);
        btnVolver = (Button) view.findViewById(R.id.btnVolver);
        btnProcedimiento = (Button) view.findViewById(R.id.btnProcedimientos);
        btnEvolucion = (Button) view.findViewById(R.id.btnEvoluciones);


        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        tree_name_show.setText(trees.getName());
        tree_specie_show.setText(trees.getSpecies_name());
        tree_birth_date_show.setText(trees.getBirth_date());
        tree_planting_date_show.setText(trees.getPlanting_date());
        tree_coordinates_show.setText(trees.getLatitude() + "; " + trees.getLongitude());
        tree_zone_show.setText(trees.getZone_id().toString());
        tree_description_show.setText(trees.getDescription());
        tree_id = trees.getId();

        btnEditarArbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction.replace(R.id.container_fragment,new TreesEditFragment(trees));
                fragmentTransaction.commit();
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction.replace(R.id.container_fragment,new TreesFragment());
                fragmentTransaction.commit();
            }
        });

        btnProcedimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction.replace(R.id.container_fragment,new ProceduresFragment(trees));
                fragmentTransaction.commit();
            }
        });

        btnEvolucion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction.replace(R.id.container_fragment,new EvolutionsFragment(trees));
                fragmentTransaction.commit();
            }
        });

        return view;
    }
}