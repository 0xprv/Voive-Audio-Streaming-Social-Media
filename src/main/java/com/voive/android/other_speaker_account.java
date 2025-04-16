package com.voive.android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.model.SlidrPosition;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.OnBalloonDismissListener;
import com.thekhaeng.pushdownanim.PushDownAnim;
import com.xw.repo.widget.BounceScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class other_speaker_account extends AppCompatActivity {

    public static Bitmap bbtt = null;
    @BindView(R.id.back)
    MaterialButton back;
    @BindView(R.id.more_about_user)
    MaterialButton moreAboutUser;
    @BindView(R.id.profile_pic)
    CircleImageView profilePic;
    @BindView(R.id.first_last_name)
    TextView firstLastName;
    @BindView(R.id.roundtable_username)
    TextView roundtableUsername;
    @BindView(R.id.speaker_follow_button)
    MaterialButton speakerFollowButton;
    @BindView(R.id.speaker_description)
    TextView speakerDescription;
    @BindView(R.id.bouncy_scroll)
    BounceScrollView bouncyScroll;
    @BindView(R.id.scroll_change_view)
    View scrollChangeView;
    @BindView(R.id.linearLayout3)
    RelativeLayout linearLayout3;
    Resources resources;
    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.suggestion)
    RecyclerView suggestion_recyler;
    @BindView(R.id.suggestion_conatainer)
    LinearLayout suggestionConatainer;
    @BindView(R.id.more_like)
    TextView moreLike;
    Palette.Swatch main_swatch;
    ParseObject main_object;
    @BindView(R.id.subscribed_table_textview)
    TextView subscribedTableTextview;
    @BindView(R.id.subscribed_table_recyler)
    RecyclerView subscribedTableRecyler;
    @BindView(R.id.subscribed_tables_container)
    LinearLayout subscribedTablesContainer;
    @BindView(R.id.what_listening_circular_imageview)
    CircleImageView whatListneningCircular;
    @BindView(R.id.XCV)
    ConstraintLayout conditional_container;
    @BindView(R.id.MAIN_DATE)
    MaterialButton show_all;
    SlidrInterface slidrInterface;
    String PASSED_ID = "";
    @BindView(R.id.shimmer)
    ShimmerFrameLayout shimmerFrameLayout;
    @BindView(R.id.to_show)
    ConstraintLayout to_show;
    @BindView(R.id.follower)
    TextView how_notify;
    @BindView(R.id.follower_click_linear)
    LinearLayout notify_linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_speaker_account);
        ButterKnife.bind(this);

        PASSED_ID=getIntent().getStringExtra("ID");

        SharedPreferences sharedPreferences = getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng = sharedPreferences.getString("lng", "hi");
        boolean is_first_popup=sharedPreferences.getBoolean("FIRST_POPUP_ACC",true);
        Context language_context = LocaleHelper.setLocale(this, lng);
        resources = language_context.getResources();




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

        bouncyScroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    slidrInterface.lock();
                    // Do what you want
                } else {

                    slidrInterface.unlock();
                }
                return false;
            }
        });


        try {


            ParseQuery<ParseUser> getted_all_detail = ParseUser.getQuery();
            getted_all_detail.getInBackground(PASSED_ID, new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser object, ParseException e) {

                    if (object.getString("speaker_information").trim().isEmpty()) {

                        speakerDescription.setVisibility(View.GONE);
                    } else {
                        speakerDescription.setVisibility(View.VISIBLE);
                        speakerDescription.setText(object.getString("speaker_information"));
                    }


                    main_object = object;

                    roundtableUsername.setText("@"+object.getUsername());

                    firstLastName.setText(object.getString("First_and_last_name"));
                    if(is_first_popup){
                        String finaltext = resources.getString(R.string.when_bla_start_speaking,object.getString("First_and_last_name"));
                        final Typeface typeface = ResourcesCompat.getFont(other_speaker_account.this, R.font.montr_bold);
                        Balloon balloon = new Balloon.Builder(other_speaker_account.this.getApplicationContext())
                                .setArrowSize(10)
                                .setArrowOrientation(ArrowOrientation.TOP)
                                .setIsVisibleArrow(true)
                                .setArrowPosition(0.5f)
                                .setWidthRatio(0.6f)
                                .setTextSize(15f)
                                .setPaddingLeft(16)
                                .setPaddingRight(16)
                                .setPaddingTop(8)
                                .setPaddingBottom(8)
                                .setPaddingLeft(16)
                                .setPaddingRight(16)
                                .setTextTypeface(typeface)
                                .setCornerRadius(6f)
                                .setAlpha(1f)
                                .setBalloonAnimation(BalloonAnimation.CIRCULAR)
                                .setText(finaltext)
                                .setTextColor(resources.getColor(R.color.black))
                                .setBackgroundColor(resources.getColor(R.color.colorAccent))
                                .setBalloonAnimation(BalloonAnimation.FADE)
                                .build();

                        balloon.setOnBalloonDismissListener(new OnBalloonDismissListener() {
                            @Override
                            public void onBalloonDismiss() {

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("FIRST_POPUP_ACC", false);
                                editor.apply();

                            }
                        });
                        balloon.dismissWithDelay(20000L);
                        balloon.showAlignBottom(speakerFollowButton);
                    }

                    ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("roundtable_user_data");
                    parseObjectParseQuery.whereEqualTo("User_ID", PASSED_ID);
                    parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {

                            ParseQuery<ParseObject> LIVE_TABLES_QUERY = new ParseQuery<ParseObject>("live_tables");
                            LIVE_TABLES_QUERY.whereContainedIn("objectId", objects.get(0).getList("Subscriptions"));
                            LIVE_TABLES_QUERY.whereEqualTo("IS_PRIVATE",false);
                            LIVE_TABLES_QUERY.setLimit(1);
                            LIVE_TABLES_QUERY.countInBackground(new CountCallback() {
                                @Override
                                public void done(int count, ParseException e) {


                                    if (count > 0) {
                                        subscribedTablesContainer.setVisibility(View.VISIBLE);
                                        subscribedTableTextview.setText(Integer.toString(count) + " " + resources.getString(R.string.subscriptions));
                                        ParseQuery<ParseObject> In_PIENCE = new ParseQuery<ParseObject>("live_tables");
                                        In_PIENCE.whereContainedIn("objectId", objects.get(0).getList("Subscriptions"));
                                        In_PIENCE.whereEqualTo("IS_PRIVATE",false);
                                        In_PIENCE.setLimit(12);
                                        In_PIENCE.findInBackground(new FindCallback<ParseObject>() {
                                            @Override
                                            public void done(List<ParseObject> objects, ParseException e) {


                                                subscribedTableRecyler.setLayoutManager(new LinearLayoutManager(other_speaker_account.this, LinearLayoutManager.VERTICAL, false));
                                                subscribedTableRecyler.setAdapter(new full_width_table_adapter(other_speaker_account.this, objects));

                                            }
                                        });
                                        PushDownAnim.setPushDownAnimTo(subscribedTableTextview).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(other_speaker_account.this, mutual_tables_activity.class);
                                                intent.putExtra("ID", object.getObjectId());
                                                intent.setType("SUBS");
                                                startActivity(intent);
                                                overridePendingTransition(R.anim.slide_from_end, R.anim.stationary);
                                            }
                                        });


                                    } else {
                                        subscribedTablesContainer.setVisibility(View.GONE);
                                    }

                                }
                            });



                        }
                    });


                    Glide.with(other_speaker_account.this.getApplicationContext()).asBitmap().load(object.getParseFile("speaker_profile_pic").getUrl())
                            .centerInside().placeholder(R.drawable.theme_placeholder).into(profilePic);



                    if (object.getString("WHAT_LISTENING").trim().isEmpty()) {
                       /* PushDownAnim.setPushDownAnimTo(profilePic).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(other_speaker_account.this, zoom_image_viewer.class);
                                intent.putExtra("file_photo_url", object.getParseFile("speaker_profile_pic").getUrl());
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_from_end, R.anim.stationary);
                            }
                        });*/
                        whatListneningCircular.setVisibility(View.GONE);
                        whatListneningCircular.setBorderWidth(0);

                    } else {

                        whatListneningCircular.setVisibility(View.VISIBLE);
                        ParseQuery<ParseObject> pp = new ParseQuery<ParseObject>("live_tables");
                        pp.getInBackground(object.getString("WHAT_LISTENING"), new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject object, ParseException e) {

                                if(e==null){
                                    Glide.with(other_speaker_account.this.getApplicationContext()).asBitmap().load(object.getParseFile("table_image").getUrl()).circleCrop()
                                            .into(whatListneningCircular);

                                    //Generating Pallete Colors
                                    Glide.with(other_speaker_account.this.getApplicationContext()).asBitmap()
                                            .load(object.getParseFile("table_image").getUrl())
                                            .into(new SimpleTarget<Bitmap>() {
                                                @Override
                                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                                                    Palette.generateAsync(resource, new Palette.PaletteAsyncListener() {
                                                        @Override
                                                        public void onGenerated(@Nullable Palette palette) {

                                                            Palette.Swatch swatch = palette.getLightVibrantSwatch() != null ? palette.getLightVibrantSwatch() : palette.getDominantSwatch();
                                                            profilePic.setBorderWidth(4);
                                                            profilePic.setBorderColor(swatch.getRgb());

                                                        }
                                                    });
                                                }
                                            });
                                    PushDownAnim.setPushDownAnimTo(profilePic).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if (object.getBoolean("Active")) {

                                                if (object.getList("co_creator").contains(ParseUser.getCurrentUser().getObjectId()) ||
                                                        object.getString("table_creator").equals(ParseUser.getCurrentUser().getObjectId()) ||
                                                        object.getList("table_subscribers").contains(ParseUser.getCurrentUser().getObjectId())) {

                                                    Intent intent = new Intent(other_speaker_account.this, special_speaker_live_table.class);
                                                    intent.putExtra("ID", object.getObjectId());
                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.slide_from_end, R.anim.stationary);

                                                } else {

                                                    if (!object.getList("BANS").contains(ParseUser.getCurrentUser().getObjectId())) {
                                                        Intent intent = new Intent(other_speaker_account.this, sound_preview.class);
                                                        intent.putExtra("ID", object.getObjectId());
                                                        startActivity(intent);
                                                        overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
                                                    } else {


                                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.activity, R.style.AlertDialogCustom);
                                                        builder1.setMessage(resources.getString(R.string.staff_ban_you));
                                                        builder1.setTitle(resources.getString(R.string.Banned));
                                                        builder1.setCancelable(false);

                                                        builder1.setPositiveButton(
                                                                resources.getString(R.string.OK),
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                        dialog.dismiss();
                                                                    }
                                                                });


                                                        AlertDialog alert11 = builder1.create();
                                                        alert11.show();

                                                    }


                                                }

                                            } else {

                                                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.activity, R.style.AlertDialogCustom);
                                                builder1.setMessage(resources.getString(R.string.SORRY_CREATOR_DISMISSED));
                                                builder1.setTitle(resources.getString(R.string.dismiss_table));
                                                builder1.setCancelable(false);

                                                builder1.setPositiveButton(
                                                        resources.getString(R.string.OK),
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.dismiss();
                                                            }
                                                        });


                                                AlertDialog alert11 = builder1.create();
                                                alert11.show();
                                            }


                                        }
                                    });
                                }
                                else {
                                    whatListneningCircular.setVisibility(View.GONE);
                                    whatListneningCircular.setBorderWidth(0);
                                }

                            }
                        });

                    }

                    ParseQuery<ParseObject> parseObjectParseQuerytt = new ParseQuery<ParseObject>("roundtable_user_data");
                    parseObjectParseQuerytt.whereEqualTo("User_ID", PASSED_ID);
                    parseObjectParseQuerytt.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {


                            for (ParseObject parseObject : objects) {


                                List<String> followers_list = parseObject.getList("who_notify");

                                if (followers_list.contains(ParseUser.getCurrentUser().getObjectId())) {

                                    speakerFollowButton.setTextColor(getResources().getColor(R.color.themetextcolor));
                                    speakerFollowButton.setText(resources.getString(R.string.un_notify));
                                    speakerFollowButton.setIconTint(ColorStateList.valueOf(getResources().getColor(R.color.themetextcolor)));
                                    speakerFollowButton.setIcon(getResources().getDrawable(R.drawable.notification_off));
                                    speakerFollowButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.card_view_color)));


                                } else {

                                    speakerFollowButton.setText(resources.getString(R.string.Notify));
                                    speakerFollowButton.setIcon(getResources().getDrawable(R.drawable.notification_on));
                                    speakerFollowButton.setIconTint(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                                    speakerFollowButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2633634D")));
                                    speakerFollowButton.setTextColor(getResources().getColor(R.color.colorAccent));


                                }
                                shimmerFrameLayout.setVisibility(View.GONE);
                                to_show.setVisibility(View.VISIBLE);

                            }


                        }
                    });

                    getting_notify_size();


                }
            });

            PushDownAnim.setPushDownAnimTo(back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    finish();
                    overridePendingTransition(R.anim.stationary, R.anim.bottom_down);

                }
            });
            PushDownAnim.setPushDownAnimTo(notify_linear).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(other_speaker_account.this, following_and_follower_activity.class);
                    intent.putExtra("ID", PASSED_ID);
                    startActivity(intent);
                    overridePendingTransition(R.anim.bottom_up, R.anim.stationary);

                }
            });

            PushDownAnim.setPushDownAnimTo(moreAboutUser).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    RoundedBottomSheetDialog roundedBottomSheetDialog = new RoundedBottomSheetDialog(other_speaker_account.this);
                    View view = LayoutInflater.from(other_speaker_account.this).inflate(R.layout.other_user_action_bottomsheet_layout, null);
                    roundedBottomSheetDialog.setContentView(view);


                    MaterialButton block = roundedBottomSheetDialog.findViewById(R.id.block_user);
                    MaterialButton create_account=roundedBottomSheetDialog.findViewById(R.id.create_account);
                    MaterialButton report = roundedBottomSheetDialog.findViewById(R.id.report_user);
                    MaterialButton creator_of = roundedBottomSheetDialog.findViewById(R.id.creator_of);
                    MaterialButton mutual = roundedBottomSheetDialog.findViewById(R.id.mutual);


                    block.setText(resources.getString(R.string.block_user));
                    create_account.setText(resources.getString(R.string.Done));
                    report.setText(resources.getString(R.string.report_user));
                    mutual.setText(resources.getString(R.string.mutual_tables));
                    creator_of.setText(resources.getString(R.string.Moderator_of));


                    report.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(other_speaker_account.this, report_account.class);
                            intent.putExtra("username", roundtableUsername.getText().toString());
                            intent.setType("ACCOUNT");
                            startActivity(intent);
                            overridePendingTransition(R.anim.alerter_slide_in_from_bottom, R.anim.stationary);

                        }
                    });
                    create_account.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            roundedBottomSheetDialog.dismiss();

                        }
                    });

                    block.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            AlertDialog.Builder builder = new AlertDialog.Builder(other_speaker_account.this);
                            View view1 = LayoutInflater.from(other_speaker_account.this).inflate(R.layout.block_alert_dialog, null, false);
                            builder.setView(view1);

                            AlertDialog alertDialog = builder.create();
                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                            alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                            alertDialog.show();

                            TextView deactivate_textview = alertDialog.findViewById(R.id.deactivate_textview);
                            TextView d_message = alertDialog.findViewById(R.id.deactivate_message_textview);

                            MaterialButton d = alertDialog.findViewById(R.id.deactivate);
                            MaterialButton n = alertDialog.findViewById(R.id.no);

                            deactivate_textview.setText(resources.getString(R.string.block_user) + " " + roundtableUsername.getText().toString());
                            d_message.setText(resources.getString(R.string.block_conformation));


                            d.setText(resources.getString(R.string.block_user));

                            PushDownAnim.setPushDownAnimTo(d).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("roundtable_user_data");
                                    parseObjectParseQuery.whereEqualTo("User_ID", ParseUser.getCurrentUser().getObjectId());
                                    parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> objects, ParseException e) {


                                            for (ParseObject parseObject : objects) {
                                                List<String> followers_list = parseObject.getList("Blocked");

                                                if (!followers_list.contains(PASSED_ID)) {

                                                    followers_list.add(PASSED_ID);

                                                    parseObject.put("Blocked", followers_list);

                                                    parseObject.saveInBackground(new SaveCallback() {
                                                        @Override
                                                        public void done(ParseException e) {

                                                            if (e == null) {

                                                                alertDialog.dismiss();
                                                                roundedBottomSheetDialog.dismiss();
                                                                Toast.makeText(other_speaker_account.this, resources.getString(R.string.speaker_blocked_succesfully), Toast.LENGTH_SHORT).show();
                                                                finish();
                                                                overridePendingTransition(R.anim.stationary, R.anim.alerter_slide_out_to_bottom);

                                                            } else {


                                                                Toast.makeText(other_speaker_account.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                                            }


                                                        }
                                                    });


                                                }


                                            }

                                        }
                                    });


                                }
                            });

                            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                            layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
                            layoutParams.width = 500;
                            alertDialog.getWindow().setAttributes(layoutParams);


                            n.setText(resources.getString(R.string.NO));

                            PushDownAnim.setPushDownAnimTo(n).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    alertDialog.dismiss();

                                }
                            });


                        }
                    });

                    PushDownAnim.setPushDownAnimTo(mutual).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(other_speaker_account.this, mutual_tables_activity.class);
                            intent.putExtra("ID", PASSED_ID);
                            intent.setType("MUTUAL");
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_from_end, R.anim.stationary);

                        }
                    });

                    PushDownAnim.setPushDownAnimTo(creator_of).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(other_speaker_account.this, creator_of_activity.class);
                            intent.putExtra("ID", PASSED_ID);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_from_end, R.anim.stationary);
                        }
                    });

                    roundedBottomSheetDialog.show();

                }
            });
            PushDownAnim.setPushDownAnimTo(speakerFollowButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("roundtable_user_data");
                    parseObjectParseQuery.whereEqualTo("User_ID", PASSED_ID);
                    parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {


                            for (ParseObject parseObject : objects) {
                                List<String> NOTIF = parseObject.getList("who_notify");
                                if (NOTIF.contains(ParseUser.getCurrentUser().getObjectId())) {
                                    NOTIF.remove(ParseUser.getCurrentUser().getObjectId());
                                    parseObject.put("who_notify", NOTIF);
                                    parseObject.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {

                                            speakerFollowButton.setText(resources.getString(R.string.Notify));
                                            speakerFollowButton.setIcon(getResources().getDrawable(R.drawable.notification_on));
                                            speakerFollowButton.setIconTint(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                                            speakerFollowButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2633634D")));
                                            speakerFollowButton.setTextColor(getResources().getColor(R.color.colorAccent));
                                            getting_notify_size();

                                        }
                                    });


                                } else {


                                    ParseQuery<ParseInstallation> parseInstallationParseQuery = ParseInstallation.getQuery();
                                    parseInstallationParseQuery.whereEqualTo("UserId", ParseUser.getCurrentUser().getObjectId());
                                    JSONObject data = new JSONObject();
                                    try {
                                        data.put("alert", ParseUser.getCurrentUser().getString("First_and_last_name") + " Liked Your's Conversations and Started Notifying You.");
                                        data.put("title", ParseUser.getCurrentUser().getString("First_and_last_name"));
                                        data.put("USER_ID", parseObject.getObjectId());
                                        data.put("Type", Constant.SOME_ONE_FOLLOW);
                                    } catch (JSONException ef) {

                                        throw new IllegalArgumentException("unexpected parsing error", ef);
                                    }
                                    ParsePush push = new ParsePush();
                                    push.setChannel("News");
                                    push.setData(data);
                                    push.setQuery(parseInstallationParseQuery);
                                    push.sendInBackground();

                                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
                                    } else {
                                        vibrator.vibrate(300);
                                    }

                                    speakerFollowButton.setText(resources.getString(R.string.un_notify));

                                    NOTIF.add(ParseUser.getCurrentUser().getObjectId());
                                    parseObject.put("who_notify", NOTIF);
                                    parseObject.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {


                                            speakerFollowButton.setTextColor(getResources().getColor(R.color.themetextcolor));
                                            speakerFollowButton.setText(resources.getString(R.string.un_notify));
                                            speakerFollowButton.setIconTint(ColorStateList.valueOf(getResources().getColor(R.color.themetextcolor)));
                                            speakerFollowButton.setIcon(getResources().getDrawable(R.drawable.notification_off));
                                            speakerFollowButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.card_view_color)));

                                            getting_notify_size();

                                        }
                                    });


                                }
                            }

                        }
                    });
                }
            });


        } catch (Exception fe) {

            fe.printStackTrace();
        }


    }

    public void getting_notify_size() {


        ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("roundtable_user_data");
        parseObjectParseQuery.whereEqualTo("User_ID", PASSED_ID);
        parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {


                for (ParseObject parseObject : objects) {
                    List<String> followers_list = parseObject.getList("who_notify");
                    how_notify.setText(Integer.toString(followers_list.size())+" "+resources.getString(R.string.Notified));


                }

            }
        });


    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.bottom_down);
    }

}
