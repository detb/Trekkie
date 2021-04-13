package com.github.detb.trekkie.db;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.github.detb.trekkie.Hike;

import java.util.List;

public class HikeRepository {
    private HikeDao hikeDao;
    private HikePointDao hikePointDao;
    private static HikeRepository instance;
    private LiveData<List<Hike>> allHikes;

    private HikeRepository(Application application)
    {
        HikeDatabase database = HikeDatabase.getInstance(application);
        hikeDao = database.hikeDao();
        hikePointDao = database.hikePointDao();
        allHikes = hikeDao.getAllHikes();
    }

    public static synchronized HikeRepository getInstance(Application application){
        if (instance == null)
            instance = new HikeRepository(application);
        return instance;
    }

    public LiveData<List<Hike>> getAllHikes(){
        return allHikes;
    }

    public void insert(Hike hike)
    {
        new InsertHikeAsync(hikeDao).execute(hike);
    }

    private static class InsertHikeAsync extends AsyncTask<Hike, Void, Void> {
        private HikeDao hikeDao;

        private InsertHikeAsync(HikeDao hikeDao){
            this.hikeDao = hikeDao;
        }

        @Override
        protected Void doInBackground(Hike... hikes) {
            hikeDao.insert(hikes[0]);
            return null;
        }
    }

    public void delete(Hike hike)
    {
        new DeleteHikeAsync(hikeDao).execute(hike);
    }

    private static class DeleteHikeAsync extends AsyncTask<Hike, Void, Void> {
        private HikeDao hikeDao;

        private DeleteHikeAsync(HikeDao hikeDao) {this.hikeDao = hikeDao;}

        @Override
        protected Void doInBackground(Hike... hikes){
            hikeDao.delete(hikes[0]);
            return null;
        }
    }
}


