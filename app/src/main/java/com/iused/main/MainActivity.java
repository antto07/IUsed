package com.iused.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iused.R;
import com.iused.adapters.LocationAdapter;
import com.iused.bean.LocationBean;
import com.iused.bean.MainProductsBeanCategories;
import com.iused.dialog.BottomSheetDialogView;
import com.iused.fragments.Donate_Product_Activity;
import com.iused.fragments.Dontated_By_Others_Fragment;
import com.iused.fragments.ExampleFragments;
import com.iused.fragments.DonateFragment;
import com.iused.fragments.OrdersFragments;
import com.iused.fragments.SellFragment;
import com.iused.fragments.WishlistFragment;
import com.iused.introduction.JSONParser;
import com.iused.introduction.LoginActivity;
import com.iused.introduction.RegisterActivity;
import com.iused.introduction.Splash;
import com.iused.utils.AsyncTaskListener;
import com.iused.utils.Constants;
import com.iused.utils.CustomTypefaceSpan;
import com.iused.utils.HttpAsync;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,AsyncTaskListener {

    private FragmentManager fragment_manager = null;

    public String colorprimary="#FF4F5A";
    public String colorblack="#000000";

    private ImageView img_categories= null;
    private ImageView img_sell= null;
    private ImageView img_orders= null;
    private ImageView img_donate= null;
    private ImageView img_wishlist= null;

    private TextView txt_categories= null;
    private TextView txt_sell= null;
    private TextView txt_orders= null;
    private TextView txt_donate= null;
    private TextView txt_wishlist= null;

    private LinearLayout linear_categories= null;
    private LinearLayout linear_sell= null;
    private LinearLayout linear_orders= null;
    private LinearLayout linear_donate= null;
    private LinearLayout linear_wishlist= null;

    private AsyncTaskListener listener= null;
    private HashMap<String, String> parameters = null;
    private HashMap<String, String> parameters_location = null;
    public static ArrayList<MainProductsBeanCategories> mainProductsBeanCategories = null;
    public static ArrayList<String> ar_category_name = new ArrayList<String>();
    public static String[] str_arr_category_name;
    public static ArrayList<String> ar_category_id = new ArrayList<String>();
    public static String[] str_arr_category_id;

    public static ArrayList<String> ar_category_name_share = new ArrayList<String>();
    public static String[] str_arr_category_name_share;
    public static ArrayList<String> ar_category_id_share = new ArrayList<String>();
    public static String[] str_arr_category_id_share;

    private ImageView img_sort= null;
    private int mDayNightMode = AppCompatDelegate.MODE_NIGHT_AUTO;

    public SharedPreferences mpref = null;
    private ProgressDialog progressDialog= null;

    private RelativeLayout layoutLocationFilter = null;
    private Animation scalingAnimation = null;
    private Animation jumpingUp = null;
    private Animation jumpingDown = null;
    private Animation slideUp = null;
    private Animation slideDown = null;
    private Animation slideUp_Location = null;
    private Animation slideDown_Location = null;
    private LinearLayout linear_main=null;
    Menu menu_filter;
    public  static Toolbar toolbar = null;
    public static LinearLayout linear_bottom_layout=null;
    public static FloatingActionButton fab = null;
    private NavigationView navigationView=null;
    private TextView txt_header_name=null;
    private TextView txt_header_email=null;
    private ImageView img_header_photo=null;

    private TextView txt_filter_cancel=null;
    private TextView txt_filter_done=null;
//    private DiscreteSeekBar seekBar_distance=null;
    private TextView txt_start_seekbar_distance=null;
    private TextView txt_seekbar_distance=null;
//    private DiscreteSeekBar seekbar_price=null;
    private SeekBar seekbar_distance_new=null;
    private TextView txt_seekbar_start_price=null;
    private TextView txt_seekbar_price=null;
    private RadioGroup radioGroupMain_sort_by = null;
    private RadioButton radiobutton_sort_by = null;
    private int sort_by_int;
    private RadioGroup radioGroupMain_posted_within = null;
    private RadioButton radiobutton_posted_within = null;
    private int posted_within_int;
    private EditText edt_price_from=null;
    private EditText edt_price_to=null;
    private RelativeLayout layoutLocationSearch = null;
    private EditText edtLocationSearch = null;
    private ListView listLocation = null;
    String Country = "";

    private String url = null;

    private List<String> sugestion = null;
    private List<LocationBean> locationBean = null;
    private Double city_latitude = 0.0;
    private Double city_longitude = 0.0;
    private HashMap<String, String> para_location = null;
    private AsyncTaskListener listener1 = null;

    public static String str_sort_by="";
    public static String str_lower_range_price="";
    public static String str_upper_range_price="";
    public static String str_lower_distance="";
    public static String str_upper_distance="";
    public static String str_posted_in="";
    String short_name = "";
    private TextView txt_location=null;
    private LinearLayout card_location=null;
    private TextView txt_current_location=null;
//    public static SwipeRefreshLayout swipeRefreshLayout;
    private Typeface face=null;
    DrawerLayout drawer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        mpref = getSharedPreferences("user_details", MODE_PRIVATE);
        if (mpref != null)

        fragment_manager = getSupportFragmentManager();
        listener= MainActivity.this;
        progressDialog= new ProgressDialog(MainActivity.this);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
//        toggle.setDrawerIndicatorEnabled(false);


        face= Typeface.createFromAsset(getAssets(), "fonts/bariolreg.otf");
//        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swiperefresh_new);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }

        View header = navigationView.getHeaderView(0);


//        navigationView.addHeaderView(header);
        txt_header_name= (TextView) header.findViewById(R.id.txt_header_username);
        txt_header_email= (TextView) header.findViewById(R.id.txt_header_email);
        img_header_photo= (ImageView) header.findViewById(R.id.img_header_user);

        try {
            txt_header_name.setText(mpref.getString("username",""));
            txt_header_email.setText(mpref.getString("email",""));
        }catch (Exception e){
            txt_header_name.setText("");
            txt_header_email.setText("");
        }

//        Log.e("photo",mpref.getString("photo","")+mpref.getString("username",""));

        try {
            Picasso.with(getApplicationContext())
                    .load(mpref.getString("photo",""))
                    .placeholder(R.drawable.no_image)
                    .fit().centerInside()
                    //.placeholder(R.drawable.user_placeholder) not considering has thumbnails are small size
                    //.error(R.drawable.user_placeholder_error)
                    .into(img_header_photo,new PicassoCallback(mpref.getString("photo","")));
        }catch (Exception e){
            Picasso.with(getApplicationContext())
                    .load("http://52.41.70.254/pics/user.jpg")
                    .placeholder(R.drawable.no_image)
                    .fit().centerInside()
                    //.placeholder(R.drawable.user_placeholder) not considering has thumbnails are small size
                    //.error(R.drawable.user_placeholder_error)
                    .into(img_header_photo,new PicassoCallback("http://52.41.70.254/pics/user.jpg"));
        }

        img_sort= (ImageView) findViewById(R.id.img_sorting);
        img_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogView.show(MainActivity.this, mDayNightMode);
            }
        });

        listener1 = MainActivity.this;
        img_categories= (ImageView) findViewById(R.id.img_icon_categories);
        img_sell= (ImageView) findViewById(R.id.img_icon_sell);
        img_orders= (ImageView) findViewById(R.id.img_icon_orders);
        img_donate= (ImageView) findViewById(R.id.img_icon_donate);
        img_wishlist= (ImageView) findViewById(R.id.img_icon_wishlist);

        txt_categories= (TextView) findViewById(R.id.txt_categories);
        txt_sell= (TextView) findViewById(R.id.txt_sell);
        txt_orders= (TextView) findViewById(R.id.txt_orders);
        txt_donate= (TextView) findViewById(R.id.txt_donate);
        txt_wishlist= (TextView) findViewById(R.id.txt_wishlist);

        linear_categories= (LinearLayout) findViewById(R.id.linear_categories);
        linear_sell= (LinearLayout) findViewById(R.id.linear_sell);
        linear_orders= (LinearLayout) findViewById(R.id.linear_orders);
        linear_donate= (LinearLayout) findViewById(R.id.linear_donate);
        linear_wishlist= (LinearLayout) findViewById(R.id.linear_wishlist);
        linear_main= (LinearLayout) findViewById(R.id.linear_main);
        txt_filter_cancel= (TextView) findViewById(R.id.txt_filter_cancel);
        txt_filter_done= (TextView) findViewById(R.id.txt_filter_done);
        radioGroupMain_posted_within= (RadioGroup) findViewById(R.id.radioGroup_posted_within);
        radioGroupMain_sort_by= (RadioGroup) findViewById(R.id.radioGroup_sort);
