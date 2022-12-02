package com.example.rsuproject.Fragments.Trees.Evolutions.States;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.rsuproject.Fragments.Trees.Procedures.Responsibles.Responsibles;
import com.example.rsuproject.R;

import java.util.ArrayList;
import java.util.List;

public class StatesAdapterA extends ArrayAdapter<States> {
    Context context;
    int resource, textViewResourceId;
    List<States> items, tempItems, suggestions;

    public StatesAdapterA(Context context, int resource, int textViewResourceId, List<States> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<States>(items); // this makes the difference.
        suggestions = new ArrayList<States>();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, parent, false);
        }
        States states = items.get(position);
        if (states != null) {
            TextView lblName = (TextView) view.findViewById(R.id.item_data);
            if (lblName != null)
                lblName.setText(states.getName());
            view.setTag(states.getId());
        }
        return view;
    }

    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((States) resultValue).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (States states : tempItems) {
                    if (states.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(states);
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
            List<States> filterList = (ArrayList<States>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (States states : filterList) {
                    add(states);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
