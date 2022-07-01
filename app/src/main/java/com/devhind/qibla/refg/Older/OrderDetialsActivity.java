package com.devhind.qibla.refg.Older;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.devhind.qibla.refg.Adapter.RequestAdapter;
import com.devhind.qibla.refg.R;
import com.devhind.qibla.refg.databinding.ActivityOrderDetialsBinding;
import com.devhind.qibla.refg.model.Doctor;
import com.devhind.qibla.refg.model.Order;
import com.devhind.qibla.refg.model.Request;
import com.devhind.qibla.refg.model.User;
import com.devhind.qibla.refg.utilites.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class OrderDetialsActivity extends AppCompatActivity {

    Order order ;
    FirebaseFirestore database;
    ActivityOrderDetialsBinding binding ;
    RequestAdapter requestAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetialsBinding.inflate(getLayoutInflater() );
        database = FirebaseFirestore.getInstance();

        setContentView(binding.getRoot());
        order = getIntent().getParcelableExtra("Order");

        binding.txtServiceType.setText(order.getServiceType());
        binding.txtOrderStartTime.setText(order.getStartTime());
        binding.txtOrderEndTime.setText(order.getEndTime());
        binding.txtOrderDate.setText(order.getDate());
        getAllRequest(order.getOrderId());


    }

    private void getAllRequest(String orderId){
        database.collection(Constants.KEY_COLLECTION_ORDERS)
                .document(orderId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        database.collection(Constants.KEY_COLLECTION_ORDERS)
                                .document(orderId).collection("Request")
                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                                        requestAdapter = new RequestAdapter(OrderDetialsActivity.this , queryDocumentSnapshots.toObjects(Request.class)) ;
                                        binding.requestRc.setLayoutManager(new LinearLayoutManager(OrderDetialsActivity.this));

                                        Log.e("reeeee",queryDocumentSnapshots.size()+"sizeeee");
                                        binding.requestRc.setAdapter(requestAdapter);
                                    }
                                });

                    }
                });

    }
}