package com.iused.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;

import com.iused.R;
import com.iused.introduction.Splash;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Antto on 23-08-2016.
 */
public class ExampleFragments extends Fragment {

    private ViewPager viewPager;
    public static PagerSlidingTabStrip tabsStrip;
    View view = null;
    private String[] title = null;
    private String[] ids = null;
    private Typeface face=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        try {
            view = inflater.inflate(R.layout.fragment_main, container, false);
            viewPager = (ViewPager) view.findViewById(R.id.viewpager);

            Bundle bundle = this.getArguments();
            title = bundle.getStringArray("pages");
            ids = bundle.getStringArray("ids");

            face= Typeface.createFromAsset(getActivity().getAssets(), "fonts/bariolbd.otf");

            // Give the PagerSlidingTabStrip the ViewPager
            tabsStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tab_strip);

            tabsStrip.setTypeface(face,0);
            tabsStrip.setTextSize(28);

            viewPager.setAdapter(new SampleFragmentPagerAdapter(getChildFragmentManager(), title.length, title, ids));
//        viewPager.setOffscreenPageLimit(0);
            // Attach the view pager to the tab strip
            tabsStrip.setViewPager(viewPager);

        } catch (Exception e) {
            e.printStackTrace();
//            Intent myint = new Intent(getActivity(), Splash.class);
//            myint.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(myint);
        }
        return view;
    }

//    @Nullable
//    @Override
//    public View getView() {
//        return view;
//    }


}
