package com.devhind.qibla.refg.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devhind.qibla.refg.Adapter.SliderAdapter;
import com.devhind.qibla.refg.R;
import com.devhind.qibla.refg.View.SignScreen.SignAsActivity;
import com.devhind.qibla.refg.model.Doctor;
import com.devhind.qibla.refg.model.Order;
import com.devhind.qibla.refg.model.Request;
import com.devhind.qibla.refg.utilites.Constants;
import com.devhind.qibla.refg.utilites.PreferenceManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    PreferenceManager preferenceManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferenceManager = new PreferenceManager(this);
        getToken();


        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.accountAction, R.id.notificationAction ,
                R.id.homeAction, R.id.messageAction)
//                R.id.homeAction, R.id.messageAction, R.id.notificationAction , R.id.accountAction)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

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
        documentReference.update(Constants.KEY_FCM_TOKEN, token).addOnSuccessListener(unsend -> showToast("Token update Successfully"))
                .addOnFailureListener(e -> showToast("Unable to update token"));

        database.collection(Constants.KEY_COLLECTION_ORDERS).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                   Order order = documentSnapshot.toObject(Order.class);
                    Log.e("messsss" , documentSnapshot.getId()+"ppppp");

                    database.collection(Constants.KEY_COLLECTION_ORDERS).document(documentSnapshot.getId())
                            .collection("Request")
                            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                    for (DocumentSnapshot documentSnapshot1 :queryDocumentSnapshots){

                                        Request request =  documentSnapshot1.toObject(Request.class);
                                        Log.e("messss34444s" , documentSnapshot1.getId()+"ppppp");

                                        Doctor doctor = new Doctor();
                                        doctor.setId(request.getDoctorRequest().getId());
                                        doctor.setSpeacliztion(request.getDoctorRequest().getSpeacliztion());
                                        doctor.setName(request.getDoctorRequest().getName());
                                        doctor.setAddress(request.getDoctorRequest().getAddress());
                                        doctor.setImage(request.getDoctorRequest().getImage());
                                        doctor.setCity(request.getDoctorRequest().getCity());
                                        doctor.setBirthday(request.getDoctorRequest().getBirthday());
                                        doctor.setPhoneNumber(request.getDoctorRequest().getPhoneNumber());
                                        doctor.setEmail(request.getDoctorRequest().getEmail());
                                        doctor.setCv(request.getDoctorRequest().getCv());
                                        doctor.setLastName(request.getDoctorRequest().getLastName());
                                        doctor.setFirstName(request.getDoctorRequest().getFirstName()
                                        );


                                        doctor.setFcmToken(token);
                                        //request.setDoctorRequest(doctor);
                                        Log.e("doctorppp" , doctor.getId()+"ppppp");
                                        Log.e("doctorppp" , doctor.getFcmToken()+"ppppp");
                                        Log.e("doctorppp" , token+"ppppp");

                                        Request request1 = new Request();
                                        request1.setDoctorRequest(doctor);
                                        request1.setRequestId(request.getRequestId());
                                        request1.setDoctorId(request.getDoctorId());
                                        request1.setOldId(request.getOldId());
                                        request1.setOrderID(request.getOrderID());
                                        request1.setAccept(request.getAccept());

                                        if (request.getDoctorRequest().getId().equals(preferenceManager.getString(Constants.KEY_USER_ID))){




                                            DocumentReference documentReference =database.collection("Orders").document(order.getOrderId()).collection("Request").document(
                                                    request.getRequestId()
                                            );

                                            documentReference.update("doctorRequest", doctor).addOnSuccessListener(unsend -> showToast("Token update Successfully"))
                                                    .addOnFailureListener(e -> showToast("Unable to update token"));



//
//                                            database.collection("Request").document(
//                                                    request.getRequestId()
//                                            ).set(request1).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                @Override
//                                                public void onSuccess(Void unused) {
//                                                    Log.e("nononnn" , request1.getRequestId()+"{request id}}");
//                                                    Log.e("nononnn" , request1.getOldId()+"{request old id}}");
//                                                    Log.e("nononnn" , request1.getDoctorRequest().getToken()+"{token  }}");
//                                                    Log.e("nononnn" , request1.getDoctorId()+"{doctor id}}");
//
//                                                    showToast("request Token update Successfully");
//                                                }
//                                            }).addOnFailureListener(new OnFailureListener() {
//                                                @Override
//                                                public void onFailure(@NonNull Exception e) {
//                                                    showToast("request Token update fail");
//                                                    showToast("request Token update fail"+e.getMessage());
//
//                                                }
//                                            });
                                        }
                                    }
                                }
                            });



                }
            }
        });




    }


    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}