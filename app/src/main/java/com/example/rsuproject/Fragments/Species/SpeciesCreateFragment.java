package com.example.rsuproject.Fragments.Species;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rsuproject.Config.Config;
import com.example.rsuproject.Fragments.Families.Families;
import com.example.rsuproject.Fragments.Families.FamiliesAdapterA;
import com.example.rsuproject.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SpeciesCreateFragment extends Fragment {

    private Button btnCancelarEspecie, btnGuardarEspecie;
    private TextInputEditText specie_name;
    private TextInputEditText specie_description;
    private AutoCompleteTextView family_id_c;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private int family_id;

    private String api, apiFamilies;

    private RequestQueue requestQueue;

    private FamiliesAdapterA familiesAdapter;

    private List<Families> ListFamily;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_species_create, container, false);
        getActivity().setTitle("Registro de Especies");

        specie_name=(TextInputEditText) view.findViewById(R.id.specie_name);
        specie_description=(TextInputEditText) view.findViewById(R.id.specie_description);

        btnCancelarEspecie=(Button) view.findViewById(R.id.btnCancelarEspecie);
        btnGuardarEspecie=(Button) view.findViewById(R.id.btnGuardarEspecie);

        fragmentManager=getActivity().getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();

        family_id_c = view.findViewById(R.id.specie_family_id);

        Config config = new Config();
        api = config.getApi()+"api_guardarespecie";
        apiFamilies = config.getApi()+"api_obtenerfamilias";

        btnCancelarEspecie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction.replace(R.id.container_fragment,new SpeciesFragment());
                fragmentTransaction.commit();
            }
        });

        btnGuardarEspecie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RegistrarEspecie();
                //Toast.makeText(getContext(), String.valueOf(family_id), Toast.LENGTH_SHORT).show();
            }
        });

        ListarFamilia();

        family_id_c.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                family_id = ListFamily.get(i).getId();
            }
        });


        // Inflate the layout for this fragment
        return view;
    }

    private void RegistrarEspecie(){

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(),"Registro de Especies"
                ,"Registrando especies",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, api, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    progressDialog.cancel();

                    //Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();

                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");


                    //Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Registro de Especies");
                    alert.setMessage(message);
                    alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            fragmentTransaction.replace(R.id.container_fragment,new SpeciesFragment());
                            fragmentTransaction.commit();
                        }
                    });

                    AlertDialog dialog = alert.create();
                    dialog.show();



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            public byte[] getBody() throws AuthFailureError {

                JSONObject parametros = new JSONObject();

                try {
                    parametros.put("name", specie_name.getText().toString());
                    parametros.put("description", specie_description.getText().toString());
                    parametros.put("family_id", String.valueOf(family_id));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String requestBody = parametros.toString();

                return requestBody.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void ListarFamilia() {

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Listado de Familias"
                , "Cargando familias", false, false);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(apiFamilies, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                //ArrayList<String> lista = new ArrayList<>();

                ListFamily = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        //lista.add(jsonObject.getString("name"));
                        ListFamily.add(new Families(jsonObject.getInt("id"),
                                jsonObject.getString("name"), jsonObject.getString("description")));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                familiesAdapter = new FamiliesAdapterA(getContext(), R.layout.fragment_species_create, R.id.item_data, ListFamily);
                //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.list_item,lista);

                family_id_c.setAdapter(familiesAdapter);
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