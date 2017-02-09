package com.iused.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.List;

/**
 * Created by Antto on 06/01/2017.
 */

public class DynamicPagerAdapter extends FragmentPagerAdapter {

    private final List<Class> mFragmentTypes;
    private final List<String> mPageTitles;

    private static final String LOG_TAG = DynamicPagerAdapter.class.getSimpleName();

    public DynamicPagerAdapter(
            FragmentManager fm,
            List<String> pageTitles,
            List<Class> fragmentTypes) {
        super(fm);
        this.mPageTitles = pageTitles;
        this.mFragmentTypes = fragmentTypes;
    }

    @Override
    public int getCount() {

        if (mFragmentTypes != null) {
            return mFragmentTypes.size();
        }

        return 0;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        if (mFragmentTypes != null &&
                position >= 0 &&
                position < mFragmentTypes.size()) {

            Class c = mFragmentTypes.get(position);

            try {
                fragment = (Fragment)Class.forName(c.getName()).newInstance();
            }
            catch (Exception ex) {
                Log.e(LOG_TAG, ex.toString());
            }
        }

        return fragment;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if (mPageTitles != null &&
                position >= 0 &&
                position < mPageTitles.size()) {
            return mPageTitles.get(position);
        }

        return null;
    }

}
