package com.iused.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class FaqPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public FaqPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Wishlist_Compete_Fragment tab1 = new Wishlist_Compete_Fragment();
                return tab1;
            case 1:
                Wishlist_Fixedprice_Fragment tab2 = new Wishlist_Fixedprice_Fragment();
                return tab2;
            case 2:
                Wishlist_Donations_Fragment tab3 = new Wishlist_Donations_Fragment();
                return tab3;
        }
        return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
