package com.devhind.qibla.refg.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devhind.qibla.refg.View.ChatActivity;
import com.devhind.qibla.refg.databinding.RequsetItemBinding;
import com.devhind.qibla.refg.model.Request;
import com.devhind.qibla.refg.utilites.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.OrderViewHolder> {
    RequsetItemBinding binding;
    private List<Request> doctors;
    private Boolean isDoctor ;
    FirebaseFirestore database ;
    PreferenceManager preferenceManager ;
    private Context context ;


    public RequestAdapter(Context context , List<Request> doctors ) {
        this.doctors = doctors;
        this.isDoctor =isDoctor;
        database = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(context);
        this.context=context;
    }
    //private UserListener userListener;


//    private Bitmap getUserImage(String encodedImage){
//        byte[] bytes = Base64.decode(encodedImage,Base64.DEFAULT);
//        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//    }




    class OrderViewHolder extends  RecyclerView.ViewHolder{
        OrderViewHolder(RequsetItemBinding requsetItemBinding){
            super(requsetItemBinding.getRoot());
            binding = requsetItemBinding;

        }

        void setUserData(Request request){

            binding.txtDoctorNameRequest.setText(request.getDoctorRequest().getName());
            binding.txtDoctorAddressRequest.setText(request.getDoctorRequest().getCity() + ","+request.getDoctorRequest().getAddress());

            binding.txtDocotrCv.setText(request.getDoctorRequest().getCv());
            Picasso.get().load(request.getDoctorRequest().getImage()).into(binding.imageDoctorRequest);
            binding.imgeStartChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context , ChatActivity.class);
                    intent.putExtra("Request" , request);
                    intent.putExtra("token" , request.getDoctorRequest().getFcmToken());
                    Log.e("jooood" ,request.getRequestId()+"sss");
                    Log.e("jooood" ,request.getDoctorRequest().getId()+"sss");
                    Log.e("jooood" ,request.getDoctorRequest().getFcmToken()+"sss");
                    context.startActivity(intent);

                }
            });


        }


}



    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RequsetItemBinding requsetItemBinding = RequsetItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),parent,false
        );
        return new OrderViewHolder(requsetItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.setUserData(doctors.get(position));

    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }
//    private  void addRequsetToOrder(String orderId){
//
//        if (preferenceManager.getBoolean(Constants.KEY_IS_Doctor)){
//            database.collection(Constants.KEY_COLLECTION_DOCTOR)
//                    .document(preferenceManager.getString(Constants.KEY_USER_ID))
//                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                        @Override
//                        public void onSuccess(DocumentSnapshot documentSnapshot) {
//                          User user =   documentSnapshot.toObject(User.class);
//                            database.collection(Constants.KEY_COLLECTION_ORDERS).document(orderId)
//                                    .collection(Constants.KEY_COLLECTION_REQUEST).add(user)
//                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                        @Override
//                                        public void onSuccess(DocumentReference documentReference) {
//                                            Toast.makeText(context, "request added", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//
//                        }
//                    });
//        }
//
//
//    }
}
