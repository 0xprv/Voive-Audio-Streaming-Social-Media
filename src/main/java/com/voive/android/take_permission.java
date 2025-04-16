package com.voive.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.button.MaterialButton;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.tapadoo.alerter.Alerter;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.thekhaeng.pushdownanim.PushDownAnim;

import butterknife.BindView;
import butterknife.ButterKnife;

public class take_permission extends AppCompatActivity {

    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.login2)
    MaterialButton login2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_permission);
        ButterKnife.bind(this);
        SharedPreferences sharedPreferences=getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng=sharedPreferences.getString("lng","en");
        Context language_context= LocaleHelper.setLocale(this,lng);
        Resources resources=language_context.getResources();
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        PushDownAnim.setPushDownAnimTo(login2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Typeface typeface = ResourcesCompat.getFont(take_permission.this, R.font.nunito_bold);
                final RxPermissions rxPermissions = new RxPermissions(take_permission.this);

                rxPermissions
                        .requestEach(Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                             )
                        .subscribe(permission -> {
                            if (permission.granted) {
                              getCurrentUserLocation();
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



            }
        });

        description.setText(resources.getString(R.string.Allow_perm_to_voive));
    }

    private void getCurrentUserLocation(){
        if(ActivityCompat.checkSelfPermission(take_permission.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(take_permission.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){


        }
        else {


            LocationRequest locationRequest=new LocationRequest();
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(3000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationServices.getFusedLocationProviderClient(take_permission.this)
                    .requestLocationUpdates(locationRequest,new LocationCallback(){

                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            LocationServices.getFusedLocationProviderClient(take_permission.this)
                                    .removeLocationUpdates(this);


                            if(locationRequest!=null && locationResult.getLocations().size()>0){
                                int lastest=locationResult.getLocations().size()-1;
                                ParseGeoPoint currentUserLocation = new ParseGeoPoint(locationResult.getLocations().get(lastest).getLatitude(),
                                        locationResult.getLocations().get(lastest).getLongitude());
                                ParseUser.getCurrentUser().put("Location",currentUserLocation);
                                ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        finish();

                                    }
                                });
                            }
                        }
                    }, Looper.getMainLooper());
        }

    }
}