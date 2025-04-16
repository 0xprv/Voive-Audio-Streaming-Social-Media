package com.voive.android;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.button.MaterialButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;
import com.tapadoo.alerter.Alerter;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class create_table_space extends AppCompatActivity {

    public static Activity activity;
    public static String table_tittle_string, table_description_string;
    @BindView(R.id.materialButton4)
    MaterialButton materialButton4;
    @BindView(R.id.start_table_textview)
    TextView startTableTextview;
    @BindView(R.id.be_a_creator)
    TextView beACreator;
    @BindView(R.id.floating_button)
    MaterialButton floatingButton;
    @BindView(R.id.table_title)
    EditText tableTitle;
    @BindView(R.id.table_descripton)
    EditText tableDescripton;
    @BindView(R.id.counter)
    TextView counter;
    private static final int REQUEST_LOCATION = 1;
    Resources resources;
    ParseGeoPoint parseGeoPointfinal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_table_space);
        ButterKnife.bind(this);
        activity = this;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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

        Slidr.attach(this, config);
        SharedPreferences languagepref = getSharedPreferences("LANG", MODE_PRIVATE);
        String languagecode = languagepref.getString("lng", "hi");
        Context context = LocaleHelper.setLocale(this, languagecode);
      resources = context.getResources();



        startTableTextview.setText(resources.getString(R.string.start_table));


        tableTitle.setHint(resources.getString(R.string.table_title));
        tableDescripton.setHint(resources.getString(R.string.TABLE_DESCRIPTION));
        floatingButton.setText(resources.getString(R.string.CREATE));

        ParseUser parseUser = new ParseUser();

        tableTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tableTitle.getText().toString().trim().isEmpty()) {

                    floatingButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.themetextsecondarycolor)));
                    floatingButton.setTextColor(getResources().getColor(R.color.black_50));
                    floatingButton.setIconTint(ColorStateList.valueOf(getResources().getColor(R.color.black_50)));
                    floatingButton.setEnabled(false);
                    counter.setText("25");
                } else {
                    floatingButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.themetextcolor)));
                    floatingButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                    floatingButton.setIconTint(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    floatingButton.setEnabled(true);
                    counter.setText(Integer.toString(25 - tableTitle.length()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        PushDownAnim.setPushDownAnimTo(floatingButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tableTitle.getText().toString().trim().isEmpty()) {
                    
                    Toast.makeText(create_table_space.this, "Table Name Is Compulsory", Toast.LENGTH_SHORT).show();

                } else {
                    
                    ParseQuery<ParseObject> parseObjectParseQuery=ParseQuery.getQuery("live_tables");
                    parseObjectParseQuery.whereEqualTo("table_name",tableTitle.getText().toString());
                    parseObjectParseQuery.setLimit(1);
                    parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            
                            if(objects.size()>0){

                                Toast.makeText(create_table_space.this, "Already Exist. Try Different Name", Toast.LENGTH_SHORT).show();
                                
                            }
                            else {
                                table_tittle_string = tableTitle.getText().toString();
                                table_description_string = tableDescripton.getText().toString();
                                ACProgressFlower dialog = new ACProgressFlower.Builder(create_table_space.this)
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

                                Glide.with(create_table_space.this).asBitmap().load("https://avatars.dicebear.com/api/initials/" + create_table_space.table_tittle_string + ".png"+"?fontSize=38&bold=true")
                                        .centerInside().into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                                resource.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                                byte[] data = stream.toByteArray();
                                                ParseFile imageFile = new ParseFile("image.png", data);

                                                List<String> S_OF_TABLE = new ArrayList<>();
                                                S_OF_TABLE.add(ParseUser.getCurrentUser().getObjectId());

                                                ParseObject parseObject = new ParseObject("live_tables");

                                                parseObject.put("table_name", create_table_space.table_tittle_string);

                                                parseObject.put("table_description", create_table_space.table_description_string);

                                                parseObject.put("table_subscribers", S_OF_TABLE);

                                                parseObject.put("table_creator", ParseUser.getCurrentUser().getObjectId());

                                                parseObject.put("IS_PRIVATE", false);

                                                parseObject.put("HOW_MANY_ALLOWED", 25);

                                                parseObject.put("Language", ParseUser.getCurrentUser().getString("PREF"));

                                                parseObject.put("start_time", "12:00 AM");

                                                parseObject.put("end_time", "12:00 PM");

                                                parseObject.put("IS_SHOW",true);

                                                parseObject.put("ALLOW_RECORDING", false);

                                                if(getCurrentUserLocation()!=null){
                                                    parseObject.put("Location", getCurrentUserLocation());
                                                }
                                                parseObject.put("START_BACK", "00:00:00");
                                                
                                                parseObject.put("END_BACK", "23:59:59");

                                                parseObject.put("table_image", imageFile);

                                                parseObject.put("co_creator", new ArrayList<>());

                                                parseObject.put("NOTIFY",S_OF_TABLE);

                                                parseObject.put("TALKING_TITLE", "");

                                                parseObject.put("BANS", new ArrayList<>());

                                                List<String> strings = new ArrayList<>();
                                                parseObject.put("category", strings);

                                                parseObject.put("Active", true);

                                                parseObject.put("ONLINE", S_OF_TABLE);

                                                parseObject.put("table_speakers", new ArrayList<>());


                                                parseObject.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {


                                                        if (e == null) {

                                                            ParseQuery<ParseObject> parseObjectParseQuery=new ParseQuery<ParseObject>("roundtable_user_data");
                                                            parseObjectParseQuery.whereEqualTo("User_ID",ParseUser.getCurrentUser().getObjectId());
                                                            parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                                                                @Override
                                                                public void done(List<ParseObject> objects, ParseException e) {

                                                                    for(ParseObject parseObjectFF:objects){

                                                                        List<String> ss=parseObjectFF.getList("Subscriptions");
                                                                        ss.add(parseObject.getObjectId());
                                                                        parseObjectFF.put("Subscriptions",ss);
                                                                        parseObjectFF.saveInBackground();

                                                                    }

                                                                }
                                                            });
                                                            dialog.dismiss();

                                                            Intent intent = new Intent(create_table_space.this, special_speaker_live_table.class);
                                                            intent.putExtra("ID", parseObject.getObjectId());
                                                            startActivity(intent);
                                                            overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
                                                            finish();


                                                        } else {


                                                            Alerter.create(create_table_space.this)
                                                                    .setText(e.getMessage())
                                                                    .setBackgroundColorInt(getResources().getColor(android.R.color.holo_red_light))
                                                                    .hideIcon()
                                                                    .enableSwipeToDismiss()
                                                                    .setDuration(5000)
                                                                    .show();

                                                        }

                                                    }
                                                });
                                            }
                                        });
                            }
                        }
                    });

                }

            }
        });


        materialButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.stationary, R.anim.alerter_slide_out_to_bottom);

            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.alerter_slide_out_to_bottom);

    }
    private ParseGeoPoint getCurrentUserLocation(){
        if(ActivityCompat.checkSelfPermission(create_table_space.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(create_table_space.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            final Typeface typeface = ResourcesCompat.getFont(create_table_space.this, R.font.nunito_bold);
            final RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions
                    .requestEach(Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                    .subscribe(permission -> { // will emit 2 Permission objects
                        if (permission.granted) {

                            getCurrentUserLocation();
                        } else if (permission.shouldShowRequestPermissionRationale) {


                            Alerter.create(create_table_space.this)
                                    .setText(resources.getString(R.string.give_perm_so_we_can_fetch_conv))
                                    .setBackgroundColorInt(getResources().getColor(R.color.red))
                                    .hideIcon()
                                    .setTextTypeface(typeface)
                                    .enableSwipeToDismiss()
                                    .setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                                            intent.setData(uri);
                                            startActivity(intent);
                                        }
                                    })
                                    .setDuration(10000)
                                    .show();


                        } else {
                            Alerter.create(create_table_space.this)
                                    .setText(resources.getString(R.string.give_perm_so_we_can_fetch_conv))
                                    .setBackgroundColorInt(getResources().getColor(R.color.red))
                                    .hideIcon()

                                    .setTextTypeface(typeface)
                                    .enableSwipeToDismiss()
                                    .setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                                            intent.setData(uri);
                                            startActivity(intent);
                                        }
                                    })
                                    .setDuration(10000)
                                    .show();

                        }
                    });
        }
        else {


            LocationRequest locationRequest=new LocationRequest();
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(3000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationServices.getFusedLocationProviderClient(create_table_space.this)
                    .requestLocationUpdates(locationRequest,new LocationCallback(){

                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            LocationServices.getFusedLocationProviderClient(create_table_space.this)
                                    .removeLocationUpdates(this);


                            if(locationRequest!=null && locationResult.getLocations().size()>0){
                                int lastest=locationResult.getLocations().size()-1;
                                ParseGeoPoint currentUserLocation = new ParseGeoPoint(locationResult.getLocations().get(lastest).getLatitude(),
                                        locationResult.getLocations().get(lastest).getLongitude());
                                parseGeoPointfinal=currentUserLocation;
                                ParseUser.getCurrentUser().put("Location",currentUserLocation);
                                ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {

                                        if(e!=null){
                                            Toast.makeText(create_table_space.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                            }
                            else {
                                parseGeoPointfinal=null;
                            }
                        }
                    }, Looper.getMainLooper());
        }
        return parseGeoPointfinal;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case REQUEST_LOCATION:
                getCurrentUserLocation();
                break;
        }
    }
}
