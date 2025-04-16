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

public class creator_of_activity extends AppCompatActivity {


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
    @BindView(R.id.textView3)
    TextView textView;
    List<ParseObject> ojj = new ArrayList<>();
    SlidrInterface slidrInterface;
    full_width_table_adapter full_width_table_adapter;

    int SKIP = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator_of_activity);
        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = getSharedPreferences("LANG", MODE_PRIVATE);
        String language = sharedPreferences.getString("lng", "en");
        Context context = LocaleHelper.setLocale(this, language);
        Resources resources = context.getResources();
        textView.setText(resources.getString(R.string.list_not_contain_private_table));

        textView24.setText(resources.getString(R.string.Moderator_of));
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

        slidrInterface = Slidr.attach(this, config);


        blockedRecyler.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    slidrInterface.lock();
                    // Do what you want
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {

                    slidrInterface.unlock();
                }
                return false;
            }
        });

        if (getIntent().getType() == null) {
            textView24.setText(resources.getString(R.string.Moderator_of));
            ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("live_tables");
            parseObjectParseQuery.setLimit(20);
            parseObjectParseQuery.whereEqualTo("table_creator", getIntent().getStringExtra("ID"));
            parseObjectParseQuery.whereEqualTo("IS_PRIVATE", false);
            parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (objects.size() > 0) {
                        ojj.addAll(objects);
                        full_width_table_adapter = new full_width_table_adapter(creator_of_activity.this, ojj);
                        blockedRecyler.setLayoutManager(new LinearLayoutManager(creator_of_activity.this, LinearLayoutManager.VERTICAL, false));
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
                                if (!blockedRecyler.canScrollVertically(1)) {

                                    SKIP = SKIP + 20;
                                    ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("live_tables");
                                    parseObjectParseQuery.setSkip(SKIP);
                                    parseObjectParseQuery.whereEqualTo("IS_PRIVATE", false);
                                    parseObjectParseQuery.setLimit(20);
                                    parseObjectParseQuery.whereEqualTo("table_creator", getIntent().getStringExtra("ID"));
                                    parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> objects, ParseException e) {

                                            if (objects.size() > 0) {
                                                ojj.addAll(SKIP, objects);
                                                full_width_table_adapter.notifyItemInserted(SKIP);
                                                full_width_table_adapter.notifyItemRangeInserted(SKIP, ojj.size());

                                            }


                                        }
                                    });


                                }
                            }
                        });

                    } else {

                        blockedRecyler.setLayoutManager(new LinearLayoutManager(creator_of_activity.this, LinearLayoutManager.VERTICAL, false));
                        blockedRecyler.setAdapter(new if_not_found_adapter(creator_of_activity.this, "", "No Tables Found.", 0));
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
