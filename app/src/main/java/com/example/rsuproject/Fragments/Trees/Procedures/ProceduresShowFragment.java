package com.example.rsuproject.Fragments.Trees.Procedures;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.rsuproject.Config.Config;
import com.example.rsuproject.Fragments.Species.Species;
import com.example.rsuproject.Fragments.Trees.Procedures.Responsibles.Responsibles;
import com.example.rsuproject.Fragments.Trees.Procedures.Types.ProceduresTypes;
import com.example.rsuproject.Fragments.Trees.Trees;
import com.example.rsuproject.Fragments.Trees.TreesFragment;
import com.example.rsuproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProceduresShowFragment extends Fragment {

    private Procedures procedures;
    private Trees trees;

    public ProceduresShowFragment(Procedures procedures, Trees trees) {
        this.procedures = procedures;
        this.trees = trees;
    }

    private Button btnVolver;
    private TextView procedure_tree_show;
    private TextView procedure_date_show;
    private TextView procedure_type_show;
    private TextView procedure_responsible_show;
    private TextView procedure_description_show;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private String apiType, apiResponsible;

    private ProceduresTypes procedureType;
    private Responsibles responsible;

    private RequestQueue requestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_procedures_show, container, false);
        getActivity().setTitle("Procedimiento N°"+procedures.getId());

        procedure_tree_show = (TextView) view.findViewById(R.id.procedure_tree_show);
        procedure_date_show = (TextView) view.findViewById(R.id.procedure_date_show);
        procedure_type_show = (TextView) view.findViewById(R.id.procedure_type_show);
        procedure_responsible_show = (TextView) view.findViewById(R.id.procedure_responsible_show);
        procedure_description_show = (TextView) view.findViewById(R.id.procedure_description_show);

        btnVolver = (Button) view.findViewById(R.id.btnVolver);

        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        procedure_tree_show.setText(trees.getName());
        procedure_date_show.setText(procedures.getDate());
        //procedure_type_show.setText( String.valueOf(procedures.getProcedure_type_id()));
        procedure_responsible_show.setText( String.valueOf(procedures.getResponsible_id()));
        procedure_description_show.setText(procedures.getDescription());

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction.replace(R.id.container_fragment,new ProceduresFragment(trees));
                fragmentTransaction.commit();
            }
        });

        Config config = new Config();
        apiType = config.getApi()+"api_obtenerprocedimientotipo/"+procedures.getProcedure_type_id();
        apiResponsible = config.getApi()+"api_obtenerresponsable/"+procedures.getResponsible_id();

        ObtenerTipoProcedimiento();
        ObtenerResponsable();

        return view;
    }

    private void ObtenerTipoProcedimiento() {

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Tipo de procedimiento"
                , "Cargando tipo de procedimiento", false, false);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(apiType, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                //ArrayList<String> lista = new ArrayList<>();
                //Toast.makeText(getContext(), apiType, Toast.LENGTH_SHORT).show();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        //lista.add(jsonObject.getString("name"));
                        procedureType = new ProceduresTypes(jsonObject.getInt("id"), jsonObject.getString("name"),
                                jsonObject.getString("description"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.list_item,lista);


                procedure_type_show.setText(procedureType.getName());
                //sp_family_id_c.setAdapter(familiesAdapter);

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

    private void ObtenerResponsable() {

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Tipo de procedimiento"
                , "Cargando tipo de procedimiento", false, false);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(apiResponsible, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                //ArrayList<String> lista = new ArrayList<>();
                //Toast.makeText(getContext(), apiType, Toast.LENGTH_SHORT).show();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        //lista.add(jsonObject.getString("name"));
                        responsible = new Responsibles(jsonObject.getInt("id"), jsonObject.getString("dni"),
                                jsonObject.getString("name"), jsonObject.getString("lastname"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.list_item,lista);


                procedure_responsible_show.setText(responsible.getLastname()+ " " +responsible.getDni());
                //sp_family_id_c.setAdapter(familiesAdapter);

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