package com.voive.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.parse.ParseObject;

public class handlig_broadcast_receiver_backroom extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        ParseObject.registerSubclass(notification_modal.class);
        String ACTION=intent.getAction();

      if(ACTION.equals("ACCEPT")){
          


      

      }
      else {




      }

    }
}
