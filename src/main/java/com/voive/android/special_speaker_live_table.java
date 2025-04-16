package com.voive.android;

import static com.voive.android.App.PLAYBACK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvale.switcher.SwitcherX;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog;
import com.discord.panels.OverlappingPanelsLayout;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;
import com.tapadoo.alerter.Alerter;
import com.thekhaeng.pushdownanim.PushDownAnim;
import com.voive.android.media.RtcTokenBuilder;
import com.xw.repo.widget.BounceScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import de.hdodenhof.circleimageview.CircleImageView;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import kotlin.Unit;

public class special_speaker_live_table extends AppCompatActivity {


    static final int MIN_DISTANCE = 150;
    @SuppressLint("NonConstantResourceId")
    public static Activity activity;
    public static RtcEngine mRtcEngine;
    public static boolean is_joined = true;
    public static String WHICH_SECTION = "";
    public static OverlappingPanelsLayout overlappingPanelsLayout;
    public static ParseLiveQueryClient parseLiveQueryClient = null;
    static int uid = 0;
    static int expirationTimeInSeconds = 3600;

    static {
        System.loadLibrary("api-keys");
    }

    BroadcastReceiver broadcastReceiver;
    @BindView(R.id.image)
    CircleImageView table_image_view;
    @BindView(R.id.textView15)
    TextView table_title;
    @BindView(R.id.description)
    TextView table_description;
    @BindView(R.id.subs_text)
    TextView subs_text;
    @BindView(R.id.creator_recyler)
    RecyclerView creator_recyler;
    @BindView(R.id.main_surface)
    ConstraintLayout mainSurface;
    @BindView(R.id.back5)
    MaterialButton back5;
    @BindView(R.id.how_many_online)
    TextView howManyOnline;
    @BindView(R.id.textView35)
    TextView textView35;
    @BindView(R.id.all_member_recyler)
    RecyclerView allMemberRecyler;
    @BindView(R.id.bounceScrollView5)
    BounceScrollView bounceScrollView5;
    @BindView(R.id.invite_people)
    MaterialButton invitePeople;
    @BindView(R.id.subscriber_mic)
    MaterialButton subscriberMic;
    @BindView(R.id.subscriber_leave_table)
    MaterialButton subscriberLeaveTable;
    @BindView(R.id.if_speaker_container)
    ConstraintLayout ifSpeakerContainer;
    @BindView(R.id.play_pause_button6)
    MaterialButton playPauseButton6;
    @BindView(R.id.table_not)
    ImageView tableNot;
    @BindView(R.id.live_indicator)
    MaterialButton live_indicator;
    @BindView(R.id.linearLayout2)
    LinearLayout sub_linear;
    @BindView(R.id.how_many_speaking)
    TextView how_many_speaking;
    @BindView(R.id.overlapping_panels)
    OverlappingPanelsLayout overlappingPanels;
    @BindView(R.id.whole_back_ground)
    ConstraintLayout wholeBackGround;
    all_member_creator__special_speaker_adapter all_member_creator__special_speaker_adapter;
    IRtcEngineEventHandler mRtcEventHandler;
    String GLOBAL_TABLE_ID;
    Resources resources;
    @BindView(R.id.main_shimmer)
    ShimmerFrameLayout main_panel_shimmer;
    @BindView(R.id.main_panel)
    ConstraintLayout main_panel_constrain;
    @BindView(R.id.edit_profile)
    MaterialButton notification_on_off;
    @BindView(R.id.click_set_conv)
    TextView click_set_conv;
    @BindView(R.id.shimer_linear)
    LinearLayout left_panel_shimmer_linear;
    @BindView(R.id.main)
    ConstraintLayout main_left_constrain_panel;
    @BindView(R.id.time_of_table)
    TextView time_of_table;
    @BindView(R.id.menu)
    MaterialButton menu;
    @BindView(R.id.if_title_empty_linear)
    LinearLayout if_title_empty;
    @BindView(R.id.if_no_one_speaking_layout)
    RelativeLayout if_no_one_speaking_layout;
    @BindView(R.id.no_one_speaking)
    TextView no_one_speaking;
    @BindView(R.id.TOO_QUITE_HERE)
    TextView TOO_QUITE_HERE;
    @BindView(R.id.first_time_swipe_right_constrain)
    ConstraintLayout first_time_swipe_right_constrain;
    @BindView(R.id.creator_textview)
    TextView creator_textview;
    @BindView(R.id.main_controller)
    LinearLayout main_controller;
    @BindView(R.id.table_cat)
    TextView table_cat;
    @BindView(R.id.moder_linear)
    LinearLayout moder_linear;
    @BindView(R.id.live)
    MaterialButton lv;
    @BindView(R.id.micro)
    MaterialButton micro;
    @BindView(R.id.tbl_nme)
    TextView tbl_nme;
    @BindView(R.id.table_clickable)
    LinearLayout table_clickable;
    String TABLE_NAME;
    boolean is_notification_sent = false;
    SimpleDateFormat dateFormat;
    int is_again;
    int VOLUME = 100;
    List<String> GLOBL_SPK = new ArrayList<>();
    private float x1, x2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_speaker_live_table);
        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng = sharedPreferences.getString("lng", "en");
        Context language_context = LocaleHelper.setLocale(this, lng);
        boolean FIRST_TIME = sharedPreferences.getBoolean("IS_FIRST_TABLE_DETAIL", true);
        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        is_again = getIntent().getIntExtra("IS_AGAIN", 0);
        if (is_again == 0) {
            leave_checK_pre();
        }

        resources = language_context.getResources();
        click_set_conv.setText(resources.getString(R.string.click_to_Set_conv));
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
        activity = this;

        Slidr.attach(this, config);

        // Initialize Audio Server IMPORTANT:)
        mRtcEventHandler = new IRtcEngineEventHandler() {
            @Override
            public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
                super.onJoinChannelSuccess(channel, uid, elapsed);

            }

            @Override
            public void onAudioRouteChanged(int routing) {
                super.onAudioRouteChanged(routing);

            }

            @Override
            public void onLeaveChannel(RtcStats stats) {
                super.onLeaveChannel(stats);
                leave_table();
            }

            @Override
            public void onUserJoined(int uid, int elapsed) {
                super.onUserJoined(uid, elapsed);
            }

            @Override
            public void onUserOffline(int uid, int reason) {
                super.onUserOffline(uid, reason);
            }

            @Override
            public void onAudioVolumeIndication(AudioVolumeInfo[] speakers, int totalVolume) {
                super.onAudioVolumeIndication(speakers, totalVolume);

                if (totalVolume > 5) {
                    ParseUser.getCurrentUser().put("SOUND", totalVolume);
                    ParseUser.getCurrentUser().saveInBackground();
                }


            }

        };
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        micro.setVisibility(audioManager.isWiredHeadsetOn() ? View.VISIBLE : View.GONE);

        audioManager.isWiredHeadsetOn();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String acc = intent.getAction();
                int iii;
                if (Intent.ACTION_HEADSET_PLUG.equals(acc)) {
                    iii = intent.getIntExtra("state", -1);
                    if (iii == 0) {
                        if (mRtcEngine != null) {
                            if (mRtcEngine != null) {
                                mRtcEngine.enableInEarMonitoring(false);
                            }

                            micro.setVisibility(View.GONE);
                        }
                    }
                    if (iii == 1) {
                        if (mRtcEngine != null) {
                            if (mRtcEngine != null) {
                                mRtcEngine.enableInEarMonitoring(false);
                                mRtcEngine.setInEarMonitoringVolume(100);
                            }
                            micro.setVisibility(View.VISIBLE);

                        }

                    }
                }
            }
        };

        IntentFilter receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(broadcastReceiver, receiverFilter);

        creator_textview.setText(resources.getString(R.string.creator));
        TOO_QUITE_HERE.setText(resources.getString(R.string.TOO_QUITE_HERE));
        overlappingPanelsLayout = findViewById(R.id.overlapping_panels);
        overlappingPanelsLayout.setEndPanelUseFullPortraitWidth(true);
        overlappingPanelsLayout.setEndPanelLockState(OverlappingPanelsLayout.LockState.CLOSE);

        GLOBAL_TABLE_ID = getIntent().getStringExtra("ID");
        ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("live_tables");
        parseObjectParseQuery.getInBackground(GLOBAL_TABLE_ID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {

                try {

                    Calendar calendar1, calendar2;
                    String string1 = object.getString("START_BACK");
                    Date time1 = new SimpleDateFormat("HH:mm:ss").parse(string1);

                    String string2 = object.getString("END_BACK");
                    Date time2 = new SimpleDateFormat("HH:mm:ss").parse(string2);

                    if (time1.getHours() > time2.getHours()) {
                        time1 = dateFormat.parse("02/26/2099 " + string1);
                        time2 = dateFormat.parse("02/27/2099 " + string2);

                        calendar1 = Calendar.getInstance();
                        calendar1.setTime(time1);
                        calendar1.add(Calendar.DATE, 1);

                        calendar2 = Calendar.getInstance();
                        calendar2.setTime(time2);
                        calendar2.add(Calendar.DATE, 1);
                    } else {

                        time1 = dateFormat.parse("02/27/2099 " + string1);
                        time2 = dateFormat.parse("02/27/2099 " + string2);

                        calendar1 = Calendar.getInstance();
                        calendar1.setTime(time1);
                        calendar1.add(Calendar.DATE, 1);

                        calendar2 = Calendar.getInstance();
                        calendar2.setTime(time2);
                        calendar2.add(Calendar.DATE, 1);

                    }


                    Date currentTime = Calendar.getInstance().getTime();
                    String someRandomTime = currentTime.getHours() + ":" + currentTime.getMinutes() + ":" + currentTime.getSeconds();
                    Calendar calendar3 = Calendar.getInstance();
                    if (time1.getHours() > currentTime.getHours() || time1.getDay() == time2.getDay()) {
                        Date d = dateFormat.parse("02/27/2099 " + someRandomTime);

                        calendar3.setTime(d);
                        calendar3.add(Calendar.DATE, 1);
                    } else {
                        Date d = dateFormat.parse("02/26/2099 " + someRandomTime);

                        calendar3.setTime(d);
                        calendar3.add(Calendar.DATE, 1);
                    }


                    Date x = calendar3.getTime();
                    if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {


                        try {
                            if (object.getString("table_creator").equals(ParseUser.getCurrentUser().getObjectId())) {

                                if (ParseUser.getCurrentUser().getParseGeoPoint("Location") != null) {

                                    object.put("Location", ParseUser.getCurrentUser().getParseGeoPoint("Location"));
                                    object.saveInBackground();

                                }

                            }
                        } catch (Exception fg) {
                            fg.printStackTrace();
                        }
                        if (!object.getBoolean("ALLOW_RECORDING")) {
                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                                    WindowManager.LayoutParams.FLAG_SECURE);
                        }
                        main_panel_shimmer.startShimmer();
                        textView35.setText(object.getString("TALKING_TITLE"));
                        if (!object.getList("table_speakers").contains(ParseUser.getCurrentUser().getObjectId())) {

                            subscriberMic.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.settingbuttonbackcoclor)));
                            subscriberMic.setIconTint(ColorStateList.valueOf(getResources().getColor(R.color.themetextsecondarycolor)));

                            subscriberMic.setIcon(getResources().getDrawable(R.drawable.mic_off));
                        } else {
                            subscriberMic.setIcon(getResources().getDrawable(R.drawable.mic_on));
                            subscriberMic.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.searchboxbackcolor)));
                            subscriberMic.setIconTint(ColorStateList.valueOf(getResources().getColor(R.color.allicontintcolor)));
                        }


                        GLOBL_SPK = object.getList("table_speakers");
                        ParseQuery<ParseUser> xcrz = ParseUser.getQuery();
                        xcrz.whereEqualTo("objectId", object.getString("table_creator"));
                        xcrz.findInBackground(new FindCallback<ParseUser>() {
                            @Override
                            public void done(List<ParseUser> objects, ParseException e) {

                                live_table_creators_adapter live_table_creators_adapter = new live_table_creators_adapter(objects, special_speaker_live_table.this, GLOBAL_TABLE_ID);
                                creator_recyler.setLayoutManager(new LinearLayoutManager(special_speaker_live_table.this, LinearLayoutManager.VERTICAL, false));
                                creator_recyler.setAdapter(live_table_creators_adapter);

                            }
                        });

                        if (is_again == 0) {
                            if (object.getList("table_speakers").contains(ParseUser.getCurrentUser().getObjectId())) {

                                List<String> sd = object.getList("table_speakers");
                                sd.remove(ParseUser.getCurrentUser().getObjectId());
                                object.put("table_speakers", sd);
                            }
                        }

                        ParseQuery<ParseUser> xcr = ParseUser.getQuery();
                        xcr.whereContainedIn("objectId", object.getList("table_speakers"));
                        xcr.findInBackground(new FindCallback<ParseUser>() {
                            @Override
                            public void done(List<ParseUser> objects, ParseException e) {


                                all_member_creator__special_speaker_adapter = new all_member_creator__special_speaker_adapter(objects, special_speaker_live_table.this, object.getString("creator"), GLOBAL_TABLE_ID, special_speaker_live_table.this);
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(special_speaker_live_table.this, 3);
                                allMemberRecyler.setLayoutManager(gridLayoutManager);
                                allMemberRecyler.setAdapter(all_member_creator__special_speaker_adapter);

                            }
                        });

                        App.IS_LISTENING = true;
                        App.WHICH_TABLE = GLOBAL_TABLE_ID;
                        List<String> strings = ParseUser.getCurrentUser().getList("LAST_VISITED");
                        if (strings == null) {
                            List<String> nn = new ArrayList<>();
                            ParseUser.getCurrentUser().put("LAST_VISITED", nn);
                            ParseUser.getCurrentUser().saveInBackground();
                        }
                        if (!strings.contains(GLOBAL_TABLE_ID)) {
                            strings.add(GLOBAL_TABLE_ID);
                            ParseUser.getCurrentUser().put("LAST_VISITED", strings);
                            ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {

                                }
                            });
                        }

                        ParseUser.getCurrentUser().put("WHAT_LISTENING", GLOBAL_TABLE_ID);
                        ParseUser.getCurrentUser().saveInBackground();

                        //Setting UP Left Panel
                        if (object.getList("category").size() > 0) {

                            List<String> mVals = object.getList("category");
                            table_cat.setText(mVals.get(0));


                        } else {
                            table_cat.setVisibility(View.GONE);
                        }


                        subs_text.setText(object.getList("table_subscribers").size() + " " + resources.getString(R.string.Subscribers));
                        time_of_table.setText(object.getString("start_time") + " To " + object.getString("end_time"));
                        List<String> ss = new ArrayList<>();
                        ss.add("News");
                        ss.add("Arts & Entertainment");
                        ss.add("Health & Beauty");
                        ss.add("Science & Tech");
                        ss.add("Politics");
                        ss.add("Business");
                        ss.add("Books & Information");

                        if (ss.contains(object.getString("table_name"))) {
                            if (object.getString("table_name").equals("News")) {
                                table_title.setText(resources.getString(R.string.News));
                                table_description.setText(resources.getString(R.string.AUTO_GENRATED_DES_NEWS));
                            } else if (object.getString("table_name").equals("Arts & Entertainment")) {
                                table_title.setText(resources.getString(R.string.arts_enter));
                                table_description.setText(resources.getString(R.string.AUTO_GENRATED_DES_ART));
                            } else if (object.getString("table_name").equals("Health & Beauty")) {
                                table_title.setText(resources.getString(R.string.health_beauty));
                                table_description.setText(resources.getString(R.string.AUTO_GENRATED_DES_HEALTH));
                            } else if (object.getString("table_name").equals("Science & Tech")) {
                                table_title.setText(resources.getString(R.string.sci_tech));
                                table_description.setText(resources.getString(R.string.AUTO_GENRATED_DES_SCI));
                            } else if (object.getString("table_name").equals("Politics")) {
                                table_title.setText(resources.getString(R.string.Politics));
                                table_description.setText(resources.getString(R.string.AUTO_GENRATED_DES_POLITICS));
                            } else if (object.getString("table_name").equals("Business")) {
                                table_title.setText(resources.getString(R.string.Buisness));
                                table_description.setText(resources.getString(R.string.AUTO_GENRATED_DES_BSN));
                            } else if (object.getString("table_name").equals("Books & Information")) {
                                table_title.setText(resources.getString(R.string.Books_Information));
                                table_description.setText(resources.getString(R.string.AUTO_GENRATED_DES_BOOK));
                            }
                            moder_linear.setVisibility(View.GONE);
                        } else {
                            table_title.setText(object.getString("table_name"));
                            moder_linear.setVisibility(View.VISIBLE);
                            table_description.setVisibility(object.getString("table_description").trim().isEmpty() ? View.GONE : View.VISIBLE);
                            table_description.setText(object.getString("table_description"));
                        }

                        if (!object.getBoolean("IS_SHOW")) {
                            moder_linear.setVisibility(View.GONE);
                        } else {
                            moder_linear.setVisibility(View.VISIBLE);
                        }


                        PushDownAnim.setPushDownAnimTo(table_clickable).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                overlappingPanels.openStartPanel();
                            }
                        });
                        tbl_nme.setText(object.getString("table_name"));
                        TABLE_NAME = object.getString("table_name");
                        Glide.with(special_speaker_live_table.this).asBitmap().load(object.getParseFile("table_image").getUrl())
                                .centerInside().into(table_image_view);

                        Glide.with(special_speaker_live_table.this).asBitmap().load(object.getParseFile("table_image").getUrl())
                                .centerInside().into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                                        Palette.generateAsync(resource, new Palette.PaletteAsyncListener() {
                                            @Override
                                            public void onGenerated(@Nullable Palette palette) {

                                                Palette.Swatch swatch = palette.getLightVibrantSwatch() != null
                                                        ? palette.getLightVibrantSwatch() : palette.getDominantSwatch();

                                                subs_text.setTextColor(swatch.getRgb());

                                                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(special_speaker_live_table.this);
                                                MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(special_speaker_live_table.this, "tag");


                                                Intent notificationIntent = new Intent(special_speaker_live_table.this, special_speaker_live_table.class);
                                                notificationIntent.putExtra("ID", GLOBAL_TABLE_ID);
                                                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                PendingIntent contentIntent = PendingIntent.getActivity(special_speaker_live_table.this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                                Notification notification = new NotificationCompat.Builder(special_speaker_live_table.this, PLAYBACK)
                                                        .setSmallIcon(R.drawable.app_main_v_logo)
                                                        .setContentTitle(object.getString("TALKING_TITLE"))
                                                        .setContentText(object.getString("table_name"))
                                                        .setTicker("LIVE")
                                                        .setContentIntent(contentIntent)
                                                        .setLargeIcon(resource)
                                                        .setShowWhen(false)
                                                        .setSubText("LIVE")
                                                        .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSessionCompat.getSessionToken()))
                                                        .setPriority(NotificationCompat.PRIORITY_MAX)
                                                        .setOngoing(true)
                                                        .setOnlyAlertOnce(true)
                                                        .build();

                                                notificationManagerCompat.notify(13, notification);

                                                left_panel_shimmer_linear.setVisibility(View.GONE);
                                                main_left_constrain_panel.setVisibility(View.VISIBLE);

                                                if (FIRST_TIME) {

                                                    first_time_swipe_right_constrain.setVisibility(View.VISIBLE);
                                                    first_time_swipe_right_constrain.setOnTouchListener(new View.OnTouchListener() {
                                                        @Override
                                                        public boolean onTouch(View v, MotionEvent event) {
                                                            first_time_swipe_right_constrain.setVisibility(View.GONE);
                                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                                            editor.putBoolean("IS_FIRST_TABLE_DETAIL", false);
                                                            editor.apply();
                                                            return false;
                                                                              }
                                                    });
                                                } else {

                                                    first_time_swipe_right_constrain.setVisibility(View.GONE);
                                                }
                                            }
                                        });

                                    }
                                });

                        notification_on_off.setIcon(object.getList("NOTIFY").contains(ParseUser.getCurrentUser().getObjectId()) ?
                                getResources().getDrawable(R.drawable.notification_on) :
                                getResources().getDrawable(R.drawable.notification_off));

                        how_many_speaking.setText(object.getList("table_speakers").size() + "/" + object.getInt("HOW_MANY_ALLOWED") + " " + resources.getString(R.string.SPEAKING));
                        List<String> tuned_ins = object.getList("ONLINE");
                        if (!tuned_ins.contains(ParseUser.getCurrentUser().getObjectId())) {

                            tuned_ins.add(ParseUser.getCurrentUser().getObjectId());
                            object.put("ONLINE", tuned_ins);
                            object.saveInBackground();

                        }
                        if (object.getList("table_speakers").size() == 0) {
                            if_no_one_speaking_layout.setVisibility(View.VISIBLE);
                        } else {
                            if_no_one_speaking_layout.setVisibility(View.GONE);
                        }
                        howManyOnline.setText(Integer.toString(object.getList("ONLINE").size()));

                        PushDownAnim.setPushDownAnimTo(notification_on_off).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (object.getList("NOTIFY").contains(ParseUser.getCurrentUser().getObjectId())) {

                                    List<String> NTF = object.getList("NOTIFY");
                                    NTF.remove(ParseUser.getCurrentUser().getObjectId());
                                    object.put("NOTIFY", NTF);
                                    object.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            Snackbar snackbar = Snackbar.make(v, object.getString("table_name") + " Muted", Snackbar.LENGTH_SHORT);
                                            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                                            snackbar.setDuration(6000);
                                            snackbar.setBackgroundTint(getResources().getColor(R.color.themetextcolor));
                                            snackbar.setTextColor(getResources().getColor(R.color.colorPrimary));
                                            snackbar.show();
                                            notification_on_off.setIcon(object.getList("NOTIFY").contains(ParseUser.getCurrentUser().getObjectId()) ?
                                                    getResources().getDrawable(R.drawable.notification_on) :
                                                    getResources().getDrawable(R.drawable.notification_off));
                                        }
                                    });
                                } else {

                                    List<String> NTF = object.getList("NOTIFY");
                                    NTF.add(ParseUser.getCurrentUser().getObjectId());
                                    object.put("NOTIFY", NTF);
                                    object.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            Snackbar snackbar = Snackbar.make(v, object.getString("table_name") + " Unmuted", Snackbar.LENGTH_SHORT);
                                            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                                            snackbar.setDuration(6000);
                                            snackbar.setBackgroundTint(getResources().getColor(R.color.themetextcolor));
                                            snackbar.setTextColor(getResources().getColor(R.color.colorPrimary));
                                            snackbar.show();
                                            notification_on_off.setIcon(object.getList("NOTIFY").contains(ParseUser.getCurrentUser().getObjectId()) ?
                                                    getResources().getDrawable(R.drawable.notification_on) :
                                                    getResources().getDrawable(R.drawable.notification_off));
                                        }
                                    });
                                }


                            }
                        });

                        if (object.getString("TALKING_TITLE").trim().isEmpty()) {
                            if (object.getList("co_creator").contains(ParseUser.getCurrentUser().getObjectId()) || object.getString("table_creator").equals(ParseUser.getCurrentUser().getObjectId())) {
                                if_title_empty.setVisibility(View.VISIBLE);
                                textView35.setVisibility(View.GONE);
                            } else {
                                if_title_empty.setVisibility(View.GONE);
                                textView35.setVisibility(View.GONE);
                            }

                        } else {
                            if_title_empty.setVisibility(View.GONE);
                            textView35.setVisibility(View.VISIBLE);
                        }

                        if (object.getList("co_creator").contains(ParseUser.getCurrentUser().getObjectId()) || object.getString("table_creator").equals(ParseUser.getCurrentUser().getObjectId())) {

                            PushDownAnim.setPushDownAnimTo(textView35).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    RoundedBottomSheetDialog roundedBottomSheetDialog = new RoundedBottomSheetDialog(special_speaker_live_table.this);
                                    roundedBottomSheetDialog.setContentView(R.layout.change_title_bottom_sheet_dialog);

                                    TextView textViewX = roundedBottomSheetDialog.findViewById(R.id.ban_text);
                                    EditText editText = roundedBottomSheetDialog.findViewById(R.id.table_descripton);
                                    MaterialButton set_title = roundedBottomSheetDialog.findViewById(R.id.banned);

                                    textViewX.setText(resources.getString(R.string.SPECEFIE_TITLE));
                                    editText.setHint(resources.getString(R.string.TLKING_TITLE));

                                    PushDownAnim.setPushDownAnimTo(set_title).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {


                                            if (!editText.getText().toString().isEmpty()) {

                                                object.put("TALKING_TITLE", editText.getText().toString());
                                                object.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {


                                                        List<String> SD = object.getList("NOTIFY");
                                                        for (String IDS : SD) {

                                                            ParseQuery<ParseInstallation> parseInstallationParseQuery = ParseInstallation.getQuery();
                                                            parseInstallationParseQuery.whereEqualTo("UserId", IDS);
                                                            parseInstallationParseQuery.findInBackground(new FindCallback<ParseInstallation>() {
                                                                @Override
                                                                public void done(List<ParseInstallation> objects, ParseException e) {


                                                                    JSONObject data = new JSONObject();
// Put data in the JSON object
                                                                    try {
                                                                        data.put("alert", "Conversation Title Changed To : " + object.getString("talking_title") + " Tap To Listen.");
                                                                        data.put("title", object.getString("table_name"));
                                                                        data.put("Id", GLOBAL_TABLE_ID);
                                                                        data.put("TYPE", Constant.TABLE_OTHER_NOTIFICATION);
                                                                    } catch (JSONException te) {
                                                                        throw new IllegalArgumentException("unexpected parsing error", te);
                                                                    }
// Configure the push
                                                                    ParsePush push = new ParsePush();
                                                                    push.setQuery(parseInstallationParseQuery);
                                                                    push.setData(data);
                                                                    push.sendInBackground();
                                                                }
                                                            });
                                                        }

                                                        textView35.setVisibility(View.VISIBLE);
                                                        if_title_empty.setVisibility(View.GONE);

                                                        textView35.setText(object.getString("TALKING_TITLE"));
                                                        roundedBottomSheetDialog.dismiss();

                                                        Toast.makeText(special_speaker_live_table.this, "👍👍", Toast.LENGTH_SHORT).show();


                                                    }
                                                });

                                            } else {


                                                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                                                    vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));

                                                } else {

                                                    vibrator.vibrate(300);
                                                }


                                            }

                                        }
                                    });


                                    roundedBottomSheetDialog.show();


                                }
                            });
                            PushDownAnim.setPushDownAnimTo(if_title_empty).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    RoundedBottomSheetDialog roundedBottomSheetDialog = new RoundedBottomSheetDialog(special_speaker_live_table.this);
                                    roundedBottomSheetDialog.setContentView(R.layout.change_title_bottom_sheet_dialog);

                                    TextView textViewX = roundedBottomSheetDialog.findViewById(R.id.ban_text);
                                    EditText editText = roundedBottomSheetDialog.findViewById(R.id.table_descripton);
                                    MaterialButton set_title = roundedBottomSheetDialog.findViewById(R.id.banned);

                                    textViewX.setText(resources.getString(R.string.SPECEFIE_TITLE));
                                    editText.setHint(resources.getString(R.string.TLKING_TITLE));

                                    PushDownAnim.setPushDownAnimTo(set_title).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {


                                            if (!editText.getText().toString().isEmpty()) {

                                                object.put("TALKING_TITLE", editText.getText().toString());
                                                object.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {


                                                        List<String> SD = object.getList("NOTIFY");
                                                        for (String IDS : SD) {

                                                            ParseQuery<ParseInstallation> parseInstallationParseQuery = ParseInstallation.getQuery();
                                                            parseInstallationParseQuery.whereEqualTo("UserId", IDS);
                                                            parseInstallationParseQuery.findInBackground(new FindCallback<ParseInstallation>() {
                                                                @Override
                                                                public void done(List<ParseInstallation> objects, ParseException e) {


                                                                    JSONObject data = new JSONObject();
                                                                    try {
                                                                        data.put("alert", "Conversation Title Changed To : " + object.getString("talking_title") + " Tap Here To Listen Conversation.");
                                                                        data.put("title", object.getString("table_name"));
                                                                        data.put("Id", GLOBAL_TABLE_ID);
                                                                        data.put("TYPE", Constant.TABLE_OTHER_NOTIFICATION);
                                                                    } catch (JSONException te) {
                                                                        throw new IllegalArgumentException("unexpected parsing error", te);
                                                                    }
//
                                                                    ParsePush push = new ParsePush();
                                                                    push.setQuery(parseInstallationParseQuery);
                                                                    push.setData(data);
                                                                    push.sendInBackground();
                                                                }
                                                            });
                                                        }
                                                        textView35.setVisibility(View.VISIBLE);
                                                        if_title_empty.setVisibility(View.GONE);
                                                        textView35.setText(object.getString("TALKING_TITLE"));
                                                        roundedBottomSheetDialog.dismiss();

                                                        Toast.makeText(special_speaker_live_table.this, "👍👍", Toast.LENGTH_SHORT).show();


                                                    }
                                                });

                                            } else {


                                                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                                                    vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));

                                                } else {

                                                    vibrator.vibrate(300);
                                                }


                                            }

                                        }
                                    });


                                    roundedBottomSheetDialog.show();


                                }
                            });

                        }


                        PushDownAnim.setPushDownAnimTo(back5).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                finish();
                                overridePendingTransition(R.anim.stationary, R.anim.bottom_down);
                            }
                        });

                        PushDownAnim.setPushDownAnimTo(menu).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (object.getList("co_creator").contains(ParseUser.getCurrentUser().getObjectId()) ||
                                        object.getString("table_creator").equals(ParseUser.getCurrentUser().getObjectId())) {

                                    RoundedBottomSheetDialog roundedBottomSheetDialog = new RoundedBottomSheetDialog(special_speaker_live_table.this);
                                    View vvv = LayoutInflater.from(special_speaker_live_table.this).inflate(R.layout.if_my_table_bottom_sheet_dialog, null);
                                    roundedBottomSheetDialog.setContentView(vvv);


                                    MaterialButton invite, pause, setting, dismiss, create_account;
                                    SwitcherX switchButton;
                                    SeekBar seekbar;
                                    TextView how_integer;

                                    invite = vvv.findViewById(R.id.invite_people);
                                    pause = vvv.findViewById(R.id.pause_notification);
                                    setting = vvv.findViewById(R.id.setting);
                                    seekbar = roundedBottomSheetDialog.findViewById(R.id.seekbar);
                                    create_account = vvv.findViewById(R.id.create_account);
                                    how_integer = roundedBottomSheetDialog.findViewById(R.id.how_integer);
                                    dismiss = vvv.findViewById(R.id.dismiss_table);
                                    switchButton = vvv.findViewById(R.id.switch1);

                                    invite.setText(resources.getString(R.string.invite_peoples));
                                    pause.setText(resources.getString(R.string.Notification));
                                    create_account.setText(resources.getString(R.string.Done));
                                    setting.setText(resources.getString(R.string.Setting));
                                    dismiss.setText(resources.getString(R.string.dismiss_table));

                                    how_integer.setText(Integer.toString(VOLUME));
                                    seekbar.setProgress(VOLUME);

                                    seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                        @Override
                                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                            how_integer.setText(Integer.toString(progress));
                                            VOLUME = progress;
                                        }

                                        @Override
                                        public void onStartTrackingTouch(SeekBar seekBar) {

                                        }

                                        @Override
                                        public void onStopTrackingTouch(SeekBar seekBar) {

                                        }
                                    });

                                    PushDownAnim.setPushDownAnimTo(create_account).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if (mRtcEngine != null) {
                                                mRtcEngine.adjustPlaybackSignalVolume(VOLUME);
                                            }
                                            roundedBottomSheetDialog.dismiss();
                                        }
                                    });

                                    PushDownAnim.setPushDownAnimTo(setting).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            Intent intent = new Intent(special_speaker_live_table.this, table_setting_page.class);
                                            intent.putExtra("ID", object.getObjectId());
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.bottom_up, R.anim.stationary);

                                        }
                                    });

                                    PushDownAnim.setPushDownAnimTo(invite).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            Intent intent = new Intent(special_speaker_live_table.this, invite_people_activity.class);
                                            intent.putExtra("ID", object.getObjectId());
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.bottom_up, R.anim.stationary);

                                        }
                                    });


                                    PushDownAnim.setPushDownAnimTo(dismiss).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {


                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(special_speaker_live_table.this, R.style.AlertDialogCustom);
                                            builder1.setMessage(resources.getString(R.string.sure_to_dismmiss_table));
                                            builder1.setTitle(resources.getString(R.string.dismiss_table));
                                            builder1.setCancelable(false);

                                            builder1.setPositiveButton(
                                                    resources.getString(R.string.YES),
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            ACProgressFlower dialogp = new ACProgressFlower.Builder(special_speaker_live_table.this)
                                                                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                                                                    .themeColor(Color.WHITE)
                                                                    .bgColor(R.color.card_view_color)
                                                                    .fadeColor(R.color.card_view_color)
                                                                    .bgAlpha(0)
                                                                    .petalThickness(6)
                                                                    .petalCount(20)
                                                                    .speed(25)
                                                                    .fadeColor(Color.DKGRAY).build();
                                                            dialogp.show();
                                                            object.put("Active", false);
                                                            object.saveInBackground(new SaveCallback() {
                                                                @Override
                                                                public void done(ParseException e) {

                                                                    if (e == null) {

                                                                        ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("roundtable_user_data");
                                                                        parseObjectParseQuery.whereEqualTo("User_ID", ParseUser.getCurrentUser().getObjectId());
                                                                        parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                                                                            @Override
                                                                            public void done(List<ParseObject> objects, ParseException e) {

                                                                                for (ParseObject parseObject : objects) {

                                                                                    List<String> ss = parseObject.getList("Subscriptions");
                                                                                    ss.remove(object.getObjectId());
                                                                                    parseObject.put("Subscriptions", ss);
                                                                                    parseObject.saveInBackground();

                                                                                }

                                                                            }
                                                                        });

                                                                        try {


                                                                            ParseQuery<ParseObject> user_data_live = new ParseQuery<ParseObject>("roundtable_user_data");
                                                                            user_data_live.whereEqualTo("User_ID", ParseUser.getCurrentUser().getObjectId());
                                                                            user_data_live.findInBackground(new FindCallback<ParseObject>() {
                                                                                @Override
                                                                                public void done(List<ParseObject> objectsX, ParseException e) {

                                                                                    List<String> sub = objectsX.get(0).getList("Subscriptions");
                                                                                    sub.remove(object.getObjectId());

                                                                                    objectsX.get(0).put("Subscriptions", sub);
                                                                                    objectsX.get(0).saveInBackground(new SaveCallback() {
                                                                                        @Override
                                                                                        public void done(ParseException e) {
                                                                                            dialogp.dismiss();
                                                                                            leave_table();
                                                                                            dialog.cancel();
                                                                                            roundedBottomSheetDialog.dismiss();
                                                                                            Toast.makeText(special_speaker_live_table.this, "Table Dismissed", Toast.LENGTH_SHORT).show();

                                                                                        }
                                                                                    });

                                                                                }
                                                                            });


                                                                        } catch (Exception fd) {

                                                                            fd.printStackTrace();
                                                                        }

                                                                    } else {

                                                                        Toast.makeText(special_speaker_live_table.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                                                    }

                                                                }
                                                            });

                                                        }
                                                    });

                                            builder1.setNegativeButton(
                                                    resources.getString(R.string.NO),
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                        }
                                                    });

                                            AlertDialog alert11 = builder1.create();
                                            alert11.show();


                                        }
                                    });
                                    switchButton.setChecked(object.getList("NOTIFY").contains(ParseUser.getCurrentUser().getObjectId()), true);
                                    switchButton.setOnCheckedChangeListener(checked ->
                                    {
                                        switchButton.setChecked(checked, true);

                                        if (checked) {
                                            List<String> ss = object.getList("NOTIFY");
                                            ss.add(ParseUser.getCurrentUser().getObjectId());

                                            object.put("NOTIFY", ss);
                                            object.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {

                                                    if (e == null) {

                                                        roundedBottomSheetDialog.dismiss();
                                                        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                                                            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                                        } else {

                                                            vibrator.vibrate(500);
                                                        }
                                                        Snackbar snackbar = Snackbar.make(v, "🔔 Notification On", Snackbar.LENGTH_SHORT);
                                                        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                                                        snackbar.setDuration(4000);
                                                        snackbar.setBackgroundTint(resources.getColor(R.color.themetextcolor));
                                                        snackbar.setTextColor(resources.getColor(R.color.colorPrimary));
                                                        snackbar.show();
                                                    }


                                                }
                                            });


                                        } else {

                                            if (object.getList("NOTIFY").contains(ParseUser.getCurrentUser().getObjectId())) {

                                                List<String> ss = object.getList("NOTIFY");
                                                ss.remove(ParseUser.getCurrentUser().getObjectId());

                                                object.put("NOTIFY", ss);
                                                object.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {

                                                        if (e == null) {

                                                            roundedBottomSheetDialog.dismiss();
                                                            roundedBottomSheetDialog.dismiss();
                                                            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

                                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                                                                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                                            } else {

                                                                vibrator.vibrate(500);
                                                            }
                                                            Snackbar snackbar = Snackbar.make(v, "🔔 Notification Off", Snackbar.LENGTH_SHORT);
                                                            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                                                            snackbar.setDuration(4000);
                                                            snackbar.setBackgroundTint(getResources().getColor(R.color.themetextcolor));
                                                            snackbar.setTextColor(getResources().getColor(R.color.colorPrimary));
                                                            snackbar.show();
                                                        }


                                                    }
                                                });

                                            }

                                        }

                                        return Unit.INSTANCE;
                                    });
                                    roundedBottomSheetDialog.show();


                                } else {

                                    RoundedBottomSheetDialog roundedBottomSheetDialog = new RoundedBottomSheetDialog(special_speaker_live_table.this);
                                    roundedBottomSheetDialog.setContentView(R.layout.if_table_is_not_mine);

                                    MaterialButton invite, pause, dismiss, report, create_account;
                                    SwitcherX switchButton;
                                    TextView how_integer;
                                    SeekBar seekbar;

                                    invite = roundedBottomSheetDialog.findViewById(R.id.invite_people);
                                    pause = roundedBottomSheetDialog.findViewById(R.id.pause_notification);
                                    dismiss = roundedBottomSheetDialog.findViewById(R.id.leave_table);
                                    how_integer = roundedBottomSheetDialog.findViewById(R.id.how_integer);
                                    switchButton = roundedBottomSheetDialog.findViewById(R.id.switch1);
                                    seekbar = roundedBottomSheetDialog.findViewById(R.id.seekbar);
                                    create_account = roundedBottomSheetDialog.findViewById(R.id.create_account);
                                    report = roundedBottomSheetDialog.findViewById(R.id.report);

                                    invite.setText(resources.getString(R.string.invite_peoples));
                                    pause.setText(resources.getString(R.string.Notification));
                                    report.setText(resources.getString(R.string.report_table));
                                    create_account.setText(resources.getString(R.string.Done));
                                    dismiss.setText(resources.getString(R.string.leave_table));

                                    how_integer.setText(Integer.toString(VOLUME));
                                    seekbar.setProgress(VOLUME);

                                    seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                        @Override
                                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                            how_integer.setText(Integer.toString(progress));
                                            VOLUME = progress;
                                        }

                                        @Override
                                        public void onStartTrackingTouch(SeekBar seekBar) {

                                        }

                                        @Override
                                        public void onStopTrackingTouch(SeekBar seekBar) {

                                        }
                                    });

                                    PushDownAnim.setPushDownAnimTo(create_account).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if (mRtcEngine != null) {
                                                mRtcEngine.adjustPlaybackSignalVolume(VOLUME);
                                            }
                                            roundedBottomSheetDialog.dismiss();
                                        }
                                    });

                                    PushDownAnim.setPushDownAnimTo(dismiss).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(special_speaker_live_table.this, R.style.AlertDialogCustom);
                                            builder1.setMessage(resources.getString(R.string.sure_to_leave_tavle));
                                            builder1.setTitle(resources.getString(R.string.leave_table));
                                            builder1.setCancelable(false);

                                            builder1.setPositiveButton(
                                                    resources.getString(R.string.YES),
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {

                                                            ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("roundtable_user_data");
                                                            parseObjectParseQuery.whereEqualTo("User_ID", ParseUser.getCurrentUser().getObjectId());
                                                            parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                                                                @Override
                                                                public void done(List<ParseObject> objects, ParseException e) {

                                                                    for (ParseObject parseObject : objects) {

                                                                        List<String> ss = parseObject.getList("Subscriptions");
                                                                        ss.remove(object.getObjectId());
                                                                        parseObject.put("Subscriptions", ss);
                                                                        parseObject.saveInBackground();

                                                                    }

                                                                }
                                                            });

                                                            List<String> subscriber = object.getList("table_subscribers");
                                                            subscriber.remove(ParseUser.getCurrentUser().getObjectId());
                                                            object.put("table_subscribers", subscriber);


                                                            List<String> notifyr = object.getList("NOTIFY");
                                                            notifyr.remove(ParseUser.getCurrentUser().getObjectId());
                                                            object.put("NOTIFY", notifyr);


                                                            List<String> speakers = object.getList("table_speakers");
                                                            speakers.remove(ParseUser.getCurrentUser().getObjectId());
                                                            object.put("table_speakers", speakers);


                                                            List<String> raise_hand = object.getList("ONLINE");
                                                            raise_hand.remove(ParseUser.getCurrentUser().getObjectId());
                                                            object.put("ONLINE", raise_hand);

                                                            object.saveInBackground(new SaveCallback() {
                                                                @Override
                                                                public void done(ParseException e) {
                                                                    leave_table();
                                                                    Toast.makeText(special_speaker_live_table.this, "Leaved", Toast.LENGTH_SHORT).show();

                                                                }
                                                            });

                                                            dialog.cancel();
                                                        }
                                                    });

                                            builder1.setNegativeButton(
                                                    resources.getString(R.string.NO),
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                        }
                                                    });

                                            AlertDialog alert11 = builder1.create();
                                            alert11.show();

                                        }
                                    });
                                    PushDownAnim.setPushDownAnimTo(invite).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if (object.getBoolean("IS_PRIVATE")) {

                                                AlertDialog.Builder builder1 = new AlertDialog.Builder(special_speaker_live_table.this, R.style.AlertDialogCustom);
                                                builder1.setMessage(resources.getString(R.string.not_have_permission_to_send_invitation));
                                                builder1.setTitle(resources.getString(R.string.this_is_private));
                                                builder1.setCancelable(false);

                                                builder1.setPositiveButton(
                                                        resources.getString(R.string.OK),
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {


                                                                dialog.cancel();
                                                            }
                                                        });


                                                AlertDialog alert11 = builder1.create();
                                                alert11.show();

                                            } else {
                                                Intent intent = new Intent(special_speaker_live_table.this, invite_people_activity.class);
                                                intent.putExtra("ID", object.getObjectId());
                                                startActivity(intent);
                                                overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
                                            }


                                        }
                                    });
                                    switchButton.setChecked(object.getList("NOTIFY").contains(ParseUser.getCurrentUser().getObjectId()), true);
                                    switchButton.setOnCheckedChangeListener(checked ->
                                    {
                                        switchButton.setChecked(checked, true);

                                        if (checked) {
                                            List<String> ss = object.getList("NOTIFY");
                                            ss.add(ParseUser.getCurrentUser().getObjectId());

                                            object.put("NOTIFY", ss);
                                            object.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {

                                                    if (e == null) {

                                                        roundedBottomSheetDialog.dismiss();
                                                        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                                                            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                                        } else {

                                                            vibrator.vibrate(500);
                                                        }
                                                        Snackbar snackbar = Snackbar.make(v, "🔔 Notification On", Snackbar.LENGTH_SHORT);
                                                        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                                                        snackbar.setDuration(4000);
                                                        snackbar.setBackgroundTint(resources.getColor(R.color.themetextcolor));
                                                        snackbar.setTextColor(resources.getColor(R.color.colorPrimary));
                                                        snackbar.show();
                                                    }


                                                }
                                            });


                                        } else {

                                            if (object.getList("NOTIFY").contains(ParseUser.getCurrentUser().getObjectId())) {

                                                List<String> ss = object.getList("NOTIFY");
                                                ss.remove(ParseUser.getCurrentUser().getObjectId());

                                                object.put("NOTIFY", ss);
                                                object.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {

                                                        if (e == null) {

                                                            roundedBottomSheetDialog.dismiss();
                                                            roundedBottomSheetDialog.dismiss();
                                                            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

                                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                                                                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                                            } else {

                                                                vibrator.vibrate(500);
                                                            }
                                                            Snackbar snackbar = Snackbar.make(v, "🔔 Notification Off", Snackbar.LENGTH_SHORT);
                                                            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                                                            snackbar.setDuration(4000);
                                                            snackbar.setBackgroundTint(getResources().getColor(R.color.themetextcolor));
                                                            snackbar.setTextColor(getResources().getColor(R.color.colorPrimary));
                                                            snackbar.show();
                                                        }


                                                    }
                                                });

                                            }

                                        }

                                        return Unit.INSTANCE;
                                    });
                                    PushDownAnim.setPushDownAnimTo(report).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(activity, report_account.class);
                                            intent.putExtra("ID", object.getObjectId());
                                            intent.setType("ANOTHER");
                                            activity.startActivity(intent);
                                            activity.overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
                                        }
                                    });

                                    roundedBottomSheetDialog.show();


                                }
                            }
                        });

                        PushDownAnim.setPushDownAnimTo(subscriberMic).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (ContextCompat.checkSelfPermission(special_speaker_live_table.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(special_speaker_live_table.this,
                                            new String[]{Manifest.permission.RECORD_AUDIO},
                                            1001);
                                } else {

                                    if (object.getString("table_creator").equals(ParseUser.getCurrentUser().getObjectId())) {
                                        if (!GLOBL_SPK.contains(ParseUser.getCurrentUser().getObjectId())) {

                                            GLOBL_SPK.add(ParseUser.getCurrentUser().getObjectId());
                                            object.put("table_speakers", GLOBL_SPK);
                                            object.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    subscriberMic.setIcon(getResources().getDrawable(R.drawable.mic_on));
                                                    subscriberMic.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.searchboxbackcolor)));
                                                    subscriberMic.setIconTint(ColorStateList.valueOf(getResources().getColor(R.color.allicontintcolor)));
                                                    if (mRtcEngine != null) {
                                                        mRtcEngine.adjustRecordingSignalVolume(100);
                                                    }

                                                }
                                            });


                                            if (!is_notification_sent) {
                                                ParseQuery<ParseObject> parseObjectParseQuery1 = new ParseQuery<ParseObject>("roundtable_user_data");
                                                parseObjectParseQuery1.whereEqualTo("User_ID", ParseUser.getCurrentUser().getObjectId());
                                                parseObjectParseQuery1.findInBackground(new FindCallback<ParseObject>() {
                                                    @Override
                                                    public void done(List<ParseObject> objects, ParseException e) {


                                                        if (object.getBoolean("IS_PRIVATE")) {
                                                            for (ParseObject xxcc : objects) {
                                                                is_notification_sent = true;
                                                                List<String> NOTIFY_LIST = object.getList("NOTIFY");
                                                                for (String IDS : NOTIFY_LIST) {
                                                                    sending_notification_to_users(IDS);
                                                                }
                                                            }
                                                        } else {
                                                            for (ParseObject xxcc : objects) {
                                                                is_notification_sent = true;
                                                                List<String> NOTIFY_LIST = xxcc.getList("who_notify");
                                                                for (String IDS : NOTIFY_LIST) {
                                                                    sending_notification_to_users(IDS);
                                                                }
                                                                List<String> ls = object.getList("NOTIFY");
                                                                for (String IDS : ls) {
                                                                    sending_notification_to_users(IDS);
                                                                }
                                                            }
                                                        }

                                                    }
                                                });

                                            }
                                        } else {
                                            GLOBL_SPK.remove(ParseUser.getCurrentUser().getObjectId());
                                            object.put("table_speakers", GLOBL_SPK);
                                            object.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    subscriberMic.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.settingbuttonbackcoclor)));
                                                    subscriberMic.setIconTint(ColorStateList.valueOf(getResources().getColor(R.color.themetextsecondarycolor)));
                                                    subscriberMic.setIcon(getResources().getDrawable(R.drawable.mic_off));

                                                    if (mRtcEngine != null) {
                                                        mRtcEngine.adjustRecordingSignalVolume(0);
                                                    }

                                                }
                                            });


                                        }


                                    } else {
                                        if (!GLOBL_SPK.contains(ParseUser.getCurrentUser().getObjectId())) {

                                            if (object.getInt("HOW_MANY_ALLOWED") > GLOBL_SPK.size()) {
                                                GLOBL_SPK.add(ParseUser.getCurrentUser().getObjectId());
                                                object.put("table_speakers", GLOBL_SPK);
                                                object.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        subscriberMic.setIcon(getResources().getDrawable(R.drawable.mic_on));
                                                        subscriberMic.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.searchboxbackcolor)));
                                                        subscriberMic.setIconTint(ColorStateList.valueOf(getResources().getColor(R.color.allicontintcolor)));

                                                        if (mRtcEngine != null) {
                                                            mRtcEngine.adjustRecordingSignalVolume(100);
                                                        }
                                                    }
                                                });

                                                if (!is_notification_sent) {
                                                    ParseQuery<ParseObject> parseObjectParseQuery1 = new ParseQuery<ParseObject>("roundtable_user_data");
                                                    parseObjectParseQuery1.whereEqualTo("User_ID", ParseUser.getCurrentUser().getObjectId());
                                                    parseObjectParseQuery1.findInBackground(new FindCallback<ParseObject>() {
                                                        @Override
                                                        public void done(List<ParseObject> objects, ParseException e) {
                                                            if (object.getBoolean("IS_PRIVATE")) {
                                                                for (ParseObject xxcc : objects) {
                                                                    is_notification_sent = true;
                                                                    List<String> NOTIFY_LIST = object.getList("NOTIFY");
                                                                    for (String IDS : NOTIFY_LIST) {
                                                                        sending_notification_to_users(IDS);
                                                                    }
                                                                }
                                                            } else {
                                                                for (ParseObject xxcc : objects) {
                                                                    is_notification_sent = true;
                                                                    List<String> NOTIFY_LIST = xxcc.getList("who_notify");
                                                                    for (String IDS : NOTIFY_LIST) {
                                                                        sending_notification_to_users(IDS);
                                                                    }
                                                                    List<String> ls = object.getList("NOTIFY");
                                                                    for (String IDS : ls) {
                                                                        sending_notification_to_users(IDS);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    });

                                                }

                                            } else {

                                                AlertDialog.Builder builder1 = new AlertDialog.Builder(special_speaker_live_table.this, R.style.AlertDialogCustom);
                                                builder1.setMessage(resources.getString(R.string.max_people_eccesed));
                                                builder1.setTitle(resources.getString(R.string.People_limit_exceed));
                                                builder1.setCancelable(false);


                                                builder1.setPositiveButton(
                                                        resources.getString(R.string.OK),
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.cancel();
                                                            }
                                                        });


                                                AlertDialog alert11 = builder1.create();
                                                alert11.show();

                                            }

                                        } else {
                                            GLOBL_SPK.remove(ParseUser.getCurrentUser().getObjectId());
                                            object.put("table_speakers", GLOBL_SPK);
                                            object.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    subscriberMic.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.settingbuttonbackcoclor)));
                                                    subscriberMic.setIconTint(ColorStateList.valueOf(getResources().getColor(R.color.themetextsecondarycolor)));
                                                    subscriberMic.setIcon(getResources().getDrawable(R.drawable.mic_off));

                                                    if (mRtcEngine != null) {
                                                        mRtcEngine.adjustRecordingSignalVolume(0);
                                                    }

                                                }
                                            });

                                        }

                                    }
                                }


                            }
                        });
                        PushDownAnim.setPushDownAnimTo(subscriberLeaveTable).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                leave_table();

                            }
                        });
                        PushDownAnim.setPushDownAnimTo(sub_linear).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(special_speaker_live_table.this, search_in_table.class);
                                intent.putExtra("ID", GLOBAL_TABLE_ID);
                                startActivity(intent);
                                overridePendingTransition(R.anim.bottom_up, R.anim.stationary);

                            }
                        });
                        PushDownAnim.setPushDownAnimTo(invitePeople).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (object.getList("co_creator").contains(ParseUser.getCurrentUser().getObjectId()) || object.getString("table_creator").equals(ParseUser.getCurrentUser().getObjectId())) {

                                    Intent intent = new Intent(special_speaker_live_table.this, invite_people_activity.class);
                                    intent.putExtra("ID", GLOBAL_TABLE_ID);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
                                } else {

                                    if (object.getBoolean("IS_PRIVATE")) {


                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(special_speaker_live_table.this, R.style.AlertDialogCustom);
                                        builder1.setMessage(resources.getString(R.string.not_have_permission_to_send_invitation));
                                        builder1.setTitle(resources.getString(R.string.this_is_private));
                                        builder1.setCancelable(false);

                                        builder1.setPositiveButton(
                                                resources.getString(R.string.OK),
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {


                                                        dialog.cancel();
                                                    }
                                                });


                                        AlertDialog alert11 = builder1.create();
                                        alert11.show();


                                    } else {
                                        Intent intent = new Intent(special_speaker_live_table.this, invite_people_activity.class);
                                        intent.putExtra("ID", GLOBAL_TABLE_ID);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.bottom_up, R.anim.stationary);

                                    }

                                }

                            }
                        });


                        int ROLE = (object.getList("co_creator").contains(ParseUser.getCurrentUser().getObjectId()) ||
                                object.getString("table_creator").equals(ParseUser.getCurrentUser().getObjectId())) ? 1 : 0;

                        initializeEngine();
                        join_table(ROLE);


                        try {
                            parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI(App.getLIVE()));
                        } catch (URISyntaxException et) {
                            et.printStackTrace();
                        }

                        ParseQuery<ParseObject> last_OTHER = new ParseQuery<ParseObject>("live_tables");
                        last_OTHER.whereEqualTo("objectId", GLOBAL_TABLE_ID);
                        SubscriptionHandling<ParseObject> subscriptionHandling_OTHER = parseLiveQueryClient.subscribe(last_OTHER);
                        subscriptionHandling_OTHER.handleSubscribe(new SubscriptionHandling.HandleSubscribeCallback<ParseObject>() {
                            @Override
                            public void onSubscribe(ParseQuery<ParseObject> query) {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        main_panel_shimmer.stopShimmer();
                                        main_panel_shimmer.setVisibility(View.GONE);
                                        main_panel_constrain.setVisibility(View.VISIBLE);
                                        main_controller.setVisibility(View.VISIBLE);
                                    }
                                });

                            }
                        });
                        subscriptionHandling_OTHER.handleEvents(new SubscriptionHandling.HandleEventsCallback<ParseObject>() {
                            @Override
                            public void onEvents(ParseQuery<ParseObject> query, SubscriptionHandling.Event event, ParseObject objectp) {

                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.post(new Runnable() {
                                    public void run() {


                                        try {

                                            Calendar calendar1, calendar2;
                                            String string1 = objectp.getString("START_BACK");
                                            Date time1 = new SimpleDateFormat("HH:mm:ss").parse(string1);

                                            String string2 = objectp.getString("END_BACK");
                                            Date time2 = new SimpleDateFormat("HH:mm:ss").parse(string2);

                                            if (time1.getHours() > time2.getHours()) {
                                                time1 = dateFormat.parse("02/26/2099 " + string1);
                                                time2 = dateFormat.parse("02/27/2099 " + string2);

                                                calendar1 = Calendar.getInstance();
                                                calendar1.setTime(time1);
                                                calendar1.add(Calendar.DATE, 1);

                                                calendar2 = Calendar.getInstance();
                                                calendar2.setTime(time2);
                                                calendar2.add(Calendar.DATE, 1);
                                            } else {

                                                time1 = dateFormat.parse("02/27/2099 " + string1);
                                                time2 = dateFormat.parse("02/27/2099 " + string2);

                                                calendar1 = Calendar.getInstance();
                                                calendar1.setTime(time1);
                                                calendar1.add(Calendar.DATE, 1);

                                                calendar2 = Calendar.getInstance();
                                                calendar2.setTime(time2);
                                                calendar2.add(Calendar.DATE, 1);

                                            }

                                            Date x = calendar3.getTime();
                                            if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {

                                            } else {
                                                leave_table();
                                            }

                                        } catch (java.text.ParseException parseException) {
                                            parseException.printStackTrace();
                                        }


                                        List<String> all_moderator = objectp.getList("table_speakers");
                                        ParseQuery<ParseUser> USER = ParseUser.getQuery();
                                        USER.whereContainedIn("objectId", all_moderator);
                                        USER.findInBackground(new FindCallback<ParseUser>() {
                                            @Override
                                            public void done(List<ParseUser> objects, ParseException e) {
                                                all_member_creator__special_speaker_adapter.ref(objects);
                                            }
                                        });

                                        if (objectp.getList("table_speakers").size() == 0) {
                                            if_no_one_speaking_layout.setVisibility(View.VISIBLE);
                                            no_one_speaking.setText(resources.getString(R.string.No_ONE_SPEAKING));
                                            TOO_QUITE_HERE.setText(resources.getString(R.string.TOO_QUITE_HERE));
                                        } else {
                                            if_no_one_speaking_layout.setVisibility(View.GONE);
                                        }

                                        if (!objectp.getString("table_creator").equals(ParseUser.getCurrentUser().getObjectId())) {

                                            if (!objectp.getList("table_subscribers").contains(ParseUser.getCurrentUser().getObjectId())) {

                                                leave_table();

                                            }

                                        }

                                        if (!objectp.getBoolean("Active")) {

                                            leave_table();
                                        }


                                        if (objectp.getString("TALKING_TITLE").trim().isEmpty()) {
                                            if (objectp.getList("co_creator").contains(ParseUser.getCurrentUser().getObjectId()) || objectp.getString("table_creator").equals(ParseUser.getCurrentUser().getObjectId())) {
                                                if_title_empty.setVisibility(View.VISIBLE);
                                                textView35.setVisibility(View.GONE);
                                            } else {
                                                if_title_empty.setVisibility(View.GONE);
                                                textView35.setVisibility(View.GONE);
                                            }

                                        } else {
                                            if_title_empty.setVisibility(View.GONE);
                                            textView35.setVisibility(View.VISIBLE);
                                        }

                                        notification_on_off.setIcon(objectp.getList("NOTIFY").contains(ParseUser.getCurrentUser().getObjectId()) ?
                                                special_speaker_live_table.this.getDrawable(R.drawable.notification_on) :
                                                special_speaker_live_table.this.getDrawable(R.drawable.notification_off));


                                        textView35.setText(objectp.getString("TALKING_TITLE"));
                                        how_many_speaking.setText(objectp.getList("table_speakers").size() + "/" + objectp.getInt("HOW_MANY_ALLOWED") + " " + resources.getString(R.string.SPEAKING));
                                        GLOBL_SPK = objectp.getList("table_speakers");
                                        Animation anim = new AlphaAnimation(0.5f, 1.0f);
                                        anim.setDuration(50); //You can manage the blinking time with this parameter
                                        anim.setStartOffset(20);
                                        anim.setRepeatMode(Animation.REVERSE);
                                        anim.setRepeatCount(Animation.ABSOLUTE);
                                        howManyOnline.startAnimation(anim);
                                        howManyOnline.setText(Integer.toString(objectp.getList("ONLINE").size()));
                                        time_of_table.setText(objectp.getString("start_time") + " To " + objectp.getString("end_time"));
                                        subs_text.setText(objectp.getList("table_subscribers").size() + " " + resources.getString(R.string.Subscribers));
                                        tbl_nme.setText(objectp.getString("table_name"));
                                        List<String> ss = new ArrayList<>();
                                        ss.add("News");
                                        ss.add("Arts & Entertainment");
                                        ss.add("Health & Beauty");
                                        ss.add("Science & Tech");
                                        ss.add("Politics");
                                        ss.add("Business");
                                        ss.add("Books & Information");

                                        if (!ss.contains(objectp.getString("table_name"))) {
                                            table_description.setVisibility(objectp.getString("table_description").trim().isEmpty() ? View.GONE : View.VISIBLE);
                                            table_title.setText(objectp.getString("table_name"));
                                            table_description.setText(objectp.getString("table_description"));
                                        }


                                    }
                                });


                            }
                        });


                    } else {
                        main_panel_shimmer.stopShimmer();
                        main_panel_shimmer.setVisibility(View.GONE);

                        // NOT LIVE
                        Intent intent = new Intent(special_speaker_live_table.this, time_remain_to_live_activity.class);
                        intent.putExtra("ID", GLOBAL_TABLE_ID);
                        startActivity(intent);
                        overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
                        finish();

                    }
                } catch (java.text.ParseException parseException) {
                    parseException.printStackTrace();
                }
            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        if (parseLiveQueryClient != null) {
            parseLiveQueryClient.disconnect();
        }
        overridePendingTransition(R.anim.stationary, R.anim.bottom_down);
    }

    private void initializeEngine() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), getaudiokey(), mRtcEventHandler);
        } catch (Exception e) {
            Log.e("CHECK_AUDIO", Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }

    public void join_table(int ROLE) {
        RtcTokenBuilder token = new RtcTokenBuilder();
        int timestamp = (int) (System.currentTimeMillis() / 1000 + expirationTimeInSeconds);
        String result = token.buildTokenWithUid(getaudiokey(), getAudiocertificate(),
                GLOBAL_TABLE_ID, uid, RtcTokenBuilder.Role.Role_Publisher, timestamp);


        if (ROLE == 1) {
            mRtcEngine.setClientRole(Constants.CLIENT_ROLE_BROADCASTER);
        } else {
            mRtcEngine.setClientRole(Constants.CLIENT_ROLE_AUDIENCE);
        }

        mRtcEngine.enableDeepLearningDenoise(true);

        mRtcEngine.setAudioProfile(Constants.AUDIO_SCENARIO_GAME_STREAMING, Constants.AUDIO_SCENARIO_MEETING);

        mRtcEngine.enableAudioVolumeIndication(200, 3, false);

        mRtcEngine.joinChannel(result, GLOBAL_TABLE_ID, "PS:)", 0);

        if (!GLOBL_SPK.contains(ParseUser.getCurrentUser().getObjectId())) {
            mRtcEngine.adjustRecordingSignalVolume(0);
        } else {
            mRtcEngine.adjustRecordingSignalVolume(100);
        }

        mRtcEngine.enableInEarMonitoring(false);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.RECORD_AUDIO)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show();
                    } else {
                        requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 1001);
                    }
                }
            }
        }

    }

    public void sending_notification_to_users(String USER_ID) {

        if (!USER_ID.equals(ParseUser.getCurrentUser().getObjectId())) {
            ParseQuery<ParseInstallation> parseInstallationParseQuery = ParseInstallation.getQuery();
            parseInstallationParseQuery.whereEqualTo("UserId", USER_ID);
            parseInstallationParseQuery.findInBackground(new FindCallback<ParseInstallation>() {
                @Override
                public void done(List<ParseInstallation> objects, ParseException e) {


                    JSONObject data = new JSONObject();

                    try {
                        data.put("alert", ParseUser.getCurrentUser().getString("First_and_last_name") + " Start Speaking 🎙 on " + TABLE_NAME + "." + "Tap To Listen What They Are Talking.");
                        data.put("title", ParseUser.getCurrentUser().getString("First_and_last_name"));
                        data.put("USER_ID", USER_ID);
                        data.put("Id", GLOBAL_TABLE_ID);
                        data.put("Type", Constant.USER_SPEAKING_ACTIVITY);
                    } catch (JSONException eT) {

                        Log.i("ERROR", eT.getLocalizedMessage());
                        // should not happen
                    }

                    ParsePush push = new ParsePush();
                    push.setQuery(parseInstallationParseQuery);
                    push.setData(data);
                    push.sendInBackground();


                }
            });

        }


    }

    @Override

    protected void onResume() {
        super.onResume();
        AudioManager am = getAudioManager();
        if (am.isSpeakerphoneOn()) {
            Log.d("LOG:", "AUDIO_ROUTE_SPEAKERPHONE");
        } else if (am.isBluetoothScoOn() || am.isBluetoothA2dpOn()) {
            Log.d("LOG:", "AUDIO_ROUTE_HEADSETBLUETOOTH");
        } else if (am.isWiredHeadsetOn()) {
            Log.d("LOG:", "AUDIO_ROUTE_HEADSET");
        } else {
            Log.d("LOG:", "AUDIO_ROUTE_EARPIECE");
            // Call setEnableSpeakerphone here to route the audio output to the speaker or earpiece
        }
    }

    public native String getaudiokey();

    public native String getAudiocertificate();

    public AudioManager getAudioManager() {
        Context context = this.getApplicationContext();
        if (context == null) {
            return null;
        }

        return (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public void leave_checK_pre() {
        ParseUser.getCurrentUser().put("WHAT_LISTENING", "");
        ParseUser.getCurrentUser().saveInBackground();

        if (mRtcEngine != null) {
            mRtcEngine.leaveChannel();
        }
        RtcEngine.destroy();
        if (parseLiveQueryClient != null) {
            parseLiveQueryClient.disconnect();
        }
        App.IS_LISTENING = false;
        App.WHICH_TABLE = "";
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(13);
    }

    public void leave_table() {
        ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("live_tables");
        parseObjectParseQuery.getInBackground(GLOBAL_TABLE_ID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                try {
                    ParseUser.getCurrentUser().put("WHAT_LISTENING", "");
                    ParseUser.getCurrentUser().saveInBackground();

                    if (mRtcEngine != null) {
                        mRtcEngine.leaveChannel();
                    }
                    RtcEngine.destroy();
                    if (parseLiveQueryClient != null) {
                        parseLiveQueryClient.disconnect();
                    }
                    List<String> speaker = object.getList("table_speakers");
                    List<String> online = object.getList("ONLINE");
                    speaker.remove(ParseUser.getCurrentUser().getObjectId());
                    object.put("table_speakers", speaker);
                    object.saveInBackground();
                    online.remove(ParseUser.getCurrentUser().getObjectId());
                    object.put("ONLINE", online);
                    object.saveInBackground();
                    App.IS_LISTENING = false;
                    App.WHICH_TABLE = "";
                    NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(13);
                    finish();
                    overridePendingTransition(R.anim.stationary, R.anim.bottom_down);
                } catch (Exception dd) {
                    dd.printStackTrace();
                }
            }
        });

    }

}
