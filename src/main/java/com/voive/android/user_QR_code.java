package com.voive.android;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import static com.voive.android.App.BAN_OR_KICKED;

import android.Manifest;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.WindowCompat;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.button.MaterialButton;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.android.gestures.StandardScaleGestureDetector;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.UiSettings;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;
import com.tapadoo.alerter.Alerter;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class user_QR_code extends AppCompatActivity implements LocationEngineCallback {


    private static final LatLng BOUND_CORNER_NW = new LatLng(32.545751, 73.881147);
    private static final LatLng BOUND_CORNER_SE = new LatLng(8.711894, 89.741397);
    private static final com.mapbox.mapboxsdk.geometry.LatLngBounds RESTRICTED_BOUNDS_AREA = new com.mapbox.mapboxsdk.geometry.LatLngBounds.Builder()
            .include(BOUND_CORNER_NW)
            .include(BOUND_CORNER_SE)
            .build();
    private final List<List<Point>> points = new ArrayList<>();;
    private final List<Point> outerPoints = new ArrayList<>();
    MapView mapView;
    MapboxMap mapboxGLB;
    Style styleGB;
    int SKIP = 0;
    private LocationComponent locationComponent;
    private MarkerView markerView;
    private MarkerViewManager markerViewManager;
    Resources resources;
    public static boolean id_url(String geti) {

        String s = "((http:\\/\\/|https:\\/\\/)?(www.)?(([a-zA-Z0-9-]){2,}\\.){1,4}([a-zA-Z]){2,6}(\\/([a-zA-Z-_\\/\\.0-9#:?=&;,]*)?)?)";
        Pattern p = Pattern.compile(s);
        Matcher m = p.matcher(geti);
        return m.find();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // Mapbox.getInstance(this, getMapboxId());
        setContentView(R.layout.activity_user__qr_code);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.searchboxbackcolor));
      /*  Window window = getWindow();
        if (Build.VERSION.SDK_INT <= 29) {
            //   window.setStatusBarColor(Color.TRANSPARENT);
            //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            //  window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        } else {
            window.setStatusBarColor(Color.TRANSPARENT);
            // Making status bar overlaps with the activity
            WindowCompat.setDecorFitsSystemWindows(window, false);
        }*/
    /*    SharedPreferences languagepref = getSharedPreferences("LANG", MODE_PRIVATE);
        String languagecode = languagepref.getString("lng", "en");
        boolean is_first_map=languagepref.getBoolean("FIRST_MAP",true);
        Context context = LocaleHelper.setLocale(this, languagecode);
         resources = context.getResources();

        MaterialButton back = findViewById(R.id.button100);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.stationary, R.anim.bottom_down);
            }
        });
        if (is_first_map) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(R.layout.nearby_conversation_alert_dialog);

            AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
            alertDialog.show();

            TextView deactivate_textview = alertDialog.findViewById(R.id.deactivate_textview);
            TextView d_message = alertDialog.findViewById(R.id.deactivate_message_textview);
            MaterialButton deactivate = alertDialog.findViewById(R.id.deactivate);


            d_message.setText(resources.getString(R.string.get_conv_list_on_map));
            deactivate.setText(resources.getString(R.string.LET_GO));
            deactivate_textview.setText(resources.getString(R.string.Voice_Map));

            PushDownAnim.setPushDownAnimTo(deactivate).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor ed=languagepref.edit();
                    ed.putBoolean("FIRST_MAP",false);
                    ed.apply();
                    alertDialog.cancel();
                }
            });
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            double dd = 0.30 * width;
            int new_width = width - (int) dd;
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
            layoutParams.width = new_width;
            alertDialog.getWindow().setAttributes(layoutParams);


        }


        MaterialButton materialButton = findViewById(R.id.get_local);
        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(user_QR_code.this, sound_bite.class);
                startActivity(intent);
               overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
                enableLocationComponent(styleGB);
            }
        });
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mapboxGLB = mapboxMap;

                mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        Toast.makeText(user_QR_code.this, marker.getSnippet(), Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });

                mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                    @Override
                    public boolean onMapClick(@NonNull LatLng point) {

                        return false;
                    }
                });

                mapboxMap.addOnScaleListener(new MapboxMap.OnScaleListener() {
                    @Override
                    public void onScaleBegin(@NonNull StandardScaleGestureDetector detector) {

                    }

                    @Override
                    public void onScale(@NonNull StandardScaleGestureDetector detector) {

                    }

                    @Override
                    public void onScaleEnd(@NonNull StandardScaleGestureDetector detector) {


                        SKIP = SKIP + 10;
                        ParseQuery<ParseObject> LOCATION_TABLE = new ParseQuery<ParseObject>("live_tables");
                        LOCATION_TABLE.setLimit(10);
                        LOCATION_TABLE.setSkip(SKIP);
                        LOCATION_TABLE.whereNotEqualTo("Location", null);
                        LOCATION_TABLE.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {

                                for (ParseObject parseObject : objects) {


                                    ParseGeoPoint parseGeoPoint = parseObject.getParseGeoPoint("Location");
                                    markerViewManager = new MarkerViewManager(mapView, mapboxMap);
                                    View customView = LayoutInflater.from(user_QR_code.this).inflate(
                                            R.layout.cutom_marker, null);
                                    customView.setLayoutParams(new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

                                    CircleImageView circleImageView4 = customView.findViewById(R.id.circleImageView4);

                                    Glide.with(user_QR_code.this.getApplicationContext()).asBitmap().apply(new RequestOptions().override(120, 120))
                                            .load(parseObject.getParseFile("table_image").getUrl())
                                            .circleCrop().into(new SimpleTarget<Bitmap>() {
                                                @Override
                                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                                                    Palette.generateAsync(resource, new Palette.PaletteAsyncListener() {
                                                        @Override
                                                        public void onGenerated(@Nullable Palette palette) {

                                                            Palette.Swatch swatch = palette.getLightVibrantSwatch() != null
                                                                    ? palette.getLightVibrantSwatch() : palette.getDominantSwatch();

                                                            circleImageView4.setBorderWidth(3);
                                                            circleImageView4.setBorderColor(swatch.getRgb());
                                                        }
                                                    });
                                                }
                                            });

                                    Glide.with(user_QR_code.this.getApplicationContext()).asBitmap().apply(new RequestOptions().override(120, 120))
                                            .load(parseObject.getParseFile("table_image").getUrl())
                                            .circleCrop().into(circleImageView4);
                                    markerView = new MarkerView(new LatLng(parseGeoPoint.getLatitude(), parseGeoPoint.getLongitude()), customView);
                                    markerViewManager.addMarker(markerView);

                                }

                            }
                        });

                    }
                });

                ParseQuery<ParseObject> LOCATION_TABLE = new ParseQuery<ParseObject>("live_tables");
                LOCATION_TABLE.setLimit(20);
                LOCATION_TABLE.whereNotEqualTo("Location", null);
                LOCATION_TABLE.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {

                        try {


                            for (ParseObject parseObject : objects) {


                                ParseGeoPoint parseGeoPoint = parseObject.getParseGeoPoint("Location");
                                markerViewManager = new MarkerViewManager(mapView, mapboxMap);
                                View customView = LayoutInflater.from(user_QR_code.this).inflate(
                                        R.layout.cutom_marker, null);
                                customView.setLayoutParams(new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

                                CircleImageView circleImageView4 = customView.findViewById(R.id.circleImageView4);

                                Glide.with(user_QR_code.this.getApplicationContext()).asBitmap().apply(new RequestOptions().override(100, 100))
                                        .load(parseObject.getParseFile("table_image").getUrl())
                                        .circleCrop().into(circleImageView4);

                                PushDownAnim.setPushDownAnimTo(circleImageView4).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent intent = new Intent(context, sound_preview.class);
                                        intent.putExtra("ID", parseObject.getObjectId());
                                        context.startActivity(intent);
                                        overridePendingTransition(R.anim.bottom_up, R.anim.stationary);

                                    }
                                });

                                markerView = new MarkerView(new LatLng(parseGeoPoint.getLatitude(), parseGeoPoint.getLongitude()), customView);
                                markerViewManager.addMarker(markerView);


                            }

                        }
                        catch (NullPointerException nn){
                            nn.printStackTrace();
                        }

                    }
                });


                mapboxMap.setMinZoomPreference(4);
                CameraPosition position = new CameraPosition.Builder()
                        .target(new LatLng(28.7041, 77.1025))
                        .zoom(15)
                        .tilt(25)
                        .build();
               // mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 3000);
                mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/pranavvishnoi/cl5452rzh008614qg7iz63pci"), new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        styleGB = style;
                        enableLocationComponent(style);
                        mapboxMap.setLatLngBoundsForCameraTarget(RESTRICTED_BOUNDS_AREA);
                        showBoundsArea(style);

                        UiSettings uiSettings = mapboxMap.getUiSettings();
                        uiSettings.setTiltGesturesEnabled(true);
                        uiSettings.setLogoEnabled(false);
                        uiSettings.setCompassEnabled(false);
                        uiSettings.setQuickZoomGesturesEnabled(true);
                        uiSettings.setZoomRate(5);
                    }
                });


            }
        });*/


    }

    public boolean validate(String ss) {

        try {
            URL obj = new URL(ss);
            obj.toURI();
            return true;
        } catch (MalformedURLException e) {
            return false;
        } catch (URISyntaxException e) {
            return false;
        }

    }

    private void showBoundsArea(@NonNull Style loadedMapStyle) {
        outerPoints.add(Point.fromLngLat(RESTRICTED_BOUNDS_AREA.getNorthWest().getLongitude(),
                RESTRICTED_BOUNDS_AREA.getNorthWest().getLatitude()));
        outerPoints.add(Point.fromLngLat(RESTRICTED_BOUNDS_AREA.getNorthEast().getLongitude(),
                RESTRICTED_BOUNDS_AREA.getNorthEast().getLatitude()));
        outerPoints.add(Point.fromLngLat(RESTRICTED_BOUNDS_AREA.getSouthEast().getLongitude(),
                RESTRICTED_BOUNDS_AREA.getSouthEast().getLatitude()));
        outerPoints.add(Point.fromLngLat(RESTRICTED_BOUNDS_AREA.getSouthWest().getLongitude(),
                RESTRICTED_BOUNDS_AREA.getSouthWest().getLatitude()));
        outerPoints.add(Point.fromLngLat(RESTRICTED_BOUNDS_AREA.getNorthWest().getLongitude(),
                RESTRICTED_BOUNDS_AREA.getNorthWest().getLatitude()));
        points.add(outerPoints);

        loadedMapStyle.addSource(new GeoJsonSource("source-id",
                Polygon.fromLngLats(points)));


    }

    private void enableLocationComponent(Style loadedMapStyle) {



        if(ActivityCompat.checkSelfPermission(user_QR_code.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(user_QR_code.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            final Typeface typeface = ResourcesCompat.getFont(user_QR_code.this, R.font.nunito_bold);
            final RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions
                    .requestEach(Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                    .subscribe(permission -> { // will emit 2 Permission objects
                        if (permission.granted) {
                            enableLocationComponent(mapboxGLB.getStyle());
                        }
                        else if (permission.shouldShowRequestPermissionRationale) {


                            Alerter.create(user_QR_code.this)
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
                        else {
                            Alerter.create(user_QR_code.this)
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
            try {
                // Activate the MapboxMap LocationComponent to show user location
                // Adding in LocationComponentOptions is also an optional parameter
                locationComponent = mapboxGLB.getLocationComponent();
                locationComponent.activateLocationComponent(this, loadedMapStyle);
                locationComponent.setLocationComponentEnabled(true);

                //Set the component's camera mode
                locationComponent.setCameraMode(CameraMode.TRACKING);
            } catch (Exception eexx) {
                eexx.printStackTrace();
            }


        }

    }


    @Override
    public void onSuccess(Object o) {

    }

    @Override
    public void onFailure(@NonNull Exception e) {

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.bottom_down);
    }

}
