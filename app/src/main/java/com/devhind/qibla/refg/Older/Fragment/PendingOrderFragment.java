package com.devhind.qibla.refg.Older.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devhind.qibla.refg.Adapter.OrderAdapter;
import com.devhind.qibla.refg.R;
import com.devhind.qibla.refg.databinding.FragmentPendingOrderBinding;
import com.devhind.qibla.refg.model.Order;
import com.devhind.qibla.refg.utilites.Constants;
import com.devhind.qibla.refg.utilites.PreferenceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PendingOrderFragment extends Fragment {


    FragmentPendingOrderBinding binding ;

    FirebaseFirestore database ;
    OrderAdapter orderAdapter;
    PreferenceManager preferenceManager ;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPendingOrderBinding.inflate(inflater , container , false);

        database = FirebaseFirestore.getInstance();
        preferenceManager=new PreferenceManager(getActivity());
        getPendingOrder(preferenceManager.getString(Constants.KEY_USER_ID));

        return binding.getRoot();
    }

    private void getPendingOrder(String userId){
        binding.progressbar.setVisibility(View.VISIBLE);

        ArrayList<Order>   orders = new ArrayList();
        database.collection(Constants.KEY_COLLECTION_ORDERS).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                      Order order = document.toObject(Order.class);

                        if (order.getOldId().equals(userId) && !order.getAccept()) {

                          orders.add(order);
                      }
                    }

                    binding.progressbar.setVisibility(View.GONE);
                    binding.pendingOrderRc.setVisibility(View.VISIBLE);
                orderAdapter = new OrderAdapter(getActivity() ,orders , false ,false ,false);
                binding.pendingOrderRc.setAdapter(orderAdapter);
                binding.pendingOrderRc.setLayoutManager(new LinearLayoutManager(getContext()));

            }
        });
    }
}