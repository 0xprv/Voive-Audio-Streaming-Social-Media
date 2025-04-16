package com.voive.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;
import com.skydoves.balloon.OnBalloonDismissListener;
import com.thekhaeng.pushdownanim.PushDownAnim;
import com.voive.android.media.RtcTokenBuilder;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import de.hdodenhof.circleimageview.CircleImageView;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;

public class preview_of_talking_two_minutes extends Fragment {

    ShimmerFrameLayout shimer;
    TextView deactivateTextview;
    LinearLayout ll;
    CircleImageView circleImage;
    TextView deactivateMessageTextview;
    TextView subsText;
    LinearLayout tblConatainer;
    LinearLayout toShow;
    MaterialButton req;
    RelativeLayout constraintLayout3;
    RecyclerView recylerView;
    RelativeLayout relativeLayout10;
    ConstraintLayout main;
    TextView no_one_speaking;
    TextView in_km;
    all_member_creator__special_speaker_adapter all_member_creator__special_speaker_adapter;
    IRtcEngineEventHandler mRtcEventHandler;
    String AUDIO_KEY;
    String AUDIO_CERTIFICATE;
    LinearLayout clic_cir;
    public preview_of_talking_two_minutes(String AUDIO_KEY, String AUDIO_CERTIFICATE) {
        this.AUDIO_KEY = AUDIO_KEY;
        this.AUDIO_CERTIFICATE = AUDIO_CERTIFICATE;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_preview_of_talking_two_minutes,container,false);


        SharedPreferences sharedPreferences = getContext().getSharedPreferences("LANG", getContext().MODE_PRIVATE);
        String lng = sharedPreferences.getString("lng", "en");
        Context language_context = LocaleHelper.setLocale(getContext(), lng);
        Resources resources = language_context.getResources();

        shimer = view.findViewById(R.id.shimer);
        deactivateTextview = view.findViewById(R.id.deactivate_textview);
        ll = view.findViewById(R.id.ll);
        circleImage = view.findViewById(R.id.circle_image);
        deactivateMessageTextview = view.findViewById(R.id.deactivate_message_textview);
        subsText = view.findViewById(R.id.subs_text);
        tblConatainer = view.findViewById(R.id.tbl_conatainer);
        toShow = view.findViewById(R.id.to_show);
        clic_cir=view.findViewById(R.id.clic_cir);
        in_km=view.findViewById(R.id.in_km);
        req = view.findViewById(R.id.req);
        no_one_speaking=view.findViewById(R.id.no_one_speaking);
        constraintLayout3 = view.findViewById(R.id.constraintLayout3);
        recylerView = view.findViewById(R.id.recyler_view);
        relativeLayout10 = view.findViewById(R.id.relativeLayout10);
        main = view.findViewById(R.id.main);
        if (sound_bite.parseLiveQueryClient != null) {
            sound_bite.parseLiveQueryClient.disconnect();
        }
        if (sound_bite.mRtcEngine != null) {
            sound_bite.mRtcEngine.leaveChannel();
        }
        RtcEngine.destroy();
        mRtcEventHandler = new IRtcEngineEventHandler() {
            @Override
            public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
                super.onJoinChannelSuccess(channel, uid, elapsed);
                Log.i("CHECK_AUDIO", "Joined");
            }

            @Override
            public void onLeaveChannel(RtcStats stats) {
                super.onLeaveChannel(stats);
                Log.i("CHECK_AUDIO", "Leaved");
            }

            @Override
            public void onUserJoined(int uid, int elapsed) {
                super.onUserJoined(uid, elapsed);
                Log.i("CHECK_AUDIO", "User Joined");
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
                Log.i("CHECK_AUDIO", "Offline");
            }

