package com.ag.appprueba_musictest.entities;

import java.sql.Time;

public class Songs {

    private int id;
    private String title;
    private String artistName;
    private String album;
    private Time duration;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getArtistName(){
        return artistName;
    }

    public void setArtistName(String artistName){
        this.artistName = artistName;
    }

    public String getAlbum(){
        return album;
    }

    public void setAlbum(String album){
        this.album = album;
    }

    public Time getDuration() {
        return duration;
    }

    public void setDuration(Time duration) {
        this.duration = duration;
    }

    public void setDuration(int durationInMillis) {
        this.duration = new Time(durationInMillis);
    }

}
