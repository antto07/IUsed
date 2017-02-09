package com.iused.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.iused.main.MainActivity;


/**
 * Created by Antto on 23-08-2016.
 */
public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

//    public static String[] tabTitles = new String[]{"Electronics","Mobile","Fashion","Cars","Motor Cycle","Real Estate"};
    int PAGE_COUNT = 0;
    private SharedPreferences pref = null;
    private Context context;
    private String[] title = null;
    private String[] ids = null;

    public SampleFragmentPagerAdapter(FragmentManager fm, int pageCount, String[] title, String[] id) {
        super(fm);
        this.PAGE_COUNT = pageCount;
        this.title = title;
        this.ids = id;
    }

    @Override
    public int getCount() {

        return PAGE_COUNT;
    }



    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        args.putInt("ARG_PAGE", position);
        args.putStringArray("title", title);
        args.putStringArray("ids", ids);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
//        for (int j = 0; j < MainActivity_Ads.ads_to_dispaly_array.length(); j++) {
//            tabTitles[j] = MainActivity_Ads.str_dp_text;
//            Log.e("tab_names",tabTitles[j]);
//        }
        return title[position];
    }
}
