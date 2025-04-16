package com.voive.android;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class App extends Application implements LifecycleObserver, NetworkChangeReceiver.ConnectivityReceiverListener {


    public static final String CLIENT_ID = "326267888873345";

    //NOTIFICATION CHANNELS
    public static final String TABLE_ACTIVITY = "TABLE_ACTIVITY";
    public static final String PLAYBACK="TABLE_PLAYBACK";
    public static final String NEARBY_LOCATION = "NEARBY_LOCATION";
    public static final String SOMEONE_FOLLOW = "SOMEONE_FOLLOW";
    public static final String INVITATION_SENT = "INVITATION_SENT";
    public static final String BAN_OR_KICKED = "BAN_OR_KICKED";
    public static final String POEPLE_NOTIFY_WHEN_THEY_START_TO_TALK = "POEPLE_NOTIFY_WHEN_THEY_START_TO_TALK";
    public static final String CHANNEL_OTHERS = "OTHERS";
    public static List<Notification_Message> notification_messages = new ArrayList<>();
    public static String GCM_ID;
    private static App mInstance;
    public static boolean IS_LISTENING=false;
    public static Context activity;
    public static String WHICH_TABLE="";
    private ValueChangeListener visibilityChangeListener;
    Network_change_listner network_change_listner=new Network_change_listner();

    public static synchronized App getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        activity=this;
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getAppId())
                .clientKey(getclient())
                .server(getserver()).build());

        if (ParseUser.getCurrentUser() != null) {
            ParseInstallation parseInstallation = ParseInstallation.getCurrentInstallation();
            parseInstallation.put("UserId", ParseUser.getCurrentUser().getObjectId());
            parseInstallation.put("GCMSenderId", getGCM());
            parseInstallation.saveInBackground();
        } else {

            ParseInstallation parseInstallation = ParseInstallation.getCurrentInstallation();
            parseInstallation.put("UserId", "");
            parseInstallation.put("GCMSenderId", getGCM());
            parseInstallation.saveInBackground();
        }



        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);


        IntentFilter intentFilter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(network_change_listner,intentFilter);

        //  GCM_ID=dataSnapshot.child("GCM").getValue().toString();

        ParseObject.registerSubclass(Backroom_Messages.class);

        ParseObject.registerSubclass(notification_modal.class);

        createNotificationChannels();


        mInstance = this;
        App.getInstance().setConnectivityListener(this);
        // addObserver
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        App.getInstance().setOnVisibilityChangeListener(new ValueChangeListener() {
            @Override
            public void onChanged(Boolean value) {

                if (value) {

                    round_table_notification_service.is_open=false;

                } else {
                    round_table_notification_service.is_open=true;


                }

            }
        });


    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            //CREATION OF NOTIFICATIONS CHANNELS
            NotificationChannel PLAYBACK_CHANNEL = new NotificationChannel(
                    PLAYBACK,
                    "Playback",
                    NotificationManager.IMPORTANCE_LOW);
            PLAYBACK_CHANNEL.setSound(null, null);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(PLAYBACK_CHANNEL);

            NotificationChannel channel_table_activity = new NotificationChannel(
                    TABLE_ACTIVITY,
                    "Table Activity",
                    NotificationManager.IMPORTANCE_HIGH);
            channel_table_activity.setSound(alarmSound, audioAttributes);
            NotificationManager manager_2 = getSystemService(NotificationManager.class);
            manager_2.createNotificationChannel(channel_table_activity);


            NotificationChannel channel_special_speaker = new NotificationChannel(
                    NEARBY_LOCATION,
                    "Nearby Conversation",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel_special_speaker.setSound(alarmSound, audioAttributes);
            NotificationManager manager_3 = getSystemService(NotificationManager.class);
            manager_3.createNotificationChannel(channel_special_speaker);


            NotificationChannel channel_MENTIONS = new NotificationChannel(
                    SOMEONE_FOLLOW,
                    "Someone Follows",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel_MENTIONS.setSound(alarmSound, audioAttributes);
            channel_MENTIONS.setLightColor(R.color.colorAccent);
            NotificationManager manager_4 = getSystemService(NotificationManager.class);
            manager_4.createNotificationChannel(channel_MENTIONS);


            NotificationChannel channel_FOLLOW = new NotificationChannel(
                    INVITATION_SENT,
                    "Invitation",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel_FOLLOW.setSound(alarmSound, audioAttributes);
            NotificationManager manager_5 = getSystemService(NotificationManager.class);
            manager_5.createNotificationChannel(channel_FOLLOW);


            NotificationChannel channel_SECTION = new NotificationChannel(
                    BAN_OR_KICKED,
                    "Banned or kicked From Table",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel_SECTION.setSound(alarmSound, audioAttributes);
            NotificationManager manager_6 = getSystemService(NotificationManager.class);
            manager_6.createNotificationChannel(channel_SECTION);


            NotificationChannel channel_CCR = new NotificationChannel(
                    POEPLE_NOTIFY_WHEN_THEY_START_TO_TALK,
                    "Get Notify When People Start To Talk",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel_CCR.setSound(alarmSound, audioAttributes);
            NotificationManager manager_6C = getSystemService(NotificationManager.class);
            manager_6C.createNotificationChannel(channel_CCR);


        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onEnterForeground() {
        Log.d("AppController", "Foreground");
        isAppInBackground(false);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onEnterBackground() {
        Log.d("AppController", "Background");
        isAppInBackground(true);
    }



    public void setOnVisibilityChangeListener(ValueChangeListener listener) {
        this.visibilityChangeListener = listener;
    }

    private void isAppInBackground(Boolean isBackground) {
        if (null != visibilityChangeListener) {
            visibilityChangeListener.onChanged(isBackground);
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Toast.makeText(mInstance, "done", Toast.LENGTH_SHORT).show();
    }

    public void setConnectivityListener(NetworkChangeReceiver.ConnectivityReceiverListener listener) {
        NetworkChangeReceiver.connectivityReceiverListener = listener;
    }

    public interface ValueChangeListener {
        void onChanged(Boolean value);
    }

    //Server Works
    public static native String getaudiokey();
    public static native String getAudiocertificate();
    public static native String getAppId();
    public static native String getclient();
    public static native String getserver();
    public static native String getLIVE();
    public static native String getGCM();
    static {
        System.loadLibrary("api-keys");
    }


}
