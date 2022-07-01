package com.devhind.qibla.refg.Older;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.devhind.qibla.refg.R;
import com.devhind.qibla.refg.View.SignScreen.SignAsActivity;
import com.devhind.qibla.refg.utilites.Constants;
import com.devhind.qibla.refg.utilites.PreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainOlderActivity extends AppCompatActivity {

    PreferenceManager preferenceManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_older);
        preferenceManager = new PreferenceManager(this);
        getToken();


        BottomNavigationView navView = findViewById(R.id.nav_view_old);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeAction, R.id.OrdersAction, R.id.messageAction , R.id.accountAction)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_old);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        getSupportActionBar().setTitle("رفق");
        NavigationUI.setupWithNavController(navView, navController);

    }
    public void getToken() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token) {
        preferenceManager.putString(Constants.KEY_FCM_TOKEN,token);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_OLDER).document(
                preferenceManager.getString(Constants.KEY_USER_ID)
        );
        documentReference.update(Constants.KEY_FCM_TOKEN, token).addOnSuccessListener(unsend -> showToast("Token update Successfully"))
                .addOnFailureListener(e -> showToast("Unable to update token"));


    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.logoutAction){
            preferenceManager.clear();
            startActivity(new Intent(this , SignAsActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }
}