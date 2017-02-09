package com.iused.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.iused.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Lenovo on 05/11/2016.
 */
public class DonorResponsesDetailsActivity extends AppCompatActivity{

    Intent intent = null;
    private TextView txt_user_name=null;
    private TextView txt_user_phone=null;
    private TextView txt_emotional_message=null;
    private CircleImageView img_user_photo=null;
    private TextView txt_product_name=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.donor_response_details_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intent=getIntent();
        getSupportActionBar().setTitle("Contact Donor");

        txt_user_name= (TextView) findViewById(R.id.txt_user_name);
        txt_user_phone= (TextView) findViewById(R.id.txt_phone_number);
        txt_emotional_message= (TextView) findViewById(R.id.txt_emotional_message);
        img_user_photo= (CircleImageView) findViewById(R.id.img_user_photo);
        txt_product_name= (TextView) findViewById(R.id.txt_product_name);

        txt_user_name.setText(intent.getStringExtra("UserName"));
        txt_user_phone.setText(intent.getStringExtra("UserPhone"));
        txt_emotional_message.setText(intent.getStringExtra("ImotionalMessage"));
        txt_product_name.setText("Donating "+intent.getStringExtra("ProductName"));

        txt_user_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + txt_user_phone.getText().toString()));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                            // TODO: Consider calling
//                            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
//                            // here to request the missing permissions, and then overriding
//                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                            //                                          int[] grantResults)
//                            // to handle the case where the user grants the permission. See the documentation
//                            // for Activity#requestPermissions for more details.
//                            return;
//                        }
                }
                startActivity(callIntent);
            }
        });

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
