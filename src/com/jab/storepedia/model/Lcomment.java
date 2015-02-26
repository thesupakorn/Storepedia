package com.jab.storepedia.model;
 
import java.util.ArrayList;
 
public class Lcomment {
    private String username, thumbnailUrl, comment;
	private int agreed, disagreed, cpid;
 
    public Lcomment() {
    }
 
    public Lcomment(int cpid, String username, String thumbnailUrl, int agreed, int disagreed, String comment) {
        this.cpid = cpid;
    	this.username = username;
        this.agreed = agreed;
        this.disagreed = disagreed;
        this.comment = comment;
        this.thumbnailUrl = thumbnailUrl;
    }
 
    public int getCPID() {
        return cpid;
    }
 
    public void setCPID(int cpid) {
        this.cpid = cpid;
    }
    
    public String getUsername() {
        return username;
    }
 
    public void setUsername(String username) {
        this.username = username;
    }
 
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
 
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
 
    public int getagreed() {
        return agreed;
    }
 
    public void setagreed(int agreed) {
        this.agreed = agreed;
    }
    public int getdisagreed() {
        return disagreed;
    }
 
    public void setdisagreed(int disagreed) {
        this.disagreed = disagreed;
    }
    public String getcomment() {
        return comment;
    }
 
    public void setcomment(String comment) {
        this.comment = comment;
    }
 
}