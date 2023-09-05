package com.example.bottom_navigationbar_view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class DFragment extends Fragment {
    public DFragment() {
        // Required empty public constructor
    }
    AutoCompleteTextView autoCompleteTextView, autoCompleteTextView1;
    ArrayAdapter<String> adapterItems, adapterItems1;

    Spinner userStateSpinner;
    List<String> states = new ArrayList<>();
    String[] STATES = {"Andaman and Nicobar", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chandigarh", "Chhattisgarh", "Dadra and Nagar Haveli", "Daman and Diu", "Delhi", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Lakshadweep", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Puducherry",
            "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal"};

    Map<String, Integer> stateIndexMap = new HashMap<>();
//    String[] CITIES = {"rohith", "rahul", "uttej"};
    String[][] CITIES = {
        {"Car Nicobar","Mayabunder","Port Blair"},
        {"Anantapur","Chittoor","Eluru","Guntur","Kadapa","Kakinada","Kurnool","Machilipatnam","Nellore","Ongole","Srikakulam","Visakhapatnam","Vizianagaram"},
        {"Along", "Anini", "Basar", "Bomdila", "Changlang", "Daporijo", "Hawai", "Itanagar", "Jamin", "Khonsa", "Koloriang", "Lemmi", "Likabali", "Longding", "Namsai", "Pangin", "Pasighat", "Raga", "Roing", "Seppa", "Tato", "Tawang Town", "Tezu", "Yingkiong", "Ziro"},
        {"Barpeta", "Bishwanath Chariali", "Bongaigaon", "Dhemaji", "Dhubri", "Dibrugarh", "Diphu", "Garamur", "Goalpara", "Golaghat", "Goroimari", "Guwahati", "Haflong", "Hailakandi", "Hamren", "Hatsingimari", "Hojai", "Jorhat", "Kajalgaon", "Karimganj", "Kokrajhar", "Mangaldoi", "Marigaon", "Mushalpur", "Nagaon", "Nalbari", "North Lakhimpur", "Sibsagar", "Silchar", "Sonari", "Tezpur", "Tinsukia", "Udalguri"},
        {"Araria", "Arrah", "Arwal", "Aurangabad", "Banka", "Begusarai", "Bettiah", "Bhabua", "Bhagalpur", "Bihar Sharif", "Buxar", "Chhapra", "Darbhanga", "Gaya", "Gopalganj", "Hajipur", "Jamui", "Jehanabad", "Katihar", "Khagaria", "Kishanganj", "Lakhisarai", "Madhepura", "Madhubani", "Motihari", "Munger", "Muzaffarpur", "Nawada", "Patna", "Purnia", "Saharsa", "Samastipur", "Sasaram", "Sheikhpura", "Sheohar", "Sitamarhi", "Siwan", "Supaul"},
        {"Chandigarh"},
        {"Ambikapur", "Baikunthpur", "Balod", "Baloda Bazar", "Balrampur", "Bemetara", "Bijapur", "Bilaspur", "Dantewada", "Dhamtari", "Durg", "Gariaband", "Gaurela Pendra Marwahi", "Jagdalpur", "Jashpur Nagar", "Kanker", "Kawardha", "Kondagaon", "Korba", "Mahasamund", "Mungeli", "Naila Janjgir", "Narayanpur", "Raigarh", "Raipur", "Rajnandgaon", "Sukma", "Surajpur"},
        {"Silvassa"},
        {"Daman","Diu"},
        {"Daryaganj", "Defence Colony", "Kanjhawala", "New Delhi", "Preet Vihar", "Rajouri Garden", "Sadar Bazaar", "Saket", "Shahdara", "Vasant Vihar"},
        {"Margao","Panaji"},
        {"Ahmedabad", "Ahwa", "Amreli", "Anand", "Bharuch", "Bhavnagar", "Bhuj", "Botad", "Chhota Udepur", "Dahod", "Gandhinagar", "Godhra", "Himatnagar", "Jamnagar", "Junagadh", "Khambhalia", "Lunavada", "Mehsana", "Modasa", "Morbi", "Nadiad", "Navsari", "Palanpur", "Patan", "Porbandar", "Rajkot", "Rajpipla", "Surat", "Surendranagar", "Vadodara", "Valsad", "Veraval", "Vyara"},
        {"Ambala", "Bhiwani", "Charkhi Dadri", "Faridabad", "Fatehabad", "Gurgaon", "Hissar", "Jhajjar", "Jind", "Kaithal", "Karnal", "Kurukshetra", "Narnaul", "Nuh", "Palwal", "Panchkula", "Panipat", "Rewari", "Rohtak", "Sirsa", "Sonipat", "Yamuna Nagar"},
        {"Bilaspur", "Chamba", "Dharamshala", "Hamirpur", "Keylong", "Kullu", "Mandi", "Nahan", "Reckong Peo", "Shimla", "Solan", "Una"},
        {"Anantnag", "Badgam", "Bandipore", "Baramulla", "Doda", "Ganderbal", "Jammu", "Kargil", "Kathua", "Kishtwar", "Kulgam", "Kupwara", "Leh", "Poonch", "Pulwama", "Rajouri", "Ramban", "Reasi", "Samba", "Shupiyan", "Srinagar", "Udhampur"},
        {"Bokaro", "Chaibasa", "Chatra", "Daltonganj", "Deoghar", "Dhanbad", "Dumka", "Garhwa", "Giridih", "Godda", "Gumla", "Hazaribag", "Jamshedpur", "Jamtara", "Khunti", "Koderma", "Latehar", "Lohardaga", "Pakur", "Ramgarh", "Ranchi", "Sahebganj", "Seraikela", "Simdega"},
        {"Bagalkot", "Bangalore", "Belgaum", "Bellary", "Bengaluru", "Bidar", "Chamarajanagar", "Chikkaballapur", "Chikmagalur", "Chitradurga", "Davangere", "Dharwad", "Gadag Betageri", "Gulbarga", "Hassan", "Haveri", "Karwar", "Kolar", "Koppal", "Madikeri", "Mandya", "Mangalore", "Mysore", "Raichur", "Ramanagara", "Shimoga", "Tumkur", "Udupi", "Vijayapura", "Yadgir"},
        {"Alappuzha", "Ernakulam", "Kalpetta", "Kannur", "Kasaragod", "Kollam", "Kottayam", "Kozhikode", "Malappuram", "Painavu", "Palakkad", "Pathanamthitta", "Thiruvananthapuram", "Thrissur"},
        {"Kavaratti"},
        {"Agar", "Alirajpur", "Anuppur", "Ashok Nagar", "Balaghat", "Barwani", "Betul", "Bhind", "Bhopal", "Burhanpur", "Chachaura", "Chhatarpur", "Chhindwara", "Damoh", "Datia", "Dewas", "Dhar", "Dindori", "Guna", "Gwalior", "Harda", "Hoshangabad", "Indore", "Jabalpur", "Jhabua", "Katni", "Khandwa", "Khargone", "Maihar", "Mandla", "Mandsaur", "Morena", "Nagda", "Narsinghpur", "Neemuch", "Niwari", "Panna", "Raisen", "Rajgarh", "Ratlam", "Rewa", "Sagar", "Satna", "Sehore", "Seoni", "Shahdol", "Shajapur", "Sheopur", "Shivpuri", "Sidhi", "Tikamgarh", "Ujjain", "Umaria", "Vidisha", "Waidhan"},
        {"Ahmednagar", "Akola", "Alibag", "Amravati", "Aurangabad", "Bandra (East)", "Beed", "Bhandara", "Buldhana", "Chandrapur", "Dhule", "Gadchiroli", "Gondia", "Hingoli", "Jalgaon", "Jalna", "Kolhapur", "Latur", "Mumbai", "Nagpur", "Nanded", "Nandurbar", "Nashik", "Oros", "Osmanabad", "Palghar", "Parbhani", "Pune", "Ratnagiri", "Sangli", "Satara", "Solapur", "Thane", "Wardha", "Washim", "Yavatmal"},
        {"Bishnupur", "Chandel", "Churachandpur", "Imphal", "Jiribam", "Kakching", "Kamjong", "Kangpokpi", "Noney Longmai", "Pherzawl", "Porompat", "Senapati", "Tamenglong", "Tengnoupal", "Thoubal", "Ukhrul"},
        {"Ampati", "Baghmara", "Jowai", "Khleihriat", "Mawkyrwat", "Nongpoh", "Nongstoin", "Resubelpara", "Shillong", "Tura", "Williamnagar"},
        {"Aizawl", "Champhai", "Kolasib", "Lawngtlai", "Lunglei", "Mamit", "Saiha", "Serchhip"},
        {"Dimapur", "Kiphire", "Kohima", "Longleng", "Mokokchung", "Mon", "Noklak", "Peren", "Phek", "Tuensang", "Wokha", "Zunheboto"},
        {"Angul", "Balangir", "Balasore", "Bargarh", "Baripada", "Bhadrak", "Bhawanipatna", "Bhubaneswar", "Boudh", "Chhatrapur", "Cuttack", "Debagarh", "Dhenkanal", "Jagatsinghpur", "Jharsuguda", "Kendrapara", "Kendujhar", "Koraput", "Malkangiri", "Nabarangpur", "Nayagarh", "Nuapada", "Panikoili", "Paralakhemundi", "Phulbani", "Puri", "Rayagada", "Sambalpur", "Subarnapur", "Sundargarh"},
        {"Karaikal","Mahe","Pondicherry","Yanam"},
        {"Amritsar", "Barnala", "Bathinda", "Faridkot", "Fatehgarh Sahib", "Fazilka", "Firozpur", "Gurdaspur", "Hoshiarpur", "Jalandhar", "Kapurthala", "Ludhiana", "Mansa", "Moga", "Mohali", "Nawanshahr", "Pathankot", "Patiala", "Rupnagar", "Sangrur", "Sri Muktsar Sahib", "Tarn Taran Sahib"},
        {"Ajmer", "Alwar", "Banswara", "Baran", "Barmer", "Bharatpur", "Bhilwara", "Bikaner", "Bundi", "Chittorgarh", "Churu", "Dausa", "Dholpur", "Dungarpur", "Ganganagar", "Hanumangarh", "Jaipur", "Jaisalmer", "Jalore", "Jhalawar", "Jhunjhunu", "Jodhpur", "Karauli", "Kota", "Nagaur", "Pali", "Pratapgarh", "Rajsamand", "Sawai Madhopur", "Sikar", "Sirohi", "Tonk", "Udaipur"},
        {"Gangtok", "Geyzing", "Mangan", "Namchi"},
        {"Ariyalur", "Chengalpattu", "Chennai", "Coimbatore", "Cuddalore", "Dharmapuri", "Dindigul", "Erode", "Hosur", "Kallakurichi", "Kanchipuram", "Karur", "Krishnagiri", "Madurai", "Mayiladuthurai", "Nagapattinam", "Nagercoil", "Namakkal", "Perambalur", "Pudukkottai", "Ramanathapuram", "Ranipet", "Salem", "Sivaganga", "Tenkasi", "Thanjavur", "Theni", "Thoothukudi", "Thoothukudi (Tuticorin)", "Tiruchirappalli", "Tirunelveli", "Tirupattur", "Tirupur", "Tiruvallur", "Tiruvannaamalai", "Tiruvarur", "Udagamandalam (Ooty)", "Vellore", "Viluppuram", "Virudhunagar"},
        {"Adilabad", "Bhongiri", "Bhupalpalle", "Gadwal", "Geesugonda", "Hyderabad", "Jagtial", "Jangaon", "Kamareddy", "Karimnagar", "Khammam", "Komaram Bheem", "Kothagudem", "Mahabubabad", "Mahbubnagar", "Mancherial", "Medak", "Mulugu", "Nagarkurnool", "Nalgonda", "Narayanpet", "Nirmal", "Nizamabad", "Peddapalle", "Sangareddy", "Shamirpet", "Shamshabad", "Siddipet", "Sircilla", "Suryapet", "Vikarabad", "Wanaparthy", "Warangal"},
        {"Agartala", "Ambassa", "Belonia", "Bishramganj", "Dharmanagar", "Kailashahar", "Khowai", "Udaipur Tripura"},
        {"Agra", "Akbarpur", "Akbarpur (Mati)", "Aligarh", "Allahabad", "Amroha", "Auraiya", "Azamgarh", "Baghpat", "Bahraich", "Ballia", "Balrampur", "Banda", "Barabanki", "Bareilly", "Basti", "Bijnor", "Budaun", "Bulandshahr", "Chandauli", "Deoria", "Etah", "Etawah", "Faizabad", "Fatehgarh", "Fatehpur", "Firozabad", "Gauriganj", "Ghaziabad", "Ghazipur", "Gonda", "Gorakhpur", "Gyanpur", "Hamirpur", "Hapur", "Hardoi", "Hathras", "Jaunpur", "Jhansi", "Kannauj", "Kanpur", "Karwi", "Kasganj", "Khalilabad", "Lakhimpur", "Lalitpur", "Lucknow", "Maharajganj", "Mahoba", "Mainpuri", "Manjhanpur", "Mathura", "Mau", "Meerut", "Mirzapur", "Moradabad", "Muzaffarnagar", "Naugarh", "Noida", "Orai", "Padrauna", "Pilibhit", "Pratapgarh", "Raebareli", "Rampur", "Robertsganj", "Saharanpur", "Sambhal", "Shahjahanpur", "Shamli", "Shravasti", "Sitapur", "Sultanpur", "Unnao", "Varanasi"},
        {"Almora", "Bageshwar", "Champawat", "Dehradun", "Gopeshwar", "Haridwar", "Nainital", "New Tehri", "Pauri", "Pithoragarh", "Rudraprayag", "Rudrapur", "Uttarkashi"},
        {"Alipore", "Alipurduar", "Baharampur", "Balurghat", "Bankura", "Barasat", "Bardhaman", "Chinsurah", "Cooch Behar", "Darjeeling", "English Bazar", "Howrah", "Jalpaiguri", "Kolkata", "Krishnanagar", "Midnapore", "Purulia", "Raiganj", "Suri", "Tamluk"}

};
    ArrayAdapter<String> userStateAdapter;
    Button btn;
    //    private AViewModel aViewModel;
    private int onViewStateRestoredCallBack = 0;

    private String selectedUserState = "Select";
    private String selectedUserCity = "Select";
    private Switch soundToggle;
    private SoundManager soundManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_d, container, false);

        if(!InternetConnectivityUtil.isConnected(getContext())) {
            InternetConnectivityUtil.closeApp(getContext(), this.getActivity());
        }

        for (int i = 0; i < STATES.length; i++) {
            stateIndexMap.put(STATES[i], i);
        }

        soundToggle = view.findViewById(R.id.soundToggle);
        soundManager = SoundManager.getInstance(requireContext());

        retrieveSoundToggle();

        loadStatesArray();

        autoCompleteTextView = view.findViewById(R.id.auto_complete_text);
        adapterItems = new ArrayAdapter<String>(getContext(), R.layout.custom_drop_down, STATES);

        autoCompleteTextView.setAdapter(adapterItems);

        // For city
        autoCompleteTextView1 = view.findViewById(R.id.auto_complete_text_city);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

                saveSelectedUserState(item);   saveSelectedUserCity("");
                autoCompleteTextView1.setText("");
                autoCompleteTextView.setText(item);
                autoCompleteTextView.setSelection(item.length());

                int index = stateIndexMap.get(item);
                System.out.println("Saved index: " + index);
                saveStatePosition(index);
                saveStateChangeFlag(true);
                saveStateChangeFlag_B_o(true);
                Toast.makeText(getContext(), "State: " + item, Toast.LENGTH_SHORT).show();
                String[] filteredCities = filterCitiesByState(index);
