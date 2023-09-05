

package com.example.bottom_navigationbar_view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bnView;

    String[] permissions = {"android.permission.POST_NOTIFICATIONS"};

    private int firstLoadA = 1;
    private int firstLoadB_o = 1;
    private SoundManager soundManager;
    private boolean isSoundEnable;
    private InterstitialAd mInterstitialAd;
    private AdView mAdView1;
    private AdRequest adRequest;
    private FragmentManager fragmentManager;
    private int countFrag = 0;
    private String currentFragmentTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = this;
        setTitle("Fresh Price");
        bnView = findViewById(R.id.bnView);
//        System.out.println("main activity OnCreate()");
//      System.out.println("settt");
        setFirstLoadA(1); setFirstLoad_B_o(1);
        setSplashScreenShown(1);

        fragmentManager = getSupportFragmentManager();
        requestPermissions(permissions, 80);

        boolean isDarkMode = (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;


        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_bg));

        // Firebase Notification
        FirebaseMessaging.getInstance().subscribeToTopic("notification")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Done";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }
                    }
                });

//        mAdView1 = findViewById(R.id.adView);
//        adRequest = new AdRequest.Builder().build();
//        mAdView1.loadAd(adRequest);

//        loadBannerAd();
        loadAd(); // Making ready

        soundManager = SoundManager.getInstance(this);

        // check internet connection
        if (!isNetworkAvailable()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Internet Connection");
            builder.setMessage("Please turn on your internet connection to use this app.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish(); // Close the app
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        bnView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                System.out.println("isSplashScreenShown: " + isSplashScreenShown());
                int id = item.getItemId();
                if(!isSplashScreenShown()) {
                    retrieveSoundToggle();
                    if (isSoundEnable) soundManager.playButton1Sound();
                }
                if (id == R.id.nav_home) {
                    loadFragment(new AFragment(), "AFragmentTag");
                } else if (id == R.id.nav_search) {
                    loadFragment(new BFragment(), "BFragmentTag");
                }else if (id == R.id.nav_meat){
                    if (mInterstitialAd != null) {
                        mInterstitialAd.show(MainActivity.this);
                    }else{
                        loadAd();
                    }
                    loadFragment(new B_0_Fragment(), "B_0_FragmentTag");
                } else if (id == R.id.nav_analysis) {
                    loadFragment(new CFragment(), "CFragmentTag");
                } else {
                    loadFragment(new DFragment(), "DFragmentTag");
                }
                return true;
            }
        });

        if(isSplashScreenShown()) { bnView.setSelectedItemId(R.id.nav_home);
            setSplashScreenShown(0);}
    }


    private boolean isSplashScreenShown() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int value = sharedPreferences.getInt("splashScreenShown", 1);
        return value == 1;
    }

    private void setSplashScreenShown(int val) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("splashScreenShown", val);
        editor.apply();
    }

    private void setFirstLoadA(int val) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("setFirstLoadA", val);
        editor.apply();
    }

    private void setFirstLoad_B_o(int val) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("setFirstLoad_B_o", val);
        editor.apply();
    }

    // check for network connection
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void loadFragment(Fragment fragment, String tag){
//        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.container, fragment, tag);
//        ft.addToBackStack(tag); // Add to the back stack
        countFrag++;
        ft.commit();
    }
    public void retrieveSoundToggle(){
        // Get a reference to the SharedPreferences
        SharedPreferences sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Retrieve the stored boolean value
        isSoundEnable = sharedPreferences.getBoolean("soundEnabled", true); // Default value is true

    }

    public void loadAd(){
        AdRequest adRequest = new AdRequest.Builder().build();
        // Interstitial ad code
        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
//                        Log.i(TAG, "onAdLoaded");
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        mInterstitialAd = null;
//                                        Log.d("TAG", "The ad was dismissed.");
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        mInterstitialAd = null;
//                                        Log.d("TAG", "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
//                                        Log.d("TAG", "The ad was shown.");
                                    }
                                });

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
//                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
//                        String error = String.format("domain: %s, code: %d, message: %s", loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());
                    }
                });
    }

    public void loadBannerAd(){
        mAdView1.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                super.onAdClicked();
                /*  pending code */
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                super.onAdFailedToLoad(adError);
                mAdView1.loadAd(adRequest);
            }

            @Override
            public void onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                super.onAdLoaded();
//                Toast.makeText(MainActivity.this, "Ad Loaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                super.onAdOpened();
            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        Fragment currentFragment = fragmentManager.findFragmentById(R.id.container); // Replace R.id.container with your fragment container ID
//        String currentFragTag = currentFragment.getTag();
//        System.out.println("currentFragment: " + currentFragment);
//        if (countFrag == 1 || currentFragTag.equals("AFragmentTag")) {
////            System.out.println("entering exiting mode");
//            showExitDialog();
//        } else {
////            System.out.println(" back mode " + countFrag);
//            loadFragment(new AFragment(), "AFragmentTag");
//            // Programmatically select the "Home" menu item in the bottom navigation bar
//            bnView.setSelectedItemId(R.id.nav_home);
//            countFrag = 1;
//        }
//    }

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit App");
        builder.setMessage("Are you sure to leave app?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User pressed "Yes," close the app
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User pressed "No," close the dialog
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
