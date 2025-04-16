package com.voive.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
import com.parse.ParseUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.model.SlidrPosition;
import com.thekhaeng.pushdownanim.PushDownAnim;

import butterknife.BindView;
import butterknife.ButterKnife;

public class what_is_my_taste extends AppCompatActivity {


    @BindView(R.id.back2)
    MaterialButton back2;
    @BindView(R.id.textView24)
    TextView textView24;
    @BindView(R.id.divider17)
    View divider17;
    @BindView(R.id.constraintLayout2)
    ConstraintLayout constraintLayout2;
    @BindView(R.id.shimer)
    ShimmerFrameLayout shimer;
    @BindView(R.id.blocked_recyler)
    RecyclerView blockedRecyler;
    SlidrInterface slidrInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_is_my_taste);
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

        slidrInterface= Slidr.attach(this, config);


        SharedPreferences languagepref = getSharedPreferences("LANG", MODE_PRIVATE);
        String languagecode = languagepref.getString("lng", "en");
        Context contextT = LocaleHelper.setLocale(this, languagecode);
        Resources resources = contextT.getResources();

        textView24.setText(resources.getString(R.string.My_Topics));

       if (ParseUser.getCurrentUser().getList("Tastes_list").size() > 0) {

            blockedRecyler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
            blockedRecyler.setAdapter(new my_tastes_adapter(this, ParseUser.getCurrentUser().getList("Tastes_list")));
            shimer.setVisibility(View.GONE);

        } else {
            blockedRecyler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            blockedRecyler.setAdapter(new if_not_found_adapter(this, "", "No Taste Found", 0));
            shimer.setVisibility(View.GONE);

        }


        PushDownAnim.setPushDownAnimTo(back2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.stationary, R.anim.slide_to_end);

            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.slide_to_end);
    }
}
