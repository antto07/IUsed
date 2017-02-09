package com.iused.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iused.R;
import com.iused.adapters.FullImageAdapter;
import com.iused.bean.MainProductsBean;
import com.iused.main.MainActivity;
import com.iused.main.ProductDetailsActivity_Negotiable;
import com.iused.utils.AsyncTaskListener;
import com.iused.utils.Constants;
import com.iused.utils.HttpAsync;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
 * Created by Antto on 27/01/2017.
 */
public class Fragment_ProductDetails_Negotiable extends Fragment implements AsyncTaskListener{

    View view = null;
    Intent intent = null;
    //    ImageView img_product;
    private TextView txt_name_first_letter = null;
    private TextView txt_name = null;
    private TextView txt_request_price = null;
    private TextView txt_product_description = null;
    private Button btn_buy_now= null;
    private EditText edt_offer_price= null;
    private EditText edt_offer_minutes= null;
    private EditText edt_deadline_type= null;

    private HashMap<String, String> para = null;
    public static HashMap<String, String> para_buy_request = null;
    private AsyncTaskListener listener = null;
    private int offer_min_price=0;
    public SharedPreferences mpref = null;
    private ProgressDialog progressDialog= null;
    private TextView txt_product_name=null;
    private TextView txt_used_for=null;
    private TextView txt_currency_code=null;
    private TextView txt_distance=null;
    private TextView txt_condition=null;
    String currentDateTimeString = "";
    String locale ="";
    String timezone="";
    private List<String> gallery = null;
    private ArrayList<String> gallery_images=null;
    ViewPager viewPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private CircleImageView img_seller_image=null;
    private ImageView img_play_video=null;
    public String str_video_link=null;
    private Typeface face,face1=null;
    MainProductsBean mainProductsBean=null;
    private int mPage;
    private String[] str_arr_product_id = null;
    private String[] str_arr_product_name = null;
    private String[] str_arr_product_used_for = null;
    private String[] str_arr_product_condition = null;
    private String[] str_arr_product_distance = null;
    private String[] str_arr_product_posted_image = null;
    private String[] str_arr_product_description = null;
    private String[] str_arr_product_price = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            view = inflater.inflate(R.layout.fragment_product_details_negotiable, container, false);
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            progressDialog= new ProgressDialog(getActivity());
            listener = Fragment_ProductDetails_Negotiable.this;
            intent=getActivity().getIntent();

            mPage = getArguments().getInt("ARG_PAGE");
            str_arr_product_id = getArguments().getStringArray("product_ids");
            str_arr_product_name = getArguments().getStringArray("product_names");
            str_arr_product_used_for = getArguments().getStringArray("product_used_for");
            str_arr_product_condition = getArguments().getStringArray("product_condition");
            str_arr_product_distance = getArguments().getStringArray("product_distance");
            str_arr_product_posted_image = getArguments().getStringArray("product_posted_image");
            str_arr_product_description = getArguments().getStringArray("product_description");
            str_arr_product_price = getArguments().getStringArray("product_price");

            mpref = getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);
            if (mpref != null)

//        img_product = (ImageView) findViewById(R.id.img_product);
                txt_name_first_letter= (TextView) view.findViewById(R.id.txt_name_first_letter);
            txt_name= (TextView) view.findViewById(R.id.txt_name);
            txt_request_price= (TextView) view.findViewById(R.id.txt_product_price);
            txt_product_description= (TextView) view.findViewById(R.id.txt_description);
            edt_offer_price= (EditText) view.findViewById(R.id.edt_offer_price);
            edt_offer_minutes= (EditText) view.findViewById(R.id.edt_deadline);
            edt_deadline_type= (EditText) view.findViewById(R.id.edt_deadline_type);
            btn_buy_now = (Button) view.findViewById(R.id.btn_buy_now_negotiable);
            txt_product_name= (TextView) view.findViewById(R.id.txt_product_name);
            txt_used_for= (TextView) view.findViewById(R.id.txt_used_for);
            txt_currency_code= (TextView) view.findViewById(R.id.curr_code);
            viewPager = (ViewPager) view.findViewById(R.id.view_pager);
            txt_distance= (TextView) view.findViewById(R.id.txt_distance);
            img_seller_image= (CircleImageView) view.findViewById(R.id.img_seller_image);
            txt_condition= (TextView) view.findViewById(R.id.txt_condition);
            img_play_video= (ImageView) view.findViewById(R.id.img_play_video);

            face= Typeface.createFromAsset(getActivity().getAssets(), "fonts/bariolbd.otf");
