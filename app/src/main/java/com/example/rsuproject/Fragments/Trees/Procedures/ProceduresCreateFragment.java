package com.example.rsuproject.Fragments.Trees.Procedures;

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
import com.example.rsuproject.Fragments.Species.Species;
import com.example.rsuproject.Fragments.Species.SpeciesAdapterA;
import com.example.rsuproject.Fragments.Trees.Procedures.Responsibles.Responsibles;
import com.example.rsuproject.Fragments.Trees.Procedures.Responsibles.ResponsiblesAdapterA;
import com.example.rsuproject.Fragments.Trees.Procedures.Types.ProceduresTypes;
import com.example.rsuproject.Fragments.Trees.Procedures.Types.ProceduresTypesAdapterA;
import com.example.rsuproject.Fragments.Trees.Trees;
import com.example.rsuproject.Fragments.Trees.TreesFragment;
import com.example.rsuproject.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ProceduresCreateFragment extends Fragment {

    private Trees trees;

    public ProceduresCreateFragment(Trees trees) {
        this.trees = trees;
    }

    private Button btnGuardarProcedimiento, btnCancelarProcedimiento;
    private TextInputEditText procedure_date;
    private AutoCompleteTextView procedure_type_c;
    private AutoCompleteTextView responsible_id_c;
    private TextInputEditText description;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private int procedure_type_id, responsible_id;

    private String api, apiType, apiResponsibles;

    private RequestQueue requestQueue;

    private ProceduresTypesAdapterA proceduresTypesAdapter;
    private ResponsiblesAdapterA responsiblesAdapter;

    private List<ProceduresTypes> proceduresTypes;
    private List<Responsibles> ListResponsibles;

    private int dia, mes, annio;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_procedures_create, container, false);
        getActivity().setTitle("Registro de Procedmientos");

        procedure_date = (TextInputEditText) view.findViewById(R.id.procedure_date);
        procedure_type_c = (AutoCompleteTextView) view.findViewById(R.id.procedure_type_id);
        responsible_id_c = (AutoCompleteTextView) view.findViewById(R.id.procedure_responsible_id);
        description = (TextInputEditText) view.findViewById(R.id.procedure_description);

        btnCancelarProcedimiento = (Button) view.findViewById(R.id.btnCancelarProcedimiento);
        btnGuardarProcedimiento = (Button) view.findViewById(R.id.btnGuardarProcedimiento);

        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        Config config = new Config();
        api = config.getApi()+"api_guardarprocedimiento";
        apiType = config.getApi()+"api_obtenerprocedimientotipos";
        apiResponsibles = config.getApi()+"api_obtenerresponsables";

        btnCancelarProcedimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction.replace(R.id.container_fragment,new ProceduresFragment(trees));
                fragmentTransaction.commit();
            }
        });

        btnGuardarProcedimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistrarProcedimiento();
            }
        });

        ListarTipoProcedimiento();
        ListarResponsable();

        procedure_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Procedure_date();
            }
        });

        Calendar date = Calendar.getInstance();

        dia = date.get(Calendar.DATE);
        mes = date.get(Calendar.MONTH);
        annio = date.get(Calendar.YEAR);

        procedure_type_c.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                procedure_type_id = proceduresTypes.get(i).getId();
            }
        });

        responsible_id_c.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                responsible_id = ListResponsibles.get(i).getId();
            }
        });

        return view;
    }

    private void RegistrarProcedimiento(){
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
                    alert.setTitle("Registro de Procedimientos");
                    alert.setMessage(message);
                    alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            fragmentTransaction.replace(R.id.container_fragment,new ProceduresFragment(trees));
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
                    parametros.put("date", procedure_date.getText().toString());
                    parametros.put("description", description.getText().toString());
                    parametros.put("procedure_type_id", String.valueOf(procedure_type_id));
                    parametros.put("responsible_id", String.valueOf(responsible_id));
                    parametros.put("tree_id", String.valueOf(trees.getId()));
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

    private void ListarTipoProcedimiento(){

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Listado de tipos"
                , "Cargando tipos de procedimientos", false, false);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(apiType, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                //ArrayList<String> lista = new ArrayList<>();

                proceduresTypes = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        //lista.add(jsonObject.getString("name"));
                        proceduresTypes.add(new ProceduresTypes(jsonObject.getInt("id"),
                                jsonObject.getString("name"),
                                jsonObject.getString("description")));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                proceduresTypesAdapter = new ProceduresTypesAdapterA(getContext(), R.layout.fragment_procedures_create, R.id.item_data, proceduresTypes);
                //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.list_item,lista);

                procedure_type_c.setAdapter(proceduresTypesAdapter);
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

    private void ListarResponsable(){

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Listado de responsables"
                , "Cargando tipos de responsables", false, false);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(apiResponsibles, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                //ArrayList<String> lista = new ArrayList<>();

                ListResponsibles = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        //lista.add(jsonObject.getString("name"));
                        ListResponsibles.add(new Responsibles(jsonObject.getInt("id"),
                                jsonObject.getString("dni"), jsonObject.getString("name"),
                                jsonObject.getString("lastname")));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                responsiblesAdapter = new ResponsiblesAdapterA(getContext(), R.layout.fragment_procedures_create, R.id.item_data, ListResponsibles);
                //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.list_item,lista);

                responsible_id_c.setAdapter(responsiblesAdapter);
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

    private void Procedure_date(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                procedure_date.setText(FormatoFecha(i2, i1, i));
                Toast.makeText(getContext(), procedure_date.getText().toString(), Toast.LENGTH_SHORT).show();
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