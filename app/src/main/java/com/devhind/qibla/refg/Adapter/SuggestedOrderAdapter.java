package com.devhind.qibla.refg.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devhind.qibla.refg.Older.OrderDetialsActivity;
import com.devhind.qibla.refg.databinding.OrderPendingItemBinding;
import com.devhind.qibla.refg.model.Doctor;
import com.devhind.qibla.refg.model.Order;
import com.devhind.qibla.refg.model.Request;
import com.devhind.qibla.refg.utilites.Constants;
import com.devhind.qibla.refg.utilites.PreferenceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

public class SuggestedOrderAdapter extends RecyclerView.Adapter<SuggestedOrderAdapter.OrderViewHolder> {
    OrderPendingItemBinding binding;
    private List<Order> orders;
    private Boolean isDoctor;
    private Boolean isProgress;
    FirebaseFirestore database;
    PreferenceManager preferenceManager;
    private Context context;


    public SuggestedOrderAdapter(Context context, List<Order> orders) {
        this.orders = orders;
        this.isDoctor = isDoctor;
        this.isProgress = isProgress;
        database = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(context);
        this.context = context;
    }
    //private UserListener userListener;


//    private Bitmap getUserImage(String encodedImage){
//        byte[] bytes = Base64.decode(encodedImage,Base64.DEFAULT);
//        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//    }


    class OrderViewHolder extends RecyclerView.ViewHolder {
        OrderViewHolder(OrderPendingItemBinding itemContainerUserBinding) {
            super(itemContainerUserBinding.getRoot());
            binding = itemContainerUserBinding;


        }

        void setUserData(Order order) {
            binding.txtServiceType.setText(order.getServiceType());
            binding.txtOrderStartTime.setText(order.getStartTime());
            binding.txtOrderEndTime.setText(order.getEndTime());
            binding.txtOrderLocation.setText(order.getCity());
            binding.txtOrderDate.setText(order.getDate());


            database.collection(Constants.KEY_COLLECTION_ORDERS).document(order.getOrderId())
                    .collection(Constants.KEY_COLLECTION_REQUEST).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            binding.txtNumberDocotr.setText(String.valueOf(queryDocumentSnapshots.size()));
                        }
                    });
//            binding.cardRequest.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (!isDoctor) {
//                        Intent intent = new Intent(context, OrderDetialsActivity.class);
//                        intent.putExtra("Order", order);
//                        context.startActivity(intent);
//                    }
//                }
//            });
           // if (isDoctor) {
                binding.btnShowAddedOrders.setText("التقديم على العرض");
         //   } else {
              //  binding.btnShowAddedOrders.setText("العروض المضافة");

           // }
       if (isProgress){
           binding.btnShowAddedOrders.setText("الغاء الطلب");
       }

            binding.btnShowAddedOrders.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (binding.btnShowAddedOrders.getText().equals("التقديم على العرض")) {

                        addRequestToOrder(order);



                    } else if (binding.btnShowAddedOrders.getText().equals("التقديم على العرض")) {
                        if (!isDoctor) {
                            Intent intent = new Intent(context, OrderDetialsActivity.class);
                            intent.putExtra("Order", order);
                            context.startActivity(intent);
                        }
                    }else if(binding.btnShowAddedOrders.getText().equals("الغاء الطلب")){
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        builder1.setMessage("Are you sure to cancel order?");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        cancelRequestToOrder(order);

                                    }
                                });

                        builder1.setNegativeButton(
                                "No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();

                    }
                }
            });


        }


    }


    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderPendingItemBinding orderPendingItemBinding = OrderPendingItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new OrderViewHolder(orderPendingItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.setUserData(orders.get(position));

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    private void addRequestToOrder(Order order) {
        if (preferenceManager.getBoolean(Constants.KEY_IS_Doctor)) {
            database.collection(Constants.KEY_COLLECTION_DOCTOR)
                    .document(preferenceManager.getString(Constants.KEY_USER_ID))
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Doctor user = documentSnapshot.toObject(Doctor.class);
                            Request request = new Request();
                            request.setOldId(order.getOldId());
                            request.setAccept(false);
                            request.setDoctorId(user.getId());
                            request.setDoctorRequest(user);
                            request.setOrderID(order.getOrderId());
                            getToken();

                            DocumentReference ref = database.collection(Constants.KEY_COLLECTION_ORDERS).document(order.getOrderId())
                                    .collection(Constants.KEY_COLLECTION_REQUEST).document();
                            request.setRequestId(ref.getId());
                            ref.set(request)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context, "request added", Toast.LENGTH_SHORT).show();


                                        }
                                    });


                        }
                    });
        }


    }
    private void cancelRequestToOrder(Order order) {
        if (preferenceManager.getBoolean(Constants.KEY_IS_Doctor)) {
            database.collection(Constants.KEY_COLLECTION_DOCTOR)
                    .document(preferenceManager.getString(Constants.KEY_USER_ID))
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Doctor user = documentSnapshot.toObject(Doctor.class);

                         database.collection(Constants.KEY_COLLECTION_ORDERS).document(order.getOrderId())
                                    .collection(Constants.KEY_COLLECTION_REQUEST)
                                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                Request request = documentSnapshot.toObject(Request.class);
                                                if (request.getDoctorRequest().getId()
                                                        .equals(user.getId())) {

                                                    Log.e("reid" , request.getRequestId()+"jkkkk");

                                                    database.collection(Constants.KEY_COLLECTION_ORDERS).document(order.getOrderId()).collection(Constants.KEY_COLLECTION_REQUEST)
                                                            .document(request.getRequestId())
                                                            .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    Toast.makeText(context, "Order Cancel", Toast.LENGTH_SHORT).show();

                                                                }
                                                            });

                                                }
                                            }
                                        }
                                    });
//                                    .document();
//                            request.setRequestId(ref.getId());
//                            ref.set(request)
//                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void unused) {
//                                            Toast.makeText(context, "request added", Toast.LENGTH_SHORT).show();
//
//                                        }
//                                    });


                        }
                    });
        }


    }
    public void getToken() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token) {
        preferenceManager.putString(Constants.KEY_FCM_TOKEN,token);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_DOCTOR).document(
                preferenceManager.getString(Constants.KEY_USER_ID)
        );
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnSuccessListener(unsend ->
                        Log.e("tok","Token update Successfully"))
                .addOnFailureListener(e ->     Log.e("tok","Unable to update token"));


    }

}
