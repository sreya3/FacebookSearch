package com.example.sreyarajendran.csci571hw09;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sreya Rajendran on 4/26/2017.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Activity context;
    ArrayList<String> albums;

    public ExpandableListAdapter(Activity context, ArrayList<String> albums) {
        this.context = context;
        this.albums = albums;
    }

    public String getChild(int groupPosition, int childPosition){
        String s = getGroup(groupPosition);
        JSONObject album;
        JSONArray pics;
        String id = null;
        String pic = null;
        try {
            album = new JSONObject(s);
            pics = album.getJSONObject("photos").getJSONArray("data");
            id = pics.getJSONObject(childPosition).getString("id");
            pic = String.format("https://graph.facebook.com/%1$s/picture?type=normal", id);
            Log.d("picture", pic);
        }catch(Exception e){
            Log.e("ExpandableListAdapter", e.getMessage());
        }
        return pic;
    }

    public String getGroup(int groupPosition){
        return albums.get(groupPosition);
    }

    public long getChildId(int groupPosition, int childPosition){
        return childPosition;
    }



    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.expandable_group_content, null);
        }
        ImageView pic1 = (ImageView) convertView.findViewById(R.id.iv_pic_1);
        ImageView pic2 = (ImageView) convertView.findViewById(R.id.iv_pic_2);
        Picasso.with(context).load(getChild(groupPosition, 0)).into(pic1);
        Picasso.with(context).load(getChild(groupPosition, 1)).into(pic2);
        return convertView;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent){
        try {
            JSONObject album = new JSONObject(getGroup(groupPosition));
            String albumName = album.getString("name");
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.expandable_group_title, null);
            }
            TextView albumTitle = (TextView) convertView.findViewById(R.id.album_title);
            albumTitle.setTypeface(null, Typeface.BOLD);
            albumTitle.setText(albumName);
        }catch (Exception e){
            Log.e("Albums", e.getMessage());
        }
        return convertView;

    }

    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    public int getGroupCount() {
        return albums.size();
    }
    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
