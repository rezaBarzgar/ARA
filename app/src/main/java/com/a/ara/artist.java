package com.a.ara;

import java.util.Date;

public class artist extends user{

    private String genre;
    private Date career_start_date;
    private boolean valid;
    private String nickname;

    public static String key_genre = "genre";
    public static String key_career_start_date = "career_start_date";
    public static String key_valid = "valid";
    public static String key_nickname = "nickname";

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Date getCareer_start_date() {
        return career_start_date;
    }

    public void setCareer_start_date(Date career_start_date) {
        this.career_start_date = career_start_date;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


}
