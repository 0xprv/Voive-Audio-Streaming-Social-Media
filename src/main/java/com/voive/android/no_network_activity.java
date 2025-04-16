package com.voive.android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class no_network_activity extends AppCompatActivity {


    @BindView(R.id.title)
    TextView title;
            @BindView(R.id.description)
            TextView description;

    public static Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_network);
        ButterKnife.bind(this);
        activity=this;
        SharedPreferences sharedPreferences=getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng=sharedPreferences.getString("lng","en");
        Context language_context= LocaleHelper.setLocale(this,lng);
        Resources resources=language_context.getResources();
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        title.setText(resources.getString(R.string.you_are_offline));
        description.setText(resources.getString(R.string.you_not_have_internet));
    }

    @Override
    public void onBackPressed() {

    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.stationary);
    }
}