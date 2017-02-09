package com.iused.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.iused.R;
import com.iused.adapters.ProductMainAdapterNonNegotiable;
import com.iused.bean.MainProductsBean;
import com.iused.introduction.LoginActivity;
import com.iused.main.MainActivity;
import com.iused.adapters.ProductMainAdapter;
import com.iused.main.ProductDetailsActivity_Negotiable;
import com.iused.main.ProductDetailsActivity_Negotiable_Viewpager;
import com.iused.main.ProductDetailsActivity_Non_Negotiable;
import com.iused.main.Sell_Products_Activity;
import com.iused.utils.AsyncTaskListener;
import com.iused.utils.Constants;
import com.iused.utils.EndlessRecyclerOnScrollListener;
import com.iused.utils.HidingScrollListener;
import com.iused.utils.HttpAsync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Antto on 23-08-2016.
 */
public class PageFragment extends Fragment implements AsyncTaskListener {

    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    public static String str_title;
    private SharedPreferences pref = null;
    private Context context;
    public static String[] str_arr_dp_text;

    public static LinearLayout linear_search_box = null;
    public static Snackbar snackbar = null;
    SharedPreferences prefs = null;

    private RecyclerView grid_item = null;
    private RecyclerView grid_item_non_negotiable = null;

    public static List<String> ar_dp_text = new ArrayList<String>();
    public static String[] str_arr_dp_text_final;
    View view = null;
    private ArrayList<MainProductsBean> mainProductsBean = null;
    private ArrayList<MainProductsBean> mainProductsBean_non_negotiable = null;
    ProductMainAdapter adapter;
    ProductMainAdapterNonNegotiable adapter_non_negotiable;

    private HashMap<String, String> para = null;
    private AsyncTaskListener listener = null;
    private RadioButton rad_negotiable = null;
    private RadioButton rad_non_negotiable = null;
    private String str_type_1 = null;
    private String str_type_2 = null;
    public static String str_sort_by = "";
    public static String str_posted_within = "";
    private String[] titles = null;
    private String[] ids = null;

    private LinearLayout linear_compete=null;
    private LinearLayout linear_fixed_price=null;
    private TextView txt_compete=null;
    private TextView txt_fixed_price=null;
    private LinearLayout linear_compete_down=null;
    private LinearLayout linear_fixed_price_down=null;

    public SharedPreferences mpref = null;
    public int int_negotiable=0;
    public int int_non_negotiable=0;

    private GridLayoutManager mLayoutManager;
    private TextView txt_sell_product=null;
    private LinearLayout linear_product=null;
    private SwipeRefreshLayout swipeRefreshLayout_compete=null;
    private SwipeRefreshLayout swipeRefreshLayout_fixed=null;
    private SwipeRefreshLayout swipeRefreshLayout_no_products=null;

    public static ArrayList<String> arrayList_negotiable_product_id=new ArrayList<String>();
    public static ArrayList<String> arrayList_unique_negotiable_product_id=null;
    public static ArrayList<String> arrayList_negotiable_product_name=new ArrayList<String>();
    public static ArrayList<String> arrayList_unique_negotiable_product_name=null;
    public static ArrayList<String> arrayList_negotiable_used_for=new ArrayList<String>();
    public static ArrayList<String> arrayList_unique_negotiable_used_for=null;
    public static ArrayList<String> arrayList_negotiable_description=new ArrayList<String>();
    public static ArrayList<String> arrayList_unique_negotiable_description=null;
    public static ArrayList<String> arrayList_negotiable_price=new ArrayList<String>();
    public static ArrayList<String> arrayList_unique_negotiable_price=null;
    public static ArrayList<String> arrayList_negotiable_condition=new ArrayList<String>();
    public static ArrayList<String> arrayList_unique_negotiable_condition=null;
    public static ArrayList<String> arrayList_negotiable_distance=new ArrayList<String>();
    public static ArrayList<String> arrayList_unique_negotiable_distance=null;
    public static ArrayList<String> arrayList_negotiable_posted_image=new ArrayList<String>();
    public static ArrayList<String> arrayList_unique_negotiable_posted_image=null;


    private ArrayList<String> arrayList_non_negotiable_id=new ArrayList<String>();
    ArrayList<String> arrayList_unique_non_negotiable=null;
    public static String[] str_arr_product_id_negotiable;
    public static String[] str_arr_product_id_non_negotiable;

    // on scroll

    private static int current_page = 0;

    private int ival = 1;
    private int loadLimit = 1;

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_tab, container, false);

        mPage = getArguments().getInt(ARG_PAGE);
        titles = getArguments().getStringArray("title");
        ids = getArguments().getStringArray("ids");

        context = getActivity();
        listener = PageFragment.this;

        mpref = getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);
        if (mpref != null)
//            Log.e("iddddd_user",mpref.getString("user_id", ""));

            pref = context.getSharedPreferences("preferencename", Context.MODE_PRIVATE);
        int size = pref.getInt("array_size", 0);
