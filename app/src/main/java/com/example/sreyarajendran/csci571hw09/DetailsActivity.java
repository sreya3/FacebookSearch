package com.example.sreyarajendran.csci571hw09;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;
import static java.security.AccessController.getContext;

public class DetailsActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.mipmap.ic_tab_album);
        tabLayout.getTabAt(1).setIcon(R.mipmap.ic_tab_post);


        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor editor = preferences.edit();
        MenuItem fav = menu.findItem(R.id.action_fav);
        String id = null;
        try {
            JSONObject data = new JSONObject(getIntent().getStringExtra("data"));
            id = data.getString("id");
        }catch (Exception e){
            Log.e("myApp", e.getMessage());
        }
        final String id2 = id;
        if(preferences.getString(id2, "").equals("")){
            fav.setTitle("Add to Favorites");
        }else{
            fav.setTitle("Remove from Favorites");
        }
        return true;
    }

    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor editor = preferences.edit();
        int type = getIntent().getIntExtra("type", -1);
        String user_id = null;
        String data2 = null;
        JSONObject data3 = null;
        try {
            data3 = new JSONObject(getIntent().getStringExtra("data"));
        }catch (Exception e){}

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_fav) {
            try {
                JSONObject data = new JSONObject(getIntent().getStringExtra("data"));
                data2 = data.toString();
                user_id = data.getString("id");
            }catch (Exception e){
                Log.e("myApp", e.getMessage());
            }
            final String id2 = user_id;

            if(preferences.getString(id2, "").equals("")) {
                item.setTitle("Remove from Favorites");
                Toast.makeText(getApplicationContext(), "Added to Favorites", Toast.LENGTH_LONG).show();
                editor.putString(id2, data2);
                editor.putInt(id2+"t", type);
                editor.apply();

            }else{
                item.setTitle("Add to Favorites");
                item.setTitle("Add to Favorites");
                Toast.makeText(getApplicationContext(), "Removed from Favorites", Toast.LENGTH_LONG).show();
                editor.putString(id2, "");
                editor.putInt(id2+"t", type);
                editor.apply();

            }

        }else if(id == R.id.action_share){
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                try {
                    Log.d("myApp", data3.getJSONObject("picture").getJSONObject("data").getString("url"));
                    Log.d("myApp", data3.getString("name"));
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setImageUrl(Uri.parse(data3.getJSONObject("picture").getJSONObject("data").getString("url")))
                            .setContentTitle(data3.getString("name"))
                            .setContentUrl(Uri.parse(data3.getJSONObject("picture").getJSONObject("data").getString("url")))
                            .setQuote(data3.getString("name"))
                            .build();
                    shareDialog.show(linkContent);
                }catch(Exception e){
                    Log.e("myApp", e.getMessage());
                }
                try {
                    Toast.makeText(getApplicationContext(), "Sharing " + data3.getString("name"), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private String savedData;
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);

            fragment.setArguments(args);
            return fragment;
        }

        int lastExpandedPosition = -1;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_details, container, false);
            int pos = getArguments().getInt(ARG_SECTION_NUMBER);
            String data = getActivity().getIntent().getStringExtra("data");
            JSONArray albums = null;
            JSONArray posts = null;
            String pic_url = null;
            String name = null;
            try {
                JSONObject data2 = new JSONObject(data);
                albums = data2.getJSONObject("albums").getJSONArray("data");
                posts = data2.getJSONObject("posts").getJSONArray("data");
                pic_url = data2.getJSONObject("picture").getJSONObject("data").getString("url");
                name = data2.getString("name");
                Log.d("Albums", albums.toString());
            }catch (Exception e){
                Log.e("Albums", e.getMessage());
            }
            switch (pos){
                case 1:
                    createAlbumsTable(albums, rootView);
                    break;
                case 2:
                    createPostsTable(posts, rootView, pic_url, name);
                    break;
            }
            return rootView;
        }

        private void createPostsTable(JSONArray posts, View view, String pic_url, String name){
            JSONObject json_data = null;
            final ArrayList<JSONObject> items = new ArrayList<JSONObject>();
            if(posts == null){
                TextView tv = (TextView)view.findViewById(R.id.tv_no_data);
                tv.setText("No Data Found");
                tv.bringToFront();
            }else {
                for (int i = 0; i < posts.length(); i++) {
                    try {
                        json_data = posts.getJSONObject(i);
                    } catch (Exception e) {
                        Log.e("Posts", e.getMessage());
                    }
                    items.add(json_data);
                    Log.d(items.get(i).toString(), "Output");

                }

                CustomList2 adapter = new
                        CustomList2(getActivity(), items, pic_url, name);
                ListView lv = (ListView) view.findViewById(R.id.lv_posts);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Toast.makeText(getContext(), "You Clicked at " + items.get(+position).toString(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }

        private void createAlbumsTable(JSONArray alb, View view){
            String json_data = null;
            ArrayList<String> items = new ArrayList<String>();
            if(alb == null){
                TextView tv = (TextView)view.findViewById(R.id.tv_no_data);
                tv.setText("No Data Found");
                tv.bringToFront();
            }else {
                for (int i = 0; i < alb.length(); i++) {
                    try {
                        json_data = alb.getJSONObject(i).toString();
                    } catch (Exception e) {
                        Log.e("Albums", e.getMessage());
                    }
                    items.add(json_data);
                    Log.d(items.get(i).toString(), "Output");

                }
                final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(getActivity(), items);
                final ExpandableListView lv = (ExpandableListView) view.findViewById(R.id.lv_albums);
                lv.setAdapter(expListAdapter);
                lv.bringToFront();


                lv.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                    @Override
                    public void onGroupExpand(int groupPosition) {
                        if (lastExpandedPosition != -1
                                && groupPosition != lastExpandedPosition) {
                            lv.collapseGroup(lastExpandedPosition);
                        }
                        lastExpandedPosition = groupPosition;
                    }
                });
            }

        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Albums";
                case 1:
                    return "Posts";

            }
            return null;
        }
    }
}
