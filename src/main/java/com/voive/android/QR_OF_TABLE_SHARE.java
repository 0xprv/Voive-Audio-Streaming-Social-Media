package com.voive.android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.sumimakito.awesomeqr.AwesomeQrRenderer;
import com.github.sumimakito.awesomeqr.RenderResult;
import com.github.sumimakito.awesomeqr.option.RenderOption;
import com.github.sumimakito.awesomeqr.option.color.Color;
import com.github.sumimakito.awesomeqr.option.logo.Logo;
import com.google.android.material.button.MaterialButton;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;

public class QR_OF_TABLE_SHARE extends AppCompatActivity {

    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.back2)
    MaterialButton back2;
    @BindView(R.id.share)
    MaterialButton share;
    String QR ;
    @BindView(R.id.bg)
    RelativeLayout bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r__o_f__t_a_b_l_e__s_h_a_r_e);
        ButterKnife.bind(this);
        getWindow().setFlags(FLAG_LAYOUT_NO_LIMITS, FLAG_LAYOUT_NO_LIMITS);
        SlidrConfig config = new SlidrConfig.Builder()
                .position(SlidrPosition.TOP)
                .sensitivity(1f)
                .scrimColor(android.graphics.Color.BLACK)
                .scrimStartAlpha(0.8f)
                .scrimEndAlpha(0f)
                .velocityThreshold(2400)
                .distanceThreshold(0.25f)
                .edge(true)
                .edgeSize(0.18f)
                .build();

        Slidr.attach(this,config);
        PushDownAnim.setPushDownAnimTo(back2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.stationary, R.anim.bottom_down);
            }
        });

        ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("live_tables");
        parseObjectParseQuery.getInBackground(getIntent().getStringExtra("ID"), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {

                Glide.with(QR_OF_TABLE_SHARE.this).asBitmap().load(object.getParseFile("table_image").getUrl()).circleCrop()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                                Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(@Nullable Palette palette) {

                                        Palette.Swatch swatch = palette.getLightVibrantSwatch() != null
                                                ? palette.getLightVibrantSwatch() : palette.getDominantSwatch();

                                        GradientDrawable gd = new GradientDrawable(
                                                GradientDrawable.Orientation.TOP_BOTTOM,
                                                new int[] {swatch.getRgb(),0x00000000});
                                        gd.setCornerRadius(0f);

                                        bg.setBackgroundDrawable(gd);
                                        Logo logo = new Logo();
                                        logo.setBitmap(resource);
                                        logo.setBorderRadius(0); // radius for logo's corners
                                        logo.setBorderWidth(10); // width of the border to be added around the logo
                                        logo.setScale(0.3f); // scale for the logo in the QR code
                                       // logo.setClippingRect(new RectF(0, 0, 180, 180)); // crop the logo image before applying it to the QR code
                                        Color color = new Color();
                                        color.setLight(0xFFFFFFFF); // for blank spaces
                                        color.setDark(swatch.getRgb()); // for non-blank spaces
                                        color.setBackground(0xFFFFFFFF); // for the background (will be overriden by background images, if set)
                                        color.setAuto(false);

                                        QR="https://voive.in/tables/invites/?id="+getIntent().getStringExtra("ID");

                                        RenderOption renderOption = new RenderOption();
                                        renderOption.setContent("https://voive.in/tables/invites/?id="+getIntent().getStringExtra("ID")); // content to encode
                                        renderOption.setSize(800); // size of the final QR code image
                                        renderOption.setBorderWidth(20); // width of the empty space around the QR code
                                        renderOption.setEcl(ErrorCorrectionLevel.M); // (optional) specify an error correction level
                                        renderOption.setPatternScale(0.50f); // (optional) specify a scale for patterns
                                        renderOption.setRoundedPatterns(false); // (optional) if true, blocks will be drawn as dots instead
                                        renderOption.setClearBorder(true); // if set to true, the background will NOT be drawn on the border area
                                        renderOption.setColor(color); // set a color palette for the QR code
                                        renderOption.setLogo(logo); // set a logo, keep reading to find more about it

                                        try {
                                            RenderResult result = AwesomeQrRenderer.render(renderOption);
                                            if (result.getBitmap() != null) {
                                                // play with the bitmap
                                                image.setImageBitmap(result.getBitmap());
                                                PushDownAnim.setPushDownAnimTo(share).setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        //---Save bitmap to external cache directory---//
                                                        //get cache directory
                                                        File cachePath = new File(getExternalCacheDir(), "my_images/");
                                                        cachePath.mkdirs();

                                                        //create png file
                                                        File file = new File(cachePath, "Image_123.png");
                                                        FileOutputStream fileOutputStream;
                                                        try {
                                                            fileOutputStream = new FileOutputStream(file);
                                                            result.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                                                            fileOutputStream.flush();
                                                            fileOutputStream.close();

                                                        } catch (FileNotFoundException e) {
                                                            e.printStackTrace();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }

                                                        //---Share File---//
                                                        //get file uri
                                                        Uri myImageFileUri = FileProvider.getUriForFile(QR_OF_TABLE_SHARE.this, getApplicationContext().getPackageName() + ".provider", file);

                                                        //create a intent
                                                        Intent intent = new Intent(Intent.ACTION_SEND);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                        intent.putExtra(Intent.EXTRA_STREAM, myImageFileUri);
                                                        intent.putExtra(Intent.EXTRA_SUBJECT, "QR Code");
                                                        intent.putExtra(Intent.EXTRA_TEXT, "सुनने "+object.getString("table_name")+ " में क्या बात चित हो रही है। ->" + QR);
                                                        intent.setType("image/png");
                                                        startActivity(Intent.createChooser(intent, "Share with"));

                                                    }
                                                });
                                            }
                                        } catch (Exception e) {
                                            Toast.makeText(QR_OF_TABLE_SHARE.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            e.printStackTrace();
                                            // Oops, something gone wrong.
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
        overridePendingTransition(R.anim.stationary, R.anim.bottom_down);
    }
}