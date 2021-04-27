package com.github.detb.trekkie;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.google.firebase.database.IgnoreExtraProperties;
import com.mapbox.geojson.Point;

@IgnoreExtraProperties
@Entity(tableName = "hike_table")
public class Hike implements Serializable {

    @Ignore
    private static final Gson gson = new Gson();

    public void setId(int id) {
        this.id = id;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    private String title;
    private String description;
    private int pictureId;

    @TypeConverter
    public static List<HikePoint> stringToHikePointList(String data)
    {
        if (data == null)
        {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<HikePoint>>(){}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String hikePointListToString(List<HikePoint> hikePoints){
        return gson.toJson(hikePoints);
    }

    @TypeConverters(Hike.class)
    public List<HikePoint> hikePointList = new ArrayList<>();

    public Hike(String title, String description, int pictureId, List<HikePoint> hikePointList) {
        this.hikePointList = hikePointList;
        this.title = title;
        this.description = description;
        this.pictureId = pictureId;
    }

    public void addHikePoint(HikePoint hikePoint)
    {
        hikePointList.add(hikePoint);
    }

    public List<HikePoint> getHikePointList() {
        return hikePointList;
    }

    public List<Point> getAllPoints() {
        List<Point> points = new ArrayList<>();
        for (HikePoint hikepoint:hikePointList
             ) {
            points.add(hikepoint.position);
        }
        return points;
    }

    public String getCoordinatesAsString()
    {
        StringBuilder toReturn = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#.####");
        List<Point> points = getAllPoints();
        for (Point point:points
             ) {
            toReturn.append('[');
            toReturn.append(df.format(point.coordinates().get(0)));
            toReturn.append(',');
            toReturn.append(df.format(point.coordinates().get(1)));
            toReturn.append(']');
            toReturn.append(',');
        }
        toReturn.deleteCharAt(toReturn.length()-1);
        return toReturn.toString();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }

    public void setHikePointList(List<HikePoint> hikePointList) {
        this.hikePointList = hikePointList;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPictureId() {
        return pictureId;
    }
}
