package com.voive.android;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class search_in_table extends AppCompatActivity {

    @BindView(R.id.drop)
    MaterialButton drop;
    @BindView(R.id.recyler_view)
    RecyclerView recyclerView;
    @BindView(R.id.search)
            TextView search_tvf;
    @BindView(R.id.name)
            TextView name;
    Resources resources;
    int TOSKIP=20;
    suggested_full_width_speakers_adapter live_table_creators_adapter;
    List<ParseUser> USERS=new ArrayList<>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_in_table);
        SharedPreferences sharedPreferences=getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng=sharedPreferences.getString("lng","en");
        Context language_context= LocaleHelper.setLocale(this,lng);
        resources=language_context.getResources();



      //  Window window = getWindow();
       // window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      //  window.setStatusBarColor(getResources().getColor(R.color.card_view_color));
        ButterKnife.bind(this);

        Slidr.attach(this);
        search_tvf.setText(resources.getString(R.string.Subscribers));
        PushDownAnim.setPushDownAnimTo(drop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.stationary, R.anim.slide_to_end);
            }
        });

        ParseQuery<ParseObject> parseObjectParseQuery=new ParseQuery<ParseObject>("live_tables");
        parseObjectParseQuery.getInBackground(getIntent().getStringExtra("ID"), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {


                name.setText(object.getString("table_name"));
                ParseQuery<ParseUser> last=ParseUser.getQuery();
                last.setLimit(20);
                last.whereContainedIn("objectId",  object.getList("table_subscribers"));
                last.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {

                        USERS.addAll(objects);
                        recyclerView.setLayoutManager(new LinearLayoutManager(search_in_table.this, LinearLayoutManager.VERTICAL, false));
                        live_table_creators_adapter=new suggested_full_width_speakers_adapter( search_in_table.this,USERS,search_in_table.this);
                        recyclerView.setAdapter(live_table_creators_adapter);
                        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                            }

                            @Override
                            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);
                                if (!recyclerView.canScrollVertically(1)) {

                                    TOSKIP = TOSKIP + 20;
                                    ParseQuery<ParseUser> LIVE = ParseUser.getQuery();
                                    LIVE.whereContainedIn("objectId",  object.getList("table_subscribers"));
                                    LIVE.setSkip(TOSKIP);
                                    LIVE.setLimit(20);
                                    LIVE.findInBackground(new FindCallback<ParseUser>() {
                                        @Override
                                        public void done(List<ParseUser> objects, ParseException e) {


                                            if(objects.size()>0){
                                                USERS.addAll(TOSKIP,objects);
                                                live_table_creators_adapter.notifyItemInserted(TOSKIP);
                                            }

                                        }
                                    });

                                }
                            }
                        });
                    }
                });


            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.slide_to_end);
    }
}