//        Log.e("dp_arr_length",size+"");
        str_arr_dp_text = new String[size];
        for (int i = 0; i < size; i++) {
            String str_arr = pref.getString("array_" + i, null);

            ar_dp_text.add(str_arr);
            str_arr_dp_text_final = new String[ar_dp_text.size()];
            str_arr_dp_text_final = ar_dp_text.toArray(str_arr_dp_text_final);

        }

        swipeRefreshLayout_compete= (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh_compete);
        swipeRefreshLayout_fixed= (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh_fixed);
        swipeRefreshLayout_no_products= (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh_no_products);

        mainProductsBean = new ArrayList<MainProductsBean>();
        mainProductsBean_non_negotiable = new ArrayList<MainProductsBean>();
        adapter = new ProductMainAdapter(getActivity(), mainProductsBean);
        adapter_non_negotiable = new ProductMainAdapterNonNegotiable(getActivity(), mainProductsBean_non_negotiable);
//        str_title = MainActivity.str_arr_category_name[mPage];
//            Log.e("category_id",MainActivity.str_arr_category_id[mPage]);
//            Log.e("category_id",MainActivity.str_arr_category_name[mPage]);
//        Log.e("mPage", ids[mPage]);

        txt_sell_product= (TextView) view.findViewById(R.id.txt_sell_product);
        linear_product= (LinearLayout) view.findViewById(R.id.linear_no_products);

        txt_sell_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mpref.getString("guest_status","").equalsIgnoreCase("0")){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else {
                    Intent intent=new Intent(getActivity(),Sell_Products_Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });

        para = new HashMap<>();
        para.put("UserId", mpref.getString("user_id", ""));
        para.put("Page", "0");
        para.put("SortBy", MainActivity.str_sort_by);
        para.put("CategoryId", ids[mPage]);
        para.put("LowerPriceRange", MainActivity.str_lower_range_price);
        para.put("UpperPriceRange", MainActivity.str_upper_range_price);
        para.put("LowerDistance", MainActivity.str_lower_distance);
        para.put("UpperDistance", MainActivity.str_upper_distance);
        para.put("PostedWithin", MainActivity.str_posted_in);
        para.put("Latitude",mpref.getString("user_lat",""));
        para.put("Longitude",mpref.getString("user_lang",""));
        swipeRefreshLayout_compete.setRefreshing(true);
        swipeRefreshLayout_fixed.setRefreshing(true);
        HttpAsync httpAsync1 = new HttpAsync(getActivity(), listener, Constants.BASE_URL+"GetProducts", para, 2, "products");
        httpAsync1.execute();

        swipeRefreshLayout_compete.setColorSchemeColors(Color.RED);
        swipeRefreshLayout_fixed.setColorSchemeColors(Color.RED);
        swipeRefreshLayout_no_products.setColorSchemeColors(Color.RED);

        swipeRefreshLayout_compete.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                para = new HashMap<>();
                para.put("UserId", mpref.getString("user_id", ""));
                para.put("Page", "0");
                para.put("SortBy", MainActivity.str_sort_by);
                para.put("CategoryId", ids[mPage]);
                para.put("LowerPriceRange", MainActivity.str_lower_range_price);
                para.put("UpperPriceRange", MainActivity.str_upper_range_price);
                para.put("LowerDistance", MainActivity.str_lower_distance);
                para.put("UpperDistance", MainActivity.str_upper_distance);
                para.put("PostedWithin", MainActivity.str_posted_in);
                para.put("Latitude",mpref.getString("user_lat",""));
                para.put("Longitude",mpref.getString("user_lang",""));
                swipeRefreshLayout_compete.setRefreshing(true);
                swipeRefreshLayout_fixed.setRefreshing(true);
                HttpAsync httpAsync1 = new HttpAsync(getActivity(), listener, Constants.BASE_URL+"GetProducts", para, 2, "products");
                httpAsync1.execute();
            }
        });

        swipeRefreshLayout_fixed.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                para = new HashMap<>();
                para.put("UserId", mpref.getString("user_id", ""));
                para.put("Page", "0");
                para.put("SortBy", MainActivity.str_sort_by);
                para.put("CategoryId", ids[mPage]);
                para.put("LowerPriceRange", MainActivity.str_lower_range_price);
                para.put("UpperPriceRange", MainActivity.str_upper_range_price);
                para.put("LowerDistance", MainActivity.str_lower_distance);
                para.put("UpperDistance", MainActivity.str_upper_distance);
                para.put("PostedWithin", MainActivity.str_posted_in);
                para.put("Latitude",mpref.getString("user_lat",""));
                para.put("Longitude",mpref.getString("user_lang",""));
                swipeRefreshLayout_compete.setRefreshing(true);
                swipeRefreshLayout_fixed.setRefreshing(true);
                swipeRefreshLayout_no_products.setRefreshing(true);
                HttpAsync httpAsync1 = new HttpAsync(getActivity(), listener, Constants.BASE_URL+"GetProducts", para, 2, "products");
                httpAsync1.execute();
            }
        });

        swipeRefreshLayout_no_products.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                para = new HashMap<>();
                para.put("UserId", mpref.getString("user_id", ""));
                para.put("Page", "0");
                para.put("SortBy", MainActivity.str_sort_by);
                para.put("CategoryId", ids[mPage]);
                para.put("LowerPriceRange", MainActivity.str_lower_range_price);
                para.put("UpperPriceRange", MainActivity.str_upper_range_price);
                para.put("LowerDistance", MainActivity.str_lower_distance);
                para.put("UpperDistance", MainActivity.str_upper_distance);
                para.put("PostedWithin", MainActivity.str_posted_in);
                para.put("Latitude",mpref.getString("user_lat",""));
                para.put("Longitude",mpref.getString("user_lang",""));
                swipeRefreshLayout_compete.setRefreshing(true);
                swipeRefreshLayout_fixed.setRefreshing(true);
                swipeRefreshLayout_no_products.setRefreshing(true);
                HttpAsync httpAsync1 = new HttpAsync(getActivity(), listener, Constants.BASE_URL+"GetProducts", para, 2, "products");
                httpAsync1.execute();
            }
        });

        grid_item = (RecyclerView) view.findViewById(R.id.list);
