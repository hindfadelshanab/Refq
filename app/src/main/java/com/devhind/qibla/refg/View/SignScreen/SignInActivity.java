package com.devhind.qibla.refg.View.SignScreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.devhind.qibla.refg.View.ForgetPasswordActivity;
import com.devhind.qibla.refg.View.MainActivity;
import com.devhind.qibla.refg.Older.MainOlderActivity;
import com.devhind.qibla.refg.databinding.ActivitySignInBinding;
import com.devhind.qibla.refg.model.Doctor;
import com.devhind.qibla.refg.utilites.Constants;
import com.devhind.qibla.refg.utilites.PreferenceManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
//        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN) && preferenceManager.getBoolean(Constants.KEY_IS_Doctor)) {
//            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }else if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN) && preferenceManager.getBoolean(Constants.KEY_IS_Older)) {
//            Intent intent = new Intent(SignInActivity.this, MainOlderActivity.class);
//            startActivity(intent);
//            finish();
//
//        }

        boolean isDoctor =getIntent().getBooleanExtra("isDoctor" ,false);
        boolean isOld =getIntent().getBooleanExtra("isOld" ,false);

//        Toast.makeText(this, getIntent().getBooleanExtra("isDoctor" ,false) +"is doctor", Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, getIntent().getBooleanExtra("isOld" ,false) +"is old", Toast.LENGTH_SHORT).show();
        binding.buttonSignIn.setOnClickListener(view -> {
            if (isValidSignInDetails() && isDoctor) {

                signInAsDoctor();
            }else if (isValidSignInDetails() && isOld){
                signInAsOld();
           //     validate(binding.inputEmail.getText().toString(),binding.inputPassword.getText().toString());

            }
        });

        setListener();
    }


    private void setListener() {
        binding.textCreateNewAccount.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));


        binding.txtForgetsPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this , ForgetPasswordActivity.class));
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private void signInAsDoctor() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(binding.inputEmail.getText().toString(), binding.inputPassword.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        loading(false);
                        String userId = mAuth.getCurrentUser().getUid();
                     //   startActivity(new Intent(SignInActivity.this, MainOlderActivity.class));

                        // database

                        database.collection(Constants.KEY_COLLECTION_DOCTOR).document(userId).get().addOnSuccessListener(
                                new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                      Doctor doctor =  documentSnapshot.toObject(Doctor.class);

                                        preferenceManager.putString(Constants.KEY_USER_ID, userId);

                                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);

                                        preferenceManager.putString(
                                                Constants.KEY_NAME,
                                                documentSnapshot.getString(Constants.KEY_NAME)
                                        );
                                        preferenceManager.putBoolean(
                                                Constants.KEY_IS_Doctor,
                                                true
                                        );
                                        preferenceManager.putBoolean(
                                                Constants.KEY_IS_Older,
                                                false
                                        );
                                        preferenceManager.putString(
                                                Constants.KEY_IMAGE,
                                              doctor.getImage()
                                        );
                                        loading(false);

                                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                    }
                                });
                    }
                });
    }


    private void signInAsOld() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(binding.inputEmail.getText().toString(), binding.inputPassword.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        loading(false);
                        String userId = mAuth.getCurrentUser().getUid();
                     //   startActivity(new Intent(SignInActivity.this, MainOlderActivity.class));

                        // database

                        database.collection(Constants.KEY_COLLECTION_OLDER).document(userId).get().addOnSuccessListener(
                                new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                                        preferenceManager.putString(Constants.KEY_USER_ID, userId);

                                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);

                                        preferenceManager.putString(
                                                Constants.KEY_NAME,
                                                documentSnapshot.getString(Constants.KEY_NAME)
                                        );
                                        preferenceManager.putBoolean(
                                                Constants.KEY_IS_Doctor,
                                                false
                                        );
                                        preferenceManager.putBoolean(
                                                Constants.KEY_IS_Older,
                                                true
                                        );
                                        preferenceManager.putString(
                                                Constants.KEY_IMAGE,
                                                documentSnapshot.getString(Constants.KEY_IMAGE)
                                        );
                                        loading(false);

                                        Intent intent = new Intent(SignInActivity.this, MainOlderActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "signInWithEmail:failure"+ e.getMessage().toString());
                        Toast.makeText(SignInActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        loading(false);
                    }
                });
    }


    private void signIn() {


        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(binding.inputEmail.getText().toString(), binding.inputPassword.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        loading(false);
                        String userId = mAuth.getCurrentUser().getUid();
                        startActivity(new Intent(SignInActivity.this, MainOlderActivity.class));

                        // database

                        database.collection(Constants.KEY_COLLECTION_DOCTOR).document(userId).get().addOnSuccessListener(
                                new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.getData() != null) {
                                            preferenceManager.putString(Constants.KEY_USER_ID, userId);

                                            preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);

                                            preferenceManager.putString(
                                                    Constants.KEY_NAME,
                                                    documentSnapshot.getString(Constants.KEY_NAME)
                                            );
                                            preferenceManager.putBoolean(
                                                    Constants.KEY_IS_Doctor,
                                                    true
                                            );
                                            preferenceManager.putBoolean(
                                                    Constants.KEY_IS_Older,
                                                    false
                                            );
                                            preferenceManager.putString(
                                                    Constants.KEY_IMAGE,
                                                    documentSnapshot.getString(Constants.KEY_IMAGE)
                                            );
                                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);


                                        } else {

                                            database.collection(Constants.KEY_COLLECTION_OLDER).document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    if (documentSnapshot.getData() != null) {
                                                        preferenceManager.putString(Constants.KEY_USER_ID, userId);

                                                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);

                                                        preferenceManager.putString(
                                                                Constants.KEY_NAME,
                                                                documentSnapshot.getString(Constants.KEY_NAME)
                                                        );
                                                        preferenceManager.putBoolean(
                                                                Constants.KEY_IS_Doctor,
                                                                false
                                                        );
                                                        preferenceManager.putBoolean(
                                                                Constants.KEY_IS_Older,
                                                                true
                                                        );
                                                        preferenceManager.putString(
                                                                Constants.KEY_IMAGE,
                                                                documentSnapshot.getString(Constants.KEY_IMAGE)
                                                        );
                                                        Intent intent = new Intent(SignInActivity.this, MainOlderActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(intent);

                                                    }

                                                }
                                            });


                                        }


                                    }
                                });


                    }
                });
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.buttonSignIn.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.buttonSignIn.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }


    private Boolean isValidSignInDetails() {
        if (binding.inputEmail.getText().toString().trim().isEmpty()) {
            showToast("Enter Email ");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
            showToast("Enter valid email");
            return false;
        } else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter Password");
            return false;

        } else {
            return true;
        }

    }

//    public SignInActivity(Context context){
//
//    }
//    public String validate(String email, String password)
//    {
//        if(email.equals("old@gmail.com") && password.equals("123456"))
//            return "Login was successful";
//        else
//            return "Invalid login!";
//    }
}