package com.devhind.qibla.refg.Older.Fragment;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.devhind.qibla.refg.Adapter.AlertAdapter;
import com.devhind.qibla.refg.Adapter.MedicalRecordAdapter;
import com.devhind.qibla.refg.Older.MainOlderActivity;
import com.devhind.qibla.refg.R;
import com.devhind.qibla.refg.bottomSheet.AddMedicalRecordBottomSheetFragment;
import com.devhind.qibla.refg.bottomSheet.CreateTaskBottomSheetFragment;
import com.devhind.qibla.refg.broadcastReceiver.AlarmBroadcastReceiver;
import com.devhind.qibla.refg.database.DatabaseClient;
import com.devhind.qibla.refg.databinding.FragmentOldProfileBinding;
import com.devhind.qibla.refg.model.Alert;
import com.devhind.qibla.refg.model.User;
import com.devhind.qibla.refg.utilites.Constants;
import com.devhind.qibla.refg.utilites.PreferenceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class OldProfileFragment extends Fragment implements CreateTaskBottomSheetFragment.setRefreshListener {
    //
//    @BindView(R.id.taskRecycler)
//    RecyclerView taskRecycler;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_add_more_alarm)
    TextView addTask;

    AlertAdapter taskAdapter;
    List<Alert> tasks = new ArrayList<>();
    FragmentOldProfileBinding binding;

    FirebaseFirestore database;
    PreferenceManager preferenceManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOldProfileBinding.inflate(inflater, container, false);

        database = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getActivity());

        ButterKnife.bind(getActivity());
        setUpAdapter();
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ComponentName receiver = new ComponentName(getActivity(), AlarmBroadcastReceiver.class);
        PackageManager pm = getActivity().getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        //Glide.with(getActivity()).load(R.drawable.first_note).into(noDataImage);

        binding.txtAddMoreAlarm.setOnClickListener(view -> {
            CreateTaskBottomSheetFragment createTaskBottomSheetFragment = new CreateTaskBottomSheetFragment();
            createTaskBottomSheetFragment.setTaskId(0, false, this, this);
            createTaskBottomSheetFragment.show(getActivity().getSupportFragmentManager(), createTaskBottomSheetFragment.getTag());

        });


        binding.txtAddMoreMedicalRecord.setOnClickListener(view -> {
            AddMedicalRecordBottomSheetFragment addMedicalRecordBottomSheetFragment = new AddMedicalRecordBottomSheetFragment();
            addMedicalRecordBottomSheetFragment.show(getActivity().getSupportFragmentManager(), addMedicalRecordBottomSheetFragment.getTag());
            getAllMedicalRecord(preferenceManager.getString(Constants.KEY_USER_ID));
            //addMedicalRecord(preferenceManager.getString(Constants.KEY_USER_ID));

        });

        getSavedTasks();
        getAllMedicalRecord(preferenceManager.getString(Constants.KEY_USER_ID));
getOldInfo(preferenceManager.getString(Constants.KEY_USER_ID));
//        calendar.setOnClickListener(view -> {
//            ShowCalendarViewBottomSheet showCalendarViewBottomSheet = new ShowCalendarViewBottomSheet();
//            showCalendarViewBottomSheet.show(getSupportFragmentManager(), showCalendarViewBottomSheet.getTag());
//        });
        return binding.getRoot();
    }
    private void getOldInfo(String oldId){
        database.collection(Constants.KEY_COLLECTION_OLDER)
                .document(oldId)
                .get().addOnSuccessListener(documentSnapshot -> {

                  User old =  documentSnapshot.toObject(User.class);

                  binding.txtOldName.setText(old.getName());
                  binding.txtOldAdderss.setText(old.getAddress());

                  binding.txtOldAge.setText(old.getAge()+"عام");
                  binding.txtOldAdderss.setText(old.getAddress());
                });
    }

    private void getAllMedicalRecord(String oldId) {
        database.collection(Constants.KEY_COLLECTION_OLDER)
                .document(oldId)
                .collection("MedicalRecord")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        ArrayList<String> allMedical = new ArrayList<>();

                        for(DocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                         String txt =   documentSnapshot.getString("txtRecorde");
                            allMedical.add(txt);
                        }

                        if (allMedical.size()>0){
                            binding.medicalRecordeRc.setVisibility(View.VISIBLE);
                            binding.txtEmptyRecycleState.setVisibility(View.GONE);

                        }else {
                            binding.medicalRecordeRc.setVisibility(View.GONE);
                            binding.txtEmptyRecycleState.setVisibility(View.VISIBLE);
                        }
                        //    queryDocumentSnapshots.toObjects(String.class);
                        MedicalRecordAdapter medicalRecordAdapter = new MedicalRecordAdapter(getActivity(),
                                allMedical);
                        binding.medicalRecordeRc.setAdapter(medicalRecordAdapter);
                        binding.medicalRecordeRc.setLayoutManager(new LinearLayoutManager(getActivity()));

                    }
                });
    }


    public void setUpAdapter() {
        if (tasks.size()>0){
            binding.alertRc.setVisibility(View.VISIBLE);
            binding.txtEmptyAlertRecycleState.setVisibility(View.GONE);

        }else {
            binding.alertRc.setVisibility(View.GONE);
            binding.txtEmptyAlertRecycleState.setVisibility(View.VISIBLE);
        }
        taskAdapter = new AlertAdapter(getActivity(), tasks, this);
        binding.alertRc.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.alertRc.setAdapter(taskAdapter);
    }

    private void getSavedTasks() {

        class GetSavedTasks extends AsyncTask<Void, Void, List<Alert>> {
            @Override
            protected List<Alert> doInBackground(Void... voids) {
                tasks = DatabaseClient
                        .getInstance(getActivity())
                        .getAppDatabase()
                        .dataBaseAction()
                        .getAllTasksList();
                return tasks;
            }

            @Override
            protected void onPostExecute(List<Alert> tasks) {
                super.onPostExecute(tasks);
                //  noDataImage.setVisibility(tasks.isEmpty() ? View.VISIBLE : View.GONE);
                setUpAdapter();
            }
        }

        GetSavedTasks savedTasks = new GetSavedTasks();
        savedTasks.execute();
    }

    @Override
    public void refresh() {
        getSavedTasks();
    }
}