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
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iused.R;
import com.iused.adapters.DonorOptionsAdapter;
import com.iused.bean.DonorResponsesBean;
import com.iused.main.DonatedProductsDetailsActivity;
import com.iused.main.DonorResponsesDetailsActivity;
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
 * Created by yash on 19-10-2016.
 */
public class Donor_Responses_Fragment extends Fragment implements AsyncTaskListener{

    View view;
    private RecyclerView list_ordered_products=null;
    private HashMap<String, String> para = null;
    private AsyncTaskListener listener = null;
    public SharedPreferences mpref = null;
    private ArrayList<DonorResponsesBean> orderedProductsBean= null;
    DonorOptionsAdapter adapter;
    private ProgressDialog progressDialog= null;
    private TextView txt_no_donor_responses=null;

    private ArrayList<String> arr_list_request_id = null;
    private ArrayList<String> arr_list_user_name = null;
    private ArrayList<String> arr_list_amount = null;
    private ArrayList<String> arr_list_photo = null;
    private ArrayList<String> arr_list_product_name = null;
    private ArrayList<String> arr_list_offer_till = null;
    private ArrayList<String> arr_list_emotional_msg = null;
    private ArrayList<String> arr_list_user_phone = null;

    public String date=null;
    private SwipeRefreshLayout swipeRefreshLayout_donor_responses;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_donor_responses, container, false);

        listener=Donor_Responses_Fragment.this;
        progressDialog= new ProgressDialog(getActivity());

        list_ordered_products= (RecyclerView) view.findViewById(R.id.list_donor_responses);
        list_ordered_products.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        list_ordered_products.setLayoutManager(layoutManager);
        list_ordered_products.setNestedScrollingEnabled(false);

        txt_no_donor_responses= (TextView) view.findViewById(R.id.txt_no_doner_responses);
        swipeRefreshLayout_donor_responses= (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh_donor_responses);
        swipeRefreshLayout_donor_responses.setColorSchemeColors(Color.RED);

        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        date = formatter.format(today);

        mpref = getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);
        if (mpref != null)

            orderedProductsBean= new ArrayList<DonorResponsesBean>();

        para = new HashMap<>();
        para.put("UserId", mpref.getString("user_id", ""));
        para.put("Datetime", date);
        para.put("For","0");
//        progressDialog.setMessage("Loading...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
        swipeRefreshLayout_donor_responses.setRefreshing(true);
        Log.e("para_get_products", para.toString());
        HttpAsync httpAsync1 = new HttpAsync(getActivity(), listener, Constants.BASE_URL+"GetProductPurchaseRequests", para, 2, "my_orders");
        httpAsync1.execute();

        swipeRefreshLayout_donor_responses.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                para = new HashMap<>();
                para.put("UserId", mpref.getString("user_id", ""));
                para.put("Datetime", date);
                para.put("For","0");
