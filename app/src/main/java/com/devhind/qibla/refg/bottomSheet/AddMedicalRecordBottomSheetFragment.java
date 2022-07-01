package com.devhind.qibla.refg.bottomSheet;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.devhind.qibla.refg.R;
import com.devhind.qibla.refg.utilites.Constants;
import com.devhind.qibla.refg.utilites.PreferenceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AddMedicalRecordBottomSheetFragment extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    //    private ItemClickListener mListener;
    FirebaseFirestore database;
    PreferenceManager preferenceManager;


    public static AddMedicalRecordBottomSheetFragment newInstance() {
        return new AddMedicalRecordBottomSheetFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.add_medical_record_bottom_sheet, container, false);

        database = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getContext());
        EditText input_medical_record = root.findViewById(R.id.input_medical_record);

        root.findViewById(R.id.btn_add_medical_record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMedicalRecord(preferenceManager.getString(Constants.KEY_USER_ID), input_medical_record.getText().toString());

            }
        });

        root.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();

            }
        });


        return root;
    }

    private void addMedicalRecord(String oldId, String txtRecorde) {
        HashMap<String , String> map = new HashMap<>();
        map.put("txtRecorde" , txtRecorde);
        database.collection(Constants.KEY_COLLECTION_OLDER).document(oldId).collection("MedicalRecord")
                .add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getContext(), "Success Added", Toast.LENGTH_SHORT).show();

                    }
                });


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }
//    @Override public void onClick(View view) {
//        Button btnAddMedicalRecord = (Button) view;
//        EditText editTextInputMMeicalText = (EditText) view;
//      //  mListener.onItemClick(tvSelected.getText().toString());
//        dismiss();
//    }
//    public interface ItemClickListener {
//        void onItemClick(String item);
//    }

}