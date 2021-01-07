package com.example.who_am_i;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.who_am_i.Model.Questions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteAdapter extends BaseAdapter implements Filterable {

        private static final int MAX_RESULTS = 10;

        private final Context mContext;
        private List<String> mResults,start;


        public AutoCompleteAdapter(Context context,ArrayList<String> questions) {
            mContext = context;
            mResults = new ArrayList<String>();
            start =questions;
        }

        @Override
        public int getCount() {
            return mResults.size();
        }

        @Override
        public String  getItem(int index) {
            return mResults.get(index);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


    @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(R.layout.dropdown_item_line, parent, false);
            }
            String questions = getItem(position);
            ((TextView) convertView.findViewById(R.id.text1)).setText(questions);

            return convertView;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        List<String> questions = findQuestions(constraint.toString());
                        // Assign the data to the FilterResults
                        filterResults.values = questions;
                        filterResults.count = questions.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        mResults = (List<String>) results.values;
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }};

            return filter;
        }

        /**
         * Returns a search result for the given book title.
         */
        private List<String> findQuestions(String text) {
            ArrayList<String> newS = new ArrayList<>();
            for(String f : start) {
                if(f.lastIndexOf(text)!=-1)
                    newS.add(f);
            }
            return newS;
        }
    }
