package com.devhind.qibla.refg.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devhind.qibla.refg.Adapter.NotificationAdapter;
import com.devhind.qibla.refg.Adapter.OrderAdapter;
import com.devhind.qibla.refg.R;
import com.devhind.qibla.refg.databinding.FragmentNotifcationBinding;
import com.devhind.qibla.refg.databinding.NotificationItemBinding;
import com.devhind.qibla.refg.model.Notfication;
import com.devhind.qibla.refg.utilites.Constants;
import com.devhind.qibla.refg.utilites.PreferenceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class NotifcationFragment extends Fragment {


    FirebaseFirestore database ;
    NotificationAdapter notificationAdapter;
    PreferenceManager preferenceManager ;

    FragmentNotifcationBinding binding ;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNotifcationBinding.inflate(inflater , container,false);
        preferenceManager=new PreferenceManager(getContext());

        database = FirebaseFirestore.getInstance();
        getAllNotifiaction(preferenceManager.getString(Constants.KEY_USER_ID));
        return binding.getRoot();
    }
    private  void  getAllNotifiaction(String userId){
        database.collection(Constants.KEY_COLLECTION_NOTIFICATION).limit(20)
                .whereEqualTo("receiverId" ,userId)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        notificationAdapter = new NotificationAdapter(getActivity(), queryDocumentSnapshots.toObjects(Notfication.class));

                        binding.notificationRc.setAdapter(notificationAdapter);
                        binding.notificationRc.setLayoutManager(new LinearLayoutManager(getActivity()));

                    }
                });

    }
}