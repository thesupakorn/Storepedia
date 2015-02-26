package com.jab.storepedia.model;
 
import java.util.ArrayList;
 
public class Location {
    private String title, thumbnailUrl;
    private int LID, Number_of_store;
 
    public Location() {
    }
 
    public Location(String name, String thumbnailUrl, int LID, int Number_of_store) {
        this.title = name;
        this.thumbnailUrl = thumbnailUrl;
        this.LID = LID;
        this.Number_of_store = Number_of_store;
    }
 
    public String getTitle() {
        return title;
    }
 
    public void setTitle(String name) {
        this.title = name;
    }
 
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
 
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
 
    public int getLID() {
        return LID;
    }
 
    public void setLID(int LID) {
        this.LID = LID;
    }
    public int getNum() {
        return Number_of_store;
    }
 
    public void setNum(int Number_of_store) {
        this.Number_of_store = Number_of_store;
    }
 
}