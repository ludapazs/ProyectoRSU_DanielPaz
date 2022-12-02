package com.example.rsuproject.Fragments.Species;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.rsuproject.Fragments.Families.Families;
import com.example.rsuproject.R;

import java.util.ArrayList;
import java.util.List;

public class SpeciesAdapterA extends ArrayAdapter<Species> {
    Context context;
    int resource, textViewResourceId;
    List<Species> items, tempItems, suggestions;


    public SpeciesAdapterA(Context context, int resource, int textViewResourceId, List<Species> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<Species>(items); // this makes the difference.
        suggestions = new ArrayList<Species>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, parent, false);
        }
        Species species = items.get(position);
        if (species != null) {
            TextView lblName = (TextView) view.findViewById(R.id.item_data);
            if (lblName != null)
                lblName.setText(species.getName());
            view.setTag(species.getId());
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
            String str = ((Species) resultValue).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Species species : tempItems) {
                    if (species.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(species);
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
            List<Species> filterList = (ArrayList<Species>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Species species : filterList) {
                    add(species);
                    notifyDataSetChanged();
                }
            }
        }
    };

}
