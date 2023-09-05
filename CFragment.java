package com.example.bottom_navigationbar_view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    String[] STATES = {"Andaman and Nicobar", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chandigarh", "Chhattisgarh", "Dadra and Nagar Haveli", "Daman and Diu", "Delhi", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Lakshadweep", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Puducherry",
            "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal"};

    AutoCompleteTextView autoCompleteTextView_1, autoCompleteTextView_2;
    ArrayAdapter<String> stateAdapter11, stateAdapter22;
    List<String> states = new ArrayList<>();
    private String selectedState1 = "error";
    private String selectedState2 = "error";
    ArrayAdapter<String> stateAdapter1, stateAdapter2;
    Spinner stateSpinner1, stateSpinner2;
    Button submitButton;

    TextView c1,c2,c3;
    TableLayout tableLayout;

    private TableAdapterC tableAdapterc;
    ArrayList<ArrayList<String>> twoTableDatas = new ArrayList<>();

    CardView tableHeadCardView, cardViewDown;
    RecyclerView tableRecyclerView;
    private custom_loading_code loadingDialog;

    RelativeLayout relativeLayout;

    private InterstitialAd mInterstitialAd1;
    public CFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_c, container, false);

        if(!InternetConnectivityUtil.isConnected(getContext())) {
            InternetConnectivityUtil.closeApp(getContext(), this.getActivity());
        }
        loadingDialog = new custom_loading_code(this.getActivity()); // Replace 'this' with your activity context
        loadStatesArray();

        loadAd();
        mInterstitialAd1 = null;

        c1 = view.findViewById(R.id.c1);