//                System.out.println("Cities list");
//                for(String s: filter)
                adapterItems1 = new ArrayAdapter<String>(getContext(), R.layout.custom_drop_down, filteredCities);
                autoCompleteTextView1.setAdapter(adapterItems1);
            }
        });

        if(retrieveStatePosition() != -1){
            System.out.println("Retrieved index: " + retrieveStatePosition());
            String[] filteredCities = filterCitiesByState(retrieveStatePosition());
            adapterItems1 = new ArrayAdapter<String>(getContext(), R.layout.custom_drop_down, filteredCities);
            autoCompleteTextView1.setAdapter(adapterItems1);
        }

        autoCompleteTextView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                saveSelectedUserCity(item);
                autoCompleteTextView1.setText(item);  autoCompleteTextView1.setSelection(item.length());
                saveStateChangeFlag(true);  saveStateChangeFlag_B_o(true);
                Toast.makeText(getContext(), "City: " + item, Toast.LENGTH_SHORT).show();
            }
        });

        soundToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) soundManager.enableSound(); // Enable sound
                else  soundManager.disableSound(); // Disable sound
                storingSoundToggle();
            }
        });
        return view;
    }

    private void loadStatesArray() {
        states = Arrays.asList(new String[]{"Andaman and Nicobar", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chandigarh", "Chhattisgarh", "Dadra and Nagar Haveli", "Daman and Diu", "Delhi", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Lakshadweep", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Puducherry",
                "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal"});
    }

    String[] filterCitiesByState (int index){
        return CITIES[index];
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getId() == R.id.presentStateSpinner1) {
            selectedUserState = parent.getItemAtPosition(position).toString();
            saveSelectedUserState(selectedUserState);

            autoCompleteTextView.setText(selectedUserState);
            autoCompleteTextView.setSelection(selectedUserState.length());
            System.out.println("onItemSelected() > " + selectedUserState);
            if(onViewStateRestoredCallBack == 0){
                saveStateChangeFlag(true);  saveStateChangeFlag_B_o(true);
            }
            onViewStateRestoredCallBack = 0;
        }
    }

    private void saveStatePosition(int flag) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        System.out.println("Position_flag " + flag);
        editor.putInt("state_change_position", flag);
        editor.apply();
    }

    private int retrieveStatePosition() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("state_change_position", -1);
    }

    private void saveStateChangeFlag(boolean flag) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        System.out.println("flag " + flag);
        editor.putBoolean("state_change_flag", flag);
        editor.apply();
    }

    private void saveStateChangeFlag_B_o(boolean flag) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        System.out.println("flag_B_o " + flag);
        editor.putBoolean("state_change_flag_B_o", flag);
        editor.apply();
    }


    private void saveSelectedUserState(String selectedUserState) {
        SharedPreferences sharedPreferences =  requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("selected_userState", selectedUserState);
        editor.apply();
    }

    private void saveSelectedUserCity(String selectedUserCity) {
        SharedPreferences sharedPreferences =  requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("selected_userCity", selectedUserCity);
        editor.apply();
    }
    //
//    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        System.out.println("onNothingSelected() ");
    }
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        selectedUserState = sharedPreferences.getString("selected_userState", "Telangana");
        autoCompleteTextView.setText(selectedUserState);
        autoCompleteTextView.setSelection(selectedUserState.length());

        selectedUserCity = sharedPreferences.getString("selected_userCity", "Kamareddy");
        autoCompleteTextView1.setText(selectedUserCity);
        autoCompleteTextView1.setSelection(selectedUserCity.length());
    }

    public void storingSoundToggle(){
        // Get a reference to the SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Get the SharedPreferences editor
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Get the current state of the soundToggle and store it
        boolean isSoundEnabled = soundManager.isSoundEnabled();
        editor.putBoolean("soundEnabled", isSoundEnabled);

        // Apply the changes
        editor.apply();
    }

    public void retrieveSoundToggle(){
        // Get a reference to the SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Retrieve the stored boolean value
        boolean isSoundEnabled = sharedPreferences.getBoolean("soundEnabled", true); // Default value is true

        // Set the soundToggle based on the retrieved value
        soundToggle.setChecked(isSoundEnabled);
    }

}

