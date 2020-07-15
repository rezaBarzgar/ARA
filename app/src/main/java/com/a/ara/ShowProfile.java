package com.a.ara;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ShowProfile extends AppCompatActivity{

    int userid;
    Boolean is_artist;
    private dbHelper dbh;
    int followers_count = 0,followings_count = 0;
    ListView listView;
    List list = new ArrayList();
    ArrayAdapter adapter;
    Boolean own;
    TextView show_profile_info;
    Bundle income_info ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbh = new dbHelper(this,"sho");

        income_info = getIntent().getExtras().getBundle("carry_info");
        userid = income_info.getInt(user.key_user_id);
        is_artist = MainActivity.isArtist(userid);

        if (income_info.getString("type").equals("own")){
            own = true;
        }else own = false;

        followings_count = dbh.show_following_count(userid);
        followers_count = dbh.show_followers_count(userid);

        if (!is_artist){
            setContentView(R.layout.activity_show_listner_profile);
            show_profile_info = (TextView) findViewById(R.id.tv_show_listner_profile_info);

            listView = (ListView) findViewById(R.id.listner_profile_search_list);

        }else {
            setContentView(R.layout.activity_show_artist_profile);
            show_profile_info = (TextView) findViewById(R.id.tv_show_artist_profile_info);

            listView = (ListView) findViewById(R.id.artist_profile_search_list);

            // add 5 latest song here

        }

        if (!is_artist && !own){
            // add last played song here
        }

        refresh_bio(income_info,show_profile_info);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!is_artist && own) {
            menu.add("show followers").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    list = dbh.get_follow_items("followers", userid);
                    refresh_display(list, userid, "follower_items");
                    return false;
                }
            });
            menu.add("show followings").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    list = dbh.get_follow_items("followings", userid);
                    refresh_display(list, userid, "following_items");
                    return false;
                }
            });
            menu.add("show playlists");
            menu.add("upgrade to premium");
        }else if (is_artist && own){
            menu.add("show followers").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    list = dbh.get_follow_items("followers", userid);
                    refresh_display(list, userid, "follower_items");
                    return false;
                }
            });
            menu.add("show followings").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    list = dbh.get_follow_items("followings", userid);
                    refresh_display(list, userid, "following_items");
                    return false;
                }
            });
            menu.add("add song to existing album");
            menu.add("add new album");
            menu.add("delete song from album");
            menu.add("delete whole album");

        }else if (!is_artist && !own){

        }else if (is_artist && !own){

        }
        menu.add("Refresh page").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                followings_count = dbh.show_following_count(userid);
                followers_count = dbh.show_followers_count(userid);
                refresh_bio(income_info,show_profile_info);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void refresh_display(List list,int userid,String tag){
        if (list == null) list = new ArrayList();
        adapter = new AraAdapter(this,list,userid,tag);
        listView.setAdapter(adapter);
    }

    private void refresh_bio(Bundle income_info,TextView show_profile_info){
        show_profile_info.setText(
                income_info.getString(user.key_user_name) + "\n"
                        + income_info.getString(user.key_user_first_name) + " "
                        + income_info.getString(user.key_user_last_name) + "\n"
                        + income_info.getString(user.key_user_email) + "\n"
                        + income_info.getString(user.key_user_region) + "\n"
                        + "followers : " + followers_count + "\n"
                        + "following : " + followings_count
        );
    }

}
