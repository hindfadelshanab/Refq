package com.devhind.qibla.refg.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devhind.qibla.refg.R;
import com.devhind.qibla.refg.View.ChatActivity;
import com.devhind.qibla.refg.View.ChatDoctorActivity;
import com.devhind.qibla.refg.databinding.ItemContainerRecentConversionBinding;
import com.devhind.qibla.refg.listeners.ConversationListener;
import com.devhind.qibla.refg.model.ChatMessage;
import com.devhind.qibla.refg.model.Doctor;
import com.devhind.qibla.refg.model.User;
import com.devhind.qibla.refg.utilites.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


import java.util.List;

public class RecentConversationAdapter extends RecyclerView.Adapter<RecentConversationAdapter.ConversationViewHolder> {
    private final List<ChatMessage> chatMessageList;
    private ConversationListener conversationListener;
    private Context context;
    private Boolean isDoctor = false;
    FirebaseFirestore database;

    public RecentConversationAdapter(List<ChatMessage> chatMessageList, Context context ,boolean isDoctor) {
        this.chatMessageList = chatMessageList;
        this.context = context;
        this.isDoctor =isDoctor;
        database = FirebaseFirestore.getInstance();
        //  this.conversationListener = conversationListener;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversationViewHolder(
                ItemContainerRecentConversionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        holder.setData(chatMessageList.get(position));

    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }


    class ConversationViewHolder extends RecyclerView.ViewHolder {
        ItemContainerRecentConversionBinding binding;

        ConversationViewHolder(ItemContainerRecentConversionBinding itemContainerRecentConversionBinding) {
            super(itemContainerRecentConversionBinding.getRoot());
            binding = itemContainerRecentConversionBinding;

        }


        void setData(ChatMessage chatMessage) {
            if (chatMessage.receiverImage!=null) {
                Picasso.get().load(chatMessage.receiverImage)
                        .into(binding.imageProfile);
            }

            // binding.imageProfile.setImageBitmap(getConversationImage(chatMessage.converstionImage));
            binding.textRecentMessage.setText(chatMessage.message);
            binding.textName.setText(chatMessage.receiverName);

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chatMessage.getReceiverId();


                    if (!isDoctor) {
                        Intent i = new Intent(context, ChatActivity.class);
                        i.putExtra("receiverId", chatMessage.getReceiverId());
                        i.putExtra("senderId", chatMessage.getReceiverId());
                      //  context.startActivity(i);
                        Log.e("is  not doctor" , isDoctor +"{jjdkldlk}");
                        database.collection(Constants.KEY_COLLECTION_DOCTOR)
                                .document(chatMessage.getReceiverId())
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Doctor doctor =   documentSnapshot.toObject(Doctor.class);
                                        i.putExtra("token" , documentSnapshot.getString("fcmToken"));
                                        context.startActivity(i);
                                        Log.e("oldddd" , doctor.getId() +"{jjdkldlk}");
                                        Log.e("oldddd" , doctor.getFcmToken() +"{jjdkldlk}");
                                        Log.e("oldddd" , documentSnapshot.getString("fcmToken") +"{tolen jjdkldlk}");



                                    }
                                });

                        Toast.makeText(context, "reccc"+ chatMessageList.size() +"size", Toast.LENGTH_SHORT).show();

                    }else if (isDoctor){
                        Intent i = new Intent(context, ChatActivity.class);
                        i.putExtra("receiverId", chatMessage.getSenderId());
                        i.putExtra("receiverName",chatMessage.getReceiverName());
                        Log.e("isdoctor" , isDoctor +"{jjdkldlk}");
                        Log.e("isdoctor" , chatMessage.getReceiverId() +"{jjdkldlk}");
                        binding.imageProfile.setImageResource(R.drawable.ic_profile);
                        database.collection(Constants.KEY_COLLECTION_OLDER)
                                        .document(chatMessage.getSenderId())
                                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                     User old =   documentSnapshot.toObject(User.class);
                                     old.getToken();
                                     i.putExtra("token" , documentSnapshot.getString("fcmToken"));
                                        context.startActivity(i);
                                        Log.e("oldddd" , old.getId() +"{jjdkldlk}");
                                        Log.e("oldddd" , documentSnapshot.getString("fcmToken") +"{tolen jjdkldlk}");



                                    }
                                });



                    }

                }
            });
//            binding.getRoot().setOnClickListener(view -> {
//
//                User user = new User();
//                user.setId(chatMessage.converstionId);
//                user.setImage(chatMessage.converstionImage);
//                user.setName(chatMessage.converstionName);
////                conversationListener.onConversationClicked(user);
//            });

        }
    }

//        private Bitmap getConversationImage(String encodedImage) {
//            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
//            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//        }


}
