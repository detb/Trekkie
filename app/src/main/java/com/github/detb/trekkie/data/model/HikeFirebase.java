package com.github.detb.trekkie.data.model;

public class HikeFirebase {
    private String title;
    private String description;
    private int pictureId;
    private String jsonHikePoints;

    public HikeFirebase() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPictureId() {
        return pictureId;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }

    public String getJsonHikePoints() {
        return jsonHikePoints;
    }

    public void setJsonHikePoints(String jsonHikePoints) {
        this.jsonHikePoints = jsonHikePoints;
    }
}
