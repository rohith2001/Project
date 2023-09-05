package com.example.bottom_navigationbar_view;

import java.util.ArrayList;

public class DataHolderMeat {
    private static DataHolderMeat instance;
    private ArrayList<ArrayList<String>> dataList;

    private DataHolderMeat() {
        dataList = new ArrayList<>();
    }

    public static DataHolderMeat getInstance() {
        if (instance == null) {
            instance = new DataHolderMeat();
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