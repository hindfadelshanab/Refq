package com.devhind.qibla.refg.View.SignScreen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.devhind.qibla.refg.View.MainActivity;
import com.devhind.qibla.refg.Older.MainOlderActivity;
import com.devhind.qibla.refg.databinding.ActivitySignUpBinding;
import com.devhind.qibla.refg.model.Doctor;
import com.devhind.qibla.refg.model.User;
import com.devhind.qibla.refg.utilites.Constants;
import com.devhind.qibla.refg.utilites.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {

    //  private static final java.util.UUID UUID = ;
    ActivitySignUpBinding binding;
    Uri encodedImage;
    PreferenceManager preferenceManager;
    FirebaseFirestore database;
    String accountType = "";
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(this);
        database = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        setListener();


    }

    void setListener() {

        boolean isDoctor =getIntent().getBooleanExtra("isDoctor" ,false);
        boolean isOld=getIntent().getBooleanExtra("isOld" ,false);
//        Toast.makeText(this, getIntent().getBooleanExtra("isDoctor" ,false) +"is doctor", Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, getIntent().getBooleanExtra("isOld" ,false) +"is old", Toast.LENGTH_SHORT).show();

        if (isOld){
            binding.txtAddress.setVisibility(View.VISIBLE);
            binding.txtAge.setVisibility(View.VISIBLE);
            binding.inputAddress.setVisibility(View.VISIBLE);
            binding.inputAge.setVisibility(View.VISIBLE);
        }
        binding.buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  startActivity(new Intent(SignUpActivity.this, MainActivity.class));
//                signUp(encodedImage);

                if (isDoctor && isValidSignUpDetails()){

                    SignUpAsDoctor();
                }else if (isOld && isValidSignUpDetails()){
                    if (!binding.inputAddress.getText().toString().isEmpty() && !binding.inputAddress.getText().toString().isEmpty() ) {
                        SignUpAsOld();
                    }else {
                        showToast("Pleas fill all filed");
                    }
                }
            }
        });
      binding.textSignIn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent = new Intent(SignUpActivity.this , SignInActivity.class) ;
              intent.putExtra("isDoctor" , isDoctor);
              intent.putExtra("isOld" , isOld);
              startActivity(intent);
          }
      });
