package com.example.rsuproject.Fragments.Trees.Evolutions;

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
import com.example.rsuproject.Fragments.Trees.Evolutions.States.States;
import com.example.rsuproject.Fragments.Trees.Evolutions.States.StatesAdapterA;
import com.example.rsuproject.Fragments.Trees.Procedures.ProceduresFragment;
import com.example.rsuproject.Fragments.Trees.Procedures.Responsibles.Responsibles;
import com.example.rsuproject.Fragments.Trees.Procedures.Responsibles.ResponsiblesAdapterA;
import com.example.rsuproject.Fragments.Trees.Trees;
import com.example.rsuproject.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class EvolutionsCreateFragment extends Fragment {

    private Trees trees;

    public EvolutionsCreateFragment(Trees trees) {
        this.trees = trees;
    }

    private Button btnGuardarEvolucion, btnCancelarEvolucion;
    private TextInputEditText evolution_date;
    private TextInputEditText evolution_height;
    private TextInputEditText evolution_width;
    private AutoCompleteTextView evolution_state_c;
    private TextInputEditText evolution_description;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private int evolution_state_id;

    private String api, apiState;

    private RequestQueue requestQueue;

    private StatesAdapterA statesAdapter;

    private List<States> ListStates;

    private int dia, mes, annio;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_evolutions_create, container, false);
        getActivity().setTitle("Registro de Evoluciones");

        evolution_date = (TextInputEditText) view.findViewById(R.id.evolution_date);
        evolution_height = (TextInputEditText) view.findViewById(R.id.evolution_height);
        evolution_width = (TextInputEditText) view.findViewById(R.id.evolution_width);
        evolution_state_c = (AutoCompleteTextView) view.findViewById(R.id.evolution_state_id);
        evolution_description = (TextInputEditText) view.findViewById(R.id.evolution_description);

        btnCancelarEvolucion = (Button) view.findViewById(R.id.btnCancelarEvolucion);
        btnGuardarEvolucion = (Button) view.findViewById(R.id.btnGuardarEvolucion);

        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        Config config = new Config();
        api = config.getApi()+"api_guardarevolucion";
        apiState = config.getApi()+"api_obtenerestados_desc";

        btnCancelarEvolucion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction.replace(R.id.container_fragment,new EvolutionsFragment(trees));
                fragmentTransaction.commit();
            }
        });

        btnGuardarEvolucion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistrarEvolucion();
            }
        });

        ListarEstadoEvolucion();

        evolution_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Evolution_date();
            }
        });

        Calendar date = Calendar.getInstance();

        dia = date.get(Calendar.DATE);
        mes = date.get(Calendar.MONTH);
        annio = date.get(Calendar.YEAR);

        evolution_state_c.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                evolution_state_id = ListStates.get(i).getId();
            }
        });

        return view;
    }

    private void RegistrarEvolucion(){
        final ProgressDialog progressDialog = ProgressDialog.show(getContext(),"Registro de Evoluciones"
                ,"Registrando evoluciones",false,false);
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
                    alert.setTitle("Registro de Evoluciones");
                    alert.setMessage(message);
                    alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            fragmentTransaction.replace(R.id.container_fragment,new EvolutionsFragment(trees));
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
                    parametros.put("date", evolution_date.getText().toString());
                    parametros.put("height", evolution_height.getText().toString());
                    parametros.put("width", evolution_width.getText().toString());
                    parametros.put("description", evolution_description.getText().toString());
                    parametros.put("tree_id", String.valueOf(trees.getId()));
                    parametros.put("state_id", String.valueOf(evolution_state_id));
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

    private void ListarEstadoEvolucion(){

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Estado de evolución"
                , "Cargando estados", false, false);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(apiState, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                //ArrayList<String> lista = new ArrayList<>();

                ListStates = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        //lista.add(jsonObject.getString("name"));
                        ListStates.add(new States(jsonObject.getInt("id"),
                                jsonObject.getString("name"), jsonObject.getString("description")));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                statesAdapter = new StatesAdapterA(getContext(), R.layout.fragment_evolutions_create, R.id.item_data, ListStates);
                //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.list_item,lista);

                evolution_state_c.setAdapter(statesAdapter);
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

    private void Evolution_date(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                evolution_date.setText(FormatoFecha(i2, i1, i));
                Toast.makeText(getContext(), evolution_date.getText().toString(), Toast.LENGTH_SHORT).show();
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