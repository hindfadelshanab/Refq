package com.devhind.qibla.refg.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devhind.qibla.refg.Adapter.OrderAdapter;
import com.devhind.qibla.refg.R;
import com.devhind.qibla.refg.databinding.FragmentProgressOrderBinding;
import com.devhind.qibla.refg.model.Order;
import com.devhind.qibla.refg.model.Request;
import com.devhind.qibla.refg.utilites.Constants;
import com.devhind.qibla.refg.utilites.PreferenceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProgressOrderFragment extends Fragment {

    FragmentProgressOrderBinding binding;
    FirebaseFirestore database;
    OrderAdapter orderAdapter;
    PreferenceManager preferenceManager;
    ArrayList<Order> orders;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProgressOrderBinding.inflate(inflater, container, false);
        database = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getActivity());
        String userId =preferenceManager.getString(Constants.KEY_USER_ID);
        orders = new ArrayList<>();


        getPendingOrder(userId);

        return binding.getRoot();
    }

    private void getPendingOrder(String doctorId) {

        binding.progressbar.setVisibility(View.VISIBLE);
        database.collection(Constants.KEY_COLLECTION_ORDERS).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Order order = documentSnapshot.toObject(Order.class);

                    order.getOrderId();
                    database.collection(Constants.KEY_COLLECTION_ORDERS).document(order.getOrderId())
                            .collection("Request")

                            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        Request request = documentSnapshot.toObject(Request.class);
                                        Log.e("request", request.getRequestId() + "dddd");
                                        Log.e("request", request.getAccept() + "dddd");
                                        if (request.getAccept() && request.getDoctorRequest().getId()
                                                .equals(doctorId)) {
                                            orders.add(order);
                                            Log.e("size", order.getOrderId() + "(jsjd)");

                                        }
                                        Log.e("doctor id", request.getDoctorRequest().getId() + "(doctor id)");
                                        Log.e("doctor id", doctorId + "(doctor id)");

                                    }
                                    Log.e("size2", order.getOrderId() + "(jsjd)");

                                    Log.e("size", orders.size() + "(jsjd)");
                                    binding.progressbar.setVisibility(View.GONE);
                                    binding.ProccessOrdersRc.setVisibility(View.VISIBLE);

                                    orderAdapter = new OrderAdapter(getActivity(), orders, true ,true ,false);
                                    binding.ProccessOrdersRc.setAdapter(orderAdapter);
                                    binding.ProccessOrdersRc.setLayoutManager(new LinearLayoutManager(getContext()));
                                    Toast.makeText(getContext(), "pending frss" + orders.size(), Toast.LENGTH_SHORT).show();
                                }
                            });

//                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                            @Override
//                            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                              Request request=  documentSnapshot.toObject(Request.class);
//                                Log.e("request" , request.getRequestId())
////                              if (request.getAccept() && request.getDoctorRequest().getId().equals(doctorId)){
////                                  orders.add(order);
////                              }
//
//
//                            }
//                        });
                }


            }
        });
    }
}