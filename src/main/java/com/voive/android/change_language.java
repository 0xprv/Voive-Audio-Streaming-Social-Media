package com.voive.android;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.model.SlidrPosition;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class change_language extends AppCompatActivity {


    public static Activity activity;
    List<recommanded_language_modal_class> recommanded_language_modal_classes = new ArrayList<>();
    recommanded_language_modal_class recommanded_language_modal_class;
    @BindView(R.id.recommanded_recylerview)
    RecyclerView recommandedRecylerview;
    @BindView(R.id.textView29)
    TextView textView29;
    @BindView(R.id.materialButton2)
    MaterialButton materialButton2;

    SlidrInterface slidrInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);
        ButterKnife.bind(this);
        activity=this;
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

        SharedPreferences languagepref = getSharedPreferences("LANG", MODE_PRIVATE);
        String languagecode = languagepref.getString("lng", "en");
        activity = this;
        Context context = LocaleHelper.setLocale(this, languagecode);
        Resources resources = context.getResources();
        textView29.setText(resources.getString(R.string.language));


        recommandedRecylerview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_MOVE){

                    slidrInterface.lock();
                    // Do what you want
                }
                else if(event.getAction() == MotionEvent.ACTION_CANCEL){

                    slidrInterface.unlock();
                }
                return false;
            }
        });

        String[] name_of_language = {"English",
                "Bengali",
                "Gujarati",
                "Hindi",
                "Marathi",
                "Punjabi",
                "Tamil",
                "Telugu",};

        String[] trandition_of_language = {"English",
                "বাংলা",
                "ગુજરાતી",
                "हिंदी",
                "मराठी",
                "ਪੰਜਾਬੀ",
                "தமிழ்",
                "తెలుగు"
        };

        String[] language_codes = {"en",
                "bn",
                "gu",
                "hi",
                "mr",
                "pa",
                "ta",
                "te",
        };
        for (int i = 0; i < language_codes.length; i++) {


            recommanded_language_modal_class = new recommanded_language_modal_class();
            recommanded_language_modal_class.setName(name_of_language[i]);
            recommanded_language_modal_class.setTranditional(trandition_of_language[i]);
            recommanded_language_modal_class.setCode(language_codes[i]);
            recommanded_language_modal_classes.add(recommanded_language_modal_class);


        }

        recommandedRecylerview.setLayoutManager(new LinearLayoutManager(change_language.this,LinearLayoutManager.VERTICAL,false));
        recommandedRecylerview.setAdapter(new change_language_adapter(change_language.this,recommanded_language_modal_classes));


        PushDownAnim.setPushDownAnimTo(materialButton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.stationary, R.anim.alerter_slide_out_to_bottom);

            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.alerter_slide_out_to_bottom);
    }
}
