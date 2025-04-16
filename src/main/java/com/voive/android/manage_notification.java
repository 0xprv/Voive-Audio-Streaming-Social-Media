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
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
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
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.model.SlidrPosition;
import com.sanojpunchihewa.glowbutton.GlowButton;
import com.thekhaeng.pushdownanim.PushDownAnim;
import com.xw.repo.widget.BounceScrollView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class manage_notification extends AppCompatActivity {


    @BindView(R.id.scroll)
    BounceScrollView bounceScrollView;
    SlidrInterface slidrInterface;
    @BindView(R.id.back)
    MaterialButton back;
    @BindView(R.id.table_image)
    CircleImageView tableImage;
    @BindView(R.id.textView15)
    TextView textView15;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.subs_text)
    TextView subsText;
    @BindView(R.id.creator_textview)
    TextView creatorTextview;
    @BindView(R.id.linearLayout4)
    LinearLayout linearLayout4;
    @BindView(R.id.creator_recyler)
    RecyclerView creatorRecyler;

    @BindView(R.id.req)
    GlowButton req;
    @BindView(R.id.more)
    MaterialButton more;
    @BindView(R.id.shimmer)
    ShimmerFrameLayout shimmer;
    @BindView(R.id.main)
    LinearLayout main;
    @BindView(R.id.time_of_table)
    TextView time_of_table;
    @BindView(R.id.table_cat)
    TextView table_cat;
    @BindView(R.id.moder_linear)
    LinearLayout moder_linear;
    @BindView(R.id.share)
    MaterialButton share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_notification);
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


        get_data(getIntent().getStringExtra("ID"));

        bounceScrollView.setOnTouchListener(new View.OnTouchListener() {
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

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.bottom_down);
        //   overridePendingTransition(R.anim.stationary, R.anim.slide_to_start);
    }

    public void get_data(String ID) {

        // LANGUAGES
        SharedPreferences sharedPreferences = getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng = sharedPreferences.getString("lng", "en");
        Context language_context = LocaleHelper.setLocale(manage_notification.this, lng);
        Resources resources = language_context.getResources();

        creatorTextview.setText(resources.getString(R.string.creator));

        ParseQuery<ParseObject> QUERY = new ParseQuery<ParseObject>("live_tables");
        QUERY.getInBackground(ID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                time_of_table.setText(object.getString("start_time") + " To " + object.getString("end_time"));
                Glide.with(manage_notification.this).asBitmap().load(object.getParseFile("table_image").getUrl())
                        .centerInside().placeholder(R.drawable.main_placeholder)
                        .thumbnail(0.5f)
                        .into(tableImage);

                Glide.with(manage_notification.this).asBitmap().load(object.getParseFile("table_image").getUrl())
                        .centerInside().placeholder(R.drawable.main_placeholder)
                        .thumbnail(0.5f)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                                Palette.generateAsync(resource, new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(@Nullable Palette palette) {

                                        Palette.Swatch swatch = palette.getLightVibrantSwatch() != null
                                                ? palette.getLightVibrantSwatch() : palette.getDominantSwatch();

                                        main.setVisibility(View.VISIBLE);
                                        shimmer.stopShimmer();
                                        shimmer.setVisibility(View.GONE);
                                        if (swatch != null) {

                                            description.setLinksClickable(true);
                                            description.setLinkTextColor(swatch.getRgb());
                                            subsText.setTextColor(swatch.getRgb());
                                            // req.setBackgroundTintList(ColorStateList.valueOf(swatch.getRgb()));
                                            // req.setTextColor(swatch.getBodyTextColor());
                                        }  else {
                                            description.setLinksClickable(true);
                                            subsText.setTextColor(getResources().getColor(R.color.themetextcolor));
                                        }

                                    }
                                });

                            }
                        });

                PushDownAnim.setPushDownAnimTo(share).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String QR="https://voive.in/tables/invites/?id="+object.getObjectId();
                        String main_string="सुनने "+object.getString("table_name")+ " में क्या बात चित हो रही है। " + QR;
                        RoundedBottomSheetDialog roundedBottomSheetDialog = new RoundedBottomSheetDialog(manage_notification.this);

                        View view = LayoutInflater.from(manage_notification.this).inflate(R.layout.bottomsheet_comment_layout, null, false);
                        roundedBottomSheetDialog.setContentView(view);

                        SharedPreferences sharedPreferences_lan = getSharedPreferences("LANG", Context.MODE_PRIVATE);
                        String lng = sharedPreferences_lan.getString("lng", "en");
                        Context language_context = LocaleHelper.setLocale(manage_notification.this, lng);
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
                                Toast.makeText(manage_notification.this, "Link Copied", Toast.LENGTH_SHORT).show();
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
                PushDownAnim.setPushDownAnimTo(more).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        RoundedBottomSheetDialog roundedBottomSheetDialog = new RoundedBottomSheetDialog(manage_notification.this);
                        View view = getLayoutInflater().inflate(R.layout.action_on_table_bottom_sheet, null, false);

                        roundedBottomSheetDialog.setContentView(view);

                        MaterialButton report = view.findViewById(R.id.report_user);

                        report.setText(resources.getString(R.string.report_table));


                        PushDownAnim.setPushDownAnimTo(report).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(manage_notification.this, report_account.class);
                                intent.setType("TABLE");
                                intent.putExtra("ID", getIntent().getStringExtra("ID"));
                                startActivity(intent);
                                overridePendingTransition(R.anim.alerter_slide_in_from_bottom, R.anim.stationary);
                            }
                        });

                        roundedBottomSheetDialog.show();


                    }
                });
                List<String> ss = new ArrayList<>();
                ss.add("News");
                ss.add("Arts & Entertainment");
                ss.add("Health & Beauty");
                ss.add("Science & Tech");
                ss.add("Politics");
                ss.add("Business");
                ss.add("Books & Information");
                if (ss.contains(object.getString("table_name")) || !object.getBoolean("IS_SHOW")) {
                    if (object.getString("table_name").equals("News")) {
                        textView15.setText(resources.getString(R.string.News));
                        description.setText(resources.getString(R.string.AUTO_GENRATED_DES_NEWS));
                    } else if (object.getString("table_name").equals("Arts & Entertainment")) {
                        textView15.setText(resources.getString(R.string.arts_enter));
                        description.setText(resources.getString(R.string.AUTO_GENRATED_DES_ART));
                    } else if (object.getString("table_name").equals("Health & Beauty")) {
                        textView15.setText(resources.getString(R.string.health_beauty));
                        description.setText(resources.getString(R.string.AUTO_GENRATED_DES_HEALTH));
                    } else if (object.getString("table_name").equals("Science & Tech")) {
                        textView15.setText(resources.getString(R.string.sci_tech));
                        description.setText(resources.getString(R.string.AUTO_GENRATED_DES_SCI));
                    } else if (object.getString("table_name").equals("Politics")) {
                        textView15.setText(resources.getString(R.string.Politics));
                        description.setText(resources.getString(R.string.AUTO_GENRATED_DES_POLITICS));
                    } else if (object.getString("table_name").equals("Business")) {
                        textView15.setText(resources.getString(R.string.Buisness));
                        description.setText(resources.getString(R.string.AUTO_GENRATED_DES_BSN));
                    } else if (object.getString("table_name").equals("Books & Information")) {
                        textView15.setText(resources.getString(R.string.Books_Information));
                        description.setText(resources.getString(R.string.AUTO_GENRATED_DES_BOOK));
                    }
                    moder_linear.setVisibility(View.GONE);
                } else {
                    textView15.setText(object.getString("table_name"));
                    moder_linear.setVisibility(View.VISIBLE);
                }

                textView15.setText(object.getString("table_name"));
                if(object.getString("table_description").trim().isEmpty()){

                    description.setVisibility(View.GONE);
                }
                else {
                    description.setVisibility(View.VISIBLE);
                    description.setText(object.getString("table_description"));
                }

                subsText.setText(Integer.toString(object.getList("table_subscribers").size()) + " " + resources.getString(R.string.Subscribers));


                List<String> WHO_OWN = new ArrayList<>();
                WHO_OWN.add(object.getString("table_creator"));
                WHO_OWN.addAll(object.getList("co_creator"));


                ParseQuery<ParseUser> parseUserParseQuery_CO = ParseUser.getQuery();
                parseUserParseQuery_CO.whereContainedIn("objectId", WHO_OWN);
                parseUserParseQuery_CO.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        creatorRecyler.setLayoutManager(new LinearLayoutManager(manage_notification.this, LinearLayoutManager.VERTICAL, false));
                        creatorRecyler.setAdapter(new live_table_creators_adapter(objects, manage_notification.this, ID));
                    }
                });


                if (object.getList("category").size() > 0) {

                    List<String> mVals = object.getList("category");
                    table_cat.setText(mVals.get(0));


                } else {
                    table_cat.setVisibility(View.GONE);
                }

            }
        });

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
