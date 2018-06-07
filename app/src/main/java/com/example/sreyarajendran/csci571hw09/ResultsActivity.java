package com.example.sreyarajendran.csci571hw09;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ResultsActivity extends AppCompatActivity
    implements UsersFragment.OnFragmentInteractionListener, PlacesFragment.OnFragmentInteractionListener, PagesFragment.OnFragmentInteractionListener, EventsFragment.OnFragmentInteractionListener,GroupsFragment.OnFragmentInteractionListener{
    private static Bundle c;
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

    public void onFragmentInteraction(Uri uri){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.mipmap.ic_tab_users);
        tabLayout.getTabAt(1).setIcon(R.mipmap.ic_tab_pages);
        tabLayout.getTabAt(2).setIcon(R.mipmap.ic_tab_events);
        tabLayout.getTabAt(3).setIcon(R.mipmap.ic_tab_places);
        tabLayout.getTabAt(4).setIcon(R.mipmap.ic_tab_groups);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
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

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_results, container, false);
            Bundle b = getActivity().getIntent().getExtras();
            if(b == null ){
                b = c;
            }else{
                c = b;
            }
            int pos = getArguments().getInt(ARG_SECTION_NUMBER);
            switch (pos) {
                case 1:
                    displayTable(b.getString("user"), rootView);
                    break;
                case 2:
                    displayTable(b.getString("page"), rootView);
                    break;
                case 3:
                    displayTable(b.getString("event"), rootView);
                    break;
                case 4:
                    displayTable(b.getString("place"), rootView);
                    break;
                case 5:
                    displayTable(b.getString("group"), rootView);
                    break;
            }

            return rootView;
        }

        public void displayTable(String data, final View view) {
            JSONObject data2 = new JSONObject();
            try {
                data2 = new JSONObject(data);
            } catch (Exception e) {
                Log.e("MyApp", e.getMessage());
            }
            final JSONObject data3 = data2;
            final ArrayList<JSONObject> items = new ArrayList<JSONObject>();

            try {
                JSONArray arr = data2.getJSONArray("data");
                JSONObject json_data;

                for (int i = 0; i < arr.length(); i++) {
                    json_data = arr.getJSONObject(i);
                    items.add(json_data);
                    Log.d(items.get(i).toString(), "Output");
                }


            } catch (Exception e) {
                Log.e("MyApp", e.getMessage());
            }
            /*ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_list_item_1, items);
            ListView lv = (ListView)v.findViewById(R.id.items_list_view);
            lv.setAdapter(mArrayAdapter);*/
            int type = getArguments().getInt(ARG_SECTION_NUMBER);
            CustomList adapter = new
                    CustomList(getActivity(), items, type, 1);
            ListView lv = (ListView) view.findViewById(R.id.items_list_view);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Toast.makeText(getContext(), "You Clicked at " + items.get(+position).toString(), Toast.LENGTH_SHORT).show();

                }
            });
            Button next_btn = (Button) view.findViewById(R.id.btn_next);

            next_btn.setOnClickListener(new View.OnClickListener() {
                String s = "dsdf";

                @Override
                public void onClick(View v) {
                    try {
                        GetPrevNext task = new GetPrevNext();
                        Log.d("MyApp", data3.getJSONObject("paging").getString("next"));
                        task.execute(data3.getJSONObject("paging").getString("next"));
                    } catch (Exception e) {
                        Log.e("myApp", "Couldn't get next page");
                    }
                }
            });
            Button prev_btn = (Button) view.findViewById(R.id.btn_prev);
            prev_btn.setOnClickListener(new View.OnClickListener() {
                String s = "dsdf";

                @Override
                public void onClick(View v) {
                    try {
                        GetPrevNext task = new GetPrevNext();
                        Log.d("MyApp", data3.getJSONObject("paging").getString("previous"));
                        task.execute(data3.getJSONObject("paging").getString("previous"));
                    } catch (Exception e) {
                        Log.e("myApp", "Couldn't get Previous page");
                    }
                }
            });
            try {
                if (data3.getJSONObject("paging").isNull("next")) {
                    next_btn.setEnabled(false);
                } else {
                    next_btn.setEnabled(true);
                }
            }catch (Exception e){
                Log.e("JSON", e.getMessage());
            }
            try {
                if (data3.getJSONObject("paging").isNull("previous")) {
                    prev_btn.setEnabled(false);
                } else {
                    prev_btn.setEnabled(true);
                }
            }catch (Exception e){
                Log.e("JSON", e.getMessage());
            }
        }


        private class GetPrevNext extends AsyncTask<String, Integer, Bundle> {
            Bundle args = new Bundle();
            Intent intent;

            protected Bundle doInBackground(String... strings) {
                final RequestQueue queue = Volley.newRequestQueue(getContext());
                String url = String.format("http://homework09-165914.appspot.com?url=%1$s&do=%2$s", strings[0], "next");
                StringRequest stringRequest = new StringRequest(Request.Method.GET, strings[0], new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("DataFromServer", response);
                        try {
                            JSONObject resp = new JSONObject(response);
                            displayTable(resp.toString(), getView());
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
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Users";
                case 1:
                    return "Pages";
                case 2:
                    return "Events";
                case 3:
                    return "Places";
                case 4:
                    return "Groups";
            }
            return null;
        }
    }
}
