package com.iused.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iused.R;

import com.iused.bean.DonatedByOthersBean;
import com.iused.bean.MainProductsBean;
import com.iused.bean.WishListProductsBeanDonations;
import com.iused.bean.WishListProductsBeanNegotiable;
import com.iused.bean.WishListProductsBeanNonNegotiable;
import com.iused.fragments.FaqPagerAdapter;
import com.iused.fragments.Wishlist_Compete_Fragment;
import com.iused.fragments.Wishlist_Donations_Fragment;
import com.iused.fragments.Wishlist_Fixedprice_Fragment;
import com.iused.utils.AsyncTaskListener;
import com.iused.utils.Constants;
import com.iused.utils.HttpAsync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Lenovo on 02/11/2016.
 */
public class WishlistActivity extends AppCompatActivity implements AsyncTaskListener {

    public SharedPreferences mpref = null;
    protected Context context = null;
    private PagerAdapter faq_adapter = null;
    private ViewPager faq_viewPager = null;
    private HashMap<String, String> para = null;
    private AsyncTaskListener listener = null;
    TabLayout tabLayout = null;
    private ProgressDialog progressDialog= null;
    private TextView txt_search_items=null;

    public static ArrayList<MainProductsBean> wishlistProductsBeanNegotiable = null;
    public static ArrayList<MainProductsBean> wishlistProductsBeanNonNegotiable = null;
    public static ArrayList<DonatedByOthersBean> wishlistProductsBeanDonations = null;

    SearchView searchViewAndroidActionBar;
    public static Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_wishlist);

        toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search For Products");

        tabLayout = (TabLayout) findViewById(R.id.faqtab_layout);

//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        context = getApplicationContext();
        progressDialog= new ProgressDialog(WishlistActivity.this);

        faq_viewPager = (ViewPager) findViewById(R.id.faq_pager);
        txt_search_items= (TextView) findViewById(R.id.txt_search_items);


        wishlistProductsBeanNegotiable = new ArrayList<MainProductsBean>();
        wishlistProductsBeanNonNegotiable = new ArrayList<MainProductsBean>();
        wishlistProductsBeanDonations = new ArrayList<DonatedByOthersBean>();


        mpref = getSharedPreferences("user_details", MODE_PRIVATE);
        if (mpref != null)
            listener = WishlistActivity.this;
        para = new HashMap<>();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchViewAndroidActionBar.setQueryHint("Enter item name");
        searchViewAndroidActionBar.setIconifiedByDefault(false);
        searchViewAndroidActionBar.requestFocus();

