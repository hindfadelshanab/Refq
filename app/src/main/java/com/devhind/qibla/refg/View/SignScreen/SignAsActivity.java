package com.devhind.qibla.refg.View.SignScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.devhind.qibla.refg.Older.MainOlderActivity;
import com.devhind.qibla.refg.View.MainActivity;
import com.devhind.qibla.refg.databinding.ActivitySignAsBinding;
import com.devhind.qibla.refg.utilites.Constants;
import com.devhind.qibla.refg.utilites.PreferenceManager;

public class SignAsActivity extends AppCompatActivity {


    PreferenceManager preferenceManager;
    ActivitySignAsBinding binding ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignAsBinding.inflate(getLayoutInflater());
        preferenceManager = new PreferenceManager(getApplicationContext());
        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN) && preferenceManager.getBoolean(Constants.KEY_IS_Doctor)) {
            Intent intent = new Intent(SignAsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN) && preferenceManager.getBoolean(Constants.KEY_IS_Older)) {
            Intent intent = new Intent(SignAsActivity.this, MainOlderActivity.class);
            startActivity(intent);
            finish();

        }
        boolean isSignIn =getIntent().getBooleanExtra("signIn" , false);
        binding.btnSignAsDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    Intent intent = new Intent(SignAsActivity.this, SignUpActivity.class);
                    intent.putExtra("isDoctor", true);
                    startActivity(intent);

            }
        });

        binding.btnSignAsOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    Intent intent = new Intent(SignAsActivity.this, SignUpActivity.class);

                    intent.putExtra("isOld", true);
                    startActivity(intent);

            }
        });
        setContentView(binding.getRoot());
    }
}