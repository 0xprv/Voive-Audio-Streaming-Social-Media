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


public class starting_language_picker extends AppCompatActivity {

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
        setContentView(R.layout.activity_starting_language_picker);
        ButterKnife.bind(this);


       Slidr.attach(this);

        SharedPreferences languagepref = getSharedPreferences("LANG", MODE_PRIVATE);
        String languagecode = languagepref.getString("lng", "hi");
        activity = this;
        Context context = LocaleHelper.setLocale(starting_language_picker.this, languagecode);
        Resources resources = context.getResources();







        textView29.setText(resources.getString(R.string.language));

        SharedPreferences languageprefx=context.getApplicationContext().getSharedPreferences("LANG",0);
        SharedPreferences.Editor editor = languageprefx.edit();
        editor.putString("lng", "hi");
        editor.apply();


        String[] name_of_language = {"Hindi",
                "Gujarati",
                "Marathi",
                "Punjabi",
                "Tamil",
                "Telugu",
                "English"};

        String[] trandition_of_language = {"हिंदी",
                "ગુજરાતી",
                "मराठी",
                "ਪੰਜਾਬੀ",
                "தமிழ்",
                "తెలుగు",
                "English"
        };

        String[] language_codes = {"hi",
                "gu-rIN",
                "mr-rIN",
                "pa",
                "ta",
                "te",
                "en"
        };
        for (int i = 0; i < language_codes.length; i++) {


            recommanded_language_modal_class = new recommanded_language_modal_class();
            recommanded_language_modal_class.setName(name_of_language[i]);
            recommanded_language_modal_class.setTranditional(trandition_of_language[i]);
            recommanded_language_modal_class.setCode(language_codes[i]);
            recommanded_language_modal_classes.add(recommanded_language_modal_class);


        }

        recommandedRecylerview.setLayoutManager(new LinearLayoutManager(starting_language_picker.this,LinearLayoutManager.VERTICAL,false));
        recommandedRecylerview.setAdapter(new change_language_adapter_starting(recommanded_language_modal_classes,starting_language_picker.this));


        PushDownAnim.setPushDownAnimTo(materialButton2).setOnClickListener(new View.OnClickListener() {
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
