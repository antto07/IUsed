package com.iused.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iused.R;


/**
 * Created by Antto on 05-10-2016.
 */
public class DonateFragment extends Fragment {

    View view;
    private FragmentTabHost tabHost;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        tabHost = new FragmentTabHost(getActivity());
        tabHost.setup(getActivity(), getChildFragmentManager(), R.layout.fragment_donate);

        Bundle arg2 = new Bundle();
        arg2.putInt("Arg for Frag2", 1);
        tabHost.addTab(tabHost.newTabSpec("Tab2").setIndicator("Donated by Others"),
                Dontated_By_Others_Fragment.class, arg2);

        Bundle arg1 = new Bundle();
        arg1.putInt("Arg for Frag1", 2);
        tabHost.addTab(tabHost.newTabSpec("Tab1").setIndicator("I Donate"),
                Donated_Products_Fragment.class, arg1);


        Bundle arg3 = new Bundle();
        arg3.putInt("Arg for Frag3", 3);
        tabHost.addTab(tabHost.newTabSpec("Tab3").setIndicator("Donor Responses"),
                Donor_Responses_Fragment.class, arg3);

        return tabHost;
    }
}
