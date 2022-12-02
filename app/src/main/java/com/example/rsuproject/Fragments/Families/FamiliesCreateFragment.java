package com.example.rsuproject.Fragments.Families;

import android.app.AlertDialog;
import android.app.DownloadManager;
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
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rsuproject.Config.Config;
import com.example.rsuproject.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


public class FamiliesCreateFragment extends Fragment {

    private Button btnCancelarFamilia, btnGuardarFamilia;
    private TextInputEditText family_name;
    private TextInputEditText family_description;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private String api;

    private RequestQueue requestQueue;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_families_create, container, false);
        getActivity().setTitle("Registro de Familias");

        family_name=(TextInputEditText) view.findViewById(R.id.family_name);
        family_description=(TextInputEditText) view.findViewById(R.id.family_description);

        btnCancelarFamilia=(Button) view.findViewById(R.id.btnCancelarFamilia);
        btnGuardarFamilia=(Button) view.findViewById(R.id.btnGuardarFamilia);

        fragmentManager=getActivity().getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();

        Config config = new Config();
        api = config.getApi()+"api_guardarfamilia";

        btnCancelarFamilia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction.replace(R.id.container_fragment,new FamiliesFragment());
                fragmentTransaction.commit();
            }
        });

        btnGuardarFamilia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RegistrarFamilia();
                //Toast.makeText(getContext(), family_name.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void RegistrarFamilia(){

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(),"Registro de Familias"
                ,"Registrando familia",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, api, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    progressDialog.cancel();

                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");

                    //Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Registro de Familias");
                    alert.setMessage(message);
                    alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            fragmentTransaction.replace(R.id.container_fragment,new FamiliesFragment());
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
                    parametros.put("name", family_name.getText().toString());
                    parametros.put("description", family_description.getText().toString());
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
}