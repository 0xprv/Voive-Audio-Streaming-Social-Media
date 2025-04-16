package com.voive.android;

import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.model.SlidrPosition;
import com.thekhaeng.pushdownanim.PushDownAnim;

import butterknife.BindView;
import butterknife.ButterKnife;

public class app_greeting extends AppCompatActivity {
    SlidrInterface slidrInterface;
    @BindView(R.id.back)
    MaterialButton back;
    @BindView(R.id.req)
    MaterialButton underst;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.textView14)
    TextView textView14;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_greeting);
        getWindow().setFlags(FLAG_LAYOUT_NO_LIMITS, FLAG_LAYOUT_NO_LIMITS);
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
                .edgeSize(18f)
                .build();

        slidrInterface = Slidr.attach(this, config);
        SharedPreferences sharedPreferences = getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng = sharedPreferences.getString("lng", "en");
        Context language_context = LocaleHelper.setLocale(this, lng);
        Resources resources = language_context.getResources();

        description.setText(resources.getString(R.string.greeting_to_people));
        underst.setText(resources.getString(R.string.i_under));
        textView14.setText(resources.getString(R.string.Place_for_talking));
        PushDownAnim.setPushDownAnimTo(back,underst).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
            }
        });

    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
    }
}