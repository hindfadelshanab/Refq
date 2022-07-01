package com.devhind.qibla.refg.Older.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devhind.qibla.refg.R;
import com.devhind.qibla.refg.View.OnlineEventActivity;
import com.devhind.qibla.refg.bottomSheet.ChooseCityBottomSheetFragment;
import com.devhind.qibla.refg.databinding.FragmentChooseCityBottomSheetBinding;
import com.devhind.qibla.refg.databinding.FragmentOlderHomeBinding;
import com.devhind.qibla.refg.utilites.Constants;
import com.devhind.qibla.refg.utilites.PreferenceManager;

public class OldHomeFragment extends Fragment implements ChooseCityBottomSheetFragment.ItemClickListener {


    FragmentOlderHomeBinding binding ;
    PreferenceManager preferenceManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentOlderHomeBinding.inflate(inflater , container,false);
        preferenceManager = new PreferenceManager(getContext());
        binding.conss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet(view , binding.txtHealthService.getText().toString());
            }
        });

        binding.btnSubscrib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity() , OnlineEventActivity.class);
                intent.putExtra("oldId" , preferenceManager.getString(Constants.KEY_USER_ID) ) ;
                startActivity(intent);
            }
        });
        return binding.getRoot();
    }

    public void showBottomSheet(View view , String serviceType) {
        ChooseCityBottomSheetFragment chooseCityBottomSheetFragment =
                ChooseCityBottomSheetFragment.newInstance();
        chooseCityBottomSheetFragment.show(getActivity().getSupportFragmentManager(),
                ChooseCityBottomSheetFragment.TAG);
        
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_SRVICE_TYPE,"الرعاية الصحية");
        chooseCityBottomSheetFragment.setArguments(bundle);

    }
    @Override
    public void onItemClick(String item) {
        Toast.makeText(getActivity(), "cityy" +item, Toast.LENGTH_SHORT).show();

    }
}