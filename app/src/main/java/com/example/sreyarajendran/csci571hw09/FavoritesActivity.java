package com.example.sreyarajendran.csci571hw09;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class FavoritesActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener, AboutMeFragment.OnFragmentInteractionListener{

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
        setContentView(R.layout.activity_favorites);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        if(getIntent().getIntExtra("tabno", -1) != -1) {
            mViewPager.setCurrentItem(getIntent().getIntExtra("tabno", -1) - 1);
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(1).setChecked(true);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.mipmap.ic_tab_users);
        tabLayout.getTabAt(1).setIcon(R.mipmap.ic_tab_pages);
        tabLayout.getTabAt(2).setIcon(R.mipmap.ic_tab_events);
        tabLayout.getTabAt(3).setIcon(R.mipmap.ic_tab_places);
        tabLayout.getTabAt(4).setIcon(R.mipmap.ic_tab_groups);



    }
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        if (id == R.id.nav_home) {
            // Handle the camera action
            Intent intent = new Intent(getApplicationContext(), MainScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);
        } else if (id == R.id.nav_favorites) {
            Intent intent = new Intent(getApplicationContext(), FavoritesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);
        } else if (id == R.id.nav_about_me) {
            Intent intent = new Intent(getApplicationContext(), MainScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("to_about", 1);
            getApplicationContext().startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favorites, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_favorites2, container, false);
            rootView.bringToFront();
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
            Map<String, ?> allEntries = pref.getAll();
            ArrayList<JSONObject> users = new ArrayList<>();
            ArrayList<JSONObject> pages = new ArrayList<>();
            ArrayList<JSONObject> events = new ArrayList<>();
            ArrayList<JSONObject> places = new ArrayList<>();
            ArrayList<JSONObject> groups = new ArrayList<>();
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
                if(!entry.getValue().toString().equals("")){
                    int type = pref.getInt(entry.getKey()+"t", -1);
                    try {
                        switch (type) {
                            case 1:
                                users.add(new JSONObject(entry.getValue().toString()));
                                break;
                            case 2:
                                pages.add(new JSONObject(entry.getValue().toString()));
                                break;
                            case 3:
                                events.add(new JSONObject(entry.getValue().toString()));
                                break;
                            case 4:
                                places.add(new JSONObject(entry.getValue().toString()));
                                break;
                            case 5:
                                groups.add(new JSONObject(entry.getValue().toString()));
                                break;
                        }
                    }catch (Exception e){

                    }
                }
            }

            int pos = getArguments().getInt(ARG_SECTION_NUMBER);
            switch (pos) {
                case 1:
                    displayTable(users, rootView);
                    break;
                case 2:
                    displayTable(pages, rootView);
                    break;
                case 3:
                    displayTable(events, rootView);
                    break;
                case 4:
                    displayTable(places, rootView);
                    break;
                case 5:
                    displayTable(groups, rootView);
                    break;
            }
            return rootView;

        }

        private void displayTable(final ArrayList<JSONObject>items, View view){
            int type = getArguments().getInt(ARG_SECTION_NUMBER);
            CustomList adapter = new
                    CustomList(getActivity(), items, type, 2);
            ListView lv = (ListView) view.findViewById(R.id.lv_favs);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Toast.makeText(getContext(), "You Clicked at " + items.get(+position).toString(), Toast.LENGTH_SHORT).show();

                }
            });
            lv.bringToFront();
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
