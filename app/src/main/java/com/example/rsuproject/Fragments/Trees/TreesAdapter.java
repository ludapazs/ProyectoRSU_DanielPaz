package com.example.rsuproject.Fragments.Trees;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rsuproject.Config.Config;
import com.example.rsuproject.R;

import java.util.List;

public class TreesAdapter extends BaseAdapter {

    private Context context;
    private List<Trees> ListTrees;

    private TextView tree_id_list;
    private TextView tree_name_list;
    private TextView tree_specie_list;


    public TreesAdapter(Context context, List<Trees> listTrees) {
        this.context = context;
        ListTrees = listTrees;
    }

    @Override
    public int getCount() {
        return ListTrees.size();
    }

    @Override
    public Object getItem(int i) {
        return ListTrees.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vista = View.inflate(context, R.layout.trees_item, null);

        tree_id_list = (TextView)  vista.findViewById(R.id.tree_id_list);
        tree_name_list = (TextView) vista.findViewById(R.id.tree_name_list);
        tree_specie_list = (TextView) vista.findViewById(R.id.tree_specie_list);

        tree_id_list.setText( String.valueOf(ListTrees.get(i).getId()));
        tree_name_list.setText(ListTrees.get(i).getName());
        tree_specie_list.setText(ListTrees.get(i).getSpecies_name());

        vista.setTag(ListTrees.get(i));
        return vista;
    }
}