//        seekBar_distance= (DiscreteSeekBar) findViewById(R.id.seekBar_distance);
        seekbar_distance_new= (SeekBar) findViewById(R.id.seekbar_distance_new);
//        seekbar_price= (DiscreteSeekBar) findViewById(R.id.seekBar_price);
        txt_seekbar_start_price= (TextView) findViewById(R.id.txt_start_price);
        txt_seekbar_price= (TextView) findViewById(R.id.txt_seekbar_price);
        txt_start_seekbar_distance= (TextView) findViewById(R.id.txt_start_distance);
        txt_seekbar_distance= (TextView) findViewById(R.id.txt_seekbar_distance);
        linear_bottom_layout= (LinearLayout) findViewById(R.id.linear_bottom_layout);
        edt_price_from= (EditText) findViewById(R.id.edt_price_from);
        edt_price_to= (EditText) findViewById(R.id.edt_price_to);
        layoutLocationSearch = (RelativeLayout) findViewById(R.id.layout_location_search);
        edtLocationSearch = (EditText) findViewById(R.id.edt_location);
        listLocation = (ListView) findViewById(R.id.list_location);
        txt_location= (TextView) findViewById(R.id.txt_location);
        card_location= (LinearLayout) findViewById(R.id.card_location);
        txt_current_location= (TextView) findViewById(R.id.txt_current_location);

        layoutLocationFilter = (RelativeLayout) findViewById(R.id.layout_location_filter);
        scalingAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.scaling_logo);
        jumpingUp = AnimationUtils.loadAnimation(MainActivity.this, R.anim.jumping_up);
        jumpingDown = AnimationUtils.loadAnimation(MainActivity.this, R.anim.jumping_down);
        slideUp = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bottom_up);
        slideDown = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bottom_down);
        slideUp_Location = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bottom_up);
        slideDown_Location = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bottom_down);

        try {
            if(mpref.getString("current_location","").equalsIgnoreCase(null)){
                card_location.setVisibility(View.GONE);
//                txt_current_location.setVisibility(View.GONE);
            }
            else {
                txt_location.setText(mpref.getString("current_location",""));
                if(mpref.getString("current_location","").length()>=15){
                    txt_current_location.setText(mpref.getString("current_location","").substring(0,10)+"..");
                }
                else {
                    txt_current_location.setText(mpref.getString("current_location",""));
                }

            }
        }catch (Exception e){
            card_location.setVisibility(View.GONE);
            txt_current_location.setVisibility(View.GONE);
        }

//        txt_current_location.setText(mpref.getString("current_location",""));

//        seekbar_price.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
//            @Override
//            public int transform(int value) {
//                txt_seekbar_price.setText(""+String.valueOf(value));
//                return value;
//            }
//        });

//        seekBar_distance.setProgress(500);
//        seekBar_distance.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
//            @Override
//            public int transform(int value) {
////                txt_seekbar_distance.setText(String.valueOf(value)+"");
//                return value;
//            }
//        });

        seekbar_distance_new.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

//                if(progress<1){
//                    txt_seekbar_distance.setText("1");
//                }
//                else {
                    txt_seekbar_distance.setText(""+progress);
//                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        txt_filter_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtLocationSearch.getWindowToken(), 0);

                posted_within_int = radioGroupMain_posted_within.getCheckedRadioButtonId();
                radiobutton_posted_within = (RadioButton) findViewById(posted_within_int);

                sort_by_int = radioGroupMain_sort_by.getCheckedRadioButtonId();
                radiobutton_sort_by = (RadioButton) findViewById(sort_by_int);

