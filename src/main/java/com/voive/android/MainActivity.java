package com.voive.android;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabItem;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.w3c.dom.Text;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.agora.rtc.RtcEngine;

public class MainActivity extends AppCompatActivity {

    public static FrameLayout frameLayout;
    public static FragmentManager fragmentManager;
    public static String WHAT_REMOVE = "";
    public static AlertDialog alertDialog;
    public static Activity activity;
    public static MaterialToolbar top_toolbar;
    public static TextView top;
    public static TextView locality;
    final Fragment fragment1 = new homefragment();
    final Fragment fragment2 = new main_search_fragment();
    final Fragment fragment3 = new tablefragment();
    final Fragment fragment4 = new map_fragment();
    final FragmentManager fm = getSupportFragmentManager();
    @BindView(R.id.nav_view2)
    BottomNavigationView navView2;
    Fragment fragment;
    @BindView(R.id.framelayout)
    FrameLayout framelayout;
    @BindView(R.id.divider8)
    View divider8;
    @BindView(R.id.divider)
    View dvdr;
    @BindView(R.id.button10)
    MaterialButton button10;
    Resources resources;
    List<ParseUser> BACKRROM_PEOPLES;
    String URL_SERVER;
    @BindView(R.id.circleImageView)
    CircleImageView circleImageView;
    @BindView(R.id.miniplay_title)
    TextView miniplay_title;
    @BindView(R.id.miniplay_table_name)
    TextView miniplay_table_name;
    @BindView(R.id.miniplay_container)
    ConstraintLayout miniplay_container;
    @BindView(R.id.materialButton5)
    MaterialButton materialButton5;
    @BindView(R.id.recent)
    MaterialButton recent;
    @BindView(R.id.invitation)
    MaterialButton invitation;
    @BindView(R.id.bg_inv)
    LinearLayout bg_inv;
    @BindView(R.id.pro_click)
    RelativeLayout pro_click;
    @BindView(R.id.badge)
    ImageView badge;
    @BindView(R.id.arrow)
            ImageView arrow;
    //Getting Handler To Check Miniplay
    Handler mini_play_handler = new Handler();
    boolean local_checking = false;
    Fragment active = fragment1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Giphy.INSTANCE.configure(this, getResources().getString(R.string.GIPHY), true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        window.setNavigationBarColor(getResources().getColor(R.color.searchboxbackcolor));

        locality = findViewById(R.id.locality);

        final RxPermissions rxPermissions = new RxPermissions(this);

        rxPermissions
                .requestEach(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.RECORD_AUDIO)
                .subscribe(permission -> {
                    if (permission.granted) {
                    } else if (permission.shouldShowRequestPermissionRationale) {

                    } else {

                    }
                });


        top = findViewById(R.id.top_text);
        top_toolbar = findViewById(R.id.toolbar);
        URL_SERVER = App.getLIVE();
        activity = this;
        BACKRROM_PEOPLES = new ArrayList<>();
        fragmentManager = getSupportFragmentManager();
        frameLayout = findViewById(R.id.framelayout);

        miniplay_runnable.run();


        SharedPreferences sharedPreferences_lan = getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng = sharedPreferences_lan.getString("lng", "en");
        boolean is_first_full_scr = sharedPreferences_lan.getBoolean("is_first_full_scr", true);
        Context language_context = LocaleHelper.setLocale(this, lng);
        resources = language_context.getResources();
        MainActivity.top.setText(resources.getString(R.string.home));

        if (is_first_full_scr) {


            Intent intent = new Intent(this, app_greeting.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            SharedPreferences.Editor editor = sharedPreferences_lan.edit();
            editor.putBoolean("is_first_full_scr", false);
            editor.apply();

        }

        ParseQuery<notification_modal> notification_modalParseQuery = ParseQuery.getQuery(notification_modal.class);
        notification_modalParseQuery.whereEqualTo("USER_ID", ParseUser.getCurrentUser().getObjectId());
        notification_modalParseQuery.whereEqualTo("READ", false);
        notification_modalParseQuery.setLimit(3);
        notification_modalParseQuery.whereEqualTo("TYPE", Constant.SOME_ONE_SEND_INVITATION);
        notification_modalParseQuery.findInBackground(new FindCallback<notification_modal>() {
            @Override
            public void done(List<notification_modal> objects, ParseException e) {

                if(objects.size()>0){
                    badge.setVisibility(objects.size() > 0 ? View.VISIBLE : View.GONE);
                    try {


                        for (notification_modal notification_modal : objects) {

                            Intent intent = new Intent(MainActivity.this, invitation_of_table_activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            intent.putExtra("ID", notification_modal.getTABLE());
                            startActivity(intent);
                        }
                    } catch (NullPointerException nn) {
                        nn.printStackTrace();
                        finish();
                        Intent intent = new Intent(MainActivity.this, server_error.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }
                }


            }
        });


        PushDownAnim.setPushDownAnimTo(invitation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, my_invitation_activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.bottom_up, R.anim.stationary);

            }
        });
        try {


            ParseLiveQueryClient parseLiveQueryClient = null;

            try {
                parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI(URL_SERVER));
            } catch (URISyntaxException et) {
                et.printStackTrace();
            }


            ParseQuery<notification_modal> XC = ParseQuery.getQuery(notification_modal.class);
            XC.whereEqualTo("USER_ID", ParseUser.getCurrentUser().getObjectId());
            XC.whereEqualTo("READ", false);
            XC.setLimit(10);
            XC.whereEqualTo("TYPE", Constant.SOME_ONE_SEND_INVITATION);


            SubscriptionHandling<notification_modal> subscriptionHandling2 = parseLiveQueryClient.subscribe(XC);

            subscriptionHandling2.handleSubscribe(new SubscriptionHandling.HandleSubscribeCallback<notification_modal>() {
                @Override
                public void onSubscribe(ParseQuery<notification_modal> query) {

                    try {

                        badge.setVisibility(query.count() > 0 ? View.VISIBLE : View.GONE);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }
            });


        } catch (Exception fg) {

            fg.printStackTrace();
        }


        PushDownAnim.setPushDownAnimTo(miniplay_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, special_speaker_live_table.class);
                intent.putExtra("ID", App.WHICH_TABLE);
                intent.putExtra("IS_AGAIN", 1);
                startActivity(intent);
                overridePendingTransition(R.anim.bottom_up, R.anim.stationary);


            }
        });

        Glide.with(MainActivity.this.getApplicationContext()).asBitmap().load(ParseUser.getCurrentUser().getParseFile("speaker_profile_pic")
                .getUrl()).thumbnail(0.3f).placeholder(R.drawable.theme_placeholder).centerInside().into(circleImageView);

        PushDownAnim.setPushDownAnimTo(pro_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, settingpage.class);
                startActivity(intent);
                overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
            }
        });
        PushDownAnim.setPushDownAnimTo(button10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, create_table_space.class);
                startActivity(intent);
                overridePendingTransition(R.anim.bottom_up, R.anim.stationary);

            }
        });
        PushDownAnim.setPushDownAnimTo(materialButton5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (special_speaker_live_table.parseLiveQueryClient != null) {
                    special_speaker_live_table.parseLiveQueryClient.disconnect();
                }
                if (special_speaker_live_table.mRtcEngine != null) {
                    special_speaker_live_table.mRtcEngine.leaveChannel();
                }
                RtcEngine.destroy();
                App.WHICH_TABLE = "";
                App.IS_LISTENING = false;
                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(13);
            }
        });

        PushDownAnim.setPushDownAnimTo(MainActivity.top).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, subscribed_table_activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
            }
        });

        navView2.getMenu().getItem(0).setTitle(resources.getString(R.string.home));
        navView2.getMenu().getItem(1).setTitle(resources.getString(R.string.Discv));
        navView2.getMenu().getItem(2).setTitle(resources.getString(R.string.Nearby));
        //navView2.getMenu().getItem(3).setTitle(resources.getString(R.string.Nearby));
        setFragment(fragment1, "1", 0);
        PushDownAnim.setPushDownAnimTo(recent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                top_toolbar.setVisibility(View.GONE);
                MainActivity.top.setVisibility(View.GONE);
                top_toolbar.setElevation(0f);
                setFragment(fragment2, "2", 0);
            }
        });
        navView2.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        MainActivity.top.setText(resources.getString(R.string.home));
                        top_toolbar.setElevation(0f);
                        MainActivity.top.setVisibility(View.VISIBLE);
                        top_toolbar.setVisibility(View.VISIBLE);
                        arrow.setVisibility(View.VISIBLE);
                        PushDownAnim.setPushDownAnimTo(MainActivity.top).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MainActivity.this, subscribed_table_activity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
                            }
                        });
                        setFragment(fragment1, "1", 0);
                        return true;
                    case R.id.navigation_home3:
                        MainActivity.top.setText(resources.getString(R.string.Discv));
                        top_toolbar.setVisibility(View.VISIBLE);
                        MainActivity.top.setVisibility(View.VISIBLE);
                        top_toolbar.setElevation(0f);
                        setFragment(fragment3, "3", 2);
                        arrow.setVisibility(View.GONE);
                        PushDownAnim.setPushDownAnimTo(MainActivity.top).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        return true;
                    case R.id.navigation_home34:


                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                            final RxPermissions rxPermissions = new RxPermissions(MainActivity.this);
                            rxPermissions
                                    .requestEach(Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION)
                                    .subscribe(permission -> {
                                        if (permission.granted) {

                                            //Checking Location Service Enabled Or Not
                                            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                            boolean gps_enabled = false;
                                            boolean network_enabled = false;

                                            try {
                                                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                                            } catch (Exception ex) {
                                            }

                                            try {
                                                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                                            } catch (Exception ex) {
                                            }

                                            if (!gps_enabled && !network_enabled) {


                                                Toast.makeText(MainActivity.this, "GPS Not Enabled", Toast.LENGTH_SHORT).show();
                                            } else {

                                                Intent intent = new Intent(MainActivity.this, Nearby_conversation_in_mao.class);
                                                startActivity(intent);
                                                overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
                                            }


                                        } else if (permission.shouldShowRequestPermissionRationale) {
                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                                            intent.setData(uri);
                                            startActivity(intent);


                                        } else {
                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                                            intent.setData(uri);
                                            startActivity(intent);
                                        }
                                    });


                        } else {
                            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            boolean gps_enabled = false;
                            boolean network_enabled = false;

                            try {
                                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                            } catch (Exception ex) {
                            }

                            try {
                                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                            } catch (Exception ex) {
                            }

                            if (!gps_enabled && !network_enabled) {


                                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.activity, R.style.AlertDialogCustom);
                                builder1.setMessage(resources.getString(R.string.GPS_NO));
                                builder1.setTitle("GPS :(");
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
                            } else {
                                if (special_speaker_live_table.parseLiveQueryClient != null) {
                                    special_speaker_live_table.parseLiveQueryClient.disconnect();
                                }
                                if (special_speaker_live_table.mRtcEngine != null) {
                                    special_speaker_live_table.mRtcEngine.leaveChannel();
                                }
                                RtcEngine.destroy();
                                App.WHICH_TABLE = "";
                                App.IS_LISTENING = false;
                                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.cancel(13);

                                Intent intent = new Intent(MainActivity.this, Nearby_conversation_in_mao.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
                            }

                        }


                }
                return true;
            }
        });


    }

    public String get_formated(int num) {

        DecimalFormat formatter = new DecimalFormat("###,###,###,###");
        String yourFormattedString = formatter.format(num);
        return yourFormattedString;
    }

    public void setFragment(Fragment fragment, String tag, int position) {
        if (fragment.isAdded()) {
            fm.beginTransaction().hide(active).show(fragment).commit();
        } else {
            fm.beginTransaction().add(R.id.framelayout, fragment, tag).commit();
        }
        navView2.getMenu().getItem(position).setChecked(true);
        active = fragment;
    }

       Runnable miniplay_runnable = new Runnable() {
        @Override
        public void run() {
            if (App.IS_LISTENING) {
                miniplay_container.setVisibility(View.VISIBLE);
                if (!local_checking) {
                    ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("live_tables");
                    parseObjectParseQuery.getInBackground(App.WHICH_TABLE, new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {

                            try {
                                miniplay_title.setSelected(true);
                                miniplay_title.setText(object.getString("TALKING_TITLE"));
                                miniplay_table_name.setText(object.getString("table_name"));
                                local_checking = false;


                                Glide.with(MainActivity.this.getApplicationContext())
                                        .asBitmap().load(object.getParseFile("table_image").getUrl())
                                        .centerInside().into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                                                Palette.generateAsync(resource, new Palette.PaletteAsyncListener() {
                                                    @Override
                                                    public void onGenerated(@Nullable Palette palette) {
                                                        Palette.Swatch swatch = palette.getLightVibrantSwatch() != null
                                                                ? palette.getLightVibrantSwatch() : palette.getDominantSwatch();

                                                        miniplay_table_name.setTextColor(swatch.getRgb());
                                                        dvdr.setBackgroundColor(swatch.getRgb());
                                                    }
                                                });

                                            }
                                        });
                            } catch (NullPointerException dd) {
                                dd.printStackTrace();
                            }


                        }
                    });
                }
            } else {

                miniplay_container.setVisibility(View.GONE);
            }
            mini_play_handler.postDelayed(miniplay_runnable, 1000);
        }
    };


}
