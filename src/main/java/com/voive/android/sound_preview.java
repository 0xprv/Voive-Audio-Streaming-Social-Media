package com.voive.android;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
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
import com.sanojpunchihewa.glowbutton.GlowButton;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.OnBalloonDismissListener;
import com.thekhaeng.pushdownanim.PushDownAnim;
import com.voive.android.media.RtcTokenBuilder;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;

public class sound_preview extends AppCompatActivity {

    static int uid = 0;
    public static RtcEngine mRtcEngine;
    static int expirationTimeInSeconds = 3600;
    @BindView(R.id.shimer)
    ShimmerFrameLayout shimer;
    @BindView(R.id.back)
    MaterialButton back;
    @BindView(R.id.deactivate_textview)
    TextView deactivateTextview;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.circle_image)
    CircleImageView circleImage;
    @BindView(R.id.deactivate_message_textview)
    TextView deactivateMessageTextview;
    @BindView(R.id.subs_text)
    TextView subsText;
    @BindView(R.id.tbl_conatainer)
    RelativeLayout tblConatainer;
    @BindView(R.id.to_show)
    LinearLayout toShow;
    @BindView(R.id.materialButton3)
    MaterialButton materialButton3;
    @BindView(R.id.req)
    CircularProgressButton req;
    @BindView(R.id.constraintLayout3)
    RelativeLayout constraintLayout3;
    IRtcEngineEventHandler mRtcEventHandler;
    @BindView(R.id.recyler_view)
    RecyclerView recylerView;
    @BindView(R.id.relativeLayout10)
    RelativeLayout relativeLayout10;
    @BindView(R.id.main)
    ConstraintLayout main;
    @BindView(R.id.live)
            MaterialButton lv;
    @BindView(R.id.table_clickable)
    LinearLayout table_clickable;
    @BindView(R.id.clic_cir)
    LinearLayout clic_cir;
    @BindView(R.id.lv)
            TextView live;
    @BindView(R.id.no_one_speaking)
            TextView no_one_speaking;
    @BindView(R.id.tbl_nme)
            TextView tbl_nme;
    all_member_creator__special_speaker_adapter all_member_creator__special_speaker_adapter;
    String ID = "";

    ParseLiveQueryClient parseLiveQueryClient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_preview);
        ButterKnife.bind(this);
        //TODO WORK ON SOUND PREVIEW
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
        Slidr.attach(this, config);
        ID = getIntent().getStringExtra("ID");

        mRtcEventHandler = new IRtcEngineEventHandler() {
            @Override
            public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
                super.onJoinChannelSuccess(channel, uid, elapsed);
            }

            @Override
            public void onLeaveChannel(RtcStats stats) {
                super.onLeaveChannel(stats);
            }

            @Override
            public void onUserJoined(int uid, int elapsed) {
                super.onUserJoined(uid, elapsed);
            }

            @Override
            public void onConnectionInterrupted() {
                super.onConnectionInterrupted();
            }

            @Override
            public void onConnectionLost() {
                super.onConnectionLost();
            }

            @Override
            public void onMicrophoneEnabled(boolean enabled) {
                super.onMicrophoneEnabled(enabled);
            }

            @Override
            public void onUserOffline(int uid, int reason) {
                super.onUserOffline(uid, reason);
            }

            @Override
            public void onAudioVolumeIndication(AudioVolumeInfo[] speakers, int totalVolume) {
                super.onAudioVolumeIndication(speakers, totalVolume);
                //ParseUser.getCurrentUser().put("SOUND", totalVolume);
                //  ParseUser.getCurrentUser().saveInBackground();
            }

        };
        initializeEngine();
        join_table(0);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseQuery<ParseObject> QUERY = new ParseQuery<ParseObject>("live_tables");
                QUERY.getInBackground(ID, new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        List<String> tuned_ins=object.getList("ONLINE");
                        if(tuned_ins.contains(ParseUser.getCurrentUser().getObjectId())){

                            tuned_ins.remove(ParseUser.getCurrentUser().getObjectId());
                            object.put("ONLINE",tuned_ins);
                            object.saveInBackground();

                        }
                    }
                });
                if(mRtcEngine!=null){
                    mRtcEngine.leaveChannel();
                }
                RtcEngine.destroy();
                finish();
                if(parseLiveQueryClient!=null){
                    parseLiveQueryClient.disconnect();
                }


            }
        });

        ParseQuery<ParseObject> QUERY = new ParseQuery<ParseObject>("live_tables");
        QUERY.getInBackground(ID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {

                SharedPreferences sharedPreferences = sound_preview.this.getSharedPreferences("LANG", Context.MODE_PRIVATE);
                String lng = sharedPreferences.getString("lng", "en");
                boolean is_first_popup=sharedPreferences.getBoolean("FIRST_POPUP",true);
                Context language_context = LocaleHelper.setLocale(sound_preview.this, lng);
                Resources resources = language_context.getResources();
                live.setText(resources.getString(R.string.LIVE));
                no_one_speaking.setText(resources.getString(R.string.No_ONE_SPEAKING));
                if (!object.getBoolean("ALLOW_RECORDING")) {
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                            WindowManager.LayoutParams.FLAG_SECURE);
                }
                if(is_first_popup){
                    final Typeface typeface = ResourcesCompat.getFont(sound_preview.this, R.font.montr_bold);
                    Balloon balloon = new Balloon.Builder(sound_preview.this.getApplicationContext())
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
                            .setText(resources.getString(R.string.sub_of_you_popup))
                            .setTextColor(resources.getColor(R.color.black))
                            .setBackgroundColor(resources.getColor(R.color.colorAccent))
                            .setBalloonAnimation(BalloonAnimation.FADE)
                            .build();

                    balloon.setOnBalloonDismissListener(new OnBalloonDismissListener() {
                        @Override
                        public void onBalloonDismiss() {

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("FIRST_POPUP", false);
                            editor.apply();

                        }
                    });
                    balloon.dismissWithDelay(20000L);
                    balloon.showAlignTop(req);
                }


                if (!object.getBoolean("ALLOW_RECORDING")) {
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                            WindowManager.LayoutParams.FLAG_SECURE);
                }

                List<String> strings=ParseUser.getCurrentUser().getList("LAST_VISITED");
                if(!strings.contains(ID)){
                    strings.add(ID);
                    ParseUser.getCurrentUser().put("LAST_VISITED",strings);
                    ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {


                        }
                    });
                }
                List<String> tuned_ins=object.getList("ONLINE");
                tbl_nme.setText(object.getString("table_name"));
                if(!tuned_ins.contains(ParseUser.getCurrentUser().getObjectId())){

                    tuned_ins.add(ParseUser.getCurrentUser().getObjectId());
                    object.put("ONLINE",tuned_ins);
                    object.saveInBackground();

                }
                ParseQuery<ParseUser> parseUserParseQuery = ParseUser.getQuery();
                parseUserParseQuery.whereContainedIn("objectId", object.getList("table_speakers"));
                parseUserParseQuery.setLimit(20);
                parseUserParseQuery.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {

                        if(objects.size()>0){
                            all_member_creator__special_speaker_adapter =
                                    new all_member_creator__special_speaker_adapter(objects, sound_preview.this, object.getString("table_creator"), ID, sound_preview.this);

                            recylerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
                            recylerView.setAdapter(all_member_creator__special_speaker_adapter);
                        }
                        else {
                            no_one_speaking.setVisibility(View.VISIBLE);
                        }



                    }
                });

                try {
                    parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI((App.getLIVE())));
                } catch (URISyntaxException et) {
                    et.printStackTrace();
                }


                ParseQuery<ParseObject> last_OTHER = new ParseQuery<ParseObject>("live_tables");
                last_OTHER.whereEqualTo("objectId", getIntent().getStringExtra("ID"));

                SubscriptionHandling<ParseObject> subscriptionHandling_OTHER = parseLiveQueryClient.subscribe(last_OTHER);

                subscriptionHandling_OTHER.handleEvents(new SubscriptionHandling.HandleEventsCallback<ParseObject>() {
                    @Override
                    public void onEvents(ParseQuery<ParseObject> query, SubscriptionHandling.Event event, ParseObject objectp) {

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            public void run() {


                                if(objectp.getString("TALKING_TITLE").trim().isEmpty()){
                                    deactivateTextview.setVisibility(View.GONE);
                                }
                                else {
                                    deactivateTextview.setVisibility(View.VISIBLE);
                                    deactivateTextview.setText(objectp.getString("TALKING_TITLE"));
                                }

                                subsText.setText(Integer.toString(objectp.getList("ONLINE").size()));
                                ParseQuery<ParseUser> USER = ParseUser.getQuery();
                                USER.whereContainedIn("objectId",  objectp.getList("table_speakers"));
                                USER.findInBackground(new FindCallback<ParseUser>() {
                                    @Override
                                    public void done(List<ParseUser> objects, ParseException e) {
                                        try {
                                            all_member_creator__special_speaker_adapter.ref(objects);
                                        }
                                        catch (Exception ccc){
                                            ccc.printStackTrace();
                                        }

                                    }
                                });

                            }
                        });


                    }
                });
                Glide.with(sound_preview.this.getApplicationContext()).asBitmap().load(object.getParseFile("table_image").getUrl())
                        .centerInside().placeholder(R.drawable.main_placeholder)
                        .thumbnail(0.5f)
                        .into(circleImage);

                subsText.setText(Integer.toString(object.getList("ONLINE").size()));

                Glide.with(getApplicationContext()).asBitmap().load(object.getParseFile("table_image").getUrl())
                        .centerInside().placeholder(R.drawable.main_placeholder).thumbnail(0.3f).into(circleImage);

                if(object.getString("TALKING_TITLE").trim().isEmpty()){
                    deactivateTextview.setVisibility(View.GONE);
                }
                else {
                    deactivateTextview.setVisibility(View.VISIBLE);
                    deactivateTextview.setText(object.getString("TALKING_TITLE"));
                }


                deactivateMessageTextview.setText(object.getString("table_name"));

                PushDownAnim.setPushDownAnimTo(materialButton3).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String QR="https://voive.in/tables/invites/?id="+object.getObjectId();
                        String main_string="सुनने "+object.getString("table_name")+ " में क्या बात चित हो रही है। " + QR;
                        RoundedBottomSheetDialog roundedBottomSheetDialog = new RoundedBottomSheetDialog(sound_preview.this);

                        View view = LayoutInflater.from(sound_preview.this).inflate(R.layout.bottomsheet_comment_layout, null, false);
                        roundedBottomSheetDialog.setContentView(view);

                        SharedPreferences sharedPreferences_lan = getSharedPreferences("LANG", Context.MODE_PRIVATE);
                        String lng = sharedPreferences_lan.getString("lng", "en");
                        Context language_context = LocaleHelper.setLocale(sound_preview.this, lng);
                        Resources resources = language_context.getResources();


                        LinearLayout twitter = view.findViewById(R.id.lr1);
                        LinearLayout whtsapp = view.findViewById(R.id.lr2);
                        LinearLayout copy = view.findViewById(R.id.lr3);
                        LinearLayout msg = view.findViewById(R.id.lr4);
                        LinearLayout more = view.findViewById(R.id.lr5);


                        PushDownAnim.setPushDownAnimTo(msg).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                                sendIntent.setData(Uri.parse("sms:"));
                                sendIntent.putExtra("sms_body", main_string);
                                startActivity(sendIntent);

                            }
                        });
                        PushDownAnim.setPushDownAnimTo(copy).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                ClipData clipData = ClipData.newPlainText("Sneak Link", main_string);
                                clipboardManager.setPrimaryClip(clipData);
                                Toast.makeText(sound_preview.this, "Link Copied", Toast.LENGTH_SHORT).show();
                                roundedBottomSheetDialog.dismiss();

                            }
                        });
                        PushDownAnim.setPushDownAnimTo(whtsapp).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                                whatsappIntent.setType("text/plain");
                                whatsappIntent.setPackage("com.whatsapp");
                                whatsappIntent.putExtra(Intent.EXTRA_TEXT, main_string);
                                try {
                                    startActivity(whatsappIntent);
                                } catch (ActivityNotFoundException ex) {
                                    final String appPackageName = "com.whatsapp"; // getPackageName() from Context or Activity object
                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                    } catch (ActivityNotFoundException anfe) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                    }
                                }

                            }
                        });
                        PushDownAnim.setPushDownAnimTo(twitter).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                shareTwitter(main_string);

                            }
                        });
                        PushDownAnim.setPushDownAnimTo(more).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("text/plain");
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Share Bite Of Conversation");
                                intent.putExtra(Intent.EXTRA_TEXT, main_string);
                                startActivity(Intent.createChooser(intent, "Share Via"));
                            }
                        });
                        roundedBottomSheetDialog.show();

                    }
                });

                if(object.getList("table_subscribers").contains(ParseUser.getCurrentUser().getObjectId())){



                    parseLiveQueryClient.disconnect();
                    if(mRtcEngine!=null){
                        mRtcEngine.leaveChannel();
                    }
                    RtcEngine.destroy();
                    finish();
                    Intent intent1 = new Intent(sound_preview.this, special_speaker_live_table.class);
                    intent1.putExtra("ID", ID);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.bottom_up, R.anim.stationary);

                }
                else {
                    req.setText(resources.getString(R.string.Subscribe));
                    PushDownAnim.setPushDownAnimTo(req).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            req.startAnimation();


                            ParseQuery<ParseObject> parseObjectParseQuery=new ParseQuery<ParseObject>("roundtable_user_data");
                            parseObjectParseQuery.whereEqualTo("User_ID",ParseUser.getCurrentUser().getObjectId());
                            parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {

                                    for(ParseObject parseObject:objects){

                                        List<String> ss=parseObject.getList("Subscriptions");
                                        if(!ss.contains(object.getObjectId())){
                                            ss.add(object.getObjectId());
                                            parseObject.put("Subscriptions",ss);
                                            parseObject.saveInBackground();
                                        }


                                    }

                                }
                            });

                            List<String> REQUESTS = object.getList("table_subscribers");
                            if(!REQUESTS.contains(ParseUser.getCurrentUser().getObjectId())){
                                REQUESTS.add(ParseUser.getCurrentUser().getObjectId());
                                object.put("table_subscribers", REQUESTS);
                            }

                            List<String> NOTIFY=object.getList("NOTIFY");
                            NOTIFY.add(ParseUser.getCurrentUser().getObjectId());
                            object.put("NOTIFY",NOTIFY);

                            object.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    req.stopAnimation();
                                    List<String> tuned_ins=object.getList("ONLINE");
                                    tuned_ins.remove(ParseUser.getCurrentUser().getObjectId());
                                    object.put("ONLINE",tuned_ins);
                                    object.saveInBackground();

                                    parseLiveQueryClient.disconnect();
                                    if(mRtcEngine!=null){
                                        mRtcEngine.leaveChannel();
                                    }
                                    RtcEngine.destroy();
                                    finish();
                                    Toast.makeText(sound_preview.this, "Subscribed", Toast.LENGTH_SHORT).show();
                                    Intent intent1 = new Intent(sound_preview.this, special_speaker_live_table.class);
                                    intent1.putExtra("ID", ID);
                                    startActivity(intent1);
                                    overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
                                }
                            });

                            ParseQuery<ParseInstallation> parseInstallationParseQuery = ParseInstallation.getQuery();
                            parseInstallationParseQuery.whereEqualTo("UserId", object.getString("table_creator"));
                            parseInstallationParseQuery.findInBackground(new FindCallback<ParseInstallation>() {
                                @Override
                                public void done(List<ParseInstallation> objects, ParseException e) {


                                    JSONObject data = new JSONObject();
                                    try {
                                        data.put("alert", ParseUser.getCurrentUser().getString("First_and_last_name") + " Subscribe Your Table");
                                        data.put("title", ParseUser.getCurrentUser().getString("First_and_last_name"));
                                        data.put("Id", object.getObjectId());
                                        data.put("TYPE", Constant.TABLE_OTHER_NOTIFICATION);
                                    } catch (JSONException te) {
                                        throw new IllegalArgumentException("unexpected parsing error", te);
                                    }
                                    ParsePush push = new ParsePush();
                                    push.setQuery(parseInstallationParseQuery);
                                    push.setData(data);
                                    push.sendInBackground();


                                }
                            });

                        }
                    });
                }

                shimer.stopShimmer();
                shimer.setVisibility(View.GONE);
                main.setVisibility(View.VISIBLE);

            }
        });

        PushDownAnim.setPushDownAnimTo(clic_cir,table_clickable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(sound_preview.this, manage_notification.class);
                intent.putExtra("ID", ID);
                startActivity(intent);
                overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
            }
        });

    }
    @Override
    public void finish() {

        ParseQuery<ParseObject> QUERY = new ParseQuery<ParseObject>("live_tables");
        QUERY.getInBackground(ID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                List<String> tuned_ins=object.getList("ONLINE");
                if(tuned_ins.contains(ParseUser.getCurrentUser().getObjectId())){

                    tuned_ins.remove(ParseUser.getCurrentUser().getObjectId());
                    object.put("ONLINE",tuned_ins);
                    object.saveInBackground();

                }
            }
        });
        if(mRtcEngine!=null){
            mRtcEngine.leaveChannel();
        }
        RtcEngine.destroy();
        super.finish();
        if(parseLiveQueryClient!=null){
            parseLiveQueryClient.disconnect();
        }
        overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
    }

    private void shareTwitter(String message) {
        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        tweetIntent.putExtra(Intent.EXTRA_TEXT, message);
        tweetIntent.setType("text/plain");

        PackageManager packManager = getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for (ResolveInfo resolveInfo : resolvedInfoList) {
            if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")) {
                tweetIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name);
                resolved = true;
                break;
            }
        }
        if (resolved) {
            startActivity(tweetIntent);
        } else {
            final String appPackageName = "com.twitter.android"; // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }


        }
    }

    public static boolean id_url(String geti) {

        String s = "((http:\\/\\/|https:\\/\\/)?(www.)?(([a-zA-Z0-9-]){2,}\\.){1,4}([a-zA-Z]){2,6}(\\/([a-zA-Z-_\\/\\.0-9#:?=&;,]*)?)?)";
        Pattern p = Pattern.compile(s);
        Matcher m = p.matcher(geti);
        return m.find();

    }

    public boolean validate(String ss) {

        try {
            URL obj = new URL(ss);
            obj.toURI();
            return true;
        } catch (MalformedURLException e) {
            return false;
        } catch (URISyntaxException e) {
            return false;
        }

    }

    public static ArrayList<String> pullLinks(String text) {
        ArrayList<String> links = new ArrayList<String>();

        //String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
        String regex = "((http:\\/\\/|https:\\/\\/)?(www.)?(([a-zA-Z0-9-]){2,}\\.){1,4}([a-zA-Z]){2,6}(\\/([a-zA-Z-_\\/\\.0-9#:?=&;,]*)?)?)";

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);

        while (m.find()) {
            String urlStr = m.group();

            if (urlStr.startsWith("(") && urlStr.endsWith(")")) {
                urlStr = urlStr.substring(1, urlStr.length() - 1);
            }

            links.add(urlStr);
        }

        return links;
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
                ID, uid, RtcTokenBuilder.Role.Role_Publisher, timestamp);


        mRtcEngine.setClientRole(Constants.CLIENT_ROLE_AUDIENCE);

        mRtcEngine.setAudioProfile(Constants.AUDIO_SCENARIO_GAME_STREAMING  , Constants.AUDIO_SCENARIO_MEETING);

        mRtcEngine.joinChannel(result, ID, "PS:)", 0);

        mRtcEngine.adjustRecordingSignalVolume(0);
    }

    public native String getaudiokey();
    public native String getAudiocertificate();
    static {
        System.loadLibrary("api-keys");
    }

}