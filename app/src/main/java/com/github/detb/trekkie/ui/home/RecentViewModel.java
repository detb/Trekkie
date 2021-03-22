package com.github.detb.trekkie.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.detb.trekkie.Hike;
import com.github.detb.trekkie.R;

import java.util.ArrayList;
import java.util.List;

public class RecentViewModel extends ViewModel {

    private MutableLiveData<List<Hike>> hikeLiveData = new MutableLiveData<>();

    public RecentViewModel() {

        // POPULATING ARRAY LIST -- THIS NEEDS TO BE DONE BY ACCESSING DATABASE
        ArrayList<Hike> hikes = new ArrayList<>();
        hikes.add(new Hike("Hike - 2020/02", "Beautiful place", R.drawable.ic_menu_camera));
        hikes.add(new Hike("Hike - 2021/02", "Good walk", R.drawable.ic_menu_camera));
        hikes.add(new Hike("Hike - 2019/03", "nice tour", R.drawable.ic_menu_gallery));
        hikes.add(new Hike("Hike - 2020/05", "nice place", R.drawable.ic_menu_camera));
        hikes.add(new Hike("Hike - 2020/09", "Ugly place", R.drawable.ic_menu_camera));

        hikeLiveData.setValue(hikes);
    }

    public LiveData<List<Hike>> getHikeLiveData() {
        return hikeLiveData;
    }

// public void addHike(Hike hike)
// {
//     hikeLiveData.getValue().add(hike);
// }
}