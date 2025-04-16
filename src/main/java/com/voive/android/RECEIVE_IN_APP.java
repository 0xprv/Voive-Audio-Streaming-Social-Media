package com.voive.android;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.button.MaterialButton;


public class RECEIVE_IN_APP extends Activity {
    private static final String LOG_TAG = "SMSReceiver";
    public static final int NOTIFICATION_ID_RECEIVED = 0x1221;
    static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    String type="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        
        type=getIntent().getStringExtra("TYPE");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.simple_alerter_layout);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

                finish();
            }
        });
          alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
      //  alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = alertDialog.getWindow().getAttributes();

        wmlp.gravity = Gravity.TOP ;
       // wmlp.x = 100;   //x position
       wmlp.y = 24;   //y position

        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

        TextView title=alertDialog.findViewById(R.id.textView38);
        TextView des=alertDialog.findViewById(R.id.des);

        ConstraintLayout constraintLayout=alertDialog.findViewById(R.id.back);

        LinearLayout linearLayout=alertDialog.findViewById(R.id.control_linear);
        MaterialButton accept=alertDialog.findViewById(R.id.deactivate);
        MaterialButton reject=alertDialog.findViewById(R.id.no);


        title.setText(getIntent().getStringExtra("TITLE"));
        des.setText(getIntent().getStringExtra("ALERT"));


        
        
        

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