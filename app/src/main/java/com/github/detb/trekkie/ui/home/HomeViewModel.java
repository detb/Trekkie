package com.github.detb.trekkie.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.github.detb.trekkie.data.model.Hike;
import com.github.detb.trekkie.db.HikeFirebaseRepository;
import com.github.detb.trekkie.db.room.HikeRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {


    private final HikeRepository hikeRepository;
    //private final HikeFirebaseRepository hikeFirebaseRepository;

    public HomeViewModel(Application application) {
        super(application);

        hikeRepository = HikeRepository.getInstance(application);
        //hikeFirebaseRepository = HikeFirebaseRepository.getInstance();
    }

    public LiveData<List<Hike>> getHikeLiveData() {
        //Log.d("TAG", "Value is: " + hikeFirebaseRepository.getHike().getValue().toString());

        //List<Hike> hikes = new ArrayList<>();
        //MutableLiveData<List<Hike>> listToReturn = new MutableLiveData<>(hikes);
        //if (!(hikeFirebaseRepository.getHike().equals(null))){
        //    listToReturn.getValue().add(hikeFirebaseRepository.getHike().getValue());
        //    return  listToReturn;
        //}
        //return listToReturn;
        return hikeRepository.getAllHikes();
    }

}