package com.devhind.qibla.refg.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.devhind.qibla.refg.R;
import com.devhind.qibla.refg.fragment.ProgressOrderFragment;
import com.devhind.qibla.refg.fragment.SuggestedOrderFragment;
import com.devhind.qibla.refg.utilites.Constants;
import com.devhind.qibla.refg.utilites.PreferenceManager;


public class ViewPagerAdapter extends FragmentPagerAdapter {

    PreferenceManager preferenceManager ;
    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;


    public ViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        preferenceManager = new PreferenceManager(context);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new SuggestedOrderFragment();

        switch (position){
            case 0 : fragment =new ProgressOrderFragment();
            break;
            case 1 : fragment =new SuggestedOrderFragment(); break;

        }
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}