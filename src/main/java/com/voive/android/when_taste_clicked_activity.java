package com.voive.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.RequestQueue;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import com.google.android.material.tabs.TabLayoutMediator;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.model.SlidrPosition;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonHighlightAnimation;
import com.skydoves.balloon.BalloonSizeSpec;
import com.skydoves.balloon.overlay.BalloonOverlayAnimation;
import com.skydoves.balloon.overlay.BalloonOverlayRoundRect;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class when_taste_clicked_activity extends AppCompatActivity {
    @BindView(R.id.shimmer)
    ShimmerFrameLayout shimmerFrameLayout;
    @BindView(R.id.button100)
            MaterialButton back;
    @BindView(R.id.textView7)
            TextView title;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    LinearLayoutManager staggeredGridLayoutManager;
    Resources resources;
    SlidrInterface slidrInterface;
    full_width_table_adapter full_width_table_adapter_color;
    List<ParseObject> parseObjects=new ArrayList<>();
    int cont=0;
    int SKIP=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_when_taste_clicked_activity);
        ButterKnife.bind(this);




         Slidr.attach(this);

        SharedPreferences languagepref = getSharedPreferences("LANG", MODE_PRIVATE);
        String languagecode = languagepref.getString("lng", "en");
        Context context = LocaleHelper.setLocale(when_taste_clicked_activity.this, languagecode);
      resources = context.getResources();


     //   getWindow().setFlags(FLAG_LAYOUT_NO_LIMITS, FLAG_LAYOUT_NO_LIMITS);

        title.setText(getIntent().getStringExtra("NAME"));

        ParseQuery<ParseObject> In_PIENCE = new ParseQuery<ParseObject>("live_tables");
        In_PIENCE.setLimit(20);
        In_PIENCE.addDescendingOrder("createdAt");
        In_PIENCE.whereNotEqualTo("IS_PRIVATE",true);
        In_PIENCE.whereEqualTo("category", getIntent().getStringExtra("ID"));
        In_PIENCE.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if(objects.size()>0){
                    parseObjects.addAll(objects);

                    staggeredGridLayoutManager = new LinearLayoutManager(when_taste_clicked_activity.this,LinearLayoutManager.VERTICAL,false);

                    recyclerView.setLayoutManager(staggeredGridLayoutManager);
                    full_width_table_adapter_color=new full_width_table_adapter(when_taste_clicked_activity.this, parseObjects);
                    recyclerView.setAdapter(full_width_table_adapter_color);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);

                    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                        }

                        @Override
                        public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            if(!recyclerView.canScrollVertically(1)){


                                SKIP+=20;

                                ParseQuery<ParseObject> In_PIENCE = new ParseQuery<ParseObject>("live_tables");
                                In_PIENCE.setLimit(20);
                                In_PIENCE.setSkip(SKIP);
                                In_PIENCE.whereNotEqualTo("IS_PRIVATE",true);
                                In_PIENCE.addDescendingOrder("createdAt");
                                In_PIENCE.whereEqualTo("category", getIntent().getStringExtra("ID"));
                                In_PIENCE.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> objects, ParseException e) {


                                        if(objects.size()>0){

                                            parseObjects.addAll(SKIP,objects);
                                            full_width_table_adapter_color.notifyItemInserted(SKIP);
                                            full_width_table_adapter_color.notifyItemRangeInserted(SKIP,parseObjects.size());
                                        }


                                    }
                                });

                            }
                        }
                    });

                }
                else {
                    recyclerView.setLayoutManager(new LinearLayoutManager(when_taste_clicked_activity.this, LinearLayoutManager.VERTICAL, false));
                    recyclerView.setAdapter(new if_not_found_adapter(when_taste_clicked_activity.this, "",
                            "Sorry We Are Unable To Find Conversations of category ''" + getIntent().getStringExtra("NAME")+"'' because we are just setting up voive.", 0));
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                }



            }
        });
        
        PushDownAnim.setPushDownAnimTo( back).setOnClickListener(new View.OnClickListener() {
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



    public static int manipulateColor(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r,255),
                Math.min(g,255),
                Math.min(b,255));
    }
}
