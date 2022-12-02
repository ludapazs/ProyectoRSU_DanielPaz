package com.example.rsuproject.Fragments.Trees.Evolutions;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.rsuproject.Config.Config;
import com.example.rsuproject.Fragments.Trees.Procedures.Procedures;
import com.example.rsuproject.Fragments.Trees.Procedures.ProceduresAdapter;
import com.example.rsuproject.Fragments.Trees.Procedures.ProceduresCreateFragment;
import com.example.rsuproject.Fragments.Trees.Procedures.ProceduresShowFragment;
import com.example.rsuproject.Fragments.Trees.Trees;
import com.example.rsuproject.Fragments.Trees.TreesShowFragment;
import com.example.rsuproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class EvolutionsFragment extends Fragment {

    private Trees trees;

    public EvolutionsFragment(Trees trees) {
        this.trees = trees;
    }

    private ListView lv_evolutions_list;
    private FloatingActionButton btnAgregarEvolucion;
    private Button btnEvolucionesVolver;

    private RequestQueue requestQueue;

    private String api;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private EvolutionsAdapter evolutionsAdapter;
    private List<Evolutions> ListEvolutions;

    private TextView evolutions_tree_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_evolutions, container, false);

        getActivity().setTitle("Listado de evoluciones");

        lv_evolutions_list = (ListView) view.findViewById(R.id.evolution_list);
        btnAgregarEvolucion = (FloatingActionButton) view.findViewById(R.id.btnAgregarEvolucion);
        btnEvolucionesVolver = (Button) view.findViewById(R.id.btnEvolutionsVolver);
        evolutions_tree_id = (TextView) view.findViewById(R.id.evolution_tree_id);

        evolutions_tree_id.setText(trees.getName());

        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        btnAgregarEvolucion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction.replace(R.id.container_fragment, new EvolutionsCreateFragment(trees));
                fragmentTransaction.commit();
            }
        });

        Config config = new Config();
        api = config.getApi()+"api_obtenerevoluciones_desc";

        ListarEvoluciones();

        lv_evolutions_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Evolutions evolutions = (Evolutions) view.getTag();
                fragmentTransaction.replace(R.id.container_fragment, new EvolutionsShowFragment(evolutions, trees));
                fragmentTransaction.commit();
            }
        });

        btnEvolucionesVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction.replace(R.id.container_fragment, new TreesShowFragment(trees));
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    private void ListarEvoluciones() {
        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Listado de Evoluciones",
                "Cargando evoluciones", false, false);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(api, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                ListEvolutions = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        if (jsonObject.getInt("tree_id") == trees.getId()){
                            ListEvolutions.add(new Evolutions(jsonObject.getInt("id"), jsonObject.getString("date"),
                                    jsonObject.getString("height"), jsonObject.getString("width"),
                                    jsonObject.getString("description"), jsonObject.getInt("tree_id"),
                                    jsonObject.getInt("state_id"), jsonObject.getInt("user_id")));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                evolutionsAdapter = new EvolutionsAdapter(getContext(), ListEvolutions);

                lv_evolutions_list.setAdapter(evolutionsAdapter);
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