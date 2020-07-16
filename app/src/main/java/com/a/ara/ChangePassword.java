package com.a.ara;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChangePassword extends AppCompatActivity {

    int userid;
    Bundle income ;
    TextView question;
    EditText answer,new_password;
    Button submit;
    String saved_question;
    private dbHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        dbh = new dbHelper(this,"sho");

        income= getIntent().getExtras().getBundle("change_password");
        userid = income.getInt(user.key_user_id);

        question = (TextView) findViewById(R.id.tv_question);
        answer = (EditText) findViewById(R.id.et_change_password_answer);
        new_password = (EditText) findViewById(R.id.et_change_password_password);
        submit = (Button) findViewById(R.id.btn_submit_change_password);

        saved_question = dbh.saved_question(userid);
        if (saved_question.equals("1")){
            saved_question = "favorite color";
        } else if (saved_question.equals("2")){
            saved_question = "first job";
        } else if (saved_question.equals("3")){
            saved_question = "dream car";
        } else if (saved_question.equals("4")){
            saved_question = "crush";
        }

        question.setText(saved_question);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answer.getText().toString().isEmpty()){
                    answer.setError("empty answer");
                    answer.requestFocus();
                }else if (new_password.getText().toString().isEmpty()){
                    new_password.setError("empty answer");
                    new_password.requestFocus();
                }

                Toast.makeText(ChangePassword.this, dbh.change_pass(
                        answer.getText().toString(),new_password.getText().toString(),userid
                ), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
