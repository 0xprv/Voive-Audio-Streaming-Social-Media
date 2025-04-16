package com.voive.android;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class pickingtopics extends AppCompatActivity {


    public static Activity activity;
    public static List<String> strings = new ArrayList<>();
    @BindView(R.id.close_button)
    MaterialButton back2;;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickingtopics);
        ButterKnife.bind(this);
        activity = this;
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.card_view_color));
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
        String languagecode = languagepref.getString("lng", "en");
        Context context = LocaleHelper.setLocale(this, languagecode);
        Resources resources = context.getResources();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        //    textView24.setText(resources.getString(R.string.select_taste));
        PushDownAnim.setPushDownAnimTo(back2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

                overridePendingTransition(R.anim.stationary, R.anim.alerter_slide_out_to_bottom);

            }
        });


        String[] sportsstring = {"Sports",
                "Art",
                "Business",
                "Reading",
                "Design",
                "Technology",
                "English",
                "Geography",
                "Psychology",
                "History",
                "Civics",
                "Podcasts",
                "Maths",
                "Space",
                "Religious",
                "Science",
                "Sociology",
                "Computer",
                "Devices",
                "Cryptocurrency",
                "3D printing",
                "Genomic",
                "AI",
                "Robots",
                "IOT",
                "Data Science",
                "Hacking",
                "Nuclear Power",
                "Dreams",
                "5G",
                "Biometrics",
                "Robotics",
                "Engineering",
                "Investing",
                "Coding",
                "VR",
                "Data Analytics",
                "Movies",
                "TV Shows",
              };

        for (int i = 0; i < sportsstring.length; i++) {
            strings.add(sportsstring[i]);

        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(new main_genre_adater(this,strings));


    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.alerter_slide_out_to_bottom);
    }


}
