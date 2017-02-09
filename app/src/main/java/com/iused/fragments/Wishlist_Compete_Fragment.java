package com.iused.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.iused.R;
import com.iused.adapters.ProductMainAdapter;
import com.iused.bean.MainProductsBean;
import com.iused.main.ProductDetailsActivity_Negotiable;
import com.iused.main.WishlistActivity;
import com.iused.utils.AsyncTaskListener;
import com.iused.utils.HidingScrollListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Lenovo on 02/11/2016.
 */
public class Wishlist_Compete_Fragment extends Fragment{

    public static RecyclerView recyclerView_compete=null;
    public static ProductMainAdapter adapter;
    public static TextView txt_no_items=null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compete_wishlist, container, false);

        txt_no_items= (TextView) view.findViewById(R.id.txt_no_items_wishlist_compete);
        recyclerView_compete= (RecyclerView) view.findViewById(R.id.recycle_compete_wishlist);
        recyclerView_compete.setHasFixedSize(true);
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView_compete.setNestedScrollingEnabled(false);
        recyclerView_compete.setLayoutManager(mLayoutManager);

        try {

        }catch (Exception e){

        }

        if (WishlistActivity.wishlistProductsBeanNegotiable.size() > 0) {
            try {
//                Log.e("products", WishlistActivity.wishlistProductsBeanNegotiable.size()+" ");
//                Log.e("products", WishlistActivity.wishlistProductsBeanNegotiable.toString());
                recyclerView_compete.setVisibility(View.VISIBLE);
                txt_no_items.setVisibility(View.GONE);

                adapter = new ProductMainAdapter(getActivity(), WishlistActivity.wishlistProductsBeanNegotiable);
                recyclerView_compete.setAdapter(adapter);
                adapter.notifyDataSetChanged();
//                                        grid_item.setVisibility(View.VISIBLE);

                adapter.SetOnItemClickListener(new ProductMainAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        try {
                            Intent intent = new Intent(getActivity(), ProductDetailsActivity_Negotiable.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("image", WishlistActivity.wishlistProductsBeanNegotiable.get(position).getImage());
                            intent.putExtra("condition",WishlistActivity.wishlistProductsBeanNegotiable.get(position).getCondition());
                            intent.putExtra("name", WishlistActivity.wishlistProductsBeanNegotiable.get(position).getProductName());
                            intent.putExtra("posted_image","");
                            intent.putExtra("price", WishlistActivity.wishlistProductsBeanNegotiable.get(position).getPrice());
                            intent.putExtra("product_name", WishlistActivity.wishlistProductsBeanNegotiable.get(position).getProductName());
                            intent.putExtra("product_id", WishlistActivity.wishlistProductsBeanNegotiable.get(position).getProductId());
                            intent.putExtra("description", WishlistActivity.wishlistProductsBeanNegotiable.get(position).getDescription());
                            intent.putExtra("distance", WishlistActivity.wishlistProductsBeanNegotiable.get(position).getDistance());
                            intent.putExtra("qty_remaining", WishlistActivity.wishlistProductsBeanNegotiable.get(position).getQtyRemaining());
                            intent.putExtra("type", WishlistActivity.wishlistProductsBeanNegotiable.get(position).getType());
                            intent.putExtra("used_for", WishlistActivity.wishlistProductsBeanNegotiable.get(position).getUsedFor());

                            startActivity(intent);

                        } catch (Exception e) {

                        }

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            recyclerView_compete.setVisibility(View.GONE);
            txt_no_items.setVisibility(View.VISIBLE);
        }
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (WishlistActivity.wishlistProductsBeanNegotiable.size() > 0) {
            try {
//                Log.e("products", WishlistActivity.wishlistProductsBeanNegotiable.size()+" ");
//                Log.e("products", WishlistActivity.wishlistProductsBeanNegotiable.toString());
                recyclerView_compete.setVisibility(View.VISIBLE);
                txt_no_items.setVisibility(View.GONE);

                adapter = new ProductMainAdapter(getActivity(), WishlistActivity.wishlistProductsBeanNegotiable);
                recyclerView_compete.setAdapter(adapter);
//                                        grid_item.setVisibility(View.VISIBLE);

                adapter.SetOnItemClickListener(new ProductMainAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        try {
                            Intent intent = new Intent(getActivity(), ProductDetailsActivity_Negotiable.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("image", WishlistActivity.wishlistProductsBeanNegotiable.get(position).getImage());
                            intent.putExtra("condition",WishlistActivity.wishlistProductsBeanNegotiable.get(position).getCondition());
                            intent.putExtra("name", WishlistActivity.wishlistProductsBeanNegotiable.get(position).getProductName());
                            intent.putExtra("posted_image","");
                            intent.putExtra("price", WishlistActivity.wishlistProductsBeanNegotiable.get(position).getPrice());
                            intent.putExtra("product_name", WishlistActivity.wishlistProductsBeanNegotiable.get(position).getProductName());
                            intent.putExtra("product_id", WishlistActivity.wishlistProductsBeanNegotiable.get(position).getProductId());
                            intent.putExtra("description", WishlistActivity.wishlistProductsBeanNegotiable.get(position).getDescription());
                            intent.putExtra("distance", WishlistActivity.wishlistProductsBeanNegotiable.get(position).getDistance());
                            intent.putExtra("qty_remaining", WishlistActivity.wishlistProductsBeanNegotiable.get(position).getQtyRemaining());
                            intent.putExtra("type", WishlistActivity.wishlistProductsBeanNegotiable.get(position).getType());
                            intent.putExtra("used_for", WishlistActivity.wishlistProductsBeanNegotiable.get(position).getUsedFor());

                            startActivity(intent);

                        } catch (Exception e) {

                        }

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            recyclerView_compete.setVisibility(View.GONE);
            txt_no_items.setVisibility(View.VISIBLE);
        }
    }
}
