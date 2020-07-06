package com.a.ara;


/**
 * Created by REZA on 7/5/2020.
 */

public class Album {
    private int id;
    private String publish_date;
    private String genre;
    private String title;

    public static String key_id = "id";
    public static String key_title = "title";
    public static String key_genre = "genre";
    public static String key_publish_date = "publish_date";


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(String publish_date) {
        this.publish_date = publish_date;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
