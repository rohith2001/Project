package com.example.bottom_navigationbar_view;

import java.util.ArrayList;

public class DataHoldingEgg {
    private static DataHoldingEgg instance;
    private ArrayList<ArrayList<String>> dataList;

    ArrayList<String> graphDates;
    private ArrayList<Float> graphData;

    private DataHoldingEgg() {
        dataList = new ArrayList<>();
    }

    public static DataHoldingEgg getInstance() {
        if (instance == null) {
            instance = new DataHoldingEgg();
        }
        return instance;
    }

    public void setDataList(ArrayList<ArrayList<String>> dataList) {
        this.dataList = dataList;
    }

    public void setGraphData(ArrayList<Float> graphData) {
        this.graphData = graphData;
    }

    public void setGraphDates(ArrayList<String> graphDates) {
        this.graphDates = graphDates;
    }

    public ArrayList<Float> getGraphData() {
        return graphData;
    }

    public ArrayList<ArrayList<String>> getDataList() {
        return dataList;
    }

    public ArrayList<String> getGraphDates() {
        return graphDates;
    }
}