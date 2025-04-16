package com.voive.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Network_change_listner extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        int status = Network_Check.getConnectivityStatusString(context);
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            if (status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {

                Intent intent1=new Intent(context,no_network_activity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                App.activity.startActivity(intent1);
            } else {

                if(no_network_activity.activity!=null){
                    no_network_activity.activity.finish();
                }
            }
        }

    }
}
