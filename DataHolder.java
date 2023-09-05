package com.example.bottom_navigationbar_view;
import java.util.ArrayList;

public class DataHolder {
    private static DataHolder instance;
    private ArrayList<ArrayList<String>> dataList;

    private DataHolder() {
        dataList = new ArrayList<>();
    }

    public static DataHolder getInstance() {
        if (instance == null) {
            instance = new DataHolder();
        }
        return instance;
    }

    public void setDataList(ArrayList<ArrayList<String>> dataList) {
        this.dataList = dataList;
    }

    public ArrayList<ArrayList<String>> getDataList() {
        return dataList;
    }
}

