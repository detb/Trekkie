package com.github.detb.trekkie.db.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.github.detb.trekkie.data.model.Hike;
import com.github.detb.trekkie.data.model.HikePoint;

@Database(entities = {Hike.class, HikePoint.class}, version = 1)
public abstract class HikeDatabase extends RoomDatabase {
    private static HikeDatabase instance;
    public abstract HikeDao hikeDao();

    public static synchronized HikeDatabase getInstance(Context context){
        if (instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(), HikeDatabase.class, "hike_database").fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
