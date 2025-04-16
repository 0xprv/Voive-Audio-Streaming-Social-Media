package com.voive.android;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.button.MaterialButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.model.SlidrPosition;
import com.tapadoo.alerter.Alerter;
import com.thekhaeng.pushdownanim.PushDownAnim;

import butterknife.BindView;
import butterknife.ButterKnife;

public class roundtble_login extends AppCompatActivity {


    Handler handler = new Handler();
    @BindView(R.id.back)
    MaterialButton back;
    @BindView(R.id.invite_textview)
    TextView inviteTextview;
    @BindView(R.id.relativeLayout7)
    RelativeLayout relativeLayout7;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.constraintLayout8)
    ConstraintLayout constraintLayout8;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.constraintLayout8_0)
    ConstraintLayout constraintLayout80;
    @BindView(R.id.login2)
    MaterialButton login2;
    SlidrInterface slidrInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roundtble_login);
        ButterKnife.bind(this);
        SlidrConfig config = new SlidrConfig.Builder()
                .position(SlidrPosition.TOP)
                .sensitivity(1f)
                .scrimColor(Color.BLACK)
                .scrimStartAlpha(0.8f)
                .scrimEndAlpha(0f)
                .velocityThreshold(2400)
                .distanceThreshold(0.25f)
                .edge(true)
                .edgeSize(0.18f)
                .build();

        slidrInterface = Slidr.attach(this, config);

        login2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.card_view_color)));


        PushDownAnim.setPushDownAnimTo(login2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {

                        if (e == null) {


                            Intent intent = new Intent(roundtble_login.this, MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_from_end, R.anim.slide_to_start);
                            finish();
                            startingactivity.activity.finish();


                        } else {

                            Alerter.create(roundtble_login.this)
                                    .setTitle("Something Went Wrong :(")
                                    .setText(e.getLocalizedMessage())
                                    .setBackgroundColorInt(getResources().getColor(android.R.color.holo_red_dark))
                                    .hideIcon()
                                    .enableSwipeToDismiss()
                                    .setDuration(5000)
                                    .show();


                        }

                    }
                });

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.stationary, R.anim.bottom_down);

            }
        });


       /* PushDownAnim.setPushDownAnimTo(forgotPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RoundedBottomSheetDialog roundedBottomSheetDialog=new RoundedBottomSheetDialog(roundtble_login.this);
                roundedBottomSheetDialog.setContentView(R.layout.forgot_password_bottom_sheet);

                EditText editText=roundedBottomSheetDialog.findViewById(R.id.table_descripton);
                MaterialButton materialButton=roundedBottomSheetDialog.findViewById(R.id.banned);

                PushDownAnim.setPushDownAnimTo(materialButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!editText.getText().toString().trim().isEmpty()){

                            ParseUser.requestPasswordResetInBackground(editText.getText().toString(), new RequestPasswordResetCallback() {
                                @Override
                                public void done(ParseException e) {

                                    Toast.makeText(roundtble_login.this, "Email Sent . Check Your Email", Toast.LENGTH_SHORT).show();
                                    roundedBottomSheetDialog.dismiss();


                                }
                            });

                        }

                    }
                });


                roundedBottomSheetDialog.show();


            }
        });*/

        runnable.run();

    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            if (!username.getText().toString().trim().isEmpty() && !password.getText().toString().trim().isEmpty()) {

                login2.setEnabled(true);
                login2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

            }

            handler.postDelayed(runnable, 1000);

        }
    };

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.bottom_down);
    }
}


