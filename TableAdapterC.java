package com.example.bottom_navigationbar_view;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class TableAdapterC extends RecyclerView.Adapter<TableAdapterC.ViewHolder> {
    private ArrayList<ArrayList<String>> dataList;
    private Context context;

    public TableAdapterC(ArrayList<ArrayList<String>> dataList,  Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    // Add this method to update the dataset of the adapter
    public void setData(ArrayList<ArrayList<String>> newData) {
        this.dataList.clear();
        this.dataList.addAll(newData);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_c, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ArrayList<String> rowData = dataList.get(position);
        holder.column1.setText(rowData.get(0));
        holder.column2.setText(rowData.get(1));
        holder.column3.setText(rowData.get(2));
        holder.column4.setText(rowData.get(3));
        holder.column5.setText(rowData.get(4));
        holder.column6.setText(rowData.get(5));
        holder.column7.setText(rowData.get(6));
        int imageResourceId = getVegetableImageResource(rowData.get(3));
        holder.imageView.setImageResource(imageResourceId);
//        if((position % 2)  != 0)  holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.skyishBlue));
//        else holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.lightOrange));

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vegetableName = rowData.get(3);
                int imageResourceId = getVegetableImageResource(vegetableName);
                showImageInDialog(imageResourceId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        // column4 -> veg name
        public TextView column1, column2, column3, column4, column5, column6, column7;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            column1 = itemView.findViewById(R.id.column1);
            column2 = itemView.findViewById(R.id.column2);
            column3 = itemView.findViewById(R.id.column3);
            column4 = itemView.findViewById(R.id.column4);
            column5 = itemView.findViewById(R.id.column5);
            column6 = itemView.findViewById(R.id.column6);
            column7 = itemView.findViewById(R.id.column7);
            imageView = itemView.findViewById(R.id.columnImage);
        }
    }

    // Method to get the vegetable image resource based on the vegetable name
    private int getVegetableImageResource(String vegetableName) {
        String imageName = vegetableName.toLowerCase().replaceAll("[,\\s\\(\\)]", ""); // Convert name to lowercase and remove commas, spaces, and parentheses
        System.out.println("imageName: " + imageName);
        return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
    }

    private void showImageInDialog(int imageResourceId) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_image);

        ImageView imageView = dialog.findViewById(R.id.dialogImageView);
        imageView.setImageResource(imageResourceId);

        dialog.show();
    }
}
