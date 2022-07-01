package com.devhind.qibla.refg.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devhind.qibla.refg.Adapter.RecentConversationAdapter;
import com.devhind.qibla.refg.R;
import com.devhind.qibla.refg.databinding.FragmentChatBinding;
import com.devhind.qibla.refg.model.ChatMessage;
import com.devhind.qibla.refg.utilites.Constants;
import com.devhind.qibla.refg.utilites.PreferenceManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class chatFragment extends Fragment {
    private PreferenceManager preferenceManager;

    private List<ChatMessage> conversions;
    private RecentConversationAdapter recentConversationAdapter;
    private FirebaseFirestore database;

    FragmentChatBinding binding ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(inflater , container,false);

        init();
        preferenceManager = new PreferenceManager(getActivity());

        listenConversion();
        return binding.getRoot();
    }
    private void init() {
        conversions = new ArrayList<>();
        recentConversationAdapter = new RecentConversationAdapter(conversions,getActivity() ,true) ;
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

                        chatMessage.setReceiverImage(  documentChange.getDocument().getString(Constants.KEY_RECEIVER_IMAGE));
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
}