//        adapter = new ProductMainAdapter(getActivity(), mainProductsBean);
        grid_item.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
        grid_item.setLayoutManager(layoutManager);
        grid_item.setNestedScrollingEnabled(false);
        mLayoutManager = new GridLayoutManager(getActivity(),2);
        // use a linear layout manager
        grid_item.setLayoutManager(mLayoutManager);


//        linear_LayoutManager = new MyCustomLayoutManager(getActivity());
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.smoothScrollToPosition(position);
//        grid_item.setAdapter(adapter);

        grid_item_non_negotiable = (RecyclerView) view.findViewById(R.id.list_non_negotiable);
//        adapter_non_negotiable = new ProductMainAdapterNonNegotiable(getActivity(), mainProductsBean_non_negotiable);
        grid_item_non_negotiable.setHasFixedSize(true);
        GridLayoutManager layoutManager1 = new GridLayoutManager(getActivity(), 2);
        grid_item_non_negotiable.setLayoutManager(layoutManager1);
        grid_item_non_negotiable.setNestedScrollingEnabled(false);
//        grid_item_non_negotiable.setAdapter(adapter_non_negotiable);

        rad_negotiable = (RadioButton) view.findViewById(R.id.rad_negotiable);
        rad_non_negotiable = (RadioButton) view.findViewById(R.id.rad_non_negotiable);

        rad_negotiable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeRefreshLayout_compete.setVisibility(View.VISIBLE);
                swipeRefreshLayout_fixed.setVisibility(View.GONE);
            }
        });

        rad_non_negotiable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeRefreshLayout_compete.setVisibility(View.GONE);
                swipeRefreshLayout_fixed.setVisibility(View.VISIBLE);
            }
        });

        linear_compete= (LinearLayout) view.findViewById(R.id.linear_compete);
        linear_fixed_price= (LinearLayout) view.findViewById(R.id.linear_fixed_price);
        txt_compete= (TextView) view.findViewById(R.id.txt_compete);
        txt_fixed_price= (TextView) view.findViewById(R.id.txt_fixed_price);
        linear_compete_down= (LinearLayout) view.findViewById(R.id.linear_compete_down);
        linear_fixed_price_down= (LinearLayout) view.findViewById(R.id.linear_fixed_price_bottom);

        linear_compete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.linear_bottom_layout.setVisibility(View.VISIBLE);

                txt_compete.setTextColor(Color.parseColor("#F14D57"));
                txt_fixed_price.setTextColor(Color.parseColor("#808080"));

                if(int_negotiable==0){
                    swipeRefreshLayout_no_products.setVisibility(View.VISIBLE);
                    swipeRefreshLayout_compete.setVisibility(View.GONE);
                    swipeRefreshLayout_fixed.setVisibility(View.GONE);
                }
                else {
                    swipeRefreshLayout_compete.setVisibility(View.VISIBLE);
                    swipeRefreshLayout_fixed.setVisibility(View.GONE);
                    swipeRefreshLayout_no_products.setVisibility(View.GONE);
                }
//                linear_compete_down.setVisibility(View.VISIBLE);
//                linear_fixed_price_down.setVisibility(View.GONE);
            }
        });

        linear_fixed_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.linear_bottom_layout.setVisibility(View.VISIBLE);

                txt_compete.setTextColor(Color.parseColor("#808080"));
                txt_fixed_price.setTextColor(Color.parseColor("#F14D57"));

                if(int_non_negotiable==0){
                    swipeRefreshLayout_no_products.setVisibility(View.VISIBLE);
                    swipeRefreshLayout_compete.setVisibility(View.GONE);
                    swipeRefreshLayout_fixed.setVisibility(View.GONE);
                }
                else {
                    swipeRefreshLayout_compete.setVisibility(View.GONE);
                    swipeRefreshLayout_fixed.setVisibility(View.VISIBLE);
                    swipeRefreshLayout_no_products.setVisibility(View.GONE);
                }
