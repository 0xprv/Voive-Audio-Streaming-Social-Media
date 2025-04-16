package com.voive.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class time_remain_to_live_activity extends AppCompatActivity {


    @BindView(R.id.textView18)
    TextView timer_textview;
    @BindView(R.id.close)
    MaterialButton close;
    @BindView(R.id.will_live_on)
    TextView will_live_on;
    @BindView(R.id.circular)
    CircleImageView circleImageView;
    @BindView(R.id.setting)
    MaterialButton settings;
    SimpleDateFormat dateFormat;
    List<String> waiting_LIST = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_remain_to_live);
        ButterKnife.bind(this);
        SharedPreferences sharedPreferences = getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng = sharedPreferences.getString("lng", "en");
        Context language_context = LocaleHelper.setLocale(this, lng);
        Resources resources = language_context.getResources();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
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


        ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("live_tables");
        parseObjectParseQuery.getInBackground(getIntent().getStringExtra("ID"), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                try {

                    Calendar calendar1, calendar2;
                    String string1 = object.getString("START_BACK");
                    Date time1 = new SimpleDateFormat("HH:mm:ss").parse(string1);

                    String string2 = object.getString("END_BACK");
                    Date time2 = new SimpleDateFormat("HH:mm:ss").parse(string2);

                    if (time1.getHours() > time2.getHours()) {
                        Toast.makeText(time_remain_to_live_activity.this, "YUP", Toast.LENGTH_SHORT).show();
                        time1 = dateFormat.parse("02/26/2099 " + string1);
                        time2 = dateFormat.parse("02/27/2099 " + string2);

                        calendar1 = Calendar.getInstance();
                        calendar1.setTime(time1);
                        calendar1.add(Calendar.DATE, 1);

                        calendar2 = Calendar.getInstance();
                        calendar2.setTime(time2);
                        calendar2.add(Calendar.DATE, 1);
                    } else {

                        time1 = dateFormat.parse("02/27/2099 " + string1);
                        time2 = dateFormat.parse("02/27/2099 " + string2);

                        calendar1 = Calendar.getInstance();
                        calendar1.setTime(time1);
                        calendar1.add(Calendar.DATE, 1);

                        calendar2 = Calendar.getInstance();
                        calendar2.setTime(time2);
                        calendar2.add(Calendar.DATE, 1);

                    }


                    Date currentTime = Calendar.getInstance().getTime();
                    String someRandomTime = currentTime.getHours() + ":" + currentTime.getMinutes() + ":" + currentTime.getSeconds();
                    Calendar calendar3 = Calendar.getInstance();
                    if (time1.getHours() > currentTime.getHours() || time1.getDay() == time2.getDay()) {
                        Date d = dateFormat.parse("02/27/2099 " + someRandomTime);

                        calendar3.setTime(d);
                        calendar3.add(Calendar.DATE, 1);
                    } else {
                        Date d = dateFormat.parse("02/26/2099 " + someRandomTime);

                        calendar3.setTime(d);
                        calendar3.add(Calendar.DATE, 1);
                    }


                    Date x = calendar3.getTime();
                    if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {

                    } else {
                        // table is not live
                        String start_string = object.getString("START_BACK");
                        Date start_time = new SimpleDateFormat("HH:mm:ss").parse(start_string);


                        String current_time_string = currentTime.getHours() + ":" + currentTime.getMinutes() + ":" + currentTime.getSeconds();
                        Date final_date = new SimpleDateFormat("HH:mm:ss").parse(current_time_string);

                        long final_milis;
                        if (final_date.getHours() > start_time.getHours()) {

                            Date date1 = dateFormat.parse("02/26/2099 " + current_time_string);
                            Date date2 = dateFormat.parse("02/27/2099 " + start_string);

                            long diff = date2.getTime() - date1.getTime();
                            final_milis = diff;

                        } else {
                            long diffInMs = start_time.getTime() - final_date.getTime();
                            final_milis = diffInMs;
                        }

                        new CountDownTimer(final_milis, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                                int hours = (int) TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                                int minutes = (int) (TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % TimeUnit.HOURS.toMinutes(1));
                                int seconds = (int) (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % TimeUnit.MINUTES.toSeconds(1));
                                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);

                                timer_textview.setText(timeLeftFormatted);

                            }

                            @Override
                            public void onFinish() {


                            }
                        }.start();

                    }
                } catch (java.text.ParseException parseException) {
                    parseException.printStackTrace();
                }


                Glide.with(time_remain_to_live_activity.this)
                        .asBitmap().load(object.getParseFile("table_image").getUrl())
                        .centerInside().placeholder(R.drawable.main_placeholder)
                        .into(circleImageView);

                if (object.getList("co_creator").contains(ParseUser.getCurrentUser().getObjectId()) || object.getString("table_creator").equals(ParseUser.getCurrentUser().getObjectId())) {
                    settings.setVisibility(View.VISIBLE);
                    settings.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            Intent intent = new Intent(time_remain_to_live_activity.this, table_setting_page.class);
                            intent.putExtra("ID", getIntent().getStringExtra("ID"));
                            startActivity(intent);

                        }
                    });

                } else {

                    settings.setVisibility(View.GONE);
                }

            }
        });

        will_live_on.setText(resources.getString(R.string.table_will_lived_on));


        PushDownAnim.setPushDownAnimTo(close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.stationary, R.anim.bottom_down);

            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.bottom_down);
    }
}