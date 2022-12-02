package com.example.rsuproject.Fragments.Trees;

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
import com.example.rsuproject.Fragments.Families.FamiliesAdapter;
import com.example.rsuproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TreesFragment extends Fragment {

    private ListView lv_trees_list;
    private FloatingActionButton btnAgregarArbol;

    private RequestQueue requestQueue;

    private String api;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private TreesAdapter treesAdapter;
    private List<Trees> ListTree;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trees, container, false);

        getActivity().setTitle("Listado de Árboles");

        lv_trees_list = (ListView) view.findViewById(R.id.trees_list);
        btnAgregarArbol = (FloatingActionButton) view.findViewById(R.id.btnAgregarArbol);

        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        btnAgregarArbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction.replace(R.id.container_fragment, new TreesCreateFragment());
                fragmentTransaction.commit();
            }
        });

        api = "http://vps-9a9535eb.vps.ovh.ca/rsu_project/api/trees";

        ListarArbol();

        lv_trees_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Trees trees = (Trees) view.getTag();

                fragmentTransaction.replace(R.id.container_fragment, new TreesShowFragment(trees));
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    private void ListarArbol(){
        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Listado de Arboles", "Cargando arboles", false, false);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(api, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                ListTree = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        ListTree.add(new Trees(jsonObject.getInt("id"), jsonObject.getString("name"),
                                jsonObject.getString("birth_date"), jsonObject.getString("planting_date"),
                                jsonObject.getString("description"), jsonObject.getString("latitude"),
                                jsonObject.getString("longitude"), jsonObject.getInt("specie_id"),
                                jsonObject.getInt("zone_id"), jsonObject.getInt("user_id"),
                                jsonObject.getString("family_name"), jsonObject.getString("species_name")));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                treesAdapter = new TreesAdapter(getContext(), ListTree);

                lv_trees_list.setAdapter(treesAdapter);
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