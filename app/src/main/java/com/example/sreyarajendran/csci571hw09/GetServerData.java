package com.example.sreyarajendran.csci571hw09;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Sreya Rajendran on 4/24/2017.
 */
public class GetServerData extends AsyncTask<String, Integer, Bundle>{
    Bundle args = new Bundle();
    Intent intent;

    protected Bundle doInBackground(String... strings){
        final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        int count = strings.length;
        StringRequest stringRequest;
        for(int i = 1; i<count; i++){
            //Toast.makeText(getApplicationContext(), Integer.toString(i), Toast.LENGTH_SHORT);
            stringRequest = createRequest(strings[0],strings[i]);
            queue.add(stringRequest);
        }
        return args;
    }

    protected void onPostExecute(Bundle result){


    }
    public StringRequest createRequest(String keyword, final String type){
        String url =String.format("http://cs-server.usc.edu:26028/server.php?search-input=%1$s&type=%2$s", keyword, type);
        //st a string response from the provided URL.
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string
                        //Toast.makeText(getActivity().getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("myApp", response);
                        args.putString(type, response);
                        if(args.size() == 5){
                            intent = new Intent(getApplicationContext(), ResultsActivity.class);
                            intent.putExtras(args);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        return stringRequest;
    }
}
