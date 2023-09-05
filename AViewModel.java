package com.example.bottom_navigationbar_view;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AViewModel extends ViewModel {
    private MutableLiveData<String> selectedUserState = new MutableLiveData<>();

    public LiveData<String> getSelectedUserState() {
        return selectedUserState;
    }

    public void setSelectedUserState(String state) {
        selectedUserState.setValue(state);
    }
}
