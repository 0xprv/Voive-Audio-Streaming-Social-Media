package com.voive.android;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.model.SlidrPosition;
import com.tapadoo.alerter.Alerter;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class from_conrtact_invite extends AppCompatActivity {

    @BindView(R.id.fast_scroller_recycler)
    RecyclerView syncRecylerview;
    @BindView(R.id.prr)
    RelativeLayout shimmer;



    List<String> strings=new ArrayList<>();

    @BindView(R.id.share)
    MaterialButton Share;
    @BindView(R.id.back)
    MaterialButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from_conrtact_invite);
        ButterKnife.bind(this);


       Slidr.attach(this);

        SharedPreferences sharedPreferences = getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng = sharedPreferences.getString("lng", "en");
        Context language_context = LocaleHelper.setLocale(this, lng);
        Resources resources = language_context.getResources();

        Share.setText(resources.getString(R.string.invite_peoples));
        RxPermissions rxPermissions = new RxPermissions(this);

        PushDownAnim.setPushDownAnimTo(Share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RoundedBottomSheetDialog roundedBottomSheetDialog = new RoundedBottomSheetDialog(from_conrtact_invite.this);

                View view = getLayoutInflater().inflate(R.layout.bottomsheet_comment_layout, null, false);
                roundedBottomSheetDialog.setContentView(view);

                SharedPreferences sharedPreferences_lan = getSharedPreferences("LANG", Context.MODE_PRIVATE);
                String lng = sharedPreferences_lan.getString("lng", "en");
                Context language_context = LocaleHelper.setLocale(from_conrtact_invite.this, lng);
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
                        ClipData clipData = ClipData.newPlainText("Sneak Link", "सुने मेरी "+ParseUser.getCurrentUser().getString("First_and_last_name")+" और अन्य भारतीय लोगो की लाइव बाते वोइवे पर। एक" +
                                " ऐसे प्लेटफार्म जो भारत में निर्मित। है -> इनमे से किसी भी लिंक पर क्लिक करे https://play.google.com/store/apps/details?id=com.voive.android या https://voive.in");
                        clipboardManager.setPrimaryClip(clipData);
                        Toast.makeText(from_conrtact_invite.this, "Link Copied", Toast.LENGTH_SHORT).show();
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

        PushDownAnim.setPushDownAnimTo(back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.stationary, R.anim.slide_to_end);
            }
        });
        if (!rxPermissions.isGranted(Manifest.permission.READ_CONTACTS)) {


            rxPermissions
                    .requestEach(Manifest.permission.READ_CONTACTS)
                    .subscribe(permission -> { // will emit 2 Permission objects
                        if (permission.granted) {
                            shimmer.setVisibility(View.VISIBLE);



                            String PHONE_NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
                            ContentResolver cr = getContentResolver();
                            Cursor cur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{PHONE_NUMBER}, null, null, null);
                            ArrayList<String> phones = new ArrayList<>();
                            while (cur.moveToNext()) {
                                String number = cur.getString(0);
                                number = number.replaceAll(" ", "");
                                number = number.replaceAll("-", "");
                                number="+91"+number;
                                if (!phones.contains(number)) phones.add(number);
                            }
                            cur.close();
                            ParseQuery<ParseUser> parseUserParseQuery=ParseUser.getQuery();
                            parseUserParseQuery.whereContainedIn("PHONE_NO",phones);
                            parseUserParseQuery.findInBackground(new FindCallback<ParseUser>() {
                                @Override
                                public void done(List<ParseUser> objects, ParseException e) {
                                    if(objects.size()>0){
                                        syncRecylerview.setLayoutManager(new LinearLayoutManager(from_conrtact_invite.this, LinearLayoutManager.VERTICAL, false));


                                        sync_or_invite_people_adapter suggested_full_width_speakers_adapter=new sync_or_invite_people_adapter(from_conrtact_invite.this
                                                ,objects,from_conrtact_invite.this);

                                        syncRecylerview.setAdapter(suggested_full_width_speakers_adapter);

                                        shimmer.setVisibility(View.GONE);

                                    }
                                    else {

                                        syncRecylerview.setLayoutManager(new LinearLayoutManager(from_conrtact_invite.this, LinearLayoutManager.VERTICAL, false));

                                        syncRecylerview.setAdapter(new if_not_found_adapter(from_conrtact_invite.this,"",resources.getString(R.string.no_one_from_contact),0));

                                        shimmer.setVisibility(View.GONE);

                                    }


                                }
                            });


                        }
                        else if (permission.shouldShowRequestPermissionRationale) {

                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);


                        }
                        else {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);

                        }
                    });

        }
        else {

            shimmer.setVisibility(View.VISIBLE);



            String PHONE_NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
            ContentResolver cr = getContentResolver();
            Cursor cur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{PHONE_NUMBER}, null, null, null);
            ArrayList<String> phones = new ArrayList<>();
            while (cur.moveToNext()) {
                String number = cur.getString(0);
                number = number.replaceAll(" ", "");
                number = number.replaceAll("-", "");
                number="+91"+number;
                if (!phones.contains(number)) phones.add(number);

            }


            cur.close();
            ParseQuery<ParseUser> parseUserParseQuery=ParseUser.getQuery();
            parseUserParseQuery.whereContainedIn("PHONE_NO",phones);
            parseUserParseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if(objects.size()>0){
                        syncRecylerview.setLayoutManager(new LinearLayoutManager(from_conrtact_invite.this, LinearLayoutManager.VERTICAL, false));


                        sync_or_invite_people_adapter suggested_full_width_speakers_adapter=new sync_or_invite_people_adapter(from_conrtact_invite.this
                                ,objects,from_conrtact_invite.this);

                        syncRecylerview.setAdapter(suggested_full_width_speakers_adapter);

                        shimmer.setVisibility(View.GONE);

                    }
                    else {

                        syncRecylerview.setLayoutManager(new LinearLayoutManager(from_conrtact_invite.this, LinearLayoutManager.VERTICAL, false));

                        syncRecylerview.setAdapter(new if_not_found_adapter(from_conrtact_invite.this,"",resources.getString(R.string.no_one_from_contact),0));

                        shimmer.setVisibility(View.GONE);

                    }


                }
            });





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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.slide_to_end);
    }
}
