package com.example.bottom_navigationbar_view;

import static android.app.Activity.RESULT_OK;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottom_navigationbar_view.ml.ConvertedModel2108;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import android.text.Html;
public class BFragment extends Fragment {

    Button camera, gallery;
    ImageView imageView;
    TextView result;
    int imageSize = 224;
    private custom_loading_code loadingDialog;

    private TableAdapter tableAdapterB;
    private TextView txtvw;
    private CardView Bhead;

    ArrayList<ArrayList<String>> tableDataB = new ArrayList<>();

    ArrayList<String> A;
    public BFragment() {
        // Required empty public constructor
    }
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_b, container, false);

        if(!InternetConnectivityUtil.isConnected(getContext())) {
            InternetConnectivityUtil.closeApp(getContext(), this.getActivity());
        }
        loadingDialog = new custom_loading_code(requireContext());
        A = new ArrayList<>();

        txtvw = view.findViewById(R.id.TEFT);
        String STR = "<font color=" + Color.RED
                + "> NOTE: </font><font color="
                + Color.BLACK + "> Results may not be accurate.</font>";
        txtvw.setText(Html.fromHtml(STR));

        TextView textView = view.findViewById(R.id.TEFT1); // Replace with your TextView ID
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        Bhead = view.findViewById(R.id.BHead);
        Bhead.setVisibility(View.INVISIBLE);

        camera = view.findViewById(R.id.button);
        gallery = view.findViewById(R.id.button2);

        result = view.findViewById(R.id.result);
        imageView = view.findViewById(R.id.imageView);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        RecyclerView tableRecyclerViewB = view.findViewById(R.id.table_recyclerViewSearch);
        tableRecyclerViewB.setLayoutManager(new LinearLayoutManager(requireContext()));

        tableAdapterB = new TableAdapter(tableDataB, getContext());   tableRecyclerViewB.setAdapter(tableAdapterB);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, 3);
                }
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(cameraIntent,1);
            }
        });
        return view;
    }
    public void classifyImage(Bitmap image){
        try {
            ConvertedModel2108 model = ConvertedModel2108.newInstance(requireContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
//            System.out.println(image.getHeight() + " " + image.getWidth());
            int pixel = 0;
            //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
            for(int i = 0; i < imageSize; i ++){
                for(int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            ConvertedModel2108.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
//            String[] classes = {"apple","banana","beetroot","bell pepper","cabbage","capsicum","carrot","cauliflower","chilli pepper","corn","cucumber","eggplant","garlic","ginger","grapes","jalepeno","kiwi","lemon","lettuce","mango","onion","orange","paprika","pear","peas","pineapple","pomegranate","potato","raddish","soy beans","spinach","sweetcorn","sweetpotato","tomato","turnip","watermelon"};
            String[] classes = {"bananaflower","greenchili","lemonlime","amaranthleaves","amla","ashgourd","beetroot","bittergourd","bottlegourd","brinjalbig","cabbage","bellpeppercapsicum","carrot","cauliflower","coconutfresh","corianderleavescilantro","corn","cucumber","curryleaves","drumsticks","fenugreekleaves","garlic","ginger","greenpeas","ivygourd","okraladiesfinger","mango","mintleaves","mushroom","mustardleaves","onions","potato","pumpkin","radishdaikon","ridgegourd","sorrellleaves","spinach","sweetpotato","tomato"};
//            System.out.println("-------------------------------------------------");
            System.out.println("classes length : " + classes.length);
            for (int i = 0; i < classes.length; i++) {
//                System.out.println("rohith:" + classes[i] + ": " + (confidences[i]));
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            // Assuming classes and confidences arrays are defined
            int[] indices = new int[classes.length];
            for (int i = 0; i < classes.length; i++) {
                indices[i] = i;
            }

            // Sort indices based on confidences
            for (int i = 0; i < confidences.length - 1; i++) {
                for (int j = i + 1; j < confidences.length; j++) {
                    if (confidences[i] < confidences[j]) {
                        float tempConfidence = confidences[i];
                        confidences[i] = confidences[j];
                        confidences[j] = tempConfidence;

                        int tempIndex = indices[i];
                        indices[i] = indices[j];
                        indices[j] = tempIndex;
                    }
                }
            }

            A.clear();

            // Get the indices of the top three confidences
            int firstHighest = indices[0];
            int secondHighest = indices[1];
            int thirdHighest = indices[2];
            int fourthHighest = indices[3];
            int fifthHighest = indices[4];

//            System.out.println("First highest: " + classes[firstHighest] + ": " + confidences[firstHighest]);
//            System.out.println("Second highest: " + classes[secondHighest] + ": " + confidences[secondHighest]);
//            System.out.println("Third highest: " + classes[thirdHighest] + ": " + confidences[thirdHighest]);
//            System.out.println("Fourth highest: " + classes[fourthHighest] + ": " + confidences[fourthHighest]);
//            System.out.println("Fifth highest: " + classes[fifthHighest] + ": " + confidences[fifthHighest]);
//
//            System.out.println("Predicted: " + classes[maxPos]);

            result.setText(classes[maxPos] + ", " + classes[secondHighest] + ", " + classes[thirdHighest]
             + ", " + classes[fourthHighest] + ", " + classes[fifthHighest]);

            if(classes[maxPos] == "onions" || classes[secondHighest] == "onions" || classes[thirdHighest] == "onions"
                || classes[fourthHighest] == "onions" || classes[fifthHighest] == "onions"){
                A.add("greenonionscallianorspringonion"); A.add("onionbig"); A.add("onionsmall");
                A.add("oniongreen");  A.add("shallotpearlonion");
            }

            if(classes[maxPos] == "brinjal" || classes[secondHighest] == "brinjal" || classes[thirdHighest] == "brinjal"
                    || classes[fourthHighest] == "brinjal" || classes[fifthHighest] == "brinjal"){
                A.add("brinjal"); A.add("brinjalbig");
            }

            if(classes[maxPos] == "greenpeas" || classes[secondHighest].equals("brinjal") || classes[thirdHighest] == "brinjal"
                    || classes[fourthHighest] == "brinjal" || classes[fifthHighest] == "brinjal"){
                A.add("greenpeas"); A.add("frenchbeans"); A.add("butterbeans"); A.add("broadbeans");
                A.add("clusterbeans");
            }
            if(!classes[maxPos].equals("greenpeas") && !classes[maxPos].equals("brinjal") && !classes[maxPos].equals("onions")){
                A.add(classes[maxPos]);
            }
            if(!classes[secondHighest].equals("greenpeas") && !classes[secondHighest].equals("brinjal") && !classes[secondHighest].equals("onions")){
                A.add(classes[secondHighest]);
            }
            if(!classes[thirdHighest].equals("greenpeas") && !classes[thirdHighest].equals("brinjal") && !classes[thirdHighest].equals("onions")){
                A.add(classes[thirdHighest]);
            }
            if(!classes[fourthHighest].equals("greenpeas") && !classes[fourthHighest].equals("brinjal") && !classes[fourthHighest].equals("onions")){
                A.add(classes[fourthHighest]);
            }
            if(!classes[fifthHighest].equals("greenpeas") && !classes[fifthHighest].equals("brinjal") && !classes[fifthHighest].equals("onions")){
                A.add(classes[fifthHighest]);
            }

            Bhead.setVisibility(View.VISIBLE);

            new BFragment.getResultsPrice().execute();
//            for(String s: A){
//                System.out.println(s);
//            }
            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == 3){
                Bitmap image = (Bitmap) data.getExtras().get("data");
                // Resize the bitmap (As the our model works on square images)
                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                imageView.setImageBitmap(image);
                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                classifyImage(image);
            }else{
                Uri dat = data.getData();
                Bitmap image = null;
                try {
                    image = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), dat);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageView.setImageBitmap(image);
                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                classifyImage(image);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class getResultsPrice extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog.show();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            tableDataB.clear();
            try {
                String x = convertString(retrieveSelectedUserState());
                String y = convertString(retrieveSelectedUserCity());
                String url = "https://market.todaypricerates.com/" + x + "-vegetables-price";
                if(y.length() > 0){
                    url = "https://market.todaypricerates.com/" + y + "-vegetables-price-in-" + x;
                }
                Document doc = Jsoup.connect(url).get();

                Elements elements = doc.getElementsByTag("div");
                Elements elements5 = doc.select("div.Cell");
                int index_1 = 0;
                ArrayList<String> Total = new ArrayList<>();
                while(index_1 <= elements5.size() - 5){
                    Elements ele = elements5.eq(index_1);
                    String content = ele.text().replaceAll("\\s","");
                    Total.add(String.valueOf(elements5.eq(index_1).text()));
                    Total.add(String.valueOf(elements5.eq(index_1+2).text()));
                    Total.add(String.valueOf(elements5.eq(index_1+3).text()));
                    Total.add(String.valueOf(elements5.eq(index_1+4).text()));
                    index_1 += 5;
                }
                for (String elementA : A) {
                    // Iterate through the elements in ArrayList B along with their indices
                    for (int index = 0; index < Total.size(); index+=4) {
                        String elementB = Total.get(index);
                        String ele = elementB.toLowerCase().replaceAll("[,\\s\\(\\)]", "");
                        if (elementA.equals(ele)) {
                            ArrayList<String>C = new ArrayList<>();
                            C.add(Total.get(index));  C.add(Total.get(index+1));  C.add(Total.get(index+2));
                            C.add(Total.get(index+3));
                            tableDataB.add(C);
                            break;
                        }
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            System.out.println("onPostExecute() ");
            loadingDialog.dismiss();
            tableAdapterB.notifyDataSetChanged();
        }
    }

    private String convertString(String userState) {
        StringBuilder str = new StringBuilder(userState);
        for(int i=0;i<str.length();i++) if(str.charAt(i) == ' ')  str.setCharAt(i, '-');
        return str.toString();
    }

    private String retrieveSelectedUserState() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("selected_userState", "Telangana");
    }

    private String retrieveSelectedUserCity() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("selected_userCity", "Kamareddy");
    }

}