package com.voive.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.RemoteInput;

public class message_button_receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        Bundle bundle= RemoteInput.getResultsFromIntent(intent);

        if(bundle!=null){

            CharSequence text=bundle.getCharSequence("Key_For_Notification");
            Toast.makeText(context, text.toString(), Toast.LENGTH_SHORT).show();
            Notification_Message notification_message=new Notification_Message(null,text);
            App.notification_messages.add(notification_message);

        }

    }
}
