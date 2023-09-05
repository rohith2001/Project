package com.example.bottom_navigationbar_view;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_TIMEOUT = 1000; // 2 seconds

    private ImageView imageView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        // Hide the action bar
//        Objects.requireNonNull(getSupportActionBar()).hide();

        // Set the variable to 1
        setSplashScreenShown(1);

//        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.2f);
//        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.2f);

        imageView = findViewById(R.id.splash_image);

//        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(imageView, scaleX, scaleY);
//        animator.setDuration(1000); // Set the animation duration in milliseconds
//        animator.setRepeatCount(ObjectAnimator.INFINITE); // Repeat the animation indefinitely
//        animator.setRepeatMode(ObjectAnimator.REVERSE); // Reverse the animation on repeat
//        animator.start();

        // Delay the start of the MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start MainActivity after the delay
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                finish();
            }
        }, SPLASH_TIMEOUT);
    }

    private void setSplashScreenShown(int val) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("splashScreenShown", val);
        editor.apply();
    }
}
