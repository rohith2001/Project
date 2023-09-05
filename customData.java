package com.example.bottom_navigationbar_view;

import java.util.ArrayList;

public class customData {
    private ArrayList<String> data;
    private int imageResource;

    public customData(ArrayList<String> data, int imageResource) {
        this.data = data;
        this.imageResource = imageResource;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public int getImageResource() {
        return imageResource;
    }
}

