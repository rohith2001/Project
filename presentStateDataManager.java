package com.example.bottom_navigationbar_view;

public class presentStateDataManager {

    private static String userState = "";

    static String getUserState() {
        return presentStateDataManager.userState;
    }

    public static void setUserState(String userState) {
        presentStateDataManager.userState = userState;
    }
}
