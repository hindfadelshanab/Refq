package com.devhind.qibla.refg.Older.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devhind.qibla.refg.Adapter.RecentConversationAdapter;
import com.devhind.qibla.refg.View.ChatActivity;
import com.devhind.qibla.refg.databinding.FragmentChatOldBinding;
import com.devhind.qibla.refg.listeners.ConversationListener;
import com.devhind.qibla.refg.model.ChatMessage;
import com.devhind.qibla.refg.model.User;
import com.devhind.qibla.refg.utilites.Constants;
import com.devhind.qibla.refg.utilites.PreferenceManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ChatOldFragment extends Fragment   {
    private PreferenceManager preferenceManager;

    private List<ChatMessage> conversions;
    private RecentConversationAdapter recentConversationAdapter;
    private FirebaseFirestore database;

    FragmentChatOldBinding binding ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatOldBinding.inflate(inflater , container , false);
        init();
        preferenceManager = new PreferenceManager(getActivity());
        loadUserDetails();
       // getToken();
       // setListener();
        listenConversion();
        return binding.getRoot();
    }
//    private void setListener() {
//        binding.imageSignOut.setOnClickListener(view -> signOut());
//        binding.fabNewChat.setOnClickListener(view ->
//                startActivity(new Intent(getApplicationContext(), UserActivity.class)));
//    }

    private void init() {
        conversions = new ArrayList<>();
        recentConversationAdapter = new RecentConversationAdapter(conversions ,getActivity() , false);
        binding.conversionRecycleView.setAdapter(recentConversationAdapter);
        database = FirebaseFirestore.getInstance();

    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    private void  listenConversion(){
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {

        if (error != null) {
            return;
        }
        if (value != null) {
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    String recevierId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = senderId;
                    chatMessage.receiverId = recevierId;
                    if (preferenceManager.getString(Constants.KEY_USER_ID).equals(senderId)) {

                        chatMessage.receiverImage = documentChange.getDocument().getString(Constants.KEY_RECEIVER_IMAGE);
                        chatMessage.receiverName = documentChange.getDocument().getString(Constants.KEY_RECEIVER_NAME);
                        chatMessage.converstionId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);

                    } else {
                        chatMessage.receiverImage = documentChange.getDocument().getString(Constants.KEY_SENDER_IMAGE);
                        chatMessage.receiverName = documentChange.getDocument().getString(Constants.KEY_SENDER_NAME);
                        chatMessage.converstionId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);

                    }
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    conversions.add(chatMessage);
                } else if (documentChange.getType() == DocumentChange.Type.MODIFIED) {

                    for (int i = 0; i < conversions.size(); i++) {
                        String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                        String recevierId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                        if (conversions.get(i).senderId.equals(senderId) && conversions.get(i).receiverId.equals(recevierId)){
                            conversions.get(i).message=documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                            conversions.get(i).dateObject= documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                            break;
                        }
                    }
                }

            }

            Collections.sort(conversions,(obj1, obj2)-> obj2.dateObject.compareTo(obj1.dateObject));
            recentConversationAdapter.notifyDataSetChanged();
            binding.conversionRecycleView.smoothScrollToPosition(0);
            binding.conversionRecycleView.setVisibility(View.VISIBLE);
            //binding.avi.hide();



        }
    };

    public void getToken() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void loadUserDetails() {
      //  binding.textName.setText(preferenceManager.getString(Constants.KEY_NAME));
//        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
//        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
      //  binding..setImageBitmap(bitmap);
    }

    private void updateToken(String token) {
        preferenceManager.putString(Constants.KEY_FCM_TOKEN,token);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(
                preferenceManager.getString(Constants.KEY_USER_ID)
        );
        documentReference.update(Constants.KEY_FCM_TOKEN, token).addOnSuccessListener(unsend -> showToast("Token update Successfully"))
//                .addOnFailureListener(e ->
//
//                   //     showToast("Unable to update token")
//
//                )
                ;


    }
//
//    public void signOut() {
//        showToast("signing out ....");
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(
//                preferenceManager.getString(Constants.KEY_USER_ID)
//        );
//
//        HashMap<String, Object> updates = new HashMap<>();
//        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
//        documentReference.update(updates).addOnSuccessListener(unsend -> {
//            preferenceManager.clear();
//            startActivity(new Intent(getActivity(), Sig.class));
//            finish();
//        }).addOnFailureListener(e -> showToast("unable to sign out"));
//
//    }

//    @Override
//    public void onConversationClicked(User user) {
//        Intent i = new Intent(getActivity(), ChatActivity.class);
//        i.putExtra("Doctor",user);
//        startActivity(i);
//
//    }
}