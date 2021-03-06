package com.devhind.qibla.refg.Adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.devhind.qibla.refg.Older.Fragment.CurrentOrderFragment;
import com.devhind.qibla.refg.Older.Fragment.DoneOrderFragment;
import com.devhind.qibla.refg.Older.Fragment.PendingOrderFragment;
import com.devhind.qibla.refg.R;
import com.devhind.qibla.refg.fragment.SuggestedOrderFragment;


public class OrderViewPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{ R.string.tab_text_order_2,R.string.tab_text_order_1 };
    private final Context mContext;

    public OrderViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new PendingOrderFragment();
        switch (position){
            case 0 : fragment =new DoneOrderFragment(); break;
            case 1 : fragment =new PendingOrderFragment(); break;

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