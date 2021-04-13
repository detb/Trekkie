package com.github.detb.trekkie.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.github.detb.trekkie.Hike;

import java.util.List;

@Dao
public interface HikeDao {
    @Insert
    void insert(Hike hike);

    @Update
    void update(Hike hike);

    @Delete
    void delete(Hike hike);

    @Query("SELECT * FROM HIKE_TABLE")
    LiveData<List<Hike>> getAllHikes();
}
