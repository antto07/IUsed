package com.iused.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

//import com.android.volley.toolbox.NetworkImageView;
//import com.mykingdom.mypillows.R;
//import com.mykingdom.mypillows.utils.VolleySingleton;

import com.iused.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ImageAdapter extends PagerAdapter {
    Context context;
    ArrayList<String> img;
    LayoutInflater layoutInflater;
    ImageView imageView;

    public ImageAdapter(Context context, ArrayList<String> img) {
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

        container.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
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