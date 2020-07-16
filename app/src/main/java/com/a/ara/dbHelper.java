package com.a.ara;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class dbHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String db_name = "ara_db";
    private String tablename = "tb_users";
    private final String cmd = "CREATE TABLE IF NOT EXISTS '" + tablename + "'" +
            "( 'userid' INTEGER PRIMARY KEY NOT NULL ," +
            " 'firstname' TEXT ," +
            " 'lastname' TEXT ," +
            " 'username' TEXT NOT NULL UNIQUE ," +
            " 'password' TEXT NOT NULL ," +
            " 'email' TEXT ," +
            " 'region' TEXT ," +
            " 'question' TEXT ," +
            " 'answer' TEXT )";

//    private final String cmd2 = "CREATE TABLE IF NOT EXISTS 'TEST' ('name' TEXT , 'phone' INTEGER);";

    private final String cmd3 = "CREATE TABLE IF NOT EXISTS 'tb_listner'('userid' integer not null ,'premium_type' integer ," +
            " 'premium_trial' datetime , 'birth_date' dateime , foreign key(userid) references tb_users(userid))";

    private final String cmd4 = "CREATE TABLE IF NOT EXISTS 'tb_listner_credit'('userid' integer not null ," +
            " 'credit_number' text , 'expire_date' date , foreign key(userid) references tb_users(userid))"; // should be filled later

    private final String cmd5 = "CREATE TABLE IF NOT EXISTS 'tb_artist'('userid' integer not null ," +
            " 'genre' text ,'career_start_date' datetime , 'valid' bool , 'nickname' text , foreign key(userid) references tb_users(userid))";

    private final String cmd6 = "CREATE TABLE IF NOT EXISTS 'tb_playlist'('id' INTEGER PRIMARY KEY NOT NULL ," +
            " 'ownerid' integer not null , 'title' text , 'create_date' date , " +
            "foreign key(ownerid) references tb_users(userid))";

    private final String cmd7 = "CREATE TABLE IF NOT EXISTS 'tb_music'('id' INTEGER PRIMARY KEY NOT NULL ," +
            " 'title' text ,'duration' integer , 'genre' text)";

    private final String cmd8 = "CREATE TABLE IF NOT EXISTS 'tb_album'('id' INTEGER PRIMARY KEY NOT NULL ," +
            " 'publish_date' date, 'title' text , 'genre' text)";

    private final String cmd9 = "CREATE TABLE IF NOT EXISTS 'tb_follow'('followingid' integer,'followerid' integer ," +
            " 'follow_date' date , foreign key(followingid) references tb_users(userid) ,foreign key(followerid) references tb_users(userid))";

    private final String cmd11 = "CREATE TABLE IF NOT EXISTS 'tb_liked_playlist'('userid' integer,'playlistid' integer ," +
            " 'like_date' date ,foreign key(userid) references tb_users(userid) ,foreign key(playlistid) references tb_playlist(id))";

    private final String cmd12 = "CREATE TABLE IF NOT EXISTS 'tb_played_song'('userid' integer not null, 'musicid' integer not null," + "" +
            "'played_date' date,foreign key(userid) references tb_users(userid),foreign key(musicid) references tb_music(id)," +
            " primary key (userid,musicid,played_date) )";

    private final String cmd13 = "CREATE TABLE IF NOT EXISTS 'tb_reported_song'('userid' integer not null, 'musicid' integer not null," +
            "'report_date' date,foreign key(userid) references tb_users(userid),foreign key(musicid) references tb_music(id)," +
            " primary key(userid,musicid))";

    private final String cmd14 = "CREATE TABLE IF NOT EXISTS 'tb_liked_music'('userid' integer not null, 'musicid' integer not null," +
            "'liked_date' date,foreign key(userid) references tb_users(userid),foreign key(musicid) references tb_music(id)," +
            " primary key(userid,musicid))";

    private final String cmd15 = "CREATE TABLE IF NOT EXISTS 'tb_liked_album'('userid' integer not null, 'albumid' integer not null," +
            "foreign key(userid) references tb_users(userid),foreign key(albumid) references tb_album(id))";

    private final String cmd16 = "CREATE TABLE IF NOT EXISTS 'tb_have_playlist'('playlistid' integer not null, 'musicid' integer not null," +
            " 'added_date' date ,foreign key(musicid) references tb_music(id), " +
            "foreign key (playlistid) references tb_playlist(id))";

    private final String cmd17 = "CREATE TABLE IF NOT EXISTS 'tb_have_album'('albumid' integer not null, 'userid' integer not null, 'musicid' integer not null," +
            " 'added_date' date,foreign key(userid) references tb_users(userid),foreign key(musicid) references tb_music(id), foreign key (albumid) references tb_album(id))";

    public dbHelper(Context context, String tableName) {
        super(context, db_name, null, 1);
        this.context = context;
//        this.tablename = tableName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(cmd);
        db.execSQL(cmd3);
        db.execSQL(cmd4);
        db.execSQL(cmd5);
        db.execSQL(cmd6);
        db.execSQL(cmd7);
        db.execSQL(cmd8);
        db.execSQL(cmd9);
        db.execSQL(cmd11);
        db.execSQL(cmd12);
        db.execSQL(cmd13);
        db.execSQL(cmd14);
        db.execSQL(cmd15);
        db.execSQL(cmd16);
        db.execSQL(cmd17);
//        db.execSQL("insert into 'tb_artist' values(2001,'pop',null,null,null)");
        Log.i("dbResult", "table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tablename);
        db.execSQL("DROP TABLE IF EXISTS 'tb_listner'");
        db.execSQL("DROP TABLE IF EXISTS 'tb_artist'");
        db.execSQL("DROP TABLE IF EXISTS 'tb_music'");
        db.execSQL("DROP TABLE IF EXISTS 'tb_album'");
        db.execSQL("DROP TABLE IF EXISTS 'tb_playlist'");
        db.execSQL("DROP TABLE IF EXISTS 'tb_have_album'");
        db.execSQL("DROP TABLE IF EXISTS 'tb_have_playlist'");
        db.execSQL("DROP TABLE IF EXISTS 'tb_shared_playlist'");
        db.execSQL("DROP TABLE IF EXISTS 'tb_follow'");
        db.execSQL("DROP TABLE IF EXISTS 'tb_played_song'");
        db.execSQL("DROP TABLE IF EXISTS 'tb_reported_song'");
        db.execSQL("DROP TABLE IF EXISTS 'tb_liked_music'");
        Log.i("dbResult", "table dropped");
        onCreate(db);
    }

    public void insert(ContentValues values, String tb_name) {
        SQLiteDatabase db = getWritableDatabase();
        long insertid = db.insert(tb_name, null, values);
        if (insertid == -1) {
            Log.i("insertion", "fucked with id : " + insertid);
        } else {
            Log.i("insertion", "inserted with id : " + insertid);
        }
        if (db.isOpen()) db.close();
    }

    public int delete(String tablename, String where_clause) {
        int count;
        SQLiteDatabase db = getWritableDatabase();
        count = db.delete(tablename, where_clause, null);
        if (db.isOpen()) db.close();
        return count;
    }

    public String date_to_string(Date date) {
        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String s = "";
        if (date == null) {
            s = format.format(d);
        } else {
            s = format.format(date);
        }
        return s;
    }

    public String date_to_string_without_time(Date date) {
        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String s = "";
        if (date == null) {
            s = format.format(d);
        } else {
            s = format.format(date);
        }
        return s;
    }

    public String custom_date_to_string(String tag, Date date) {
        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat day = new SimpleDateFormat("dd");
        SimpleDateFormat month = new SimpleDateFormat("MM");
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        String s = "";

        if (date == null) {
            if (tag.equals("year")) {
                s = year.format(d);
            } else if (tag.equals("month")) {
                s = month.format(d);
            } else if (tag.equals("day")) {
                s = day.format(d);
            }
        } else {
            if (tag.equals("year")) {
                s = year.format(date);
            } else if (tag.equals("month")) {
                s = month.format(date);
            } else if (tag.equals("day")) {
                s = day.format(date);
            }
        }
        return s;
    }

    public Date string_to_date(String s) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = format.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public List get_users_listner(String query, String username) {
        SQLiteDatabase db = getReadableDatabase();
        List user_list_listner = new ArrayList();
        Cursor cursor = db.rawQuery("select * from 'tb_users' where userid<1999 and" +
                " (firstname like '%" + query + "%' or lastname like '%" + query + "%'" +
                " or username like '" + query + "%') and not username='" + username + "'", null);
        if (cursor.moveToFirst()) {
            do {
                ContentValues temp_v = new ContentValues();
                temp_v.put(user.key_user_id, cursor.getInt(cursor.getColumnIndex(user.key_user_id)));
                temp_v.put(user.key_user_name, cursor.getString(cursor.getColumnIndex(user.key_user_name)));
                temp_v.put(user.key_user_first_name, cursor.getString(cursor.getColumnIndex(user.key_user_first_name)));
                temp_v.put(user.key_user_last_name, cursor.getString(cursor.getColumnIndex(user.key_user_last_name)));
                temp_v.put(user.key_user_email, cursor.getString(cursor.getColumnIndex(user.key_user_email)));
                temp_v.put(user.key_user_region, cursor.getString(cursor.getColumnIndex(user.key_user_region)));
                user_list_listner.add(temp_v);
            } while (cursor.moveToNext());
        }
        cursor.close();
        if (db.isOpen()) db.close();
        return user_list_listner;
    }

    public List get_users_artist(String query, String username) {
        SQLiteDatabase db = getReadableDatabase();
        List user_list_artist = new ArrayList();
        Cursor cursor = db.rawQuery("select * from 'tb_users' as U left join 'tb_artist' as A " +
                "on U.userid = A.userid where U.userid between 2000 and 2999 and " +
                "(U.firstname like '%" + query + "%' or U.lastname like '%" + query + "%' " +
                "or U.username like '%" + query + "%' or A.nickname like '%" + query + "%') " +
                "and not U.username = '" + username + "'", null);
        if (cursor.moveToFirst()) {
            do {
                ContentValues temp_v = new ContentValues();
                temp_v.put(user.key_user_id, cursor.getInt(cursor.getColumnIndex(user.key_user_id)));
                temp_v.put(user.key_user_name, cursor.getString(cursor.getColumnIndex(user.key_user_name)));
                temp_v.put(user.key_user_first_name, cursor.getString(cursor.getColumnIndex(user.key_user_first_name)));
                temp_v.put(user.key_user_last_name, cursor.getString(cursor.getColumnIndex(user.key_user_last_name)));
                temp_v.put(user.key_user_email, cursor.getString(cursor.getColumnIndex(user.key_user_email)));
                temp_v.put(user.key_user_region, cursor.getString(cursor.getColumnIndex(user.key_user_region)));
                temp_v.put(artist.key_nickname, cursor.getString(cursor.getColumnIndex(artist.key_nickname)));
                temp_v.put(artist.key_genre, cursor.getString(cursor.getColumnIndex(artist.key_genre)));
                user_list_artist.add(temp_v);
            } while (cursor.moveToNext());
        }
        cursor.close();
        if (db.isOpen()) db.close();
        return user_list_artist;
    }

    public List get_song(String query) {
        SQLiteDatabase db = getReadableDatabase();
        List song_list = new ArrayList();
        Cursor cursor = db.rawQuery("select music.id, music.duration, music.genre, music.title,artist.nickname, album.title  from 'tb_have_album' as have_album join 'tb_music' as music " +
                "on music.id = have_album.musicid " +
                "join 'tb_artist' as artist on have_album.userid = artist.userid " +
                "join 'tb_album' as album on album.id = have_album.albumid " +
                "where music.title like '" + query + "%'", null);
        if (cursor.moveToFirst()) {
            do {
                ContentValues temp_v = new ContentValues();
                temp_v.put(Music.key_music_id, cursor.getInt(cursor.getColumnIndex(Music.key_music_id)));
                temp_v.put(Music.key_music_duration, cursor.getString(cursor.getColumnIndex(Music.key_music_duration)));
                temp_v.put(Music.key_music_genre, cursor.getString(cursor.getColumnIndex(Music.key_music_genre)));
                temp_v.put(Music.key_music_title, cursor.getString(cursor.getColumnIndex(Music.key_music_title)));
                temp_v.put(artist.key_nickname, cursor.getString(cursor.getColumnIndex(artist.key_nickname)));
                temp_v.put(Album.key_title, cursor.getString(cursor.getColumnIndex(Album.key_title)));

                song_list.add(temp_v);
            } while (cursor.moveToNext());
        }
        cursor.close();
        if (db.isOpen()) db.close();
        return song_list;
    }

    public List get_songs_of_album(int albumid) {
        SQLiteDatabase db = getReadableDatabase();
        List song_list = new ArrayList();
        Cursor cursor = db.rawQuery("select music.id,music.title,music.duration,music.genre,artist.nickname from 'tb_have_album' as have_album  join 'tb_music' as music  " +
                "               on music.id = have_album.musicid " +
                "                join 'tb_artist' as artist on have_album.userid = artist.userid " +
                "                 join 'tb_album' as album on album.id = have_album.albumid  " +
                "               where album.id = " + String.valueOf(albumid), null);
        if (cursor.moveToFirst()) {
            do {
                ContentValues temp_v = new ContentValues();
                temp_v.put(Music.key_music_id, cursor.getInt(cursor.getColumnIndex(Music.key_music_id)));
                temp_v.put(Music.key_music_duration, cursor.getString(cursor.getColumnIndex(Music.key_music_duration)));
                temp_v.put(Music.key_music_genre, cursor.getString(cursor.getColumnIndex(Music.key_music_genre)));
                temp_v.put(Music.key_music_title, cursor.getString(cursor.getColumnIndex(Music.key_music_title)));
                temp_v.put(artist.key_nickname, cursor.getString(cursor.getColumnIndex(artist.key_nickname)));

                song_list.add(temp_v);
            } while (cursor.moveToNext());
        }
        cursor.close();
        if (db.isOpen()) db.close();
        return song_list;
    }

    public List get_songs_of_song(int songid) {
        SQLiteDatabase db = getReadableDatabase();
        List song_list = new ArrayList();

        Cursor cursor = db.rawQuery("select music.id, music.title, music.duration, music.genre, artist.nickname from 'tb_have_album' as A " +
                "join 'tb_music' as music on music.id = A.musicid join 'tb_artist' as artist on A.userid = artist.userid " +
                "where A.albumid in ( " +
                "select B.albumid from 'tb_have_album' as B " +
                "where B.musicid =  "+ String.valueOf(songid)+")", null);

        if (cursor.moveToFirst()) {
            do {
                ContentValues temp_v = new ContentValues();
                temp_v.put(Music.key_music_id, cursor.getInt(cursor.getColumnIndex(Music.key_music_id)));
                temp_v.put(Music.key_music_duration, cursor.getString(cursor.getColumnIndex(Music.key_music_duration)));
                temp_v.put(Music.key_music_genre, cursor.getString(cursor.getColumnIndex(Music.key_music_genre)));
                temp_v.put(Music.key_music_title, cursor.getString(cursor.getColumnIndex(Music.key_music_title)));
                temp_v.put(artist.key_nickname, cursor.getString(cursor.getColumnIndex(artist.key_nickname)));

                song_list.add(temp_v);
            } while (cursor.moveToNext());
        }
        cursor.close();
        if (db.isOpen()) db.close();
        return song_list;
    }

    public List get_songs_of_playlist(int playlistid) {
        SQLiteDatabase db = getReadableDatabase();
        List song_list = new ArrayList();
        Cursor cursor = db.rawQuery("select music.id, music.title, music.duration, music.genre, artist.nickname from 'tb_have_playlist' as A " +
                "join 'tb_music' as music on music.id = A.musicid join 'tb_have_album' as B " +
                "on B.musicid = A.musicid join 'tb_artist' as artist on B.userid = artist.userid " +
                "where A.playlistid = "+String.valueOf(playlistid), null);
                        //      cursor is empty
        if (cursor.moveToFirst()) {
            do {
                ContentValues temp_v = new ContentValues();
                temp_v.put(Music.key_music_id, cursor.getInt(cursor.getColumnIndex(Music.key_music_id)));
                temp_v.put(Music.key_music_duration, cursor.getString(cursor.getColumnIndex(Music.key_music_duration)));
                temp_v.put(Music.key_music_genre, cursor.getString(cursor.getColumnIndex(Music.key_music_genre)));
                temp_v.put(Music.key_music_title, cursor.getString(cursor.getColumnIndex(Music.key_music_title)));
                temp_v.put(artist.key_nickname, cursor.getString(cursor.getColumnIndex(artist.key_nickname)));

                song_list.add(temp_v);
            } while (cursor.moveToNext());
        }
        cursor.close();
        if (db.isOpen()) db.close();
        return song_list;
    }

    public List get_album(String query) {
        SQLiteDatabase db = getReadableDatabase();
        List album_list = new ArrayList();
        Cursor cursor = db.rawQuery("select * from 'tb_have_album' as H join 'tb_album' as album on H.albumid = album.id " +
                "join 'tb_artist' as artist_b on H.userid = artist_b.userid " +
                "where album.title like '" + query + "%'", null);
        if (cursor.moveToFirst()) {
            do {
                ContentValues temp_v = new ContentValues();
                temp_v.put(Album.key_id, cursor.getInt(cursor.getColumnIndex(Album.key_id)));
                temp_v.put(Album.key_title, cursor.getString(cursor.getColumnIndex(Album.key_title)));
                temp_v.put(Album.key_genre, cursor.getString(cursor.getColumnIndex(Album.key_genre)));
                temp_v.put(Album.key_publish_date, cursor.getString(cursor.getColumnIndex(Album.key_publish_date)));
                temp_v.put(artist.key_nickname, cursor.getString(cursor.getColumnIndex(artist.key_nickname)));
                album_list.add(temp_v);
            } while (cursor.moveToNext());
        }
        cursor.close();
        if (db.isOpen()) db.close();
        return album_list;
    }

    public List get_playlist(String query) {
        SQLiteDatabase db = getReadableDatabase();
        List playlist_list = new ArrayList();
        Cursor cursor = db.rawQuery("SELECT pl.id, pl.ownerid, pl.title,pl.create_date, user.username FROM 'tb_playlist' as pl join 'tb_users' as user on pl.ownerid = user.userid " +
                "where pl.title like '" +query+"%' ", null);
        if (cursor.moveToFirst()) {
            do {
                ContentValues temp_v = new ContentValues();
                temp_v.put(playlist.key_id, cursor.getInt(cursor.getColumnIndex(playlist.key_id)));
                temp_v.put(playlist.key_owner_id, cursor.getInt(cursor.getColumnIndex(playlist.key_owner_id)));
                temp_v.put(playlist.key_title, cursor.getString(cursor.getColumnIndex(playlist.key_title)));
                temp_v.put(playlist.key_create_date, cursor.getString(cursor.getColumnIndex(playlist.key_create_date)));
                temp_v.put(user.key_user_name, cursor.getString(cursor.getColumnIndex(user.key_user_name)));
                playlist_list.add(temp_v);
            } while (cursor.moveToNext());
        }
        cursor.close();
        if (db.isOpen()) db.close();
        return playlist_list;
    }

    public List get_follow_items(String which, int userid) {
        SQLiteDatabase db = getReadableDatabase();
        List result = new ArrayList();
        Cursor cursor;
        Cursor user_cursor;
        int id;

        if (which.equals("followers")) {
            cursor = db.rawQuery("select * from 'tb_follow' where " +
                    "followerid=" + String.valueOf(userid), null);

            if (cursor.moveToFirst()) {
                do {
                    ContentValues values = new ContentValues();
                    id = cursor.getInt(cursor.getColumnIndex("followingid"));
                    user_cursor = db.rawQuery("select * from 'tb_users' where userid=" +
                            String.valueOf(id), null);

                    if (user_cursor.moveToFirst()) {
                        values.put(user.key_user_id, user_cursor.getInt(user_cursor.getColumnIndex(user.key_user_id)));
                        values.put(user.key_user_name, user_cursor.getString(user_cursor.getColumnIndex(user.key_user_name)));
                        values.put(user.key_user_first_name, user_cursor.getString(user_cursor.getColumnIndex(user.key_user_first_name)));
                        values.put(user.key_user_last_name, user_cursor.getString(user_cursor.getColumnIndex(user.key_user_last_name)));
                        result.add(values);
                    }
                    user_cursor.close();
                } while (cursor.moveToNext());
            }
            cursor.close();
        } else if (which.equals("followings")) {
            cursor = db.rawQuery("select * from 'tb_follow' where " +
                    "followingid=" + String.valueOf(userid), null);

            if (cursor.moveToFirst()) {
                do {
                    ContentValues values = new ContentValues();
                    id = cursor.getInt(cursor.getColumnIndex("followerid"));
                    user_cursor = db.rawQuery("select * from 'tb_users' where userid=" +
                            String.valueOf(id), null);

                    if (user_cursor.moveToFirst()) {
                        values.put(user.key_user_id, user_cursor.getInt(user_cursor.getColumnIndex(user.key_user_id)));
                        values.put(user.key_user_name, user_cursor.getString(user_cursor.getColumnIndex(user.key_user_name)));
                        values.put(user.key_user_first_name, user_cursor.getString(user_cursor.getColumnIndex(user.key_user_first_name)));
                        values.put(user.key_user_last_name, user_cursor.getString(user_cursor.getColumnIndex(user.key_user_last_name)));
                        result.add(values);
                    }
                    user_cursor.close();
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        if (db.isOpen()) db.close();
        return result;
    }

    public String follow_user(int following_id, int follower_id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from 'tb_follow' where followingid="
                + String.valueOf(following_id) + " and followerid="
                + String.valueOf(follower_id), null);

        if (cursor.getCount() > 0) {
            return "you are already following this user";
        } else {
            cursor.close();
            if (db.isOpen()) db.close();
            db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("followingid", following_id);
            values.put("followerid", follower_id);
            values.put("follow_date", date_to_string(null));
            long insertid = db.insert("tb_follow", null, values);
            if (db.isOpen()) db.close();
            if (insertid == -1) {
                return "Failed";
            } else return "Followed";
        }
    }

    public String unfollow_user(int following_id, int follower_id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from 'tb_follow' where followingid="
                + String.valueOf(following_id) + " and followerid="
                + String.valueOf(follower_id), null);

        if (cursor.getCount() == 0) {
            return "you are not following this user";
        } else {
            cursor.close();
            if (db.isOpen()) db.close();
            db = getWritableDatabase();
            long deleteid = db.delete("tb_follow", "followingid =" + String.valueOf(following_id) +
                    " and followerid =" + String.valueOf(follower_id), null);
            if (db.isOpen()) db.close();
            if (deleteid == -1) {
                return "Failed";
            } else return "unFollowed";
        }
    }

    public int show_followers_count(int userid) {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from 'tb_follow' where " +
                "followerid=" + String.valueOf(userid), null);

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        if (db.isOpen()) db.close();
        return count;
    }

    public int show_following_count(int userid) {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from 'tb_follow' where " +
                "followingid=" + String.valueOf(userid), null);

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        if (db.isOpen()) db.close();
        return count;
    }

    public String play_song(int userid, int musicid) {
        String result = "";
        int premium = -1;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from 'tb_listner' as listner " +
                "where listner.userid = " + String.valueOf(userid), null);
        if (cursor.moveToFirst()) {
            premium = cursor.getInt(cursor.getColumnIndex(listner.key_premium_type));
        }
        cursor.close();
        if (premium == 0) {
            String yesterday = "";
            String today = custom_date_to_string("day", null);
            String this_month = custom_date_to_string("month", null);
            String this_year = custom_date_to_string("year", null);

            int day = Integer.valueOf(today);
            day--;
            yesterday = String.valueOf(day) + "/" + this_month + "/" + this_year;
            yesterday = date_to_string(string_to_date(yesterday));

            cursor = db.rawQuery("SELECT * FROM 'tb_played_song' as PL " +
                    "where PL.userid = " + String.valueOf(userid) + " and PL.played_date > '" + yesterday + "'", null);
            cursor.moveToFirst();
            if (cursor.getCount() < 5) {
                if (db.isOpen()) db.close();
                cursor.close();
                ContentValues values = new ContentValues();
                values.put(user.key_user_id, userid);
                values.put("musicid", musicid);
                values.put("played_date", date_to_string_without_time(null));
                insert(values, "tb_played_song");
                return "played";
            } else return "you can't play any more songs today";

        } else if (premium > 0 && premium < 5) {
            if (db.isOpen()) db.close();
            ContentValues values = new ContentValues();
            values.put(user.key_user_id, userid);
            values.put(Music.key_music_id, musicid);
            values.put("played_date", date_to_string(null));
            insert(values, "tb_played_song");
            return "played";
        }

        return result;
    }

    public String report_song(int userid, int musicid) {
        String result = "";
        int report_state = -1;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from 'tb_reported_song' " +
                "where userid = " + String.valueOf(userid) + " and musicid =" + String.valueOf(musicid), null);
        if (cursor.moveToFirst()) {
            report_state = cursor.getInt(0);
        }
        if (report_state == 1) {
            return "you have already reported this song";
        } else if (report_state == 0) {
            if (db.isOpen()) db.close();
            cursor.close();
            ContentValues values = new ContentValues();
            values.put(user.key_user_id, userid);
            values.put("musicid", musicid);
            values.put("report_date", date_to_string(null));
            insert(values, "tb_reported_song");
            return "reported";
        }

        return result;
    }

    public String like_song(int userid, int musicid) {
        String result = "";
        int like_state = -1;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from 'tb_liked_music' " +
                "where userid = " + String.valueOf(userid) + " and musicid = " + String.valueOf(musicid), null);
        if (cursor.moveToFirst()) {
            like_state = cursor.getInt(0);
        }
        if (like_state == 0) {
            if (db.isOpen()) db.close();
            cursor.close();
            ContentValues values = new ContentValues();
            values.put(user.key_user_id, userid);
            values.put("musicid", musicid);
            values.put("liked_date", date_to_string(null));
            insert(values, "tb_liked_music");
            return "liked";
        } else if (like_state == 1) return "you already liked it";

        return result;
    }

    public String unlike_song(int userid, int musicid) {
        String result = "";
        int like_state = -1;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from 'tb_liked_music' " +
                "where userid = " + String.valueOf(userid) + " and musicid = " + String.valueOf(musicid), null);
        if (cursor.moveToFirst()) {
            like_state = cursor.getInt(0);
        }
        if (db.isOpen()) db.close();
        cursor.close();
        if (like_state == 1) {
            int res = delete("tb_liked_music", "userid = " + String.valueOf(userid) + " and musicid = " + String.valueOf(musicid));
            if (res == 1) {
                return "unliked";
            } else return "failed";
        } else if (like_state == 0) return "you dont even liked that!";
        return result;
    }

    public String like_album(int userid, int albumid) {
        String result = "";
        int like_state = -1;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from 'tb_liked_album' " +
                "where userid = " + String.valueOf(userid) + " and albumid = " + String.valueOf(albumid), null);
        if (cursor.moveToFirst()) {
            like_state = cursor.getInt(0);
        }
        if (like_state == 0) {
            if (db.isOpen()) db.close();
            cursor.close();
            ContentValues values = new ContentValues();
            values.put(user.key_user_id, userid);
            values.put("albumid", albumid);
            insert(values, "tb_liked_album");
            return "liked";
        } else if (like_state == 1) return "you already liked it";


        return result;
    }

    public String unlike_album(int userid, int albumid) {
        String result = "";
        int like_state = -1;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from 'tb_liked_album' " +
                "where userid = " + String.valueOf(userid) + " and albumid = " + String.valueOf(albumid), null);
        if (cursor.moveToFirst()) {
            like_state = cursor.getInt(0);
        }
        if (db.isOpen()) db.close();
        cursor.close();
        if (like_state == 1) {
            int res = delete("tb_liked_album", "userid = " + String.valueOf(userid) + " and albumid = " + String.valueOf(albumid));
            if (res == 1) {
                return "unliked";
            } else return "failed";
        } else if (like_state == 0) return "you don't even liked that!";
        return result;
    }

    public ContentValues get_artist_from_musicid(int id, String tag) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getReadableDatabase();
        if (tag.equals("song")) {
            Cursor cursor = db.rawQuery("SELECT artist.userid, artist.genre, artist.career_start_date, artist.valid, artist.nickname FROM 'tb_have_album' as A " +
                    "join 'tb_artist' as artist on A.userid = artist.userid " +
                    "where A.musicid =" + String.valueOf(id), null);
            if (cursor.moveToFirst()) {
                values.put(artist.key_user_id, cursor.getInt(cursor.getColumnIndex(artist.key_user_id)));
                values.put(artist.key_genre, cursor.getString(cursor.getColumnIndex(artist.key_genre)));
                values.put(artist.key_career_start_date, cursor.getString(cursor.getColumnIndex(artist.key_career_start_date)));
                values.put(artist.key_valid, cursor.getInt(cursor.getColumnIndex(artist.key_valid)));
                values.put(artist.key_nickname, cursor.getString(cursor.getColumnIndex(artist.key_nickname)));
            }
            cursor.close();
            if (db.isOpen()) db.close();
        } else if (tag.equals("album")) {
            Cursor cursor = db.rawQuery("SELECT album.id, album.publish_date, album.title, album.genre FROM 'tb_have_album' as A " +
                    "join 'tb_album' as album on A.albumid = album.id " +
                    "where A.musicid =" + String.valueOf(id), null);
            if (cursor.moveToFirst()) {
                values.put(Album.key_id, cursor.getInt(cursor.getColumnIndex(Album.key_id)));
                values.put(Album.key_publish_date, cursor.getString(cursor.getColumnIndex(Album.key_publish_date)));
                values.put(Album.key_title, cursor.getString(cursor.getColumnIndex(Album.key_title)));
                values.put(Album.key_genre, cursor.getString(cursor.getColumnIndex(Album.key_genre)));
            }
            cursor.close();
            if (db.isOpen()) db.close();
        }


        return values;
    }

    public String sug_another_artist(int userid) {
        String result = "suggested artist : ";
        int artist_id = -1;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select artist.userid, count(*) from 'tb_played_song' as played join 'tb_have_album' as A " +
                "on played.musicid = A.musicid join 'tb_artist' as artist " +
                "on artist.userid = A.userid " +
                "where played.userid =  "+ String.valueOf(userid) +
                " group by artist.userid " +
                "limit 1", null);
        if (cursor.moveToFirst()) {
            artist_id = cursor.getInt(0);
        }

        cursor.close();

        if (artist_id != -1) {
            cursor = db.rawQuery("select artist.nickname from'tb_artist' as artist " +
                    "where artist.genre = ( " +
                    "select artist.genre from 'tb_artist' as artist " +
                    "where artist.userid =  " + String.valueOf(artist_id) +
                    ") and not artist.userid =" + String.valueOf(artist_id), null);
        }
        if (cursor.moveToFirst()) {
            result = result + cursor.getString(0);
        } else {
            result = "not founded suggested artist";
        }
        if (db.isOpen()) db.close();
        cursor.close();
        return result;
    }

    public String sug_popular_songs_of_week() {
        String result = "songs of the week : ";
        String thisweek = "";
        String today = custom_date_to_string("day", null);
        String this_month = custom_date_to_string("month", null);
        String this_year = custom_date_to_string("year", null);

        int day = Integer.valueOf(today);
        day = day - 7;
        if (day <= 0) day = 1;
        thisweek = String.valueOf(day) + "/" + this_month + "/" + this_year ;
        thisweek = "'" + date_to_string_without_time(string_to_date(thisweek)) + "'" ;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select music.title,count(*) from 'tb_played_song' as played join " +
                "'tb_music' as music on music.id = played.musicid " +
                "where played.played_date > " + thisweek +
                "group by music.title " +
                "limit 5", null);

        if (cursor.moveToFirst()) {
            do {
                result = result + cursor.getString(cursor.getColumnIndex(Music.key_music_title)) + ", ";
            } while (cursor.moveToNext());
        }
        if (db.isOpen()) db.close();
        cursor.close();
        return result;
    }

    public String sug_music_based_on_playes_genre(int userid) {
        String result = "suggested songs : ";
        String genre_name = "";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select music.genre, count(*) as cnt from 'tb_played_song' as played " +
                "join 'tb_music' as music on music.id = played.musicid " +
                "where played.userid = " + String.valueOf(userid) +
                " group by music.genre " +
                " order by cnt desc " +
                " limit 1", null);
        if (cursor.moveToFirst()) {
            genre_name = cursor.getString(0);
        }
        cursor.close();
        if (!genre_name.equals("")) {
            cursor = db.rawQuery("select music.title from 'tb_music' as music " +
                    "where music.genre = '" + genre_name + "' and music.id not in ( " +
                    "select played.musicid from 'tb_played_song' as played " +
                    "where played.userid = " + String.valueOf(userid) +
                    ") limit 5", null);
        }

        if (cursor.moveToFirst()) {
            do {
                result = result + cursor.getString(0) + ", ";
            } while (cursor.moveToNext());
        } else result = "not founded suggested music";
        if (db.isOpen()) db.close();
        cursor.close();
        return result;
    }

    public String sug_music_based_on_playlist(int userid) {
        String result = "suggestion for playlist : ";
        String playlist_id = "";
        String common_genre = "";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT A.playlistid , count(*) FROM 'tb_have_playlist' as A join 'tb_playlist' as playlist " +
                "on A.playlistid = playlist.id " +
                "where playlist.ownerid = " + String.valueOf(userid) +
                " group by A.playlistid limit 1", null);
        if (cursor.moveToFirst()) {
            playlist_id = String.valueOf(cursor.getInt(0));
        }
        cursor.close();
        cursor = db.rawQuery("select music.genre , count(*) from 'tb_have_playlist' as A join " +
                "'tb_music' as music on A.musicid = music.id " +
                "where A.playlistid = '" + playlist_id +
                "' group by music.genre limit 1", null);
        if (cursor.moveToFirst()) {
            common_genre = cursor.getString(0);
        }
        cursor.close();
        cursor = db.rawQuery("select music.title from 'tb_music' as music " +
                "where music.genre = '" + common_genre + "' and music.id not in ( " +
                "select A.musicid from 'tb_have_playlist' as A " +
                "where A.playlistid = '" + playlist_id + "' )" +
                " limit 5", null);
        if (cursor.moveToFirst()) {
            do {
                result = result + cursor.getString(0) + ", ";
            } while (cursor.moveToNext());

        } else result = "not founded suggested music for you playlist";
        if (db.isOpen()) db.close();
        cursor.close();
        return result;
    }

    public String sug_same_region_artist(int userid) {
        String result = "suggested artist in your region : ";
        String user_region = "";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select user.region from 'tb_users' as user " +
                "where user.userid = " + String.valueOf(userid), null);
        if (cursor.moveToFirst()) {
            user_region = String.valueOf(cursor.getInt(0));
        }
        cursor.close();
        cursor = db.rawQuery("select artist.nickname from 'tb_artist' as artist join " +
                "        'tb_users' as user on artist.userid = user.userid " +
                "        where user.region = " + user_region + " and artist.userid not in ( " +
                "          select followerid from 'tb_follow' " +
                "          where followingid = 1030 and followerid between 2000 and 2999 " +
                "        )limit 1", null);
        if (cursor.moveToFirst()) {
            result = result + (cursor.getString(0));
        } else result = "no artist from you region founded";
        if (db.isOpen()) db.close();
        cursor.close();
        return result;
    }

    public String latest_song_played(int userid) {
        String result = "last music that played : ";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select music.title from 'tb_played_song' as played join 'tb_music' as music " +
                "on music.id = played.musicid " +
                " where played.userid = " + String.valueOf(userid) +
                " order by played.played_date desc " +
                " limit 1", null);
        if (cursor.moveToFirst()) {
            result = result + cursor.getString(0);
        }else result = "no music played recently";
        cursor.close();
        return result;
    }

    public String five_latest_songs_of_artist(int userid){
        String result ="5 latest songs of artist : ";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select music.title from 'tb_have_album' as A "+
                "join 'tb_music' as music on music.id = A.musicid "+
                "where A.userid = "+String.valueOf(userid)+
                " order by A.added_date desc "+
                "limit 5", null);
        if (cursor.moveToFirst()) {
            result = result + cursor.getString(0)+ ", ";
        }else result = "no music released recently";
        if (db.isOpen()) db.close();
        cursor.close();
        return result;
    }

    public String check_for_premium_end(int userid) {
        String result = "";

        return result;
    }

    public String saved_question(int userid){
        String result ;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT user.question  FROM 'tb_users' as user " +
                "where user.userid = "+String.valueOf(userid), null);
        if (cursor.moveToFirst()){
            result = cursor.getString(0);
        }else result = "not founded !";
        if (db.isOpen()) db.close();
        cursor.close();
        return result;
    }

    public String change_pass(String answer,String new_pass,int userid){
        String result = "";
        boolean is_equal;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM 'tb_users' as user " +
                "where user.userid = "+String.valueOf(userid), null);
        if (cursor.moveToFirst()){
            ContentValues temp_v = new ContentValues();
            is_equal = answer.equals(cursor.getString(cursor.getColumnIndex(user.key_user_answer)));
            if (is_equal){
                temp_v.put(user.key_user_id,cursor.getInt(cursor.getColumnIndex(user.key_user_id)));
                temp_v.put(user.key_user_first_name,cursor.getString(cursor.getColumnIndex(user.key_user_first_name)));
                temp_v.put(user.key_user_last_name,cursor.getString(cursor.getColumnIndex(user.key_user_last_name)));
                temp_v.put(user.key_user_name,cursor.getString(cursor.getColumnIndex(user.key_user_name)));
                temp_v.put(user.key_user_password,new_pass);
                temp_v.put(user.key_user_email,cursor.getString(cursor.getColumnIndex(user.key_user_email)));
                temp_v.put(user.key_user_region,cursor.getString(cursor.getColumnIndex(user.key_user_region)));
                temp_v.put(user.key_user_question,cursor.getString(cursor.getColumnIndex(user.key_user_question)));
                temp_v.put(user.key_user_answer,cursor.getString(cursor.getColumnIndex(user.key_user_answer)));
                temp_v.put(user.key_user_answer,cursor.getString(cursor.getColumnIndex(user.key_user_answer)));

                delete("tb_users","userid = " + String.valueOf(userid));
                insert(temp_v,"tb_users");
                result = "password changed successfully";
            }else{
                result = "your answers doesn't match !";
            }
        }else result = "not founded !";
        if (db.isOpen()) db.close();
        cursor.close();
        return result;
    }

    public List get_artist_albums(int userid){
        SQLiteDatabase db = getReadableDatabase();
        List result = new ArrayList();
        Cursor cursor;
        cursor = db.rawQuery("select distinct album.id,album.title, artist.userid from 'tb_album' as album  " +
                "join 'tb_have_album' as A on A.albumid = album.id " +
                "join 'tb_artist' as artist on A.userid = artist.userid " +
                "where artist.userid = " + String.valueOf(userid), null);
        if (cursor.moveToFirst()){
            do {
                ContentValues values = new ContentValues();
                values.put(Album.key_id, cursor.getInt(cursor.getColumnIndex(Album.key_id)));
                values.put(Album.key_title, cursor.getString(cursor.getColumnIndex(Album.key_title)));
                values.put(artist.key_user_id, cursor.getString(cursor.getColumnIndex(artist.key_user_id)));
                result.add(values);
            }while (cursor.moveToNext());
        }
        if (db.isOpen()) db.close();
        cursor.close();
        return result;
    }

    public String add_new_album(String title,String genre){
        String result="";
        int id;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("select id from 'tb_album' " +
                "order by id desc " +
                "limit 1", null);
        if (cursor.moveToFirst()){
            id = cursor.getInt(0) + 1;
        }else id = 4001;
        ContentValues values = new ContentValues();
        values.put(Album.key_id,id);
        values.put(Album.key_publish_date,date_to_string_without_time(null));
        values.put(Album.key_title, title);
        values.put(Album.key_genre, genre);

        insert(values,"tb_album");
        result = "inserted successfully";
        if (db.isOpen()) db.close();
        cursor.close();
        return result;
    }
    public String add_song(String title, int duration, String genre,int albumid, int userid){
        String result = "";
        int music_id;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("select id from 'tb_music' " +
                "order by id desc " +
                "limit 1", null);
        if (cursor.moveToFirst()){
            music_id = cursor.getInt(0) + 1;
        }else music_id =3001;

        ContentValues music_values = new ContentValues();
        music_values.put(Music.key_music_id,music_id);
        music_values.put(Music.key_music_title , title);
        music_values.put(Music.key_music_duration, duration);
        music_values.put(Music.key_music_genre, genre);
        insert(music_values,"tb_music");


        ContentValues have_album = new ContentValues();
        have_album.put(Album.key_id,albumid);
        have_album.put(artist.key_user_id,userid);
        have_album.put(Music.key_music_id,music_id);
        have_album.put("added_date", date_to_string_without_time(null));
        insert(have_album, "tb_have_album");


        result = "successfully added!";
        if (db.isOpen()) db.close();
        cursor.close();
        return result;

    }
}

