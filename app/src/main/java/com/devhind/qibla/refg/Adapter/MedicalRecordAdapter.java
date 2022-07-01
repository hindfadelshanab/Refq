package com.devhind.qibla.refg.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devhind.qibla.refg.Older.OrderDetialsActivity;
import com.devhind.qibla.refg.databinding.MedicalRecordItemBinding;
import com.devhind.qibla.refg.databinding.OrderPendingItemBinding;
import com.devhind.qibla.refg.model.Order;
import com.devhind.qibla.refg.model.User;
import com.devhind.qibla.refg.utilites.Constants;
import com.devhind.qibla.refg.utilites.PreferenceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MedicalRecordAdapter extends RecyclerView.Adapter<MedicalRecordAdapter.OrderViewHolder> {
    MedicalRecordItemBinding binding;
    private List<String> medicalRecords;
    FirebaseFirestore database ;
    PreferenceManager preferenceManager ;
    private Context context ;


    public MedicalRecordAdapter(Context context , List<String> medicalRecords ) {
        this.medicalRecords = medicalRecords;
        database = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(context);
        this.context=context;
    }




    class OrderViewHolder extends  RecyclerView.ViewHolder{
        OrderViewHolder(MedicalRecordItemBinding itemContainerUserBinding){
            super(itemContainerUserBinding.getRoot());
            binding = itemContainerUserBinding;


        }

        void setUserData(String medical){
            binding.txtMedicalRecord.setText(medical);




        }


}



    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MedicalRecordItemBinding orderPendingItemBinding = MedicalRecordItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),parent,false
        );
        return new OrderViewHolder(orderPendingItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.setUserData(medicalRecords.get(position));

    }

    @Override
    public int getItemCount() {
        return medicalRecords.size();
    }

}
