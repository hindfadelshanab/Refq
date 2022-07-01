package com.devhind.qibla.refg.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devhind.qibla.refg.Adapter.OrderAdapter;
import com.devhind.qibla.refg.Adapter.ViewPagerAdapter;
import com.devhind.qibla.refg.R;
import com.devhind.qibla.refg.View.OnlineEventActivity;
import com.devhind.qibla.refg.databinding.FragmentHomeBinding;
import com.devhind.qibla.refg.model.Doctor;
import com.devhind.qibla.refg.model.Order;
import com.devhind.qibla.refg.utilites.Constants;
import com.devhind.qibla.refg.utilites.PreferenceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class HomeFragment extends Fragment {


    FragmentHomeBinding binding ;
    FirebaseFirestore database ;
    OrderAdapter orderAdapter;
    PreferenceManager preferenceManager ;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater , container,false);
        database = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getActivity());

        String userId = preferenceManager.getString(Constants.KEY_USER_ID);

        getDoctorInfo(userId);

        binding.layoutAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity() , OnlineEventActivity.class));
            }
        });
     //   getPendingOrder();

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity(), getChildFragmentManager());
        binding.viewPager.setAdapter(viewPagerAdapter);
        binding.tabs.setupWithViewPager( binding.viewPager);
        return  binding.getRoot();
    }

    private  void getDoctorInfo(String userId){

        database.collection(Constants.KEY_COLLECTION_DOCTOR)
                .document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        Doctor doctor = documentSnapshot.toObject(Doctor.class);
                        binding.txtDoctorName.setText(doctor.getName());
                        Picasso.get().load(doctor.getImage())
                                .into(binding.imagDoctor);
                    }
                });
    }

//
//    private void getPendingOrder(){
//        database.collection(Constants.KEY_COLLECTION_ORDERS).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                ArrayList<Order> orders = (ArrayList<Order>) queryDocumentSnapshots.toObjects(Order.class);
//                orderAdapter = new OrderAdapter(getActivity() , orders , true) ;
//                binding.OrdersRc.setAdapter(orderAdapter);
//                binding.OrdersRc.setLayoutManager(new LinearLayoutManager(getContext()));
//                Toast.makeText(getContext(), "pending frss" +orders.size(), Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }
}