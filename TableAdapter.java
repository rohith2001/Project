package com.example.bottom_navigationbar_view;
import static androidx.core.content.ContentProviderCompat.requireContext;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import android.view.ScaleGestureDetector;


public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {
    private ArrayList<ArrayList<String>> dataList;
    private Context context;


    public TableAdapter(ArrayList<ArrayList<String>> dataList, Context context) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_layout, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ArrayList<String> rowData = dataList.get(position);
        holder.column1.setText(rowData.get(0));
        holder.column2.setText(rowData.get(1));
        holder.column3.setText(rowData.get(2));
        holder.column4.setText(rowData.get(3));
        int imageResourceId = getVegetableImageResource(rowData.get(0));
        holder.imageView.setImageResource(imageResourceId);
        if((position % 2)  != 0)  holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
        else holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the vegetable name from the rowData
                String vegetableName = rowData.get(0); // Assuming the vegetable name is in the first column
                // Get the image resource ID using the getVegetableImageResource method
                int imageResourceId = getVegetableImageResource(vegetableName);

                // Show the image in a larger view using a Dialog or a custom fullscreen activity
                showImageInDialog(imageResourceId);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void showImageInDialog(int imageResourceId) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_image);
        ImageView imageView = dialog.findViewById(R.id.dialogImageView);
        imageView.setImageResource(imageResourceId);

//        imageView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
////                scaleGestureDetector.onTouchEvent(event); // Pass touch events to ScaleGestureDetector
//                imageView.setScaleX(2);
//                imageView.setScaleY(2);
//                return true;
//            }
//        });

        dialog.show();
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView column1, column2, column3, column4;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            column1 = itemView.findViewById(R.id.column1);
            column2 = itemView.findViewById(R.id.column2);
            column3 = itemView.findViewById(R.id.column3);
            column4 = itemView.findViewById(R.id.column4);
            imageView = itemView.findViewById(R.id.columnImage);
        }
    }

    // Method to update the data in the adapter
    public void updateData(ArrayList<ArrayList<String>> newData) {
        this.dataList = newData;
        notifyDataSetChanged();
    }

    // Method to get the vegetable image resource based on the vegetable name
    private int getVegetableImageResource(String vegetableName) {
        String imageName = vegetableName.toLowerCase().replaceAll("[,\\s\\(\\)]", ""); // Convert name to lowercase and remove commas, spaces, and parentheses
        return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
    }

}
