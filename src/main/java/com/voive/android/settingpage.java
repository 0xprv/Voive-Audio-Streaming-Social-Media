package com.voive.android;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bitvale.switcher.SwitcherX;
import com.bumptech.glide.Glide;
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog;
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;
import com.google.android.material.button.MaterialButton;
import com.jakewharton.processphoenix.ProcessPhoenix;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
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
import com.tapadoo.alerter.Alerter;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.thekhaeng.pushdownanim.PushDownAnim;
import com.xw.repo.widget.BounceScrollView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;


public class settingpage extends AppCompatActivity {

    @BindView(R.id.back2)
    MaterialButton back2;
    @BindView(R.id.privacypolicy)
    MaterialButton privacypolicy;
    @BindView(R.id.termsandcondition)
    MaterialButton termsandcondition;
    @BindView(R.id.communityguidlines)
    MaterialButton communityguidlines;
    @BindView(R.id.opensourcelibraries)
    MaterialButton opensourcelibraries;
    @BindView(R.id.logout)
    MaterialButton logout;

    SharedPreferences sharedPreferences1;
    @BindView(R.id.language)
    MaterialButton language;

    @BindView(R.id.notification)
    MaterialButton notification;
    @BindView(R.id.blocktable)
    MaterialButton blocktable;
    @BindView(R.id.disable_account)
    MaterialButton disableAccount;
    @BindView(R.id.bounceScrollView)
    BounceScrollView bounceScrollView;
    @BindView(R.id.helpcenter)
    MaterialButton helpcenter;
    @BindView(R.id.report_bug)
    MaterialButton reportBug;
    @BindView(R.id.notification_switch)
    SwitcherX notification_switch;
    @BindView(R.id.send_invites)
    MaterialButton sendInvites;
    @BindView(R.id.invitation_switch)
    SwitcherX invitationSwitch;
    @BindView(R.id.slum)
    TextView slum;

