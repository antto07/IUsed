package com.iused.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iused.R;
import com.iused.adapters.ProductMainAdapter;
import com.iused.bean.MainProductsBean;
import com.iused.main.MainActivity;
import com.iused.main.WishlistActivity;
import com.iused.utils.AsyncTaskListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Lenovo on 02/11/2016.
 */
public class CompeteWishlistFragment extends Fragment implements AsyncTaskListener{

    private RecyclerView recyclerView_compete=null;
    ProductMainAdapter adapter;
    private HashMap<String, String> para = null;
    private AsyncTaskListener listener = null;
    public SharedPreferences mpref = null;

    public static ArrayList<MainProductsBean> wishlistProductsBeanNegotiable = null;
    public static ArrayList<MainProductsBean> wishlistProductsBeanNonNegotiable = null;
    public static ArrayList<MainProductsBean> wishlistProductsBeanDonations = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compete_wishlist, container, false);

        mpref = getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);
        if (mpref != null)
            listener = CompeteWishlistFragment.this;
        para = new HashMap<>();
        recyclerView_compete= (RecyclerView) view.findViewById(R.id.recycle_compete_wishlist);

        if (WishlistActivity.wishlistProductsBeanNegotiable.size() > 0) {
            try {
                Log.e("products", WishlistActivity.wishlistProductsBeanNegotiable.size()+" ");
                adapter = new ProductMainAdapter(getActivity(), WishlistActivity.wishlistProductsBeanNegotiable);
                recyclerView_compete.setAdapter(adapter);
//                                        grid_item.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return view;
    }

    @Override
    public void onTaskCancelled(String data) {

    }

    @Override
    public void onTaskComplete(String result, String tag) {



    }
}
