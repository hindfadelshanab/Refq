package com.devhind.qibla.refg.Older.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devhind.qibla.refg.Adapter.OrderViewPagerAdapter;
import com.devhind.qibla.refg.Adapter.ViewPagerAdapter;
import com.devhind.qibla.refg.R;
import com.devhind.qibla.refg.databinding.FragmentHomeBinding;
import com.devhind.qibla.refg.databinding.FragmentOldOrderBinding;

public class OldOrderFragment extends Fragment {

    FragmentOldOrderBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOldOrderBinding.inflate(inflater , container,false);
        OrderViewPagerAdapter viewPagerAdapter = new OrderViewPagerAdapter(getActivity(), getChildFragmentManager());
        binding.viewPagerOrder.setAdapter(viewPagerAdapter);
        binding.orderTabs.setupWithViewPager( binding.viewPagerOrder);
        return  binding.getRoot();
    }

}