package com.voive.android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.model.SlidrPosition;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class main_explore_page_of_app extends AppCompatActivity {

    @BindView(R.id.close_button)
    MaterialButton closeButton;
    @BindView(R.id.searcher_recyler)
    RecyclerView searcherRecyler;

    List<ParseObject> ALL_TABLES = new ArrayList<>();
    SlidrInterface slidrInterface;
    //Adapter
    full_width_table_adapter full_width_table_adapter;

    int SKIP = 0;
    int COUNT = 0;
    @BindView(R.id.shimmer)
    ShimmerFrameLayout shimmer;
    @BindView(R.id.tap)
    TextView tap;
    @BindView(R.id.loading_indicator)
    ProgressBar progressBar;
    @BindView(R.id.ttl)
    TextView ttl;
    @BindView(R.id.first_time_swipe_right_constrain)
    ConstraintLayout first_time_swipe_right_constrain;
    @BindView(R.id.tap_here)
    TextView tap_here;
    @BindView(R.id.deactivate_message_textview)
    TextView deactivate_message_textview;
    @BindView(R.id.create_account)
    MaterialButton create_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_explore_page_of_app);

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
        SharedPreferences languagepref = getSharedPreferences("LANG", MODE_PRIVATE);
        String languagecode = languagepref.getString("lng", "en");
        boolean FIRST_TIME = languagepref.getBoolean("IS_FIRST", true);
        boolean FIRST_TIME_TAP = languagepref.getBoolean("IS_FIRST_TAP_TO_LISTEN", true);
        Context context = LocaleHelper.setLocale(this, languagecode);
        Resources resources = context.getResources();

        tap_here.setText(resources.getString(R.string.Tap_here));
        deactivate_message_textview.setText(resources.getString(R.string.to_listen_conv));

        ttl.setText(resources.getString(R.string.Suggested_tables));
        //  AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        tap.setText(resources.getString(R.string.tap_what_people_talk));

        if (FIRST_TIME) {

            AlertDialog.Builder builder = new AlertDialog.Builder(main_explore_page_of_app.this);
            builder.setView(R.layout.explore_tables_alert_introduction);

            AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            alertDialog.show();

            TextView deactivate_textview = alertDialog.findViewById(R.id.deactivate_textview);
            TextView d_message = alertDialog.findViewById(R.id.deactivate_message_textview);
            MaterialButton deactivate = alertDialog.findViewById(R.id.deactivate);


            d_message.setText(resources.getString(R.string.what_people_talking_about));
            deactivate.setText(resources.getString(R.string.Listen));
            deactivate_textview.setText(resources.getString(R.string.EXPLORE));


            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    SharedPreferences.Editor editor = languagepref.edit();
                    editor.putBoolean("IS_FIRST", false);
                    editor.apply();
                }
            });
            PushDownAnim.setPushDownAnimTo(deactivate).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    alertDialog.cancel();
                    SharedPreferences.Editor editor = languagepref.edit();
                    editor.putBoolean("IS_FIRST", false);
                    editor.apply();
                }
            });
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            double dd = 0.30 * width;
            int new_width = width - (int) dd;
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
            layoutParams.width = new_width;
            alertDialog.getWindow().setAttributes(layoutParams);


        }

        ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("live_tables");
        parseObjectParseQuery.setLimit(20);
        parseObjectParseQuery.whereEqualTo("IS_PRIVATE",false);
        parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                ALL_TABLES.addAll(objects);
                full_width_table_adapter = new full_width_table_adapter(main_explore_page_of_app.this, ALL_TABLES);
                searcherRecyler.setLayoutManager(new LinearLayoutManager(main_explore_page_of_app.this, LinearLayoutManager.VERTICAL, false));
                searcherRecyler.setAdapter(full_width_table_adapter);
                shimmer.setVisibility(View.GONE);
                shimmer.stopShimmer();
                if(FIRST_TIME_TAP){
                    first_time_swipe_right_constrain.setVisibility(View.VISIBLE);
                    create_account.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            first_time_swipe_right_constrain.setVisibility(View.GONE);
                            SharedPreferences.Editor editor = languagepref.edit();
                            editor.putBoolean("IS_FIRST_TAP_TO_LISTEN", false);
                            editor.apply();
                        }
                    });

                }

            }
        });

        searcherRecyler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (full_width_table_adapter != null && !searcherRecyler.canScrollVertically(1)) {

                    progressBar.setVisibility(View.VISIBLE);
                    SKIP = SKIP + 20;
                    ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("live_tables");
                    parseObjectParseQuery.setSkip(SKIP);
                    parseObjectParseQuery.setLimit(20);
                    parseObjectParseQuery.whereEqualTo("IS_PRIVATE",false);
                    parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {

                            if (objects.size() > 0) {
                                ALL_TABLES.addAll(SKIP, objects);
                                full_width_table_adapter.notifyItemRangeInserted(SKIP, ALL_TABLES.size());
                                //    full_width_table_adapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                            }

                            progressBar.setVisibility(View.GONE);
                        }
                    });


                }
            }
        });


        closeButton.setOnClickListener(new View.OnClickListener() {
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
