package com.example.bottom_navigationbar_view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.Random;

public class AFragment extends Fragment {
    private List<HorizontalItem> itemList;

    private List<HorizontalItem> itemListDown;
    private HorizontalAdapter horizontalAdapter;   private HorizontalAdapter1 horizontalAdapter1;
    TextView tv1;
    List<String> stateList1 = new ArrayList<>();
    List<String> stateList2 = new ArrayList<>();
    ArrayList<ArrayList<String>> tableData = new ArrayList<>();

    CardView priceCardView;
    private TableAdapter tableAdapter;

    RecyclerView recyclerView, recyclerView_1;
    private TextView tvsay;
    private custom_loading_code loadingDialog;
    public int firstLoad = 1;

    private AdView mAdView;
    public AFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_a, container, false);

        if(!InternetConnectivityUtil.isConnected(getContext())) {
            InternetConnectivityUtil.closeApp(getContext(), this.getActivity());
        }
//        showLoadingDialog();
        loadingDialog = new custom_loading_code(requireContext());

        System.out.println("oncreate() AFragment");
        System.out.println("isFirstLoadA() " + isFirstLoadA());

        /* Ad part */
        // Ads setup
        MobileAds.initialize(requireContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
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
                mAdView.loadAd(adRequest);
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
//                Toast.makeText(requireContext(), "Ad Loaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                super.onAdOpened();
            }
        });
        /*Ad part*/


        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView_1 = view.findViewById(R.id.recyclerView_1);

        // Prepare your list of HorizontalItems (replace the placeholders with your actual data)
        itemList = new ArrayList<>();   itemListDown = new ArrayList<>();

        tv1 = view.findViewById(R.id.TV1);
        tvsay = view.findViewById(R.id.TVSay);
        priceCardView = view.findViewById(R.id.priceCardView);

        // Create and set the adapter for the RecyclerView
        horizontalAdapter = new HorizontalAdapter(itemList, requireContext());  recyclerView.setAdapter(horizontalAdapter);
        horizontalAdapter1 = new HorizontalAdapter1(itemListDown, requireContext()); recyclerView_1.setAdapter(horizontalAdapter1);


        // Set the LayoutManager to display items horizontally
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_1.setLayoutManager(layoutManager1);

        RecyclerView tableRecyclerView = view.findViewById(R.id.table_recyclerView);
        tableRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Set up the custom adapter with the tableData and attach it to the RecyclerView
        tableAdapter = new TableAdapter(tableData, getContext());   tableRecyclerView.setAdapter(tableAdapter);

        cardViewColorChangeCall();
//        System.out.println("AFragment check post 1");
        if(retrieveStateChangeFlag()){
            new getPriceIncreased().execute();
            saveStateChangeFlag(false);
//            System.out.println("AFragment check post 2");
        }else if(isFirstLoadA()){
            new getPriceIncreased().execute();
            System.out.println("firstLoad");
            setFirstLoadA(0);
        }else{
            System.out.println("need to print something..");
            DataHolder dataHolder = DataHolder.getInstance();
            tableData = dataHolder.getDataList();

            DataHolderHeading dataHolderHeading = DataHolderHeading.getInstance();
            DataHolderHeading1 dataHolderHeading1 = DataHolderHeading1.getInstance();
            itemList = dataHolderHeading.getDataList();
            itemListDown = dataHolderHeading1.getDataList();

            if(itemList.size() == 0 && itemListDown.size() == 0){
//                System.out.println("Need both sizes are (0)..");
                ViewGroup.LayoutParams params=recyclerView.getLayoutParams();
                params.height=0;
                recyclerView.setLayoutParams(params);
                recyclerView_1.setLayoutParams(params);
            }

            horizontalAdapter = new HorizontalAdapter(itemList, requireContext()); recyclerView.setAdapter(horizontalAdapter);
            horizontalAdapter1 = new HorizontalAdapter1(itemListDown, requireContext()); recyclerView_1.setAdapter(horizontalAdapter1);

            tableAdapter.setData(tableData);   tableAdapter.notifyDataSetChanged();
        }
        String x = retrieveSelectedUserState(), y = retrieveSelectedUserCity();
        System.out.println(x + ":" + y);
        if(y.length() > 0) tv1.setText("City: " + y + ",     State: " + x);
        else tv1.setText("State: " + x);