//                linear_compete_down.setVisibility(View.GONE);
//                linear_fixed_price_down.setVisibility(View.VISIBLE);
            }
        });

//        grid_item.addOnScrollListener(new HidingScrollListener() {
//            @Override
//            public void onHide() {
////                hideViews();
//            }
//
//            @Override
//            public void onShow() {
////                showViews();
//            }
//        });

        grid_item_non_negotiable.setOnScrollListener(new EndlessRecyclerOnScrollListener(
                mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                // do somthing...
                loadMoreData(current_page);
            }
        });

        grid_item.setOnScrollListener(new EndlessRecyclerOnScrollListener(
                mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                // do somthing...
                loadMoreData(current_page);
            }
        });


        grid_item.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }

            @Override
            public void onShow() {
                showViews();
            }
        });

        grid_item_non_negotiable.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }

            @Override
            public void onShow() {
                showViews();
            }
        });


        return view;
    }


    private void hideViews() {
//        ExampleFragments.tabsStrip.animate().translationY(-ExampleFragments.tabsStrip.getHeight()).setInterpolator(new AccelerateInterpolator(2));
//        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)MainActivity.linear_bottom_layout.getLayoutParams();
//        int fabBottomMargin = lp.bottomMargin;
//        MainActivity.linear_bottom_layout.animate().translationY(MainActivity.fab.getHeight()+fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
        MainActivity.linear_bottom_layout.setVisibility(View.GONE);
//        ExampleFragments.tabsStrip.setVisibility(View.GONE);

    }

    private void showViews() {
//        ExampleFragments.tabsStrip.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
//        MainActivity.linear_bottom_layout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
        MainActivity.linear_bottom_layout.setVisibility(View.VISIBLE);
//        ExampleFragments.tabsStrip.setVisibility(View.VISIBLE);
    }


    private void loadMoreData(int current_page) {
        loadLimit = ival;

        for (int i = ival; i <= loadLimit; i++){

            para = new HashMap<>();
            para.put("UserId", mpref.getString("user_id", ""));
            para.put("Page", i+"");
            para.put("SortBy", MainActivity.str_sort_by);
            para.put("CategoryId", ids[mPage]);
            para.put("LowerPriceRange", MainActivity.str_lower_range_price);
            para.put("UpperPriceRange", MainActivity.str_upper_range_price);
            para.put("LowerDistance", MainActivity.str_lower_distance);
            para.put("UpperDistance", MainActivity.str_upper_distance);
            para.put("PostedWithin", MainActivity.str_posted_in);
            para.put("Latitude",mpref.getString("user_lat",""));
            para.put("Longitude",mpref.getString("user_lang",""));
            swipeRefreshLayout_compete.setRefreshing(true);
            swipeRefreshLayout_fixed.setRefreshing(true);
            swipeRefreshLayout_no_products.setRefreshing(true);
            HttpAsync httpAsync1 = new HttpAsync(getActivity(), listener, Constants.BASE_URL+"GetProducts", para, 2, "products_loadmore");
            httpAsync1.execute();

            ival++;
        }
//        adapter.notifyDataSetChanged();
    }


//    private void hideViews() {
//        MainActivity.toolbar.animate().translationY(-MainActivity.toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
//
//
//
//
//
//        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams)MainActivity.linear_bottom_layout.getLayoutParams();
//        int fabBottomMargin = lp.bottomMargin;
//        MainActivity.linear_bottom_layout.animate().translationY(MainActivity.fab.getHeight()+fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
//    }
//
//    private void showViews() {
//        MainActivity.toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
//        MainActivity.linear_bottom_layout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
//    }


