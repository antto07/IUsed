package com.iused.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iused.R;
import com.iused.adapters.SoldProductsAdapter;
import com.iused.bean.SoldProductsBean;
import com.iused.introduction.LoginActivity;
import com.iused.main.SoldProductsDetailsActivity;
import com.iused.utils.AsyncTaskListener;
import com.iused.utils.Constants;
import com.iused.utils.HttpAsync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Antto on 10-10-2016.
 */
public class Sold_Products_Fragment extends Fragment implements AsyncTaskListener{

    View view;
    private RecyclerView list_ordered_products=null;
    private HashMap<String, String> para = null;
    private AsyncTaskListener listener = null;
    public SharedPreferences mpref = null;
    private ArrayList<SoldProductsBean> orderedProductsBean= null;
    SoldProductsAdapter adapter;
    private ProgressDialog progressDialog= null;
    private TextView txt_no_sold_products=null;

    private ArrayList<String> arr_list_request_id = null;
    private ArrayList<String> arr_list_user_name = null;
    private ArrayList<String> arr_list_amount = null;
    private ArrayList<String> arr_list_photo = null;
    private ArrayList<String> arr_list_product_name = null;
    private ArrayList<String> arr_list_offer_till = null;
    private ArrayList<String> arr_list_expires_on = null;
    private ArrayList<String> arr_list_phone = null;
    private ArrayList<String> arr_list_currency = null;
    String date = null;
    private SwipeRefreshLayout swipeRefreshLayout_sold_products;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sold_products, container, false);

        listener=Sold_Products_Fragment.this;
        progressDialog= new ProgressDialog(getActivity());

        list_ordered_products= (RecyclerView) view.findViewById(R.id.list_sold_products);
        list_ordered_products.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
        list_ordered_products.setLayoutManager(layoutManager);
        list_ordered_products.setNestedScrollingEnabled(false);

        txt_no_sold_products= (TextView) view.findViewById(R.id.txt_no_sold_products);
        swipeRefreshLayout_sold_products= (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh_sold_products);
        swipeRefreshLayout_sold_products.setColorSchemeColors(Color.RED);

        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        date = formatter.format(today);
