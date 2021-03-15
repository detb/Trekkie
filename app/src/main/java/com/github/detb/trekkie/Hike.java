package com.github.detb.trekkie;

public class Hike {

    private String title;
    private String description;
    private int pictureId;

    public Hike(String title, String description, int pictureId) {
        this.title = title;
        this.description = description;
        this.pictureId = pictureId;
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
