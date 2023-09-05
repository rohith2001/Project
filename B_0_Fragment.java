package com.example.bottom_navigationbar_view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class B_0_Fragment extends Fragment {

    ArrayList <BarEntry> entries;
    ArrayList<Float> graphData = new ArrayList<>();
    private ArrayList<String> xValues = new ArrayList<>();
    ArrayList<ArrayList<String>> eggTable = new ArrayList<>();

    ArrayList<ArrayList<String>> eggPriceList = new ArrayList<>();

    ArrayList<ArrayList<String>> meatList = new ArrayList<>();
    CardView tableHeadCardViewb;
    TableAdapter tableAdapter;
    private custom_loading_code loadingDialog;
    TableAdapterDown tableAdapterDown;
    TextView eggPriceTv;
    private CardView textegg,B_o_Heading;
    private int firstLoad;

    TextView B_o_Heading_tv;
    BarChart barChart;

    private InterstitialAd mInterstitialAd;
    public B_0_Fragment() {}

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_b_0_, container, false);

        if(!InternetConnectivityUtil.isConnected(getContext())) {
            InternetConnectivityUtil.closeApp(getContext(), this.getActivity());
        }

        loadingDialog = new custom_loading_code(this.getActivity()); // Replace 'this' with your activity context

        barChart = view.findViewById(R.id.barchart);
        entries = new ArrayList<>();

        B_o_Heading = view.findViewById(R.id.B_o_Heading);
        B_o_Heading_tv = view.findViewById(R.id.B_o_Heading_tv);

        textegg = view.findViewById(R.id.textegg);
        if(DarkModeOn()) textegg.setBackgroundColor(Color.rgb(50,50,50));
        else textegg.setBackgroundColor(Color.WHITE);

        RecyclerView tableRecyclerView = view.findViewById(R.id.table_recyclerViewb);
        tableRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Set up the custom adapter with the tableData and attach it to the RecyclerView
        tableAdapter = new TableAdapter(eggPriceList, getContext());
        tableRecyclerView.setAdapter(tableAdapter);


        RecyclerView tableRecyclerViewDown = view.findViewById(R.id.table_recyclerViewbSecond);
        tableRecyclerViewDown.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Set up the custom adapter with the tableData and attach it to the RecyclerView
        tableAdapterDown = new TableAdapterDown(meatList);
        tableRecyclerViewDown.setAdapter(tableAdapterDown);

//        tableHeadCardViewb = view.findViewById(R.id.tableHeadCardViewb);
//        cardViewColorChangeCall(tableHeadCardViewb);

//        --------------------------------------------------------
        if(retrieveStateChangeFlag_B_o()){
            System.out.println("if");
            loadingDialog.show();
            //  using AsyncTask
            new B_0_Fragment.getPriceNonVeg().execute();
            saveStateChangeFlag_B_o(false);
        }else if(isFirstLoad_B_o()){
            new B_0_Fragment.getPriceNonVeg().execute();
            setFirstLoad_B_o(0);
        }else{
            DataHoldingEgg dataHolderEgg = DataHoldingEgg.getInstance();
            eggPriceList = dataHolderEgg.getDataList();

            DataHolderMeat dataHolderMeat = DataHolderMeat.getInstance();
            meatList = dataHolderMeat.getDataList();

            graphData = dataHolderEgg.getGraphData();
            xValues = dataHolderEgg.getGraphDates();

            tableAdapter.setData(eggPriceList);
            tableAdapter.notifyDataSetChanged();

            tableAdapterDown.setData(meatList);
            tableAdapterDown.notifyDataSetChanged();

            updateBarChart();
        }

        String x = retrieveSelectedUserState(), y = retrieveSelectedUserCity();
