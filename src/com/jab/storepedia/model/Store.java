package com.jab.storepedia.model;
 
import java.util.ArrayList;
 
public class Store {
    private String title, thumbnailUrl;
    private int LID, SID;
    private double rating;
    private String genre;
 
    public Store() {
    }
 
    public Store(String name, String thumbnailUrl, int LID, int SID, double rating,
            String genre) {
        this.title = name;
        this.thumbnailUrl = thumbnailUrl;
        this.LID = LID;
        this.SID = SID;
        this.rating = rating;
        this.genre = genre;
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
 
    public int getSID() {
        return SID;
    }
    
    public void setSID(int SID) {
        this.SID = SID;
    }
    
    public double getRating() {
        return rating;
    }
 
    public void setRating(double rating) {
        this.rating = rating;
    }
 
    public String getGenre() {
        return genre;
    }
 
    public void setGenre(String genre) {
        this.genre = genre;
    }
 
}