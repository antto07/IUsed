package com.iused.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.widget.TextView;

import com.iused.R;
import com.iused.utils.AsyncTaskListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Lenovo on 14/11/2016.
 */
public class OrderedProductsDetailsActivity extends AppCompatActivity{

    Intent intent = null;
    private TextView txt_user_name=null;
    private TextView txt_user_phone=null;
//    private TextView txt_emotional_message=null;
    private CircleImageView img_user_photo=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.order_response_details_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intent=getIntent();
        getSupportActionBar().setTitle(intent.getStringExtra("ProductName"));

        txt_user_name= (TextView) findViewById(R.id.txt_user_name);
        txt_user_phone= (TextView) findViewById(R.id.txt_phone_number);
//        txt_emotional_message= (TextView) findViewById(R.id.txt_emotional_message);
        img_user_photo= (CircleImageView) findViewById(R.id.img_user_photo);

        txt_user_name.setText(intent.getStringExtra("UserName"));
        txt_user_phone.setText(intent.getStringExtra("UserPhone"));
//        txt_emotional_message.setText(intent.getStringExtra("ImotionalMessage"));

        try {
            Picasso.with(getApplicationContext())
                    .load(intent.getStringExtra("Photo"))
                    //.placeholder(R.drawable.user_placeholder) not considering has thumbnails are small size
                    //.error(R.drawable.user_placeholder_error)
                    .into(img_user_photo,new PicassoCallback(intent.getStringExtra("Photo")));
        }catch (Exception e){
            Picasso.with(getApplicationContext())
                    .load("http://52.41.70.254/pics/user.jpg")
                    //.placeholder(R.drawable.user_placeholder) not considering has thumbnails are small size
                    //.error(R.drawable.user_placeholder_error)
                    .into(img_user_photo,new PicassoCallback("http://52.41.70.254/pics/user.jpg"));
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
            Picasso.with(getApplicationContext()).load("http://52.41.70.254/pics/user.jpg").into(img_user_photo);
        }
    }

  }
