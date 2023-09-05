package com.example.bottom_navigationbar_view;

public class HorizontalItem {
    private String itemName1, itemName2;
    private int imageResourceId;

    public HorizontalItem(String itemName1, String itemName2, int imageResourceId) {
        this.itemName1 = itemName1;
        this.itemName2 = itemName2;
        this.imageResourceId = imageResourceId;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getItemName1() {
        return itemName1;
    }
    public String getItemName2() {
        return itemName2;
    }
}
