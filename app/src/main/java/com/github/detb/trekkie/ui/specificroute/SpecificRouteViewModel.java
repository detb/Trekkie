package com.github.detb.trekkie.ui.specificroute;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.github.detb.trekkie.Hike;
import com.github.detb.trekkie.db.HikeRepository;

public class SpecificRouteViewModel extends AndroidViewModel {

    private HikeRepository repository;

    public SpecificRouteViewModel(@NonNull Application application) {
        super(application);

        repository = HikeRepository.getInstance(application);
    }

    public LiveData<Hike> getHike(int id)
    {
        return repository.getHike(id);
    }
}
