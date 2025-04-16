package com.voive.android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.piasy.biv.BigImageViewer;
import com.github.piasy.biv.loader.glide.GlideImageLoader;
import com.github.piasy.biv.view.BigImageView;
import com.google.android.material.button.MaterialButton;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class zoom_image_viewer extends AppCompatActivity {


    @BindView(R.id.mBigImage)
    BigImageView mBigImage;
    @BindView(R.id.close)
    MaterialButton close;

    @BindView(R.id.share)
    MaterialButton share;



    @SuppressLint({"CheckResult", "WrongThread"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BigImageViewer.initialize(GlideImageLoader.with(this));
        setContentView(R.layout.activity_zoom_image_viewer);
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

        Slidr.attach(this, config);

        Window w = getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        ButterKnife.bind(this);

        String sss = getIntent().getStringExtra("file_photo_url");
        mBigImage.showImage(Uri.parse(getIntent().getStringExtra("file_photo_url")));
        Glide.with(this).asBitmap()
                .load(sss)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                        // Extract color
                        Palette.from(resource).generate(p -> {

                            Palette.Swatch swatch;
                            swatch = p.getDarkVibrantSwatch();



                            if (swatch != null) {

                                mBigImage.setBackgroundColor(swatch.getRgb());


                            } else {


                                mBigImage.setBackgroundColor(getResources().getColor(android.R.color.black));


                            }



                        });
                    }
                });

        PushDownAnim.setPushDownAnimTo(share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Glide.with(zoom_image_viewer.this).asBitmap().load(getIntent().getStringExtra("file_photo_url")).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        PackageManager pm = getPackageManager();
                        try {
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            resource.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                            String path = MediaStore.Images.Media.insertImage(getContentResolver(), resource, "Title", null);
                            Uri imageUri = Uri.parse(path);



                            Intent waIntent = new Intent(Intent.ACTION_SEND);
                            waIntent.setType("image/*");
                            waIntent.putExtra(android.content.Intent.EXTRA_STREAM, imageUri);
                            startActivity(Intent.createChooser(waIntent, "Share with"));
                        } catch (Exception e) {
                            Log.e("Error on sharing", e + " ");
                        }
                    }
                });


            }
        });


PushDownAnim.setPushDownAnimTo(close).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        finish();
        overridePendingTransition(R.anim.stationary, R.anim.alerter_slide_out_to_bottom);
    }
});

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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.alerter_slide_out_to_bottom);
    }
}