//        Log.e("date",date);

        mpref = getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);
        if (mpref != null)

       orderedProductsBean= new ArrayList<SoldProductsBean>();

        if(mpref.getString("user_id", "").equalsIgnoreCase("")){
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else {
            para = new HashMap<>();
            para.put("UserId", mpref.getString("user_id", ""));
            para.put("Datetime", date);
            para.put("For","1");
//            progressDialog.setMessage("Loading...");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
            swipeRefreshLayout_sold_products.setRefreshing(true);
            Log.e("para_get_products", para.toString());
            HttpAsync httpAsync1 = new HttpAsync(getActivity(), listener, Constants.BASE_URL+"GetProductPurchaseRequests", para, 2, "my_orders");
            httpAsync1.execute();
        }

        swipeRefreshLayout_sold_products.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(mpref.getString("user_id", "").equalsIgnoreCase("")){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else {
                    para = new HashMap<>();
                    para.put("UserId", mpref.getString("user_id", ""));
                    para.put("Datetime", date);
                    para.put("For","1");
//            progressDialog.setMessage("Loading...");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
                    swipeRefreshLayout_sold_products.setRefreshing(true);
                    Log.e("para_get_products", para.toString());
                    HttpAsync httpAsync1 = new HttpAsync(getActivity(), listener, Constants.BASE_URL+"GetProductPurchaseRequests", para, 2, "my_orders");
                    httpAsync1.execute();
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        if(mpref.getString("user_id", "").equalsIgnoreCase("")){
//            Intent intent = new Intent(getActivity(), LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//        }
//        else {
//            para = new HashMap<>();
//            para.put("UserId", mpref.getString("user_id", ""));
//            para.put("Datetime", date);
//            para.put("For","1");
//            progressDialog.setMessage("Loading...");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//            Log.e("para_get_products", para.toString());
//            HttpAsync httpAsync1 = new HttpAsync(getActivity(), listener, "http://54.191.146.243:8088/GetProductPurchaseRequests", para, 2, "my_orders");
//            httpAsync1.execute();
//        }

    }

    @Override
    public void onTaskCancelled(String data) {

    }

    @Override
    public void onTaskComplete(String result, String tag) {

//        progressDialog.dismiss();
        swipeRefreshLayout_sold_products.setRefreshing(false);
        if(result.equalsIgnoreCase("fail")){
            try {
                Toast.makeText(getActivity(),"Check your internet connection",Toast.LENGTH_SHORT).show();
            }catch (Exception e){

            }
        }
        else {
            if(tag.equalsIgnoreCase("my_orders")){
                orderedProductsBean.clear();
                JSONObject jsonObject = null;
                try {


                    jsonObject = new JSONObject(result);
                    if (jsonObject != null){
                        if(jsonObject.getString("errFlag").equalsIgnoreCase("0")){
                            JSONArray jsonsendarr = jsonObject.optJSONArray("iWantToSell");
                            if (jsonsendarr.length() > 0){
                                for (int i = 0; i < jsonsendarr.length(); i++){
                                    if (jsonObject != null){
                                        SoldProductsBean ordered_Products_Bean=new SoldProductsBean();
                                        JSONObject volumobject = jsonsendarr.getJSONObject(i);

                                        ordered_Products_Bean.setUsedFor(volumobject.getString("UsedFor"));
                                        ordered_Products_Bean.setCondition(volumobject.getString("Condition"));
                                        ordered_Products_Bean.setProductId(volumobject.getString("ProductId"));
                                        ordered_Products_Bean.setProductName(volumobject.getString("ProductName"));
                                        ordered_Products_Bean.setProductImage(volumobject.getString("ProductImage"));
                                        ordered_Products_Bean.setCount(volumobject.getString("count"));
                                        ordered_Products_Bean.setStatus(volumobject.getString("Status"));
                                        ordered_Products_Bean.setType(volumobject.getString("Type"));
                                        ordered_Products_Bean.setInt_arr_count(volumobject.getInt("count"));

                                        JSONArray jsonsendarr1 = volumobject.getJSONArray("Reqeusts");
                                        arr_list_request_id=new ArrayList<>();
                                        arr_list_user_name=new ArrayList<>();
                                        arr_list_amount=new ArrayList<>();
                                        arr_list_photo=new ArrayList<>();
                                        arr_list_product_name=new ArrayList<>();
                                        arr_list_offer_till=new ArrayList<>();
                                        arr_list_expires_on=new ArrayList<>();
                                        arr_list_phone=new ArrayList<>();
                                        arr_list_currency=new ArrayList<>();

                                        if (jsonsendarr1.length() > 0){
                                            for (int j = 0; j < jsonsendarr1.length();j++){
                                                if (volumobject != null){
                                                    JSONObject request_object = jsonsendarr1.getJSONObject(j);
                                                    ordered_Products_Bean.setRequestId(request_object.getString("RequestId"));
//                                                    ordered_Products_Bean.setUsername(request_object.getString("UserName"));
//                                                    ordered_Products_Bean.setPhoto(request_object.getString("Photo"));
                                                    ordered_Products_Bean.setQty(request_object.getString("Qty"));
//                                                    ordered_Products_Bean.setAmount(request_object.getString("Amount"));
                                                    ordered_Products_Bean.setOriginalPrice(request_object.getString("OriginalPrice"));
                                                    ordered_Products_Bean.setOfferApplied(request_object.getString("OfferApplied"));
//                                                    ordered_Products_Bean.setOfferTill(request_object.getString("OfferTill"));
                                                    ordered_Products_Bean.setStatus_request(request_object.getString("Status"));
                                                    ordered_Products_Bean.setProductId_request(request_object.getString("ProductId"));
                                                    ordered_Products_Bean.setProductName_request(request_object.getString("ProductName"));

                                                    arr_list_request_id.add(request_object.getString("RequestId"));
                                                    ordered_Products_Bean.setArrayList_request_id(arr_list_request_id);
                                                    arr_list_user_name.add(request_object.getString("UserName"));
                                                    ordered_Products_Bean.setArrayList_user_name(arr_list_user_name);
                                                    arr_list_amount.add(request_object.getString("Amount"));
                                                    ordered_Products_Bean.setArrayList_amount(arr_list_amount);
                                                    arr_list_photo.add(request_object.getString("Photo"));
                                                    ordered_Products_Bean.setArrayList_photo(arr_list_photo);
                                                    arr_list_product_name.add(request_object.getString("ProductName"));
                                                    ordered_Products_Bean.setArrayList_product_name(arr_list_product_name);
                                                    arr_list_offer_till.add(request_object.getString("OfferTill"));
                                                    ordered_Products_Bean.setArrayList_offer_till(arr_list_offer_till);
                                                    arr_list_expires_on.add(request_object.getString("expiresin"));
                                                    ordered_Products_Bean.setArrayList_expires_on(arr_list_expires_on);
                                                    arr_list_phone.add(request_object.getString("UserPhone"));
                                                    ordered_Products_Bean.setArrayList_phone(arr_list_phone);
                                                    arr_list_currency.add(request_object.getString("Currency"));
                                                    ordered_Products_Bean.setArrayList_Currency(arr_list_currency);

                                                }
                                            }
                                        }

                                        orderedProductsBean.add(ordered_Products_Bean);

                                    }
                                }

                                if(orderedProductsBean.size()>0){
                                    try {

                                        adapter = new SoldProductsAdapter(getActivity(), orderedProductsBean);
                                        list_ordered_products.setAdapter(adapter);
//                                        grid_item.setVisibility(View.VISIBLE);

                                    }catch (Exception e){

                                    }
                                }

                                adapter.SetOnItemClickListener(new SoldProductsAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {

                                        Log.e("int_arr",orderedProductsBean.get(position).getInt_arr_count()+"");
                                        if(orderedProductsBean.get(position).getInt_arr_count()>0){
                                            try {

                                                Intent intent = new Intent(getActivity(), SoldProductsDetailsActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                intent.putExtra("image", orderedProductsBean.get(position).getPhoto());
//                                                intent.putExtra("name", orderedProductsBean.get(position).getArrayList_user_name());
//                                                intent.putExtra("price", orderedProductsBean.get(position).getArrayList_amount());
                                                intent.putExtra("product_name_intent", orderedProductsBean.get(position).getProductName());
                                                intent.putExtra("product_id", orderedProductsBean.get(position).getProductId());
                                                intent.putExtra("used_for",orderedProductsBean.get(position).getUsedFor());
                                                intent.putExtra("condition",orderedProductsBean.get(position).getCondition());
//                                                intent.putExtra("offer_till", orderedProductsBean.get(position).getOfferTill());
                                                intent.putExtra("offer_applied", orderedProductsBean.get(position).getOfferApplied());
                                                intent.putExtra("status", orderedProductsBean.get(position).getStatus());
                                                intent.putExtra("type",orderedProductsBean.get(position).getType());
                                                intent.putExtra("product_image",orderedProductsBean.get(position).getProductImage());
                                                intent.putExtra("count",orderedProductsBean.get(position).getCount());

                                                intent.putExtra("request_id", orderedProductsBean.get(position).getArrayList_request_id());
                                                intent.putExtra("name", orderedProductsBean.get(position).getArrayList_user_name());
                                                intent.putExtra("price", orderedProductsBean.get(position).getArrayList_amount());
                                                intent.putExtra("image",orderedProductsBean.get(position).getArrayList_photo());
                                                intent.putExtra("product_name",orderedProductsBean.get(position).getArrayList_product_name());
                                                intent.putExtra("offer_till",orderedProductsBean.get(position).getArrayList_offer_till());
                                                intent.putExtra("expires_on",orderedProductsBean.get(position).getArrayList_expires_on());
                                                intent.putExtra("phone",orderedProductsBean.get(position).getArrayList_phone());
                                                intent.putExtra("currency",orderedProductsBean.get(position).getArrayList_Currency());
//                                                intent.putExtra("used_for", mainProductsBean_non_negotiable.get(position).getUsedFor());
//                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                                    ImageView imageView = (ImageView) view.findViewById(R.id.img_product);
//                                                    ActivityOptionsCompat options = ActivityOptionsCompat.
//                                                            makeSceneTransitionAnimation(getActivity(), imageView, "profile");
//                                                    startActivity(intent, options.toBundle());
//                                                } else {
                                                    startActivity(intent);
//                                                }
                                            } catch (Exception e) {

                                            }
                                        }
                                        else {

                                        }
                                    }
                                });

                            }
                            else {
                                list_ordered_products.setVisibility(View.GONE);
                                txt_no_sold_products.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }


    }
}
