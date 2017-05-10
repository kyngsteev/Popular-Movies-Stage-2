package com.example.android.popularmovies.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.android.popularmovies.ui.fragment.OverviewFragment;
import com.example.android.popularmovies.ui.fragment.ReviewFragment;
import com.example.android.popularmovies.ui.fragment.TrailerFragment;

/**
 * Created by Omoarukhe on 07/05/2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                OverviewFragment tab1 = new OverviewFragment();
                return tab1;
            case 1:
                TrailerFragment tab2 = new TrailerFragment();
                return tab2;
            case 2:
                ReviewFragment tab3 = new ReviewFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "OVERVIEW";
            case 1:
                return "TRAILERS";
            case 2:
                return "REVIEW";
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
