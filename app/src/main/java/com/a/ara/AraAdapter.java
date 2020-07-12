package com.a.ara;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

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
            }else if (tag.equals("follow_items")){
                s = values.getAsString(user.key_user_name);
                s += " : " + values.getAsString(user.key_user_first_name);
                s += " " + values.getAsString(user.key_user_last_name);
            }
            info.setText(s);

            final PopupMenu popupMenu = new PopupMenu(plus_icon.getContext(),plus_icon);
            if (tag.equals("users")) {
                popupMenu.inflate(R.menu.listner_popup_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.popup_menu_follow) {
                            Toast.makeText(activity, dbh.follow_user(userid, values.getAsInteger(user.key_user_id))
                                    , Toast.LENGTH_SHORT).show();
                        } else if (id == R.id.popup_menu_information) {
                            show_information(values);
                        } else if (id == R.id.popup_menu_unfollow) {
                            Toast.makeText(activity, dbh.unfollow_user(userid, values.getAsInteger(user.key_user_id))
                                    , Toast.LENGTH_SHORT).show();
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
                        //                                              fill if's here
                        return false;
                    }
                });
            } else if (tag.equals("songs")){
                popupMenu.inflate(R.menu.song_popup_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        //                                              fill if's here
                        return false;
                    }
                });
            } else if (tag.equals("albums")){
                popupMenu.inflate(R.menu.album_popup_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        //                                              fill if's here
                        return false;
                    }
                });
            }else if (tag.equals("follow_items")){

            }
            plus_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupMenu.show();
                }
            });
        }

    }

    private void show_information(ContentValues values) {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.information);
        changeDialogSize(dialog);
        TextView tv_information = (TextView) dialog.findViewById(R.id.tv_information);
        tv_information.setText(values.getAsString(user.key_user_name));
        dialog.show();
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

}
