package com.iused.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

//import com.android.volley.toolbox.NetworkImageView;
//import com.mykingdom.mypillows.Hotelier.FullImageActivity;
//import com.mykingdom.mypillows.R;
//import com.mykingdom.mypillows.User.FullImageUserActivity;
//import com.mykingdom.mypillows.utils.VolleySingleton;


import com.iused.R;
import com.iused.main.FullImageUserActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by user on 12-02-2016.
 */
public class FullImageAdapter extends PagerAdapter {
    Context context;
    ArrayList<String> img;
    LayoutInflater layoutInflater;
    ImageView imageView ;

    public FullImageAdapter(Context context, ArrayList<String> img) {
        this.context = context;
        this.img = img;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return img.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.galleryslide_images, container, false);
        imageView = (ImageView) view.findViewById(R.id.networkimage);
//        LinearLayout.LayoutParams vp =
//                new LinearLayouct.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT);
//        imageView.setLayoutParams(vp);
//        imageView.getLayoutParams().height=300;
//        imageView.setScaleType(NetworkImageView.ScaleType.CENTER_INSIDE);
//        imageView.setImageUrl(img.get(position), VolleySingleton.getInstance(context).getImageLoader());

        try {
            Picasso.with(context)
                    .load(img.get(position))
                    //.placeholder(R.drawable.user_placeholder) not considering has thumbnails are small size
                    //.error(R.drawable.user_placeholder_error)
                    .into(imageView,new PicassoCallback(img.get(position)));
        }catch (Exception e){
            Picasso.with(context)
                    .load("http://52.41.70.254/pics/user.jpg")
                    //.placeholder(R.drawable.user_placeholder) not considering has thumbnails are small size
                    //.error(R.drawable.user_placeholder_error)
                    .into(imageView,new PicassoCallback("http://52.41.70.254/pics/user.jpg"));
        }

        imageView.setOnClickListener(new OnImageClickListener(position));
        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    class OnImageClickListener implements View.OnClickListener {

        int postion;

        // constructor
        public OnImageClickListener(int position) {
            this.postion = position;
        }

        @Override
        public void onClick(View v) {
            ArrayList<String> arr = new ArrayList<>();
            for (int i = 0; i < img.size(); i++) {
                arr.add(img.get(i));
            }


            // on selecting grid view image
            // launch full screen activity
            Intent i = new Intent(context, FullImageUserActivity.class);

            Bundle bundleObject = new Bundle();
            bundleObject.putSerializable("gallery_images", arr);
            bundleObject.putInt("position", postion);
            i.putExtras(bundleObject);
            context.startActivity(i);
        }

    }

    class PicassoCallback implements Callback {

        String thumbnail_poster;

        public PicassoCallback(String thumbnail) {
            this.thumbnail_poster=thumbnail;
        }

        @Override
        public void onSuccess() {

        }

        @Override
        public void onError() {
//            loadRottenPoster=true;
            Picasso.with(context).load("http://52.41.70.254/pics/user.jpg").into(imageView);
        }
    }

}
