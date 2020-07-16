package com.a.ara;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNewAlbum extends AppCompatActivity {

    EditText title,genre;
    Button submit;
    int userid;
    private dbHelper dbh;
    Bundle income ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_album);

        dbh = new dbHelper(this,"sho");

        income= getIntent().getExtras().getBundle("add_new_album");
        userid = income.getInt(user.key_user_id);

        title = (EditText) findViewById(R.id.et_add_album_title);
        genre = (EditText) findViewById(R.id.et_add_album_genre);
        submit = (Button) findViewById(R.id.btn_submit_new_album);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.getText().toString().isEmpty()){
                    title.setError("empty title");
                    title.requestFocus();
                }else if (genre.getText().toString().isEmpty()){
                    genre.setError("empty genre");
                    genre.requestFocus();
                }

                Toast.makeText(AddNewAlbum.this, dbh.add_new_album(
                        title.getText().toString(),genre.getText().toString()
                ), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
