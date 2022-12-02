package com.example.rsuproject.Fragments.Zones;

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

public class ZonesAdapterA extends ArrayAdapter<Zones> {

    Context context;
    int resource, textViewResourceId;
    List<Zones> items, tempItems, suggestions;


    public ZonesAdapterA(Context context, int resource, int textViewResourceId, List<Zones> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<Zones>(items); // this makes the difference.
        suggestions = new ArrayList<Zones>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, parent, false);
        }
        Zones zones = items.get(position);
        if (zones != null) {
            TextView lblName = (TextView) view.findViewById(R.id.item_data);
            if (lblName != null)
                lblName.setText(zones.getName());
            view.setTag(zones.getId());
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
            String str = ((Zones) resultValue).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Zones zones : tempItems) {
                    if (zones.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(zones);
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
            List<Zones> filterList = (ArrayList<Zones>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Zones zones : filterList) {
                    add(zones);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
