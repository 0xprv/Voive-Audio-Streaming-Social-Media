package com.voive.android;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment;


public class app_starting_alert_dialog extends SupportBlurDialogFragment {


    public app_starting_alert_dialog() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view=getLayoutInflater().inflate(R.layout.if_app_starting_first_time_image,null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();


        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        double dd=0.20*width;
        double dd_hh=0.20*height;
        int new_width=width-(int)dd;
        int new_h=height-(int)dd_hh;

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
        layoutParams.width = 500;
     //   layoutParams.height=new_h;
        layoutParams.gravity= Gravity.CENTER;
        layoutParams.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        alertDialog.getWindow().setAttributes(layoutParams);
        return alertDialog;
    }

    @Override
    protected float getDownScaleFactor() {
        // Allow to customize the down scale factor.
        return 6.0f;
    }

    @Override
    protected int getBlurRadius() {
        // Allow to customize the blur radius factor.
        return 12;
    }

    @Override
    protected boolean isActionBarBlurred() {
        // Enable or disable the blur effect on the action bar.
        // Disabled by default.
        return true;
    }

    @Override
    protected boolean isDimmingEnable() {
        // Enable or disable the dimming effect.
        // Disabled by default.
        return true;
    }
    public static boolean id_url(String geti){

        String s="((http:\\/\\/|https:\\/\\/)?(www.)?(([a-zA-Z0-9-]){2,}\\.){1,4}([a-zA-Z]){2,6}(\\/([a-zA-Z-_\\/\\.0-9#:?=&;,]*)?)?)";
        Pattern p=Pattern.compile(s);
        Matcher m=p.matcher(geti);
        return m.find();

    }

    public boolean validate(String ss){

        try {
            URL obj = new URL(ss);
            obj.toURI();
            return true;
        } catch (MalformedURLException e) {
            return false;
        } catch (URISyntaxException e) {
            return false;
        }

    }
}