//        c2 = view.findViewById(R.id.c2);
        c3 = view.findViewById(R.id.c3);

        relativeLayout = view.findViewById(R.id.relativeLayout123);
        boolean isDarkMode = (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        if(isDarkMode){
            relativeLayout.setBackgroundColor(Color.BLACK);
        }else{
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.white_90));
        }

        // Intialize cardView tableHeadCardView
        tableHeadCardView = view.findViewById(R.id.tableHeadCardView);
        tableLayout = view.findViewById(R.id.tableLayoutID);
        cardViewDown = view.findViewById(R.id.cardviewDown);

        autoCompleteTextView_1 = view.findViewById(R.id.auto_complete_text_1);
        stateAdapter11 = new ArrayAdapter<String>(getContext(), R.layout.custom_drop_down, STATES);
        autoCompleteTextView_1.setAdapter(stateAdapter11);

        autoCompleteTextView_2 = view.findViewById(R.id.auto_complete_text_2);
        stateAdapter22 = new ArrayAdapter<String>(getContext(), R.layout.custom_drop_down, STATES);
        autoCompleteTextView_2.setAdapter(stateAdapter22);

        autoCompleteTextView_1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                selectedState1 = item;
                saveSelectedState1(item);
                autoCompleteTextView_1.setText(item);
                autoCompleteTextView_1.setSelection(item.length());
                Toast.makeText(getContext(), "State: " + item, Toast.LENGTH_SHORT).show();
            }
        });

        autoCompleteTextView_2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                selectedState2 = item;
                saveSelectedState2(item);
                autoCompleteTextView_2.setText(item);
                autoCompleteTextView_2.setSelection(item.length());
                Toast.makeText(getContext(), "State: " + item, Toast.LENGTH_SHORT).show();
            }
        });

        tableRecyclerView = view.findViewById(R.id.table_recyclerView);
        // Calculate the desired height in pixels (change this value to your desired height)
        // Calculate the desired height in dp (density-independent pixels)
        int desiredHeightInDp = 550; // Your desired height in dp

        // Convert dp to pixels using the display metrics
        float scale = getResources().getDisplayMetrics().density;
        int desiredHeightInPixels = (int) (desiredHeightInDp * scale + 0.5f);



        // To make the RecyclerView invisible
        cardViewDown.setVisibility(View.INVISIBLE);

        ViewColorChangeCall();


        submitButton = view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                if(mInterstitialAd1 != null){
                    mInterstitialAd1.show(requireActivity());
                }else{
                    loadAd();
                }

                Toast.makeText(getContext(), selectedState1 + ", " + selectedState2, Toast.LENGTH_SHORT).show();
                if(selectedState2 == selectedState1) Toast.makeText(getContext(), "Choose Different states", Toast.LENGTH_SHORT).show();
                else{
                    // save the click
//                    saveSelectedFragment("okayy".toString());

                    new CFragment.loadTwoCountriesData().execute();
                    c1.setText(selectedState1);     c3.setText(selectedState2);

                    tableHeadCardView.setVisibility(View.VISIBLE);
                    tableHeadCardView.setRadius(0);
                    // To make the RecyclerView visible
                    cardViewDown.setVisibility(View.VISIBLE);
                    cardViewDown.setRadius(0);
                    // Set the height using LayoutParams
                    ViewGroup.LayoutParams layoutParams = tableRecyclerView.getLayoutParams();
                    layoutParams.height = desiredHeightInPixels;
                    tableRecyclerView.setLayoutParams(layoutParams);

                    tableRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

                    // Set up the custom adapter with the tableData and attach it to the RecyclerView
                    tableAdapterc = new TableAdapterC(twoTableDatas, getContext());
                    tableRecyclerView.setAdapter(tableAdapterc);
                }
                loadingDialog.dismiss();
            }
        });
        return view;
    }

    private void loadStatesArray() {
        states = Arrays.asList(new String[]{"Andaman and Nicobar", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chandigarh", "Chhattisgarh", "Dadra and Nagar Haveli", "Daman and Diu", "Delhi", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Lakshadweep", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Puducherry",
                "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal"});
    }

    private void ViewColorChangeCall() {
        boolean isDarkMode = (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;

        if(!isDarkMode) {
            System.out.println("Light Mode");
            tableLayout.setBackgroundColor(Color.rgb(200,200,200));
            tableHeadCardView.setCardBackgroundColor(getResources().getColor(R.color.white));


        }else{
            System.out.println("Dark Mode");
            tableLayout.setBackgroundColor(Color.rgb(20,20,20));
            tableHeadCardView.setCardBackgroundColor(getResources().getColor(R.color.black_8));
        }
    }
    private class loadTwoCountriesData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            twoTableDatas.clear();
            try {
                String x = convertString(selectedState1);
                String y = convertString(selectedState2);
                String url_x = "https://market.todaypricerates.com/" + x + "-vegetables-price";
                String url_y = "https://market.todaypricerates.com/" + y + "-vegetables-price";
                Document doc_x = Jsoup.connect(url_x).get();
                Document doc_y = Jsoup.connect(url_y).get();

                Elements element_X = doc_x.getElementsByTag("div");
                Elements elements_x = doc_x.select("div.Cell");

                Elements element_Y = doc_y.getElementsByTag("div");
                Elements elements_y = doc_y.select("div.Cell");

                int index_1 = 0;
                while(index_1 <= elements_x.size() - 5){
                    ArrayList<String> A = new ArrayList<>();
                    A.add(String.valueOf(elements_x.eq(index_1+2).text()));
                    A.add(String.valueOf(elements_x.eq(index_1+3).text()));
                    A.add(String.valueOf(elements_x.eq(index_1+4).text()));
//                    twoTableDatas.add(A);
                    A.add(String.valueOf(elements_y.eq(index_1).text()));
                    A.add(String.valueOf(elements_y.eq(index_1+2).text()));
                    A.add(String.valueOf(elements_y.eq(index_1+3).text()));
                    A.add(String.valueOf(elements_y.eq(index_1+4).text()));
                    twoTableDatas.add(A);
                    index_1 += 5;
                }

            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            System.out.println("c onpostExecute()");
            // Notify the adapter that the data has changed
            tableAdapterc.notifyDataSetChanged();

            loadingDialog.dismiss();
        }
    }

    private void saveSelectedState1(String country) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("selected_state1", country);
        editor.apply();
    }

    private void saveSelectedState2(String country) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("selected_state2", country);
        editor.apply();
    }
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        // Retrieve the selected country and gender from SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        selectedState1 = sharedPreferences.getString("selected_state1", "Andhra Pradesh");
        selectedState2 = sharedPreferences.getString("selected_state2", "Telangana");

        autoCompleteTextView_1.setText(selectedState1);
        autoCompleteTextView_1.setSelection(selectedState1.length());

        autoCompleteTextView_2.setText(selectedState2);
        autoCompleteTextView_2.setSelection(selectedState2.length());

    }

    private String convertString(String userState) {
        StringBuilder str = new StringBuilder(userState);
        for(int i=0;i<str.length();i++) if(str.charAt(i) == ' ')  str.setCharAt(i, '-');
        return str.toString();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void loadAd(){
        AdRequest adRequest = new AdRequest.Builder().build();

        // Interstitial ad code
        InterstitialAd.load(requireActivity(),"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd1 = interstitialAd;
//                        Log.i(TAG, "onAdLoaded");
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        mInterstitialAd1 = null;
//                                        Log.d("TAG", "The ad was dismissed.");
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        mInterstitialAd1 = null;
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
                        mInterstitialAd1 = null;
//                        String error = String.format("domain: %s, code: %d, message: %s", loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());
                    }
                });
    }

}