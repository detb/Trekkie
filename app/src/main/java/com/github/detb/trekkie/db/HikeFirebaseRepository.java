package com.github.detb.trekkie.db;

import androidx.lifecycle.LiveData;

import com.github.detb.trekkie.Hike;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class HikeFirebaseRepository {
    private HikeFirebaseDao hikeFirebaseDao;
    private static HikeFirebaseRepository instance;

    private HikeFirebaseRepository()
    {
        this.hikeFirebaseDao = HikeFirebaseDao.getInstance();
    }

    public static HikeFirebaseRepository getInstance(){
        if (instance == null)
            instance = new HikeFirebaseRepository();
        return instance;
    }

    public LiveData<Hike> getHike()
    {
        return  hikeFirebaseDao.getHike();
    }

    public LiveData<List<Hike>> getHikes()
    {
        return  hikeFirebaseDao.getHikes();
    }

    public void updateHike(Hike hike)
    {
        hikeFirebaseDao.updateHike(hike);
    }

    public void setUser(FirebaseUser user)
    {
        hikeFirebaseDao.setUser(user);
    }

    public FirebaseUser getUser()
    {
        return hikeFirebaseDao.getUser();
    }

    public void updateDatabase() {
        hikeFirebaseDao.updateDatabase();
    }

    public void setHike(Hike hike) {
        hikeFirebaseDao.setHike(hike);
    }

    public void setText() {
        hikeFirebaseDao.setText();
    }
}
