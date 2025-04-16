package com.voive.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
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
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.r0adkll.slidr.Slidr;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ban_peoples_activity extends AppCompatActivity {


    @BindView(R.id.back2)
    MaterialButton back2;
    @BindView(R.id.textView24)
    TextView textView24;
    @BindView(R.id.divider17)
    View divider17;
    @BindView(R.id.constraintLayout2)
    ConstraintLayout constraintLayout2;
    @BindView(R.id.blocked_recyler)
    RecyclerView blockedRecyler;

    List<ParseUser> USERS_LIST;
    int SKIP = 20;
    ban_unban_people_adapter ban_unban_people_adapter;
    @BindView(R.id.shimer)
    ShimmerFrameLayout shimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ban_peoples_activity);
        ButterKnife.bind(this);

        USERS_LIST = new ArrayList<>();

        Slidr.attach(this);
        SharedPreferences sharedPreferences = getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng = sharedPreferences.getString("lng", "en");
        Context language_context = LocaleHelper.setLocale(this, lng);
        Resources resources = language_context.getResources();
        textView24.setText(resources.getString(R.string.ban));

        PushDownAnim.setPushDownAnimTo(back2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                finish();
                overridePendingTransition(R.anim.stationary, R.anim.slide_to_start);
            }
        });

        ParseQuery<ParseObject> GETTED = new ParseQuery<ParseObject>("live_tables");
        GETTED.getInBackground(getIntent().getStringExtra("ID"), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {


                if (object.getList("BANS").size() > 0) {


                    ParseQuery<ParseUser> USERS = ParseUser.getQuery();
                    USERS.whereEqualTo("objectId", object.getList("BANS"));
                    USERS.countInBackground(new CountCallback() {
                        @Override
                        public void done(int count, ParseException e) {


                            ParseQuery<ParseUser> USERS = ParseUser.getQuery();
                            USERS.whereContainedIn("objectId", object.getList("BANS"));
                            USERS.setLimit(20);
                            USERS.findInBackground(new FindCallback<ParseUser>() {
                                @Override
                                public void done(List<ParseUser> objects, ParseException e) {

                                    USERS_LIST.addAll(objects);
                                    blockedRecyler.setLayoutManager(new LinearLayoutManager(ban_peoples_activity.this, LinearLayoutManager.VERTICAL, false));
                                    ban_unban_people_adapter = new ban_unban_people_adapter(USERS_LIST, ban_peoples_activity.this, getIntent().getStringExtra("ID"));
                                    blockedRecyler.setAdapter(ban_unban_people_adapter);
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
                                            if (!blockedRecyler.canScrollVertically(1) && blockedRecyler.getAdapter().getItemCount() < USERS_LIST.size()) {

                                                SKIP = SKIP + 20;

                                                ParseQuery<ParseUser> USERS = ParseUser.getQuery();
                                                USERS.whereEqualTo("objectId", USERS_LIST);
                                                USERS.countInBackground(new CountCallback() {
                                                    @Override
                                                    public void done(int count, ParseException e) {


                                                        ParseQuery<ParseUser> USERS = ParseUser.getQuery();
                                                        USERS.whereEqualTo("objectId", USERS_LIST);
                                                        USERS.setSkip(SKIP);
                                                        USERS.setLimit(20);
                                                        USERS.findInBackground(new FindCallback<ParseUser>() {
                                                            @Override
                                                            public void done(List<ParseUser> objects, ParseException e) {

                                                                if(objects.size()>0){
                                                                    USERS_LIST.addAll(SKIP, objects);
                                                                    ban_unban_people_adapter.notifyItemInserted(SKIP);
                                                                }



                                                            }
                                                        });

                                                    }
                                                });


                                            }
                                        }
                                    });

                                }
                            });

                        }
                    });


                } else {

                    blockedRecyler.setLayoutManager(new LinearLayoutManager(ban_peoples_activity.this, LinearLayoutManager.VERTICAL, false));
                    blockedRecyler.setAdapter(new if_not_found_adapter(ban_peoples_activity.this, "", "No Banned Speakers :)", 0));
                    shimer.setVisibility(View.GONE);
                    shimer.stopShimmer();

                }

            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.slide_to_start);
    }
}
