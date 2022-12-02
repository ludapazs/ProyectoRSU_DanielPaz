package com.example.rsuproject.Fragments.Families;

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
import com.example.rsuproject.Fragments.Trees.TreesFragment;
import com.example.rsuproject.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;


public class FamiliesEditFragment extends Fragment {
    private Families families;

    public FamiliesEditFragment(Families families) {
        this.families = families;
    }


    private Button btnCancelarFamiliaEdit, btnActualizaFamilia, btnElimarFamilia;
    private TextInputEditText family_name_edit;
    private TextInputEditText family_description_edit;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private String apiActualiza, apiElimina;

    private RequestQueue requestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_families_edit, container, false);


        getActivity().setTitle("Edición de Familias");

        family_name_edit = (TextInputEditText) view.findViewById(R.id.family_name_edit);
        family_description_edit = (TextInputEditText) view.findViewById(R.id.family_description_edit);

        btnCancelarFamiliaEdit = (Button) view.findViewById(R.id.btnCancelarFamiliaEdit);
        btnActualizaFamilia = (Button) view.findViewById(R.id.btnActualizaFamilia);
        btnElimarFamilia = (Button) view.findViewById(R.id.btnElimarFamilia);

        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        family_name_edit.setText(families.getName());
        family_description_edit.setText(families.getDescription());

        Config config = new Config();
        apiActualiza = config.getApi() + "api_actualizarfamilia";
        apiElimina = config.getApi() + "api_eliminarfamilia";

        btnActualizaFamilia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActualizarFamilia();
            }
        });

        btnElimarFamilia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Elimnación de Familias");
                alert.setMessage("¿Seguro de eliminar?");
                alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        EliminarFamilia();
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

        btnCancelarFamiliaEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction.replace(R.id.container_fragment,new FamiliesFragment());
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    private void ActualizarFamilia() {

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Actualización de Familias"
                , "Actualizando familia", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiActualiza, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    progressDialog.cancel();

                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");

                    //Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Actualización de Familias");
                    alert.setMessage(message);
                    alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            fragmentTransaction.replace(R.id.container_fragment, new FamiliesFragment());
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
                    parametros.put("id", families.getId());
                    parametros.put("name", family_name_edit.getText().toString());
                    parametros.put("description", family_description_edit.getText().toString());
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

    private void EliminarFamilia() {

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Elimación de Familias"
                , "Eliminado familia", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiElimina, new Response.Listener<String>() {
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

                    //Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Eliminación de Familias");
                    alert.setMessage(message);
                    alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                            if (status==0) {
                                dialogInterface.cancel();
                            }else {
                                dialogInterface.cancel();
                                fragmentTransaction.replace(R.id.container_fragment, new FamiliesFragment());
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
                    parametros.put("id", families.getId());

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
}