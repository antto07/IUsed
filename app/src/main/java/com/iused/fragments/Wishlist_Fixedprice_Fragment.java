package com.iused.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iused.R;
import com.iused.adapters.ProductMainAdapterNonNegotiable;
import com.iused.main.ProductDetailsActivity_Non_Negotiable;
import com.iused.main.WishlistActivity;

/**
 * Created by Lenovo on 02/11/2016.
 */
public class Wishlist_Fixedprice_Fragment extends Fragment{

    public static RecyclerView recyclerView_fixed_price=null;
    public static ProductMainAdapterNonNegotiable adapter;
    public static TextView txt_no_items=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fixedprice_wishlist, container, false);

        txt_no_items= (TextView) view.findViewById(R.id.txt_no_items_wishlist_fixed_price);
        recyclerView_fixed_price= (RecyclerView) view.findViewById(R.id.recycle_fixedprice_wishlist);
        recyclerView_fixed_price.setHasFixedSize(true);
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView_fixed_price.setNestedScrollingEnabled(false);
        recyclerView_fixed_price.setLayoutManager(mLayoutManager);

        if (WishlistActivity.wishlistProductsBeanNonNegotiable.size() > 0) {
            try {
//                Log.e("products", WishlistActivity.wishlistProductsBeanNegotiable.size()+" ");
//                Log.e("products", WishlistActivity.wishlistProductsBeanNegotiable.toString());
                recyclerView_fixed_price.setVisibility(View.VISIBLE);
                txt_no_items.setVisibility(View.GONE);

                adapter = new ProductMainAdapterNonNegotiable(getActivity(), WishlistActivity.wishlistProductsBeanNonNegotiable);
                recyclerView_fixed_price.setAdapter(adapter);
                adapter.notifyDataSetChanged();
//                                        grid_item.setVisibility(View.VISIBLE);

                adapter.SetOnItemClickListener(new ProductMainAdapterNonNegotiable.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        try {
                            Intent intent = new Intent(getActivity(), ProductDetailsActivity_Non_Negotiable.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("image", WishlistActivity.wishlistProductsBeanNonNegotiable.get(position).getImage());
                            intent.putExtra("name", WishlistActivity.wishlistProductsBeanNonNegotiable.get(position).getProductName());
                            intent.putExtra("posted_image","");
                            intent.putExtra("condition",WishlistActivity.wishlistProductsBeanNonNegotiable.get(position).getCondition());
                            intent.putExtra("price", WishlistActivity.wishlistProductsBeanNonNegotiable.get(position).getPrice());
                            intent.putExtra("product_name", WishlistActivity.wishlistProductsBeanNonNegotiable.get(position).getProductName());
                            intent.putExtra("product_id", WishlistActivity.wishlistProductsBeanNonNegotiable.get(position).getProductId());
                            intent.putExtra("description", WishlistActivity.wishlistProductsBeanNonNegotiable.get(position).getDescription());
                            intent.putExtra("distance", WishlistActivity.wishlistProductsBeanNonNegotiable.get(position).getDistance());
                            intent.putExtra("qty_remaining", WishlistActivity.wishlistProductsBeanNonNegotiable.get(position).getQtyRemaining());
                            intent.putExtra("type", WishlistActivity.wishlistProductsBeanNonNegotiable.get(position).getType());
                            intent.putExtra("used_for", WishlistActivity.wishlistProductsBeanNonNegotiable.get(position).getUsedFor());

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
            recyclerView_fixed_price.setVisibility(View.GONE);
            txt_no_items.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (WishlistActivity.wishlistProductsBeanNonNegotiable.size() > 0) {
            try {
//                Log.e("products", WishlistActivity.wishlistProductsBeanNegotiable.size()+" ");
//                Log.e("products", WishlistActivity.wishlistProductsBeanNegotiable.toString());
                recyclerView_fixed_price.setVisibility(View.VISIBLE);
                txt_no_items.setVisibility(View.GONE);

                adapter = new ProductMainAdapterNonNegotiable(getActivity(), WishlistActivity.wishlistProductsBeanNonNegotiable);
                recyclerView_fixed_price.setAdapter(adapter);
//                                        grid_item.setVisibility(View.VISIBLE);

                adapter.SetOnItemClickListener(new ProductMainAdapterNonNegotiable.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        try {
                            Intent intent = new Intent(getActivity(), ProductDetailsActivity_Non_Negotiable.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("image", WishlistActivity.wishlistProductsBeanNonNegotiable.get(position).getImage());
                            intent.putExtra("name", WishlistActivity.wishlistProductsBeanNonNegotiable.get(position).getProductName());
                            intent.putExtra("posted_image","");
                            intent.putExtra("condition",WishlistActivity.wishlistProductsBeanNonNegotiable.get(position).getCondition());
                            intent.putExtra("price", WishlistActivity.wishlistProductsBeanNonNegotiable.get(position).getPrice());
                            intent.putExtra("product_name", WishlistActivity.wishlistProductsBeanNonNegotiable.get(position).getProductName());
                            intent.putExtra("product_id", WishlistActivity.wishlistProductsBeanNonNegotiable.get(position).getProductId());
                            intent.putExtra("description", WishlistActivity.wishlistProductsBeanNonNegotiable.get(position).getDescription());
                            intent.putExtra("distance", WishlistActivity.wishlistProductsBeanNonNegotiable.get(position).getDistance());
                            intent.putExtra("qty_remaining", WishlistActivity.wishlistProductsBeanNonNegotiable.get(position).getQtyRemaining());
                            intent.putExtra("type", WishlistActivity.wishlistProductsBeanNonNegotiable.get(position).getType());
                            intent.putExtra("used_for", WishlistActivity.wishlistProductsBeanNonNegotiable.get(position).getUsedFor());

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
            recyclerView_fixed_price.setVisibility(View.GONE);
            txt_no_items.setVisibility(View.VISIBLE);
        }

    }
}
