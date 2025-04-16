package com.voive.android;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.button.MaterialButton;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;
import com.tapadoo.alerter.Alerter;
import com.thekhaeng.pushdownanim.PushDownAnim;

import butterknife.BindView;
import butterknife.ButterKnife;


public class pickingusername extends AppCompatActivity {


    @BindView(R.id.otpnum)
    EditText otpnum;
    @BindView(R.id.textView23)
    TextView textView23;
    @BindView(R.id.login2)
    MaterialButton login2;
    @BindView(R.id.dd)
    ConstraintLayout dd;
    @BindView(R.id.button12)
    MaterialButton button12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickingusername);
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

        Slidr.attach(this,config);
        String s = "By Creating Account You Will Agree Our Terms And Service And Privacy Policy . Thank You";
        SpannableString spannableString = new SpannableString(s);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {

                Intent browserIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.notion.so/Terms-And-Condition-a3b0e186120e4193b63130f7a8e1c033"));
                startActivity(browserIntent2);


            }
        };
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {


                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.notion.so/Privacy-Policy-d5ebe9a6f2e540d9afcf42e6d2ce65f0"));
                startActivity(browserIntent);

            }
        };
        spannableString.setSpan(clickableSpan, 39, 56, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(clickableSpan1, 61, 75, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView23.setText(spannableString);
        textView23.setMovementMethod(LinkMovementMethod.getInstance());

        PushDownAnim.setPushDownAnimTo(button12).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.stationary, R.anim.alerter_slide_out_to_bottom);
            }
        });

        PushDownAnim.setPushDownAnimTo(login2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!otpnum.getText().toString().trim().isEmpty()){

                    if(otpnum.getText().toString().length()>=4){


                        ParseUser.getCurrentUser().setUsername(otpnum.getText().toString());
                        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {

                                if(e==null){

                                    startingactivity.activity.finish();
                                    finish();
                                    Intent intent=new Intent(pickingusername.this,MainActivity.class);
                                    startActivity(intent);


                                }
                                else {

                                    Alerter.create(pickingusername.this)
                                            .setTitle(e.getMessage())
                                            .setBackgroundColorInt(getResources().getColor(android.R.color.holo_red_light))
                                            .hideIcon()
                                            .enableSwipeToDismiss()
                                            .setDuration(4000)
                                            .show();
                                }

                            }
                        });

                    }
                    else {

                        Toast.makeText(pickingusername.this, "Username Atleast Contains 4 letters 😅", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(pickingusername.this, "Empty Username Not Allowed 😅", Toast.LENGTH_SHORT).show();

                }



            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.alerter_slide_out_to_bottom);
    }
}

