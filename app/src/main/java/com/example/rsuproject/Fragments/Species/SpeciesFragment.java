package com.example.rsuproject.Fragments.Species;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.rsuproject.Config.Config;
import com.example.rsuproject.Fragments.Families.Families;
import com.example.rsuproject.Fragments.Families.FamiliesEditFragment;
import com.example.rsuproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SpeciesFragment extends Fragment {

    private ListView lv_species_list;
    private FloatingActionButton btnAgregarEspecie;

    private RequestQueue requestQueue;

    private String api;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private SpeciesAdapter speciesAdapter;
    private List<Species> ListSpecies;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_species, container, false);

        getActivity().setTitle("Listado de Especies");

        lv_species_list = (ListView) view.findViewById(R.id.species_list);
        btnAgregarEspecie = (FloatingActionButton) view.findViewById(R.id.btnAgregarEspecie);

        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        btnAgregarEspecie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction.replace(R.id.container_fragment, new SpeciesCreateFragment());
                fragmentTransaction.commit();
            }
        });

        Config config = new Config();
        api = config.getApi()+"api_obtenerespecies";

        ListarEspecies();

        lv_species_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Species species = (Species) view.getTag();

                fragmentTransaction.replace(R.id.container_fragment,new SpeciesEditFragment(species));
                fragmentTransaction.commit();

                //Toast.makeText(getContext(), view.getTag().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void ListarEspecies(){
        final ProgressDialog progressDialog = ProgressDialog.show(getContext(),"Listado de Especies"
                ,"Cargando especies",false,false);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(api, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                ListSpecies = new ArrayList<>();

                for (int i=0; i<response.length(); i++){
                    try {
                        jsonObject = response.getJSONObject(i);

                        ListSpecies.add(new Species(jsonObject.getInt("id"), jsonObject.getString("name"), jsonObject.getString("description"), jsonObject.getInt("family_id")));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                speciesAdapter = new SpeciesAdapter(getContext(),ListSpecies);

                lv_species_list.setAdapter(speciesAdapter);
                progressDialog.cancel();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }
}