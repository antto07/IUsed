package com.iused.utils;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.iused.fragments.Fragment_ProductDetails_Negotiable;

import java.util.List;

public class DynamicPagerAdapter_SwipeProducts extends FragmentPagerAdapter {

    private final List<Class> mFragmentTypes;
    public static List<String> list_product_id = null;
    public static List<String> list_name = null;
    public static List<String> list_used_for = null;
    public static List<String> list_condition = null;
    public static List<String> list_distance = null;
    public static List<String> list_posted_image = null;
    public static List<String> list_description = null;
    public static List<String> list_price = null;
    private static final String LOG_TAG = DynamicPagerAdapter_SwipeProducts.class.getSimpleName();

    public DynamicPagerAdapter_SwipeProducts(
            FragmentManager fm,
            List<String> pageTitles,List<String> product_name,List<String> used_for,List<String> description,List<String> price,List<String> condition,List<String> distance,
            List<String> posted_image,
            List<Class> fragmentTypes) {
        super(fm);
        this.list_product_id = pageTitles;
        this.list_name=product_name;
        this.list_used_for=used_for;
        this.list_description=description;
        this.list_price=price;
        this.list_condition=condition;
        this.list_distance=distance;
        this.list_posted_image=posted_image;

        this.mFragmentTypes = fragmentTypes;
    }

    @Override
    public int getCount() {
            return list_product_id.size();
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {

        Bundle args = new Bundle();
        args.putInt("ARG_PAGE", position);
        args.putStringArray("product_ids", list_product_id.toArray(new String[list_product_id.size()]));
        args.putStringArray("product_names", list_name.toArray(new String[list_name.size()]));
        args.putStringArray("product_used_for", list_used_for.toArray(new String[list_used_for.size()]));
        args.putStringArray("product_condition", list_condition.toArray(new String[list_condition.size()]));
        args.putStringArray("product_distance", list_distance.toArray(new String[list_distance.size()]));
        args.putStringArray("product_posted_image", list_posted_image.toArray(new String[list_posted_image.size()]));
        args.putStringArray("product_description", list_description.toArray(new String[list_description.size()]));
        args.putStringArray("product_price", list_price.toArray(new String[list_price.size()]));
//        args.putStringArray("ids", ids);

      Fragment_ProductDetails_Negotiable tabOneFragment=new Fragment_ProductDetails_Negotiable();
        tabOneFragment.setArguments(args);
        return tabOneFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if (list_product_id != null &&
                position >= 0 &&
                position < list_product_id.size()) {
            return list_product_id.get(position);
        }

        return null;
    }
}
