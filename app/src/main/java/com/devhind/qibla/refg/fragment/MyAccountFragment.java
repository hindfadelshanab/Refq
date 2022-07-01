package com.devhind.qibla.refg.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devhind.qibla.refg.View.SignScreen.CompleteSignUpActivity;
import com.devhind.qibla.refg.View.SignScreen.SignAsActivity;
import com.devhind.qibla.refg.View.SignScreen.SignInActivity;
import com.devhind.qibla.refg.databinding.FragmentMyAccountBinding;
import com.devhind.qibla.refg.model.Doctor;
import com.devhind.qibla.refg.utilites.Constants;
import com.devhind.qibla.refg.utilites.PreferenceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.protobuf.Any;
import com.squareup.picasso.Picasso;

import java.util.HashMap;


public class MyAccountFragment extends Fragment {


    PreferenceManager preferenceManager;
    FirebaseFirestore db;
    FragmentMyAccountBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyAccountBinding.inflate(inflater, container, false);

        db = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getActivity());
        getDoctorInfo(preferenceManager.getString(Constants.KEY_USER_ID));
        binding.cardSignOu.setOnClickListener(view -> signOut());


        return binding.getRoot();
    }

    private void getDoctorInfo(String doctorId) {
        db.collection(Constants.KEY_COLLECTION_DOCTOR)
                .document(doctorId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                      Doctor doctor =  documentSnapshot.toObject(Doctor.class);
                      if (doctor!=null) {
                          binding.txtDoctoNamerProfile.setText(doctor.getName());
                          Picasso.get().load(doctor.getImage())
                                  .into(binding.imageDoctorProfile);

                          if (doctor.getCv()!=null){
                              binding.txtDoctoCvProfile.setText(doctor.getCv());

                          }else {
                              binding.txtDoctoCvProfile.setVisibility(View.GONE);
                          }
                      }
                        binding.cardUpdateProfile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(getActivity() , CompleteSignUpActivity.class);
                                intent.putExtra("DoctorProfile" , doctor);
                                getActivity().startActivity(intent);

                            }
                        });

                    }
                });
    }
    private void  signOut() {
        Toast.makeText(getActivity(), "signing out ....", Toast.LENGTH_SHORT).show();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_DOCTOR).document(
                preferenceManager.getString(Constants.KEY_USER_ID)
        );

            preferenceManager.clear();
            startActivity(new Intent(getActivity(), SignAsActivity.class));
            // finish()

    }

}