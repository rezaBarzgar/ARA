package com.a.ara;

import android.support.annotation.StringDef;
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
    Button btn_commit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Cbox = (CheckBox) findViewById(R.id.C_Box);
        et_firstname = (EditText) findViewById(R.id.et_firstname);
        et_lastname = (EditText) findViewById(R.id.et_lastname);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        et_email = (EditText) findViewById(R.id.et_email);
        et_answer = (EditText) findViewById(R.id.et_answer);
        et_birthday = (EditText) findViewById(R.id.et_birthday);
        et_nickname = (EditText) findViewById(R.id.et_nickname);

        sp_region = (Spinner) findViewById(R.id.sp_region);
        sp_question = (Spinner) findViewById(R.id.sp_question);

        btn_commit = (Button) findViewById(R.id.btn_commit);

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

//        et_username.setError("empty username");
//        et_username.requestFocus();

        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!testUsername(et_username.getText().toString())){
                    et_username.setError("empty username");
                    et_username.requestFocus();
                }
                if (!isValidEmailAddress(et_email.getText().toString())){
                    et_email.setError("invalid Email Address");
                    et_email.requestFocus();
                }
                if (!isValidPassword(et_password.getText().toString())){
                    et_password.setError("invalid password");
                    et_password.requestFocus();
                }
                if (!isValidBdate(et_birthday.getText().toString())){
                    et_birthday.setError("invalid birthday format");
                    et_birthday.requestFocus();
                }



            }


        });

    }
    public boolean testUsername(String username){
        return !(username.length() > 50 | username.length() < 4 );

    }
    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
    public boolean isValidPassword(String password){
        String regex = "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$";
        return password.matches(regex);
    }
    public boolean isValidBdate(String Bdate){
        String regex = "^[0-3]?[0-9]/[0-3]?[0-9]/(?:[0-9]{2})?[0-9]{2}$";
        return Bdate.matches(regex);
    }

    // select * from users where username=(entered username) --> if null then insert it
    // else show a alert dialog
}
