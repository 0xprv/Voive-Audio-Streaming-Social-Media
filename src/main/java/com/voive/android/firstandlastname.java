package com.voive.android;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.button.MaterialButton;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;
import com.tapadoo.alerter.Alerter;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;


public class firstandlastname extends AppCompatActivity {

    public static String first_and_last_name, only_last_name;
    @BindView(R.id.login2)
    MaterialButton login2;
    @BindView(R.id.button12)
    MaterialButton button12;
    @BindView(R.id.firstname)
    EditText firstname;
    @BindView(R.id.lastname)
    EditText lastname;
    @BindView(R.id.constraintLayout9)
    ConstraintLayout constraintLayout9;
    @BindView(R.id.otpnum)
    EditText username;
    @BindView(R.id.constraintLayout8)
    ConstraintLayout constraintLayout8;
    @BindView(R.id.textView23)
    TextView textView23;

    public static Activity activity;


    private Handler j = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstandlastname);
        ButterKnife.bind(this);
        activity = this;

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
        SharedPreferences sharedPreferences = getSharedPreferences("LANG", MODE_PRIVATE);
        String lng = sharedPreferences.getString("lng", "en");
        Context context = LocaleHelper.setLocale(this, lng);
        Resources resources = context.getResources();

        login2.setText(resources.getString(R.string.create_account));
        String s = "By Creating Account You Will Agree Our Terms And Service And Privacy Policy . Thank You";
        SpannableString spannableString = new SpannableString(s);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {

                Intent browserIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.notion.so/Terms-And-Condition-a3b0e186120e4193b63130f7a8e1c033"));
                startActivity(browserIntent2);


            }
        };


        PushDownAnim.setPushDownAnimTo(login2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ACProgressFlower dialog = new ACProgressFlower.Builder(firstandlastname.this)
                        .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                        .themeColor(Color.WHITE)
                        .bgColor(R.color.card_view_color)
                        .fadeColor(R.color.card_view_color)
                        .bgAlpha(0)
                        .petalThickness(6)
                        .petalCount(20)
                        .speed(25)
                        .fadeColor(Color.DKGRAY).build();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                ParseUser parseUser = new ParseUser();
                parseUser.setUsername(username.getText().toString());
                parseUser.setPassword(App.getAppId()+App.getaudiokey());
                parseUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e == null) {

                            Random obj = new Random();
                            int rand_num = obj.nextInt(0xffffff + 1);

                            String colorCode = String.format("%06x", rand_num);

                            Glide.with(firstandlastname.this).asBitmap().load("https://avatars.dicebear.com/api/initials/"+username.getText().toString().trim()+".png?b=%23"+colorCode)
                                    .centerInside().into(new SimpleTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                            resource.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                            byte[] data = stream.toByteArray();
                                            ParseFile imageFile = new ParseFile("image.png", data);

                                            imageFile.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {

                                                    if (e == null) {


                                                        ParseUser.getCurrentUser().put("speaker_profile_pic", imageFile);
                                                        ParseUser.getCurrentUser().put("First_and_last_name", firstname.getText().toString() + " " + lastname.getText().toString());
                                                        ParseUser.getCurrentUser().put("NOTIFICATION_UPDATES", true);
                                                        ParseUser.getCurrentUser().put("NSFW",false);
                                                        ParseUser.getCurrentUser().put("PRIVATE_LISTENING",false);
                                                        ParseUser.getCurrentUser().put("WHAT_LISTENING","");
                                                        ParseUser.getCurrentUser().put("PHONE_NO",phonenumberforotp.number);
                                                        ParseUser.getCurrentUser().put("PREF","Hindi");
                                                        ParseUser.getCurrentUser().put("ALLOW_INVITES",true);
                                                        ParseUser.getCurrentUser().put("LAST_VISITED",new ArrayList<>());
                                                        ParseUser.getCurrentUser().put("Tastes_list", new ArrayList<>());
                                                        ParseUser.getCurrentUser().put("BLOCKED_TABLE",new ArrayList<>());
                                                        ParseUser.getCurrentUser().put("speaker_information", "");
                                                        ParseUser.getCurrentUser().put("Blocked_List", new ArrayList<String>());

                                                        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                                                            @Override
                                                            public void done(ParseException e) {

                                                                if (e == null) {


                                                                    ParseObject parseObject = new ParseObject("roundtable_user_data");
                                                                    parseObject.put("User_ID", ParseUser.getCurrentUser().getObjectId());
                                                                    parseObject.put("Subscriptions",new ArrayList<>());
                                                                    parseObject.put("who_notify", new ArrayList<>());
                                                                    parseObject.put("Invitations", new ArrayList<>());
                                                                    parseObject.saveInBackground(new SaveCallback() {
                                                                        @Override
                                                                        public void done(ParseException e) {


                                                                            if (e == null) {
                                                                                ParseInstallation parseInstallation = ParseInstallation.getCurrentInstallation();
                                                                                parseInstallation.put("UserId", ParseUser.getCurrentUser().getObjectId());
                                                                                parseInstallation.put("GCMSenderId", "517462011969");
                                                                                parseInstallation.saveInBackground();

                                                                                startingactivity.activity.finish();
                                                                                starting_language_picker.activity.finish();
                                                                                phonenumberforotp.activity.finish();
                                                                                finish();
                                                                                Intent intent=new Intent(firstandlastname.this,MainActivity.class);
                                                                                startActivity(intent);
                                                                                overridePendingTransition(R.anim.bottom_up, R.anim.stationary);


                                                                            } else {


                                                                                dialog.dismiss();

                                                                                Alerter.create(firstandlastname.this)
                                                                                        .setText(e.getMessage())
                                                                                        .setBackgroundColorInt(getResources().getColor(android.R.color.holo_red_light))
                                                                                        .hideIcon()
                                                                                        .enableSwipeToDismiss()
                                                                                        .setDuration(4000)
                                                                                        .show();


                                                                            }


                                                                        }
                                                                    });


                                                                    dialog.dismiss();

                                                                } else {

                                                                    Toast.makeText(firstandlastname.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    dialog.dismiss();
                                                                }

                                                            }
                                                        });


                                                    } else {

                                                        Alerter.create(firstandlastname.this)
                                                                .setText(e.getMessage())
                                                                .setBackgroundColorInt(getResources().getColor(android.R.color.holo_red_light))
                                                                .hideIcon()
                                                                .enableSwipeToDismiss()
                                                                .setDuration(4000)
                                                                .show();


                                                    }

                                                }
                                            });
                                        }
                                    });








                        } else {

                            ParseUser.logOut();
                            Alerter.create(firstandlastname.this)
                                    .setText(e.getLocalizedMessage())
                                    .setBackgroundColorInt(getResources().getColor(android.R.color.holo_red_light))
                                    .hideIcon()
                                    .setDuration(5000)
                                    .show();

                        }

                    }
                });


            }
        });

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

        runnable.run();

    }

    @OnClick(R.id.button12)
    public void onViewClicked() {

        finish();
        overridePendingTransition(R.anim.stationary, R.anim.bottom_down);

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {


            if (!username.getText().toString().trim().isEmpty() && !firstname.getText().toString().trim().isEmpty()) {

                login2.setEnabled(true);
                login2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                login2.setTextColor(getResources().getColor(R.color.black));

            } else {
                login2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.card_view_color)));
                login2.setEnabled(false);
                login2.setTextColor(getResources().getColor(R.color.white_50));

            }

            j.postDelayed(runnable, 1000);

        }
    };

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.bottom_down);
    }

}
