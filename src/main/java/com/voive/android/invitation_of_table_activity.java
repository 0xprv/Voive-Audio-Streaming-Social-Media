package com.voive.android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class invitation_of_table_activity extends AppCompatActivity {


    @BindView(R.id.cover_image)
    CircleImageView cover_image;
    @BindView(R.id.table_name)
    TextView table_name;
    @BindView(R.id.acceept_invitation)
    MaterialButton acceeptInvitation;
    @BindView(R.id.reject_invitation)
    MaterialButton rejectInvitation;

    @BindView(R.id.MAIN_CONT)
    CardView MAINCONT;

    @BindView(R.id.close)
    MaterialButton close;

    String GLOBAL_ID = "";
    @BindView(R.id.gradient)
    RelativeLayout gradient;
    @BindView(R.id.shimmer)
    ShimmerFrameLayout shimmer;
    @BindView(R.id.table_sub)
    TextView table_sub;

    public static int manipulateColor(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r, 255),
                Math.min(g, 255),
                Math.min(b, 255));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_invitation_of_table);

        GLOBAL_ID = getIntent().getStringExtra("ID");

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
        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng = sharedPreferences.getString("lng", "en");
        Context language_context = LocaleHelper.setLocale(this, lng);
        Resources resources = language_context.getResources();
        //   inviteTextview.setText(resources.getString(R.string.Invitation));
        acceeptInvitation.setText(resources.getString(R.string.ACCEPT));


        ParseQuery<ParseObject> COUNT = new ParseQuery<ParseObject>("live_tables");
        COUNT.whereEqualTo("objectId", GLOBAL_ID);
        COUNT.countInBackground(new CountCallback() {
            @Override
            public void done(int count, ParseException e) {


                if (count > 0) {

                    MAINCONT.setVisibility(View.VISIBLE);

                    ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("live_tables");
                    parseObjectParseQuery.getInBackground(GLOBAL_ID, new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {


                            table_name.setText(object.getString("table_name"));

                            Glide.with(invitation_of_table_activity.this.getApplicationContext()).asBitmap().load(object.getParseFile("table_image").getUrl())
                                    .centerCrop().into(cover_image);

                            shimmer.stopShimmer();
                            shimmer.setVisibility(View.GONE);
                            gradient.setVisibility(View.VISIBLE);


                            table_sub.setText(object.getList("table_subscribers").size() +" "+resources.getString(R.string.Subscribers));
                            PushDownAnim.setPushDownAnimTo(acceeptInvitation).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    ParseQuery<ParseObject> parseObjectParseQuery1 = new ParseQuery<ParseObject>("Notifications");
                                    parseObjectParseQuery1.whereEqualTo("USER_ID", ParseUser.getCurrentUser().getObjectId());
                                    parseObjectParseQuery1.whereEqualTo("TYPE", Constant.SOME_ONE_SEND_INVITATION);
                                    parseObjectParseQuery1.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> objects, ParseException e) {

                                            for(ParseObject pp:objects){
                                                pp.deleteInBackground();
                                            }
                                        }
                                    });

                                    if (object.getBoolean("Active")) {
                                        if (object.getList("BANS").contains(ParseUser.getCurrentUser().getObjectId())) {

                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(invitation_of_table_activity.this, R.style.AlertDialogCustom);
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
                                            alert11.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                                            alert11.show();


                                        }
                                        else {



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

                                            List<String> Subscribers = new ArrayList<String>();
                                            Subscribers = object.getList("table_subscribers");
                                            if(!Subscribers.contains(ParseUser.getCurrentUser().getObjectId())){
                                                Subscribers.add(ParseUser.getCurrentUser().getObjectId());
                                                object.put("table_subscribers", Subscribers);

                                            }

                                            List<String> NOTIFY = new ArrayList<String>();
                                            NOTIFY = object.getList("NOTIFY");
                                            NOTIFY.add(ParseUser.getCurrentUser().getObjectId());

                                            object.put("NOTIFY", NOTIFY);

                                            object.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
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

                                                    ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("roundtable_user_data");
                                                    parseObjectParseQuery.whereEqualTo("User_ID", ParseUser.getCurrentUser().getObjectId());
                                                    parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                                                        @Override
                                                        public void done(List<ParseObject> objects, ParseException e) {


                                                            for (ParseObject parseObject : objects) {


                                                                List<String> INVITES = parseObject.getList("Invitations");
                                                                parseObject.remove(GLOBAL_ID);
                                                                parseObject.put("Invitations", INVITES);
                                                                parseObject.saveInBackground(new SaveCallback() {
                                                                    @Override
                                                                    public void done(ParseException e) {


                                                                        Intent intent = new Intent(invitation_of_table_activity.this, special_speaker_live_table.class);
                                                                        intent.putExtra("ID", object.getObjectId());
                                                                        startActivity(intent);
                                                                        overridePendingTransition(R.anim.slide_from_end, R.anim.stationary);
                                                                        finish();
                                                                    }
                                                                });

                                                            }

                                                        }
                                                    });

                                                }
                                            });

                                        }

                                    }
                                    else {

                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(invitation_of_table_activity.this, R.style.AlertDialogCustom);
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
                                        alert11.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                                        alert11.show();

                                    }

                                }
                            });
                            PushDownAnim.setPushDownAnimTo(rejectInvitation).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ParseQuery<ParseObject> parseObjectParseQuery1 = new ParseQuery<ParseObject>("Notifications");
                                    parseObjectParseQuery1.whereEqualTo("USER_ID", ParseUser.getCurrentUser().getObjectId());
                                    parseObjectParseQuery1.whereEqualTo("TYPE", Constant.SOME_ONE_SEND_INVITATION);
                                    parseObjectParseQuery1.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> objects, ParseException e) {

                                            for(ParseObject pp:objects){
                                                pp.deleteInBackground();
                                            }
                                        }
                                    });

                                    ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("roundtable_user_data");
                                    parseObjectParseQuery.whereEqualTo("User_ID", ParseUser.getCurrentUser().getObjectId());
                                    parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> objects, ParseException e) {


                                            for (ParseObject parseObject : objects) {


                                                List<String> INVITES = parseObject.getList("Invitations");
                                                parseObject.remove(GLOBAL_ID);
                                                parseObject.put("Invitations", INVITES);
                                                parseObject.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {

                                                        overridePendingTransition(R.anim.stationary, R.anim.bottom_down);
                                                        Toast.makeText(invitation_of_table_activity.this, "Rejected", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }
                                                });

                                            }

                                        }
                                    });


                                }
                            });

                        }
                    });
                }
                else {


                    MAINCONT.setVisibility(View.INVISIBLE);
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(invitation_of_table_activity.this, R.style.AlertDialogCustom);
                    builder1.setMessage(resources.getString(R.string.invitation_is_broken));
                    builder1.setTitle(resources.getString(R.string.INVALID_INVITATION));
                    builder1.setCancelable(false);

                    builder1.setPositiveButton(
                            resources.getString(R.string.OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });


                    AlertDialog alert11 = builder1.create();
                    alert11.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    alert11.show();

                }
            }
        });

        PushDownAnim.setPushDownAnimTo(close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> parseObjectParseQuery1 = new ParseQuery<ParseObject>("Notifications");
                parseObjectParseQuery1.whereEqualTo("USER_ID", ParseUser.getCurrentUser().getObjectId());
                parseObjectParseQuery1.whereEqualTo("TYPE", Constant.SOME_ONE_SEND_INVITATION);
                parseObjectParseQuery1.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {

                        for(ParseObject pp:objects){
                            pp.deleteInBackground();
                        }
                    }
                });

                finish();
                overridePendingTransition(R.anim.stationary, R.anim.bottom_down);
            }
        });

    }

    @Override
    public void finish() {
        ParseQuery<ParseObject> parseObjectParseQuery1 = new ParseQuery<ParseObject>("Notifications");
        parseObjectParseQuery1.whereEqualTo("USER_ID", ParseUser.getCurrentUser().getObjectId());
        parseObjectParseQuery1.whereEqualTo("TYPE", Constant.SOME_ONE_SEND_INVITATION);
        parseObjectParseQuery1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                for(ParseObject pp:objects){
                    pp.deleteInBackground();
                }
            }
        });

        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.bottom_down);
    }


}