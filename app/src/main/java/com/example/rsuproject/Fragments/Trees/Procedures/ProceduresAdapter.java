package com.example.rsuproject.Fragments.Trees.Procedures;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rsuproject.Fragments.Trees.Trees;
import com.example.rsuproject.R;

import java.util.List;

public class ProceduresAdapter extends BaseAdapter {

    private Context context;
    private List<Procedures> ListProcedures;

    private TextView procedure_type_list;
    private TextView procedure_date_list;

    public ProceduresAdapter(Context context, List<Procedures> listProcedures) {
        this.context = context;
        ListProcedures = listProcedures;
    }

    @Override
    public int getCount() {
        return ListProcedures.size();
    }

    @Override
    public Object getItem(int i) {
        return ListProcedures.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vista = View.inflate(context, R.layout.procedures_item, null);

        procedure_type_list = (TextView) vista.findViewById(R.id.procedure_type_list);
        procedure_date_list = (TextView) vista.findViewById(R.id.procedure_date_list);

        procedure_type_list.setText(String.valueOf(ListProcedures.get(i).getDescription_type()));
        procedure_date_list.setText(ListProcedures.get(i).getDate());

        vista.setTag(ListProcedures.get(i));
        return vista;
    }
}
