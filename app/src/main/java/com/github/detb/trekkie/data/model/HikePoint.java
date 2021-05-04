package com.github.detb.trekkie.data.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mapbox.geojson.Point;

import java.lang.reflect.Type;
import java.util.List;

@Entity(tableName = "hikePoints")
public class HikePoint {
    // variables declaration
    @TypeConverters(HikePoint.class)
    public Point position;
    private String description;

    // used to convert to JSON
    @Ignore
    private static final Gson gson = new Gson();

    // TypeConverters used by Room
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

    @PrimaryKey(autoGenerate = true)
    private int id;

    // Constructor
    public HikePoint(Point position, String description) {
        this.position = position;
        this.description = description;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Point getPosition() {
        return position;
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

    @Override
    public String toString() {
        return "HikePoint{" +
                "position=" + position +
                ", description='" + description + '\'' +
                '}';
    }
}

