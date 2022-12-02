package com.example.rsuproject.Fragments.Trees.Evolutions;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rsuproject.Fragments.Trees.Procedures.Procedures;
import com.example.rsuproject.R;

import java.util.List;

public class EvolutionsAdapter extends BaseAdapter {
    private Context context;
    private List<Evolutions> ListEvolutions;

    private TextView evolution_date_list;
    private TextView evolution_height_list;
    private TextView evolution_width_list;
    private TextView evolution_state_list;

    public EvolutionsAdapter(Context context, List<Evolutions> listEvolutions) {
        this.context = context;
        ListEvolutions = listEvolutions;
    }

    @Override
    public int getCount() {
        return ListEvolutions.size();
    }

    @Override
    public Object getItem(int i) {
        return ListEvolutions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vista = View.inflate(context, R.layout.evolutions_item, null);

        evolution_date_list = (TextView) vista.findViewById(R.id.evolution_date_list);
        evolution_height_list = (TextView) vista.findViewById(R.id.evolution_height_list);
        evolution_width_list = (TextView) vista.findViewById(R.id.evolution_width_list);
        evolution_state_list = (TextView) vista.findViewById(R.id.evolution_state_list);

        evolution_date_list.setText(ListEvolutions.get(i).getDate());
        evolution_height_list.setText(ListEvolutions.get(i).getHeight());
        evolution_width_list.setText(ListEvolutions.get(i).getWidth());
        evolution_state_list.setText(String.valueOf(ListEvolutions.get(i).getState_id()));

        vista.setTag(ListEvolutions.get(i));
        return vista;
    }
}