//                Log.e("posted_within",radiobutton_posted_within.getText().toString());
//                Log.e("sort_by",radiobutton_sort_by.getText().toString());

                if (radioGroupMain_sort_by.getCheckedRadioButtonId() == -1){
                    str_sort_by="";
                }
                else {
                    if(radiobutton_sort_by.getText().toString().equalsIgnoreCase("Price: high to low")){
                        str_sort_by="2";
                    }
                    else if(radiobutton_sort_by.getText().toString().equalsIgnoreCase("Price: low to high")){
                        str_sort_by="1";
                    }
                    else if(radiobutton_sort_by.getText().toString().equalsIgnoreCase("Closest First")){
                        str_sort_by="3";
                    }
                    else if(radiobutton_sort_by.getText().toString().equalsIgnoreCase("Newest First")){
                        str_sort_by="4";
                    }
                    else {
                        str_sort_by="";
                    }
                }


                if (radioGroupMain_posted_within.getCheckedRadioButtonId() == -1){
                    str_posted_in="";
                }
                else {
                    if(radiobutton_posted_within.getText().toString().equalsIgnoreCase("The last 24h")){
                        str_posted_in="1";
                    }
                    else if(radiobutton_posted_within.getText().toString().equalsIgnoreCase("The last 7 days")){
                        str_posted_in="7";
                    }
                    else if(radiobutton_posted_within.getText().toString().equalsIgnoreCase("The last 30 days")){
                        str_posted_in="30";
                    }
                    else {
                        str_posted_in="";
                    }

                }

                str_lower_range_price=edt_price_from.getText().toString();
                str_upper_range_price=edt_price_to.getText().toString();
                str_lower_distance=txt_start_seekbar_distance.getText().toString();
                str_upper_distance=txt_seekbar_distance.getText().toString();

                if(str_lower_range_price.equalsIgnoreCase("")&&str_upper_range_price.equalsIgnoreCase("")){
                    layoutLocationFilter.setAnimation(slideDown);
                    layoutLocationFilter.startAnimation(slideDown);
                    linear_main.setVisibility(View.VISIBLE);

                    str_lower_range_price="0";
                    str_upper_range_price="1000000";
                }
                else if(str_upper_range_price.equalsIgnoreCase("")&&!str_upper_range_price.equalsIgnoreCase("")){
                    layoutLocationFilter.setAnimation(slideDown);
                    layoutLocationFilter.startAnimation(slideDown);
                    linear_main.setVisibility(View.VISIBLE);

                    str_lower_range_price="0";
                    str_upper_range_price=edt_price_to.getText().toString();
                }
                else if(!str_upper_range_price.equalsIgnoreCase("")&&str_upper_range_price.equalsIgnoreCase("")){
                    layoutLocationFilter.setAnimation(slideDown);
                    layoutLocationFilter.startAnimation(slideDown);
                    linear_main.setVisibility(View.VISIBLE);

                    str_lower_range_price=edt_price_from.getText().toString();
                    str_upper_range_price="1000000";
                }

                try {

                    if(Long.parseLong(str_lower_range_price)>Long.parseLong(str_upper_range_price)){
                        Toast.makeText(getApplicationContext(),"Minimum price should not be greater than Maximum price",Toast.LENGTH_SHORT).show();
                    }
                    else {

                        layoutLocationFilter.setAnimation(slideDown);
                        layoutLocationFilter.startAnimation(slideDown);
                        linear_main.setVisibility(View.VISIBLE);

                        try {
                            radioGroupMain_sort_by.clearCheck();
                            radioGroupMain_posted_within.clearCheck();
                        }catch (Exception e){

                        }

                        parameters_location = new HashMap<>();
                        parameters_location.put("UserId",mpref.getString("user_id",""));
                        parameters_location.put("Longitude",mpref.getString("user_lang",""));
                        parameters_location.put("Latitude",mpref.getString("user_lat",""));
                        parameters_location.put("CountryCode",mpref.getString("shortname_country",""));
                        Log.e("parameters_location",parameters_location.toString());
                        progressDialog.setMessage("Loading...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        new HttpAsync(getApplicationContext(), listener,Constants.BASE_URL+"UpdateLocation" , parameters_location, 2, "location").execute();

                    }

                }catch (Exception e){
                    layoutLocationFilter.setAnimation(slideDown);
                    layoutLocationFilter.startAnimation(slideDown);
                    linear_main.setVisibility(View.VISIBLE);

                    parameters_location = new HashMap<>();
                    parameters_location.put("UserId",mpref.getString("user_id",""));
                    parameters_location.put("Longitude",mpref.getString("user_lang",""));
                    parameters_location.put("Latitude",mpref.getString("user_lat",""));
                    parameters_location.put("CountryCode",mpref.getString("shortname_country",""));
                    Log.e("parameters_location",parameters_location.toString());
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    new HttpAsync(getApplicationContext(), listener,Constants.BASE_URL+"UpdateLocation" , parameters_location, 2, "location").execute();

                }


            }
        });

        txt_filter_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutLocationFilter.setAnimation(slideDown);
                layoutLocationFilter.startAnimation(slideDown);
                linear_main.setVisibility(View.VISIBLE);

                try {
                    radioGroupMain_sort_by.clearCheck();
                    radioGroupMain_posted_within.clearCheck();
                }catch (Exception e){

                }

            }
        });

        scalingAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                imgFetchingLocation.setAnimation(jumpingUp);
