package com.github.detb.trekkie.ui.specificroute;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.github.detb.trekkie.data.model.Hike;
import com.github.detb.trekkie.db.room.HikeRepository;

public class SpecificRouteViewModel extends AndroidViewModel {

    private final HikeRepository repository;

    public SpecificRouteViewModel(@NonNull Application application) {
        super(application);

        repository = HikeRepository.getInstance(application);
    }

    public LiveData<Hike> getHike(int id)
    {
        return repository.getHike(id);
    }

    public void deleteHike(Hike hike)
    {
        repository.delete(hike);
    }
}
