package com.example.sreyarajendran.csci571hw09;

        import android.app.Activity;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.squareup.picasso.Picasso;

        import org.json.JSONObject;

        import java.text.DateFormat;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.Locale;

/**
 * Created by Sreya Rajendran on 4/26/2017.
 */

public class CustomList2 extends ArrayAdapter<JSONObject>{
    private final Activity context;
    private final ArrayList<JSONObject> posts;
    private final String pic_url;
    private final String name;
    public  CustomList2(Activity context, ArrayList<JSONObject> posts, String pic_url, String name){
        super(context, R.layout.single_post, posts);
        this.context = context;
        this.posts = posts;
        this.pic_url = pic_url;
        this.name = name;
    }

    public View getView(final int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.single_post, null, true);
        TextView postTitle = (TextView)rowView.findViewById(R.id.tv_post_name);
        ImageView posterImage = (ImageView)rowView.findViewById(R.id.iv_poster);
        TextView postTime = (TextView)rowView.findViewById(R.id.tv_post_time);
        TextView postMessage = (TextView)rowView.findViewById(R.id.tv_post_message);
        try {
            postTitle.setText(name);
            Picasso.with(context).load(pic_url).into(posterImage);
            String time = posts.get(position).getString("created_time");
            String startTime = "2013-12-21T18:30:00+0100";
            SimpleDateFormat incomingFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            Date date = incomingFormat.parse(startTime);

            SimpleDateFormat outgoingFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());

            postTime.setText(outgoingFormat.format(date));
            postMessage.setText(posts.get(position).getString("message"));
        }catch (Exception e){
            Log.e("posts", e.getMessage());
        }
        return rowView;
    }
}
