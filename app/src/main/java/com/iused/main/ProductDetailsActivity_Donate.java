package com.iused.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iused.R;
import com.iused.adapters.FullImageAdapter;
import com.iused.introduction.LoginActivity;
import com.iused.utils.AsyncTaskListener;
import com.iused.utils.Constants;
import com.iused.utils.HttpAsync;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yash on 21-10-2016.
 */
public class ProductDetailsActivity_Donate extends AppCompatActivity implements AsyncTaskListener{

    Intent intent = null;
    ImageView img_product;
    private TextView txt_name_first_letter = null;
    private TextView txt_name = null;
    private TextView txt_request_price = null;
    private TextView txt_product_description = null;
    private Button btn_want_to_buy= null;

    private HashMap<String, String> para = null;
    public static HashMap<String, String> para_buy_request = null;
    private AsyncTaskListener listener = null;
    public SharedPreferences mpref = null;
        private ProgressDialog progressDialog= null;
    private TextView txt_product_name=null;
    private TextView txt_used_for=null;
    private EditText edt_message=null;
    private TextView txt_condition=null;
    private TextView txt_distance=null;

    String currentDateTimeString = "";
    String locale ="";
    String timezone="";

    private List<String> gallery = null;
    private ArrayList<String> gallery_images=null;
    ViewPager viewPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private CircleImageView img_seller_image=null;
    private Typeface face=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_product_details_donate);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listener = ProductDetailsActivity_Donate.this;
        progressDialog=new ProgressDialog(ProductDetailsActivity_Donate.this);
        intent=getIntent();
        getSupportActionBar().setTitle(intent.getStringExtra("product_name"));

        mpref = getSharedPreferences("user_details", MODE_PRIVATE);
        if (mpref != null)

            txt_name_first_letter= (TextView) findViewById(R.id.txt_name_first_letter);
        txt_name= (TextView) findViewById(R.id.txt_name);
        txt_request_price= (TextView) findViewById(R.id.txt_product_price);
        btn_want_to_buy= (Button) findViewById(R.id.btn_want_to_buy);
        txt_product_description= (TextView) findViewById(R.id.txt_product_description);
        txt_product_name= (TextView) findViewById(R.id.txt_product_name);
        txt_used_for= (TextView) findViewById(R.id.txt_used_for);
        img_seller_image= (CircleImageView) findViewById(R.id.img_seller_image);
        txt_condition= (TextView) findViewById(R.id.txt_condition);
        txt_distance= (TextView) findViewById(R.id.txt_distance);

        txt_product_name.setText(intent.getStringExtra("product_name"));
        txt_used_for.setText(intent.getStringExtra("used_for")+" old");
        txt_request_price.setText("₹ "+intent.getStringExtra("price"));
        txt_product_description.setText(intent.getStringExtra("description"));
        edt_message= (EditText) findViewById(R.id.edt_message);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        img_seller_image= (CircleImageView) findViewById(R.id.img_seller_image);
        txt_condition.setText(intent.getStringExtra("Condition"));
        txt_distance.setText(Math.round(Double.parseDouble(intent.getStringExtra("distance")))+" Km(s) away");

        face= Typeface.createFromAsset(getAssets(), "fonts/bariolreg.otf");
        edt_message.setTypeface(face);

        img_product = (ImageView) findViewById(R.id.img_product);
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//            img_product.setTransitionName("profile");
//        }

