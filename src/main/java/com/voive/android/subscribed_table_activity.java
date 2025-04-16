package com.voive.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class subscribed_table_activity extends AppCompatActivity {


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
    @BindView(R.id.if_not_found)
    RelativeLayout if_not_found;
    @BindView(R.id.bottom_text)
            TextView bottom_text;
    @BindView(R.id.description)
            TextView description;
    SlidrInterface slidrInterface;
    List<ParseUser> parseUserList = new ArrayList<>();
    manage_subscribed_table_adapter suggested_speakers_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribed_table_activity);
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


        slidrInterface = Slidr.attach(this, config);

        SharedPreferences sharedPreferences_lan = getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng = sharedPreferences_lan.getString("lng", "en");
        Context language_context = LocaleHelper.setLocale(this, lng);
        Resources resources = language_context.getResources();

        textView24.setText(resources.getString(R.string.subscriptions));
        description.setText(resources.getString(R.string.NO_Subscriptions_yet));

        ParseQuery<ParseObject> user_data = new ParseQuery<ParseObject>("roundtable_user_data");
        user_data.whereEqualTo("User_ID", ParseUser.getCurrentUser().getObjectId());
        user_data.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                List<String> SUBSCRIPTIONS_LIST = objects.get(0).getList("Subscriptions");
                assert SUBSCRIPTIONS_LIST != null;
                Collections.reverse(SUBSCRIPTIONS_LIST);


                ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("live_tables");
                parseObjectParseQuery.whereContainedIn("objectId", SUBSCRIPTIONS_LIST);
                parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (objects.size() > 0) {
                            bottom_text.setText(Integer.toString(objects.size())+" "+resources.getString(R.string.subscriptions));
                            blockedRecyler.setVisibility(View.VISIBLE);
                            if_not_found.setVisibility(View.GONE);
                            suggested_speakers_adapter = new manage_subscribed_table_adapter(subscribed_table_activity.this, objects);
                            suggested_speakers_adapter.setHasStableIds(true);
                            blockedRecyler.setLayoutManager(new LinearLayoutManager(subscribed_table_activity.this, LinearLayoutManager.VERTICAL, false));
                            blockedRecyler.setAdapter(suggested_speakers_adapter);
                            shimer.stopShimmer();
                            shimer.setVisibility(View.GONE);

                        } else {
                            bottom_text.setText(resources.getString(R.string.NO_Subscriptions_yet));
                            blockedRecyler.setVisibility(View.GONE);
                            if_not_found.setVisibility(View.VISIBLE);
                            shimer.stopShimmer();
                            shimer.setVisibility(View.GONE);

                        }

                    }
                });

            }
        });


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
