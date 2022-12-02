package com.example.rsuproject.Fragments.Species;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rsuproject.R;

import java.util.List;

public class SpeciesAdapter extends BaseAdapter {

    private Context context;
    private List<Species> ListSpecies;

    private TextView species_id_list;
    private TextView species_name_list;

    public SpeciesAdapter(Context context, List<Species> listSpecies) {
        this.context = context;
        ListSpecies = listSpecies;
    }

    @Override
    public int getCount() {
        return ListSpecies.size();
    }

    @Override
    public Object getItem(int i) {
        return ListSpecies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vista = View.inflate(context, R.layout.species_item, null);

        species_id_list = (TextView)  vista.findViewById(R.id.specie_id_list);
        species_name_list = (TextView) vista.findViewById(R.id.specie_name_list);

        species_id_list.setText(String.valueOf(ListSpecies.get(i).getId()));
        species_name_list.setText(ListSpecies.get(i).getName());

        vista.setTag(ListSpecies.get(i));
        return vista;
    }
}
