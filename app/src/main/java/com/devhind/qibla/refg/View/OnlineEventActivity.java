package com.devhind.qibla.refg.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import com.devhind.qibla.refg.databinding.ActivityOnlineEventBinding;

import org.jitsi.meet.sdk.BroadcastEvent;
import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

import timber.log.Timber;

public class OnlineEventActivity extends AppCompatActivity {

    ActivityOnlineEventBinding binding;

    private BroadcastReceiver broadcastReceiver =  new BroadcastReceiver()  {

        @Override
        public void onReceive(Context context, Intent intent) {
            onBroadcastReceived(intent);

        }


    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOnlineEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        URL serverURL =null;
        try {
            serverURL = new URL("https://meet.jit.si");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        JitsiMeetConferenceOptions defaultOptions = new JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverURL)

                .setFeatureFlag("welcomepage.enabled", false)
                .build();
        JitsiMeet.setDefaultConferenceOptions(defaultOptions);

        registerForBroadcastMessages();

        binding.button4.setOnClickListener(view -> joinToEvent());


    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);

        super.onDestroy();
    }

    private void joinToEvent() {
         String text = binding.conferenceName.getText().toString();
        if (!text.isEmpty()) {

            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                    .setRoom(text)
                    .build();
            JitsiMeetActivity.launch(this, options);
        }
    }



    private void registerForBroadcastMessages() {
        IntentFilter intentFilter = new IntentFilter();

        /* This registers for every possible event sent from JitsiMeetSDK
           If only some of the events are needed, the for loop can be replaced
           with individual statements:
           ex:  intentFilter.addAction(BroadcastEvent.Type.AUDIO_MUTED_CHANGED.action);
                intentFilter.addAction(BroadcastEvent.Type.CONFERENCE_TERMINATED.action);
                ... other events
         */


        for (BroadcastEvent.Type type :BroadcastEvent.Type.values()){
            intentFilter.addAction(type.getAction());
        }
        LocalBroadcastManager.getInstance(this
        ).registerReceiver(broadcastReceiver, intentFilter);
    }

    private void onBroadcastReceived( Intent intent) {
        if (intent != null) {
            BroadcastEvent event = new BroadcastEvent(intent);

            if (event.getType()==BroadcastEvent.Type.CONFERENCE_JOINED){
                Timber.i("Conference Joined with url%s", event.getData().get("url"));

            }else if (event.getType()==BroadcastEvent.Type.PARTICIPANT_JOINED){
                Timber.i("Participant joined%s", event.getData().get("name"));
            }else {
                Timber.i("Received event: %s", event.getType());
            }
        }
    }
}