package com.voive.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class server_error extends AppCompatActivity {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.description)
    TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_error);
        ButterKnife.bind(this);
        SharedPreferences sharedPreferences=getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng=sharedPreferences.getString("lng","en");
        Context language_context= LocaleHelper.setLocale(this,lng);
        Resources resources=language_context.getResources();
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        title.setText(resources.getString(R.string.Server_error));
        description.setText(resources.getString(R.string.Server_error_des));
    }
}