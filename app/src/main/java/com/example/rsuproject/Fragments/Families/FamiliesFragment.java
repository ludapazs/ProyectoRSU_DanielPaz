package com.example.rsuproject.Fragments.Families;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.rsuproject.Config.Config;
import com.example.rsuproject.Fragments.MainFragment;
import com.example.rsuproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FamiliesFragment extends Fragment {

    private ListView lv_families_list;
    private FloatingActionButton btnAgregarFamilia;

    //private ArrayAdapter<String> familiesAdapter;

    private RequestQueue requestQueue;

    private String api;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private FamiliesAdapter familiesAdapter;
    private List<Families> ListFamily;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_families, container, false);;

        getActivity().setTitle("Listado de Familias");

        lv_families_list=(ListView) view.findViewById(R.id.families_list);
        btnAgregarFamilia=(FloatingActionButton) view.findViewById(R.id.btnAgregarFamilia);

        fragmentManager=getActivity().getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();

        btnAgregarFamilia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentTransaction.replace(R.id.container_fragment,new FamiliesCreateFragment());
                fragmentTransaction.commit();

            }
        });

        Config config = new Config();
        api = config.getApi()+"api_obtenerfamilias";

        ListarFamilia();

        lv_families_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Families families = (Families) view.getTag();

                fragmentTransaction.replace(R.id.container_fragment,new FamiliesEditFragment(families));
                fragmentTransaction.commit();

                //Toast.makeText(getContext(), view.getTag().toString(), Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    private void ListarFamilia(){

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(),"Listado de Familias"
                ,"Cargando familias",false,false);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(api, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject=null;

                //familiesAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);

                ListFamily = new ArrayList<>();

                for(int i=0; i<response.length();i++){
                    try {
                        jsonObject = response.getJSONObject(i);

                        //familiesAdapter.add(jsonObject.getString("name"));
                        ListFamily.add(new Families(jsonObject.getInt("id"),
                                jsonObject.getString("name"),jsonObject.getString("description")));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                familiesAdapter= new FamiliesAdapter(getContext(),ListFamily);

                lv_families_list.setAdapter(familiesAdapter);
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