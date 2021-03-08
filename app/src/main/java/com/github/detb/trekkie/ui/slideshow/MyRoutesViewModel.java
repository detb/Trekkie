package com.github.detb.trekkie.ui.slideshow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyRoutesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyRoutesViewModel() {

        mText = new MutableLiveData<>();
        mText.setValue("This is where i can see my routes");
    }

    public LiveData<String> getText() {
        return mText;
    }
}