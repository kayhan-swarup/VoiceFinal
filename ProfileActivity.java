package com.swarup.kayhan.voice;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


public class ProfileActivity extends FragmentActivity {
    ViewPager pager;int count=3;
    Fragment[] fragments= new Fragment[count];


    MyPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager);
        pager = (ViewPager)findViewById(R.id.pager);
//        adapter = new MyPagerAdapter(getSupportFragmentManager());
        fragments[0] = new PlaceHolderFragment();
        fragments[1] = new TestFragment();
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        if(getActionBar()!=null)
            getActionBar().hide();
    }

    @SuppressLint("ValidFragment")
    public class PlaceHolderFragment extends Fragment{
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.test_fragment,container,false);

            return v;
        }
    }
    @SuppressLint("ValidFragment")
    public class TestFragment extends Fragment{

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.test_fragment2,container,false);

            return v;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
    class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "HOME";
                case 1:
                    return "LIST";
                default:
                    return "title";
            }
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = null;
            switch(i){
                case 0:
                    if(Login.user!=null)
                    fragment = new User(Login.user.getUserId(),Login.user.getPassword(),Login.user.getEmail(),Login.user.getFirstName(),Login.user.getLastName(),Login.user.getSex());
                    else
                    fragment = new User("Test");
                    break;
                case 1:
                    fragment = new Post();
                    break;
                case 2:
                    fragment = new RecentPosts();
                    break;
                default:
                    break;

            }
            return fragment;
        }
    }
}