//                imgFetchingLocation.startAnimation(jumpingUp);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        jumpingUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                imgFetchingLocation.setAnimation(jumpingDown);
//                imgFetchingLocation.startAnimation(jumpingDown);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        jumpingDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                imgFetchingLocation.setAnimation(jumpingUp);
//                imgFetchingLocation.startAnimation(jumpingUp);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        slideUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                layoutLocationFilter.setVisibility(View.VISIBLE);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputMethodManager.toggleSoftInputFromWindow(edtLocationSearch.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        slideDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                layoutLocationFilter.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        slideUp_Location.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                layoutLocationSearch.setVisibility(View.VISIBLE);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputMethodManager.toggleSoftInputFromWindow(edtLocationSearch.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        slideDown_Location.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                layoutLocationSearch.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        edtLocationSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + s.toString().replace(" ", "%20") + "&location=&radius=50&sensor=true&key=AIzaSyDhphpswcw1UOEQ9pZRAVzv1DpBQ9if--Y";
                parser parser = new parser();
                parser.execute();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtLocationSearch.getWindowToken(), 0);

                edtLocationSearch.setText("");
                para_location = new HashMap<>();
                para_location.put("placeid",locationBean.get(position).getPlace_id());
                para_location.put("key","AIzaSyDhphpswcw1UOEQ9pZRAVzv1DpBQ9if--Y");
                Log.e("parameters_location",para_location.toString());
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                new HttpAsync(getApplicationContext(), listener1,"https://maps.googleapis.com/maps/api/place/details/json?" , para_location, 1, "city_latitude").execute();

            }
        });


        linear_categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                swipeRefreshLayout.setRefreshing(false);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtLocationSearch.getWindowToken(), 0);
                try {
                    Log.e("entered ", "entered");
                    changeFragment(new ExampleFragments());
                    menu_filter.findItem(R.id.filter).setVisible(true);
                }catch (Exception e){

                }

                img_categories.setImageResource(R.drawable.buy_red);
                img_sell.setImageResource(R.drawable.sell_black);
                img_orders.setImageResource(R.drawable.orders_black);
                img_donate.setImageResource(R.drawable.donate_black);
                img_wishlist.setImageResource(R.drawable.wishlist_gray);

                txt_categories.setTextColor(Color.parseColor(colorprimary));
                txt_sell.setTextColor(Color.parseColor(colorblack));
                txt_orders.setTextColor(Color.parseColor(colorblack));
                txt_donate.setTextColor(Color.parseColor(colorblack));
                txt_wishlist.setTextColor(Color.parseColor(colorblack));
            }
        });

        linear_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(mpref.getString("guest_status","").equalsIgnoreCase("0")){
                    Intent intent = new Intent(getApplicationContext(), Sell_Products_Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
//                    try {
//                        menu_filter.findItem(R.id.filter).setVisible(false);
//                        changeFragment(new Dontated_By_Others_Fragment());
//
//                    }catch (Exception e){
//
//                    }
//                }
//                else {
//                    try {
//                        menu_filter.findItem(R.id.filter).setVisible(false);
//                        changeFragment(new Dontated_By_Others_Fragment());
//
//                    }catch (Exception e){
//
//                    }
//
//                }


//                img_categories.setImageResource(R.drawable.buy_black);
//                img_sell.setImageResource(R.drawable.donate_orange);
//                img_orders.setImageResource(R.drawable.idonate_black);
//                img_donate.setImageResource(R.drawable.settings_black);
//                img_wishlist.setImageResource(R.drawable.wishlist_gray);
//
//                txt_categories.setTextColor(Color.parseColor(colorblack));
//                txt_sell.setTextColor(Color.parseColor(colorprimary));
//                txt_orders.setTextColor(Color.parseColor(colorblack));
//                txt_donate.setTextColor(Color.parseColor(colorblack));
//                txt_wishlist.setTextColor(Color.parseColor(colorblack));
            }
        });

        linear_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                swipeRefreshLayout.setRefreshing(false);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtLocationSearch.getWindowToken(), 0);

                if(mpref.getString("guest_status","").equalsIgnoreCase("0")){
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.putExtra("offer_negotiable","none");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else {
                    try {
                        menu_filter.findItem(R.id.filter).setVisible(false);
                        changeFragment(new OrdersFragments());

                    }catch (Exception e){

                    }

                    img_categories.setImageResource(R.drawable.buy_black);
                    img_sell.setImageResource(R.drawable.sell_black);
                    img_orders.setImageResource(R.drawable.orders_red);
                    img_donate.setImageResource(R.drawable.donate_black);
                    img_wishlist.setImageResource(R.drawable.wishlist_gray);

                    txt_categories.setTextColor(Color.parseColor(colorblack));
                    txt_sell.setTextColor(Color.parseColor(colorblack));
                    txt_orders.setTextColor(Color.parseColor(colorprimary));
                    txt_donate.setTextColor(Color.parseColor(colorblack));
                    txt_wishlist.setTextColor(Color.parseColor(colorblack));
                }

            }
        });

        linear_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtLocationSearch.getWindowToken(), 0);

//                img_categories.setImageResource(R.drawable.buy_black);
//                img_sell.setImageResource(R.drawable.donate_black);
//                img_orders.setImageResource(R.drawable.idonate_black);
//                img_donate.setImageResource(R.drawable.settings_orange);
//                img_wishlist.setImageResource(R.drawable.wishlist_gray);
//
//                txt_categories.setTextColor(Color.parseColor(colorblack));
//                txt_sell.setTextColor(Color.parseColor(colorblack));
//                txt_orders.setTextColor(Color.parseColor(colorblack));
//                txt_donate.setTextColor(Color.parseColor(colorprimary));
//                txt_wishlist.setTextColor(Color.parseColor(colorblack));

//                if(mpref.getString("guest_status","").equalsIgnoreCase("0")){
//                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                    intent.putExtra("offer_negotiable","none");
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//
//                }
//                else {
//                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                }

//                swipeRefreshLayout.setRefreshing(false);
                if(mpref.getString("guest_status","").equalsIgnoreCase("0")){
//                    Intent intent = new Intent(getApplicationContext(), Donate_Product_Activity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
                    try {
                        menu_filter.findItem(R.id.filter).setVisible(false);
                        changeFragment(new Dontated_By_Others_Fragment());

                    }catch (Exception e){

                    }
                }
                else {
                    try {
                        menu_filter.findItem(R.id.filter).setVisible(false);
                        changeFragment(new DonateFragment());

                    }catch (Exception e){

                    }

                }

                img_categories.setImageResource(R.drawable.buy_black);
                img_sell.setImageResource(R.drawable.sell_black);
                img_orders.setImageResource(R.drawable.orders_black);
                img_donate.setImageResource(R.drawable.donate_red);
                img_wishlist.setImageResource(R.drawable.wishlist_gray);

                txt_categories.setTextColor(Color.parseColor(colorblack));
                txt_sell.setTextColor(Color.parseColor(colorblack));
                txt_orders.setTextColor(Color.parseColor(colorblack));
                txt_donate.setTextColor(Color.parseColor(colorprimary));
                txt_wishlist.setTextColor(Color.parseColor(colorblack));

            }
        });

        linear_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                swipeRefreshLayout.setRefreshing(false);
                try {
                    menu_filter.findItem(R.id.filter).setVisible(false);
                    changeFragment(new WishlistFragment());

                }catch (Exception e){

                }

                img_categories.setImageResource(R.drawable.buy_black);
                img_sell.setImageResource(R.drawable.sell_black);
                img_orders.setImageResource(R.drawable.orders_black);
                img_donate.setImageResource(R.drawable.donate_black);
                img_wishlist.setImageResource(R.drawable.wishlist_blue);

                txt_categories.setTextColor(Color.parseColor(colorblack));
                txt_sell.setTextColor(Color.parseColor(colorblack));
                txt_orders.setTextColor(Color.parseColor(colorblack));
                txt_donate.setTextColor(Color.parseColor(colorblack));
                txt_wishlist.setTextColor(Color.parseColor(colorprimary));
            }
        });


//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                parameters_location = new HashMap<>();
//                parameters_location.put("UserId",mpref.getString("user_id",""));
//                parameters_location.put("Longitude",mpref.getString("user_lang",""));
//                parameters_location.put("Latitude",mpref.getString("user_lat",""));
//                Log.e("parameters_location",parameters_location.toString());
//                progressDialog.setMessage("Loading...");
//                progressDialog.setCancelable(false);
//                progressDialog.show();
//                new HttpAsync(getApplicationContext(), listener,"http://54.191.146.243:8088/UpdateLocation" , parameters_location, 2, "location").execute();
//
//            }
//        });


        parameters_location = new HashMap<>();
        parameters_location.put("UserId",mpref.getString("user_id",""));
        parameters_location.put("Longitude",mpref.getString("user_lang",""));
        parameters_location.put("Latitude",mpref.getString("user_lat",""));
        parameters_location.put("CountryCode",mpref.getString("shortname_country",""));
        Log.e("parameters_location",parameters_location.toString());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL+"UpdateLocation" , parameters_location, 2, "location").execute();


