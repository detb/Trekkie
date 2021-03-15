package com.github.detb.trekkie.ui.newroute;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewRouteViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NewRouteViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is where you can add new routes");
    }

    public LiveData<String> getText() {
        return mText;
    }
}