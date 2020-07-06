package com.a.ara;

public class Music {
    private int id; // primary key
    private String title;
    private int duration;
    private String genre;

    public static String key_music_id = "id";
    public static String key_music_title = "title";
    public static String key_music_duration = "duration";
    public static String key_music_genre = "genre";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
