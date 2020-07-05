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

public class dbHelper extends SQLiteOpenHelper{

    private static final String db_name = "ARA_db";
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
            " 'answer' TEXT )" ;

//    private final String cmd2 = "CREATE TABLE IF NOT EXISTS 'TEST' ('name' TEXT , 'phone' INTEGER);";

    private final String cmd3 = "CREATE TABLE IF NOT EXISTS 'tb_listner'('userid' integer not null ,'premium_type' integer ," +
            " 'premium_trial' datetime , 'birth_date' dateime , foreign key(userid) references tb_users(userid))";

    private final String cmd4 = "CREATE TABLE IF NOT EXISTS 'tb_listner_credit'('userid' integer not null ," +
            " 'credit_number' text , 'expire_date' date , foreign key(userid) references tb_users(userid))";

    private final String cmd5 = "CREATE TABLE IF NOT EXISTS 'tb_artist'('userid' integer not null ," +
            " 'genre' text ,'career_start_date' datetime , 'valid' bool , 'nickname' text , foreign key(userid) references tb_users(userid))";

    private final String cmd6 = "CREATE TABLE IF NOT EXISTS 'tb_playlist'('id' INTEGER PRIMARY KEY NOT NULL ," +
            " 'ownerid' integer not null , 'title' text , 'create_date' date , 'number_of_musics' integer , " +
            "foreign key(ownerid) references tb_users(userid))";

    private final String cmd7 = "CREATE TABLE IF NOT EXISTS 'tb_music'('id' INTEGER PRIMARY KEY NOT NULL ," +
            " 'title' text ,'duration' integer , 'genre' text)";

    private final String cmd8 = "CREATE TABLE IF NOT EXISTS 'tb_album'('id' INTEGER PRIMARY KEY NOT NULL ," +
            " 'publish_date' date, 'title' text , 'genre' text)";

    private final String cmd9 = "CREATE TABLE IF NOT EXISTS 'tb_follow'('followingid' integer,'followerid' integer ," +
            " 'follow_date' date , foreign key(followingid) references tb_users(userid) ,foreign key(followerid) references tb_users(userid))";

    private final String cmd10 = "CREATE TABLE IF NOT EXISTS 'tb_shared_playlist'('userid' integer,'playlistid' integer ," +
            "foreign key(userid) references tb_users(userid) ,foreign key(playlistid) references tb_playlist(id))";

    private final String cmd11 = "CREATE TABLE IF NOT EXISTS 'tb_liked_playlist'('userid' integer,'playlistid' integer ," +
            " 'like_date' date ,foreign key(userid) references tb_users(userid) ,foreign key(playlistid) references tb_playlist(id))";

    private final String cmd12 = "CREATE TABLE IF NOT EXISTS 'tb_played_song'('userid' integer not null, 'musicid' integer not null,"+"" +
            "'played_date' date,foreign key(userid) references tb_users(userid),foreign key(musicid) references tb_music(id), primary key (played_date) )";

    private final String cmd13 = "CREATE TABLE IF NOT EXISTS 'tb_reported_song'('userid' integer not null, 'musicid' integer not null," +
            "'report_date' date,foreign key(userid) references tb_users(userid),foreign key(musicid) references tb_music(id))";

    private final String cmd14 = "CREATE TABLE IF NOT EXISTS 'tb_liked_music'('userid' integer not null, 'musicid' integer not null," +
            "'liked_date' date,foreign key(userid) references tb_users(userid),foreign key(musicid) references tb_music(id))";

    private final String cmd15 = "CREATE TABLE IF NOT EXISTS 'tb_liked_album'('userid' integer not null, 'albumid' integer not null," +
            "foreign key(userid) references tb_users(userid),foreign key(albumid) references tb_album(id))";

    private final String cmd16 = "CREATE TABLE IF NOT EXISTS 'tb_have_playlist'('playlistid' integer not null, 'userid' integer not null, 'musicid' integer not null," +
            " 'added_date' date , foreign key(userid) references tb_users(userid),foreign key(musicid) references tb_music(id), foreign key (playlistid) references tb_playlist(id))";

