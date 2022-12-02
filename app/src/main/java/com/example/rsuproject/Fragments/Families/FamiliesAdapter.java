package com.example.rsuproject.Fragments.Families;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rsuproject.R;

import java.util.List;

public class FamiliesAdapter extends BaseAdapter {

    private Context context;
    private List<Families> ListFamilies;

    private TextView family_id_list;
    private TextView family_name_list;

    public FamiliesAdapter(Context context, List<Families> listFamilies) {
        this.context = context;
        ListFamilies = listFamilies;
    }

    @Override
    public int getCount() {
        return ListFamilies.size();
    }

    @Override
    public Object getItem(int i) {
        return ListFamilies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vista = View.inflate(context, R.layout.families_item,null);

        family_id_list=(TextView) vista.findViewById(R.id.family_id_list);
        family_name_list=(TextView) vista.findViewById(R.id.family_name_list);

        family_id_list.setText( String.valueOf(ListFamilies.get(i).getId()));
        family_name_list.setText(ListFamilies.get(i).getName());

        vista.setTag(ListFamilies.get(i));

        return vista;
    }
}