//        edt_offer_price.setTypeface(face);
            edt_offer_minutes.setTypeface(face);
            edt_deadline_type.setTypeface(face);

            txt_product_name.setText(str_arr_product_name[mPage]);
            txt_used_for.setText(str_arr_product_used_for[mPage]+" Old");
            txt_request_price.setText(mpref.getString("currency_symbol","")+" "+str_arr_product_price[mPage]);
            txt_product_description.setText(str_arr_product_description[mPage]);
            txt_currency_code.setText(mpref.getString("currency_symbol",""));
            txt_condition.setText(str_arr_product_condition[mPage]);
            txt_distance.setText(Math.round(Double.parseDouble(str_arr_product_distance[mPage]))+" Km(s) away");

            locale = getActivity().getResources().getConfiguration().locale.getDisplayCountry();
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            currentDateTimeString = formatter.format(today);
            timezone= TimeZone.getDefault().getDisplayName();


            try {
                Picasso.with(getActivity())
                        .load(str_arr_product_posted_image[mPage])
                        //.placeholder(R.drawable.user_placeholder) not considering has thumbnails are small size
                        //.error(R.drawable.user_placeholder_error)
//                    .resize(200,200)
                        .into(img_seller_image,new PicassoCallback(str_arr_product_posted_image[mPage]));
            }catch (Exception e){
                Picasso.with(getActivity())
                        .load("http://52.41.70.254/pics/user.jpg")
                        //.placeholder(R.drawable.user_placeholder) not considering has thumbnails are small size
                        //.error(R.drawable.user_placeholder_error)
//                    .resize(200,200)
                        .into(img_seller_image,new PicassoCallback(str_arr_product_posted_image[mPage]));
            }

            para = new HashMap<>();
            para.put("UserId", mpref.getString("user_id",""));
            para.put("ProductId", str_arr_product_id[mPage]);
            para.put("Datetime",currentDateTimeString);
//            progressDialog.setMessage("Loading...");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
            Log.e("products_details", para.toString());
            HttpAsync httpAsync1 = new HttpAsync(getActivity(), listener, Constants.BASE_URL+"ProductDetail", para, 2, "product_details");
            httpAsync1.execute();

        }catch (Exception e){

        }

        return view;
    }

    @Override
    public void onTaskCancelled(String data) {

    }

    @Override
    public void onTaskComplete(String result, String tag) {
//        progressDialog.dismiss();
        if(result.equalsIgnoreCase("fail")){
            try {
                Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_SHORT).show();
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
                                        offer_min_price=volumobject.getInt("NextBidShouldBe");
//                                        Log.e("NextBidShouldBe",offer_min_price+"");
//                                        gallery.add(volumobject.getString("ImageLinks"));
                                        gallery=new ArrayList<String>( Arrays.asList(volumobject.getString("ImageLinks").split("\\s*,\\s*")));
                                        gallery_images=new ArrayList<>(gallery);

                                        str_video_link=volumobject.getString("VideoLinks");
                                        if(str_video_link.equalsIgnoreCase("")){
                                            img_play_video.setVisibility(View.GONE);
                                        }
                                        else {
                                            img_play_video.setVisibility(View.VISIBLE);
                                        }

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
                                            FullImageAdapter adapter = new FullImageAdapter(getActivity(), gallery_images);
                                            viewPager.setAdapter(adapter);


                                            CirclePageIndicator indicator = (CirclePageIndicator)
                                                    view.findViewById(R.id.indicator);

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
                            Toast.makeText(getActivity(),jsonObject.getString("errMsg"),Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(getActivity(),MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            getActivity().finish();
                        }
                        else {
//                            Toast.makeText(getApplicationContext(),jsonObject.getString("errMsg"),Toast.LENGTH_LONG).show();

                            AlertDialog alertDialog=new AlertDialog.Builder(getActivity()).setMessage(jsonObject.getString("errMsg"))
                                    .setCancelable(false)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
//                                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
//                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                            startActivity(intent);
//                                            finish();
                                            dialog.dismiss();
                                        }
                                    }).show();

                        }
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
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
            Picasso.with(getActivity()).load("http://52.41.70.254/pics/user.jpg").into(img_seller_image);
        }
    }

}
