package com.voive.android;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.model.SlidrPosition;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class mutual_tables_activity extends AppCompatActivity {

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
    @BindView(R.id.textView3)
    TextView textView;
    full_width_table_adapter full_width_table_adapter;
    List<ParseObject> ojj=new ArrayList<>();
    SlidrInterface slidrInterface;
    int COUNT;
    int SKIP=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mutual_tables_activity);
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

        SharedPreferences sharedPreferences = getSharedPreferences("LANG", MODE_PRIVATE);
        String language = sharedPreferences.getString("lng", "en");
        Context context = LocaleHelper.setLocale(this, language);
        Resources resources = context.getResources();
        textView.setText(resources.getString(R.string.list_not_contain_private_table));
        if(getIntent().getType().equals("SUBS")){


            textView24.setText(resources.getString(R.string.subscribed_tables));

            ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("roundtable_user_data");
            parseObjectParseQuery.whereEqualTo("User_ID",getIntent().getStringExtra("ID"));
            parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objectsX, ParseException e) {

                    ParseQuery<ParseObject> LIVE_TABLES_QUERY = new ParseQuery<ParseObject>("live_tables");
                    LIVE_TABLES_QUERY.whereContainedIn("objectId", objectsX.get(0).getList("Subscriptions"));
                    LIVE_TABLES_QUERY.whereEqualTo("IS_PRIVATE",false);
                    LIVE_TABLES_QUERY.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if(objects.size()>0) {


                                ojj.addAll(objects);
                                blockedRecyler.setLayoutManager(new LinearLayoutManager(mutual_tables_activity.this,LinearLayoutManager.VERTICAL,false));
                                full_width_table_adapter=new full_width_table_adapter(mutual_tables_activity.this,ojj);
                                blockedRecyler.setAdapter(full_width_table_adapter);
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
                                        if(!blockedRecyler.canScrollVertically(1) && full_width_table_adapter!=null) {

                                            SKIP=SKIP+20;
                                            ParseQuery<ParseObject> parseObjectParseQuery=new ParseQuery<ParseObject>("live_tables");
                                            parseObjectParseQuery.setSkip(SKIP);
                                            parseObjectParseQuery.setLimit(20);
                                            parseObjectParseQuery.whereContainedIn("objectId", objectsX.get(0).getList("Subscriptions"));
                                            parseObjectParseQuery.whereEqualTo("IS_PRIVATE",false);
                                            parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                                                @Override
                                                public void done(List<ParseObject> objects, ParseException e) {

                                                    if(objects.size()>0){
                                                        ojj.addAll(SKIP,objects);
                                                        full_width_table_adapter.notifyItemInserted(SKIP);
                                                        full_width_table_adapter.notifyItemRangeInserted(SKIP,ojj.size());

                                                    }

                                                }
                                            });



                                        }
                                    }
                                });
                            }
                            else {

                                blockedRecyler.setLayoutManager(new LinearLayoutManager(mutual_tables_activity.this,LinearLayoutManager.VERTICAL,false));
                                blockedRecyler.setAdapter(new if_not_found_adapter(mutual_tables_activity.this, "", "No Mutual Tables.", 0));
                                shimer.setVisibility(View.GONE);
                                shimer.stopShimmer();

                            }

                        }
                    });



                }
            });



        }
        else {
            textView24.setText(resources.getString(R.string.mutual_tables));

            List<String> both=new ArrayList<>();
            both.add(ParseUser.getCurrentUser().getObjectId());
            both.add(getIntent().getStringExtra("ID"));
            ParseQuery<ParseObject> parseObjectParseQuery=new ParseQuery<ParseObject>("live_tables");
            parseObjectParseQuery.whereContainsAll("table_subscribers",both);
            parseObjectParseQuery.setLimit(20);

            parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if(objects.size()>0) {


                        ojj.addAll(objects);
                        blockedRecyler.setLayoutManager(new LinearLayoutManager(mutual_tables_activity.this,LinearLayoutManager.VERTICAL,false));
                        full_width_table_adapter=new full_width_table_adapter(mutual_tables_activity.this,ojj);
                        blockedRecyler.setAdapter(full_width_table_adapter);
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
                                if(!blockedRecyler.canScrollVertically(1) && full_width_table_adapter!=null) {

                                    SKIP=SKIP+20;
                                    ParseQuery<ParseObject> parseObjectParseQuery=new ParseQuery<ParseObject>("live_tables");
                                    parseObjectParseQuery.whereEqualTo("table_subscribers", ParseUser.getCurrentUser().getObjectId());
                                    parseObjectParseQuery.setSkip(SKIP);
                                    parseObjectParseQuery.setLimit(20);
                                    parseObjectParseQuery.whereEqualTo("table_subscribers", getIntent().getStringExtra("ID"));
                                    parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> objects, ParseException e) {

                                            if(objects.size()>0){
                                                ojj.addAll(SKIP,objects);
                                                full_width_table_adapter.notifyItemInserted(SKIP);
                                                full_width_table_adapter.notifyItemRangeInserted(SKIP,ojj.size());

                                            }


                                        }
                                    });


                                }
                            }
                        });
                    }
                    else {

                        blockedRecyler.setLayoutManager(new LinearLayoutManager(mutual_tables_activity.this,LinearLayoutManager.VERTICAL,false));
                        blockedRecyler.setAdapter(new if_not_found_adapter(mutual_tables_activity.this, "", "No Mutual Tables.", 0));
                        shimer.setVisibility(View.GONE);
                        shimer.stopShimmer();

                    }
                }
            });


        }


        back2.setOnClickListener(new View.OnClickListener() {
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
