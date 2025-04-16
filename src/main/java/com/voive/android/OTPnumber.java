package com.voive.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;
import com.tapadoo.alerter.Alerter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;


public class OTPnumber extends AppCompatActivity {

    @BindView(R.id.login2)
    MaterialButton login2;
    @BindView(R.id.otpnum)
    EditText otpnum;

    @BindView(R.id.resend)
    TextView resend;
    @BindView(R.id.button12)
    MaterialButton button12;
    public static Activity activity;
    String verificationid;
    FirebaseAuth mAuth;
    @BindView(R.id.textView2)
    TextView OTPVerificationText;
    @BindView(R.id.immediateStop)
    TextView immediateStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpnumber);
        ButterKnife.bind(this);
        activity = this;
        SharedPreferences languagepref=getSharedPreferences("LANG",MODE_PRIVATE);
        String languagecode=languagepref.getString("lng","en");
        Context context = LocaleHelper.setLocale(this, languagecode);
        Resources resources = context.getResources();

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

        OTPVerificationText.setText(resources.getString(R.string.OTP_verification));
        login2.setText(resources.getString(R.string.next_step));
        immediateStop.setText(resources.getString(R.string.is_no_yours));
        resend.setText(resources.getString(R.string.resend_code));
        mAuth = FirebaseAuth.getInstance();


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phonenumberforotp.number, // first parameter is user's mobile number
                60, // second parameter is time limit for OTP
                // verification which is 60 seconds in our case.
                TimeUnit.SECONDS,
                OTPnumber.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){


                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationid = s;
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        String code = phoneAuthCredential.getSmsCode();
                        if (code != null) {
//                        progressBar.setVisibility(View.VISIBLE);

                            otpnum.setText(code);

                            verifyCode(code);
                        }
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                      //  otpnum.setError("OTP Is Wrong :(");

                        final Typeface typeface = ResourcesCompat.getFont(OTPnumber.this, R.font.montr_bold);
                        Alerter.create(OTPnumber.this)
                                .setText(resources.getString(R.string.fifty_veri_a_day))
                                .setTextTypeface(typeface)
                                .setEnterAnimation(R.anim.bottom_down)
                                .setExitAnimation(R.anim.bottom_up)
                                .setBackgroundColorInt(context.getResources().getColor(android.R.color.holo_red_light))
                                .hideIcon()
                                .enableSwipeToDismiss()
                                .setDuration(4000)
                                .show();

                    }
                }

        );

        login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }


    private void verifyCode(String code) {
        ACProgressFlower dialog = new ACProgressFlower.Builder(OTPnumber.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .bgColor(R.color.card_view_color)
                .fadeColor(R.color.card_view_color)
                .bgAlpha(0)
                .petalThickness(6)
                .petalCount(20)
                .speed(25)
                .fadeColor(Color.DKGRAY).build();
        dialog.show();

        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationid, code);
            signInWithCredential(credential);
            dialog.dismiss();
        }
        catch (Exception df){

            Alerter.create(OTPnumber.this)
                    .setText(df.getMessage())
                    .setBackgroundColorInt(getResources().getColor(android.R.color.holo_red_light))
                    .hideIcon()
                    .enableSwipeToDismiss()
                    .setDuration(4000)
                    .show();
            df.printStackTrace();
            dialog.dismiss();
        }

   //     otpnum.setText(code);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            ParseQuery<ParseUser> parseUserParseQuery=ParseUser.getQuery();
                            parseUserParseQuery.whereNotEqualTo("PHONE_NO",null);
                            parseUserParseQuery.whereEqualTo("PHONE_NO",phonenumberforotp.number);
                            parseUserParseQuery.findInBackground(new FindCallback<ParseUser>() {
                                @Override
                                public void done(List<ParseUser> objects, ParseException e) {


                                    try {


                                        if (objects.size() > 0) {

                                            ParseUser.logInInBackground(objects.get(0).getUsername(), App.getAppId()+App.getaudiokey(), new LogInCallback() {
                                                @Override
                                                public void done(ParseUser user, ParseException e) {

                                                    if (e == null) {
                                                        Toast.makeText(OTPnumber.this, "Verified 👍", Toast.LENGTH_SHORT).show();
                                                        startingactivity.activity.finish();
                                                        starting_language_picker.activity.finish();
                                                        phonenumberforotp.activity.finish();
                                                        finish();
                                                        Intent intent = new Intent(OTPnumber.this, MainActivity.class);
                                                        startActivity(intent);
                                                        overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
                                                    } else {

                                                        Toast.makeText(OTPnumber.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                                    }

                                                }
                                            });

                                        } else {
                                            finish();
                                            Intent intent = new Intent(OTPnumber.this, firstandlastname.class);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
                                        }

                                    }
                                    catch (Exception exx){

                                        Toast.makeText(OTPnumber.this, exx.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        } else {
                            Toast.makeText(OTPnumber.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }



    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.bottom_down);
    }

    @OnClick(R.id.button12)
    public void onViewClicked() {

        finish();
        overridePendingTransition(R.anim.stationary, R.anim.bottom_down);
    }
}
