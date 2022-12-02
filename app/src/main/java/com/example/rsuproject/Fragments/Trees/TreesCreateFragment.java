package com.example.rsuproject.Fragments.Trees;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.Settings;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.SimpleTimeZone;

public class TreesCreateFragment extends Fragment {

    private Button btnCancelarArbol, btnGuardarArbol;
    private TextInputEditText tree_name;
    private TextInputEditText tree_description;
    private TextInputEditText tree_birth_date;
    private TextInputEditText tree_planting_date;
    public TextInputEditText tree_latitude;
    public TextInputEditText tree_longitude;
    private AutoCompleteTextView specie_id_c;
    private AutoCompleteTextView zone_id_c;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private int specie_id, zone_id;
    private String api, apiSpecies, apiZones;

    private RequestQueue requestQueue;

    private SpeciesAdapterA speciesAdapter;
    private ZonesAdapterA zonesAdapter;

    private List<Species> ListSpecie;
    private List<Zones> ListZones;

    private int dia, mes, annio;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trees_create, container, false);
        getActivity().setTitle("Registro de Arboles");

        tree_name = (TextInputEditText) view.findViewById(R.id.tree_name);
        tree_description = (TextInputEditText) view.findViewById(R.id.tree_description);
        tree_birth_date = (TextInputEditText) view.findViewById(R.id.tree_birth_date);
        tree_planting_date = (TextInputEditText) view.findViewById(R.id.tree_planting_date);
        tree_latitude = (TextInputEditText) view.findViewById(R.id.tree_latitude);
        tree_longitude = (TextInputEditText) view.findViewById(R.id.tree_longitude);

        btnCancelarArbol = (Button) view.findViewById(R.id.btnCancelarArbol);
        btnGuardarArbol = (Button) view.findViewById(R.id.btnGuardarArbol);

        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        specie_id_c = view.findViewById(R.id.tree_specie_id);
        zone_id_c = view.findViewById(R.id.tree_zone_id);

        Config config = new Config();
        api = config.getApi()+"api_guardararbol";
        apiSpecies = config.getApi()+"api_obtenerespecies";
        apiZones = config.getApi()+"api_obtenerzonas";

        btnCancelarArbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction.replace(R.id.container_fragment,new TreesFragment());
                fragmentTransaction.commit();
            }
        });

        btnGuardarArbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistrarArbol();
            }
        });

        ListarEspecie();
        ListarZona();

        iniciar_localizacion();

        specie_id_c.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                specie_id = ListSpecie.get(i).getId();
            }
        });

        zone_id_c.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                zone_id = ListZones.get(i).getId();
            }
        });

        tree_birth_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Birth_date();
            }
        });

        tree_planting_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Planting_date();
            }
        });

        Calendar date = Calendar.getInstance();

        dia = date.get(Calendar.DATE);
        mes = date.get(Calendar.MONTH);
        annio = date.get(Calendar.YEAR);

        return view;
    }

    private void RegistrarArbol(){
        final ProgressDialog progressDialog = ProgressDialog.show(getContext(),"Registro de Arboles"
                ,"Registrando arboles",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, api, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    progressDialog.cancel();

                    Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();

                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");


                    //Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Registro de Arboles");
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
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

                try {
                    parametros.put("name", tree_name.getText().toString());
                    parametros.put("birth_date", tree_birth_date.getText().toString() + " 00:00:00");
                    parametros.put("planting_date", tree_planting_date.getText().toString() +" 00:00:00");
                    parametros.put("description", tree_description.getText().toString());
                    parametros.put("latitude", tree_latitude.getText().toString());
                    parametros.put("longitude", tree_longitude.getText().toString());
                    parametros.put("specie_id", String.valueOf(specie_id));
                    parametros.put("zone_id", String.valueOf(zone_id));
                    parametros.put("user_id", 1);
                    //Toast.makeText(getContext(), parametros.toString(), Toast.LENGTH_SHORT).show();
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

                specie_id_c.setAdapter(speciesAdapter);
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

                zone_id_c.setAdapter(zonesAdapter);
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

    private void iniciar_localizacion(){
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        Localizacion localizacion = new Localizacion();
        localizacion.setLocalizacion(this);

        final Boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gpsEnabled){
            Intent propiedades = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(propiedades);
        }

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) localizacion);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) localizacion);
    }

    private void Birth_date(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                tree_birth_date.setText(FormatoFecha(i2, i1, i));
                Toast.makeText(getContext(), tree_birth_date.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        }, annio, mes, dia);

        datePickerDialog.show();
    }

    private void Planting_date(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                tree_planting_date.setText(FormatoFecha(i2, i1, i));
                Toast.makeText(getContext(), tree_planting_date.getText().toString(), Toast.LENGTH_SHORT).show();
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