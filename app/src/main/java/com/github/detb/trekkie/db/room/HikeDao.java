package com.github.detb.trekkie.db.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.github.detb.trekkie.data.model.Hike;

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

    @Query("SELECT * FROM HIKE_TABLE WHERE id = :id")
    LiveData<Hike> getHike(int id);

    @Query("DELETE FROM HIKE_TABLE WHERE id = :id")
    void deleteHike(int id);
}
