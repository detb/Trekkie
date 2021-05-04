package com.github.detb.trekkie.db.room;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.github.detb.trekkie.data.model.Hike;

import java.util.List;

public class HikeRepository {
    // variables declaration
    private final HikeDao hikeDao;
    private static HikeRepository instance;
    private final LiveData<List<Hike>> allHikes;

    // Constructor
    private HikeRepository(Application application)
    {
        HikeDatabase database = HikeDatabase.getInstance(application);
        hikeDao = database.hikeDao();
        allHikes = hikeDao.getAllHikes();
    }

    // getInstance
    public static synchronized HikeRepository getInstance(Application application){
        if (instance == null)
            instance = new HikeRepository(application);
        return instance;
    }

    // insert
    public void insert(Hike hike)
    {
        new InsertHikeAsync(hikeDao).execute(hike);
    }

    private static class InsertHikeAsync extends AsyncTask<Hike, Void, Void> {
        private final HikeDao hikeDao;

        private InsertHikeAsync(HikeDao hikeDao){
            this.hikeDao = hikeDao;
        }

        @Override
        protected Void doInBackground(Hike... hikes) {
            hikeDao.insert(hikes[0]);
            return null;
        }
    }

    // get
    public LiveData<List<Hike>> getAllHikes(){
        return allHikes;
    }

    public LiveData<Hike> getHike(int id)
    {
        return hikeDao.getHike(id);
    }

    // delete
    public void delete(Hike hike)
    {
        new DeleteHikeAsync(hikeDao).execute(hike);
    }

    private static class DeleteHikeAsync extends AsyncTask<Hike, Void, Void> {
        private final HikeDao hikeDao;

        private DeleteHikeAsync(HikeDao hikeDao) {this.hikeDao = hikeDao;}

        @Override
        protected Void doInBackground(Hike... hikes){
            hikeDao.delete(hikes[0]);
            return null;
        }
    }
}


