package com.a.ara;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

// private final String cmd2 = "CREATE TABLE IF NOT EXISTS 'TEST' ('name' TEXT , 'phone' INTEGER);";

    private final String cmd3 = "CREATE TABLE IF NOT EXISTS 'tb_listner'('userid' integer not null ,'premium_type' integer ," +
            " 'premium_trial' date , 'birth_date' date , foreign key(userid) references tb_users(userid))";

    private final String cmd4 = "CREATE TABLE IF NOT EXISTS 'tb_listner_credit'('userid' integer not null ," +
            " 'credit_number' text , 'expire_date' date , foreign key(userid) references tb_users(userid))";

    private final String cmd5 = "CREATE TABLE IF NOT EXISTS 'tb_artist'('userid' integer not null ," +
            " 'genre' text ,'career_start_date' date , 'valid' bool , 'nickname' text , foreign key(userid) references tb_users(userid))";

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
            "'added_date' date,foreign key(userid) references tb_users(userid),foreign key(musicid) references tb_music(id), foreign key (playlistid) references tb_playlist(id))";

    private final String cmd17 = "CREATE TABLE IF NOT EXISTS 'tb_have_album'('albumid' integer not null, 'userid' integer not null, 'musicid' integer not null," +
            "'added_date' date,foreign key(userid) references tb_users(userid),foreign key(musicid) references tb_music(id), foreign key (albumid) references tb_album(id))";


    public dbHelper(Context context,String tableName) {
        super(context, db_name, null, 1);
// this.tablename = tableName;
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
// db.execSQL("DROP TABLE IF EXISTS 'TEST'");
        Log.i("dbResult","table dropped");
        onCreate(db);
    }

    public void insert(ContentValues values){
        SQLiteDatabase db = getWritableDatabase();
        long insertid = db.insert(tablename,null,values);
        if (insertid == -1){
            Log.i("insertion","fucked with id : "+insertid);
        }else {
            Log.i("insertion","inserted with id : "+insertid);
        }
        if (db.isOpen())db.close();
    }
}