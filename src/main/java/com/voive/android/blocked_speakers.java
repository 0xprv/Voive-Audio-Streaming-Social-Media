package com.voive.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.thekhaeng.pushdownanim.PushDownAnim;
import com.xw.repo.widget.BounceScrollView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class blocked_speakers extends AppCompatActivity {


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
    @BindView(R.id.bounceScrollView)
    BounceScrollView bounceScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_speakers);
        ButterKnife.bind(this);
        //getting languages
        SharedPreferences sharedPreferences = getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng = sharedPreferences.getString("lng", "en");
        Context language_context = LocaleHelper.setLocale(this, lng);
        Resources resources = language_context.getResources();

        textView24.setText(resources.getString(R.string.manageblockuser));

        ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("roundtable_user_data");
        parseObjectParseQuery.whereEqualTo("User_ID", ParseUser.getCurrentUser().getObjectId());
        parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {


                for (ParseObject parseObject : objects) {

                    List<String> strings = parseObject.getList("Blocked");

                    if (strings.size() > 0) {
                        ParseQuery<ParseUser> USERS = ParseUser.getQuery();
                        USERS.whereEqualTo("objectId", strings);
                        USERS.findInBackground(new FindCallback<ParseUser>() {
                            @Override
                            public void done(List<ParseUser> objects, ParseException e) {


                            }
                        });
                    } else {

                        blockedRecyler.setLayoutManager(new LinearLayoutManager(blocked_speakers.this, LinearLayoutManager.VERTICAL, false));
                        blockedRecyler.setAdapter(new if_not_found_adapter(blocked_speakers.this, "", resources.getString(R.string.no_blocked_speaker), 0));
                    }


                }

            }
        });

        PushDownAnim.setPushDownAnimTo(back2).setOnClickListener(new View.OnClickListener() {
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
}
