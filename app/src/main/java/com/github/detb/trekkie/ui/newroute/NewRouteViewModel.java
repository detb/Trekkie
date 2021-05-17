package com.github.detb.trekkie.ui.newroute;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.github.detb.trekkie.data.model.Hike;
import com.github.detb.trekkie.db.HikeFirebaseRepository;
import com.github.detb.trekkie.db.room.HikeRepository;

public class NewRouteViewModel extends AndroidViewModel {

    private final HikeRepository hikeRepository;
    //private final HikeFirebaseRepository hikeFirebaseRepository;

    public NewRouteViewModel(Application application) {
        super(application);

        hikeRepository = HikeRepository.getInstance(application);
        //hikeFirebaseRepository = HikeFirebaseRepository.getInstance();
        //hikeFirebaseRepository.setText();
    }

    public void pushHikeToDb(Hike hike){

        //hikeFirebaseRepository.setHike(hike);
        // hikeRepository.saveHikeToFirebase(hikeData); NOT WORKING
        hikeRepository.insert(hike);
    }
}