package com.example.rsuproject.Fragments.Trees;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
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
import com.example.rsuproject.Fragments.Species.Species;
import com.example.rsuproject.Fragments.Species.SpeciesAdapterA;
import com.example.rsuproject.Fragments.Species.SpeciesFragment;
import com.example.rsuproject.Fragments.Zones.Zones;
import com.example.rsuproject.Fragments.Zones.ZonesAdapterA;
import com.example.rsuproject.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class TreesEditFragment extends Fragment {

    private Trees trees;

    public TreesEditFragment(Trees trees) {
        this.trees = trees;
    }

    private Button btnCancelarArbolEdit, btnActualizarArbol, btnEliminarArbol;
    private TextInputEditText tree_name_edit;
    private TextInputEditText tree_description_edit;
    private TextInputEditText tree_birth_date_edit;
    private TextInputEditText tree_latitude_edit;
    private TextInputEditText tree_longitude_edit;
    private AutoCompleteTextView specie_id_c_edit;
    private AutoCompleteTextView zone_id_c_edit;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private int specie_id, zone_id;
    private String apiActualiza, apiElimina, apiSpecies, apiZones, apiEspecie, apiZona;

    private Integer tree_usuario;

    private RequestQueue requestQueue;

    private SpeciesAdapterA speciesAdapter;
    private ZonesAdapterA zonesAdapter;

    private List<Species> ListSpecie;
    private List<Zones> ListZones;

    private Species specie;
    private Zones zone;

    private int dia, mes, annio;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trees_edit, container, false);
        getActivity().setTitle("Edición de Arboles");

        tree_name_edit = (TextInputEditText) view.findViewById(R.id.tree_name_edit);
        tree_description_edit = (TextInputEditText) view.findViewById(R.id.tree_description_edit);
        tree_birth_date_edit = (TextInputEditText) view.findViewById(R.id.tree_birth_date_edit);
        tree_latitude_edit = (TextInputEditText) view.findViewById(R.id.tree_latitude_edit);
        tree_longitude_edit = (TextInputEditText) view.findViewById(R.id.tree_longitude_edit);

        btnCancelarArbolEdit = (Button) view.findViewById(R.id.btnCancelarArbolEdit);
        btnActualizarArbol = (Button) view.findViewById(R.id.btnActualizarArbol);
        btnEliminarArbol = (Button) view.findViewById(R.id.btnElimarArbol);

        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        specie_id_c_edit = view.findViewById(R.id.tree_specie_id_edit);
        zone_id_c_edit = view.findViewById(R.id.tree_zone_id_edit);

        tree_name_edit.setText(trees.getName());
        tree_description_edit.setText(trees.getDescription());
        tree_birth_date_edit.setText(trees.getBirth_date());
        tree_latitude_edit.setText(trees.getLatitude());
        tree_longitude_edit.setText(trees.getLongitude());
        tree_usuario = trees.getUser_id();

        specie_id = trees.getSpecie_id();
        zone_id = trees.getZone_id();

        Config config = new Config();
        apiActualiza = config.getApi()+"api_actualizararbol";
        apiElimina = config.getApi()+"api_eliminararbol";
        apiSpecies = config.getApi()+"api_obtenerespecies";
        apiZones = config.getApi()+"api_obtenerzonas";

        apiEspecie = config.getApi()+"api_obtenerespecie/"+specie_id;
        apiZona = config.getApi()+"api_obtenerzona/"+zone_id;

        btnCancelarArbolEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction.replace(R.id.container_fragment,new TreesShowFragment(trees));
                fragmentTransaction.commit();
            }
        });

        btnActualizarArbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActualizarArbol();
            }
        });

        btnEliminarArbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Elimnación de Arboles");
                alert.setMessage("¿Seguro de eliminar?");
                alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        EliminarArbol();
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

        ListarEspecieSeleccionada();
        ListarZonaSeleccionada();

        ListarEspecie();
        ListarZona();


        specie_id_c_edit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                specie_id = ListSpecie.get(i).getId();
            }
        });

        zone_id_c_edit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                zone_id = ListZones.get(i).getId();
            }
        });

        tree_birth_date_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Birth_date();
            }
        });


        Calendar date = Calendar.getInstance();

        dia = date.get(Calendar.DATE);
        mes = date.get(Calendar.MONTH);
        annio = date.get(Calendar.YEAR);

        return view;
    }

    private void ActualizarArbol(){
        final ProgressDialog progressDialog = ProgressDialog.show(getContext(),"Actualizaciòn de Arboles"
                ,"Actualizando arboles",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiActualiza, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    progressDialog.cancel();

                    Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();

                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");


                    //Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Actualización de Arboles");
                    alert.setMessage(message);
                    alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            fragmentTransaction.replace(R.id.container_fragment,new TreesFragment());
                            fragmentTransaction.commit();
                        }
                    });

                    AlertDialog dialog = alert.create();
                    dialog.show();



                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
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
                    parametros.put("id", trees.getId());
                    parametros.put("name", tree_name_edit.getText().toString());
                    parametros.put("birth_date", tree_birth_date_edit.getText().toString() + " 00:00:00");
                    parametros.put("description", tree_description_edit.getText().toString());
                    parametros.put("latitude", tree_latitude_edit.getText().toString());
                    parametros.put("longitude", tree_longitude_edit.getText().toString());
                    parametros.put("specie_id", String.valueOf(specie_id));
                    parametros.put("zone_id", String.valueOf(zone_id));
                    parametros.put("user_id", String.valueOf(tree_usuario));
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

    private void ListarEspecie() {

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Listado de Especies"
                , "Cargando especies", false, false);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(apiSpecies, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                //ArrayList<String> lista = new ArrayList<>();

                ListSpecie = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        //lista.add(jsonObject.getString("name"));
                        ListSpecie.add(new Species(jsonObject.getInt("id"),
                                jsonObject.getString("name"),
                                jsonObject.getString("description"),
                                jsonObject.getInt("family_id")));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                speciesAdapter = new SpeciesAdapterA(getContext(), R.layout.fragment_species_create, R.id.item_data, ListSpecie);
                //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.list_item,lista);

                specie_id_c_edit.setAdapter(speciesAdapter);
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

    private void ListarZona() {

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Listado de Zonas"
                , "Cargando zonas", false, false);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(apiZones, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                //ArrayList<String> lista = new ArrayList<>();

                ListZones = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        //lista.add(jsonObject.getString("name"));
                        ListZones.add(new Zones(jsonObject.getInt("id"),
                                jsonObject.getInt("area"),
                                jsonObject.getString("description"),
                                jsonObject.getString("name")));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                zonesAdapter = new ZonesAdapterA(getContext(), R.layout.fragment_species_create, R.id.item_data, ListZones);
                //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.list_item,lista);

                zone_id_c_edit.setAdapter(zonesAdapter);
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

    private void EliminarArbol() {

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Elimación de Arboles"
                , "Eliminado arbol", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiElimina, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    progressDialog.cancel();

                    JSONObject jsonObject = new JSONObject(response);

                    int status = jsonObject.getInt("status");
                    String message="";
                    message = jsonObject.getString("message");


                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Eliminación de Arboles");
                    alert.setMessage(message);
                    alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                            if (status==0) {
                                dialogInterface.cancel();
                            }else {
                                dialogInterface.cancel();
                                fragmentTransaction.replace(R.id.container_fragment, new TreesFragment());
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
                    parametros.put("id", trees.getId());

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

    private void ListarEspecieSeleccionada() {

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Especie seleccionada"
                , "Cargando especie", false, false);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(apiEspecie, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                //ArrayList<String> lista = new ArrayList<>();


                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        //lista.add(jsonObject.getString("name"));
                        specie = new Species(jsonObject.getInt("id"), jsonObject.getString("name"),
                                jsonObject.getString("description"), jsonObject.getInt("family_id"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.list_item,lista);

                specie_id_c_edit.setText(specie.getName());
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

    private void ListarZonaSeleccionada() {

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Zona seleccionada"
                , "Cargando zona", false, false);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(apiZona, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                //ArrayList<String> lista = new ArrayList<>();


                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        //lista.add(jsonObject.getString("name"));
                        zone = new Zones(jsonObject.getInt("id"), jsonObject.getInt("area"),
                                jsonObject.getString("description"), jsonObject.getString("name"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.list_item,lista);

                zone_id_c_edit.setText(zone.getName());
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

    private void Birth_date(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                tree_birth_date_edit.setText(FormatoFecha(i2, i1, i));
                Toast.makeText(getContext(), tree_birth_date_edit.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        }, annio, mes, dia);

        datePickerDialog.show();
    }

    private String FormatoFecha(int d, int m, int y){
        String dia, mes, fecha;
        int i_mes;
        if (d<=9){
            dia = "0"+d;
        }else{
            dia = String.valueOf(d);
        }

        i_mes = m+1;
        if (i_mes<=9){
            mes = "0"+i_mes;
        }else{
            mes = String.valueOf(i_mes);
        }

        fecha = y+"-"+mes+"-"+dia;

        return fecha;
    }
}