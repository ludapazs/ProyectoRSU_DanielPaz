package com.example.rsuproject.Fragments.Families;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.rsuproject.R;

import java.util.ArrayList;
import java.util.List;

public class FamiliesAdapterA extends ArrayAdapter<Families> {
    Context context;
    int resource, textViewResourceId;
    List<Families> items, tempItems, suggestions;


    public FamiliesAdapterA(Context context, int resource, int textViewResourceId, List<Families> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<Families>(items); // this makes the difference.
        suggestions = new ArrayList<Families>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, parent, false);
        }
        Families families = items.get(position);
        if (families != null) {
            TextView lblName = (TextView) view.findViewById(R.id.item_data);
            if (lblName != null)
                lblName.setText(families.getName());
            view.setTag(families.getId());
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((Families) resultValue).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Families families : tempItems) {
                    if (families.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(families);
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
            List<Families> filterList = (ArrayList<Families>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Families families : filterList) {
                    add(families);
                    notifyDataSetChanged();
                }
            }
        }
    };


}
