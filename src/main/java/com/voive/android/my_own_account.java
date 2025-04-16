package com.voive.android;

import static com.voive.android.App.PLAYBACK;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.google.android.material.button.MaterialButton;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.model.SlidrPosition;
import com.tapadoo.alerter.Alerter;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.thekhaeng.pushdownanim.PushDownAnim;
import com.xw.repo.widget.BounceScrollView;
import com.yalantis.ucrop.UCrop;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import de.hdodenhof.circleimageview.CircleImageView;

public class my_own_account extends AppCompatActivity {

    @BindView(R.id.back)
    MaterialButton back;
    @BindView(R.id.profile_pic)
    CircleImageView profilePic;
    @BindView(R.id.first_last_name)
    TextView firstLastName;
    @BindView(R.id.roundtable_username)
    TextView roundtableUsername;

    @BindView(R.id.speaker_description)
    TextView speakerDescription;
    @BindView(R.id.edit_profile)
    MaterialButton editProfile;
    Bitmap NEW_PHOTO_BITMAP;
    MaterialButton youtube;
    MaterialButton twitter;
    MaterialButton insta;
    @BindView(R.id.linearLayout3)
    RelativeLayout linearLayout3;
    @BindView(R.id.follower)
    TextView follower;
    @BindView(R.id.divider)
    View divider;
    @BindView(R.id.scrollView)
    BounceScrollView scrollView;
    @BindView(R.id.attach_bio)
    MaterialButton attachBio;
    @BindView(R.id.textView8)
    RelativeTimeTextView relativeTimeTextView;
    RoundedBottomSheetDialog roundedBottomSheetDialog;
    CircleImageView circleImageView;
    SlidrInterface slidrInterface;
    @BindView(R.id.clickable)
    LinearLayout followerClickLinear;
    @BindView(R.id.recent)
    MaterialButton recent;
    @BindView(R.id.textView)
            TextView title;
    @BindView(R.id.phone_des)
            TextView phone_des;
    @BindView(R.id.click)
    ConstraintLayout click;
    Resources resources;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_own_account);
        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = getSharedPreferences("LANG", MODE_PRIVATE);
        String language = sharedPreferences.getString("lng", "hi");
        Context context = LocaleHelper.setLocale(my_own_account.this, language);
       resources = context.getResources();
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

        if (ParseUser.getCurrentUser() != null) {


            relativeTimeTextView.setReferenceTime(ParseUser.getCurrentUser().getCreatedAt().getTime());
            ParseUser.getCurrentUser().saveInBackground();
            PushDownAnim.setPushDownAnimTo(profilePic).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(my_own_account.this, zoom_image_viewer.class);
                    intent.putExtra("file_photo_url", ParseUser.getCurrentUser().getParseFile("speaker_profile_pic").getUrl());
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_from_end, R.anim.stationary);


                }
            });



            ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("roundtable_user_data");
            parseObjectParseQuery.whereEqualTo("User_ID", ParseUser.getCurrentUser().getObjectId());
            parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {


                    for (ParseObject parseObject : objects) {


                        List<String> followers_list = parseObject.getList("who_notify");
                        follower.setText(Integer.toString(followers_list.size())+" "+resources.getString(R.string.Notified));

                    }


                }
            });



            PushDownAnim.setPushDownAnimTo(editProfile).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(my_own_account.this, settingpage.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_from_end, R.anim.stationary);
                }
            });


            scrollView.setOnScrollListener(new BounceScrollView.OnScrollListener() {
                @Override
                public void onScrolling(int scrollX, int scrollY) {

                    if (scrollY == 0) {

                        divider.setVisibility(View.GONE);

                    } else {

                        divider.setVisibility(View.VISIBLE);
                    }

                }
            });




            PushDownAnim.setPushDownAnimTo(followerClickLinear).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(my_own_account.this, following_and_follower_activity.class);
                    intent.putExtra("ID", ParseUser.getCurrentUser().getObjectId());
                    startActivity(intent);

                }
            });

            attachBio.setText(resources.getString(R.string.EDIT));

            PushDownAnim.setPushDownAnimTo(attachBio).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    RoundedBottomSheetDialog roundedBottomSheetDialog = new RoundedBottomSheetDialog(my_own_account.this);
                    View view = getLayoutInflater().inflate(R.layout.change_username_bottom_sheet_layout, null, false);
                    roundedBottomSheetDialog.setContentView(view);

                    TextView name, username, Bio;
                    EditText edit_name, edit_username, edit_bio;
                    CircleImageView pp;
                    MaterialButton login2;

                    pp = view.findViewById(R.id.pp);

                    Glide.with(my_own_account.this).asBitmap().load(ParseUser.getCurrentUser().getParseFile("speaker_profile_pic").getUrl()).centerInside().into(pp);

                    PushDownAnim.setPushDownAnimTo(pp).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            RxPermissions rxPermissions = new RxPermissions(my_own_account.this);

                            if (!rxPermissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                                rxPermissions
                                        .request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        .subscribe(granted -> {
                                            if (granted) {
                                                Matisse.from(my_own_account.this)
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

                                Matisse.from(my_own_account.this)
                                        .choose(MimeType.ofImage())
                                        .countable(false)
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
                    edit_name = view.findViewById(R.id.first_name_edittext);
                    edit_username = view.findViewById(R.id.username_edittext);
                    edit_bio = view.findViewById(R.id.bio_edittext);
                    name = view.findViewById(R.id.textView47);
                    username = view.findViewById(R.id.username);
                    Bio = view.findViewById(R.id.bio);
                    login2 = view.findViewById(R.id.login2);
                    login2.setText(resources.getString(R.string.Done));

                    edit_bio.setText(resources.getString(R.string.BIO));
                    name.setText(resources.getString(R.string.Alias_name));
                    Bio.setText(resources.getString(R.string.BIO));
                    PushDownAnim.setPushDownAnimTo(login2).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (!edit_name.getText().toString().trim().isEmpty() && !edit_username.getText().toString().isEmpty()) {

                                ParseUser.getCurrentUser().put("First_and_last_name", edit_name.getText().toString()+" "+username.getText().toString());
                                ParseUser.getCurrentUser().put("speaker_information", edit_bio.getText().toString());
                                ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {

                                        roundedBottomSheetDialog.dismiss();
                                        set();
                                        Toast.makeText(my_own_account.this, "Updated", Toast.LENGTH_SHORT).show();

                                    }
                                });

                            }


                        }
                    });

                    roundedBottomSheetDialog.show();


                }
            });

            set();




        }


        PushDownAnim.setPushDownAnimTo(back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.stationary, R.anim.bottom_down);
            }
        });
        PushDownAnim.setPushDownAnimTo(recent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(my_own_account.this, recent_listened.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_start, R.anim.stationary);
            }
        });

        PushDownAnim.setPushDownAnimTo(click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(my_own_account.this, from_conrtact_invite.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_end, R.anim.stationary);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 70 && data != null) {

            List<Uri> mSelected = Matisse.obtainResult(data);


            start_crop(mSelected.get(0));

        } else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {

            try {


                ACProgressFlower dialog = new ACProgressFlower.Builder(my_own_account.this)
                        .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                        .themeColor(Color.WHITE)
                        .bgColor(R.color.card_view_color)
                        .fadeColor(R.color.card_view_color)
                        .bgAlpha(0)
                        .petalThickness(6)
                        .petalCount(20)
                        .speed(25)
                        .fadeColor(Color.DKGRAY).build();


                dialog.show();


                final Uri resultUri = UCrop.getOutput(data);
                Bitmap photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                //  Glide.with(table_setting_page.this).asBitmap().load(photo).centerCrop().into(tableBannerImage);
                NEW_PHOTO_BITMAP = photo;



                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                NEW_PHOTO_BITMAP.compress(Bitmap.CompressFormat.PNG, 80, stream);
                byte[] dataX = stream.toByteArray();
                ParseFile imageFile = new ParseFile("image.png", dataX);

                ParseUser.getCurrentUser().put("speaker_profile_pic",imageFile);
                ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        Glide.with(my_own_account.this).asBitmap().load(NEW_PHOTO_BITMAP).centerInside().thumbnail(0.3f)
                                .into(profilePic);
                        dialog.dismiss();
                        Toast.makeText(my_own_account.this, "Updated", Toast.LENGTH_SHORT).show();
                    }
                });



            } catch (IOException e) {
                SharedPreferences sharedPreferences = getSharedPreferences("LANG", MODE_PRIVATE);
                String language = sharedPreferences.getString("lng", "hi");
                Context context = LocaleHelper.setLocale(my_own_account.this, language);
                Resources resources = context.getResources();
                Alerter.create(my_own_account.this)
                        .setTitle(resources.getString(R.string.something_went_wrong))
                        .setText(e.getMessage())
                        .setBackgroundColorInt(getResources().getColor(android.R.color.holo_red_light))
                        .hideIcon()
                        .enableSwipeToDismiss()
                        .setDuration(4000)
                        .show();
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Alerter.create(my_own_account.this)
                    .setText(cropError.getLocalizedMessage())
                    .setBackgroundColorInt(getResources().getColor(android.R.color.holo_red_light))
                    .hideIcon()
                    .enableSwipeToDismiss()
                    .setDuration(4000)
                    .show();
        }

    }

    public void start_crop(Uri uri) {

        String destination = new StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString();

        UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destination)))
                .withAspectRatio(16, 9)
                .withAspectRatio(1, 1)
                .withAspectRatio(4, 3)
                .withAspectRatio(5, 4)

                .withOptions(oprions())
                .start(my_own_account.this);


    }

    public UCrop.Options oprions() {

        UCrop.Options options = new UCrop.Options();

        options.setCompressionQuality(80);

        options.setHideBottomControls(false);
        options.setCircleDimmedLayer(true);
        options.getOptionBundle();
        options.setFreeStyleCropEnabled(true);


        options.setStatusBarColor(getResources().getColor(R.color.colorAccent));
        options.setToolbarColor(getResources().getColor(R.color.colorAccent));
        options.setToolbarWidgetColor(getResources().getColor(android.R.color.white));
        options.setToolbarTitle("Crop Banner Image");

        return options;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.bottom_down);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera_intent, 123);
                overridePendingTransition(R.anim.slide_from_end, R.anim.stationary);

            }
        }
    }


    public String get_formated(int num) {

        DecimalFormat formatter = new DecimalFormat("###,###,###,###");
        String yourFormattedString = formatter.format(num);
        return yourFormattedString;
    }

    public void set(){
        speakerDescription.setVisibility(ParseUser.getCurrentUser().getString("speaker_information").trim().isEmpty()?View.GONE:View.VISIBLE);
        speakerDescription.setText(ParseUser.getCurrentUser().getString("speaker_information"));
        firstLastName.setText(ParseUser.getCurrentUser().getString("First_and_last_name"));
        roundtableUsername.setText(ParseUser.getCurrentUser().getUsername());
        Glide.with(my_own_account.this).asBitmap().load(ParseUser.getCurrentUser().getParseFile("speaker_profile_pic").getUrl()).centerInside().into(profilePic);


    }
}