//        System.out.println(x + ":" + y);
        if(y.length() > 0) B_o_Heading_tv.setText("City: " + y + ",     State: " + x);
        else B_o_Heading_tv.setText(x);
        if(DarkModeOn()) B_o_Heading.setBackgroundColor(Color.rgb(40,40,40));
        else B_o_Heading.setBackgroundColor(Color.WHITE);
        return view;
    }

    private boolean DarkModeOn(){
        boolean isDarkMode = (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        return isDarkMode;
    }

    private void cardViewColorChangeCall(CardView c) {
        boolean isDarkMode = (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        if(!isDarkMode) {
            System.out.println("Light Mode");
            c.setCardBackgroundColor(getResources().getColor(R.color.white));
        }else{
            System.out.println("Dark Mode");
            c.setCardBackgroundColor(getResources().getColor(R.color.black_8));
        }
    }

    private class getPriceNonVeg extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog.show(); // Show the loading dialog here
        }

        @Override
        protected Void doInBackground(Void... voids) {

            graphData = new ArrayList<>();  // Initialize graphData here
            xValues = new ArrayList<>();    // Initialize xValues here

            try {
                String x = convertString(retrieveSelectedUserState());
                String y = convertString(retrieveSelectedUserCity());
                String url = "https://market.todaypricerates.com/" + x + "-egg-rate";
                String url2 = "https://market.todaypricerates.com/" + x + "-chicken-mutton-beef-fish-rate";
                if(y.length() > 0){
                    url = "https://market.todaypricerates.com/egg-rate-in-" +  y;
                    url2 = "https://market.todaypricerates.com/chicken-mutton-beef-fish-rate-in-" + y;
                }
                Document doc = Jsoup.connect(url).get();
                Document doc2 = Jsoup.connect(url2).get();

                eggTable.clear();
                eggPriceList.clear();
                meatList.clear();

                Elements divElements = doc.select("div.single-sidebar");
                Elements divElements2 = doc2.select("div.single-sidebar");

                ArrayList<String> eggPrice = new ArrayList<>();
                eggPrice.add("Egg ");

                // If there are multiple div elements with the specified class, you can loop through them
                for (Element divElement : divElements) {
                    // Find all the tr elements within the div element
                    Elements trElements = divElement.select("table.shop_table tr");
                    int count = 0;
                    for (Element trElement : trElements) {
                        count++;
                        if(count > 26){
                            eggPriceList.add(eggPrice);
                            break;
                        }
//                        System.out.println("cnt: " + count);
                        //  Assuming each row contains two states, you may need to modify this based on your HTML structure
                        Elements data = trElement.select("td");
                        if (data.size() >= 5) {
                            String priceString = data.get(2).text();
                            String numericValueString = priceString.replace("₹", "").trim();
                            float price_ = Float.parseFloat(numericValueString);
                            graphData.add(price_);
                            String s = data.get(0).text();  s = s.substring(0,s.length()-5);
                            if(count < 11) xValues.add(s);
                        }else if(data.size() >= 2){
                            eggPrice.add(data.get(1).text().toString());
//                            System.out.println("data : " + data.get(1).text().toString());
                        }
                    }
                }
                int stopping = 0;
                for(Element divElement : divElements2){
                    Elements trElements = divElement.select("table.shop_table tr");
                    for (Element trElement : trElements) {
                        stopping++;
                        if(stopping > 21) break;
                        Elements data = trElement.select("td");
                        if(data.size() == 3){
                            ArrayList<String> meatPrice = new ArrayList<>();
                            meatPrice.add(data.get(0).text().toString());
                            meatPrice.add(data.get(2).text().toString());
//                            System.out.println(data.get(0).text().toString());
//                            System.out.println(data.get(2).text().toString());
                            meatList.add(meatPrice);
                        }
                    }
                }
//                Collections.reverse(graphData);

            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            System.out.println("onPostExecute()");
            tableAdapter.notifyDataSetChanged();
            tableAdapterDown.notifyDataSetChanged();

            DataHoldingEgg dataHoldingEgg = DataHoldingEgg.getInstance();
            dataHoldingEgg.setDataList(eggPriceList);

            DataHolderMeat dataHolderMeat = DataHolderMeat.getInstance();
            dataHolderMeat.setDataList(meatList);

            dataHoldingEgg.setGraphData(graphData);
            dataHoldingEgg.setGraphDates(xValues);

            // Call the method to update the BarChart with the fetched data
            updateBarChart();

            loadingDialog.dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    private boolean isFirstLoad_B_o() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int value = sharedPreferences.getInt("setFirstLoad_B_o", 1);
        return value == 1;
    }

    private void setFirstLoad_B_o(int val) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("setFirstLoad_B_o", val);
        editor.apply();
    }

    private void updateBarChart() {
        if(graphData == null) return;
        if (graphData.size() == 21) {
            entries.clear();
            for (int i = 0; i < 10; i++) {
                entries.add(new BarEntry(i,  graphData.get(i)));
            }

            // Update the BarChart with the new entries
            BarDataSet dataSet = new BarDataSet(entries, "Dates");
            dataSet.setColors(ColorTemplate.getHoloBlue()); // Set custom colors
            if(DarkModeOn()) dataSet.setValueTextColor(Color.WHITE);
            else dataSet.setValueTextColor(Color.BLACK);
            dataSet.setValueTextSize(9f);

            // Set custom bar width and spacing between the bars
            float barWidth = 0.5f; // Modify this value to adjust the width of the bars

            BarData barData = new BarData(dataSet);
            barData.setBarWidth(barWidth); // Set a custom width for the bars

            barChart.setData(barData);
            barChart.animateY(1000, Easing.EaseInOutCubic); // Apply animation
            barChart.getDescription().setEnabled(false); // Disable chart description

            // Customize the X-axis of the BarChart
            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues));
            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            if(DarkModeOn()) barChart.getXAxis().setTextColor(Color.rgb(230,230,230));
            else barChart.getXAxis().setTextColor(Color.BLACK);
            barChart.getXAxis().setTextSize(10f);
            barChart.getXAxis().setGranularity(1f);
            barChart.getXAxis().setXOffset(2f); // Set the offset for the X-axis values
            barChart.getXAxis().setGranularityEnabled(true);
            barChart.getXAxis().setDrawGridLines(false);

            // Customize the Y-axis of the BarChart
            YAxis yAxis = barChart.getAxisLeft();
            if(DarkModeOn()) yAxis.setTextColor(Color.rgb(230,230,230));
            else yAxis.setTextColor(Color.BLACK);
            yAxis.setTextSize(10f);
            yAxis.setAxisMinimum(0f);
            yAxis.setAxisMaximum(8f); // Set a custom maximum value for the Y-axis
            yAxis.setLabelCount(6); // Set a custom number of labels on the Y-axis
            yAxis.setGranularity(1f);
            yAxis.setGranularityEnabled(true);
            yAxis.setDrawGridLines(false);

            barChart.getAxisRight().setEnabled(false); // Hide the right Y-axis

            // Set a custom background color and border for the BarChart
            if(DarkModeOn()) barChart.setBackgroundColor(Color.rgb(30,30,30));
            else barChart.setBackgroundColor(Color.TRANSPARENT);

            // Refresh the BarChart to update the display
            barChart.invalidate();
        }
    }

    private String retrieveSelectedUserState() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("selected_userState", "Telangana");
    }

    private String retrieveSelectedUserCity() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("selected_userCity", "Kamareddy");
    }

    private void saveStateChangeFlag_B_o(boolean flag) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        System.out.println("flag " + flag);
        editor.putBoolean("state_change_flag_B_o", flag);
        editor.apply();
    }

    private boolean retrieveStateChangeFlag_B_o() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("state_change_flag_B_o", false);
    }

    private String convertString(String userState) {
        StringBuilder str = new StringBuilder(userState);
        StringBuilder str1 = new StringBuilder("Andaman-and-Nicobar");
        for(int i=0;i<str.length();i++) if(str.charAt(i) == ' ')  str.setCharAt(i, '-');
//        System.out.println("convert() " + str + " , " + str1);
        if (str.toString().equals(str1.toString())) {
            str.append("-Islands");
        }
//        System.out.println(str);
        return str.toString();
    }
}