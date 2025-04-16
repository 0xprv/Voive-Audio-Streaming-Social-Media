package com.voive.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class backrrom_call_button extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String BACKROOM=intent.getStringExtra("BACKROOM");
        String USER=intent.getStringExtra("ID");

        Toast.makeText(context, BACKROOM, Toast.LENGTH_SHORT).show();
    }
}
