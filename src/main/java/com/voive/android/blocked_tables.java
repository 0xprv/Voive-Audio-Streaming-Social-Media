package com.voive.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.model.SlidrPosition;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class blocked_tables extends AppCompatActivity {



    @BindView(R.id.another_setting_textview)
    TextView anotherSettingTextview;
    @BindView(R.id.back_3)
    MaterialButton back3;

    @BindView(R.id.blocked_recyler)
    RecyclerView blockedRecyler;

    @BindView(R.id.shimmer)
    ShimmerFrameLayout shimmerFrameLayout;

    SlidrInterface slidrInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_tables);
        ButterKnife.bind(this);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.card_view_color));
        SharedPreferences sharedPreferences=getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng=sharedPreferences.getString("lng","en");
        Context language_context= LocaleHelper.setLocale(this,lng);
        Resources resources=language_context.getResources();

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

        blockedRecyler.setOnTouchListener(new View.OnTouchListener() {
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

        ParseQuery<ParseObject> parseObjectParseQuery=new ParseQuery<ParseObject>("live_tables");
        parseObjectParseQuery.whereContainedIn("objectId", ParseUser.getCurrentUser().getList("BLOCKED_TABLE"));
        parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if(objects.size()>0){


                    blockedRecyler.setLayoutManager(new LinearLayoutManager(blocked_tables.this,LinearLayoutManager.VERTICAL,false));
                    blockedRecyler.setAdapter(new block_unblock_table_adapter(objects,blocked_tables.this));
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);

                }
                else {

                    blockedRecyler.setLayoutManager(new LinearLayoutManager(blocked_tables.this,LinearLayoutManager.VERTICAL,false));
                    blockedRecyler.setAdapter(new if_not_found_adapter(blocked_tables.this, "", "No Blocked Tables", 0));
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);

                }

            }
        });


        anotherSettingTextview.setText(resources.getString(R.string.manageblocktable));
    }
    @OnClick({R.id.back_3})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.back_3:
                finish();
                overridePendingTransition(R.anim.stationary, R.anim.alerter_slide_out_to_bottom);
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.alerter_slide_out_to_bottom);
    }
}
