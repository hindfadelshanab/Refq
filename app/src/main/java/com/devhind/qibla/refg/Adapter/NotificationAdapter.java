package com.devhind.qibla.refg.Adapter;

import android.app.Notification;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devhind.qibla.refg.databinding.MedicalRecordItemBinding;
import com.devhind.qibla.refg.databinding.NotificationItemBinding;
import com.devhind.qibla.refg.model.Notfication;
import com.devhind.qibla.refg.utilites.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.OrderViewHolder> {
    NotificationItemBinding binding;
    private List<Notfication> notfications;
    FirebaseFirestore database ;
    PreferenceManager preferenceManager ;
    private Context context ;


    public NotificationAdapter(Context context , List<Notfication> notfications ) {
        this.notfications = notfications;
        database = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(context);
        this.context=context;
    }




    class OrderViewHolder extends  RecyclerView.ViewHolder{
        OrderViewHolder(NotificationItemBinding itemContainerUserBinding){
            super(itemContainerUserBinding.getRoot());
            binding = itemContainerUserBinding;


        }

        void setUserData(Notfication notfication){
            binding.txtMessage.setText(notfication.getMessage());




        }


}



    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NotificationItemBinding orderPendingItemBinding = NotificationItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),parent,false
        );
        return new OrderViewHolder(orderPendingItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.setUserData(notfications.get(position));

    }

    @Override
    public int getItemCount() {
        return notfications.size();
    }

}
