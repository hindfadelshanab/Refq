package com.devhind.qibla.refg.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.devhind.qibla.refg.Adapter.ChatAdapter;
import com.devhind.qibla.refg.R;
import com.devhind.qibla.refg.databinding.ActivityChatBinding;
import com.devhind.qibla.refg.model.ChatMessage;
import com.devhind.qibla.refg.model.Doctor;
import com.devhind.qibla.refg.model.Notfication;
import com.devhind.qibla.refg.model.Request;
import com.devhind.qibla.refg.model.User;
import com.devhind.qibla.refg.network.ApiClient;
import com.devhind.qibla.refg.network.ApiService;
import com.devhind.qibla.refg.utilites.Constants;
import com.devhind.qibla.refg.utilites.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    private Request request;
    private String RVId;
    private List<ChatMessage> chatMessageList;
    private ChatAdapter chatAdapter;
    private PreferenceManager preferenceManager;
    ActivityChatBinding binding;
    private FirebaseFirestore database;
    private String conversationId = null;
    private Uri encodedImage = null;
    private int RESULT_LOAD_IMG = 100;
    String defaultImage = "https://firebasestorage.googleapis.com/v0/b/refg-74c1a.appspot.com/o/7853767_kashifarif_user_profile_person_account_icon.png?alt=media&token=03223828-1ef9-4eea-b211-e41a9c358b2b";
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());

        init();
        request = getIntent().getParcelableExtra("Request");
        RVId = getIntent().getStringExtra("receiverId");

        Log.e("senderid", preferenceManager.getString(Constants.KEY_USER_ID) + "( sender id )");
        Log.e("senderid", preferenceManager.getBoolean(Constants.KEY_IS_Older) + "( is older )");
        Log.e("senderid", preferenceManager.getBoolean(Constants.KEY_IS_Doctor) + "( is doctor )");
        Log.e("senderid3333", RVId + "( RVId)");

        if (request != null) {
            Doctor receiverUser = request.getDoctorRequest();
            getAllMessage(receiverUser.getId());
        } else if (RVId != null) {
            getAllMessage(RVId);
            binding.linearAcceptOrCancel.setVisibility(View.GONE);

        }
        binding.layoutSend.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if (binding.inputMessage.getText().toString().isEmpty()) {
                    Toast.makeText(ChatActivity.this, "No Message to send", Toast.LENGTH_SHORT).show();
                } else {


                    Date d = new Date();
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setMessage(binding.inputMessage.getText().toString());
                    chatMessage.setTime(d.getTime());

                    chatMessage.setSenderId(preferenceManager.getString(Constants.KEY_USER_ID));
                    chatMessage.setDateTime(getReadableDateTime(new Date()));

                    if (request != null) {
                        chatMessage.setReceiverId(request.getDoctorRequest().getId());
                        chatMessage.setReceiverImage(request.getDoctorRequest().getImage());
                        chatMessage.setReceiverName(request.getDoctorRequest().getName());
                        sendMessage(chatMessage, request.getDoctorRequest().getId(), getIntent().getStringExtra("token"), encodedImage, request.getDoctorRequest().getImage());

                    } else if (RVId != null) {
                        Log.e("reeee", RVId + "{jjdkldlk}");
                        binding.linearAcceptOrCancel.setVisibility(View.GONE);
                        chatMessage.setReceiverId(RVId);
                        sendMessage(chatMessage, RVId, getIntent().getStringExtra("token"), encodedImage, defaultImage);
                    }


                }

            }
        });

        binding.txtAcceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptRequest(request);
                Log.e("reeq", "clickkk");

                Log.e("reeq", request.getRequestId() + "yaaaa");
                Log.e("reeq", request.getDoctorRequest().getName() + "yaeae");
            }
        });


    }

    private void acceptRequest(Request request) {

        Request newRequest = new Request();
        newRequest.setRequestId(request.getRequestId());
        newRequest.setAccept(true);
        newRequest.setOrderID(request.getOrderID());
        newRequest.setDoctorRequest(request.getDoctorRequest());
        newRequest.setOldId(request.getOldId());
        database.collection(Constants.KEY_COLLECTION_ORDERS)
                .document(request.getOrderID())
                .collection(Constants.KEY_COLLECTION_REQUEST)
                .document(request.getRequestId())
                .set(newRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ChatActivity.this, "Request Accept", Toast.LENGTH_SHORT).show();
                        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_ORDERS)
                                .document(request.getOrderID());
                        documentReference.update("accept", true)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        showToast("Order Done");

                                        database.collection(Constants.KEY_COLLECTION_OLDER)
                                                .document(preferenceManager.getString(Constants.KEY_USER_ID))
                                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        User user = documentSnapshot.toObject(User.class);
                                                        makeNotification(getIntent().getStringExtra("token"),
                                                                request.getDoctorRequest().getId(),
                                                                user.getName() + "لقد تم قبول طلبك من قبل"
                                                        );
                                                    }
                                                });

                                    }
                                });


                        binding.linearAcceptOrCancel.setVisibility(View.GONE);

                        Log.e("reeq2", "Done");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("reeq2", "fail" + e.getMessage());

                    }
                });
    }

    private void init() {

        binding.chatRecyclerView.setAdapter(chatAdapter);
        database = FirebaseFirestore.getInstance();
        binding.layoutPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


            }
        });


    }

    private void sendMessage(ChatMessage chatMessage, String receiverUser, String tokenn,
                             Uri fileUri, String receiverImage) {


        if (fileUri != null) {
            String fileName = UUID.randomUUID().toString() + ".jpg";
            StorageReference refStorage = FirebaseStorage.getInstance().getReference().child("chat/" + fileName);
            refStorage.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {

                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = uri.toString();
                            chatMessage.setMessageImage(imageUrl);

                            DocumentReference ref =
                                    database.collection("Messages").document();
                            chatMessage.setMessageId(ref.getId());
                            ref.set(chatMessage).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(ChatActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                                    binding.inputMessage.getText().clear();
                                    getAllMessage(receiverUser);
                                    if (preferenceManager.getBoolean(Constants.KEY_IS_Doctor)) {
                                        database.collection(Constants.KEY_COLLECTION_DOCTOR)
                                                .document(preferenceManager.getString(Constants.KEY_USER_ID))
                                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        Doctor user = documentSnapshot.toObject(Doctor.class);

                                                    }
                                                });
                                    }


                                    //  if (preferenceManager.getBoolean(Constants.KEY_IS_Doctor)) {
                                    database.collection(Constants.KEY_COLLECTION_OLDER)
                                            .document(preferenceManager.getString(Constants.KEY_USER_ID))
                                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    User user = documentSnapshot.toObject(User.class);
                                                    makeNotification(tokenn, receiverUser,
                                                            user.getName() + "ارسل لك رسالة"
                                                    );
                                                }
                                            });

                                    makeNotification(tokenn, receiverUser,
                                            preferenceManager.getString(Constants.KEY_NAME) + "ارسل لك رسالة"
                                    );
                                    encodedImage = null;
                                    binding.imageInChat.setImageResource(R.drawable.ic_baseline_photo_24);
                                    if (conversationId != null) {
                                        updateConversation(binding.inputMessage.getText().toString());
                                    } else {
                                        HashMap<String, Object> map = new HashMap<>();
                                        map.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
                                        map.put(Constants.KEY_SENDER_NAME, preferenceManager.getString(Constants.KEY_NAME));
                                        map.put(Constants.KEY_SENDER_IMAGE, preferenceManager.getString(Constants.KEY_IMAGE));
                                        map.put(Constants.KEY_RECEIVER_ID, receiverUser);
                                        Log.e("reee3e", receiverUser + "{jjdkldlk}");
                                        map.put(Constants.KEY_RECEIVER_NAME, chatMessage.getReceiverName());
                                        map.put(Constants.KEY_RECEIVER_IMAGE, chatMessage.getReceiverImage());

                                        map.put(Constants.KEY_LAST_MESSAGE, chatMessage.message);
                                        map.put(Constants.KEY_TIMESTAMP, new Date());


                                        addConversation(map);

                                    }


                                    // send masses
                                }
                            });

                        }
                    });
                }
            });
        } else {
            DocumentReference ref =
                    database.collection("Messages").document();
            chatMessage.setMessageId(ref.getId());
            // makeNotification( receiverUser.getFcmToken(),receiverUser.getId());

            ref.set(chatMessage).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(ChatActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                    binding.inputMessage.getText().clear();
                    getAllMessage(receiverUser);
                    encodedImage = null;
                    makeNotification(tokenn, receiverUser,
                            preferenceManager.getString(Constants.KEY_NAME) + "ارسل لك رسالة"
                    );


                    binding.imageInChat.setImageResource(R.drawable.ic_baseline_photo_24);
                    if (conversationId != null) {
                        Log.e("ccoccc", conversationId + "8888");
                        updateConversation(chatMessage.message);

                    } else {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
                        map.put(Constants.KEY_SENDER_NAME, preferenceManager.getString(Constants.KEY_NAME));
                        map.put(Constants.KEY_SENDER_IMAGE, preferenceManager.getString(Constants.KEY_IMAGE));
                        map.put(Constants.KEY_RECEIVER_ID, receiverUser);
                        Log.e("reee3e", receiverUser + "{jjdkldlk}");

                        map.put(Constants.KEY_LAST_MESSAGE, chatMessage.message);
                        map.put(Constants.KEY_RECEIVER_NAME, chatMessage.getReceiverName());
                        map.put(Constants.KEY_RECEIVER_IMAGE, chatMessage.getReceiverImage());

                        map.put(Constants.KEY_TIMESTAMP, new Date());
                        addConversation(map);

                    }
                    //      Log.e("reeetooo" , receiverUser.getToken());


                    // send masses
                }
            });
        }


    }

    private void addConversation(HashMap<String, Object> conversation) {

        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_CONVERSATIONS).document();
        conversationId = documentReference.getId();

        documentReference.set(conversation).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });

    }

    private void updateConversation(String message) {
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .document(conversationId);
        documentReference.update(Constants.KEY_LAST_MESSAGE, message,
                Constants.KEY_TIMESTAMP, new Date());

    }

    private final OnCompleteListener<QuerySnapshot> conversationCompleteListener = task -> {
        if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
            conversationId = documentSnapshot.getId();
        }
    };


    private void getAllMessage(String receiverId) {

        chatMessageList = new ArrayList<>();
        database.collection("Messages")
                .whereEqualTo("senderId", preferenceManager.getString(Constants.KEY_USER_ID))
                .whereEqualTo("receiverId", receiverId)

                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w("TAG", "Listen failed.", error);
                            return;
                        }
                        if (value != null) {
                            int count = chatMessageList.size();

                            chatMessageList.addAll(value.toObjects(ChatMessage.class));



                            chatAdapter = new ChatAdapter(chatMessageList, preferenceManager.getString(Constants.KEY_USER_ID));
                            //   chatAdapter = new ChatAdapter(chatMessageList, preferenceManager.getString(Constants.KEY_USER_ID));

                            binding.chatRecyclerView.setAdapter(chatAdapter);
                            binding.chatRecyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
                            binding.chatRecyclerView.scrollToPosition(chatMessageList.size() - 1);


//                            if (chatMessageList.size()>0) {
//                                Collections.sort(chatMessageList, (chatMessage1, chatMessage2) -> chatMessage1.time.compareTo(chatMessage2.time)
//                                );
//                            }
//                            Collections.sort(chatMessageList, (chatMessage, chatMessage2) ->
//                                    chatMessage.dateTime.compareTo(chatMessage2.dateTime));
                            if (count == 0) {
                                chatAdapter.notifyDataSetChanged();
                            } else {
                                chatAdapter.notifyItemRangeInserted(chatMessageList.size(), chatMessageList.size());
                                binding.chatRecyclerView.smoothScrollToPosition(chatMessageList.size() - 1);
                            }
                            ;

                        } else {
                            Log.d("TAG", "Current data: null");
                        }
                        if (conversationId == null) {
                            checkForConversation();
                        }
                  //      Toast.makeText(ChatActivity.this, "chat 1" + chatMessageList.size() + "size", Toast.LENGTH_SHORT).show();

                    }
                });
        database.collection("Messages")
                .whereEqualTo("senderId", receiverId)
                .whereEqualTo("receiverId", preferenceManager.getString(Constants.KEY_USER_ID))

                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w("TAG", "Listen failed.", error);
                            return;
                        }
                        if (value != null) {
                            int count = chatMessageList.size();

                            chatMessageList.addAll(value.toObjects(ChatMessage.class));

                            chatMessageList.sort((a,b)-> a.time.compareTo(b.time) );
                            chatAdapter = new ChatAdapter(chatMessageList, preferenceManager.getString(Constants.KEY_USER_ID));
                            //   chatAdapter = new ChatAdapter(chatMessageList, preferenceManager.getString(Constants.KEY_USER_ID));
                   //         Toast.makeText(ChatActivity.this, "chat2" + chatMessageList.size() + "size", Toast.LENGTH_SHORT).show();

                            binding.chatRecyclerView.setAdapter(chatAdapter);
                            binding.chatRecyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
                            binding.chatRecyclerView.scrollToPosition(chatMessageList.size() - 1);
//                            if (chatMessageList.size()>0) {
//                                Collections.sort(chatMessageList, (chatMessage1, chatMessage2) -> chatMessage1.time.compareTo(chatMessage2.time)
//                                );
//                            }
                           // Collections.sort(chatMessageList, (chatMessage, chatMessage2) -> chatMessage.time.compareTo(chatMessage2.time));
                            if (count == 0) {
                                chatAdapter.notifyDataSetChanged();
                            } else {
                                chatAdapter.notifyItemRangeInserted(chatMessageList.size(), chatMessageList.size());
                                binding.chatRecyclerView.smoothScrollToPosition(chatMessageList.size() - 1);
                            }
                            ;

                        } else {
                            Log.d("TAG", "Current data: null");
                        }

                        if (conversationId == null) {
                            checkForConversation();
                        }
                    }
                });

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getReadableDateTime(Date date) {
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

    private void checkForConversation() {
        if (chatMessageList.size() != 0) {
            checkConversationRemotely(preferenceManager.getString(Constants.KEY_USER_ID), RVId);
            checkConversationRemotely(RVId, preferenceManager.getString(Constants.KEY_USER_ID));

        }
    }

    private void checkConversationRemotely(String senderId, String receiverId) {

        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID, senderId)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverId)
                .get()
                .addOnCompleteListener(conversationCompleteListener);

    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                binding.imageInChat.setImageBitmap(selectedImage);
                encodedImage = imageUri;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(ChatActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(ChatActivity.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }


    private void showToast(String messgae) {
        Toast.makeText(ChatActivity.this, messgae, Toast.LENGTH_SHORT).show();
    }


    private void makeNotification(String token, String reciverId, String boddy) {
        try {

            JSONArray tokens = new JSONArray();
            tokens.put(token);

            JSONObject data = new JSONObject();
            data.put(Constants.KEY_USER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
            data.put(Constants.KEY_NAME, preferenceManager.getString(Constants.KEY_NAME));
            data.put(Constants.KEY_FCM_TOKEN, preferenceManager.getString(Constants.KEY_FCM_TOKEN));
            data.put(Constants.KEY_MESSAGE, boddy);

            JSONObject body = new JSONObject();
            body.put(Constants.REMOTE_MSG_DATA, data);
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

            sendNotification(body.toString(), reciverId, boddy);

        } catch (Exception e) {
            showToast(e.getMessage());
            Log.e("fgggdjkkksk", e.getMessage() + "1");
        }

    }

    private void sendNotification(String messageBody, String receiverId, String body) {
        ApiClient.getClient().create(ApiService.class).sendMessage(Constants.getremoteMsgHeaders()
                , messageBody).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body() != null) {
                            JSONObject responseJson = new JSONObject(response.body());
                            JSONArray results = responseJson.getJSONArray("results");
                            if (responseJson.getInt("failure") == 1) {
                                JSONObject error = (JSONObject) results.get(0);
                                showToast(error.getString("error"));
                                return;
                            }
                        }
                    } catch (JSONException e) {
                        Log.e("Error", e.getMessage());
                    }
                    showToast("Notification Send Successfully");
                    Notfication notfication = new Notfication();
                    notfication.setMessage(body);
                    notfication.setReceiverId(receiverId);
                    notfication.setSenderId(preferenceManager.getString(Constants.KEY_USER_ID));
                    database.collection(Constants.KEY_COLLECTION_NOTIFICATION)
                            .add(notfication).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    showToast("Notification added");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    showToast("Notification added fail");

                                }
                            });

                } else {

                    showToast("Error" + response.errorBody());

                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                showToast(t.getMessage());
                Log.e("fgggdjkkksk", t.getMessage() + "66");

            }
        });
    }
}
