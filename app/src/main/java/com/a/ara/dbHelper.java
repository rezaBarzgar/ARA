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
            " 'answer' TEXT )";


    public dbHelper(Context context,String tableName) {
        super(context, db_name, null, 1);
//        this.tablename = tableName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(cmd);
        Log.i("dbResult", "table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ tablename);
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
