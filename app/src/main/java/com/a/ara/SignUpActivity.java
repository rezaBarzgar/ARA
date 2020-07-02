package com.a.ara;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    CheckBox Cbox ;
    EditText et_firstname,et_lastname,et_username,et_password,et_email;
    EditText et_answer , et_birthday , et_nickname;
    Spinner sp_region , sp_question;
    String[] region_items ,question_items ;
    String region;
    String question;
    Button btn_commit,btn_test_username;
    dbHelper dbh ;
    Intent result_intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setResult(RESULT_CANCELED,result_intent);
        initviews();

    }

    private void initviews() {

        Cbox = (CheckBox) findViewById(R.id.C_Box);
        et_firstname = (EditText) findViewById(R.id.et_firstname);
        et_lastname = (EditText) findViewById(R.id.et_lastname);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        et_email = (EditText) findViewById(R.id.et_email);
        et_answer = (EditText) findViewById(R.id.et_answer);
        et_birthday = (EditText) findViewById(R.id.et_birthday);
        et_nickname = (EditText) findViewById(R.id.et_nickname);

        dbh = new dbHelper(this,"sho");

        btn_commit = (Button) findViewById(R.id.btn_commit);
        btn_test_username = (Button) findViewById(R.id.btn_test_username);

        sp_region = (Spinner) findViewById(R.id.sp_region);
        sp_question = (Spinner) findViewById(R.id.sp_question);

        region_items = getResources().getStringArray(R.array.regions);
        question_items = getResources().getStringArray(R.array.questions);

        ArrayAdapter<String> rg_adapter = new ArrayAdapter<String>(SignUpActivity.this,
                android.R.layout.simple_spinner_item,region_items);
        rg_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_region.setAdapter(rg_adapter);
        sp_region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                region = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> q_adapter = new ArrayAdapter<String>(SignUpActivity.this,
                android.R.layout.simple_spinner_item,question_items);
        q_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_question.setAdapter(q_adapter);
        sp_question.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                question = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Cbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Cbox.isChecked()){
                    et_nickname.setEnabled(true);
                    et_birthday.setEnabled(false);
                    et_birthday.setHint("- not available -");
                    et_nickname.setHint("Nickname");
                }else {
                    et_nickname.setEnabled(false);
                    et_birthday.setEnabled(true);
                    et_birthday.setHint("Birth date");
                    et_nickname.setHint("- not available -");
                }
            }
        });

        btn_test_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_username.getText().toString().equals("")){
                    et_username.setError("Empty username");
                    et_username.requestFocus();
                    return;
                }
                if (!testUsername(et_username.getText().toString())){
                    et_username.setError("invalid username");
                    et_username.requestFocus();
                    return;
                }
                Cursor cursor = valid_username();
                if (cursor.getCount()>0){
                    Toast toast = Toast.makeText(SignUpActivity.this, "Already Taken !", Toast.LENGTH_SHORT);
                    toast.getView().setBackgroundColor(Color.rgb(200,0,0));
                    toast.show();
                    return;
                }else {
                    Toast toast = Toast.makeText(SignUpActivity.this, "Available", Toast.LENGTH_SHORT);
                    toast.getView().setBackgroundColor(Color.rgb(0,150,0));
                    toast.show();
                }
            }
        });

        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //          check the edit texts
                if (et_username.getText().toString().equals("")){
                    et_username.setError("Empty username");
                    et_username.requestFocus();
                    return;
                }else if (!testUsername(et_username.getText().toString())){
                    et_username.setError("invalid username");
                    et_username.requestFocus();
                    return;
                }else if (!isValidPassword(et_password.getText().toString())){
                    et_password.setError("invalid pattern for password");
                    et_password.requestFocus();
                    return;
                }else if (et_password.getText().toString().length() <8 |et_password.getText().toString().length()>50){
                    et_password.setError("password must be between 8 and 50 characters");
                    et_password.requestFocus();
                    return;
                }else if (!isValidEmailAddress(et_email.getText().toString())){
                    et_email.setError("invalid Email Address");
                    et_email.requestFocus();
                    return;
                }else if (!Cbox.isChecked()){
                    if (!isValidBdate(et_birthday.getText().toString())) {
                        et_birthday.setError("invalid birthday format");
                        et_birthday.requestFocus();
                        return;
                    }
                }

                //      check username availability
                Cursor cursor = valid_username();
                if (cursor.getCount()>0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setMessage("Already Taken UserName !");
                    builder.show();
                }else {

                    Bundle result_bundle = new Bundle();
                    ContentValues values = new ContentValues();
                    int max_userid = 0;
                    SQLiteDatabase db = dbh.getReadableDatabase();
                    Cursor cursor2 = db.rawQuery("SELECT MAX(userid) FROM 'tb_users'",null);

                    if (cursor2.getCount()>0) {
                        if (cursor2.moveToFirst()) {
                            max_userid = cursor2.getInt(cursor2.getColumnIndex("MAX(userid)"));
                        }
                        cursor2.close();
//                        Toast.makeText(SignUpActivity.this, "max userid : " + max_userid, Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(SignUpActivity.this, "Error userid", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    values.put(user.key_user_id,max_userid+1);
                    values.put(user.key_user_first_name,et_firstname.getText().toString());
                    values.put(user.key_user_last_name,et_lastname.getText().toString());
                    values.put(user.key_user_name,et_username.getText().toString());
                    values.put(user.key_user_password,et_password.getText().toString());
                    values.put(user.key_user_email,et_email.getText().toString());
                    values.put(user.key_user_region,region);
                    values.put(user.key_user_question,question);
                    values.put(user.key_user_answer,et_answer.getText().toString());
                    dbh.insert(values);
                    cursor.close();
                    result_bundle.putInt(user.key_user_id,values.getAsInteger(user.key_user_id));
                    result_bundle.putString(user.key_user_first_name,values.getAsString(user.key_user_first_name));
                    result_bundle.putString(user.key_user_last_name,values.getAsString(user.key_user_last_name));
                    result_bundle.putString(user.key_user_name,values.getAsString(user.key_user_name));
                    result_bundle.putString(user.key_user_password,values.getAsString(user.key_user_password));
                    result_bundle.putString(user.key_user_email,values.getAsString(user.key_user_email));
                    result_bundle.putString(user.key_user_region,values.getAsString(user.key_user_region));
                    result_bundle.putString(user.key_user_question,values.getAsString(user.key_user_question));
                    result_bundle.putString(user.key_user_answer,values.getAsString(user.key_user_answer));
                    result_intent.putExtra("result_sign_up",result_bundle);
                    setResult(RESULT_OK,result_intent);
                    finish();
                }

            }
        });

    }

    private Cursor valid_username() {
        String uname;
        uname = et_username.getText().toString();
        uname = "'" + uname + "'";
        SQLiteDatabase db = dbh.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT username FROM 'tb_users' WHERE username=" + uname ,null);
        return cursor;
    }

    public boolean testUsername(String username){
        return !(username.length() > 30 | username.length() < 4 );
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public boolean isValidPassword(String password){
        String regex = "[a-zA-Z0-9@$&!]+";
        return password.matches(regex);
    }

    public boolean isValidBdate(String Bdate){
        String regex = "^[0-3]?[0-9]/[0-3]?[0-9]/(?:[0-9]{2})?[0-9]{2}$";
        return Bdate.matches(regex);
    }
    public boolean isArtist(int usernameid){
        int temp = usernameid;
        while(temp>10) temp = temp /10;
        if (temp == 2) return true;
        else return false;
    }

    // select * from users where username=(entered username) --> if null then insert it
    // else show a alert dialog
}
