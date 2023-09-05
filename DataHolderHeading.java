package com.example.bottom_navigationbar_view;

import java.util.ArrayList;
import java.util.List;


public class DataHolderHeading {
    private static DataHolderHeading instance;
    private List<HorizontalItem> dataListHeading;

    private DataHolderHeading() {
        dataListHeading = new ArrayList<>();
    }

    public static DataHolderHeading getInstance() {
        if (instance == null) {
            instance = new DataHolderHeading();
        }
        return instance;
    }

    public void setDataList(List<HorizontalItem> dataList) {
        this.dataListHeading = dataList;
    }

    public List<HorizontalItem> getDataList() {
        return dataListHeading;
    }
}

