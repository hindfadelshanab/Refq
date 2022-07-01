package com.devhind.qibla.refg.View.SignScreen;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.devhind.qibla.refg.View.MainActivity;
import com.devhind.qibla.refg.databinding.ActivityCompleteSignUpBinding;
import com.devhind.qibla.refg.model.Doctor;
import com.devhind.qibla.refg.utilites.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.UUID;

public class CompleteSignUpActivity extends AppCompatActivity {
    Uri encodedImage;
    ActivityCompleteSignUpBinding binding;

    FirebaseFirestore database;
    int mYear, mMonth, mDay;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompleteSignUpBinding.inflate(getLayoutInflater());
        database = FirebaseFirestore.getInstance();
        Doctor oldDoctor = getIntent().getParcelableExtra("doctor");
        Doctor doctorProfile = getIntent().getParcelableExtra("DoctorProfile");

        if (doctorProfile!=null){
            binding.txtAddImage.setText("تعديل الصورة الشخصية");
            binding.inputFirstName.setText(doctorProfile.getFirstName());
            binding.inputFamily.setText(doctorProfile.getLastName());
            binding.inputDoctorDate.setText(doctorProfile.getBirthday());



        }
        binding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidSignUpDetails()) {

                    if (oldDoctor !=null) {

                        updateDoctorInfo(oldDoctor, encodedImage);
                    }else {
                        updateDoctorInfo(doctorProfile , encodedImage);
                    }
                }

            }
        });
        binding.txtAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pickImage.launch(intent);
            }
        });


        binding.inputDoctorDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(CompleteSignUpActivity.this,
                        (view1, year, monthOfYear, dayOfMonth) -> {
                            binding.inputDoctorDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            datePickerDialog.dismiss();
                        }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        });


        setContentView(binding.getRoot());
    }

    private void updateDoctorInfo(Doctor oldInfo, Uri fileUri) {


        Doctor newDoctor = new Doctor();
        newDoctor.setFirstName(binding.inputFirstName.getText().toString());
        newDoctor.setLastName(binding.inputFamily.getText().toString());

        newDoctor.setEmail(oldInfo.getEmail());
        newDoctor.setName(oldInfo.getName());
        newDoctor.setId(oldInfo.getId());
        newDoctor.setPhoneNumber(binding.inputDoctorPhone.getText().toString());
        newDoctor.setBirthday(binding.inputDoctorDate.getText().toString());
        newDoctor.setCity(binding.inputDoctorCity.getText().toString());
        newDoctor.setAddress(binding.inputDoctorAddress.getText().toString());
        newDoctor.setCv(binding.inputDoctorCv.getText().toString());
        newDoctor.setSpeacliztion(binding.inputDoctorSpeac.getText().toString());

        if (fileUri != null) {
            String fileName = UUID.randomUUID().toString() + ".jpg";
            StorageReference refStorage = FirebaseStorage.getInstance().getReference().child("images/"+fileName);
            refStorage.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            newDoctor.setImage(uri.toString());

                            database.collection(Constants.KEY_COLLECTION_DOCTOR)
                                    .document(oldInfo.getId())
                                    .set(newDoctor)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            showToast("Doctor Information updated ");

                                            Intent intent = new Intent(CompleteSignUpActivity.this , MainActivity.class);
                                            intent.putExtra("doctor" , newDoctor);
                                            startActivity(intent);
                                        }
                                    });
                        }
                    });


                }
            });
        }else {
            newDoctor.setImage("");

            database.collection(Constants.KEY_COLLECTION_DOCTOR)
                    .document(oldInfo.getId())
                    .set(newDoctor)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            showToast("Doctor Information updated ");

                            Intent intent = new Intent(CompleteSignUpActivity.this , MainActivity.class);
                            intent.putExtra("doctor" , newDoctor);
                            startActivity(intent);
                        }
                    });
        }
    }




                private Boolean isValidSignUpDetails() {
                    if (encodedImage == null) {
                        showToast("Select profile image");
                        return false;
                    } else if (binding.inputFirstName.getText().toString().trim().isEmpty()) {
                        showToast("Enter First name");
                        return false;
                    } else if (binding.inputFamily.getText().toString().trim().isEmpty()) {
                        showToast("Enter last Name");
                        return false;
                    } else if (binding.inputDoctorDate.getText().toString().trim().isEmpty()) {
                        showToast("Choose date");
                        return false;
                    } else if (binding.inputDoctorPhone.getText().toString().trim().isEmpty()) {
                        showToast("Enter Phone");
                        return false;
                    } else if (binding.inputDoctorSpeac.getText().toString().trim().isEmpty()) {
                        showToast("Enter specialization");
                        return false;
                    } else if (binding.inputDoctorCity.getText().toString().trim().isEmpty()) {
                        showToast("Enter City");
                        return false;
                    } else if (binding.inputDoctorAddress.getText().toString().trim().isEmpty()) {
                        showToast("Enter Address");
                        return false;
                    } else if (binding.inputDoctorCity.getText().toString().trim().isEmpty()) {
                        showToast("Enter Cv");
                        return false;
                    }
//        else if (!Patterns.in.matcher(binding.inputEmail.getText().toString()).matches()) {
//            showToast("Enter valid email");
//            return false;
//        } else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
//            showToast("Enter password");
//            return false;
//        }
                    else {
                        return true;
                    }

                }

                private void showToast(String message) {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }

                private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode() == RESULT_OK) {
                                if (result.getData() != null) {
                                    Uri imageUri = result.getData().getData();
                                    try {
                                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                        binding.imgUserImage.setImageBitmap(bitmap);
                                        encodedImage = imageUri;

                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                );
            }