//    @Override
//    public void onResume() {
//        super.onResume();
//        grid_item.setVisibility(View.VISIBLE);
//        grid_item_non_negotiable.setVisibility(View.GONE);
//        rad_negotiable.setChecked(true);
//    }

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


    @Override
    public void onTaskCancelled(String data) {

    }

    @Override
    public void onTaskComplete(String result, String tag) {
        swipeRefreshLayout_compete.setRefreshing(false);
        swipeRefreshLayout_fixed.setRefreshing(false);
        swipeRefreshLayout_no_products.setRefreshing(false);
        if (result.equalsIgnoreCase("fail")) {
            try {
                Toast.makeText(getActivity(),"Check your internet connection",Toast.LENGTH_SHORT).show();
            }catch (Exception e){

            }

        } else {
            if (tag.equalsIgnoreCase("products")) {
                JSONObject jsonObject = null;
                mainProductsBean.clear();
                mainProductsBean_non_negotiable.clear();

                try {

                    jsonObject = new JSONObject(result);
                    if (jsonObject != null) {
                        if (jsonObject.getString("errFlag").equalsIgnoreCase("0")) {
                            JSONArray jsonsendarr = jsonObject.optJSONArray("Negotiable");
                            int_negotiable=jsonsendarr.length();
                            if (jsonsendarr.length() > 0) {
//                                mainProductsBean = new ArrayList<MainProductsBean>();
                                for (int i = 0; i < jsonsendarr.length(); i++) {
                                    if (jsonObject != null) {

                                        MainProductsBean main_Products_Bean = new MainProductsBean();
                                        JSONObject volumobject = jsonsendarr.getJSONObject(i);

                                        main_Products_Bean.setUsedFor(volumobject.getString("UsedFor"));
                                        main_Products_Bean.setCondition(volumobject.getString("Condition"));
                                        main_Products_Bean.setPostedBy(volumobject.getString("PostedBy"));
                                        main_Products_Bean.setProductName(volumobject.getString("ProductName"));
                                        main_Products_Bean.setPostedByImage(volumobject.getString("PostedByImage"));
                                        main_Products_Bean.setDistance(volumobject.getString("Distance"));
                                        main_Products_Bean.setImage(volumobject.getString("Image"));
                                        main_Products_Bean.setPrice(volumobject.getString("Price"));
                                        main_Products_Bean.setQtyRemaining(volumobject.getString("QtyRemaining"));
                                        main_Products_Bean.setFavorite(volumobject.getString("Favorite"));
                                        main_Products_Bean.setDescription(volumobject.getString("Description"));
                                        main_Products_Bean.setType(volumobject.getString("Type"));
                                        main_Products_Bean.setProductId(volumobject.getString("ProductId"));

                                        arrayList_negotiable_product_id.add(volumobject.getString("ProductId"));
                                        arrayList_unique_negotiable_product_id = removeDuplicates(arrayList_negotiable_product_id);

                                        arrayList_negotiable_product_name.add(volumobject.getString("ProductName"));
                                        arrayList_unique_negotiable_product_name = removeDuplicates(arrayList_negotiable_product_name);

                                        arrayList_negotiable_used_for.add(volumobject.getString("UsedFor"));
                                        arrayList_unique_negotiable_used_for = removeDuplicates(arrayList_negotiable_used_for);

                                        arrayList_negotiable_description.add(volumobject.getString("Description"));
                                        arrayList_unique_negotiable_description = removeDuplicates(arrayList_negotiable_description);

                                        arrayList_negotiable_price.add(volumobject.getString("Price"));
                                        arrayList_unique_negotiable_price = removeDuplicates(arrayList_negotiable_price);

                                        arrayList_negotiable_condition.add(volumobject.getString("Condition"));
                                        arrayList_unique_negotiable_condition = removeDuplicates(arrayList_negotiable_condition);

                                        arrayList_negotiable_distance.add(volumobject.getString("Distance"));
                                        arrayList_unique_negotiable_distance= removeDuplicates(arrayList_negotiable_distance);

                                        arrayList_negotiable_posted_image.add(volumobject.getString("PostedByImage"));
                                        arrayList_unique_negotiable_posted_image = removeDuplicates(arrayList_negotiable_posted_image);


//                                        str_arr_product_id_negotiable=new String[arrayList_negotiable_id.size()];
//                                        str_arr_product_id_negotiable=arrayList_negotiable_id.toArray(str_arr_product_id_negotiable);

                                        str_type_1 = volumobject.getString("Type");

                                        mainProductsBean.add(main_Products_Bean);
                                    }
                                }

                                if (mainProductsBean.size() > 0) {
                                    try {
                                        Log.e("products", mainProductsBean.size()+" ");

                                        grid_item.setAdapter(adapter);
                                        ival = 1;

                                        grid_item.setOnScrollListener(new EndlessRecyclerOnScrollListener(
                                                mLayoutManager) {
                                            @Override
                                            public void onLoadMore(int current_page) {
                                                // do somthing...
                                                loadMoreData(current_page);
                                            }
                                        });
//                                        grid_item.scrollToPosition(mainProductsBean.size()-4);
//                                        grid_item.smoothScrollToPosition(mainProductsBean.size());
//                                        grid_item.setVisibility(View.VISIBLE);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }


                                adapter.SetOnItemClickListener(new ProductMainAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {

                                        try {
                                            Intent intent = new Intent(getActivity(), ProductDetailsActivity_Negotiable.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.putExtra("image", mainProductsBean.get(position).getImage());
                                            intent.putExtra("condition",mainProductsBean.get(position).getCondition());
                                            intent.putExtra("name", mainProductsBean.get(position).getProductName());
                                            intent.putExtra("posted_image",mainProductsBean.get(position).getPostedByImage());
                                            intent.putExtra("price", mainProductsBean.get(position).getPrice());
                                            intent.putExtra("product_name", mainProductsBean.get(position).getProductName());
                                            intent.putExtra("product_id", mainProductsBean.get(position).getProductId());
                                            intent.putExtra("description", mainProductsBean.get(position).getDescription());
                                            intent.putExtra("distance", mainProductsBean.get(position).getDistance());
                                            intent.putExtra("qty_remaining", mainProductsBean.get(position).getQtyRemaining());
                                            intent.putExtra("type", mainProductsBean.get(position).getType());
                                            intent.putExtra("used_for", mainProductsBean.get(position).getUsedFor());
//                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                                ImageView imageView = (ImageView) view.findViewById(R.id.img_product);
//                                                ActivityOptionsCompat options = ActivityOptionsCompat.
//                                                        makeSceneTransitionAnimation(getActivity(), imageView, "profile");
//                                                startActivity(intent, options.toBundle());
//                                            } else {
//                                            intent.putExtra("negotiable_bean",mainProductsBean);
                                                startActivity(intent);
//                                            }
                                        } catch (Exception e) {

                                        }

                                    }
                                });


                            }
                            else {
                                swipeRefreshLayout_no_products.setVisibility(View.VISIBLE);
                                swipeRefreshLayout_compete.setVisibility(View.GONE);
                            }

                            JSONArray jsonsendarr1 = jsonObject.optJSONArray("NonNegotiable");
                            int_non_negotiable=jsonsendarr1.length();
                            if (jsonsendarr1.length() > 0) {
//                                mainProductsBean_non_negotiable = new ArrayList<MainProductsBean>();
                                Log.e("non_neg_length", jsonsendarr1.length()+" ");
                                for (int i = 0; i < jsonsendarr1.length(); i++) {
                                    if (jsonObject != null) {
                                        MainProductsBean main_Products_Bean = new MainProductsBean();
                                        JSONObject volumobject = jsonsendarr1.getJSONObject(i);

                                        main_Products_Bean.setUsedFor(volumobject.getString("UsedFor"));
                                        main_Products_Bean.setCondition(volumobject.getString("Condition"));
                                        main_Products_Bean.setPostedBy(volumobject.getString("PostedBy"));
                                        main_Products_Bean.setProductName(volumobject.getString("ProductName"));
                                        main_Products_Bean.setPostedByImage(volumobject.getString("PostedByImage"));
                                        main_Products_Bean.setDistance(volumobject.getString("Distance"));
                                        main_Products_Bean.setImage(volumobject.getString("Image"));
                                        main_Products_Bean.setPrice(volumobject.getString("Price"));
                                        main_Products_Bean.setQtyRemaining(volumobject.getString("QtyRemaining"));
                                        main_Products_Bean.setFavorite(volumobject.getString("Favorite"));
                                        main_Products_Bean.setDescription(volumobject.getString("Description"));
                                        main_Products_Bean.setType(volumobject.getString("Type"));
                                        main_Products_Bean.setProductId(volumobject.getString("ProductId"));

                                        str_type_2 = volumobject.getString("Type");

                                        mainProductsBean_non_negotiable.add(main_Products_Bean);
//                                        mainProductsBean.add(main_Products_Bean);
                                    }
                                }

                                if (mainProductsBean_non_negotiable.size() > 0) {
                                    try {
//                                        Log.e("products_non", mainProductsBean_non_negotiable.size()+" ");

                                        grid_item_non_negotiable.setAdapter(adapter_non_negotiable);
                                        ival = 1;

                                        grid_item_non_negotiable.setOnScrollListener(new EndlessRecyclerOnScrollListener(
                                                mLayoutManager) {
                                            @Override
                                            public void onLoadMore(int current_page) {
                                                // do somthing...
                                                loadMoreData(current_page);
                                            }
                                        });
//                                        grid_item_non_negotiable.scrollToPosition(mainProductsBean_non_negotiable.size()-4);
//                                        grid_item_non_negotiable.smoothScrollToPosition(mainProductsBean_non_negotiable.size());
//                                        grid_item_non_negotiable.setVisibility(View.VISIBLE);

                                    } catch (Exception e) {
                                    }
                                }

                                adapter_non_negotiable.SetOnItemClickListener(new ProductMainAdapterNonNegotiable.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {

                                        try {
                                            Intent intent = new Intent(getActivity(), ProductDetailsActivity_Non_Negotiable.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.putExtra("image", mainProductsBean_non_negotiable.get(position).getImage());
                                            intent.putExtra("name", mainProductsBean_non_negotiable.get(position).getProductName());
                                            intent.putExtra("posted_image",mainProductsBean_non_negotiable.get(position).getPostedByImage());
                                            intent.putExtra("condition",mainProductsBean_non_negotiable.get(position).getCondition());
                                            intent.putExtra("price", mainProductsBean_non_negotiable.get(position).getPrice());
                                            intent.putExtra("product_name", mainProductsBean_non_negotiable.get(position).getProductName());
                                            intent.putExtra("product_id", mainProductsBean_non_negotiable.get(position).getProductId());
                                            intent.putExtra("description", mainProductsBean_non_negotiable.get(position).getDescription());
                                            intent.putExtra("distance", mainProductsBean_non_negotiable.get(position).getDistance());
                                            intent.putExtra("qty_remaining", mainProductsBean_non_negotiable.get(position).getQtyRemaining());
                                            intent.putExtra("type", mainProductsBean_non_negotiable.get(position).getType());
                                            intent.putExtra("used_for", mainProductsBean_non_negotiable.get(position).getUsedFor());
//                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                                ImageView imageView = (ImageView) view.findViewById(R.id.img_product);
//                                                ActivityOptionsCompat options = ActivityOptionsCompat.
//                                                        makeSceneTransitionAnimation(getActivity(), imageView, "profile");
//                                                startActivity(intent, options.toBundle());
//                                            } else {
                                                startActivity(intent);
//                                            }
                                        } catch (Exception e) {

                                        }

                                    }
                                });


                            }
                            else {
                                swipeRefreshLayout_no_products.setVisibility(View.VISIBLE);
                                swipeRefreshLayout_fixed.setVisibility(View.GONE);
                            }

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if(tag.equalsIgnoreCase("products_loadmore")){
                JSONObject jsonObject = null;
//                mainProductsBean.clear();
//                mainProductsBean_non_negotiable.clear();

                try {

                    jsonObject = new JSONObject(result);
                    if (jsonObject != null) {
                        if (jsonObject.getString("errFlag").equalsIgnoreCase("0")) {
                            JSONArray jsonsendarr = jsonObject.optJSONArray("Negotiable");
                            if (jsonsendarr.length() > 0) {
//                                mainProductsBean = new ArrayList<MainProductsBean>();
                                for (int i = 0; i < jsonsendarr.length(); i++) {
                                    if (jsonObject != null) {

                                        MainProductsBean main_Products_Bean = new MainProductsBean();
                                        JSONObject volumobject = jsonsendarr.getJSONObject(i);

                                        main_Products_Bean.setUsedFor(volumobject.getString("UsedFor"));
                                        main_Products_Bean.setCondition(volumobject.getString("Condition"));
                                        main_Products_Bean.setPostedBy(volumobject.getString("PostedBy"));
                                        main_Products_Bean.setProductName(volumobject.getString("ProductName"));
                                        main_Products_Bean.setPostedByImage(volumobject.getString("PostedByImage"));
                                        main_Products_Bean.setDistance(volumobject.getString("Distance"));
                                        main_Products_Bean.setImage(volumobject.getString("Image"));
                                        main_Products_Bean.setPrice(volumobject.getString("Price"));
                                        main_Products_Bean.setQtyRemaining(volumobject.getString("QtyRemaining"));
                                        main_Products_Bean.setFavorite(volumobject.getString("Favorite"));
                                        main_Products_Bean.setDescription(volumobject.getString("Description"));
                                        main_Products_Bean.setType(volumobject.getString("Type"));
                                        main_Products_Bean.setProductId(volumobject.getString("ProductId"));

                                        str_type_1 = volumobject.getString("Type");

                                        mainProductsBean.add(main_Products_Bean);
                                    }
                                }

                                if (mainProductsBean.size() > 0) {
                                    try {
                                        Log.e("products", mainProductsBean.size()+" ");
//                                        adapter = new ProductMainAdapter(getActivity(), mainProductsBean);
//                                        grid_item.setAdapter(adapter);
//                                        grid_item.scrollToPosition(mainProductsBean.size()-4);
//                                        grid_item.smoothScrollToPosition(mainProductsBean.size());
                                        adapter.notifyDataSetChanged();
//                                        grid_item.setVisibility(View.VISIBLE);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }


                                adapter.SetOnItemClickListener(new ProductMainAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {

                                        try {
                                            Intent intent = new Intent(getActivity(), ProductDetailsActivity_Negotiable.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.putExtra("image", mainProductsBean.get(position).getImage());
                                            intent.putExtra("name", mainProductsBean.get(position).getProductName());
                                            intent.putExtra("posted_image",mainProductsBean.get(position).getPostedByImage());
                                            intent.putExtra("condition",mainProductsBean.get(position).getCondition());
                                            intent.putExtra("price", mainProductsBean.get(position).getPrice());
                                            intent.putExtra("product_name", mainProductsBean.get(position).getProductName());
                                            intent.putExtra("product_id", mainProductsBean.get(position).getProductId());
                                            intent.putExtra("description", mainProductsBean.get(position).getDescription());
                                            intent.putExtra("distance", mainProductsBean.get(position).getDistance());
                                            intent.putExtra("qty_remaining", mainProductsBean.get(position).getQtyRemaining());
                                            intent.putExtra("type", mainProductsBean.get(position).getType());
                                            intent.putExtra("used_for", mainProductsBean.get(position).getUsedFor());
//                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                                ImageView imageView = (ImageView) view.findViewById(R.id.img_product);
//                                                ActivityOptionsCompat options = ActivityOptionsCompat.
//                                                        makeSceneTransitionAnimation(getActivity(), imageView, "profile");
//                                                startActivity(intent, options.toBundle());
//                                            } else {
                                                startActivity(intent);
//                                            }
                                        } catch (Exception e) {

                                        }

                                    }
                                });


                            }


                            JSONArray jsonsendarr1 = jsonObject.optJSONArray("NonNegotiable");
                            if (jsonsendarr1.length() > 0) {
//                                mainProductsBean_non_negotiable = new ArrayList<MainProductsBean>();
                                Log.e("non_neg_length", jsonsendarr1.length()+" ");
                                for (int i = 0; i < jsonsendarr1.length(); i++) {
                                    if (jsonObject != null) {
                                        MainProductsBean main_Products_Bean = new MainProductsBean();
                                        JSONObject volumobject = jsonsendarr1.getJSONObject(i);

                                        main_Products_Bean.setUsedFor(volumobject.getString("UsedFor"));
                                        main_Products_Bean.setCondition(volumobject.getString("Condition"));
                                        main_Products_Bean.setPostedBy(volumobject.getString("PostedBy"));
                                        main_Products_Bean.setProductName(volumobject.getString("ProductName"));
                                        main_Products_Bean.setPostedByImage(volumobject.getString("PostedByImage"));
                                        main_Products_Bean.setDistance(volumobject.getString("Distance"));
                                        main_Products_Bean.setImage(volumobject.getString("Image"));
                                        main_Products_Bean.setPrice(volumobject.getString("Price"));
                                        main_Products_Bean.setQtyRemaining(volumobject.getString("QtyRemaining"));
                                        main_Products_Bean.setFavorite(volumobject.getString("Favorite"));
                                        main_Products_Bean.setDescription(volumobject.getString("Description"));
                                        main_Products_Bean.setType(volumobject.getString("Type"));
                                        main_Products_Bean.setProductId(volumobject.getString("ProductId"));

                                        str_type_2 = volumobject.getString("Type");

                                        mainProductsBean_non_negotiable.add(main_Products_Bean);
//                                        mainProductsBean.add(main_Products_Bean);
                                    }
                                }

                                if (mainProductsBean_non_negotiable.size() > 0) {
                                    try {
                                        Log.e("products_non", mainProductsBean_non_negotiable.size()+" ");
//                                        adapter_non_negotiable = new ProductMainAdapterNonNegotiable(getActivity(), mainProductsBean_non_negotiable);
//                                        grid_item_non_negotiable.setAdapter(adapter_non_negotiable);
//                                        grid_item_non_negotiable.setVisibility(View.VISIBLE);
//                                        grid_item_non_negotiable.scrollToPosition(mainProductsBean_non_negotiable.size()-4);
//                                        grid_item_non_negotiable.smoothScrollToPosition(mainProductsBean_non_negotiable.size());
                                        adapter_non_negotiable.notifyDataSetChanged();

                                    } catch (Exception e) {
                                    }
                                }

                                adapter_non_negotiable.SetOnItemClickListener(new ProductMainAdapterNonNegotiable.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {

                                        try {
                                            Intent intent = new Intent(getActivity(), ProductDetailsActivity_Non_Negotiable.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.putExtra("image", mainProductsBean_non_negotiable.get(position).getImage());
                                            intent.putExtra("name", mainProductsBean_non_negotiable.get(position).getProductName());
                                            intent.putExtra("posted_image",mainProductsBean_non_negotiable.get(position).getPostedByImage());
                                            intent.putExtra("condition",mainProductsBean_non_negotiable.get(position).getCondition());
                                            intent.putExtra("price", mainProductsBean_non_negotiable.get(position).getPrice());
                                            intent.putExtra("product_name", mainProductsBean_non_negotiable.get(position).getProductName());
                                            intent.putExtra("product_id", mainProductsBean_non_negotiable.get(position).getProductId());
                                            intent.putExtra("description", mainProductsBean_non_negotiable.get(position).getDescription());
                                            intent.putExtra("distance", mainProductsBean_non_negotiable.get(position).getDistance());
                                            intent.putExtra("qty_remaining", mainProductsBean_non_negotiable.get(position).getQtyRemaining());
                                            intent.putExtra("type", mainProductsBean_non_negotiable.get(position).getType());
                                            intent.putExtra("used_for", mainProductsBean_non_negotiable.get(position).getUsedFor());
//                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                                ImageView imageView = (ImageView) view.findViewById(R.id.img_product);
//                                                ActivityOptionsCompat options = ActivityOptionsCompat.
//                                                        makeSceneTransitionAnimation(getActivity(), imageView, "profile");
//                                                startActivity(intent, options.toBundle());
//                                            } else {
                                                startActivity(intent);
//                                            }
                                        } catch (Exception e) {

                                        }
                                    }
                                });
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
