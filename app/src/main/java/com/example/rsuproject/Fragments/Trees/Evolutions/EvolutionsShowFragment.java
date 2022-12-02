package com.example.rsuproject.Fragments.Trees.Evolutions;

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
import com.example.rsuproject.Fragments.Trees.Evolutions.States.States;
import com.example.rsuproject.Fragments.Trees.Procedures.ProceduresFragment;
import com.example.rsuproject.Fragments.Trees.Procedures.Responsibles.Responsibles;
import com.example.rsuproject.Fragments.Trees.Trees;
import com.example.rsuproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class EvolutionsShowFragment extends Fragment {

    private Evolutions evolutions;
    private Trees trees;

    public EvolutionsShowFragment(Evolutions evolutions, Trees trees) {
        this.evolutions = evolutions;
        this.trees = trees;
    }

    private Button btnVolver;
    private TextView evolution_tree_show;
    private TextView evolution_date_show;
    private TextView evolution_width_show;
    private TextView evolution_height_show;
    private TextView evolution_state_show;
    private TextView evolution_description_show;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private String apiState;

    private States states;

    private RequestQueue requestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_evolutions_show, container, false);
        getActivity().setTitle("Evolución N°"+evolutions.getId());

        evolution_tree_show = (TextView) view.findViewById(R.id.evolution_tree_show);
        evolution_date_show = (TextView) view.findViewById(R.id.evolution_date_show);
        evolution_width_show = (TextView) view.findViewById(R.id.evolution_width_show);
        evolution_height_show = (TextView) view.findViewById(R.id.evolution_height_show);
        evolution_state_show = (TextView) view.findViewById(R.id.evolution_state_show);
        evolution_description_show = (TextView) view.findViewById(R.id.evolution_description_show);

        btnVolver = (Button) view.findViewById(R.id.btnVolver);

        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        evolution_tree_show.setText(trees.getName());
        evolution_date_show.setText(evolutions.getDate());
        evolution_width_show.setText(evolutions.getWidth());
        evolution_height_show.setText(evolutions.getHeight());
        //evolution_state_show.setText(evolutions.getState_id());  //Falta mostrar nombre del estado
        evolution_description_show.setText(evolutions.getDescription());

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction.replace(R.id.container_fragment,new EvolutionsFragment(trees));
                fragmentTransaction.commit();
            }
        });

        Config config = new Config();
        apiState = config.getApi()+"api_obtenerestados_desc";

        ObtenerEstado();

        return view;
    }

    private void ObtenerEstado() {

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Estado de evolución"
                , "Cargando estado de evolución", false, false);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(apiState, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                //ArrayList<String> lista = new ArrayList<>();
                //Toast.makeText(getContext(), apiType, Toast.LENGTH_SHORT).show();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        //lista.add(jsonObject.getString("name"));

                        if (jsonObject.getInt("id") == evolutions.getState_id()){
                            states = new States(jsonObject.getInt("id"),
                                    jsonObject.getString("name"), jsonObject.getString("description"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.list_item,lista);


                evolution_state_show.setText(states.getName());
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