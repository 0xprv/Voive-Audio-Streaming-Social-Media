package com.voive.android;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.button.MaterialButton;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;

import com.thekhaeng.pushdownanim.PushDownAnim;

import butterknife.BindView;
import butterknife.ButterKnife;

public class phonenumberforotp extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.login2)
    MaterialButton login2;
    @BindView(R.id.otpnum)
    EditText otpnum;
    public static String number;
    String prefix="+91";
    @BindView(R.id.button12)
    MaterialButton button12;
    @BindView(R.id.your_phone)
    TextView your_phone;

    public static Activity activity;

    int verifycase = 0;

    @BindView(R.id.dummy)
    LinearLayout dummy;
    @BindView(R.id.textView2)
    TextView textView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonenumberforotp);
        ButterKnife.bind(this);
        activity = this;
        SharedPreferences languagepref = getSharedPreferences("LANG", MODE_PRIVATE);
        String languagecode = languagepref.getString("lng", "en");
        Context context = LocaleHelper.setLocale(this, languagecode);
        Resources resources = context.getResources();
        your_phone.setText(resources.getText(R.string.Your_Phone));
        PushDownAnim.setPushDownAnimTo(dummy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(phonenumberforotp.this, resources.getString(R.string.We_support_india_only), Toast.LENGTH_SHORT).show();
            }
        });
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

        login2.setText(resources.getString(R.string.next_step));
        textView2.setText(resources.getString(R.string.We_support_india_only));

        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Auth.CREDENTIALS_API)
                .build();
        mGoogleApiClient.connect();

        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();

        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(mGoogleApiClient, hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(), 1008, null, 0, 0, 0, null);
        } catch (IntentSender.SendIntentException e) {
            Log.e("", "Could not start hint picker Intent", e);
        }


       PushDownAnim.setPushDownAnimTo(login2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (verifycase == 0) {
                    if (otpnum.getText().length() >= 10) {
                        number = prefix + otpnum.getText().toString();
                        Intent intent = new Intent(phonenumberforotp.this, OTPnumber.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
                    }
                } else {
                    number = otpnum.getText().toString();
                    Intent intent2 = new Intent(phonenumberforotp.this, OTPnumber.class);
                    startActivity(intent2);
                    overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
                }

            }
        });

        otpnum.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE:
                        if (verifycase == 0) {
                            if (otpnum.getText().length() >= 10) {
                                number = prefix + otpnum.getText().toString();
                                Intent intent = new Intent(phonenumberforotp.this, OTPnumber.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
                            }
                        } else {
                            number = prefix+otpnum.getText().toString();
                            Intent intent2 = new Intent(phonenumberforotp.this, OTPnumber.class);
                            startActivity(intent2);
                            overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
                        }

                        break;
                }
                return false;
            }
        });


        otpnum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



            }

            @Override
            public void afterTextChanged(Editable s) {

                if (otpnum.getText().length() >= 10) {

                    login2.setEnabled(true);
                    login2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    login2.setTextColor(getResources().getColor(android.R.color.black));

                } else {
                    login2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.card_view_color)));
                    login2.setEnabled(false);
                    login2.setTextColor(getResources().getColor(R.color.white_50));

                }
            }
        });
        button12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.stationary, R.anim.alerter_slide_out_to_bottom);

            }
        });

    }



    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.alerter_slide_out_to_bottom);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1008) {
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                if (credential != null) {
                    number = credential.getId();
                    Intent intent = new Intent(phonenumberforotp.this, OTPnumber.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.bottom_up, R.anim.stationary);

                } else {
                    Toast.makeText(this, "No Number Found", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    }
