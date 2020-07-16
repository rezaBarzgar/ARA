package com.a.ara;

public class playlist {

    private int id;
    private int ownerid;
    private String create_date;
    private String title;

    public static String key_id = "id";
    public static String key_title = "title";
    public static String key_owner_id = "ownerid";
    public static String key_create_date = "create_date";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(int ownerid) {
        this.ownerid = ownerid;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