    private final String cmd17 = "CREATE TABLE IF NOT EXISTS 'tb_have_album'('albumid' integer not null, 'userid' integer not null, 'musicid' integer not null," +
            " 'added_date' date,foreign key(userid) references tb_users(userid),foreign key(musicid) references tb_music(id), foreign key (albumid) references tb_album(id))";


    public dbHelper(Context context,String tableName) {
        super(context, db_name, null, 1);
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
        db.execSQL(cmd10);
        db.execSQL(cmd11);
        db.execSQL(cmd12);
        db.execSQL(cmd13);
        db.execSQL(cmd14);
        db.execSQL(cmd15);
        db.execSQL(cmd16);
        db.execSQL(cmd17);
        Log.i("dbResult", "table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ tablename);
        db.execSQL("DROP TABLE IF EXISTS 'tb_listner'");
        db.execSQL("DROP TABLE IF EXISTS 'tb_artist'");
        Log.i("dbResult","table dropped");
        onCreate(db);
    }

    public void insert(ContentValues values,String tb_name){
        SQLiteDatabase db = getWritableDatabase();
        long insertid = db.insert(tb_name,null,values);
        if (insertid == -1){
            Log.i("insertion","fucked with id : "+insertid);
        }else {
            Log.i("insertion","inserted with id : "+insertid);
        }
        if (db.isOpen())db.close();
    }

    public String date_to_string(Date date){
        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String s = "";
        if (date==null) {
            s = format.format(d);
        }else {
            s = format.format(date);
        }
        return s;
    }

    public Date string_to_date(String s){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = format.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public List get_users_listner(String query,String username){
        SQLiteDatabase db = getReadableDatabase();
        List user_list_listner = new ArrayList();
        Cursor cursor = db.rawQuery("select * from 'tb_users' where userid<1999 and" +
                " (firstname like '%" + query + "%' or lastname like '%" + query + "%'" +
                " or username like '" + query + "%') and not username='" + username + "'",null);
        if (cursor.moveToFirst()){
            do {
                ContentValues temp_v = new ContentValues();
                temp_v.put(user.key_user_id,cursor.getInt(cursor.getColumnIndex(user.key_user_id)));
                temp_v.put(user.key_user_name,cursor.getString(cursor.getColumnIndex(user.key_user_name)));
                temp_v.put(user.key_user_first_name,cursor.getString(cursor.getColumnIndex(user.key_user_first_name)));
                temp_v.put(user.key_user_last_name,cursor.getString(cursor.getColumnIndex(user.key_user_last_name)));
                user_list_listner.add(temp_v);
            }while (cursor.moveToNext());
        }
        cursor.close();
        if (db.isOpen())db.close();
        return user_list_listner;
    }

    public List get_users_artist(String query,String username){
        SQLiteDatabase db = getReadableDatabase();
        List user_list_artist = new ArrayList();
        Cursor cursor = db.rawQuery("select * " +
                "from 'tb_users' as user , 'tb_artist' as artist " +
                "where user.userid between 1999 and 2999 and" +
                "(user.firstname like '%"+query+"%' or user.lastname like '%"+query+"%' or user.username like '%"+query+"%'" +
                "or artist.nickname like '%"+query+"%')" +
                "and not username = '"+username+"'",null);
        if (cursor.moveToFirst()){
            do {
                ContentValues temp_v = new ContentValues();
                temp_v.put(user.key_user_id,cursor.getInt(cursor.getColumnIndex(user.key_user_id)));
                temp_v.put(user.key_user_name,cursor.getString(cursor.getColumnIndex(user.key_user_name)));
                temp_v.put(user.key_user_first_name,cursor.getString(cursor.getColumnIndex(user.key_user_first_name)));
                temp_v.put(user.key_user_last_name,cursor.getString(cursor.getColumnIndex(user.key_user_last_name)));

                //todo check line below if it is ok or not!
                temp_v.put(artist.key_nickname,cursor.getString(cursor.getColumnIndex(artist.key_nickname)));
                temp_v.put(artist.key_genre,cursor.getString(cursor.getColumnIndex(artist.key_genre)));
                user_list_artist.add(temp_v);


            }while (cursor.moveToNext());
        }
        cursor.close();
        if (db.isOpen())db.close();
        return user_list_artist;
    }

    public List get_song(String query,String username){
        SQLiteDatabase db = getReadableDatabase();
        List song_list = new ArrayList();
        Cursor cursor = db.rawQuery("select * from 'tb_music' as music, 'tb_have_album' as have_album, 'tb_album' as album," +
                "'tb_artist' as artist"  +
                "where music.title like '%"+query+"%' and music.id = have_album.musicid and artist.id = have_album.albumid" +
                "and artist.userid = have_album.userid",null); // fill
        if (cursor.moveToFirst()){
            do {
                ContentValues temp_v = new ContentValues();
                temp_v.put(Music.key_music_id,cursor.getInt(cursor.getColumnIndex(Music.key_music_id)));
                temp_v.put(Music.key_music_duration,cursor.getString(cursor.getColumnIndex(Music.key_music_duration)));
                temp_v.put(Music.key_music_genre,cursor.getString(cursor.getColumnIndex(Music.key_music_genre)));
                temp_v.put(Music.key_music_title,cursor.getString(cursor.getColumnIndex(Music.key_music_title)));
                temp_v.put(artist.key_nickname,cursor.getString(cursor.getColumnIndex(artist.key_nickname)));
                temp_v.put(Album.key_title,cursor.getString(cursor.getColumnIndex(Album.key_title)));

                song_list.add(temp_v);
            }while (cursor.moveToNext());
        }
        cursor.close();
        if (db.isOpen())db.close();
        return song_list;
    }

    public List get_album(String query,String username){//todo check this logically
        SQLiteDatabase db = getReadableDatabase();
        List album_list = new ArrayList();
        Cursor cursor = db.rawQuery("select * from 'tb_music' as music, 'tb_have_album' as have_album, 'tb_album' as album," +
                "'tb_artist' as artist"  +
                "where album.title like '%"+query+"%' and  artist.id = have_album.albumid" +
                "and artist.userid = have_album.userid",null); // fill
        if (cursor.moveToFirst()){
            do {
                ContentValues temp_v = new ContentValues();
                temp_v.put(Album.key_id,cursor.getInt(cursor.getColumnIndex(Album.key_id)));
                temp_v.put(Album.key_title,cursor.getString(cursor.getColumnIndex(Album.key_title)));
                temp_v.put(Album.key_genre,cursor.getString(cursor.getColumnIndex(Album.key_genre)));
                temp_v.put(Album.key_publish_date,cursor.getString(cursor.getColumnIndex(Album.key_publish_date)));
                temp_v.put(artist.key_nickname,cursor.getString(cursor.getColumnIndex(artist.key_nickname)));
                album_list.add(temp_v);
            }while (cursor.moveToNext());
        }
        cursor.close();
        if (db.isOpen())db.close();
        return album_list;
    } // todo fill this

    public String follow_user(int following_id,int follower_id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from 'tb_follow' where followingid="
                + String.valueOf(following_id) + " and followerid="
                + String.valueOf(follower_id),null);

        if (cursor.getCount()>0){
            return "you are already following this user";
        }else {
            cursor.close();
            if (db.isOpen())db.close();
            db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("followingid",following_id);
            values.put("followerid",follower_id);
            values.put("follow_date",date_to_string(null));
            long insertid = db.insert("tb_follow",null,values);
            if (insertid == -1){
                return "Failed";
            }else return "Followed";
        }
    }

    public String unfollow_user(int following_id,int follower_id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from 'tb_follow' where followingid="
                + String.valueOf(following_id) + " and followerid="
                + String.valueOf(follower_id),null);

        if (cursor.getCount()==0){
            return "you are not following this user";
        }else {
            cursor.close();
            if (db.isOpen())db.close();
            db = getWritableDatabase();
            long deleteid = db.delete("tb_follow","followingid =" + String.valueOf(following_id) +
                    " and followerid =" + String.valueOf(follower_id),null);
            if (deleteid== -1){
                return "Failed";
            }else return "unFollowed";
        }
    }
}