//        if (drawer.isDrawerOpen(GravityCompat.START)) {
////            drawer.closeDrawer(GravityCompat.START);
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(edtLocationSearch.getWindowToken(), 0);
//        }

    }

    private void applyFontToMenuItem(MenuItem mi) {
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , face), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if(layoutLocationFilter.getVisibility() == View.VISIBLE){
            layoutLocationFilter.setAnimation(slideDown);
            layoutLocationFilter.startAnimation(slideDown);
            linear_main.setVisibility(View.VISIBLE);
            layoutLocationSearch.setVisibility(View.INVISIBLE);
        }
        else if(layoutLocationSearch.getVisibility() == View.VISIBLE){
            layoutLocationSearch.setAnimation(slideDown);
            layoutLocationSearch.startAnimation(slideDown);
            linear_main.setVisibility(View.VISIBLE);
            layoutLocationFilter.setVisibility(View.INVISIBLE);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menu_filter=menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.filter) {

//            layoutLocationFilter.setAnimation(slideUp);
//            layoutLocationFilter.startAnimation(slideUp);
//            linear_main.setVisibility(View.GONE);
//            layoutLocationSearch.setVisibility(View.GONE);
            Intent intent=new Intent(getApplicationContext(),Activity_Filter.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            return true;
        }
        if(id==R.id.search){
//            swipeRefreshLayout.setRefreshing(false);
//            Intent intent = new Intent(getApplicationContext(), WishlistActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
            layoutLocationSearch.setAnimation(slideUp_Location);
            layoutLocationSearch.startAnimation(slideUp_Location);
            linear_main.setVisibility(View.GONE);
            layoutLocationFilter.setVisibility(View.GONE);
        }
        if(id==R.id.search_items){
            Intent intent = new Intent(getApplicationContext(), WishlistActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
//        swipeRefreshLayout.setRefreshing(false);
        item.setCheckable(false);

        if (id == R.id.nav_camera) {
            // Handle the camera action

            drawer.closeDrawers();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtLocationSearch.getWindowToken(), 0);

            if(layoutLocationFilter.getVisibility() == View.VISIBLE){
                layoutLocationFilter.setAnimation(slideDown);
                layoutLocationFilter.startAnimation(slideDown);
                linear_main.setVisibility(View.VISIBLE);

                try {
                    changeFragment(new ExampleFragments());
                    menu_filter.findItem(R.id.filter).setVisible(true);
                }catch (Exception e){

                }
            }
            else if(layoutLocationSearch.getVisibility() == View.VISIBLE){
                layoutLocationSearch.setAnimation(slideDown);
                layoutLocationSearch.startAnimation(slideDown);
                linear_main.setVisibility(View.VISIBLE);

                try {
                    changeFragment(new ExampleFragments());
                    menu_filter.findItem(R.id.filter).setVisible(true);
                }catch (Exception e){

                }
            }
            else {
                try {
                    changeFragment(new ExampleFragments());
                    menu_filter.findItem(R.id.filter).setVisible(true);
                }catch (Exception e){

                }
            }

            img_categories.setImageResource(R.drawable.buy_red);
            img_sell.setImageResource(R.drawable.sell_black);
            img_orders.setImageResource(R.drawable.orders_black);
            img_donate.setImageResource(R.drawable.donate_black);
            img_wishlist.setImageResource(R.drawable.wishlist_gray);

            txt_categories.setTextColor(Color.parseColor(colorprimary));
            txt_sell.setTextColor(Color.parseColor(colorblack));
            txt_orders.setTextColor(Color.parseColor(colorblack));
            txt_donate.setTextColor(Color.parseColor(colorblack));
            txt_wishlist.setTextColor(Color.parseColor(colorblack));


        } else if (id == R.id.nav_gallery) {

//            if(mpref.getString("guest_status","").equalsIgnoreCase("0")){
//                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//            }
//            else {
                drawer.closeDrawers();
                Intent intent=new Intent(getApplicationContext(),Sell_Products_Activity.class);
//                Intent intent=new Intent(getApplicationContext(),Donate_Product_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);


//            }



//            try {
//                changeFragment(new SellFragment());
//                menu_filter.findItem(R.id.filter).setVisible(false);
//
//            }catch (Exception e){
//
//            }
//
//            img_categories.setImageResource(R.drawable.categories_gray);
//            img_sell.setImageResource(R.drawable.sell_blue);
//            img_orders.setImageResource(R.drawable.orders_gray);
//            img_donate.setImageResource(R.drawable.donate_gray);
//            img_wishlist.setImageResource(R.drawable.wishlist_gray);
//
//            txt_categories.setTextColor(Color.parseColor(colorblack));
//            txt_sell.setTextColor(Color.parseColor(colorprimary));
//            txt_orders.setTextColor(Color.parseColor(colorblack));
//            txt_donate.setTextColor(Color.parseColor(colorblack));
//            txt_wishlist.setTextColor(Color.parseColor(colorblack));
        }
        else if (id == R.id.nav_profile) {
            if(mpref.getString("guest_status","").equalsIgnoreCase("0")){
                drawer.closeDrawers();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("offer_negotiable","none");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
            else {
                drawer.closeDrawers();
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

        }
        else if (id == R.id.nav_slideshow) {
            drawer.closeDrawers();
            Intent intent = new Intent(getApplicationContext(), TermsandConditionsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);


        } else if (id == R.id.nav_manage) {
            drawer.closeDrawers();
            Intent intent = new Intent(getApplicationContext(), FAQsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }
        else if (id == R.id.nav_privacy_policy) {
            drawer.closeDrawers();
            Intent intent = new Intent(getApplicationContext(), PrivacyPolicyActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);



        }


//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    private void applyFontToMenuItem(MenuItem mi) {
//        SpannableString mNewTitle = new SpannableString(mi.getTitle());
//        mNewTitle.setSpan(new CustomTypefaceSpan("" , face), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//        mi.setTitle(mNewTitle);
//    }


    public class parser extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub


            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.getJSONFromUrl(url.toString());
            if (json != null) {
                try {
                    JSONArray contacts = json.getJSONArray("predictions");
                    sugestion = new ArrayList<String>();
                    locationBean = new ArrayList<>();
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String description = c.getString("description");
                        String place_id=c.getString("place_id");
                        LocationBean bn = new LocationBean();
                        JSONArray ja = c.getJSONArray("terms");
                        if (ja.length() >= 3) {
                            bn.setDescription(description);
                            bn.setPlace_id(place_id);
                            bn.setPlace(ja.getJSONObject((0)).getString("value"));
                            bn.setCity(ja.getJSONObject((1)).optString("value"));
                            bn.setState(ja.getJSONObject((2)).optString("value"));
                            bn.setCountry(ja.getJSONObject((ja.length() - 1)).optString("value"));
                            locationBean.add(bn);
                        }
                        sugestion.add(description);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            //			pb.setVisibility(View.GONE);
            try {
                LocationAdapter adapter = new LocationAdapter(MainActivity.this, locationBean);
                listLocation.setAdapter(adapter);
            }catch (Exception e){

            }

        }


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
//            pb.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onTaskCancelled(String data) {

    }

    @Override
    public void onTaskComplete(String result, String tag) {
//        swipeRefreshLayout.setRefreshing(false);

        progressDialog.dismiss();
        if(result.equalsIgnoreCase("fail")){
            try {
                Toast.makeText(getApplicationContext(),"Check your internet connection",Toast.LENGTH_SHORT).show();
            }catch (Exception e){

            }
        }
        else{
            if(tag.equalsIgnoreCase("products")){

                Log.e("response",result.toString());
                mainProductsBeanCategories = new ArrayList<MainProductsBeanCategories>();
                try {

                    JSONObject json = new JSONObject(result);
                    if(json!= null){

                        if(json.getString("errFlag").equalsIgnoreCase("0")){
                            JSONArray jsonsendarr = json.optJSONArray("Categories");

                            if (jsonsendarr.length() > 0){
                                for (int i = 0; i < jsonsendarr.length(); i++){
                                    if (json != null){
                                        MainProductsBeanCategories main_Products_Bean = new MainProductsBeanCategories();
                                        JSONObject volumobject = jsonsendarr.getJSONObject(i);

                                        main_Products_Bean.setId(volumobject.getString("_id"));
                                        main_Products_Bean.setTitle(volumobject.getString("Name"));
//                                    Log.e("title",volumobject.getString("name"));

//                                    JSONObject jo_new = volumobject.getJSONObject("posters");
//                                        main_Products_Bean.setImage(volumobject.getString("coupon_img_file_name"));
//                                    Log.e("image",volumobject.getString("coupon_img_file_name"));

                                        ar_category_name.add("All");
                                        ar_category_name.add(volumobject.getString("Name"));
                                        ArrayList<String> unique = removeDuplicates(ar_category_name);
                                        str_arr_category_name=new String[unique.size()];
                                        str_arr_category_name=unique.toArray(str_arr_category_name);

                                        ar_category_id.add("");
                                        ar_category_id.add(volumobject.getString("_id"));
                                        ArrayList<String> unique1 = removeDuplicates(ar_category_id);
                                        str_arr_category_id=new String[unique1.size()];
                                        str_arr_category_id=unique1.toArray(str_arr_category_id);

                                        ar_category_name_share.add(volumobject.getString("Name"));
                                        ArrayList<String> unique_share = removeDuplicates(ar_category_name_share);
                                        str_arr_category_name_share=new String[unique_share.size()];
                                        str_arr_category_name_share=unique_share.toArray(str_arr_category_name_share);

                                        ar_category_id_share.add(volumobject.getString("_id"));
                                        ArrayList<String> unique1_share = removeDuplicates(ar_category_id_share);
                                        str_arr_category_id_share=new String[unique1_share.size()];
                                        str_arr_category_id_share=unique1_share.toArray(str_arr_category_id_share);

                                        mpref = getSharedPreferences("user_details", MODE_PRIVATE);
                                        SharedPreferences.Editor ed = mpref.edit();
                                        ed.putInt("array_size", str_arr_category_name_share.length);
                                        for(int k=0;k<str_arr_category_name_share.length; k++)
                                            ed.putString("array_" + k, str_arr_category_name_share[k]);

                                        ed.putInt("array_size_id", str_arr_category_id_share.length);
                                        for(int l=0;l<str_arr_category_id_share.length; l++)
                                            ed.putString("array_id" + l, str_arr_category_id_share[l]);

                                        ed.commit();

//                                        List<String> tmpList = Arrays.asList(str_arr_dp_text);
//                                        //create a treeset with the list, which eliminates duplicates
//                                        TreeSet<String> unique = new TreeSet<String>(tmpList);
//
//                                        str_dp_text_final=new String[unique.size()];
//                                        str_dp_text_final = unique.toArray(new String[unique.size()]);

                                        mainProductsBeanCategories.add(main_Products_Bean);

                                    }
                                }

                                if(mainProductsBeanCategories.size()>0){

                                    try {
                                        changeFragment(new ExampleFragments());
                                        linear_main.setVisibility(View.VISIBLE);
                                    }catch (Exception e){
//
                                    }

                                    img_categories.setImageResource(R.drawable.buy_red);
                                    img_sell.setImageResource(R.drawable.sell_black);
                                    img_orders.setImageResource(R.drawable.orders_black);
                                    img_donate.setImageResource(R.drawable.donate_black);
                                    img_wishlist.setImageResource(R.drawable.wishlist_gray);

                                    txt_categories.setTextColor(Color.parseColor(colorprimary));
                                    txt_sell.setTextColor(Color.parseColor(colorblack));
                                    txt_orders.setTextColor(Color.parseColor(colorblack));
                                    txt_donate.setTextColor(Color.parseColor(colorblack));
                                    txt_wishlist.setTextColor(Color.parseColor(colorblack));

//                                    if(mpref.getString("guest_status","").equalsIgnoreCase("0")){
////                    Intent intent = new Intent(getApplicationContext(), Donate_Product_Activity.class);
////                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                    startActivity(intent);
//                                        try {
//                                            menu_filter.findItem(R.id.filter).setVisible(false);
//                                            changeFragment(new Dontated_By_Others_Fragment());
//
//                                            img_categories.setImageResource(R.drawable.buy_black);
//                                            img_sell.setImageResource(R.drawable.donate_orange);
//                                            img_orders.setImageResource(R.drawable.idonate_black);
//                                            img_donate.setImageResource(R.drawable.settings_black);
//                                            img_wishlist.setImageResource(R.drawable.wishlist_gray);
//
//                                            txt_categories.setTextColor(Color.parseColor(colorblack));
//                                            txt_sell.setTextColor(Color.parseColor(colorprimary));
//                                            txt_orders.setTextColor(Color.parseColor(colorblack));
//                                            txt_donate.setTextColor(Color.parseColor(colorblack));
//                                            txt_wishlist.setTextColor(Color.parseColor(colorblack));
//
//                                        }catch (Exception e){
//
//                                        }
//                                    }
//                                    else {
//                                        try {
//                                            linear_main.setVisibility(View.VISIBLE);
//                                            menu_filter.findItem(R.id.filter).setVisible(false);
//                                            changeFragment(new Dontated_By_Others_Fragment());
//
//                                            img_categories.setImageResource(R.drawable.buy_black);
//                                            img_sell.setImageResource(R.drawable.donate_orange);
//                                            img_orders.setImageResource(R.drawable.idonate_black);
//                                            img_donate.setImageResource(R.drawable.settings_black);
//                                            img_wishlist.setImageResource(R.drawable.wishlist_gray);
//
//                                            txt_categories.setTextColor(Color.parseColor(colorblack));
//                                            txt_sell.setTextColor(Color.parseColor(colorprimary));
//                                            txt_orders.setTextColor(Color.parseColor(colorblack));
//                                            txt_donate.setTextColor(Color.parseColor(colorblack));
//                                            txt_wishlist.setTextColor(Color.parseColor(colorblack));
//
//                                        }catch (Exception e){
//
//                                        }
//
//                                    }

                                }
                                else {
                                    if(mpref.getString("guest_status","").equalsIgnoreCase("0")){
//                    Intent intent = new Intent(getApplicationContext(), Donate_Product_Activity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
                                        try {
                                            menu_filter.findItem(R.id.filter).setVisible(false);
                                            changeFragment(new Dontated_By_Others_Fragment());

                                            img_categories.setImageResource(R.drawable.buy_black);
                                            img_sell.setImageResource(R.drawable.donate_orange);
                                            img_orders.setImageResource(R.drawable.idonate_black);
                                            img_donate.setImageResource(R.drawable.settings_black);
                                            img_wishlist.setImageResource(R.drawable.wishlist_gray);

                                            txt_categories.setTextColor(Color.parseColor(colorblack));
                                            txt_sell.setTextColor(Color.parseColor(colorprimary));
                                            txt_orders.setTextColor(Color.parseColor(colorblack));
                                            txt_donate.setTextColor(Color.parseColor(colorblack));
                                            txt_wishlist.setTextColor(Color.parseColor(colorblack));

                                        }catch (Exception e){

                                        }
                                    }
                                    else {
                                        try {
                                            menu_filter.findItem(R.id.filter).setVisible(false);
                                            changeFragment(new Dontated_By_Others_Fragment());

                                            img_categories.setImageResource(R.drawable.buy_black);
                                            img_sell.setImageResource(R.drawable.donate_orange);
                                            img_orders.setImageResource(R.drawable.idonate_black);
                                            img_donate.setImageResource(R.drawable.settings_black);
                                            img_wishlist.setImageResource(R.drawable.wishlist_gray);

                                            txt_categories.setTextColor(Color.parseColor(colorblack));
                                            txt_sell.setTextColor(Color.parseColor(colorprimary));
                                            txt_orders.setTextColor(Color.parseColor(colorblack));
                                            txt_donate.setTextColor(Color.parseColor(colorblack));
                                            txt_wishlist.setTextColor(Color.parseColor(colorblack));

                                        }catch (Exception e){

                                        }

                                    }
                                }


                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(),json.getString("errMsg"),Toast.LENGTH_LONG).show();
                        }


                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

//                adapter=new ProductMainAdapter(getActivity(),mainProductsBeanCategories);
//                grid_item.setAdapter(adapter);
            }
            else if(tag.equalsIgnoreCase("location")){
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    if (jsonObject != null){
                        if(jsonObject.getString("errFlag").equalsIgnoreCase("0")){

                            img_categories.setImageResource(R.drawable.buy_red);
                            img_sell.setImageResource(R.drawable.sell_black);
                            img_orders.setImageResource(R.drawable.orders_black);
                            img_donate.setImageResource(R.drawable.donate_black);
                            img_wishlist.setImageResource(R.drawable.wishlist_gray);

                            txt_categories.setTextColor(Color.parseColor(colorprimary));
                            txt_sell.setTextColor(Color.parseColor(colorblack));
                            txt_orders.setTextColor(Color.parseColor(colorblack));
                            txt_donate.setTextColor(Color.parseColor(colorblack));
                            txt_wishlist.setTextColor(Color.parseColor(colorblack));
//                            img_categories.setImageResource(R.drawable.buy_red);
//                            img_sell.setImageResource(R.drawable.sell_black);
//                            img_orders.setImageResource(R.drawable.orders_black);
//                            img_donate.setImageResource(R.drawable.donate_black);
//                            img_wishlist.setImageResource(R.drawable.wishlist_gray);
//
//                            txt_categories.setTextColor(Color.parseColor(colorprimary));
//                            txt_sell.setTextColor(Color.parseColor(colorblack));
//                            txt_orders.setTextColor(Color.parseColor(colorblack));
//                            txt_donate.setTextColor(Color.parseColor(colorblack));
//                            txt_wishlist.setTextColor(Color.parseColor(colorblack));

                            mpref = getSharedPreferences("user_details", MODE_PRIVATE);
                            SharedPreferences.Editor ed = mpref.edit();
                            ed.putString("currency_code", jsonObject.getString("Currency"));
                            ed.putString("currency_symbol", jsonObject.getString("CurrencySymbole"));
                            ed.putString("country_code", jsonObject.getString("CountryCode"));
                            ed.commit();

                            Log.e("loc_success",jsonObject.getString("errMsg"));
                            Log.e("currency_code",jsonObject.getString("Currency"));
                            Log.e("currency_symbol",jsonObject.getString("CurrencySymbole"));
                            Log.e("country_code",jsonObject.getString("CountryCode"));
//                            Toast.makeText(getApplicationContext(),jsonObject.getString("errMsg"),Toast.LENGTH_LONG).show();
                            Log.e("loc_success",jsonObject.getString("errMsg"));
                            parameters = new HashMap<>();
                            progressDialog.setMessage("Loading...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            new HttpAsync(getApplicationContext(), listener,Constants.BASE_URL+"GetCategories" , parameters, 2, "products").execute();


                        }
                        else {
                            Toast.makeText(getApplicationContext(),jsonObject.getString("errMsg"),Toast.LENGTH_LONG).show();
                        }
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            else if (tag.equalsIgnoreCase("city_latitude")) {
                try {
                    JSONObject job1 = new JSONObject(result);
                    if (job1 != null) {
                        JSONObject job2 = job1.getJSONObject("result");
                        JSONArray jsonArray = job2.getJSONArray("address_components");
//                    JSONObject jsonObject = jsonArray.getJSONObject(0);
//                    JSONArray jsonArray1 = jsonObject.getJSONArray("types");
//                    if (jsonArray1.length() > 0) {
//                        for (int j = 0; j < jsonArray1.length(); j++) {
//                            String str = jsonArray1.getString(j);
//                            if (str.equalsIgnoreCase("country")) {
//                                curr = jsonObject.getString("short_name");
//                                Log.e("curr",curr);
////                                try {
////                                    ar_dp_text = new ArrayList<String>();
////                                    ar_dp_text.add(curr);
////                                    str_arr_dp_text=new String[ar_dp_text.size()];
////                                    str_arr_dp_text=ar_dp_text.toArray(str_arr_dp_text);
////                                }catch (Exception e){
////
////                                }
//                            }
//                        }
//                    }

                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject zero2 = jsonArray.getJSONObject(i);
                            String long_name = zero2.getString("long_name");
                            short_name = zero2.getString("short_name");
                            JSONArray mtypes = zero2.getJSONArray("types");
                            String Type = mtypes.getString(0);

                            if (Type.equalsIgnoreCase("country")) {
                                Country = short_name;
                                short_name=Country;
                            Log.e("shortname",short_name);

                                mpref = getSharedPreferences("user_details", MODE_PRIVATE);
                                SharedPreferences.Editor ed = mpref.edit();
                                ed.putString("shortname_country", short_name);
                                ed.commit();
                            }
                            else if (Type.equalsIgnoreCase("locality")) {
//                                Country = short_name;
//                                Log.e("locality",zero2.getString("long_name"));
                                mpref = getSharedPreferences("user_details", MODE_PRIVATE);
                                SharedPreferences.Editor ed = mpref.edit();
                                ed.putString("current_location", zero2.getString("long_name"));
                                ed.commit();
                            }

                        }


//                        para_curr = new HashMap<>();
//                        para_curr.put("cn_code",Country);
//                        Log.e("para_country",Country);
//                        HttpAsync httpAsync = new HttpAsync(getApplicationContext(),asyncTaskListener,curr_url,para_curr,1,"curr");
//                        httpAsync.execute();
//                    if(jsonArray.length()>0){
//                        JSONObject jsonObject = job2.getJSONObject(3);
//                        String add = jsonObject.getString("formatted_address");
//                        Log.e("add",add);
//                        Log.e("location_name",job2.getString("name"));
                        JSONObject jsonObject1 = job2.getJSONObject("geometry");
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("location");
                        try {
                            city_latitude = Double.valueOf(jsonObject2.getString("lat"));
                            city_longitude = Double.valueOf(jsonObject2.getString("lng"));

                            layoutLocationSearch.setAnimation(slideDown);
                            layoutLocationSearch.startAnimation(slideDown);

                            mpref = getSharedPreferences("user_details", MODE_PRIVATE);
                            SharedPreferences.Editor ed = mpref.edit();
                            ed.putString("user_lat", city_latitude.toString());
                            ed.putString("user_lang", city_longitude.toString());
//                            ed.putString("imei", imei);
                            ed.commit();

                            parameters_location = new HashMap<>();
                            parameters_location.put("UserId",mpref.getString("user_id",""));
                            parameters_location.put("Longitude",mpref.getString("user_lang",""));
                            parameters_location.put("Latitude",mpref.getString("user_lat",""));
                            parameters_location.put("CountryCode",mpref.getString("shortname_country",""));
                            Log.e("parameters_location_cha",parameters_location.toString());
                            progressDialog.setMessage("Loading...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            new HttpAsync(getApplicationContext(), listener,Constants.BASE_URL+"UpdateLocation" , parameters_location, 2, "location").execute();


                            try {
                                if(mpref.getString("current_location","").equalsIgnoreCase("")){
                                    card_location.setVisibility(View.GONE);
                                }
                                else {
                                    txt_location.setText(job2.getString("name"));
                                    if(job2.getString("name").length()>15){
                                        txt_current_location.setText(job2.getString("name").substring(0,10)+"..");
                                    }
                                    else {
                                        txt_current_location.setText(job2.getString("name"));
                                    }

                                }
                            }catch (Exception e){
                                card_location.setVisibility(View.GONE);
                            }

//                            nextActivity();
                        }catch (Exception e){
                            city_latitude = 12.8989;
                            city_longitude = 77.7878;

                            layoutLocationSearch.setAnimation(slideDown);
                            layoutLocationSearch.startAnimation(slideDown);

                            mpref = getSharedPreferences("user_details", MODE_PRIVATE);
                            SharedPreferences.Editor ed = mpref.edit();
                            ed.putString("user_lat", "12.8989");
                            ed.putString("user_lang", "77.7878");
//                            ed.putString("imei", imei);
                            ed.commit();

                            parameters_location = new HashMap<>();
                            parameters_location.put("UserId",mpref.getString("user_id",""));
                            parameters_location.put("Longitude",mpref.getString("user_lang",""));
                            parameters_location.put("Latitude",mpref.getString("user_lat",""));
                            parameters_location.put("CountryCode",short_name);
                            Log.e("parameters_location_exc",parameters_location.toString());
                            progressDialog.setMessage("Loading...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            new HttpAsync(getApplicationContext(), listener,Constants.BASE_URL+"UpdateLocation" , parameters_location, 2, "location").execute();

//                            nextActivity();
                        }


//                        Log.e("latlng", String.valueOf(latitude)+","+String.valueOf(longitude));
//                    }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    static ArrayList<String> removeDuplicates(ArrayList<String> list) {

        // Store unique items in result.
        ArrayList<String> result = new ArrayList<>();

        // Record encountered Strings in HashSet.
        HashSet<String> set = new HashSet<>();

        // Loop over argument list.
        for (String item : list) {

            // If String is not in set, add it to the list and the set.
            if (!set.contains(item)) {
                result.add(item);
                set.add(item);
            }
        }
        return result;
    }

    public void changeFragment(Fragment fragment){
        try {
            Bundle bundle = new Bundle();
            bundle.putStringArray("pages", str_arr_category_name);
            bundle.putStringArray("ids", str_arr_category_id);
            fragment.setArguments(bundle);
            fragment_manager.beginTransaction().replace(R.id.fragments, fragment).commit();

        }catch (Exception e){
            e.printStackTrace();
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
            Picasso.with(getApplicationContext()).load("http://52.41.70.254/pics/user.jpg").into(img_header_photo);
        }
    }
}
