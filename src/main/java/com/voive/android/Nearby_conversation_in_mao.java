package com.voive.android;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.button.MaterialButton;
import com.parse.FindCallback;
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
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.agora.rtc.RtcEngine;

public class Nearby_conversation_in_mao extends AppCompatActivity {
    @BindView(R.id.back2)
    MaterialButton back;
    @BindView(R.id.viewpager)
    RecyclerView viewPager2;
    @BindView(R.id.fetchinf_progress_relative)
    LinearLayout fetchinf_progress_relative;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.textView24)
            TextView textView24;
    @BindView(R.id.locality)
            TextView locality;
    SlidrInterface slidrInterface;
    public static String ID = "44";
    Resources resources;
    LocationManager locationManager;
    boolean is_to_fetxhed=true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_conversation_in_mao);

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

        ButterKnife.bind(this);
        SharedPreferences sharedPreferences_lan = getSharedPreferences("LANG", Context.MODE_PRIVATE);
        boolean is_first_popup=sharedPreferences_lan.getBoolean("FIRST_NRABY",true);
        String lng = sharedPreferences_lan.getString("lng", "en");
        Context language_context = LocaleHelper.setLocale(this, lng);
        resources = language_context.getResources();

        ButterKnife.bind(this);
        textView24.setText(resources.getString(R.string.Nearby));


        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        getCurrentUserLocation();

        PushDownAnim.setPushDownAnimTo(back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.stationary, R.anim.bottom_down);
            }
        });
        if(is_first_popup) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Nearby_conversation_in_mao.this);
            builder.setView(R.layout.nearby_conversation_alert_dialog);

            AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.setCancelable(true);
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
            alertDialog.show();

            TextView deactivate_textview = alertDialog.findViewById(R.id.deactivate_textview);
            TextView d_message = alertDialog.findViewById(R.id.deactivate_message_textview);
            ImageView image = alertDialog.findViewById(R.id.image);
            TextView note = alertDialog.findViewById(R.id.note);
            MaterialButton deactivate = alertDialog.findViewById(R.id.deactivate);

            note.setVisibility(View.GONE);

            d_message.setText(resources.getString(R.string.get_personalised_conversation));
            deactivate.setText(resources.getString(R.string.Listen));
            deactivate_textview.setText(resources.getString(R.string.nearby_conv));

            PushDownAnim.setPushDownAnimTo(deactivate).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    alertDialog.cancel();
                }
            });

            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    SharedPreferences.Editor ed = sharedPreferences_lan.edit();
                    ed.putBoolean("FIRST_NRABY", false);
                    ed.apply();
                    alertDialog.cancel();
                }
            });
            Glide.with(Nearby_conversation_in_mao.this).asBitmap().load(R.drawable.ic_location).into(image);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            Nearby_conversation_in_mao.this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            double dd = 0.30 * width;
            int new_width = width - (int) dd;
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
            layoutParams.width = new_width;
            alertDialog.getWindow().setAttributes(layoutParams);
        }

    }
    public void setting(){
        final RxPermissions rxPermissions = new RxPermissions(Nearby_conversation_in_mao.this);

        if (!rxPermissions.isGranted(Manifest.permission.ACCESS_FINE_LOCATION) &&
                !rxPermissions.isGranted( Manifest.permission.ACCESS_COARSE_LOCATION)) {

            Intent intent=new Intent(Nearby_conversation_in_mao.this,take_permission.class);
            startActivity(intent);
            Nearby_conversation_in_mao.this.overridePendingTransition(R.anim.bottom_up, R.anim.stationary);

        }
        else {


            if(ParseUser.getCurrentUser().getParseGeoPoint("Location")!=null){

                ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("live_tables");
                parseObjectParseQuery.setLimit(20);
                parseObjectParseQuery.whereWithinKilometers("Location",ParseUser.getCurrentUser().getParseGeoPoint("Location"),5.0);
                parseObjectParseQuery.whereEqualTo("IS_PRIVATE",false);
                parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {

                        if(e==null){
                            if(objects.size()>0){



                                full_width_table_adapter full_width_table_adapter = new full_width_table_adapter(Nearby_conversation_in_mao.this, objects);
                                viewPager2.setLayoutManager(new LinearLayoutManager(Nearby_conversation_in_mao.this, LinearLayoutManager.VERTICAL, false));
                                viewPager2.setAdapter(full_width_table_adapter);
                                fetchinf_progress_relative.setVisibility(View.GONE);
                                description.setVisibility(View.GONE);


                            }
                            else {
                                fetchinf_progress_relative.setVisibility(View.GONE);
                                description.setVisibility(View.VISIBLE);
                            }
                        }
                        else {

                            Log.i("ERROR IS",e.getMessage());
                            //  Toast.makeText(sound_bite.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }



                    }
                });



            }

        }

    }
    private void getCurrentUserLocation(){

        if(ActivityCompat.checkSelfPermission(Nearby_conversation_in_mao.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Nearby_conversation_in_mao.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Nearby_conversation_in_mao.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
            Toast.makeText(this, "First", Toast.LENGTH_SHORT).show();
        }
        else {





            LocationRequest locationRequest=new LocationRequest();
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(3000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationServices.getFusedLocationProviderClient(Nearby_conversation_in_mao.this)
                    .requestLocationUpdates(locationRequest,new LocationCallback(){


                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            LocationServices.getFusedLocationProviderClient(Nearby_conversation_in_mao.this)
                                    .removeLocationUpdates(this);

                            if(locationResult.getLocations().size()>0){
                                int lastest=locationResult.getLocations().size()-1;
                                ParseGeoPoint currentUserLocation = new ParseGeoPoint(locationResult.getLocations().get(lastest).getLatitude(),
                                        locationResult.getLocations().get(lastest).getLongitude());


                                ParseUser.getCurrentUser().put("Location",currentUserLocation);
                                ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {

                                        setting();
                                        getCompleteAddressString(ParseUser.getCurrentUser().getParseGeoPoint("Location").getLatitude(),ParseUser.getCurrentUser().getParseGeoPoint("Location").getLongitude());

                                        if(e!=null){
                                            Toast.makeText(Nearby_conversation_in_mao.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                            }
                            else {


                                Toast.makeText(Nearby_conversation_in_mao.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, Looper.getMainLooper());
        }

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
    private void getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        Log.i("OLLLAA","LPOP");
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(MainActivity.activity, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String postalCode = addresses.get(0).getLocality();
            locality.setVisibility(View.VISIBLE);
           locality.setText(city+","+state);

            String finaltext = resources.getString(R.string.no_conv_near_dash,city+","+state);
            description.setText(finaltext);
        } catch (IOException e) {
            e.printStackTrace();
            MainActivity.locality.setVisibility(View.GONE);
            Log.i("FINAF OUTPIR",e.getMessage());
        }


    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.bottom_down);
    }
}