package com.example.bottom_navigationbar_view;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

public class CustomSpinner extends androidx.appcompat.widget.AppCompatSpinner {

    public CustomSpinner(Context context) {
        super(context);
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean performClick() {
        // Open the dropdown below the spinner
        boolean handled = false;
        try {
            handled = super.performClick();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return handled;
    }
}

