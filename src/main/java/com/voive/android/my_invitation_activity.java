package com.voive.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class my_invitation_activity extends AppCompatActivity {

    @BindView(R.id.back)
    MaterialButton back;
    @BindView(R.id.invite_textview)
    TextView inviteTextview;
    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @BindView(R.id.shimmer)
    ShimmerFrameLayout shimmer;
    @BindView(R.id.blocked_recyler)
    RecyclerView recyclerView;
    @BindView(R.id.how_inv)
            TextView how_inv;
    invitation_adapter invitation_adapter;

    int TOTAL_COUNT = 0;
    int SKIP = 20;

    List<ParseObject> OBJ = new ArrayList<>();
    SlidrInterface slidrInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_invitation_activity);
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

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    slidrInterface.lock();
                    // Do what you want
                } else {

                    slidrInterface.unlock();
                }
                return false;
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("LANG", MODE_PRIVATE);
        String language = sharedPreferences.getString("lng", "en");
        Context context = LocaleHelper.setLocale(this, language);
        Resources resources = context.getResources();

        inviteTextview.setText(resources.getString(R.string.Invitation));

        PushDownAnim.setPushDownAnimTo(back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.stationary, R.anim.bottom_down);
            }
        });


        ParseQuery<ParseObject> parseObjectParseQuery1 = new ParseQuery<ParseObject>("Notifications");
        parseObjectParseQuery1.whereEqualTo("USER_ID", ParseUser.getCurrentUser().getObjectId());
        parseObjectParseQuery1.whereEqualTo("TYPE", Constant.SOME_ONE_SEND_INVITATION);
        parseObjectParseQuery1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                for(ParseObject pp:objects){
                    pp.deleteInBackground();
                }
            }
        });

        ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("roundtable_user_data");
        parseObjectParseQuery.whereEqualTo("User_ID", ParseUser.getCurrentUser().getObjectId());
        parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {


                for (ParseObject parseObject : objects) {


                    List<String> INVITES = parseObject.getList("Invitations");

                    ParseQuery<ParseObject> TABLES = new ParseQuery<ParseObject>("live_tables");
                    TABLES.whereContainedIn("objectId", INVITES);
                    TABLES.setLimit(20);
                    TABLES.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            how_inv.setText(Integer.toString(objects.size())+" "+resources.getString(R.string.Invitation));
                            recyclerView.setVisibility(View.VISIBLE);
                            invitation_adapter suggested_speakers_adapter = new invitation_adapter( objects,my_invitation_activity.this);
                            suggested_speakers_adapter.setHasStableIds(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(my_invitation_activity.this, LinearLayoutManager.VERTICAL, false));
                            recyclerView.setAdapter(suggested_speakers_adapter);
                            shimmer.stopShimmer();
                            shimmer.setVisibility(View.GONE);
                        }
                    });


                }

            }
        });


    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.bottom_down);

    }
}