            @Override
            public void onAudioVolumeIndication(AudioVolumeInfo[] speakers, int totalVolume) {
                super.onAudioVolumeIndication(speakers, totalVolume);
                Log.i("CHECK_AUDIO", Integer.toString(totalVolume));
                //ParseUser.getCurrentUser().put("SOUND", totalVolume);
                //  ParseUser.getCurrentUser().saveInBackground();
            }

        };
        initializeEngine();
        join_table(0, getArguments().getString("ID"));

        ParseQuery<ParseObject> parseObjectParseQuery=new ParseQuery<ParseObject>("live_tables");
        parseObjectParseQuery.getInBackground(getArguments().getString("ID"), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {

                if (!object.getBoolean("ALLOW_RECORDING")) {
                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                            WindowManager.LayoutParams.FLAG_SECURE);
                }

                List<String> strings = ParseUser.getCurrentUser().getList("LAST_VISITED");
                if (!strings.contains(object.getObjectId())) {
                    strings.add(object.getObjectId());
                    ParseUser.getCurrentUser().put("LAST_VISITED", strings);
                    ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {


                        }
                    });
                }
                ParseGeoPoint currentUserLocation = new ParseGeoPoint(29.1967885, 78.9729364);
                double distance = currentUserLocation.distanceInKilometersTo(object.getParseGeoPoint("Location"));
                in_km.setText(String.format("%.2f", distance)+"KM");
                List<String> tuned_ins = object.getList("ONLINE");
                if (!tuned_ins.contains(ParseUser.getCurrentUser().getObjectId())) {

                    tuned_ins.add(ParseUser.getCurrentUser().getObjectId());
                    object.put("ONLINE", tuned_ins);
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
                                    new all_member_creator__special_speaker_adapter(objects, getContext(), object.getString("table_creator"), object.getObjectId(), getActivity());

                            recylerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
                            recylerView.setAdapter(all_member_creator__special_speaker_adapter);
                        }
                        else {
                            no_one_speaking.setVisibility(View.VISIBLE);
                            no_one_speaking.setText(resources.getString(R.string.No_ONE_SPEAKING));
                        }



                    }
                });

                try {
                    sound_bite.parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI(App.getLIVE()));
                } catch (URISyntaxException et) {
                    et.printStackTrace();
                }


                ParseQuery<ParseObject> last_OTHER = new ParseQuery<ParseObject>("live_tables");
                last_OTHER.whereEqualTo("objectId", object.getObjectId());

                SubscriptionHandling<ParseObject> subscriptionHandling_OTHER = sound_bite.parseLiveQueryClient.subscribe(last_OTHER);

                subscriptionHandling_OTHER.handleEvents(new SubscriptionHandling.HandleEventsCallback<ParseObject>() {
                    @Override
                    public void onEvents(ParseQuery<ParseObject> query, SubscriptionHandling.Event event, ParseObject objectp) {

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            public void run() {


                                ParseQuery<ParseObject> last_OTHER = new ParseQuery<ParseObject>("live_tables");
                                last_OTHER.getInBackground(object.getObjectId(), new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject object, ParseException e) {
                                        deactivateTextview.setText(objectp.getString("TALKING_TITLE"));
                                        subsText.setText(Integer.toString(objectp.getList("ONLINE").size()));
                                        ParseQuery<ParseUser> USER = ParseUser.getQuery();
                                        USER.whereContainedIn("objectId", objectp.getList("table_speakers"));
                                        USER.setLimit(20);
                                        USER.findInBackground(new FindCallback<ParseUser>() {
                                            @Override
                                            public void done(List<ParseUser> objects, ParseException e) {
                                                if(all_member_creator__special_speaker_adapter!=null){
                                                    all_member_creator__special_speaker_adapter.ref(objects);
                                                }

                                            }
                                        });

                                    }
                                });


                            }
                        });


                    }
                });

                clic_cir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getContext(), manage_notification.class);
                        intent.putExtra("ID",getArguments().getString("ID"));
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.bottom_up, R.anim.stationary);

                    }
                });

                subsText.setText(Integer.toString(object.getList("ONLINE").size()));
                try {

                    Glide.with(getContext().getApplicationContext()).asBitmap().load(object.getParseFile("table_image").getUrl())
                            .centerInside().placeholder(R.drawable.main_placeholder).thumbnail(0.3f).into(circleImage);

                }
                catch (NullPointerException ff){
                    ff.printStackTrace();
                }




                if(object.getString("TALKING_TITLE").trim().isEmpty()){
                    ll.setVisibility(View.GONE);
                }
                else {
                    ll.setVisibility(View.VISIBLE);
                    deactivateTextview.setText(object.getString("TALKING_TITLE"));
                }

                deactivateMessageTextview.setText(object.getString("table_name"));

                if(object.getList("table_subscribers").contains(ParseUser.getCurrentUser().getObjectId())){

                    req.setVisibility(View.GONE);
                }
                else {

                    req.setVisibility(View.VISIBLE);
                }



                PushDownAnim.setPushDownAnimTo(req).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ACProgressFlower dialog = new ACProgressFlower.Builder(getContext())
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
                        ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("roundtable_user_data");
                        parseObjectParseQuery.whereEqualTo("User_ID", ParseUser.getCurrentUser().getObjectId());
                        parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {

                                for (ParseObject parseObject : objects) {

                                    List<String> ss = parseObject.getList("Subscriptions");
                                    ss.add(object.getObjectId());
                                    parseObject.put("Subscriptions", ss);
                                    parseObject.saveInBackground();

                                }

                            }
                        });
                        List<String> REQUESTS = object.getList("table_subscribers");
                        REQUESTS.add(ParseUser.getCurrentUser().getObjectId());
                        object.put("table_subscribers", REQUESTS);

                        //NOTIFIcATIOn
                        List<String> NOTIFY = object.getList("NOTIFY");
                        NOTIFY.add(ParseUser.getCurrentUser().getObjectId());
                        object.put("NOTIFY", NOTIFY);

                        object.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {

                                List<String> tuned_ins = object.getList("ONLINE");
                                tuned_ins.remove(ParseUser.getCurrentUser().getObjectId());
                                object.put("ONLINE", tuned_ins);
                                object.saveInBackground();

                                if (sound_bite.parseLiveQueryClient != null) {
                                    sound_bite.parseLiveQueryClient.disconnect();
                                }
                                if (sound_bite.mRtcEngine != null) {
                                    sound_bite.mRtcEngine.leaveChannel();
                                }
                                RtcEngine.destroy();
                                getActivity().finish();
                                dialog.dismiss();
                                Toast.makeText(getContext(), "Subscribed", Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(getContext(), special_speaker_live_table.class);
                                intent1.putExtra("ID", object.getObjectId());
                                getContext().startActivity(intent1);
                                getActivity().overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
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

                shimer.stopShimmer();
                shimer.setVisibility(View.GONE);
                main.setVisibility(View.VISIBLE);
                tblConatainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getContext(), manage_notification.class);
                        intent.putExtra("ID", object.getObjectId());
                        getContext().startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.bottom_up, R.anim.stationary);

                    }
                });
            }
        });
       

        return view;
    }
    private void initializeEngine() {
        try {
            sound_bite.mRtcEngine = RtcEngine.create(getContext(), AUDIO_KEY, mRtcEventHandler);
        } catch (Exception e) {
            Log.e("CHECK_AUDIO", Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }

    public void join_table(int ROLE, String ID) {
        RtcTokenBuilder token = new RtcTokenBuilder();
        int timestamp = (int) (System.currentTimeMillis() / 1000 + 3600);
        String result = token.buildTokenWithUid(AUDIO_KEY, AUDIO_CERTIFICATE,
                ID, 0, RtcTokenBuilder.Role.Role_Publisher, timestamp);

        sound_bite.mRtcEngine.setClientRole(Constants.CLIENT_ROLE_AUDIENCE);

        sound_bite.mRtcEngine.setEnableSpeakerphone(true);

        sound_bite.mRtcEngine.enableDeepLearningDenoise(true);

        sound_bite.mRtcEngine.setAudioProfile(Constants.AUDIO_PROFILE_MUSIC_HIGH_QUALITY_STEREO, Constants.AUDIO_SCENARIO_SHOWROOM);

        sound_bite.mRtcEngine.setDefaultAudioRoutetoSpeakerphone(true);

        sound_bite.mRtcEngine.joinChannel(result, ID, "PS:)", 0);

        sound_bite.mRtcEngine.adjustRecordingSignalVolume(0);
    }
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}
