package com.a.ara;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    dbHelper dbh;
    EditText et_username,et_password;
    SharedPreferences preferences;
    String current_user = "";
    String search_for;
    String[] search_for_items ;
    ListView listView;
    List list = new ArrayList();
    ArrayAdapter adapter;

    public static final int sign_up_req_code = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbh = new dbHelper(this,"sho");

        preferences = getSharedPreferences("locals",MODE_PRIVATE);
        current_user = preferences.getString("username","***");
        if (current_user.equals("***")){
            setContentView(R.layout.activity_main);
            initviews();
            Toast.makeText(this, "Login First", Toast.LENGTH_SHORT).show();
        }else {
            setContentView(R.layout.main_page);
            init_main_page_views();
            Toast.makeText(this, "logged in as : " + current_user, Toast.LENGTH_SHORT).show();
        }

    }

    private void init_main_page_views() {
        TextView show_info = (TextView) findViewById(R.id.tv_show_info);
        show_info.setText(preferences.getString(user.key_user_name,"NOT FOUND") +
        "\t" + preferences.getString(user.key_user_first_name,"NOT FOUND") +
        " " + preferences.getString(user.key_user_last_name,"NOT FOUND") +
        "\n" + preferences.getString(user.key_user_email,"NOT FOUND") +
        "\t" + preferences.getString(user.key_user_region,"NOT FOUND"));

        Spinner sp_search_for = (Spinner) findViewById(R.id.sp_search_for);
        search_for_items = getResources().getStringArray(R.array.search_options);

        ArrayAdapter<String> s_adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item,search_for_items);
        s_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_search_for.setAdapter(s_adapter);
        sp_search_for.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                search_for = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listView = (ListView) findViewById(R.id.search_list);

        SearchView searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (search_for.equals("users")){
                    list = dbh.get_users_listner(newText,preferences.getString(user.key_user_name,"NOT FOUND"));
                    refresh_display(list,preferences.getInt(user.key_user_id,0),search_for);
                }else if (search_for.equals("artists")){
                    list = dbh.get_users_artist(newText,preferences.getString(user.key_user_name,"NOT FOUND"));
                    if (list.size()>0){
                        Toast.makeText(MainActivity.this, "yes", Toast.LENGTH_SHORT).show();
                    }
                    refresh_display(list,preferences.getInt(user.key_user_id,0),search_for);
                }else if (search_for.equals("songs")){
                    list = dbh.get_song(newText,preferences.getString(user.key_user_name,"NOT FOUND"));
                    refresh_display(list,preferences.getInt(user.key_user_id,0),search_for);
                }else if (search_for.equals("albums")){
                    list = dbh.get_album(newText,preferences.getString(user.key_user_name,"NOT FOUND"));
                    refresh_display(list,preferences.getInt(user.key_user_id,0),search_for);
                }
