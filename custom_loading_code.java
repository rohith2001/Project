package com.example.bottom_navigationbar_view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

import androidx.cardview.widget.CardView;

public class custom_loading_code {

    private Context context;
    private Dialog dialog;

//    CardView loadingCardView;

    public custom_loading_code(Context context) {
        this.context = context;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_loading_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Setting the loadingCardView border Radius
        CardView cardView = dialog.findViewById(R.id.loadingCardView);
        int cornerRadius = 15;
        cardView.setRadius(cornerRadius);
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}

