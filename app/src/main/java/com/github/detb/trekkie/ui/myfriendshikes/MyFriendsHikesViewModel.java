package com.github.detb.trekkie.ui.myfriendshikes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyFriendsHikesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MyFriendsHikesViewModel() {

        mText = new MutableLiveData<>();
        mText.setValue("This is where i can see my friends hikes");
    }

    public LiveData<String> getText() {
        return mText;
    }
}