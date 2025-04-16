package com.voive.android;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.button.MaterialButton;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.livequery.ParseLiveQueryClient;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.model.SlidrPosition;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.agora.rtc.RtcEngine;

public class sound_bite extends AppCompatActivity {

    @BindView(R.id.viewpager)
    VerticalViewPager viewPager2;
    @BindView(R.id.back)
    MaterialButton back;
    @BindView(R.id.fetchinf_progress_relative)
    LinearLayout fetchinf_progress_relative;
    @BindView(R.id.description)
            TextView description;
    SlidrInterface slidrInterface;
    public static String ID = "44";
    public static RtcEngine mRtcEngine;
    Resources resources;
    LocationManager locationManager;
    public static ParseLiveQueryClient parseLiveQueryClient = null;
    ParseGeoPoint parseGeoPointfinal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_bite);
        SharedPreferences sharedPreferences=getSharedPreferences("LANG",Context.MODE_PRIVATE);
        String lng=sharedPreferences.getString("lng","en");
        boolean is_first_popup=sharedPreferences.getBoolean("FIRST_NRABY",true);
        Context language_context= LocaleHelper.setLocale(this,lng);
        resources=language_context.getResources();
        ButterKnife.bind(this);
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        getCurrentUserLocation();

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


        viewPager2.setOnTouchListener(new View.OnTouchListener() {
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


        description.setText(resources.getString(R.string.No_result_found));

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

    private String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf("WTF", "UTF-8 should always be supported", e);
            return "";
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.bottom_down);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 1001:
                getCurrentUserLocation();
                break;
        }
    }
        private void getCurrentUserLocation(){
            if(ActivityCompat.checkSelfPermission(sound_bite.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(sound_bite.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(sound_bite.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
            }
            else {


                LocationRequest locationRequest=new LocationRequest();
                locationRequest.setInterval(10000);
                locationRequest.setFastestInterval(3000);
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                LocationServices.getFusedLocationProviderClient(sound_bite.this)
                        .requestLocationUpdates(locationRequest,new LocationCallback(){

                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                super.onLocationResult(locationResult);
                                LocationServices.getFusedLocationProviderClient(sound_bite.this)
                                        .removeLocationUpdates(this);

                                if(locationRequest!=null && locationResult.getLocations().size()>0){
                                    int lastest=locationResult.getLocations().size()-1;
                                    ParseGeoPoint currentUserLocation = new ParseGeoPoint(locationResult.getLocations().get(lastest).getLatitude(),
                                            locationResult.getLocations().get(lastest).getLongitude());
                                    ParseUser.getCurrentUser().put("Location",currentUserLocation);
                                    ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {

                                            if(e!=null){
                                                Toast.makeText(sound_bite.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                                }

                                else {

                                    Toast.makeText(sound_bite.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, Looper.getMainLooper());
            }

        }

}
