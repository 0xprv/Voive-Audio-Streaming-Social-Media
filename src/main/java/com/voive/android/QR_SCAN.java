package com.voive.android;

import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.material.button.MaterialButton;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.model.SlidrPosition;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.thekhaeng.pushdownanim.PushDownAnim;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QR_SCAN extends AppCompatActivity {
    @BindView(R.id.back2)
    MaterialButton back2;
    SlidrInterface slidrInterface;
    private CodeScanner mCodeScanner;
    Resources resources;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r__s_c_a_n);
        ButterKnife.bind(this);
        getWindow().setFlags(FLAG_LAYOUT_NO_LIMITS, FLAG_LAYOUT_NO_LIMITS);
        SharedPreferences sharedPreferences = getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng = sharedPreferences.getString("lng", "en");
        Context language_context = LocaleHelper.setLocale(this, lng);
         resources = language_context.getResources();
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

        slidrInterface = Slidr.attach(this, config);

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);

        mCodeScanner.setCamera(CodeScanner.CAMERA_BACK);
        mCodeScanner.setFormats(CodeScanner.ALL_FORMATS);
        mCodeScanner.setAutoFocusEnabled(true);
        mCodeScanner.setFlashEnabled(false);
        mCodeScanner.startPreview();
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Uri uri = Uri.parse(result.getText());

                        if (uri != null) {
                            if(uri.getHost().equals("www.voive.in") || uri.getHost().equals("voive.in")){
                                if (uri.getPath().equals("/tables/invites/")) {
                                    if (uri.getQueryParameter("id") != null) {

                                        String id=uri.getQueryParameter("id");

                                        ParseQuery<ParseObject> parseObjectParseQuery=ParseQuery.getQuery("live_tables");
                                        parseObjectParseQuery.getInBackground(id, new GetCallback<ParseObject>() {
                                            @Override
                                            public void done(ParseObject object, ParseException e) {



                                                if (object.getBoolean("Active")) {

                                                    if (object.getList("co_creator").contains(ParseUser.getCurrentUser().getObjectId()) || object.getString("table_creator").equals(ParseUser.getCurrentUser().getObjectId())
                                                            || object.getList("table_subscribers").contains(ParseUser.getCurrentUser().getObjectId())) {


                                                        Intent intent = new Intent(QR_SCAN.this, special_speaker_live_table.class);
                                                        intent.putExtra("ID", object.getObjectId());
                                                        startActivity(intent);
                                                        finish();
                                                        overridePendingTransition(R.anim.slide_from_end, R.anim.stationary);
                                                    } else {

                                                        if (!object.getList("BANS").contains(ParseUser.getCurrentUser().getObjectId())) {
                                                            Intent intent = new Intent(QR_SCAN.this, sound_preview.class);
                                                            intent.putExtra("ID", object.getObjectId());
                                                            startActivity(intent);
                                                            finish();
                                                            overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
                                                        } else {


                                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(QR_SCAN.this, R.style.AlertDialogCustom);
                                                            builder1.setMessage(resources.getString(R.string.staff_ban_you));
                                                            builder1.setTitle(resources.getString(R.string.Banned));
                                                            builder1.setCancelable(false);

                                                            builder1.setPositiveButton(
                                                                    resources.getString(R.string.OK),
                                                                    new DialogInterface.OnClickListener() {
                                                                        public void onClick(DialogInterface dialog, int id) {
                                                                            dialog.dismiss();
                                                                        }
                                                                    });


                                                            AlertDialog alert11 = builder1.create();
                                                            alert11.show();

                                                        }

                                                    }
                                                }
                                                else {

                                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(QR_SCAN.this, R.style.AlertDialogCustom);
                                                    builder1.setMessage(resources.getString(R.string.SORRY_CREATOR_DISMISSED));
                                                    builder1.setTitle(resources.getString(R.string.dismiss_table));
                                                    builder1.setCancelable(false);

                                                    builder1.setPositiveButton(
                                                            resources.getString(R.string.OK),
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    dialog.dismiss();
                                                                }
                                                            });


                                                    AlertDialog alert11 = builder1.create();
                                                    alert11.show();
                                                }


                                            }
                                        });
                                    } else {

                                        Toast.makeText(QR_SCAN.this, "Broken Or Invalid Link", Toast.LENGTH_SHORT).show();

                                    }

                                }
                                else {
                                    Toast.makeText(QR_SCAN.this, "Broken Or Invalid Link", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(QR_SCAN.this, "Broken Or Invalid Link", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(QR_SCAN.this, "Broken Or Invalid Link", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });


        MaterialButton sd=findViewById(R.id.i_have_invitation);
        PushDownAnim.setPushDownAnimTo(sd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                RxPermissions rxPermissions = new RxPermissions(QR_SCAN.this);

                if (!rxPermissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    rxPermissions
                            .request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(granted -> {
                                if (granted) {
                                    Matisse.from(QR_SCAN.this)
                                            .choose(MimeType.ofImage())
                                            .countable(true)
                                            .maxSelectable(1)
                                            .theme(R.style.Matisse_Dracula)
                                            .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                                            .thumbnailScale(0.85f)
                                            .imageEngine(new GlideEngine())
                                            .showPreview(true) // Default is `true`
                                            .forResult(70);
                                    overridePendingTransition(R.anim.slide_from_end, R.anim.stationary);

                                }
                            });
                } else {
                    Matisse.from(QR_SCAN.this)
                            .choose(MimeType.ofImage())
                            .countable(true)
                            .maxSelectable(1)
                            .theme(R.style.Matisse_Dracula)
                            .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                            .thumbnailScale(0.85f)
                            .imageEngine(new GlideEngine())
                            .showPreview(true) // Default is `true`
                            .forResult(70);
                    overridePendingTransition(R.anim.slide_from_end, R.anim.stationary);
                }


            }
        });
        PushDownAnim.setPushDownAnimTo(back2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.stationary, R.anim.alerter_slide_out_to_bottom);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 111: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        mCodeScanner.setDecodeCallback(new DecodeCallback() {
                            @Override
                            public void onDecoded(@NonNull final Result result) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(QR_SCAN.this, result.getText(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        mCodeScanner.startPreview();


                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        finish();

                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //the case is because you might be handling multiple request codes here
            case 70:
                if(data == null) {
                    Toast.makeText(QR_SCAN.this, "QR Code Is Invalid", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Uri> mSelected = Matisse.obtainResult(data);
                Uri uri = mSelected.get(0);
                try
                {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    if (bitmap == null)
                    {
                        Toast.makeText(QR_SCAN.this, "QR Code Is Invalid", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int width = bitmap.getWidth(), height = bitmap.getHeight();
                    int[] pixels = new int[width * height];
                    bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
                    bitmap.recycle();
                    bitmap = null;
                    RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
                    BinaryBitmap bBitmap = new BinaryBitmap(new HybridBinarizer(source));
                    MultiFormatReader reader = new MultiFormatReader();
                    try
                    {
                        Result result = reader.decode(bBitmap);

                        Uri rUri= Uri.parse(result.getText());

                        if (rUri != null) {
                            if(rUri.getHost().equals("www.voive.in") || rUri.getHost().equals("voive.in")){
                                if (rUri.getPath().equals("/tables/invites/")) {
                                    if (rUri.getQueryParameter("id") != null) {
                                        String id=rUri.getQueryParameter("id");

                                        ParseQuery<ParseObject> parseObjectParseQuery=ParseQuery.getQuery("live_tables");
                                        parseObjectParseQuery.getInBackground(id, new GetCallback<ParseObject>() {
                                            @Override
                                            public void done(ParseObject object, ParseException e) {


                                                if(e!=null || object==null){

                                                    Toast.makeText(QR_SCAN.this, "Couldn't Fetch Conversation From QR", Toast.LENGTH_SHORT).show();

                                                }
                                                else {
                                                    if (object.getBoolean("Active")) {

                                                        if (object.getList("co_creator").contains(ParseUser.getCurrentUser().getObjectId()) || object.getString("table_creator").equals(ParseUser.getCurrentUser().getObjectId())
                                                                || object.getList("table_subscribers").contains(ParseUser.getCurrentUser().getObjectId())) {


                                                            Intent intent = new Intent(QR_SCAN.this, special_speaker_live_table.class);
                                                            intent.putExtra("ID", object.getObjectId());
                                                            startActivity(intent);
                                                            finish();
                                                            overridePendingTransition(R.anim.slide_from_end, R.anim.stationary);
                                                        } else {

                                                            if (!object.getList("BANS").contains(ParseUser.getCurrentUser().getObjectId())) {
                                                                Intent intent = new Intent(QR_SCAN.this, sound_preview.class);
                                                                intent.putExtra("ID", object.getObjectId());
                                                                startActivity(intent);
                                                                finish();
                                                                overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
                                                            } else {


                                                                AlertDialog.Builder builder1 = new AlertDialog.Builder(QR_SCAN.this, R.style.AlertDialogCustom);
                                                                builder1.setMessage(resources.getString(R.string.staff_ban_you));
                                                                builder1.setTitle(resources.getString(R.string.Banned));
                                                                builder1.setCancelable(false);

                                                                builder1.setPositiveButton(
                                                                        resources.getString(R.string.OK),
                                                                        new DialogInterface.OnClickListener() {
                                                                            public void onClick(DialogInterface dialog, int id) {
                                                                                dialog.dismiss();
                                                                            }
                                                                        });


                                                                AlertDialog alert11 = builder1.create();
                                                                alert11.show();

                                                            }

                                                        }
                                                    }
                                                    else {

                                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(QR_SCAN.this, R.style.AlertDialogCustom);
                                                        builder1.setMessage(resources.getString(R.string.SORRY_CREATOR_DISMISSED));
                                                        builder1.setTitle(resources.getString(R.string.dismiss_table));
                                                        builder1.setCancelable(false);

                                                        builder1.setPositiveButton(
                                                                resources.getString(R.string.OK),
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                        dialog.dismiss();
                                                                    }
                                                                });


                                                        AlertDialog alert11 = builder1.create();
                                                        alert11.show();
                                                    }
                                                }




                                            }
                                        });
                                    } else {

                                       // Toast.makeText(QR_SCAN.this, "Broken Or Invalid Link", Toast.LENGTH_SHORT).show();

                                    }

                                }
                                else {
                                   // Toast.makeText(QR_SCAN.this, "Broken Or Invalid Link", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                               // Toast.makeText(QR_SCAN.this, "Broken Or Invalid Link", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                           // Toast.makeText(QR_SCAN.this, "Broken Or Invalid Link", Toast.LENGTH_SHORT).show();
                        }


                    }
                    catch (NotFoundException e)
                    {
                        Log.e("TAG", "decode exception", e);
                    }
                }
                catch (FileNotFoundException e)
                {
                    Log.e("TAG", "can not open file" + uri.toString(), e);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCodeScanner != null) {
             mCodeScanner.startPreview();
        }

    }

    @Override
    protected void onPause() {
        if (mCodeScanner != null) {
            mCodeScanner.releaseResources();
        }
        super.onPause();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.alerter_slide_out_to_bottom);
    }
}