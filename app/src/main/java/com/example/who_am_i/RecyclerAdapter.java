package com.example.who_am_i;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.text.DateFormat;
import java.util.ArrayList;
import androidx.recyclerview.widget.RecyclerView;

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final Context context;
        ArrayList<String> list;
        public static final int MESSAGE_TYPE_IN = 1;
        public static final int MESSAGE_TYPE_OUT = 2;

        public RecyclerAdapter(Context context, ArrayList<String> list) { // you can pass other parameters in constructor
            this.context = context;
            this.list = list;
        }

        private class MessageInViewHolder extends RecyclerView.ViewHolder {

            TextView item1;
            MessageInViewHolder(final View itemView) {
                super(itemView);
                item1 = itemView.findViewById(R.id.item1);

            }
            void bind(int position) {

                item1.setText(list.get(position));

            }
        }

        private class MessageOutViewHolder extends RecyclerView.ViewHolder {

            TextView message;
            MessageOutViewHolder(final View itemView) {
                super(itemView);
                message = itemView.findViewById(R.id.item1);

            }
            void bind(int position) {

                message.setText(list.get(position).toString());

            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType%2+1 == MESSAGE_TYPE_IN) {
                return new MessageInViewHolder(LayoutInflater.from(context).inflate(R.layout.item, parent, false));
            }
            return new MessageOutViewHolder(LayoutInflater.from(context).inflate(R.layout.item2, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (position%2+1 == MESSAGE_TYPE_IN) {
                ((MessageInViewHolder) holder).bind(position);
            } else {
                ((MessageOutViewHolder) holder).bind(position);
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position%2;
        }
    }

