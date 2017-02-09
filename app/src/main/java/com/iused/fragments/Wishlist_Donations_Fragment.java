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
import com.iused.adapters.DonatedByOthersAdapter;
import com.iused.main.ProductDetailsActivity_Donate;
import com.iused.main.WishlistActivity;

/**
 * Created by Lenovo on 02/11/2016.
 */
public class Wishlist_Donations_Fragment extends Fragment{

    public static RecyclerView recyclerView_donations=null;
    public  static DonatedByOthersAdapter adapter;
    public static TextView txt_no_items=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donations_wishlist, container, false);

        txt_no_items= (TextView) view.findViewById(R.id.txt_no_items_wishlist_donations);
        recyclerView_donations= (RecyclerView) view.findViewById(R.id.recycle_donations_wishlist);
        recyclerView_donations.setHasFixedSize(true);
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView_donations.setNestedScrollingEnabled(false);
        recyclerView_donations.setLayoutManager(mLayoutManager);

        if (WishlistActivity.wishlistProductsBeanNegotiable.size() > 0) {
            try {
//                Log.e("products", WishlistActivity.wishlistProductsBeanNegotiable.size()+" ");
//                Log.e("products", WishlistActivity.wishlistProductsBeanNegotiable.toString());
                recyclerView_donations.setVisibility(View.VISIBLE);
                txt_no_items.setVisibility(View.GONE);

                adapter = new DonatedByOthersAdapter(getActivity(), WishlistActivity.wishlistProductsBeanDonations);
                recyclerView_donations.setAdapter(adapter);
//                                        grid_item.setVisibility(View.VISIBLE);

                adapter.SetOnItemClickListener(new DonatedByOthersAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        try {
                            Intent intent = new Intent(getActivity(), ProductDetailsActivity_Donate.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("image",WishlistActivity.wishlistProductsBeanDonations.get(position).getImage());
                            intent.putExtra("name", "");
                            intent.putExtra("price", WishlistActivity.wishlistProductsBeanDonations.get(position).getPrice());
                            intent.putExtra("Condition",WishlistActivity.wishlistProductsBeanDonations.get(position).getCondition());
                            intent.putExtra("donor_image","");
                            intent.putExtra("product_name", WishlistActivity.wishlistProductsBeanDonations.get(position).getProductName());
                            intent.putExtra("product_id", WishlistActivity.wishlistProductsBeanDonations.get(position).getProductId());
                            intent.putExtra("description", WishlistActivity.wishlistProductsBeanDonations.get(position).getDescription());
                            intent.putExtra("distance", WishlistActivity.wishlistProductsBeanDonations.get(position).getDistance());
                            intent.putExtra("qty_remaining", WishlistActivity.wishlistProductsBeanDonations.get(position).getQtyRemaining());
                            intent.putExtra("type", WishlistActivity.wishlistProductsBeanDonations.get(position).getType());
                            intent.putExtra("used_for", WishlistActivity.wishlistProductsBeanDonations.get(position).getUsedFor());
                            intent.putExtra("phone", "");

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
            recyclerView_donations.setVisibility(View.GONE);
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
                recyclerView_donations.setVisibility(View.VISIBLE);
                txt_no_items.setVisibility(View.GONE);

                adapter = new DonatedByOthersAdapter(getActivity(), WishlistActivity.wishlistProductsBeanDonations);
                recyclerView_donations.setAdapter(adapter);
                adapter.notifyDataSetChanged();
//                                        grid_item.setVisibility(View.VISIBLE);

                adapter.SetOnItemClickListener(new DonatedByOthersAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        try {
                            Intent intent = new Intent(getActivity(), ProductDetailsActivity_Donate.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("image",WishlistActivity.wishlistProductsBeanDonations.get(position).getImage());
                            intent.putExtra("name", "");
                            intent.putExtra("price", WishlistActivity.wishlistProductsBeanDonations.get(position).getPrice());
                            intent.putExtra("Condition",WishlistActivity.wishlistProductsBeanDonations.get(position).getCondition());
                            intent.putExtra("donor_image","");
                            intent.putExtra("product_name", WishlistActivity.wishlistProductsBeanDonations.get(position).getProductName());
                            intent.putExtra("product_id", WishlistActivity.wishlistProductsBeanDonations.get(position).getProductId());
                            intent.putExtra("description", WishlistActivity.wishlistProductsBeanDonations.get(position).getDescription());
                            intent.putExtra("distance", WishlistActivity.wishlistProductsBeanDonations.get(position).getDistance());
                            intent.putExtra("qty_remaining", WishlistActivity.wishlistProductsBeanDonations.get(position).getQtyRemaining());
                            intent.putExtra("type", WishlistActivity.wishlistProductsBeanDonations.get(position).getType());
                            intent.putExtra("used_for", WishlistActivity.wishlistProductsBeanDonations.get(position).getUsedFor());
                            intent.putExtra("phone", "");

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
            recyclerView_donations.setVisibility(View.GONE);
            txt_no_items.setVisibility(View.VISIBLE);
        }

    }
}
