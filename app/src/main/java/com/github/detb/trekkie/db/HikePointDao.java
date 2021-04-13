package com.github.detb.trekkie.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.github.detb.trekkie.HikePoint;

import java.util.List;

@Dao
public interface HikePointDao {
    @Insert
    void insert(HikePoint hikePoint);

    @Update
    void update(HikePoint hikePoint);

    @Delete
    void delete(HikePoint hikePoint);

    @Query("SELECT * FROM hike_table")
    LiveData<List<HikePoint>> getAllHikePoints();
}
