package com.iused.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;

//import com.mykingdom.mypillows.Adapter.ImageAdapter;
//import com.mykingdom.mypillows.R;
//import com.mykingdom.mypillows.utils.AsyncTaskListener;
//import com.mykingdom.mypillows.utils.SettingSharedPrefs;

import com.iused.R;
import com.iused.adapters.ImageAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 12-02-2016.
 */
public class FullImageUserActivity extends AppCompatActivity {

    ViewPager viewPager;
    private ArrayList<String> gallery = null;
    private int position;
    ImageAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fullscreenuserview);

        viewPager = (ViewPager) findViewById(R.id.user_fullpager);


        Bundle bundleObject = getIntent().getExtras();
        // Get ArrayList Bundle
        gallery = (ArrayList<String>) bundleObject.getSerializable("gallery_images");
        Log.e("gal", gallery.toString());


        position = bundleObject.getInt("position", position);


        adapter = new ImageAdapter(this, gallery);
        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(position);

    }
}
