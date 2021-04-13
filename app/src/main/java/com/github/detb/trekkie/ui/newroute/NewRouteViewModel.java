package com.github.detb.trekkie.ui.newroute;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.detb.trekkie.Hike;
import com.github.detb.trekkie.db.HikeRepository;

public class NewRouteViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;
    private HikeRepository hikeRepository;

    public NewRouteViewModel(Application application) {
        super(application);

        hikeRepository = HikeRepository.getInstance(application);


        mText = new MutableLiveData<>();
        mText.setValue("This is where you can add new routes");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void pushHikeToDb(Hike hikeData){
        hikeRepository.insert(hikeData);
    }
}