package com.github.detb.trekkie.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.detb.trekkie.Hike;
import com.github.detb.trekkie.HikePoint;
import com.github.detb.trekkie.MainActivity;
import com.github.detb.trekkie.R;
import com.github.detb.trekkie.db.HikeRepository;
import com.mapbox.geojson.Point;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {


    private final HikeRepository hikeRepository;

    public HomeViewModel(Application application) {
        super(application);

        hikeRepository = HikeRepository.getInstance(application);
    }

    public LiveData<List<Hike>> getHikeLiveData() {
        return hikeRepository.getAllHikes();
    }

}