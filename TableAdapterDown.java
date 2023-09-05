package com.example.bottom_navigationbar_view;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class TableAdapterDown extends RecyclerView.Adapter<TableAdapterDown.ViewHolder> {
    private ArrayList<ArrayList<String>> dataList;

    public TableAdapterDown(ArrayList<ArrayList<String>> dataList) {
        this.dataList = dataList;
    }

    public void setData(ArrayList<ArrayList<String>> newData) {
        this.dataList.clear();
        this.dataList.addAll(newData);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meat_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ArrayList<String> rowData = dataList.get(position);
        holder.column1.setText(rowData.get(0));
        holder.column2.setText(rowData.get(1));

        if((position % 2)  != 0){
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.ash1));
        } else {
            // Odd position, set background color 2
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView column1, column2;

        public ViewHolder(View itemView) {
            super(itemView);
            column1 = itemView.findViewById(R.id.meatList1);
            column2 = itemView.findViewById(R.id.meatList2);
        }
    }

    // Method to update the data in the adapter
    public void updateData(ArrayList<ArrayList<String>> newData) {
        this.dataList = newData;
        notifyDataSetChanged();
    }
}

