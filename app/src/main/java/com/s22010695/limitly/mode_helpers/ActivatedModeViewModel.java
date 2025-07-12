package com.s22010695.limitly.mode_helpers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ActivatedModeViewModel extends ViewModel {
    private final MutableLiveData<String> activatedMode = new MutableLiveData<>("none");

    public LiveData<String> getActivatedMode() {
        return activatedMode;
    }

    public void setActivatedMode(String mode) {
        activatedMode.setValue(mode);
    }
}
