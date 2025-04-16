package com.voive.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
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


public class nearby_tables extends AppCompatActivity {

    @BindView(R.id.textView24)
    TextView textView24;
    @BindView(R.id.back2)
    MaterialButton back;
    @BindView(R.id.blocked_recyler)
    RecyclerView blockedRecyler;
    @BindView(R.id.shimer)
    ShimmerFrameLayout shimer;
    SlidrInterface slidrInterface;
    full_width_table_adapter full_width_table_adapter;
    List<ParseObject> ojj=new ArrayList<>();
    int SKIP=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_tables);
        ButterKnife.bind(this);



        ParseQuery<ParseObject> parseObjectParseQuery=new ParseQuery<ParseObject>("live_tables");
        parseObjectParseQuery.whereNear("Location", getCurrentUserLocation());
        parseObjectParseQuery.whereNotEqualTo("Location","Nil");
        parseObjectParseQuery.whereNotEqualTo("IS_PRIVATE",true);
        parseObjectParseQuery.setLimit(20);
        parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects.size()>0) {


                    ojj.addAll(objects);
                    blockedRecyler.setLayoutManager(new LinearLayoutManager(nearby_tables.this,LinearLayoutManager.VERTICAL,false));
                    full_width_table_adapter=new full_width_table_adapter(nearby_tables.this,ojj);
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

                    blockedRecyler.setLayoutManager(new LinearLayoutManager(nearby_tables.this,LinearLayoutManager.VERTICAL,false));
                    blockedRecyler.setAdapter(new if_not_found_adapter(nearby_tables.this, "", "No Mutual Tables.", 0));
                    shimer.setVisibility(View.GONE);
                    shimer.stopShimmer();

                }
            }
        });

        PushDownAnim.setPushDownAnimTo(back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.stationary, R.anim.alerter_slide_out_to_bottom);
            }
        });

        // Uri uri = getIntent().getData();

        // checking if the uri is null or not.
      /*  if (uri != null) {

            if(uri.getPath().equals("tables/invites/")){


            }
            else if(uri.getPath().equals("/speakers/")){


            }
            else if(uri.getPath().equals("/sneaks/")){


                if(uri.getQueryParameter("id")!=null){

                    Intent intent=new Intent(connect_to_social_activity.this,sound_bite.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_from_end, R.anim.stationary);

                }
                else {

                    Toast.makeText(this, "Broken Or Invalid Link", Toast.LENGTH_SHORT).show();

                }

            }


        }*/

    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.alerter_slide_out_to_bottom);
    }
    private ParseGeoPoint getCurrentUserLocation(){

        // finding currentUser
        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser == null) {
            // if it's not possible to find the user, do something like returning to login activity
        }
        // otherwise, return the current user location
        return currentUser.getParseGeoPoint("Location");

    }
}