//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(searchViewAndroidActionBar, InputMethodManager.SHOW_IMPLICIT);

        searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewAndroidActionBar.clearFocus();
                Log.e("query", query);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchViewAndroidActionBar.getWindowToken(), 0);

                para = new HashMap<>();
                para.put("UserId", mpref.getString("user_id", ""));
                para.put("WishList", query);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                HttpAsync httpAsync1 = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL+"UpdateWishList", para, 2, "products");
                httpAsync1.execute();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchViewAndroidActionBar.getWindowToken(), 0);
            searchViewAndroidActionBar.clearFocus();
        }catch (Exception e){

        }

    }

    @Override
    public void onTaskCancelled(String data) {

    }

    @Override
    public void onTaskComplete(String result, String tag) {

        progressDialog.dismiss();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchViewAndroidActionBar.getWindowToken(), 0);
        faq_viewPager.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.VISIBLE);
        txt_search_items.setVisibility(View.GONE);

        if (result.equalsIgnoreCase("fail")) {
            try {
                Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
            }catch (Exception e){

            }
        } else {
            if (tag.equalsIgnoreCase("products")) {
                JSONObject jsonObject = null;

                wishlistProductsBeanNegotiable.clear();
                wishlistProductsBeanNonNegotiable.clear();
                wishlistProductsBeanDonations.clear();

                tabLayout.removeAllTabs();
                tabLayout.addTab(tabLayout.newTab().setText("Compete"));
                tabLayout.addTab(tabLayout.newTab().setText("Fixed Price"));
                tabLayout.addTab(tabLayout.newTab().setText("Donations"));


                try {

                    jsonObject = new JSONObject(result);
                    if (jsonObject != null) {
                        if (jsonObject.getString("errFlag").equalsIgnoreCase("0")) {

//                            tabLayout.removeAllTabs();
//                            tabLayout.refreshDrawableState();
//                            faq_adapter = new FaqPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());


//                            faq_adapter = new FaqPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
//                            faq_viewPager.setAdapter(faq_adapter);
//                            faq_viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//                            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//                                @Override
//                                public void onTabSelected(TabLayout.Tab tab) {
//                                    faq_viewPager.setCurrentItem(tab.getPosition());
//
//                                }
//
//                                @Override
//                                public void onTabUnselected(TabLayout.Tab tab) {
//
//                                }
//
//                                @Override
//                                public void onTabReselected(TabLayout.Tab tab) {
//
//                                }
//                            });


                            JSONArray jsonsendarr = jsonObject.optJSONArray("Negotiable");
                            if (jsonsendarr.length() > 0) {

                                for (int i = 0; i < jsonsendarr.length(); i++) {
                                    if (jsonObject != null) {

                                        MainProductsBean main_Products_Bean = new MainProductsBean();
                                        JSONObject volumobject = jsonsendarr.getJSONObject(i);

                                        main_Products_Bean.setUsedFor(volumobject.getString("UsedFor"));
                                        main_Products_Bean.setCondition(volumobject.getString("Condition"));
                                        main_Products_Bean.setPostedBy(volumobject.getString("PostedBy"));
                                        main_Products_Bean.setProductName(volumobject.getString("ProductName"));
//                                        main_Products_Bean.setPostedByImage(volumobject.getString("PostedByImage"));
                                        main_Products_Bean.setDistance(volumobject.getString("Distance"));
                                        main_Products_Bean.setImage(volumobject.getString("Image"));
                                        main_Products_Bean.setPrice(volumobject.getString("Price"));
                                        main_Products_Bean.setQtyRemaining(volumobject.getString("QtyRemaining"));
                                        main_Products_Bean.setFavorite(volumobject.getString("Favorite"));
                                        main_Products_Bean.setDescription(volumobject.getString("Description"));
                                        main_Products_Bean.setType(volumobject.getString("Type"));
                                        main_Products_Bean.setProductId(volumobject.getString("ProductId"));

//                                        str_type_1 = volumobject.getString("Type");

                                        wishlistProductsBeanNegotiable.add(main_Products_Bean);

                                        faq_adapter = new FaqPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
                                        faq_viewPager.setAdapter(faq_adapter);
                                        faq_viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                                        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                            @Override
                                            public void onTabSelected(TabLayout.Tab tab) {
                                                faq_viewPager.setCurrentItem(tab.getPosition());

                                            }

                                            @Override
                                            public void onTabUnselected(TabLayout.Tab tab) {

                                            }

                                            @Override
                                            public void onTabReselected(TabLayout.Tab tab) {

                                            }
                                        });

                                    }
                                }

                            }
                            else{
                                try {
                                    Wishlist_Compete_Fragment.adapter.notifyDataSetChanged();
                                    Wishlist_Compete_Fragment.recyclerView_compete.setVisibility(View.GONE);
                                    Wishlist_Compete_Fragment.txt_no_items.setVisibility(View.VISIBLE);
                                }catch (Exception e){
//                                    Wishlist_Compete_Fragment.adapter.notifyDataSetChanged();
//                                    Wishlist_Compete_Fragment.recyclerView_compete.setVisibility(View.GONE);
//                                    Wishlist_Compete_Fragment.txt_no_items.setVisibility(View.VISIBLE);
                                    faq_viewPager.setVisibility(View.GONE);
                                    txt_search_items.setVisibility(View.VISIBLE);
                                }

                            }

                            JSONArray jsonsendarr1 = jsonObject.optJSONArray("NonNegotiable");
                            if (jsonsendarr1.length() > 0) {

                                Log.e("non_neg_length", jsonsendarr1.length() + " ");
                                for (int i = 0; i < jsonsendarr1.length(); i++) {
                                    if (jsonObject != null) {
                                        MainProductsBean main_Products_Bean = new MainProductsBean();
                                        JSONObject volumobject = jsonsendarr1.getJSONObject(i);

                                        main_Products_Bean.setUsedFor(volumobject.getString("UsedFor"));
                                        main_Products_Bean.setCondition(volumobject.getString("Condition"));
                                        main_Products_Bean.setPostedBy(volumobject.getString("PostedBy"));
                                        main_Products_Bean.setProductName(volumobject.getString("ProductName"));
//                                        main_Products_Bean.setPostedByImage(volumobject.getString("PostedByImage"));
                                        main_Products_Bean.setDistance(volumobject.getString("Distance"));
                                        main_Products_Bean.setImage(volumobject.getString("Image"));
                                        main_Products_Bean.setPrice(volumobject.getString("Price"));
                                        main_Products_Bean.setQtyRemaining(volumobject.getString("QtyRemaining"));
                                        main_Products_Bean.setFavorite(volumobject.getString("Favorite"));
                                        main_Products_Bean.setDescription(volumobject.getString("Description"));
                                        main_Products_Bean.setType(volumobject.getString("Type"));
                                        main_Products_Bean.setProductId(volumobject.getString("ProductId"));

//                                        str_type_2 = volumobject.getString("Type");

                                        wishlistProductsBeanNonNegotiable.add(main_Products_Bean);
//                                        mainProductsBean.add(main_Products_Bean);

                                        faq_adapter = new FaqPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
                                        faq_viewPager.setAdapter(faq_adapter);
                                        faq_viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                                        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                            @Override
                                            public void onTabSelected(TabLayout.Tab tab) {
                                                faq_viewPager.setCurrentItem(tab.getPosition());

                                            }

                                            @Override
                                            public void onTabUnselected(TabLayout.Tab tab) {

                                            }

                                            @Override
                                            public void onTabReselected(TabLayout.Tab tab) {

                                            }
                                        });
                                    }
                                }

                            }
                            else {
                                try {
                                    Wishlist_Fixedprice_Fragment.adapter.notifyDataSetChanged();
                                    Wishlist_Fixedprice_Fragment.recyclerView_fixed_price.setVisibility(View.GONE);
                                    Wishlist_Fixedprice_Fragment.txt_no_items.setVisibility(View.VISIBLE);
                                }catch (Exception e){
//                                    Wishlist_Fixedprice_Fragment.adapter.notifyDataSetChanged();
//                                    Wishlist_Fixedprice_Fragment.recyclerView_fixed_price.setVisibility(View.GONE);
//                                    Wishlist_Fixedprice_Fragment.txt_no_items.setVisibility(View.VISIBLE);
                                    faq_viewPager.setVisibility(View.GONE);
                                    txt_search_items.setVisibility(View.VISIBLE);
                                }

                            }

                            JSONArray jsonsendarr2 = jsonObject.optJSONArray("Donation");
                            if (jsonsendarr2.length() > 0) {

                                Log.e("donation", jsonsendarr2.length() + " ");
                                for (int i = 0; i < jsonsendarr2.length(); i++) {
                                    if (jsonObject != null) {
                                        DonatedByOthersBean ordered_Products_Bean=new DonatedByOthersBean();
                                        JSONObject volumobject = jsonsendarr2.getJSONObject(i);

                                        ordered_Products_Bean.setUsedFor(volumobject.getString("UsedFor"));
                                        ordered_Products_Bean.setPostedBy(volumobject.getString("PostedBy"));
//                                        ordered_Products_Bean.setPostedByUserName(volumobject.getString("PostedByUserName"));
//                                        ordered_Products_Bean.setPostedByPhone(volumobject.getString("PostedByPhone"));
                                        ordered_Products_Bean.setCondition(volumobject.getString("Condition"));
                                        ordered_Products_Bean.setProductName(volumobject.getString("ProductName"));
//                                        ordered_Products_Bean.setPostedByImage(volumobject.getString("PostedByImage"));
                                        ordered_Products_Bean.setDistance(volumobject.getString("Distance"));
                                        ordered_Products_Bean.setImage(volumobject.getString("Image"));
                                        ordered_Products_Bean.setPrice(volumobject.getString("Price"));
//                                        ordered_Products_Bean.setQtyRemaining(volumobject.getString("QtyRemaining"));
//                                        ordered_Products_Bean.setFavorite(volumobject.getString("Favorite"));
                                        ordered_Products_Bean.setDescription(volumobject.getString("Description"));
                                        ordered_Products_Bean.setType(volumobject.getString("Type"));
                                        ordered_Products_Bean.setProductId(volumobject.getString("ProductId"));

//                                        str_type_2 = volumobject.getString("Type");

                                        wishlistProductsBeanDonations.add(ordered_Products_Bean);
//                                        mainProductsBean.add(main_Products_Bean);

                                        faq_adapter = new FaqPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
                                        faq_viewPager.setAdapter(faq_adapter);
                                        faq_viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                                        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                            @Override
                                            public void onTabSelected(TabLayout.Tab tab) {
                                                faq_viewPager.setCurrentItem(tab.getPosition());

                                            }

                                            @Override
                                            public void onTabUnselected(TabLayout.Tab tab) {

                                            }

                                            @Override
                                            public void onTabReselected(TabLayout.Tab tab) {

                                            }
                                        });
                                    }
                                }

                            }
                            else {
//                                Wishlist_Donations_Fragment.adapter.notifyDataSetChanged();
//                                Wishlist_Donations_Fragment.recyclerView_donations.setVisibility(View.GONE);
//                                Wishlist_Donations_Fragment.txt_no_items.setVisibility(View.VISIBLE);
                                faq_viewPager.setVisibility(View.GONE);
                                txt_search_items.setVisibility(View.VISIBLE);
                            }

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
