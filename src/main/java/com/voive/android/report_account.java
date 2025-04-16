package com.voive.android;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.model.SlidrPosition;
import com.thekhaeng.pushdownanim.PushDownAnim;
import com.webianks.library.scroll_choice.ScrollChoice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class report_account extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    @BindView(R.id.report_textview)
    TextView reportTextview;

    @BindView(R.id.according_to_gov)
    TextView accordingToGov;
    @BindView(R.id.what_strange_textbox)
    EditText whatStrangeTextbox;
    @BindView(R.id.scroll_choice)
    ScrollChoice scrollChoice;
    @BindView(R.id.login2)
    MaterialButton reporte;
    @BindView(R.id.close)
    MaterialButton close;
    SlidrInterface slidrInterface;

    DatabaseReference databaseReference,databaseReference_table;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_account);
        ButterKnife.bind(this);
        SharedPreferences sharedPreferences = getSharedPreferences("LANG", MODE_PRIVATE);
        String lan = sharedPreferences.getString("lng", "en");
        Context context = LocaleHelper.setLocale(this, lan);
        Resources resources = context.getResources();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference= firebaseDatabase.getReference("USER_REPORTS");
        databaseReference_table= firebaseDatabase.getReference("TABLE_REPORTS");

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

        slidrInterface= Slidr.attach(this, config);

        scrollChoice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_MOVE){

                    slidrInterface.lock();
                    // Do what you want
                }
               else {
                    slidrInterface.unlock();
                }
                return false;
            }
        });


        List<String> data = new ArrayList<>();
        data.add(resources.getString(R.string.report_1));
        data.add(resources.getString(R.string.report_2));
        data.add(resources.getString(R.string.report_3));
        data.add(resources.getString(R.string.report_4));
        data.add(resources.getString(R.string.report_5));
        data.add(resources.getString(R.string.report_6));
        data.add(resources.getString(R.string.report_7));
        data.add(resources.getString(R.string.report_8));
        data.add(resources.getString(R.string.report_9));
        data.add(resources.getString(R.string.report_10));
        data.add(resources.getString(R.string.report_11));
        data.add(resources.getString(R.string.report_12));

        scrollChoice.addItems(data, 5);

        reportTextview.setText(resources.getString(R.string.report_user));
        accordingToGov.setText(resources.getString(R.string.according_to_indian_gov));
        whatStrangeTextbox.setHint(resources.getString(R.string.what_stramge));
        reporte.setText(resources.getString(R.string.report_user));
        PushDownAnim.setPushDownAnimTo(close).setScale(0.5f).setDurationPush(300).setDurationRelease(50).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.alerter_slide_in_from_bottom, R.anim.stationary);
            }
        });

        PushDownAnim.setPushDownAnimTo(reporte).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getIntent().getType().equals("ACCOUNT")){


                    ParseQuery<ParseUser> parseUserParseQuery=ParseUser.getQuery();
                    parseUserParseQuery.getInBackground(getIntent().getStringExtra("ID"), new GetCallback<ParseUser>() {
                        @Override
                        public void done(ParseUser object, ParseException e) {


                            Date c = Calendar.getInstance().getTime();
                            System.out.println("Current time => " + c);

                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                            String formattedDate = df.format(c);

                            REPORT_MODAL report_modal=new REPORT_MODAL();
                            report_modal.setUSER_ID(getIntent().getStringExtra("ID"));
                            report_modal.setUSER_NAME(object.getUsername());
                            report_modal.setDATE(formattedDate);
                            report_modal.setCAUSE(scrollChoice.getCurrentSelection());



                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    // inside the method of on Data change we are setting
                                    // our object class to our database reference.
                                    // data base reference will sends data to firebase.
                                    databaseReference.setValue(report_modal);


                                    Toast.makeText(report_account.this, object.getUsername()+" Reported . Now We Look At This Account,Don't Worry", Toast.LENGTH_SHORT).show();
                                    finish();
                                    overridePendingTransition(R.anim.alerter_slide_in_from_bottom, R.anim.stationary);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // if the data is not added or it is cancelled then
                                    // we are displaying a failure toast message.
                                    Toast.makeText(report_account.this, "Fail to add Complain " + error, Toast.LENGTH_SHORT).show();
                                }
                            });






                        }
                    });

                }
                else {

                    ParseQuery<ParseObject> objectParseQuery=new ParseQuery<ParseObject>("live_tables");
                    objectParseQuery.getInBackground(getIntent().getStringExtra("ID"), new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {

                            Date c = Calendar.getInstance().getTime();

                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                            String formattedDate = df.format(c);

                            REPORT_MODAL_TABLE report_modal=new REPORT_MODAL_TABLE();
                            report_modal.setTBL_ID(getIntent().getStringExtra("ID"));
                            report_modal.setTBL_NAME(object.getString("table_name"));
                            report_modal.setDATE(formattedDate);
                            report_modal.setEdittext(whatStrangeTextbox.getText().toString());
                            report_modal.setCAUSE(scrollChoice.getCurrentSelection());

                            databaseReference_table.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    // inside the method of on Data change we are setting
                                    // our object class to our database reference.
                                    // data base reference will sends data to firebase.
                                    databaseReference_table.setValue(report_modal);

                                    Toast.makeText(report_account.this, object.getString("table_name")+" Reported . Now We Look At This Table What Are They Talking", Toast.LENGTH_SHORT).show();
                                    finish();
                                    overridePendingTransition(R.anim.alerter_slide_in_from_bottom, R.anim.stationary);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // if the data is not added or it is cancelled then
                                    // we are displaying a failure toast message.
                                    Toast.makeText(report_account.this, "Fail to add Complain " + error, Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });



                }


            }
        });


    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.alerter_slide_in_from_bottom, R.anim.stationary);
    }

}
