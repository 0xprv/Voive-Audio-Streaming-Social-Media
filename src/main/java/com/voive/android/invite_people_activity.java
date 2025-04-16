package com.voive.android;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.model.SlidrPosition;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class invite_people_activity extends AppCompatActivity {

    @BindView(R.id.close)
    MaterialButton close;
    @BindView(R.id.invite_textview)
    TextView inviteTextview;
    @BindView(R.id.editText3)
    EditText editText3;
    @BindView(R.id.recyler_view)
    RecyclerView recylerView;
    @BindView(R.id.share)
    MaterialButton share;
    @BindView(R.id.login2)
    MaterialButton s_inviaion;


    public static List<String> ss = new ArrayList<>();
    invite_people_adapter invite_people_adapter;
    @BindView(R.id.qr)
    MaterialButton qr;
    @BindView(R.id.s_conv)
    TextView sConv;
    @BindView(R.id.part_of_them)
    TextView partOfThem;
    @BindView(R.id.if_not_search)
    LinearLayout ifNotSearch;
    @BindView(R.id.how_many)
    TextView how_many;
    @BindView(R.id.when_to_sent)
    RelativeLayout when_to_sent;
    private List<ParseUser> pp = new ArrayList<>();
    int SKIP = 0;
    Handler handler = new Handler();
    SlidrInterface slidrInterface;
    String GLOBAL_ID;
    String NAME_TABLE;
    Resources resources;
    List<String> not_to_show=new ArrayList<>();
    Palette.Swatch swatchX;
     Window window;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_people_activity);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
       window = getWindow();
       // window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

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

        ss.clear();
        GLOBAL_ID=getIntent().getStringExtra("ID");
        slidrInterface = Slidr.attach(this, config);
        SharedPreferences languagepref = getSharedPreferences("LANG", MODE_PRIVATE);
        String languagecode = languagepref.getString("lng", "en");
        Context context = LocaleHelper.setLocale(this, languagecode);
         resources = context.getResources();
        inviteTextview.setText(resources.getString(R.string.invite_peoples));
        sConv.setText(resources.getString(R.string.Search_for_peoples));
        partOfThem.setText(resources.getString(R.string.and_call_in_table));
        s_inviaion.setText(resources.getString(R.string.send));
        editText3.setHint(resources.getString(R.string.Search));

        ParseQuery<ParseObject> ppxx=ParseQuery.getQuery("live_tables");
        ppxx.getInBackground(GLOBAL_ID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {


                Glide.with(invite_people_activity.this).asBitmap().load(object.getParseFile("table_image").getUrl()).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                        Palette.generateAsync(resource, new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(@Nullable Palette palette) {

                                Palette.Swatch swatch = palette.getLightVibrantSwatch() != null
                                        ? palette.getLightVibrantSwatch() : palette.getDominantSwatch();

                                swatchX=swatch;

                            }
                        });
                    }
                });
                NAME_TABLE=object.getString("table_name");
                not_to_show=object.getList("table_subscribers");
            }
        });
        PushDownAnim.setPushDownAnimTo(qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(invite_people_activity.this, QR_OF_TABLE_SHARE.class);
                intent.putExtra("ID", GLOBAL_ID);
                startActivity(intent);
                overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
            }
        });


        editText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!s.toString().trim().isEmpty()) {
                    recylerView.setVisibility(View.VISIBLE);
                    ParseQuery<ParseUser> parseUserParseQuery = ParseUser.getQuery();
                    parseUserParseQuery.whereContains("username", s.toString());

                    ParseQuery<ParseUser> parseUserParseQuery1 = ParseUser.getQuery();
                    parseUserParseQuery1.whereContains("First_and_last_name", s.toString());

                    List<ParseQuery<ParseUser>> parseQueries = new ArrayList<>();
                    parseQueries.add(parseUserParseQuery);
                    parseQueries.add(parseUserParseQuery1);


                    ParseQuery<ParseUser> fn = ParseQuery.or(parseQueries);
                    fn.whereNotContainedIn("objectId",not_to_show);
                    fn.setLimit(30);
                    fn.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {

                            if (objects.size() > 0) {
                                ifNotSearch.setVisibility(View.GONE);
                                invite_people_adapter = new invite_people_adapter(objects, invite_people_activity.this, GLOBAL_ID);
                                recylerView.setLayoutManager(new LinearLayoutManager(invite_people_activity.this, LinearLayoutManager.VERTICAL, false));
                                recylerView.setAdapter(invite_people_adapter);
                            } else {
                                recylerView.setLayoutManager(new LinearLayoutManager(invite_people_activity.this, LinearLayoutManager.VERTICAL, false));
                                recylerView.setAdapter(new if_not_found_adapter(context,"","No People Found",0));
                                ifNotSearch.setVisibility(View.GONE);

                            }


                        }
                    });


                } else {
                    ss.clear();
                    sConv.setText(resources.getString(R.string.Search_for_peoples));
                    partOfThem.setText(resources.getString(R.string.and_call_in_table));
                    ifNotSearch.setVisibility(View.VISIBLE);
                    recylerView.setVisibility(View.INVISIBLE);
                    recylerView.setAdapter(null);

                }
            }
        });

        PushDownAnim.setPushDownAnimTo(share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String main_string="https://voive.in/tables/invites/?id=" + GLOBAL_ID;
                RoundedBottomSheetDialog roundedBottomSheetDialog = new RoundedBottomSheetDialog(invite_people_activity.this);

                View view = LayoutInflater.from(invite_people_activity.this).inflate(R.layout.bottomsheet_comment_layout, null, false);
                roundedBottomSheetDialog.setContentView(view);

                SharedPreferences sharedPreferences_lan = getSharedPreferences("LANG", Context.MODE_PRIVATE);
                String lng = sharedPreferences_lan.getString("lng", "en");
                Context language_context = LocaleHelper.setLocale(invite_people_activity.this, lng);
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
                        sendIntent.putExtra("sms_body", "सुनने "+NAME_TABLE+ " में क्या बात चित हो रही है। - " + main_string);
                        startActivity(sendIntent);

                    }
                });
                PushDownAnim.setPushDownAnimTo(copy).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText("Sneak Link", "सुनने "+NAME_TABLE+ " में क्या बात चित हो रही है। - " + main_string);
                        clipboardManager.setPrimaryClip(clipData);
                        Toast.makeText(invite_people_activity.this, "Link Copied", Toast.LENGTH_SHORT).show();
                        roundedBottomSheetDialog.dismiss();

                    }
                });
                PushDownAnim.setPushDownAnimTo(whtsapp).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                        whatsappIntent.setType("text/plain");
                        whatsappIntent.setPackage("com.whatsapp");
                        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "सुनने "+NAME_TABLE+ " में क्या बात चित हो रही है। - " + main_string);
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
                        shareTwitter("सुनने "+NAME_TABLE+ " में क्या बात चित हो रही है। - " + main_string);

                    }
                });
                PushDownAnim.setPushDownAnimTo(more).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Share Conversation");
                        intent.putExtra(Intent.EXTRA_TEXT, "सुनने "+NAME_TABLE+ " में क्या बात चित हो रही है। - " + main_string);
                        startActivity(Intent.createChooser(intent, "Share Via"));
                    }
                });
                roundedBottomSheetDialog.show();




            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.stationary, R.anim.bottom_down);
            }
        });

        PushDownAnim.setPushDownAnimTo(s_inviaion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ACProgressFlower dialog = new ACProgressFlower.Builder(invite_people_activity.this)
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

                if (ss.size() > 0) {

                    for (String NAME : ss) {

                        ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("roundtable_user_data");
                        parseObjectParseQuery.whereEqualTo("User_ID", NAME);
                        parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objectsT, ParseException e) {

                                for (ParseObject parseObject : objectsT) {

                                    List<String> INVITES = parseObject.getList("Invitations");

                                    if (!INVITES.contains(GLOBAL_ID)) {

                                        INVITES.add(GLOBAL_ID);
                                        parseObject.put("Invitations", INVITES);
                                        parseObject.saveInBackground();


                                        ParseQuery<ParseInstallation> parseInstallationParseQuery = ParseInstallation.getQuery();
                                        parseInstallationParseQuery.whereEqualTo("UserId", NAME);
                                        parseInstallationParseQuery.findInBackground(new FindCallback<ParseInstallation>() {
                                            @Override
                                            public void done(List<ParseInstallation> objects, ParseException e) {


                                                JSONObject data = new JSONObject();
// Put data in the JSON object
                                                try {
                                                    data.put("alert", ParseUser.getCurrentUser().getString("First_and_last_name")
                                                            + " Send You A Invitation in "+NAME_TABLE+". Tap To Listen Live Conversation.");
                                                    data.put("title", getIntent().getStringExtra("NAME"));
                                                    data.put("Id", GLOBAL_ID);
                                                    data.put("Type", Constant.SOME_ONE_SEND_INVITATION);
                                                } catch (JSONException eT) {

                                                    Log.i("ERROR", eT.getLocalizedMessage());
                                                    // should not happen
                                                }
// Configure the push
                                                ParsePush push = new ParsePush();
                                                push.setQuery(parseInstallationParseQuery);
                                                push.setData(data);
                                                push.sendInBackground();


                                            }
                                        });

                                        notification_modal notification_modal = new notification_modal();
                                        notification_modal.setTYPE(Constant.SOME_ONE_SEND_INVITATION);
                                        notification_modal.setREAD(false);
                                        notification_modal.setTABLE(GLOBAL_ID);
                                        notification_modal.setSENDER(ParseUser.getCurrentUser().getObjectId());
                                        notification_modal.setRECEIVER(NAME);
                                        notification_modal.saveInBackground();
                                    }


                                }


                            }
                        });

                    }

                    dialog.dismiss();
                    finish();
                    Toast.makeText(invite_people_activity.this, "Invitation Sent", Toast.LENGTH_SHORT).show();

                } else {
                    dialog.dismiss();
                    Toast.makeText(invite_people_activity.this, "Please Select Someone To Send Invitation", Toast.LENGTH_SHORT).show();

                }

            }
        });

        runnable.run();

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            if (ss.size()==0) {

                when_to_sent.setVisibility(View.GONE);
                window.setNavigationBarColor(getResources().getColor(R.color.colorPrimary));

            } else {
                when_to_sent.setVisibility(View.VISIBLE);
                how_many.setText(Integer.toString(ss.size())+" "+resources.getString(R.string.Invitation));

                when_to_sent.setBackgroundColor(swatchX.getRgb());
                s_inviaion.setIconTint(ColorStateList.valueOf(swatchX.getRgb()));
                s_inviaion.setTextColor(swatchX.getRgb());
                how_many.setTextColor(swatchX.getBodyTextColor());
                if(isColorDark(swatchX.getRgb())){
                    s_inviaion.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                }
                else {
                    s_inviaion.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                }
                 window.setNavigationBarColor(swatchX.getRgb());
            }

            handler.postDelayed(this, 1000);
        }
    };
    public boolean isColorDark(int color){
        double darkness = 1-(0.299*Color.red(color) + 0.587*Color.green(color) + 0.114*Color.blue(color))/255;
        if(darkness<0.5){
            return false; // It's a light color
        }else{
            return true; // It's a dark color
        }
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.bottom_down);
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
}
