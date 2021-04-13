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

public class RecentViewModel extends AndroidViewModel {

    private MutableLiveData<List<Hike>> hikeLiveData = new MutableLiveData<>();
    private HikeRepository hikeRepository;

    public RecentViewModel(Application application) {
        super(application);

        hikeRepository = HikeRepository.getInstance(application);
        // POPULATING ARRAY LIST -- THIS NEEDS TO BE DONE BY ACCESSING DATABASE

        Point point1 = Point.fromLngLat(-57.225365, -33.213144);
        Point point2 = Point.fromLngLat(-54.14164, -33.981818);
        Point point3 = Point.fromLngLat(-56.990533, -30.583266);

        HikePoint hikePoint = new HikePoint(point1, "Beaut");
        HikePoint hikePoint2 = new HikePoint(point2, "Beauty");
        HikePoint hikePoint3 = new HikePoint(point3, "Beautiful");

        ArrayList<HikePoint> points = new ArrayList<>();
        points.add(hikePoint);
        points.add(hikePoint2);
        points.add(hikePoint3);

        ArrayList<Hike> hikes = new ArrayList<>();
        hikes.add(new Hike("Hike - 2020/02", "Beautiful place", R.drawable.ic_menu_camera, points));
        hikes.add(new Hike("Hike - 2021/02", "Good walk", R.drawable.ic_menu_camera, points));
        hikes.add(new Hike("Hike - 2019/03", "nice tour", R.drawable.ic_menu_gallery, points));
        hikes.add(new Hike("Hike - 2020/05", "nice place", R.drawable.ic_menu_camera, points));
        hikes.add(new Hike("Hike - 2020/09", "Ugly place", R.drawable.ic_menu_camera, points));

    }

    public LiveData<List<Hike>> getHikeLiveData() {
        return hikeRepository.getAllHikes();
    }

// public void addHike(Hike hike)
// {
//     hikeLiveData.getValue().add(hike);
// }
}