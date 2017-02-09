package com.iused.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.iused.R;
import com.iused.adapters.OrderedProductsAdapter;
import com.iused.bean.OrderedProductsBean;
import com.iused.main.OrderedProductsDetailsActivity;
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
public class Ordered_Products_Fragment extends Fragment implements AsyncTaskListener{

    View view;
    private RecyclerView list_ordered_products=null;
    private HashMap<String, String> para = null;
    private AsyncTaskListener listener = null;
    public SharedPreferences mpref = null;
    public static ArrayList<OrderedProductsBean> orderedProductsBean= null;
    OrderedProductsAdapter adapter;
    private ProgressDialog progressDialog= null;
    private TextView txt_no_bought_products=null;
    public String date=null;
    private SwipeRefreshLayout swipeRefreshLayout_ordered_products;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ordered_products, container, false);

        progressDialog= new ProgressDialog(getActivity());
        listener=Ordered_Products_Fragment.this;

        list_ordered_products= (RecyclerView) view.findViewById(R.id.list_ordered_products);
        list_ordered_products.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),1);
        list_ordered_products.setLayoutManager(layoutManager);
        list_ordered_products.setNestedScrollingEnabled(false);

        txt_no_bought_products= (TextView) view.findViewById(R.id.txt_no_bought_products);
        swipeRefreshLayout_ordered_products= (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh_ordered_products);
        swipeRefreshLayout_ordered_products.setColorSchemeColors(Color.RED);

        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        date = formatter.format(today);

        mpref = getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);
        if (mpref != null)

        orderedProductsBean= new ArrayList<OrderedProductsBean>();

        para = new HashMap<>();
        para.put("UserId", mpref.getString("user_id", ""));
        para.put("Datetime", date);
        para.put("For","1");
//        progressDialog.setMessage("Loading...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
        swipeRefreshLayout_ordered_products.setRefreshing(true);
        Log.e("para_get_products", para.toString());
        HttpAsync httpAsync1 = new HttpAsync(getActivity(), listener, Constants.BASE_URL+"GetProductPurchaseRequests", para, 2, "my_orders");
        httpAsync1.execute();

        swipeRefreshLayout_ordered_products.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                para = new HashMap<>();
                para.put("UserId", mpref.getString("user_id", ""));
                para.put("Datetime", date);
                para.put("For","1");
//        progressDialog.setMessage("Loading...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
                swipeRefreshLayout_ordered_products.setRefreshing(true);
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

//        progressDialog.dismiss();
        swipeRefreshLayout_ordered_products.setRefreshing(false);
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
                                        OrderedProductsBean ordered_Products_Bean=new OrderedProductsBean();
                                        JSONObject volumobject = jsonsendarr.getJSONObject(i);

                                        ordered_Products_Bean.setRequestId(volumobject.getString("RequestId"));
                                        ordered_Products_Bean.setUserName(volumobject.getString("UserName"));
                                        ordered_Products_Bean.setUserPhone(volumobject.getString("UserPhone"));
                                        ordered_Products_Bean.setCurrency(volumobject.getString("Currency"));
                                        ordered_Products_Bean.setUsedFor(volumobject.getString("UsedFor"));
                                        ordered_Products_Bean.setCondition(volumobject.getString("Condition"));
                                        ordered_Products_Bean.setPhoto(volumobject.getString("Photo"));
                                        ordered_Products_Bean.setProductId(volumobject.getString("ProductId"));
                                        ordered_Products_Bean.setProductName(volumobject.getString("ProductName"));
                                        ordered_Products_Bean.setQty(volumobject.getString("Qty"));
                                        ordered_Products_Bean.setAmount(volumobject.getString("Amount"));
                                        ordered_Products_Bean.setAmount(volumobject.getString("OriginalPrice"));
                                        ordered_Products_Bean.setProductImage(volumobject.getString("ProductImage"));
                                        ordered_Products_Bean.setOfferApplied(volumobject.getInt("OfferApplied"));
                                        ordered_Products_Bean.setOfferTill(volumobject.getString("OfferTill"));
                                        ordered_Products_Bean.setStatus(volumobject.getString("Status"));

                                        orderedProductsBean.add(ordered_Products_Bean);

                                    }
                                }

                                if(orderedProductsBean.size()>0){
                                    try {

                                        adapter = new OrderedProductsAdapter(getActivity(), orderedProductsBean);
                                        list_ordered_products.setAdapter(adapter);
//                                        grid_item.setVisibility(View.VISIBLE);

                                    }catch (Exception e){

                                    }
                                }

                                adapter.SetOnItemClickListener(new OrderedProductsAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        if(orderedProductsBean.get(position).getStatus().equalsIgnoreCase("1")){
                                            Intent intent = new Intent(getActivity(), OrderedProductsDetailsActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
                                            intent.putExtra("RequestId",orderedProductsBean.get(position).getRequestId());
                                            intent.putExtra("UserName",orderedProductsBean.get(position).getUserName());
                                            intent.putExtra("Photo",orderedProductsBean.get(position).getPhoto());
                                            intent.putExtra("Currency",orderedProductsBean.get(position).getCurrency());
                                            intent.putExtra("UserPhone",orderedProductsBean.get(position).getUserPhone());
                                            intent.putExtra("Qty",orderedProductsBean.get(position).getQty());
                                            intent.putExtra("Amount",orderedProductsBean.get(position).getAmount());                    intent.putExtra("Currency",orderedProductsBean.get(position).getCurrency());
                                            intent.putExtra("ProductId",orderedProductsBean.get(position).getProductId());
                                            intent.putExtra("ProductName",orderedProductsBean.get(position).getProductName());
                                            intent.putExtra("UsedFor",orderedProductsBean.get(position).getUsedFor());
                                            intent.putExtra("Condition",orderedProductsBean.get(position).getCondition());
                                            intent.putExtra("OriginalPrice",orderedProductsBean.get(position).getOriginalPrice());
                                            intent.putExtra("ProductImage",orderedProductsBean.get(position).getProductImage());
                                            intent.putExtra("OfferApplied",orderedProductsBean.get(position).getOfferApplied());
                                            intent.putExtra("OfferTill",orderedProductsBean.get(position).getOfferTill());
                                            intent.putExtra("Status",orderedProductsBean.get(position).getStatus());

                                            startActivity(intent);
                                        }
                                    }
                                });


                            }
                            else {
                                swipeRefreshLayout_ordered_products.setVisibility(View.GONE);
                                txt_no_bought_products.setVisibility(View.VISIBLE);
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
