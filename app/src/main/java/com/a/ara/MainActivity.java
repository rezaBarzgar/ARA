package com.a.ara;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    dbHelper dbh;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView login_result = (TextView) findViewById(R.id.login_result);
        Button sign_up = (Button) findViewById(R.id.btn_sign_up);
        Button login = (Button) findViewById(R.id.btn_login);

        dbh = new dbHelper(this,"sho");
        SQLiteDatabase db = dbh.getWritableDatabase();
        Log.i("dbResult" , "database opened");
        dbh.onUpgrade(db,db.getVersion(),db.getVersion());
        int v = db.getVersion();
        Toast.makeText(this, String.valueOf(v), Toast.LENGTH_SHORT).show();
        if (db != null && db.isOpen()){
            db.close();
            Log.i("dbResult" , "database closed");
        }

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SignUpActivity.class));
//                addUser();
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("add users").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                addUser();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void addUser() {

        InputStream inputStream = getResources().openRawResource(R.raw.users);
        StringBuilder sb = new StringBuilder();
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        try {
            while (bis.available() != 0){
                sb.append((char) bis.read());
            }
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Fucking Exception", Toast.LENGTH_SHORT).show();
        }

        if (sb.equals(null))return;
        String jsonString = sb.toString();
        ContentValues values = new ContentValues();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i <jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                values.put(user.key_user_id,jsonObject.getInt(user.key_user_id));
                values.put(user.key_user_first_name,jsonObject.getString(user.key_user_first_name));
                values.put(user.key_user_last_name,jsonObject.getString(user.key_user_last_name));
                values.put(user.key_user_name,jsonObject.getString(user.key_user_name));
                values.put(user.key_user_password,jsonObject.getString(user.key_user_password));
                values.put(user.key_user_email,jsonObject.getString(user.key_user_email));
                values.put(user.key_user_region,jsonObject.getString(user.key_user_region));
                values.put(user.key_user_question,jsonObject.getString(user.key_user_question));
                values.put(user.key_user_answer,jsonObject.getString(user.key_user_answer));
                dbh.insert(values);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
