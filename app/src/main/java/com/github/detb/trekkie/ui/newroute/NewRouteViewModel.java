package com.github.detb.trekkie.ui.newroute;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.detb.trekkie.Hike;
import com.github.detb.trekkie.db.HikeRepository;

public class NewRouteViewModel extends AndroidViewModel {

    private final HikeRepository hikeRepository;

    public NewRouteViewModel(Application application) {
        super(application);

        hikeRepository = HikeRepository.getInstance(application);

    }

    public void pushHikeToDb(Hike hike){
        // hikeRepository.saveHikeToFirebase(hikeData); NOT WORKING
        hikeRepository.insert(hike);
    }
}