package com.a.ara;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddSongToExistingAlbum extends AppCompatActivity {

    EditText title,genre,duration;
    ListView ex_albums;
    Button submit;
    int userid;
    private dbHelper dbh;
    Bundle income ;
    List list = new ArrayList();
    ArrayAdapter adapter;
    public static String st_title = "";
    public static String st_genre = "";
    public static int st_duration = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song_to_existing_album);

        dbh = new dbHelper(this,"sho");

        income= getIntent().getExtras().getBundle("add_to_existing_album");
        userid = income.getInt(user.key_user_id);

        title = (EditText) findViewById(R.id.et_add_album_title_2);
        genre = (EditText) findViewById(R.id.et_add_album_genre_2);
        duration = (EditText) findViewById(R.id.et_add_album_duration);
        submit = (Button) findViewById(R.id.btn_submit_existing_album);
        ex_albums = (ListView) findViewById(R.id.list_existing_albums);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.getText().toString().isEmpty()){
                    title.setError("empty title");
                    title.requestFocus();
                }else if (genre.getText().toString().isEmpty()){
                    genre.setError("empty genre");
                    genre.requestFocus();
                }else if (duration.getText().toString().isEmpty()){
                    duration.setError("empty duration");
                    duration.requestFocus();
                }

                st_title = title.getText().toString();
                st_genre = genre.getText().toString();
                st_duration= Integer.valueOf(duration.getText().toString());

                list = dbh.get_artist_albums(userid);
                if (list == null) list = new ArrayList();
                adapter = new AraAdapter(AddSongToExistingAlbum.this,list,userid,"ex_album");
                ex_albums.setAdapter(adapter);
            }
        });

    }
}