//                else {
//                    list = null;
//                    refresh_display(list,preferences.getInt(user.key_user_id,0));
//                }
                return false;
            }
        });
    }

    private void initviews() {

        final TextView login_result = (TextView) findViewById(R.id.login_result);
        Button sign_up = (Button) findViewById(R.id.btn_sign_up);
        Button login = (Button) findViewById(R.id.btn_login);
        et_username = (EditText) findViewById(R.id.et_username_2);
        et_password = (EditText) findViewById(R.id.et_password_2);

        login_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                login_result.setTextColor(Color.rgb(random.nextInt(256),
                        random.nextInt(256),random.nextInt(256)));
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
                startActivityForResult(intent,sign_up_req_code);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void refresh_display(List list,int userid,String tag){
        if (list == null) list = new ArrayList();
        adapter = new AraAdapter(this,list,userid,tag);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == sign_up_req_code){
            if (resultCode == RESULT_OK){
                Bundle result_bnd = data.getBundleExtra("result_sign_up");

                preferences.edit().putString(user.key_user_name, result_bnd.getString(user.key_user_name)).apply();
                preferences.edit().putInt(user.key_user_id, result_bnd.getInt(user.key_user_id)).apply();
                preferences.edit().putString(user.key_user_first_name, result_bnd.getString(user.key_user_first_name)).apply();
                preferences.edit().putString(user.key_user_last_name, result_bnd.getString(user.key_user_last_name)).apply();
                preferences.edit().putString(user.key_user_email, result_bnd.getString(user.key_user_email)).apply();
                preferences.edit().putString(user.key_user_region, result_bnd.getString(user.key_user_region)).apply();

                setContentView(R.layout.main_page);
                init_main_page_views();

            }else if (resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Canseled by user", Toast.LENGTH_SHORT).show();
            }
        }
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
            Toast.makeText(this, " Exception", Toast.LENGTH_SHORT).show();
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
                dbh.insert(values,"tb_users");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loginUser() {
        String uname , pass, pass2 = "";
        SQLiteDatabase db = dbh.getReadableDatabase();

        if (et_username.getText().toString().trim().isEmpty()){
            et_username.setError("Empty username");
            et_username.requestFocus();
            return;
        }else if (et_password.getText().toString().trim().isEmpty()){
            et_password.setError("Empty password");
            et_password.requestFocus();
            return;
        }

        uname = et_username.getText().toString();
        pass = et_password.getText().toString();
        uname = "'" + uname + "'";

        Cursor cursor = db.rawQuery("SELECT * FROM 'tb_users' WHERE username=" + uname ,null);
        if (cursor.getCount() == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Username Not Found !");
            builder.show();
            return;
        }

        if (cursor.moveToFirst()){
            pass2 = cursor.getString(cursor.getColumnIndex(user.key_user_password));
            if (pass2.equals(pass)){
                Toast.makeText(this, "Logged in ", Toast.LENGTH_SHORT).show();
                et_password.getText().clear();
            }else {
                Toast.makeText(this, "Wrong Pass", Toast.LENGTH_SHORT).show();
                return;
            }
            preferences.edit().putBoolean("isArtist",isArtist(cursor
                    .getInt(cursor.getColumnIndex(user.key_user_id)))).apply();
            preferences.edit().putString(user.key_user_name,
                    cursor.getString(cursor.getColumnIndex(user.key_user_name))).apply();
            preferences.edit().putInt(user.key_user_id,
                    cursor.getInt(cursor.getColumnIndex(user.key_user_id))).apply();
            preferences.edit().putString(user.key_user_first_name,
                    cursor.getString(cursor.getColumnIndex(user.key_user_first_name))).apply();
            preferences.edit().putString(user.key_user_last_name,
                    cursor.getString(cursor.getColumnIndex(user.key_user_last_name))).apply();
            preferences.edit().putString(user.key_user_email,
                    cursor.getString(cursor.getColumnIndex(user.key_user_email))).apply();
            preferences.edit().putString(user.key_user_region,
                    cursor.getString(cursor.getColumnIndex(user.key_user_region))).apply();
        }
        cursor.close();
        if (db.isOpen())db.close();
        setContentView(R.layout.main_page);
        init_main_page_views();
    }

    private void clear_database(){
        SQLiteDatabase db = dbh.getWritableDatabase();
        Log.i("dbResult" , "database opened");
        dbh.onUpgrade(db,db.getVersion(),db.getVersion());
        int v = db.getVersion();
//        Toast.makeText(this, String.valueOf(v), Toast.LENGTH_SHORT).show();
        if (db != null && db.isOpen()){
            db.close();
            Log.i("dbResult" , "database closed");
        }
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
        menu.add("log out").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (preferences.contains("username")) {
                    preferences.edit().clear().apply();
                    Toast.makeText(MainActivity.this, "logged out", Toast.LENGTH_SHORT).show();
                    setContentView(R.layout.activity_main);
                    initviews();
                }else {
                    Toast.makeText(MainActivity.this, "Not logged in", Toast.LENGTH_SHORT).show();
                }
//                preferences.edit().remove("username").apply();
                return false;
            }
        });
        menu.add("clear database").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                clear_database();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public boolean isArtist(int usernameid){
        int temp = usernameid;
        while(temp>10) temp = temp /10;
        if (temp == 2) return true;
        else return false;
    }

}