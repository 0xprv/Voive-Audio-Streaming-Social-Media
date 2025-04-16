package com.voive.android;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

public class RECEIVE_IN_APP_CALL extends Activity {
    private static final String LOG_TAG = "SMSReceiver";
    public static final int NOTIFICATION_ID_RECEIVED = 0x1221;
    static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.nearby_conversation_alert_dialog);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

                finish();
            }
        });
          alertDialog.getWindow().getAttributes().windowAnimations = R.style.Message_Animation;

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
        layoutParams.width = 500;
        alertDialog.getWindow().setAttributes(layoutParams);
    }




   /* private final round_table_notification_service mReceivedSMSReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (ACTION.equals(action)) {
                //your SMS processing code
                displayAlert();
            }
        }

    }*/
}