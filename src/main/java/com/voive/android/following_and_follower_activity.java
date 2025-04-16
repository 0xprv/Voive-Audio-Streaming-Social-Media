package com.voive.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class following_and_follower_activity extends AppCompatActivity {

    @BindView(R.id.back2)
    MaterialButton back2;
    @BindView(R.id.textView24)
    TextView textView24;
    @BindView(R.id.constraintLayout2)
    ConstraintLayout constraintLayout2;
    @BindView(R.id.shimer)
    ShimmerFrameLayout shimer;
    @BindView(R.id.blocked_recyler)
    RecyclerView blockedRecyler;
    suggested_full_width_speakers_adapter live_table_subscribers_adapter;
    List<String> following;
    List<ParseUser> USERS;
    int TOSKIP=20;
    Resources resources;

    SlidrInterface slidrInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_and_follower_activity);
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

        SharedPreferences sharedPreferences = getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng = sharedPreferences.getString("lng", "en");
        Context language_context = LocaleHelper.setLocale(this, lng);
        resources = language_context.getResources();

        following=new ArrayList<>();
        USERS=new ArrayList<>();

        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.stationary, R.anim.alerter_slide_out_to_bottom);

            }
        });

        ParseQuery<ParseObject> pp=new ParseQuery<ParseObject>("roundtable_user_data");
        pp.whereEqualTo("User_ID",getIntent().getStringExtra("ID"));
        pp.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                for(ParseObject object:objects){


                    following.addAll(object.getList("who_notify"));
                    textView24.setText(resources.getString(R.string.Notified));

                    ParseQuery<ParseUser> ppp = ParseUser.getQuery();
                    ppp.whereContainedIn("objectId", following);
                    ppp.setLimit(20);
                    ppp.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {

                            if(objects.size()>0){
                                USERS.addAll(objects);
                                live_table_subscribers_adapter =new suggested_full_width_speakers_adapter( following_and_follower_activity.this,USERS,following_and_follower_activity.this);
                                blockedRecyler.setLayoutManager(new LinearLayoutManager(following_and_follower_activity.this, LinearLayoutManager.VERTICAL, false));
                                blockedRecyler.setAdapter(live_table_subscribers_adapter);
                                shimer.setVisibility(View.GONE);
                                shimer.stopShimmer();
                                blockedRecyler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                    @Override
                                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                        super.onScrollStateChanged(recyclerView, newState);
                                    }

                                    @Override
                                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                        super.onScrolled(recyclerView, dx, dy);
                                        if (!recyclerView.canScrollVertically(1) && live_table_subscribers_adapter!=null) {

                                            TOSKIP = TOSKIP + 20;
                                            ParseQuery<ParseUser> LIVE = ParseUser.getQuery();
                                            LIVE.whereContainedIn("objectId", following);
                                            LIVE.setSkip(TOSKIP);
                                            LIVE.setLimit(20);
                                            LIVE.findInBackground(new FindCallback<ParseUser>() {
                                                @Override
                                                public void done(List<ParseUser> objects, ParseException e) {


                                                    if(objects.size()>0){
                                                        USERS.addAll(TOSKIP,objects);
                                                        live_table_subscribers_adapter.notifyItemInserted(TOSKIP);
                                                    }

                                                }
                                            });

                                        }
                                    }
                                });
                            }
                            else {

                                blockedRecyler.setLayoutManager(new LinearLayoutManager(following_and_follower_activity.this, LinearLayoutManager.VERTICAL, false));
                                blockedRecyler.setAdapter(new if_not_found_adapter(following_and_follower_activity.this, "", "Too Quite Here 😅", 0));
                                shimer.setVisibility(View.GONE);
                                shimer.stopShimmer();
                            }


                        }
                    });

                }

            }
        });



    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.alerter_slide_out_to_bottom);
    }
}