//              setOnClickListener(
//            //  startActivity(new Intent(SignUpActivity.this, MainActivity.class));
//              view -> onBackPressed()
//
//      );
//
//        binding.profileImage.setOnClickListener(view -> {
//            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            pickImage.launch(intent);
//        });





    }


    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
    private void SignUpAsDoctor(){
//        if (fileUri != null) {
//            String fileName = UUID.randomUUID().toString() + ".jpg";
//            StorageReference refStorage = FirebaseStorage.getInstance().getReference().child("ProfilePicture/"+fileName);
//            refStorage.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                                                 @Override
//                                                                 public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                                                     taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                                                         @Override
//                                                                         public void onSuccess(Uri uri) {
//
//                                                                         }
//                                                                     });
//                                                                 }
//
//
//                                                             });
//        }
        loading(true);
        mAuth.createUserWithEmailAndPassword(
                binding.inputEmail.getText().toString(),
                binding.inputPassword.getText().toString()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if (task.isSuccessful()) {

                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    String userId = currentUser.getUid().toString();

                    Doctor doctor = new Doctor();
                    doctor.setEmail(binding.inputEmail.getText().toString());
                    doctor.setName(binding.inputName.getText().toString());
                    doctor.setId(currentUser.getUid().toString());
                    database.collection(Constants.KEY_COLLECTION_DOCTOR).document(userId)
                            .set(doctor)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    loading(false);
                                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                                    preferenceManager.putString(
                                            Constants.KEY_USER_ID,
                                            userId
                                    );
                                    preferenceManager.putString(
                                            Constants.KEY_NAME,
                                            binding.inputName.getText().toString()
                                    );
                                    preferenceManager.putBoolean(
                                            Constants.KEY_IS_Older,
                                            false
                                    );
                                    preferenceManager.putBoolean(
                                            Constants.KEY_IS_Doctor,
                                            true
                                    );
                                    loading(false);
                                    Intent intent = new Intent(SignUpActivity.this, CompleteSignUpActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.putExtra("doctor",doctor);
                                    startActivity(intent);
                                }
                            });



                }
            }
        });


                }

    private void SignUpAsOld(){

        loading(true);

                            mAuth.createUserWithEmailAndPassword(
                binding.inputEmail.getText().toString(),
                binding.inputPassword.getText().toString()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if (task.isSuccessful()) {

                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    String userId = currentUser.getUid().toString();

                    User user = new User();
                    user.setEmail(binding.inputEmail.getText().toString());
                    user.setName(binding.inputName.getText().toString());
                    user.setId(currentUser.getUid().toString());
                    user.setAddress(binding.inputAddress.getText().toString());
                    user.setAge(binding.inputAge.getText().toString());
                    database.collection(Constants.KEY_COLLECTION_OLDER).document(userId)
                            .set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    loading(false);
                                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                                    preferenceManager.putString(
                                            Constants.KEY_USER_ID,
                                            userId
                                    );
                                    preferenceManager.putString(
                                            Constants.KEY_NAME,
                                            binding.inputName.getText().toString()
                                    );
                                    preferenceManager.putBoolean(
                                            Constants.KEY_IS_Older,
                                            true
                                    );
                                    preferenceManager.putBoolean(
                                            Constants.KEY_IS_Doctor,
                                            false
                                    );
                                    loading(false);
                                    Intent intent = new Intent(SignUpActivity.this, MainOlderActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            });



                }
            }
        });


    }

    private void signUp(Uri fileUri) {


//        if (fileUri != null) {
//            val fileName = UUID.randomUUID().toString() + ".jpg"
//            val refStorage = FirebaseStorage.getInstance().reference.child("images/$fileName")
//            refStorage.putFile(fileUri)
//                    .addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
//                            taskSnapshot.storage.downloadUrl.addOnSuccessListener {
//                            var imageUrl = it.toString()


        if (fileUri != null) {
            String fileName = UUID.randomUUID().toString() + ".jpg";
            StorageReference refStorage = FirebaseStorage.getInstance().getReference().child("ProfilePicture/"+fileName);
            refStorage.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            mAuth.createUserWithEmailAndPassword(
                                    binding.inputEmail.getText().toString(),
                                    binding.inputPassword.getText().toString()
                            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {


                                    if (task.isSuccessful()) {

                                        FirebaseUser currentUser = mAuth.getCurrentUser();
                                        String userId = currentUser.getUid().toString();

                                        User user = new User();
                                        user.setEmail(binding.inputEmail.getText().toString());
                                        user.setName(binding.inputName.getText().toString());
                                        user.setAccountType(accountType);
                                        user.setImage(String.valueOf(uri));
                                        user.setId(currentUser.getUid().toString());


                                        if (accountType.equals("كبير سن")) {

                                            database.collection(Constants.KEY_COLLECTION_OLDER).document(userId)
                                                    .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            loading(false);
                                                            preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                                                            preferenceManager.putString(
                                                                    Constants.KEY_USER_ID,
                                                                    userId
                                                            );
                                                            preferenceManager.putString(
                                                                    Constants.KEY_NAME,
                                                                    binding.inputName.getText().toString()
                                                            );
                                                            preferenceManager.putBoolean(
                                                                    Constants.KEY_IS_Older,
                                                                    true
                                                            );
                                                            preferenceManager.putBoolean(
                                                                    Constants.KEY_IS_Doctor,
                                                                    false
                                                            );
                                                            preferenceManager.putString(Constants.KEY_IMAGE, uri.toString());
                                                            Intent intent = new Intent(SignUpActivity.this, MainOlderActivity.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);

                                                        }
                                                    });


                                        } else if (accountType.equals("طبيب")) {


                                            database.collection(Constants.KEY_COLLECTION_DOCTOR).document(userId)
                                                    .set(user)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            loading(false);
                                                            preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                                                            preferenceManager.putString(
                                                                    Constants.KEY_USER_ID,
                                                                    userId
                                                            );
                                                            preferenceManager.putString(
                                                                    Constants.KEY_NAME,
                                                                    binding.inputName.getText().toString()
                                                            );
                                                            preferenceManager.putBoolean(
                                                                    Constants.KEY_IS_Older,
                                                                    false
                                                            );
                                                            preferenceManager.putBoolean(
                                                                    Constants.KEY_IS_Doctor,
                                                                    true
                                                            );
                                                            preferenceManager.putString(Constants.KEY_IMAGE, uri.toString());

                                                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                        }
                                                    });

                                        }
                                    }


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    showToast(e.getMessage() + "Fail Sign up");
                                }
                            });


                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }


        loading(true);
