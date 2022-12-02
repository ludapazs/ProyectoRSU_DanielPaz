package com.example.rsuproject.Fragments.Trees.Procedures;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.example.rsuproject.Fragments.Trees.Evolutions.EvolutionsFragment;
import com.example.rsuproject.Fragments.Trees.Trees;
import com.example.rsuproject.Fragments.Trees.TreesAdapter;
import com.example.rsuproject.Fragments.Trees.TreesShowFragment;
import com.example.rsuproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProceduresFragment extends Fragment {

    private Trees trees;

    public ProceduresFragment(Trees trees) {
        this.trees = trees;
    }

    private ListView lv_procedures_list;
    private FloatingActionButton btnAgregarProcedimiento;
    private Button btnProcedimientosVolver;

    private RequestQueue requestQueue;

    private String api;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private ProceduresAdapter proceduresAdapter;
    private List<Procedures> ListProcedures;

    private TextView procedures_tree_id;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_procedures, container, false);

        getActivity().setTitle("Listado de Procedimientos");

        lv_procedures_list = (ListView) view.findViewById(R.id.procedures_list);
        btnAgregarProcedimiento = (FloatingActionButton) view.findViewById(R.id.btnAgregarProcedimiento);
        btnProcedimientosVolver = (Button) view.findViewById(R.id.btnProcedimientosVolver);
        procedures_tree_id = (TextView) view.findViewById(R.id.procedure_tree_id);

        procedures_tree_id.setText(trees.getName());

        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        btnAgregarProcedimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction.replace(R.id.container_fragment, new ProceduresCreateFragment(trees));
                fragmentTransaction.commit();
            }
        });

        Config config = new Config();
        api = config.getApi()+"api_obtenerprocedimientos";

        ListarProcedimiento();

        lv_procedures_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Procedures procedures = (Procedures) view.getTag();
                fragmentTransaction.replace(R.id.container_fragment, new ProceduresShowFragment(procedures, trees));
                fragmentTransaction.commit();
            }
        });

        btnProcedimientosVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction.replace(R.id.container_fragment, new TreesShowFragment(trees));
                fragmentTransaction.commit();
            }
        });


        return view;
    }

    private void ListarProcedimiento() {
        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Listado de Procedimientos", "Cargando procedimientos", false, false);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(api, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                ListProcedures = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        if (jsonObject.getInt("tree_id") == trees.getId()){
                            ListProcedures.add(new Procedures(jsonObject.getInt("id"), jsonObject.getString("date"),
                                    jsonObject.getString("description"), jsonObject.getInt("procedure_type_id"),
                                    jsonObject.getInt("tree_id"), jsonObject.getInt("responsible_id"), jsonObject.getInt("user_id"),
                                    jsonObject.getString("description_type")));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                proceduresAdapter = new ProceduresAdapter(getContext(), ListProcedures);

                lv_procedures_list.setAdapter(proceduresAdapter);
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