package com.example.bottom_navigationbar_view;

import java.util.ArrayList;
import java.util.List;

public class DataHolderHeading1 {
    private static DataHolderHeading1 instance;
    private List<HorizontalItem> dataListHeading;

    private DataHolderHeading1() {
        dataListHeading = new ArrayList<>();
    }

    public static DataHolderHeading1 getInstance() {
        if (instance == null) {
            instance = new DataHolderHeading1();
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
