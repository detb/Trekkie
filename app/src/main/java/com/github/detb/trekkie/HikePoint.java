package com.github.detb.trekkie;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mapbox.geojson.Point;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity(tableName = "hikePoints")
public class HikePoint {

    @Ignore
    private static Gson gson = new Gson();

    @PrimaryKey(autoGenerate = true)
    private int id;


    @TypeConverter
    public static Point stringToPoint(String data)
    {
        if (data == null)
        {
            return null;
        }

        Type pointType = new TypeToken<List<HikePoint>>(){}.getType();

        return gson.fromJson(data, pointType);
    }

    @TypeConverter
    public static String hikePointListToString(Point point){
        return gson.toJson(point);
    }

    @TypeConverters(HikePoint.class)
    public Point position;

    private String description;

    public HikePoint(Point position, String description) {
        this.position = position;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Point getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "HikePoint{" +
                "position=" + position +
                ", description='" + description + '\'' +
                '}';
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

