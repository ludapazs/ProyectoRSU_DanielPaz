package com.example.rsuproject.Fragments.Species;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTabHost;
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
import com.example.rsuproject.Fragments.Families.FamiliesFragment;
import com.example.rsuproject.Fragments.Trees.TreesFragment;
import com.example.rsuproject.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class SpeciesEditFragment extends Fragment {

    private Species species;

    public SpeciesEditFragment(Species species) {
        this.species = species;
    }

    private Button btnCancelarEspecieEdit, btnActualizarEspecie, btnEliminarEspecie;
    private TextInputEditText specie_name_edit;
    private TextInputEditText specie_description_edit;
    private AutoCompleteTextView specie_family_edit;

    private int family_id;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private String apiActualiza, apiElimina, apiFamilia, apiFamilies;

    private RequestQueue requestQueue;

    private List<Families> ListFamily;
    private Families family;

    private FamiliesAdapterA familiesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_species_edit, container, false);

        getActivity().setTitle("Registro de Especies");

        specie_name_edit = (TextInputEditText) view.findViewById(R.id.specie_name_edit);
        specie_description_edit = (TextInputEditText) view.findViewById(R.id.specie_description_edit);
        specie_family_edit = (AutoCompleteTextView) view.findViewById(R.id.specie_family_id_edit);

        btnCancelarEspecieEdit = (Button) view.findViewById(R.id.btnCancelarEspecieEdit);
        btnActualizarEspecie = (Button) view.findViewById(R.id.btnActualizarEspecie);
        btnEliminarEspecie = (Button) view.findViewById(R.id.btnElimarEspecie);

        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        specie_name_edit.setText(species.getName());
        specie_description_edit.setText(species.getDescription());
        //specie_family_edit.setText();

        family_id = species.getFamily_id();
        //Toast.makeText(getContext(), String.valueOf(family_id), Toast.LENGTH_SHORT).show();

        Config config = new Config();
        apiActualiza = config.getApi()+"api_actualizarespecie";
        apiElimina = config.getApi()+"api_eliminarespecie";
        apiFamilia = config.getApi()+"api_obtenerfamilia/"+family_id;
        apiFamilies = config.getApi()+"api_obtenerfamilias";



        btnCancelarEspecieEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction.replace(R.id.container_fragment,new SpeciesFragment());
                fragmentTransaction.commit();
            }
        });

        btnActualizarEspecie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActualizarEspecie();
            }
        });

        btnEliminarEspecie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Elimnación de Especies");
                alert.setMessage("¿Seguro de eliminar?");
                alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        EliminarEspecie();
                    }
                });
                alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog dialog = alert.create();
                dialog.show();


            }
        });

        ListarFamiliaSeleccionada();
        ListarFamilia();

        specie_family_edit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                family_id = ListFamily.get(i).getId();
            }
        });

        //family_position = ListFamily.indexOf(family_id);
        //Toast.makeText(getContext(), String.valueOf(family_position), Toast.LENGTH_SHORT).show();

        return view;
    }

    private void ActualizarEspecie(){

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Actualización de Especies"
                , "Actualizando especie", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiActualiza, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    progressDialog.cancel();

                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");
                    String message="";
                    if (status==0) {
                        message = jsonObject.getString("Mensaje");
                    }else if(status==1){
                        message = jsonObject.getString("message");
                    }

                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Actualización de Especies");
                    alert.setMessage(message);
                    alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            fragmentTransaction.replace(R.id.container_fragment, new SpeciesFragment());
                            fragmentTransaction.commit();
                        }
                    });

                    AlertDialog dialog = alert.create();
                    dialog.show();


                } catch (JSONException e) {
                    progressDialog.cancel();
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            public byte[] getBody() throws AuthFailureError {

                JSONObject parametros = new JSONObject();

                try {
                    parametros.put("id", species.getId());
                    parametros.put("name", specie_name_edit.getText().toString());
                    parametros.put("description", specie_description_edit.getText().toString());
                    parametros.put("family_id", String.valueOf(family_id));
                } catch (JSONException e) {
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
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

    private void EliminarEspecie() {

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Elimación de Especies"
                , "Eliminado especie", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiElimina, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    progressDialog.cancel();

                    JSONObject jsonObject = new JSONObject(response);

                    int status = jsonObject.getInt("status");
                    String message="";
                    message = jsonObject.getString("message");

                    //Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Eliminación de Especies");
                    alert.setMessage(message);
                    alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                            if (status==0) {
                                dialogInterface.cancel();
                            }else {
                                dialogInterface.cancel();
                                fragmentTransaction.replace(R.id.container_fragment, new SpeciesFragment());
                                fragmentTransaction.commit();
                            }

                        }
                    });

                    AlertDialog dialog = alert.create();
                    dialog.show();


                } catch (JSONException e) {
                    progressDialog.cancel();
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            public byte[] getBody() throws AuthFailureError {

                JSONObject parametros = new JSONObject();

                try {
                    parametros.put("id", species.getId());

                } catch (JSONException e) {
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
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
                familiesAdapter = new FamiliesAdapterA(getContext(), R.layout.fragment_species_edit, R.id.item_data, ListFamily);
                //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.list_item,lista);

                specie_family_edit.setAdapter(familiesAdapter);
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

    private void ListarFamiliaSeleccionada() {

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Familia seleccionada"
                , "Cargando familia", false, false);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(apiFamilia, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                //ArrayList<String> lista = new ArrayList<>();


                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        //lista.add(jsonObject.getString("name"));
                        family = new Families(jsonObject.getInt("id"),
                                jsonObject.getString("name"), jsonObject.getString("description"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.list_item,lista);

                specie_family_edit.setText(family.getName());
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