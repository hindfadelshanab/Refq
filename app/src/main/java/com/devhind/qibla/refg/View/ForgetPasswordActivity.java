package com.devhind.qibla.refg.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.devhind.qibla.refg.databinding.ActivityForgetPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {


    ActivityForgetPasswordBinding binding ;

    private FirebaseAuth mAuth ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance() ;

        binding.btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String  email = binding.edtResetEmail.getText().toString().trim() ;
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(ForgetPasswordActivity.this, "Enter your email!", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgetPasswordActivity.this, "Check email to reset your password!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ForgetPasswordActivity.this, "Fail to send reset password email!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }
        });



    }
}