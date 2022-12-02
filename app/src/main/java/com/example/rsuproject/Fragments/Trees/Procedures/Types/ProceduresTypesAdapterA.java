package com.example.rsuproject.Fragments.Trees.Procedures.Types;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.rsuproject.Fragments.Species.Species;
import com.example.rsuproject.R;

import java.util.ArrayList;
import java.util.List;

public class ProceduresTypesAdapterA extends ArrayAdapter<ProceduresTypes> {
    Context context;
    int resource, textViewResourceId;
    List<ProceduresTypes> items, tempItems, suggestions;

    public ProceduresTypesAdapterA(Context context, int resource, int textViewResourceId, List<ProceduresTypes> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<ProceduresTypes>(items); // this makes the difference.
        suggestions = new ArrayList<ProceduresTypes>();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, parent, false);
        }
        ProceduresTypes proceduresTypes = items.get(position);
        if (proceduresTypes != null) {
            TextView lblName = (TextView) view.findViewById(R.id.item_data);
            if (lblName != null)
                lblName.setText(proceduresTypes.getName());
            view.setTag(proceduresTypes.getId());
        }
        return view;
    }

    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((ProceduresTypes) resultValue).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (ProceduresTypes proceduresTypes : tempItems) {
                    if (proceduresTypes.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(proceduresTypes);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<ProceduresTypes> filterList = (ArrayList<ProceduresTypes>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (ProceduresTypes proceduresTypes : filterList) {
                    add(proceduresTypes);
                    notifyDataSetChanged();
                }
            }
        }
    };

}