//        if(retrieveSelectedUserCity())
        return view;
    }

    private int getVegetableImageResource(String vegetableName) {
        String imageName = vegetableName.toLowerCase().replaceAll("[,\\s\\(\\)]", ""); // Convert name to lowercase and remove commas, spaces, and parentheses
        return getResources().getIdentifier(imageName, "drawable", requireContext().getPackageName());
    }

    private boolean isFirstLoadA() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int value = sharedPreferences.getInt("setFirstLoadA", 1);
        return value == 1;
    }

    private void setFirstLoadA(int val) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("setFirstLoadA", val);
        editor.apply();
    }

    private class getPriceIncreased extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog.show(); // Show the loading dialog here
        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                System.out.println("came inside doInBackground()");
                String url = "https://vegetablemarketprice.com/";
                Document doc = Jsoup.connect(url).get();
//                itemList.clear(); itemListDown.clear();
//
//
//                // Find the div element with class "col-sm-6 px-md-5"
//                Elements divElements = doc.select("div.col-sm-6.px-md-5");
//                ArrayList<String>temp = new ArrayList<>();
//
//                int times = 0;
//                // If there are multiple div elements with the specified class, you can loop through them
//                for (Element divElement : divElements) {
//                    // Find all the tr elements within the div element
//                    Elements trElements = divElement.select("table tr");
//                    for (Element trElement : trElements) {
//                        String state1, state2;
//                        // Assuming each row contains two states, you may need to modify this based on your HTML structure
//                        Elements x = trElement.select("td");
//                        if (x.size() >= 2 && times == 0) {
//                            state1 = x.get(0).text(); state2 = x.get(1).text();
//                            stateList1.add(state1);  stateList2.add(state2);
//                            temp.add(String.valueOf(state1));
//                        }else if(x.size() >= 2 && times == 1){
//                            state1 = x.get(0).text(); state2 = x.get(1).text();
//                            int imageResourceId = getVegetableImageResource(state1);
//                            boolean isDarkMode = (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
////                            if(isDarkMode) imageResourceId = getVegetableImageResource(state1 + "11");
//                            temp.add(String.valueOf(state1));
//                            itemListDown.add(new HorizontalItem(state1, String.valueOf("-" + state2 + "↓"), imageResourceId));
//                        }
//                    }
//                    times+=1;
//                }
////                System.out.println("Length of itemListDown : " + itemListDown.size());
////                for(HorizontalItem e : itemListDown){
////                    System.out.println(e.getItemName1() + ", " + e.getItemName2());
////                }
//                String checkUrl = "https://vegetablemarketprice.com/market/andhraPradesh/today";
//                Document checkDoc = Jsoup.connect(checkUrl).get();
//
//                // Find all td elements within the tbody
//                Elements tds = checkDoc.select("#todayVegetableTableContainer tr td");
//                int cnt = 0;
//                ArrayList<String>checktmp = new ArrayList<>();
//                for (Element td : tds) {
//                    String data = td.text();
//                    if(cnt%6 == 1)  checktmp.add(data);
//                    cnt++;
//                }
//                ArrayList<String>Store = new ArrayList<>();
//                int i = 0;
//                while(i < checktmp.size()){
//                    int found = 0;
//                    for(String s: temp){
//                        String trimmedStr1 = s.replaceAll("\\s+", "");
//                        String trimmedStr2 = checktmp.get(i).replaceAll("\\s+", "");
//                        if(trimmedStr1.equals(trimmedStr2)){
//                            found = 1; break;
//                        }
//                    }
//                    if(found == 0)  Store.add(checktmp.get(i));
//                    i++;
//                }
//                i = 0;
//                while(i < Store.size() && stateList1.size() >= 0 && stateList1.size() < 3){
//                    stateList1.add(Store.get(i));  stateList2.add(String.valueOf(generateRandomValue(0.01,2.10)));
//                    i++;
//                }
//                while(i < Store.size() && itemListDown.size() >= 0 && itemListDown.size() < 3){
//                    int imageResourceId = getVegetableImageResource(Store.get(i));
//                    itemListDown.add(new HorizontalItem(Store.get(i), String.valueOf("-" + String.valueOf(generateRandomValue(0.01,2.10)) + "↓"), imageResourceId));
//                    i++;
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            tableData.clear();
//            try {
//                String x = convertString(retrieveSelectedUserState());
//                String y = convertString(retrieveSelectedUserCity());
//                String url = "https://market.todaypricerates.com/" + x + "-vegetables-price";
//                if(y.length() > 0){
//                    url = "https://market.todaypricerates.com/" + y + "-vegetables-price-in-" + x;
//                }
//                Document doc = Jsoup.connect(url).get();
//
//                Elements elements = doc.getElementsByTag("div");
//                Elements elements5 = doc.select("div.Cell");
//                int index_1 = 0;
//                while(index_1 <= elements5.size() - 5){
//                    Elements ele = elements5.eq(index_1);
//                    String content = ele.text().replaceAll("\\s","");
////                    System.out.println("printing.. " + String.valueOf(elements5.eq(index_1).text()));
//                    ArrayList<String> A = new ArrayList<>();
//                    A.add(String.valueOf(elements5.eq(index_1).text()));
//                    A.add(String.valueOf(elements5.eq(index_1+2).text()));
//                    A.add(String.valueOf(elements5.eq(index_1+3).text()));
//                    A.add(String.valueOf(elements5.eq(index_1+4).text()));
//                    tableData.add(A);
//                    index_1 += 5;
//                }

            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            for(int i=0;i<stateList1.size();i++){
                int imageResourceId = getVegetableImageResource(stateList1.get(i));
                itemList.add(new HorizontalItem(stateList1.get(i), String.valueOf("+" + stateList2.get(i) + "↑"), imageResourceId));
            }
            System.out.println("onPostExecute() ");

            // Notify the adapter that the data has changed
            horizontalAdapter.notifyDataSetChanged();  tableAdapter.notifyDataSetChanged();
            horizontalAdapter1.notifyDataSetChanged();
            DataHolder dataHolder = DataHolder.getInstance(); dataHolder.setDataList(tableData);

            DataHolderHeading dataHolderHeading = DataHolderHeading.getInstance();
            dataHolderHeading.setDataList(itemList);

            DataHolderHeading1 dataHolderHeading1 = DataHolderHeading1.getInstance();
            dataHolderHeading1.setDataList(itemListDown);

            if(itemList.size() == 0 && itemListDown.size() == 0){
//                System.out.println("both sizes are (0)");
                ViewGroup.LayoutParams params=recyclerView.getLayoutParams();
                params.height=0;
                recyclerView.setLayoutParams(params);
                recyclerView_1.setLayoutParams(params);
            }
            loadingDialog.dismiss();
        }
    }

    private void cardViewColorChangeCall() {
        boolean isDarkMode = (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;

        if(!isDarkMode) {
//            System.out.println("Light Mode");
            priceCardView.setCardBackgroundColor(getResources().getColor(R.color.white));
        }else{
//            System.out.println("Dark Mode");
            priceCardView.setCardBackgroundColor(getResources().getColor(R.color.black_8));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (loadingDialog != null) loadingDialog.dismiss();
    }


    private void saveSelectedUserState(String selectedUserState) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("selected_userState", selectedUserState);
        editor.apply();
    }

    private String retrieveSelectedUserState() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("selected_userState", "Telangana");
    }

    private String retrieveSelectedUserCity() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("selected_userCity", "Kamareddy");
    }
    private void saveStateChangeFlag(boolean flag) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        System.out.println("flag " + flag);
        editor.putBoolean("state_change_flag", flag);
        editor.apply();
    }



    private boolean retrieveStateChangeFlag() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("state_change_flag", false);
    }

    private void saveDataForAFragment(boolean flag) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("Data_AFragment", flag);
        editor.apply();
    }


    private String convertString(String userState) {
        StringBuilder str = new StringBuilder(userState);
        for(int i=0;i<str.length();i++) if(str.charAt(i) == ' ')  str.setCharAt(i, '-');
        return str.toString();
    }

    public static float generateRandomValue(double minValue, double maxValue) {
        if (minValue >= maxValue) {
            throw new IllegalArgumentException("minValue must be less than maxValue");
        }
        Random random = new Random();
        float randomDouble = (float) (minValue + random.nextFloat() * (maxValue - minValue));

        // Format the random value to have two digits after the decimal point
        DecimalFormat decimalFormat = new DecimalFormat("#.00");

        String formattedValue = decimalFormat.format(randomDouble);

        // Parse the formatted value back to a double
        return Float.parseFloat(formattedValue);
    }

}