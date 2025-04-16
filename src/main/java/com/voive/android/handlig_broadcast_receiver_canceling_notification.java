package com.voive.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class handlig_broadcast_receiver_canceling_notification extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        special_speaker_live_table.is_joined=false;


    }
}
