package com.a.ara;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AraAdapter extends ArrayAdapter{

    private Activity activity;
    private List list;
    private dbHelper dbh;
    private int userid;
    private String tag;

    public AraAdapter(Activity activity,List list,int userid,String tag){
        super(activity,android.R.layout.simple_list_item_1,list);
        this.activity = activity;
        this.list = list;
        this.userid = userid;
        this.tag = tag;
        this.dbh = new dbHelper(activity,"sho");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(activity)
                    .inflate(R.layout.ara_list_item,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.fill(position);
        return convertView;
    }

    public class ViewHolder{
        ImageView plus_icon;
        TextView info;

        public ViewHolder(View view){
            plus_icon = (ImageView) view.findViewById(R.id.ic_plus);
            info = (TextView) view.findViewById(R.id.tv_info);

        }

        public void fill(int position){
            final ContentValues values = (ContentValues) list.get(position);
            String s = "";
            if (tag.equals("users")) {
                s = values.getAsString(user.key_user_name);
                s += " : " + values.getAsString(user.key_user_first_name);
                s += " " + values.getAsString(user.key_user_last_name);
            }else if (tag.equals("artists")){
                s = values.getAsString(user.key_user_name);
                s += " : " + values.getAsString(user.key_user_first_name);
                s += " " + values.getAsString(user.key_user_last_name);
            }else if (tag.equals("songs")){
                s = values.getAsString(Music.key_music_title);
                s += " : " + values.getAsString(Music.key_music_genre);
                s += " _ " + values.getAsString(artist.key_nickname);
                s += " _ " + values.getAsString(Album.key_title);
            }else if (tag.equals("albums")){
                s = values.getAsString(Album.key_title);
                s += " : " + values.getAsString(artist.key_nickname);
                s += " _ " + values.getAsString(Album.key_genre);
            }else if (tag.equals("playlists")){
                s = values.getAsString(playlist.key_title);
                s += " : " + values.getAsString(user.key_user_name);
            }else if (tag.equals("follower_items")){
                s = values.getAsString(user.key_user_name);
                s += " : " + values.getAsString(user.key_user_first_name);
                s += " " + values.getAsString(user.key_user_last_name);
            }else if (tag.equals("following_items")){
                s = values.getAsString(user.key_user_name);
                s += " : " + values.getAsString(user.key_user_first_name);
                s += " " + values.getAsString(user.key_user_last_name);
            }else if (tag.equals("album_items")){
                s = values.getAsString(Music.key_music_title);
                s += " : " + values.getAsString(Music.key_music_genre);
                s += " _ " + values.getAsString(artist.key_nickname);
            }
            info.setText(s);

            final PopupMenu popupMenu = new PopupMenu(plus_icon.getContext(),plus_icon);
            if (tag.equals("users")) {
                popupMenu.inflate(R.menu.listner_popup_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.popup_menu_follow_listner) {
                            Toast.makeText(activity, dbh.follow_user(userid, values.getAsInteger(user.key_user_id))
                                    , Toast.LENGTH_SHORT).show();
                        } else if (id == R.id.popup_menu_unfollow_listner) {
                            Toast.makeText(activity, dbh.unfollow_user(userid, values.getAsInteger(user.key_user_id))
                                    , Toast.LENGTH_SHORT).show();
                        } else if (id == R.id.popup_menu_profile_listner) {
                            Intent show_pro = show_others_profile(values);
                            activity.startActivity(show_pro);
                        }
                        return false;
                    }
                });
            }else if (tag.equals("artists")){
                popupMenu.inflate(R.menu.artist_popup_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.popup_menu_follow_artist){
                            Toast.makeText(activity, dbh.follow_user(userid, values.getAsInteger(user.key_user_id))
                                    , Toast.LENGTH_SHORT).show();
                        }else if (id == R.id.popup_menu_unfollow_artist){
                            Toast.makeText(activity, dbh.unfollow_user(userid, values.getAsInteger(user.key_user_id))
                                    , Toast.LENGTH_SHORT).show();
                        }else if (id == R.id.popup_menu_profile_artist){
                            Intent show_pro = show_others_profile(values);
                            activity.startActivity(show_pro);
                        }
                        return false;
                    }
                });
            } else if (tag.equals("songs")){
                popupMenu.inflate(R.menu.song_popup_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.popup_menu_play_song){
                            Toast.makeText(activity, dbh.play_song(userid,values.getAsInteger(Music.key_music_id))
                                    , Toast.LENGTH_SHORT).show();
                        }else if (id == R.id.popup_menu_report_song){
                            Toast.makeText(activity, dbh.report_song(userid,values.getAsInteger(Music.key_music_id))
                                    , Toast.LENGTH_SHORT).show();
                        }else if (id == R.id.popup_menu_go_to_artist){
                            ContentValues values1 = dbh.get_artist_from_musicid(values.getAsInteger(Music.key_music_id),"song");
                            if (values1.getAsInteger(artist.key_user_id) == null){
                                Toast.makeText(activity, "No such a user !", Toast.LENGTH_SHORT).show();
                            }else {
                                Intent show_pro = show_others_profile(values1);
                                activity.startActivity(show_pro);
                            }
                        }else if (id == R.id.popup_menu_go_to_album){
                            // add new func in dbHelper : find album from songid
                            // tomorrow
                        }else if (id == R.id.popup_menu_like_song){
                            Toast.makeText(activity, dbh.like_song(userid,values.getAsInteger(Music.key_music_id))
                                    , Toast.LENGTH_SHORT).show();
                        }else if (id == R.id.popup_menu_unlike_song){
                            Toast.makeText(activity, dbh.unlike_song(userid,values.getAsInteger(Music.key_music_id))
                                    , Toast.LENGTH_SHORT).show();
                        }else if (id == R.id.popup_menu_add_to_playlist){

                        }
                        return false;
                    }
                });
            } else if (tag.equals("albums")){
                popupMenu.inflate(R.menu.album_popup_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.popup_menu_play_album){
                            // should be removed
                        }else if (id == R.id.popup_menu_go_to_artist){
                            ContentValues values1 = dbh.get_artist_from_musicid(values.getAsInteger(Album.key_id),"album");
                            if (values1.getAsInteger(artist.key_user_id) == null) {
                                Toast.makeText(activity, "No such a user !", Toast.LENGTH_SHORT).show();
                            }else {
                                Intent show_pro = show_others_profile(values1);
                                activity.startActivity(show_pro);
                            }
                        }else if (id == R.id.popup_menu_like_album){
                            Toast.makeText(activity, dbh.like_album(userid,values.getAsInteger(Album.key_id))
                                    , Toast.LENGTH_SHORT).show();
                        }else if (id == R.id.popup_menu_unlike_album){
                            Toast.makeText(activity, dbh.unlike_album(userid,values.getAsInteger(Album.key_id))
                                    , Toast.LENGTH_SHORT).show();
                        }else if (id == R.id.popup_menu_album_songs){
                            show_album_dialog(values.getAsInteger(Album.key_id));
                        }
                        return false;
                    }
                });
            }else if (tag.equals("follower_items")){
                popupMenu.inflate(R.menu.follower_popup_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.follower_popup_menu_show_profile){
                            Intent show_pro = show_others_profile(values);
                            activity.startActivity(show_pro);
                        }else if (id == R.id.follower_popup_menu_remove_from_followers){
                            String s = dbh.unfollow_user(values.getAsInteger(user.key_user_id), userid);
                            if (s.equals("unFollowed")){
                                Toast.makeText(activity, "user is not following you any more",
                                        Toast.LENGTH_SHORT).show();
                            }else Toast.makeText(activity, "this user is not following you",
                                    Toast.LENGTH_SHORT).show();
                        }else if (id == R.id.follower_popup_menu_follow){
                            Toast.makeText(activity, dbh.follow_user(userid, values.getAsInteger(user.key_user_id))
                                    , Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                });
            }else if (tag.equals("following_items")){
                popupMenu.inflate(R.menu.following_popup_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.following_popup_menu_show_profile){
                            Intent show_pro = show_others_profile(values);
                            activity.startActivity(show_pro);
                        }else if (id == R.id.following_popup_menu_unfollow){
                            Toast.makeText(activity, dbh.unfollow_user(userid, values.getAsInteger(user.key_user_id))
                                    , Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                });
            }else if (tag.equals("album_items")){
                popupMenu.inflate(R.menu.album_item_popup_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.popup_menu_play_album_song){
                            Toast.makeText(activity, dbh.play_song(userid,values.getAsInteger(Music.key_music_id))
                                    , Toast.LENGTH_SHORT).show();
                        }else if (id == R.id.popup_menu_report_album_song){
                            Toast.makeText(activity, dbh.report_song(userid,values.getAsInteger(Music.key_music_id))
                                    , Toast.LENGTH_SHORT).show();
                        }else if (id == R.id.popup_menu_like_album_song){
                            Toast.makeText(activity, dbh.like_song(userid,values.getAsInteger(Music.key_music_id))
                                    , Toast.LENGTH_SHORT).show();
                        }else if (id == R.id.popup_menu_unlike_album_song){
                            Toast.makeText(activity, dbh.unlike_song(userid,values.getAsInteger(Music.key_music_id))
                                    , Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                });
            }
            plus_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupMenu.show();
                }
            });
        }

    }

    private Intent show_others_profile(ContentValues values) {
        Intent show_pro  = new Intent(activity, ShowProfile.class);
        Bundle carry_info = new Bundle();
        carry_info.putInt(user.key_user_id,
                values.getAsInteger(user.key_user_id));
        carry_info.putString(user.key_user_name,
                values.getAsString(user.key_user_name));
        carry_info.putString(user.key_user_first_name,
                values.getAsString(user.key_user_first_name));
        carry_info.putString(user.key_user_last_name,
                values.getAsString(user.key_user_last_name));
        carry_info.putString(user.key_user_email,
                values.getAsString(user.key_user_email));
        carry_info.putString(user.key_user_region,
                values.getAsString(user.key_user_region));
        carry_info.putString("type","other");
        show_pro.putExtra("carry_info",carry_info);
        return show_pro;
    }

    private Point getScreenSize(Activity activity){
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return size;
    }

    private void changeDialogSize(Dialog dialog){
        Point scrSize = getScreenSize(activity);
        if (dialog.getWindow() != null){
            dialog.getWindow().setLayout((int)(0.8 * scrSize.x),ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void show_album_dialog(int albumid){
        List list = dbh.get_songs_of_album(albumid);
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.album_list);
        ListView album_list = (ListView) dialog.findViewById(R.id.album_items_list);
        if (list == null) list = new ArrayList();
        ArrayAdapter adapter = new AraAdapter(activity,list,userid,"album_items");
        album_list.setAdapter(adapter);
        changeDialogSize(dialog);
        dialog.show();
    }

}