//        loading(true);
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        HashMap<String ,Object> user = new HashMap<>();
//        user.put(Constants.KEY_NAME,binding.inputName.getText().toString());
//        user.put(Constants.KEY_EMAIL,binding.inputEmail.getText().toString());
//        user.put(Constants.KEY_PASSWORD,binding.inputPassword.getText().toString());
//        user.put(Constants.KEY_IMAGE,encodedImage);
//        database.collection(Constants.KEY_COLLECTION_USERS)
//                .add(user)
//                .addOnSuccessListener(documentReference -> {
//                    loading(false);
//                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
//                    preferenceManager.putString(Constants.KEY_USER_ID,documentReference.getId());
//                    preferenceManager.putString(Constants.KEY_NAME,binding.inputName.getText().toString());
//                    preferenceManager.putString(Constants.KEY_IMAGE, String.valueOf(encodedImage));
//                    Intent  intent = new Intent(getApplicationContext(),MainActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                })
//                .addOnFailureListener(e -> {
//                    loading(false);
//                    showToast(e.getMessage());
//
//                });
    }

    private String encodImage(Bitmap bitmap) {
        int preViewWidth = 150;
        int preViewHeight = bitmap.getHeight() * preViewWidth / bitmap.getWidth();
        Bitmap preViewBitmap = Bitmap.createScaledBitmap(bitmap, preViewWidth, preViewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        preViewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
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
                            binding.profileImage.setImageBitmap(bitmap);
                            encodedImage = imageUri;

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private Boolean isValidSignUpDetails() {
          if (binding.inputName.getText().toString().trim().isEmpty()) {
            showToast("Enter name");
            return false;
        } else if (binding.inputEmail.getText().toString().trim().isEmpty()) {
            showToast("Enter email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
            showToast("Enter valid email");
            return false;
        } else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter password");
            return false;
        } else {
            return true;
        }

    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.buttonSignUp.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.buttonSignUp.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }


//    private fun setListener() {
//
//        binding.buttonSignUp.setOnClickListener(View.OnClickListener {
//            startActivity(Intent(this, MainActivity::class.java))
//
//        })
////        binding.textSignIn.setOnClickListener(View.OnClickListener {
////            startActivity(Intent(this , SignInActivity::class.java))
////
////        })
//
//        binding.radio.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
//                var radioButton = findViewById<View>(checkedId) as RadioButton
//                //  Toast.makeText(baseContext, radioButton.getText(), Toast.LENGTH_SHORT).show()
//                accountType = radioButton.text.toString()
//
//        }
//        )
////        val selectedId: Int = binding.radio.getCheckedRadioButtonId()
////
////        var radioButton = findViewById<View>(selectedId) as RadioButton
//        binding.textSignIn.setOnClickListener { view -> onBackPressed() }
//        binding.buttonSignUp.setOnClickListener { view ->
//            if (isValidSignUpDetails() == true) {
//                signUp(encodedImage!!)
//            }
//        }
//        binding.layoutImage.setOnClickListener { view ->
//                val intent =
//                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//            pickImage.launch(intent)
//        }
//    }
//
//    private fun showToast(message: String?) {
//        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
//    }
//
//    private fun signUp(fileUri: Uri) {
//        Log.e("image" , "imageee :${fileUri}" )
//        loading(true)
//        preferenceManager!!.putString(Constants.KEY_Chat_ID ,"")
//
//        val database = FirebaseFirestore.getInstance()
//        val mAuth = FirebaseAuth.getInstance()
//        if (fileUri != null) {
//            val fileName = UUID.randomUUID().toString() + ".jpg"
//            val refStorage = FirebaseStorage.getInstance().reference.child("images/$fileName")
//            refStorage.putFile(fileUri)
//                    .addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
//                            taskSnapshot.storage.downloadUrl.addOnSuccessListener {
//                            var imageUrl = it.toString()
//
//                            mAuth.createUserWithEmailAndPassword(
//                                    binding.inputEmail.text.toString(),
//                                    binding.inputPassword.text.toString()
//                            ).addOnCompleteListener(this,
//                                    OnCompleteListener<AuthResult?> { task ->
//            if (task.isSuccessful) {
//                val currentUser: FirebaseUser? = mAuth.getCurrentUser()
//                val userId = currentUser!!.uid.toString()
//                var user: User = User()
//                user.name = binding.inputName.text.toString()
//                user.email = binding.inputEmail.text.toString()
//                user.accountType = accountType
//                user.image = imageUrl!!
//                        user.id = currentUser!!.uid.toString()
//                Log.e("image" , "image url :${imageUrl}" )
//
//
//                if (accountType == "مريض") {
//                    database.collection(Constants.KEY_COLLECTION_PATIENT).document(userId)
//                            .set(user)
//                            .addOnSuccessListener {
//                        loading(false)
//                        preferenceManager!!.putBoolean(Constants.KEY_IS_SIGNED_IN, true)
//                        preferenceManager!!.putString(
//                                Constants.KEY_USER_ID,
//                                userId
//                        )
//                        preferenceManager!!.putString(
//                                Constants.KEY_NAME,
//                                binding.inputName.text.toString()
//                        )
//                        preferenceManager!!.putBoolean(
//                                Constants.KEY_IS_Student,
//                                true
//                        )
//                        preferenceManager!!.putBoolean(
//                                Constants.KEY_IS_Teacher,
//                                false
//                        )
//                        preferenceManager!!.putString(Constants.KEY_IMAGE, imageUrl)
//
//                        val intent =
//                                Intent(applicationContext, MainActivity::class.java)
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                        startActivity(intent)
//                    }
//
//                                            .addOnFailureListener { e: Exception ->
//                            loading(false)
//                        showToast(e.message)
//
//                    }
//
//                } else if (accountType == "طبيب") {
//
//                    database.collection(Constants.KEY_COLLECTION_DOCTOR).document(userId)
//                            .set(user)
//                            .addOnSuccessListener {
//                        loading(false)
//                        preferenceManager!!.putBoolean(Constants.KEY_IS_SIGNED_IN, true)
//                        preferenceManager!!.putString(
//                                Constants.KEY_USER_ID,
//                                userId
//                        )
//                        preferenceManager!!.putString(
//                                Constants.KEY_NAME,
//                                binding.inputName.text.toString()
//                        )
//                        preferenceManager!!.putBoolean(
//                                Constants.KEY_IS_Student,
//                                false
//                        )
//                        preferenceManager!!.putBoolean(
//                                Constants.KEY_IS_Teacher,
//                                true
//                        )
//                        preferenceManager!!.putString(Constants.KEY_IMAGE, imageUrl)
//
//                        val intent =
//                                Intent(applicationContext, MainActivity::class.java)
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                        startActivity(intent)
//                    }
//                                            .addOnFailureListener { e: Exception ->
//                            loading(false)
//                        showToast(e.message)
//
//                    }
//                }
//
//            } else {
//                // If sign in fails, display a message to the user.
//
//                Log.w(TAG, "createUserWithEmail:failure", task.exception)
//                Toast.makeText(
//                        this, "Authentication failed.",
//                        Toast.LENGTH_SHORT
//                ).show()
//
//            }
//                            })
//
//            Log.e("image" , "image :${it.toString()}" )
//                    }
//                })
//
//                .addOnFailureListener(OnFailureListener { e ->
//                    print(e.message)
//            })
//        }
//
//    }
//
//    private val pickImage = registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()
//    ) { result: ActivityResult ->
//        if (result.resultCode == RESULT_OK) {
//            if (result.data != null) {
//                val imageUri = result.data!!.data
//                try {
//                    val inputStream =
//                            contentResolver.openInputStream(imageUri!!)
//                    val bitmap = BitmapFactory.decodeStream(inputStream)
//                    binding.imageProfile.setImageBitmap(bitmap)
//                    binding.textAddImage.visibility = View.GONE
//                    encodedImage = result.data!!.data
//                } catch (e: FileNotFoundException) {
//                    e.printStackTrace()
//                }
//            }
//        }
//    }
//
//    private fun isValidSignUpDetails(): Boolean? {
//
//        return if (encodedImage == null) {
//            showToast("Select profile image")
//
//            false
//        } else if (binding.inputName.text.toString().trim().isEmpty()) {
//
//            binding.inputName.setError("Enter name")
//            false
//        } else if (binding.inputEmail.text.toString().trim().isEmpty()) {
//            showToast("Enter email")
//            binding.inputEmail.setError("Enter email")
//
//            false
//        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.text.toString()).matches()) {
//            showToast("Enter valid email")
//            binding.inputEmail.setError("Enter valid email")
//
//            false
//        }  else if (accountType == "") {
//            showToast("Pleas select Account type")
//            //binding.inputEmail.setError("Pleas select Account type")
//            false
//        } else if (binding.inputPassword.text.toString().trim().isEmpty()) {
//            showToast("Enter password")
//            binding.inputPassword.setError("Enter password")
//
//            false
//        } else {
//            true
//        }
//    }
//
//    private fun loading(isLoading: Boolean) {
//        if (isLoading) {
//            binding.buttonSignUp.visibility = View.INVISIBLE
//            binding.progressBar.visibility = View.VISIBLE
//        } else {
//            binding.buttonSignUp.visibility = View.VISIBLE
//            binding.progressBar.visibility = View.INVISIBLE
//        }
//    }

}