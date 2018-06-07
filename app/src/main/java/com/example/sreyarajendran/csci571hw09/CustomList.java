package com.example.sreyarajendran.csci571hw09;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sreyarajendran.csci571hw09.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.prefs.PreferenceChangeEvent;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Sreya Rajendran on 4/25/2017.
 */

public class CustomList extends ArrayAdapter<JSONObject>{
    private final Activity context;
    private final ArrayList<JSONObject> arr;
    private final int type;
    private final int actno;
    public CustomList(Activity context,
                      ArrayList<JSONObject> arr, int type, int actno) {
        super(context, R.layout.single_item, arr);
        this.context = context;
        this.arr = arr;
        this.type = type;
        this.actno = actno;
    }
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.single_item, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.tv_name);
        Button det_btn = (Button)rowView.findViewById(R.id.btn_det);
        final Button fav_btn = (Button)rowView.findViewById(R.id.btn_fav);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.iv_pic);
        try {
            txtTitle.setText(arr.get(position).getString("name"));

            Picasso.with(context).load(arr.get(position).getJSONObject("picture").getJSONObject("data").getString("url")).into(imageView);
        }catch (Exception e){
            Log.e("MyApp", e.getMessage());
        }
        det_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetDetails task = new GetDetails();
                try {
                    task.execute(arr.get(position).getString("id"));
                }catch (Exception e){
                    Log.e("myApp", e.getMessage());
                }

            }
        });
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetDetails task = new GetDetails();
                try {
                    task.execute(arr.get(position).getString("id"));
                }catch (Exception e){
                    Log.e("myApp", e.getMessage());
                }
            }
        });
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor editor = preferences.edit();
        String id = null;
        try {
            id = arr.get(position).getString("id");
        }catch (Exception e){
            Log.e("myApp", e.getMessage());
        }
        final String id2 = id;
        if(preferences.getString(id2, "").equals("")){
            fav_btn.setBackgroundResource(R.mipmap.ic_fav_off);
        }else{
            fav_btn.setBackgroundResource(R.mipmap.ic_fav_on);
        }
        fav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(preferences.getString(id2, "").equals("")) {
                    editor.putString(id2, arr.get(position).toString());
                    editor.putInt(id2+"t", type);
                    editor.apply();
                    fav_btn.setBackgroundResource(R.mipmap.ic_fav_on);

                }else{
                    editor.putString(id2, "");
                    editor.putInt(id2+"t", type);
                    editor.apply();
                    fav_btn.setBackgroundResource(R.mipmap.ic_fav_off);
                    if(actno == 2){
                        Intent intent = new Intent(getApplicationContext(), FavoritesActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("tabno", type);
                        getApplicationContext().startActivity(intent);

                    }
                }
            }
        });
        return rowView;
    }

    private class GetDetails extends AsyncTask<String, Integer, Bundle> {
        Bundle args = new Bundle();
        Intent intent;

        protected Bundle doInBackground(String... strings) {
            final RequestQueue queue = Volley.newRequestQueue(getContext());
            String url = String.format("http://homework09-165914.appspot.com?id=%1$s&do=%2$s", strings[0], "get_details");
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("DataFromServer", response);
                    try {
                        JSONObject resp = new JSONObject(response);
                        intent = new Intent(getApplicationContext(), DetailsActivity.class);
                        intent.putExtra("data", response);
                        intent.putExtra("type", type);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(intent);
                    } catch (Exception e) {
                        Log.e("myApp", e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            queue.add(stringRequest);

            return args;

        }


    }
}
