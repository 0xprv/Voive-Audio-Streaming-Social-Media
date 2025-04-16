package com.voive.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.model.SlidrPosition;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class my_tables_activity extends AppCompatActivity {

    @BindView(R.id.back)
    MaterialButton back;
    @BindView(R.id.invite_textview)
    TextView inviteTextview;
    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @BindView(R.id.shimmer)
    ShimmerFrameLayout shimmer;
    @BindView(R.id.recyler_view)
    RecyclerView recylerView;
    int SKIP=0;
    SlidrInterface slidrInterface;
    List<ParseObject> parseObjects=new ArrayList<>();
    full_width_table_adapter full_width_table_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tables_activity);
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

        inviteTextview.setText(resources.getString(R.string.my_tables));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.stationary, R.anim.slide_to_end);
            }
        });

        ParseQuery<ParseObject> parseObjectParseQuery=new ParseQuery<ParseObject>("live_tables");
        parseObjectParseQuery.setLimit(20);
        parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if(objects.size()>0){

                    parseObjects.addAll(objects);
                    recylerView.setLayoutManager(new LinearLayoutManager(my_tables_activity.this,LinearLayoutManager.VERTICAL,false));
                    full_width_table_adapter=new full_width_table_adapter(my_tables_activity.
                            this,parseObjects);
                    recylerView.setAdapter(full_width_table_adapter);

                    shimmer.stopShimmer();
                    shimmer.setVisibility(View.GONE);


                    recylerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                        }

                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            if(!recyclerView.canScrollVertically(1)){

                                SKIP=SKIP+20;

                                ParseQuery<ParseObject> parseObjectParseQuery=new ParseQuery<ParseObject>("live_tables");
                                parseObjectParseQuery.setLimit(20);
                                parseObjectParseQuery.setSkip(SKIP);
                                parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> objects, ParseException e) {


                                        if(objects.size()>0){

                                            parseObjects.addAll(0,objects);
                                            full_width_table_adapter.notifyItemInserted(0);
                                            full_width_table_adapter.notifyItemRangeInserted(0,objects.size());
                                        }

                                    }
                                });



                            }
                        }
                    });
                }
                else {

                    recylerView.setLayoutManager(new LinearLayoutManager(my_tables_activity.this,LinearLayoutManager.VERTICAL,false));
                    recylerView.setAdapter(new if_not_found_adapter(my_tables_activity.this,"Oh Snap!","No Table Found 😅",0));

                    shimmer.stopShimmer();
                    shimmer.setVisibility(View.GONE);

                }





            }
        });



    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.slide_to_end);
    }
}