//        Picasso.with(getApplicationContext())
//                .load(intent.getStringExtra("image"))
//                //.placeholder(R.drawable.user_placeholder) not considering has thumbnails are small size
//                //.error(R.drawable.user_placeholder_error)
//                .into(img_product,new PicassoCallback(intent.getStringExtra("image")));

        locale = getApplicationContext().getResources().getConfiguration().locale.getDisplayCountry();
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        currentDateTimeString = formatter.format(today);
        timezone= TimeZone.getDefault().getDisplayName();

        try {
            Picasso.with(getApplicationContext())
                    .load(intent.getStringExtra("donor_image"))
                    //.placeholder(R.drawable.user_placeholder) not considering has thumbnails are small size
                    //.error(R.drawable.user_placeholder_error)
//                    .resize(200,200)
                    .into(img_seller_image);
        }catch (Exception e){
            Picasso.with(getApplicationContext())
                    .load("http://52.41.70.254/pics/user.jpg")
                    //.placeholder(R.drawable.user_placeholder) not considering has thumbnails are small size
                    //.error(R.drawable.user_placeholder_error)
//                    .resize(200,200)
                    .into(img_seller_image);
        }

        btn_want_to_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_message.getWindowToken(), 0);

                if(edt_message.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Enter a message",Toast.LENGTH_SHORT).show();
                }
                else {

                    para_buy_request = new HashMap<>();
                    para_buy_request.put("UserId", mpref.getString("user_id",""));
                    para_buy_request.put("ProductId", intent.getStringExtra("product_id"));
                    para_buy_request.put("Datetime",currentDateTimeString);
                    para_buy_request.put("Amount",intent.getStringExtra("price"));
                    para_buy_request.put("OfferTill","");
                    para_buy_request.put("Qty",intent.getStringExtra("qty_remaining"));
                    para_buy_request.put("Message",edt_message.getText().toString());

                    if(mpref.getString("guest_status","").equalsIgnoreCase("0")){
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.putExtra("offer_negotiable","donate_offer");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    else {
                        progressDialog.setMessage("Loading...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        Log.e("products_details", para_buy_request.toString());
                        HttpAsync httpAsync1 = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL+"PurchaseRequests", para_buy_request, 2, "buy_request");
                        httpAsync1.execute();
                    }


                }

            }
        });



        para = new HashMap<>();
        para.put("UserId", mpref.getString("user_id",""));
        para.put("ProductId", intent.getStringExtra("product_id"));
        para.put("Datetime",currentDateTimeString);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("products_details", para.toString());
        HttpAsync httpAsync1 = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL+"ProductDetail", para, 2, "product_details");
        httpAsync1.execute();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onTaskCancelled(String data) {

    }

    @Override
    public void onTaskComplete(String result, String tag) {


        progressDialog.dismiss();
        if(result.equalsIgnoreCase("fail")){
            try {
                Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
            }catch (Exception e){

            }
        }
        else {
            if(tag.equalsIgnoreCase("product_details")){
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    if (jsonObject != null){
                        if(jsonObject.getString("errFlag").equalsIgnoreCase("0")){
                            JSONArray jsonsendarr = jsonObject.optJSONArray("Data");
                            if (jsonsendarr.length() > 0){
                                for (int i = 0; i < jsonsendarr.length(); i++){
                                    if (jsonObject != null){
                                        JSONObject volumobject = jsonsendarr.getJSONObject(i);

                                        txt_name_first_letter.setText(String.valueOf(volumobject.getString("UserName").charAt(0)).toUpperCase());
                                        txt_name.setText(volumobject.getString("UserName"));

                                        gallery=new ArrayList<String>( Arrays.asList(volumobject.getString("ImageLinks").split("\\s*,\\s*")));
                                        gallery_images=new ArrayList<>(gallery);

                                        if(gallery_images!=null && gallery_images.size()>0) {

                                            NUM_PAGES = gallery_images.size();
                                        }else
                                        {
                                            NUM_PAGES = 1;
                                        }

                                        // Auto start of viewpager
                                        final Handler handler = new Handler();
                                        final Runnable Update = new Runnable() {
                                            public void run() {
                                                if (currentPage == NUM_PAGES) {
                                                    currentPage = 0;
                                                }
                                                viewPager.setCurrentItem(currentPage++, true);
                                            }
                                        };
                                        Timer swipeTimer = new Timer();
                                        swipeTimer.schedule(new TimerTask() {
                                            @Override
                                            public void run() {
                                                handler.post(Update);
                                            }
                                        }, 3000, 3000);

                                        if(gallery_images!=null && gallery_images.size()>0) {
                                            FullImageAdapter adapter = new FullImageAdapter(this, gallery_images);
                                            viewPager.setAdapter(adapter);


                                            CirclePageIndicator indicator = (CirclePageIndicator)
                                                    findViewById(R.id.indicator);

                                            if(gallery_images.size()==1){
                                                indicator.setVisibility(View.INVISIBLE);
//                                                viewPager.setVisibility(View.GONE);
//                                                img_product.setVisibility(View.VISIBLE);
                                            }

                                            else {
//                                                viewPager.setVisibility(View.VISIBLE);
//                                                img_product.setVisibility(View.GONE);
                                                final float density = getResources().getDisplayMetrics().density;
                                                indicator.setFillColor(0xFFFFFFFF);
                                                indicator.setStrokeColor(0xFFFFFFFF);
                                                indicator.setStrokeWidth(1);
                                                indicator.setRadius(6 * density);
                                                indicator.setViewPager(viewPager);

                                                //Set circle indicator radius
                                                indicator.setRadius(5 * density);
                                            }


                                            indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                                                @Override
                                                public void onPageSelected(int position) {
                                                    currentPage = position;

                                                }

                                                @Override
                                                public void onPageScrolled(int pos, float arg1, int arg2) {

                                                }

                                                @Override
                                                public void onPageScrollStateChanged(int pos) {

                                                }
                                            });
                                        }


                                    }
                                }
                            }
                        }
//                        Toast.makeText(getApplicationContext(),jsonObject.getString("errMsg"),Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            else if(tag.equalsIgnoreCase("buy_request")){
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    if (jsonObject != null){
                        if(jsonObject.getString("errFlag").equalsIgnoreCase("0")){
                            Toast.makeText(getApplicationContext(),jsonObject.getString("errMsg"),Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),jsonObject.getString("errMsg"),Toast.LENGTH_LONG).show();
                        }
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }

    }

//    class PicassoCallback implements Callback {
//
//        String thumbnail_poster;
//
//        public PicassoCallback(String thumbnail) {
//            this.thumbnail_poster=thumbnail;
//        }
//
//        @Override
//        public void onSuccess() {
//
//        }
//
//        @Override
//        public void onError() {
////            loadRottenPoster=true;
//            Picasso.with(getApplicationContext()).load("http://52.41.70.254/pics/user.jpg").into(img_product);
//        }
//    }

}