//        progressDialog.setMessage("Loading...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
                swipeRefreshLayout_donor_responses.setRefreshing(true);
                Log.e("para_get_products", para.toString());
                HttpAsync httpAsync1 = new HttpAsync(getActivity(), listener, Constants.BASE_URL+"GetProductPurchaseRequests", para, 2, "my_orders");
                httpAsync1.execute();
            }
        });

        return view;
    }

    @Override
    public void onTaskCancelled(String data) {

    }

    @Override
    public void onTaskComplete(String result, String tag) {


        swipeRefreshLayout_donor_responses.setRefreshing(false);
//        progressDialog.dismiss();
        if(result.equalsIgnoreCase("fail")){
            try {
                Toast.makeText(getActivity(),"Check your internet connection",Toast.LENGTH_SHORT).show();
            }catch (Exception e){

            }

        }
        else {
            if(tag.equalsIgnoreCase("my_orders")){
                JSONObject jsonObject = null;
                try {
                    orderedProductsBean.clear();

                    jsonObject = new JSONObject(result);
                    if (jsonObject != null){
                        if(jsonObject.getString("errFlag").equalsIgnoreCase("0")){
                            JSONArray jsonsendarr = jsonObject.optJSONArray("iWantToBuy");
                            if (jsonsendarr.length() > 0){
                                for (int i = 0; i < jsonsendarr.length(); i++){
                                    if (jsonObject != null){
                                        DonorResponsesBean ordered_Products_Bean=new DonorResponsesBean();
                                        JSONObject volumobject = jsonsendarr.getJSONObject(i);

                                        ordered_Products_Bean.setRequestId(volumobject.getString("RequestId"));
                                        ordered_Products_Bean.setUsername(volumobject.getString("UserName"));
                                        ordered_Products_Bean.setPhoto(volumobject.getString("Photo"));
                                        ordered_Products_Bean.setUserPhone(volumobject.getString("UserPhone"));
                                        ordered_Products_Bean.setProductId(volumobject.getString("ProductId"));
                                        ordered_Products_Bean.setProductName(volumobject.getString("ProductName"));
                                        ordered_Products_Bean.setEmotionalMessage(volumobject.getString("ImotionalMessage"));
                                        ordered_Products_Bean.setQty(volumobject.getString("Qty"));
                                        ordered_Products_Bean.setAmount(volumobject.getString("Amount"));
                                        ordered_Products_Bean.setCurrency(volumobject.getString("Currency"));
                                        ordered_Products_Bean.setUsedFor(volumobject.getString("UsedFor"));
                                        ordered_Products_Bean.setCondition(volumobject.getString("Condition"));
                                        ordered_Products_Bean.setOriginalPrice(volumobject.getString("OriginalPrice"));
                                        ordered_Products_Bean.setProductImage(volumobject.getString("ProductImage"));
                                        ordered_Products_Bean.setOfferApplied(volumobject.getString("OfferApplied"));
                                        ordered_Products_Bean.setOfferTill(volumobject.getString("OfferTill"));
                                        ordered_Products_Bean.setStatus(volumobject.getString("Status"));

//                                        ordered_Products_Bean.setProductId(volumobject.getString("ProductId"));
//                                        ordered_Products_Bean.setProductName(volumobject.getString("ProductName"));
//                                        ordered_Products_Bean.setProductImage(volumobject.getString("ProductImage"));
//                                        ordered_Products_Bean.setCount(volumobject.getString("count"));
//                                        ordered_Products_Bean.setStatus(volumobject.getString("Status"));
//                                        ordered_Products_Bean.setType(volumobject.getString("Type"));
//                                        ordered_Products_Bean.setInt_arr_count(volumobject.getInt("count"));

//                                        JSONArray jsonsendarr1 = volumobject.getJSONArray("Reqeusts");
//                                        arr_list_request_id=new ArrayList<>();
//                                        arr_list_user_name=new ArrayList<>();
//                                        arr_list_amount=new ArrayList<>();
//                                        arr_list_photo=new ArrayList<>();
//                                        arr_list_product_name=new ArrayList<>();
//                                        arr_list_offer_till=new ArrayList<>();
//                                        arr_list_emotional_msg=new ArrayList<>();
//                                        arr_list_user_phone=new ArrayList<>();

//                                        if (jsonsendarr1.length() > 0){
//                                            for (int j = 0; j < jsonsendarr1.length();j++){
//                                                if (volumobject != null){
//                                                    JSONObject request_object = jsonsendarr1.getJSONObject(j);
//                                                    ordered_Products_Bean.setRequestId(request_object.getString("RequestId"));
////                                                    ordered_Products_Bean.setUsername(request_object.getString("UserName"));
////                                                    ordered_Products_Bean.setPhoto(request_object.getString("Photo"));
//                                                    ordered_Products_Bean.setQty(request_object.getString("Qty"));
////                                                    ordered_Products_Bean.setAmount(request_object.getString("Amount"));
//                                                    ordered_Products_Bean.setOriginalPrice(request_object.getString("OriginalPrice"));
//                                                    ordered_Products_Bean.setOfferApplied(request_object.getString("OfferApplied"));
////                                                    ordered_Products_Bean.setOfferTill(request_object.getString("OfferTill"));
//                                                    ordered_Products_Bean.setStatus_request(request_object.getString("Status"));
//                                                    ordered_Products_Bean.setProductId_request(request_object.getString("ProductId"));
//                                                    ordered_Products_Bean.setProductName_request(request_object.getString("ProductName"));
//
//                                                    arr_list_request_id.add(request_object.getString("RequestId"));
//                                                    ordered_Products_Bean.setArrayList_request_id(arr_list_request_id);
//                                                    arr_list_user_name.add(request_object.getString("UserName"));
//                                                    ordered_Products_Bean.setArrayList_user_name(arr_list_user_name);
//                                                    arr_list_amount.add(request_object.getString("Amount"));
//                                                    ordered_Products_Bean.setArrayList_amount(arr_list_amount);
//                                                    arr_list_photo.add(request_object.getString("Photo"));
//                                                    ordered_Products_Bean.setArrayList_photo(arr_list_photo);
//                                                    arr_list_product_name.add(request_object.getString("ProductName"));
//                                                    ordered_Products_Bean.setArrayList_product_name(arr_list_product_name);
//                                                    arr_list_offer_till.add(request_object.getString("OfferTill"));
//                                                    ordered_Products_Bean.setArrayList_offer_till(arr_list_offer_till);
//                                                    arr_list_emotional_msg.add(request_object.getString("ImotionalMessage"));
//                                                    ordered_Products_Bean.setArrayList_emotional_image(arr_list_emotional_msg);
//                                                    arr_list_user_phone.add(request_object.getString("UserPhone"));
//                                                    ordered_Products_Bean.setArrayList_user_phone(arr_list_user_phone);
//
//
//                                                }
//                                            }
//                                        }

                                        orderedProductsBean.add(ordered_Products_Bean);

                                    }
                                    else {

                                    }
                                }

                                if(orderedProductsBean.size()>0){
                                    try {

                                        adapter = new DonorOptionsAdapter(getActivity(), orderedProductsBean);
                                        list_ordered_products.setAdapter(adapter);
//                                        grid_item.setVisibility(View.VISIBLE);

                                    }catch (Exception e){

                                    }
                                }

                                adapter.SetOnItemClickListener(new DonorOptionsAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {



//                                        Log.e("int_arr",orderedProductsBean.get(position).getInt_arr_count()+"");
//                                        if(orderedProductsBean.get(position).getInt_arr_count()>0){
                                            try {

                                                if(orderedProductsBean.get(position).getStatus().equalsIgnoreCase("1")){
                                                    Intent intent = new Intent(getActivity(), DonorResponsesDetailsActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    intent.putExtra("RequestId",orderedProductsBean.get(position).getRequestId());
                                                    intent.putExtra("UserName",orderedProductsBean.get(position).getUsername());
                                                    intent.putExtra("Photo",orderedProductsBean.get(position).getPhoto());
                                                    intent.putExtra("UserPhone",orderedProductsBean.get(position).getUserPhone());
                                                    intent.putExtra("ProductId",orderedProductsBean.get(position).getProductId());
                                                    intent.putExtra("ProductName",orderedProductsBean.get(position).getProductName());
                                                    intent.putExtra("ImotionalMessage",orderedProductsBean.get(position).getEmotionalMessage());
                                                    intent.putExtra("Qty",orderedProductsBean.get(position).getQty());
                                                    intent.putExtra("Amount",orderedProductsBean.get(position).getAmount());
                                                    intent.putExtra("Currency",orderedProductsBean.get(position).getCurrency());
                                                    intent.putExtra("UsedFor",orderedProductsBean.get(position).getUsedFor());
                                                    intent.putExtra("Condition",orderedProductsBean.get(position).getCondition());
                                                    intent.putExtra("OriginalPrice",orderedProductsBean.get(position).getOriginalPrice());
                                                    intent.putExtra("ProductImage",orderedProductsBean.get(position).getProductImage());
                                                    intent.putExtra("OfferApplied",orderedProductsBean.get(position).getOfferApplied());
                                                    intent.putExtra("OfferTill",orderedProductsBean.get(position).getOfferTill());
                                                    intent.putExtra("Status",orderedProductsBean.get(position).getStatus());

//                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                                    ImageView imageView = (ImageView) view.findViewById(R.id.img_product);
//                                                    ActivityOptionsCompat options = ActivityOptionsCompat.
//                                                            makeSceneTransitionAnimation(getActivity(), imageView, "profile");
//                                                    startActivity(intent, options.toBundle());
//                                                } else {
                                                    startActivity(intent);
                                                }
                                                else {

                                                }
//                                                }
                                            } catch (Exception e) {

                                            }
//                                        }
//                                        else {
//
//                                        }
                                    }
                                });

                            }
                            else {
                                list_ordered_products.setVisibility(View.GONE);
                                txt_no_donor_responses.setVisibility(View.VISIBLE);
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
