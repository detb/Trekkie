package com.github.detb.trekkie.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RecentViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RecentViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the home page");
    }

    public LiveData<String> getText() {
        return mText;
    }
}