    @BindView(R.id.ONLINE)
    MaterialButton ONLINE_TEXT;
    @BindView(R.id.online_switch)
    SwitcherX ONLINE_SWITCH;
    @BindView(R.id.blog)
    MaterialButton BLOG;
    @BindView(R.id.private_listebibg_des)
    TextView private_listebibg_des;
    @BindView(R.id.notification_sett)
    MaterialButton notification_sett;
    @BindView(R.id.textView24)
    TextView textView24;
    @BindView(R.id.which_lang_title)
    TextView which_lang_title;
    @BindView(R.id.whihc_lang_des)
    TextView whihc_lang_des;
    @BindView(R.id.lng_spinner)
    MaterialSpinner lng_spinner;
    @BindView(R.id.invite_friends)
    MaterialButton invite_friends;
    @BindView(R.id.account_clickable)
    ConstraintLayout account_clickable;
    @BindView(R.id.invitation)
            MaterialButton invitation;
    @BindView(R.id.circleImageView3)
    CircleImageView circleImageView3;
    @BindView(R.id.textView12)
            TextView textView12;
    @BindView(R.id.textView16)
            TextView phone_no;
    @BindView(R.id.sync_contact)
            MaterialButton sync_contact;
    SlidrInterface slidrInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingpage);
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


        slidrInterface = Slidr.attach(this, config);

        SharedPreferences languagepref = getSharedPreferences("LANG", MODE_PRIVATE);
        String languagecode = languagepref.getString("lng", "en");
        boolean is_first_popup=languagepref.getBoolean("FIRST_POPUP_INVI",true);
        Context context = LocaleHelper.setLocale(settingpage.this, languagecode);
        Resources resources = context.getResources();

        language.setText(resources.getString(R.string.language));
        slum.setText(resources.getString(R.string.Allow_to_send_invite));

        disableAccount.setText(resources.getString(R.string.deactivate_account));
        notification.setText(resources.getString(R.string.Notification));
        language.setText(resources.getString(R.string.language));
        sync_contact.setText(resources.getString(R.string.Sync_Contacts));
        helpcenter.setText(resources.getString(R.string.help_center));
        sendInvites.setText(resources.getString(R.string.Invitation));
        ONLINE_TEXT.setText(resources.getString(R.string.PP_Listening));
        textView24.setText(resources.getString(R.string.Setting));
        which_lang_title.setText(resources.getString(R.string.NSFW_CONV));
        whihc_lang_des.setText(resources.getString(R.string.which_lng_des));
        blocktable.setText(resources.getString(R.string.manageblocktable));
        private_listebibg_des.setText(resources.getString(R.string.private_listening));
        privacypolicy.setText(resources.getString(R.string.Privacy));
        termsandcondition.setText(resources.getString(R.string.Terms));
        communityguidlines.setText(resources.getString(R.string.comunityguid));
        notification_sett.setText(resources.getString(R.string.notification_setting));
        opensourcelibraries.setText(resources.getString(R.string.opensource));
        reportBug.setText(resources.getString(R.string.report_a_bug));
        logout.setText(resources.getString(R.string.logout));


        PushDownAnim.setPushDownAnimTo(notification_sett).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               openNotificationSettings();
            }
        });

        PushDownAnim.setPushDownAnimTo(helpcenter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browserIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://deeply-satellite-088.notion.site/Voicedesk-Help-Center-c0e2ce8cb7a7457d8aeb456763ab0269"));
                startActivity(browserIntent2);
            }
        });
        PushDownAnim.setPushDownAnimTo(BLOG).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://voicedeskapp.com/blog"));
                startActivity(browserIntent2);
                overridePendingTransition(R.anim.slide_from_end, R.anim.stationary);

            }
        });
        PushDownAnim.setPushDownAnimTo(sync_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(settingpage.this, from_conrtact_invite.class);
                startActivity(intent);
                overridePendingTransition(R.anim.bottom_up, R.anim.stationary);

            }
        });
        PushDownAnim.setPushDownAnimTo(invitation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(settingpage.this, my_invitation_activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.bottom_up, R.anim.stationary);

            }
        });
        PushDownAnim.setPushDownAnimTo(blocktable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(settingpage.this, blocked_tables.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_end, R.anim.stationary);

            }
        });


        PushDownAnim.setPushDownAnimTo(reportBug).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://airtable.com/shr82ARsERecCACU9"));
                startActivity(browserIntent2);
                overridePendingTransition(R.anim.slide_from_end, R.anim.stationary);

            }
        });
        account_clickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(settingpage.this, my_own_account.class);
                startActivity(intent);
                overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
            }
        });
        PushDownAnim.setPushDownAnimTo(invite_friends).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RoundedBottomSheetDialog roundedBottomSheetDialog = new RoundedBottomSheetDialog(settingpage.this);

                View view = getLayoutInflater().inflate(R.layout.bottomsheet_comment_layout, null, false);
                roundedBottomSheetDialog.setContentView(view);

                SharedPreferences sharedPreferences_lan = getSharedPreferences("LANG", Context.MODE_PRIVATE);
                String lng = sharedPreferences_lan.getString("lng", "en");
                Context language_context = LocaleHelper.setLocale(settingpage.this, lng);
                Resources resources = language_context.getResources();

                LinearLayout twitter = view.findViewById(R.id.lr1);
                LinearLayout whtsapp = view.findViewById(R.id.lr2);
                LinearLayout copy = view.findViewById(R.id.lr3);
                LinearLayout msg = view.findViewById(R.id.lr4);
                LinearLayout more = view.findViewById(R.id.lr5);


                PushDownAnim.setPushDownAnimTo(copy).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText("Sneak Link", "सुने मेरी "+ParseUser.getCurrentUser().getString("First_and_last_name")+" और अन्य भारतीय लोगो की लाइव बाते वोइवे पर। एक" +
                                " ऐसे प्लेटफार्म जो भारत में निर्मित। है -> इनमे से किसी भी लिंक पर क्लिक करे https://play.google.com/store/apps/details?id=com.voive.android या https://voive.in");
                        clipboardManager.setPrimaryClip(clipData);
                        Toast.makeText(settingpage.this, "Link Copied", Toast.LENGTH_SHORT).show();
                        roundedBottomSheetDialog.dismiss();

                    }
                });
                PushDownAnim.setPushDownAnimTo(whtsapp).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                        whatsappIntent.setType("text/plain");
                        whatsappIntent.setPackage("com.whatsapp");
                        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "सुने मेरी "+ParseUser.getCurrentUser().getString("First_and_last_name")+" और अन्य भारतीय लोगो की लाइव बाते वोइवे पर। एक ऐसे" +
                                " प्लेटफार्म जो भारत में निर्मित। है -> इनमे से किसी भी लिंक पर क्लिक करे https://play.google.com/store/apps/details?id=com.voive.android या https://voive.in");
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
                        shareTwitter("सुने मेरी "+ParseUser.getCurrentUser().getString("First_and_last_name")+" और अन्य भारतीय लोगो की लाइव बाते वोइवे पर। एक ऐसे प्लेटफार्म जो भारत में निर्मित। है -> इनमे से किसी भी" +
                                " लिंक पर क्लिक करे https://play.google.com/store/apps/details?id=com.voive.android या https://voive.in");

                    }
                });
                PushDownAnim.setPushDownAnimTo(more).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        /*This will be the actual content you wish you share.*/
                        String shareBody = "\"सुने मेरी \"+ParseUser.getCurrentUser().getString(\"First_and_last_name\")+\" और अन्य भारतीय लोगो की लाइव बाते वोइवे पर। एक ऐसे प्लेटफार्म जो भारत में निर्मित। है -> इनमे से किसी भी लिंक" +
                                " पर क्लिक करे https://play.google.com/store/apps/details?id=com.voive.android या https://voive.in\"";
                        /*The type of the content is text, obviously.*/
                        intent.setType("text/plain");
                        /*Applying information Subject and Body.*/
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Share Bite Of Conversation");
                        intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                        /*Fire!*/
                        startActivity(Intent.createChooser(intent, "Share Via"));
                    }
                });
                roundedBottomSheetDialog.show();

            }
        });
        PushDownAnim.setPushDownAnimTo(disableAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(settingpage.this);
                builder.setView(R.layout.deactivate_account_alert_dialog);

                AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                alertDialog.show();

                TextView deactivate_textview = alertDialog.findViewById(R.id.deactivate_textview);
                TextView d_message = alertDialog.findViewById(R.id.deactivate_message_textview);

                MaterialButton d = alertDialog.findViewById(R.id.deactivate);
                MaterialButton n = alertDialog.findViewById(R.id.no);

                PushDownAnim.setPushDownAnimTo(d).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ACProgressFlower dialog = new ACProgressFlower.Builder(settingpage.this)
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
                        ParseUser.getCurrentUser().put("Activate", false);
                        ParseUser.getCurrentUser().saveInBackground();
                        ParseUser.logOutInBackground(new LogOutCallback() {
                            @Override
                            public void done(ParseException e) {


                                if (e == null) {

                                    ProcessPhoenix.triggerRebirth(context.getApplicationContext());
                                    if (special_speaker_live_table.mRtcEngine != null) {

                                        special_speaker_live_table.mRtcEngine.leaveChannel();
                                    }
                                } else {

                                    Toast.makeText(settingpage.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                }

                            }
                        });


                    }
                });

                PushDownAnim.setPushDownAnimTo(n).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();

                    }
                });

                deactivate_textview.setText(resources.getString(R.string.deactivate_account));
                d_message.setText(resources.getString(R.string.are_you_sure_to_deactivate));


                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
                layoutParams.width = 500;
                alertDialog.getWindow().setAttributes(layoutParams);


            }
        });


        MaterialButton from_contact=findViewById(R.id.from_contact);
        from_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RoundedBottomSheetDialog roundedBottomSheetDialog = new RoundedBottomSheetDialog(settingpage.this);

                View view = getLayoutInflater().inflate(R.layout.bottomsheet_comment_layout, null, false);
                roundedBottomSheetDialog.setContentView(view);

                SharedPreferences sharedPreferences_lan = getSharedPreferences("LANG", Context.MODE_PRIVATE);
                String lng = sharedPreferences_lan.getString("lng", "en");
                Context language_context = LocaleHelper.setLocale(settingpage.this, lng);
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
                        sendIntent.putExtra("sms_body", "सुने मेरी "+ParseUser.getCurrentUser().getString("First_and_last_name")+" और अन्य भारतीय लोगो की लाइव बाते वोइवे पर। एक" +
                                " ऐसे प्लेटफार्म जो भारत में निर्मित। है -> इनमे से किसी भी लिंक पर क्लिक करे https://play.google.com/store/apps/details?id=com.voive.android या https://voive.in");
                        startActivity(sendIntent);

                    }
                });

                PushDownAnim.setPushDownAnimTo(copy).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText("Link", "सुने मेरी "+ParseUser.getCurrentUser().getString("First_and_last_name")+" और अन्य भारतीय लोगो की लाइव बाते वोइवे पर। एक" +
                                " ऐसे प्लेटफार्म जो भारत में निर्मित। है -> इनमे से किसी भी लिंक पर क्लिक करे https://play.google.com/store/apps/details?id=com.voive.android या https://voive.in");
                        clipboardManager.setPrimaryClip(clipData);
                        Toast.makeText(settingpage.this, "Link Copied", Toast.LENGTH_SHORT).show();
                        roundedBottomSheetDialog.dismiss();

                    }
                });
                PushDownAnim.setPushDownAnimTo(whtsapp).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                        whatsappIntent.setType("text/plain");
                        whatsappIntent.setPackage("com.whatsapp");
                        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "सुने मेरी "+ParseUser.getCurrentUser().getString("First_and_last_name")+" और अन्य भारतीय लोगो की लाइव बाते वोइवे पर। एक ऐसे" +
                                " प्लेटफार्म जो भारत में निर्मित। है -> इनमे से किसी भी लिंक पर क्लिक करे https://play.google.com/store/apps/details?id=com.voive.android या https://voive.in");
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
                        shareTwitter("सुने मेरी "+ParseUser.getCurrentUser().getString("First_and_last_name")+" और अन्य भारतीय लोगो की लाइव बाते वोइवे पर। एक ऐसे प्लेटफार्म जो भारत में निर्मित। है -> इनमे से किसी भी" +
                                " लिंक पर क्लिक करे https://play.google.com/store/apps/details?id=com.voive.android या https://voive.in");

                    }
                });
                PushDownAnim.setPushDownAnimTo(more).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        /*This will be the actual content you wish you share.*/
                        String shareBody = "\"सुने मेरी \"+ParseUser.getCurrentUser().getString(\"First_and_last_name\")+\" और अन्य भारतीय लोगो की लाइव बाते वोइवे पर। एक ऐसे प्लेटफार्म जो भारत में निर्मित। है -> इनमे से किसी भी लिंक" +
                                " पर क्लिक करे https://play.google.com/store/apps/details?id=com.voive.android या https://voive.in\"";
                        /*The type of the content is text, obviously.*/
                        intent.setType("text/plain");
                        /*Applying information Subject and Body.*/
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Share Bite Of Conversation");
                        intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                        /*Fire!*/
                        startActivity(Intent.createChooser(intent, "Share Via"));
                    }
                });
                roundedBottomSheetDialog.show();

            }
        });
        phone_no.setText(ParseUser.getCurrentUser().getString("PHONE_NO"));
        TextView tt=findViewById(R.id.vveerr);
        tt.setText("Voive VBeta-1.0.0 "+System.getProperty("os.arch"));
        List<String> ss=new ArrayList<>();
        ss.add("Hindi");
        ss.add("Marathi");
        ss.add("Tamil");
        ss.add("Telugu");
        ss.add("Gujarati");
        ss.add("Punjabi");
        ss.add("English");
        lng_spinner.setSelectedIndex(ss.indexOf(ParseUser.getCurrentUser().getString("PREF")));
        lng_spinner.setItems(ss);
        lng_spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                ParseUser.getCurrentUser().put("PREF", item);
                ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(settingpage.this, "Saved", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
        Glide.with(settingpage.this.getApplicationContext()).asBitmap().load(ParseUser.getCurrentUser().getParseFile("speaker_profile_pic").getUrl()).circleCrop()
                .into(circleImageView3);
        textView12.setText(ParseUser.getCurrentUser().getString("First_and_last_name"));


        ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("roundtable_user_data");
        parseObjectParseQuery.whereEqualTo("User_ID", ParseUser.getCurrentUser().getObjectId());
        parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                for(ParseObject parseObject:objects){
                    List<String> inv=parseObject.getList("Invitations");

                    if(inv.size()>0){
                        invitation.setVisibility(View.VISIBLE);
                        if(is_first_popup){
                            final Typeface typeface = ResourcesCompat.getFont(settingpage.this, R.font.montr_semi);
                            Balloon balloon = new Balloon.Builder(settingpage.this.getApplicationContext())
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
                                    .setText(resources.getString(R.string.tap_up_to_see_inv))
                                    .setTextColor(resources.getColor(R.color.black))
                                    .setBackgroundColor(resources.getColor(R.color.colorAccent))
                                    .setBalloonAnimation(BalloonAnimation.FADE)
                                    .build();

                            balloon.setOnBalloonDismissListener(new OnBalloonDismissListener() {
                                @Override
                                public void onBalloonDismiss() {

                                    SharedPreferences.Editor editor = languagepref.edit();
                                    editor.putBoolean("FIRST_POPUP_INVI", false);
                                    editor.apply();

                                }
                            });
                            balloon.dismissWithDelay(20000L);
                            balloon.showAlignBottom(invitation);
                        }
                    }
                    else {
                        invitation.setVisibility(View.GONE);
                    }
                }


            }
        });

        ONLINE_SWITCH.setChecked(ParseUser.getCurrentUser().getBoolean("PRIVATE_LISTENING"), true);
        invite_friends.setText(resources.getString(R.string.invite_peoples));
        notification_switch.setChecked(ParseUser.getCurrentUser().getBoolean("NOTIFICATION_UPDATES"), true);

        invitationSwitch.setChecked(ParseUser.getCurrentUser().getBoolean("ALLOW_INVITES"), true);

        invitationSwitch.setOnCheckedChangeListener(checked ->
        {
            invitationSwitch.setChecked(checked, true);


            ParseUser.getCurrentUser().put("ALLOW_INVITES", checked);
            ParseUser.getCurrentUser().saveInBackground();

            Toast.makeText(settingpage.this, "Saved", Toast.LENGTH_SHORT).show();
            return Unit.INSTANCE;
        });

        notification_switch.setOnCheckedChangeListener(checked ->
        {
            notification_switch.setChecked(checked, true);


            ParseUser.getCurrentUser().put("NOTIFICATION_UPDATES", checked);
            ParseUser.getCurrentUser().saveInBackground();

            Toast.makeText(settingpage.this, "Saved", Toast.LENGTH_SHORT).show();
            return Unit.INSTANCE;
        });

        ONLINE_SWITCH.setOnCheckedChangeListener(checked ->
        {
            ONLINE_SWITCH.setChecked(checked, true);


            ParseUser.getCurrentUser().put("PRIVATE_LISTENING", checked);
            ParseUser.getCurrentUser().saveInBackground();

            Toast.makeText(settingpage.this, "Saved", Toast.LENGTH_SHORT).show();
            return Unit.INSTANCE;
        });


        opensourcelibraries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OssLicensesMenuActivity.setActivityTitle(settingpage.this.getResources().getString(R.string.opensource));
                startActivity(new Intent(settingpage.this, OssLicensesMenuActivity.class));


            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder1 = new AlertDialog.Builder(settingpage.this, R.style.AlertDialogCustom);
                builder1.setMessage(resources.getString(R.string.logoutconformation));
                builder1.setTitle(resources.getString(R.string.logout));
                builder1.setCancelable(false);

                builder1.setPositiveButton(
                        resources.getString(R.string.YES),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                ParseUser.logOutInBackground(new LogOutCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        ProcessPhoenix.triggerRebirth(context.getApplicationContext());
                                        Toast.makeText(settingpage.this, "Sign Out", Toast.LENGTH_SHORT).show();

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
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(settingpage.this, change_language.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_end, R.anim.stationary);

            }
        });


    }

    @OnClick(R.id.back2)
    public void onViewClicked() {
        finish();
        overridePendingTransition(R.anim.stationary, R.anim.bottom_down);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.bottom_down);
    }

    @OnClick({R.id.privacypolicy, R.id.termsandcondition, R.id.communityguidlines})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.privacypolicy:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.notion.so/Privacy-Policy-d5ebe9a6f2e540d9afcf42e6d2ce65f0"));
                startActivity(browserIntent);
                break;
            case R.id.termsandcondition:
                Intent browserIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.notion.so/Terms-And-Condition-a3b0e186120e4193b63130f7a8e1c033"));
                startActivity(browserIntent2);
                break;
            case R.id.communityguidlines:
                Intent browserIntent3 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.notion.so/Community-Guidelines-3c03fba4d42e4767b8f746c7ee6bd7b6"));
                startActivity(browserIntent3);
                break;
        }
    }
    private void openNotificationSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            startActivity(intent);
        } else {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }
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
