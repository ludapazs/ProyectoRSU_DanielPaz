package com.example.rsuproject.Fragments.Trees.Procedures.Responsibles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.rsuproject.Fragments.Trees.Procedures.Types.ProceduresTypes;
import com.example.rsuproject.R;

import java.util.ArrayList;
import java.util.List;

public class ResponsiblesAdapterA extends ArrayAdapter<Responsibles> {
    Context context;
    int resource, textViewResourceId;
    List<Responsibles> items, tempItems, suggestions;

    public ResponsiblesAdapterA(Context context, int resource, int textViewResourceId, List<Responsibles> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<Responsibles>(items); // this makes the difference.
        suggestions = new ArrayList<Responsibles>();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, parent, false);
        }
        Responsibles responsibles = items.get(position);
        if (responsibles != null) {
            TextView lblName = (TextView) view.findViewById(R.id.item_data);
            if (lblName != null)
                lblName.setText(responsibles.getName());
            view.setTag(responsibles.getId());
        }
        return view;
    }

    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((Responsibles) resultValue).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Responsibles responsibles : tempItems) {
                    if (responsibles.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(responsibles);
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
            List<Responsibles> filterList = (ArrayList<Responsibles>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Responsibles responsibles : filterList) {
                    add(responsibles);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
