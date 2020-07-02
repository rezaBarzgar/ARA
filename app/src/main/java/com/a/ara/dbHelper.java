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
            " 'genre' text ,'career_start_date' date , 'valid' bool , 'nickame' text , foreign key(userid) references tb_users(userid))";

    private final String cmd6 = "CREATE TABLE IF NOT EXISTS 'tb_playlist'('id' INTEGER PRIMARY KEY NOT NULL ," +
            " 'ownerid' integer not null , 'title' text , 'create_date' date , 'number_of_musics' integer , " +
            "foreign key(ownerid) references tb_users(userid))";

    private final String cmd7 = "CREATE TABLE IF NOT EXISTS 'tb_music'()";
    private final String cmd8 = "CREATE TABLE IF NOT EXISTS 'tb_album'()";
    private final String cmd9 = "CREATE TABLE IF NOT EXISTS 'tb_follow'()";
    private final String cmd10 = "CREATE TABLE IF NOT EXISTS 'tb_shared_playlist'()";
    private final String cmd11 = "CREATE TABLE IF NOT EXISTS 'tb_liked_playlist'()";
    private final String cmd12 = "CREATE TABLE IF NOT EXISTS 'tb_played_song'()";
    private final String cmd13 = "CREATE TABLE IF NOT EXISTS 'tb_reported_song'()";
    private final String cmd14 = "CREATE TABLE IF NOT EXISTS 'tb_liked_music'()";
    private final String cmd15 = "CREATE TABLE IF NOT EXISTS 'tb_liked_album'()";
    private final String cmd16 = "CREATE TABLE IF NOT EXISTS 'tb_have_playlist'()";
    private final String cmd17 = "CREATE TABLE IF NOT EXISTS 'tb_have_album'()";


    public dbHelper(Context context,String tableName) {
        super(context, db_name, null, 1);
// this.tablename = tableName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(cmd);
// db.execSQL(cmd2);
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