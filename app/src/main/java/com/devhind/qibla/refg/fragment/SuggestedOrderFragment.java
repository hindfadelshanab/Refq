package com.devhind.qibla.refg.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devhind.qibla.refg.Adapter.OrderAdapter;
import com.devhind.qibla.refg.databinding.FragmentSuggestedOrderBinding;
import com.devhind.qibla.refg.model.Order;
import com.devhind.qibla.refg.model.Request;
import com.devhind.qibla.refg.utilites.Constants;
import com.devhind.qibla.refg.utilites.PreferenceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class SuggestedOrderFragment extends Fragment {
    FirebaseFirestore database;
    OrderAdapter orderAdapter;
    FragmentSuggestedOrderBinding binding;
    ArrayList<Order> orders;
    PreferenceManager preferenceManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSuggestedOrderBinding.inflate(inflater, container, false);
        database = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getContext());

        orders = new ArrayList<>();

        getSuggestedOrder();

        return binding.getRoot();
    }

    private void getSuggestedOrder() {
        orders.clear();
        binding.progressbar.setVisibility(View.VISIBLE);

        database.collection(Constants.KEY_COLLECTION_ORDERS).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Order order = documentSnapshot.toObject(Order.class);
                    if (!order.getAccept()) {

                        orders.add(order);
                    }
//
//                    database.collection(Constants.KEY_COLLECTION_ORDERS)
////                            .document(order.getOrderId())
////                            .collection("Request")
//
//                            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                                @Override
//                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                                        Order order1 = documentSnapshot.toObject(Order.class);
//                                        if (!order1.getAccept()) {
//
//                                            orders.add(order1);
//                                        }
//
//
////                                        Request request = documentSnapshot.toObject(Request.class);
////                                        Log.e("request", request.getRequestId() + "dddd");
////                                        Log.e("request", request.getAccept() + "dddd");
////                                        if (!request.getDoctorRequest().getId()
////                                                .equals(preferenceManager.getString(Constants.KEY_USER_ID))) {
////                                            orders.add(order);
////                                            Log.e("size", order.getOrderId() + "(jsjd)");
////
////                                        }
////                                        Log.e("doctor id", request.getDoctorRequest().getId() + "(doctor id)");
//
//                                    }
//                                    Log.e("size2", order.getOrderId() + "(jsjd)");
//
//
//                                }
//                            });
                }
                binding.progressbar.setVisibility(View.GONE);
                binding.OrdersRc.setVisibility(View.VISIBLE);

                orderAdapter = new OrderAdapter(getActivity(), orders, true, false, false);
                binding.OrdersRc.setAdapter(orderAdapter);
                binding.OrdersRc.setLayoutManager(new LinearLayoutManager(getContext()));
                Toast.makeText(getContext(), "pending frss" + orders.size(), Toast.LENGTH_SHORT).show();

            }
        });

//        database.collection(Constants.KEY_COLLECTION_ORDERS).get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//
//                ArrayList<Order> orders = (ArrayList<Order>) queryDocumentSnapshots.toObjects(Order.class);
//                orderAdapter = new OrderAdapter(getActivity() , orders , true) ;
//                binding.OrdersRc.setAdapter(orderAdapter);
//                binding.OrdersRc.setLayoutManager(new LinearLayoutManager(getContext()));
//                Toast.makeText(getContext(), "pending frss" +orders.size(), Toast.LENGTH_SHORT).show();
//
//            }
//        });
    }

}