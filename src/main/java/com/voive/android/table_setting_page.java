package com.voive.android;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import com.bitvale.switcher.SwitcherX;
import com.bumptech.glide.Glide;
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.google.android.material.button.MaterialButton;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.model.SlidrPosition;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.OnBalloonDismissListener;
import com.tapadoo.alerter.Alerter;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.thekhaeng.pushdownanim.PushDownAnim;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.xw.repo.widget.BounceScrollView;
import com.yalantis.ucrop.UCrop;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;


public class table_setting_page extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;
    public static List<add_speaker_with_id_modal> selected;
    List<table_type_modal> table_type_modals = new ArrayList<>();
    Bitmap NEW_PHOTO_BITMAP;
    String TABLE_ID;
    @BindView(R.id.back2)
    MaterialButton back2;
    @BindView(R.id.textView24)
    TextView textView24;
    @BindView(R.id.divider17)
    View divider17;
    @BindView(R.id.constraintLayout2)
    ConstraintLayout constraintLayout2;
    @BindView(R.id.table_title)
    EditText tableTitle;
    @BindView(R.id.table_description)
    EditText tableDescription;
    @BindView(R.id.table_timing_textview)
    TextView tableTimingTextview;
    @BindView(R.id.firstname)
    MaterialButton firstname;
    @BindView(R.id.lastname)
    MaterialButton lastname;
    @BindView(R.id.constraintLayout9)
    ConstraintLayout constraintLayout9;
    @BindView(R.id.startang_and_ending_time_textview)
    TextView startangAndEndingTimeTextview;
    @BindView(R.id.table_type_des_time_textview)
    TextView tableTypeDesTimeTextview;
    @BindView(R.id.table_taste_textview)
    TextView tableTasteTextview;
    @BindView(R.id.max_people_textview)
    TextView maxPeopleTextview;
    @BindView(R.id.how_integer)
    TextView howInteger;
    @BindView(R.id.linearLayout6)
    RelativeLayout linearLayout6;
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    @BindView(R.id.private_switch_switch)
    SwitcherX privateSwitchSwitch;
    @BindView(R.id.private_table_textview)
    TextView privateTableTextview;
    @BindView(R.id.private_table_des_textview)
    TextView privateTableDesTextview;
    @BindView(R.id.request_to_join_switch)
    SwitcherX requestToJoinSwitch;
    @BindView(R.id.invitation_table_textview)
    TextView invitationTableTextview;
    @BindView(R.id.initation_des_textview)
    TextView initationDesTextview;
    @BindView(R.id.speak_to_join_switch)
    SwitcherX speakToJoinSwitch;
    @BindView(R.id.speak_table_textview)
    TextView speakTableTextview;
    @BindView(R.id.speak_des_textview)
    TextView speakDesTextview;
    @BindView(R.id.who_can_speak_textview)
    TextView whoCanSpeakTextview;
    @BindView(R.id.who_can_speak_reative)
    RelativeLayout whoCanSpeakReative;
    @BindView(R.id.purpose_textview)
    TextView purposeTextview;
    @BindView(R.id.purpose_of_table_relative)
    RelativeLayout purposeOfTableRelative;
    @BindView(R.id.adult_content_textview)
    TextView adultContentTextview;
    @BindView(R.id.scroll)
    BounceScrollView bounceScrollView;
    @BindView(R.id.adult_textview_description)
    TextView adultTextviewDescription;
    @BindView(R.id.switch2)
    SwitcherX switch2;
    @BindView(R.id.invite_people)
    MaterialButton invitePeople;
    @BindView(R.id.invite_people_relative)
    RelativeLayout invitePeopleRelative;
    @BindView(R.id.bans_textview)
    TextView bansTextview;
    @BindView(R.id.ban_people_relative)
    RelativeLayout banPeopleRelative;
    @BindView(R.id.dismiss_table_textview)
    TextView dismissTableTextview;
    @BindView(R.id.dismiss_table_relative)
    RelativeLayout dismissTableRelative;
    Resources resources;
    @BindView(R.id.table_image)
    CircleImageView tableImage;
    @BindView(R.id.tick)
    MaterialButton floatingActionButton;
    @BindView(R.id.sneaking_textview)
    TextView sneaking_textview;
    @BindView(R.id.sneaking_des_textview)
    TextView sneaking_des_textview;
    @BindView(R.id.sneaking_switch)
    SwitcherX sneaking_switch;
    @BindView(R.id.allow_recording_textview)
    TextView allow_recording_textview;
    @BindView(R.id.allow_recording_des_textview)
    TextView allow_recording_des_textview;
    @BindView(R.id.allow_recording_switch)
    SwitcherX allow_recording_switch;
    SlidrInterface slidrInterface;
    @BindView(R.id.allow_media_switch)
    SwitcherX allowMediaSwitch;
    @BindView(R.id.allow_media_textview)
    TextView allowMediaTextview;
    @BindView(R.id.allow_media_des_textview)
    TextView allowMediaDesTextview;
    @BindView(R.id.lng_spinner)
    Spinner lng_spinner;
    @BindView(R.id.which_lang_title)
    TextView which_lang_textview;
    @BindView(R.id.whihc_lang_des)
    TextView which_lang_des_textview;
    @BindView(R.id.textView8)
    RelativeTimeTextView textView8;
    @BindView(R.id.which_cat)
            TextView which_cat;
    LocationManager locationManager;
    int check=0;
    int check_lng=0;
    MaterialButton button10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_setting_page);
        ButterKnife.bind(this);


        SharedPreferences sharedPreferences = getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng = sharedPreferences.getString("lng", "en");
        boolean is_first_popup=sharedPreferences.getBoolean("FIRST_POPUP_CATEGORY",true);
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

        bounceScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    slidrInterface.lock();
                    // Do what you want
                } else {

                    slidrInterface.unlock();
                }
                return false;
            }
        });
    button10=findViewById(R.id.button10);
        if(is_first_popup) {
            final Typeface typeface = ResourcesCompat.getFont(this, R.font.montr_bold);
            Balloon balloon = new Balloon.Builder(this.getApplicationContext())
                    .setArrowSize(10)
                    .setArrowOrientation(ArrowOrientation.TOP)
                    .setIsVisibleArrow(true)
                    .setArrowPosition(0.8f)
                    .setWidthRatio(0.6f)
                    .setTextSize(14f)
                    .setPaddingLeft(16)
                    .setMarginRight(16)
                    .setPaddingTop(4)
                    .setPaddingBottom(4)
                    .setPaddingRight(16)
                    .setPaddingLeft(16)
                    .setPaddingRight(16)
                    .setTextTypeface(typeface)
                    .setCornerRadius(2f)
                    .setAlpha(1f)
                    .setBalloonAnimation(BalloonAnimation.CIRCULAR)
                    .setText(resources.getString(R.string.click_add_to_cat))
                    .setTextColor(resources.getColor(R.color.black))
                    .setBackgroundColor(resources.getColor(R.color.colorAccent))
                    .setBalloonAnimation(BalloonAnimation.FADE)
                    .build();
            balloon.setOnBalloonDismissListener(new OnBalloonDismissListener() {
                @Override
                public void onBalloonDismiss() {

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("FIRST_POPUP_CATEGORY", false);
                    editor.apply();

                }
            });

            balloon.dismissWithDelay(20000L);
            balloon.showAlignBottom(button10);
        }


        tableTitle.setHint(resources.getString(R.string.TABLE_NAME));
        privateTableTextview.setText(resources.getString(R.string.pruivate_table));
        invitationTableTextview.setText(resources.getString(R.string.invitation_to_join));
        speakTableTextview.setText(resources.getString(R.string.everyone_can_talk));
        privateTableDesTextview.setText(resources.getString(R.string.only_co_creator_invite_other_people));
        speakDesTextview.setText(resources.getString(R.string.who_can_open_mic_or_not));
        initationDesTextview.setText(resources.getString(R.string.people_must_have_invitation));
        tableDescription.setHint(resources.getString(R.string.TABLE_DESCRIPTION));
        purposeTextview.setText(resources.getString(R.string.purpose_of_table_title));
        tableTimingTextview.setText(resources.getString(R.string.TABLE_TIMMING));
        firstname.setText(resources.getString(R.string.starting_time));
        maxPeopleTextview.setText(resources.getString(R.string.max_people_no_people));
        allow_recording_textview.setText(resources.getString(R.string.allow_recording));
        allow_recording_des_textview.setText(resources.getString(R.string.allow_recording_des));
        lastname.setText(resources.getString(R.string.ending_time));
        whoCanSpeakTextview.setText(resources.getString(R.string.spec_who_can_speak));
        textView24.setText(resources.getString(R.string.TABLE_SETTING));
        startangAndEndingTimeTextview.setText(resources.getString(R.string.STARTING_AND_ENDING_DESCRIPTION));
        tableTasteTextview.setText(resources.getString(R.string.Categories));
        allowMediaTextview.setText(resources.getString(R.string.Allow_media));
        which_lang_textview.setText(resources.getText(R.string.speaking_language));
        which_lang_des_textview.setText(resources.getText(R.string.which_lng_des));
        allowMediaDesTextview.setText(resources.getString(R.string.Allow_media_des));
        invitePeople.setText(resources.getString(R.string.invite_peoples));
        bansTextview.setText(resources.getString(R.string.BANNED_SPEAKER));
        adultContentTextview.setText(resources.getString(R.string.NSFW_CONV));
        dismissTableTextview.setText(resources.getString(R.string.dismiss_table));
        adultTextviewDescription.setText(resources.getString(R.string.adulr_content_description));

        get_data(getIntent().getStringExtra("ID"));


        PushDownAnim.setPushDownAnimTo(back2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.stationary, R.anim.alerter_slide_out_to_bottom);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 70 && data != null) {


            List<Uri> mSelected = Matisse.obtainResult(data);
            start_crop(mSelected.get(0));


        }

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {

            try {


                ACProgressFlower dialog = new ACProgressFlower.Builder(table_setting_page.this)
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

                ParseQuery<ParseObject> SETTED = new ParseQuery<ParseObject>("live_tables");
                SETTED.getInBackground(getIntent().getStringExtra("ID"), new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {

                        textView8.setReferenceTime(object.getCreatedAt().getTime());
                        object.put("table_image", imageFile);
                        object.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {

                                Glide.with(table_setting_page.this.getApplicationContext()).asBitmap().centerInside().load(object.getParseFile("table_image").getUrl()).
                                        into(tableImage);

                                dialog.dismiss();
                                Toast.makeText(table_setting_page.this, "Table Banner Image Updated...", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

            } catch (IOException e) {
                SharedPreferences sharedPreferences = getSharedPreferences("LANG", MODE_PRIVATE);
                String language = sharedPreferences.getString("lng", "hi");
                Context context = LocaleHelper.setLocale(table_setting_page.this, language);
                Resources resources = context.getResources();
                Alerter.create(table_setting_page.this)
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
            Alerter.create(table_setting_page.this)
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
                .start(table_setting_page.this);


    }

    public UCrop.Options oprions() {

        UCrop.Options options = new UCrop.Options();

        options.setCompressionQuality(100);

        options.setHideBottomControls(false);
        options.setCircleDimmedLayer(true);
        options.getOptionBundle();
        options.setFreeStyleCropEnabled(true);


        options.setStatusBarColor(getResources().getColor(R.color.black));
        options.setToolbarColor(getResources().getColor(R.color.black));
        options.setToolbarWidgetColor(getResources().getColor(android.R.color.white));
        options.setToolbarTitle("Crop Banner Image");

        return options;
    }

    public void get_data(String TABLE_ID) {


        ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("live_tables");
        parseObjectParseQuery.getInBackground(TABLE_ID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {

                tableTitle.setText(object.getString("table_name"));
                tableDescription.setText(object.getString("table_description"));
                firstname.setText(object.getString("start_time"));
                lastname.setText(object.getString("end_time"));

                //Switch Setup
                privateSwitchSwitch.setChecked(object.getBoolean("IS_PRIVATE"), true);


                List<String> array = object.getList("Category");

                tableTitle.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        floatingActionButton.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                tableDescription.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        floatingActionButton.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                PushDownAnim.setPushDownAnimTo(floatingActionButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        object.put("table_description", tableDescription.getText().toString());
                        if (!tableTitle.getText().toString().trim().isEmpty()) {
                            object.put("table_name", tableTitle.getText().toString());
                            object.saveInBackground();
                            floatingActionButton.setVisibility(View.INVISIBLE);
                            Toast.makeText(table_setting_page.this, "Saved", Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(table_setting_page.this, "Title Should Not Be Empty 👈🏽", Toast.LENGTH_SHORT).show();

                        }


                    }
                });

                Glide.with(table_setting_page.this.getApplicationContext()).asBitmap().centerInside().load(object.getParseFile("table_image").getUrl()).
                        into(tableImage);





                which_cat.setVisibility(object.getList("category").size()>0?View.VISIBLE:View.GONE);
                if(object.getList("category").size()>0){
                    which_cat.setText(object.getList("category").get(0).toString());
                }



                PushDownAnim.setPushDownAnimTo(button10).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        RoundedBottomSheetDialog roundedBottomSheetDialog=new RoundedBottomSheetDialog(table_setting_page.this);
                        roundedBottomSheetDialog.setContentView(R.layout.select_category_bottom);

                        TagFlowLayout tagFlowLayout=roundedBottomSheetDialog.findViewById(R.id.id_flowlayout);
                        List<String> ss = new ArrayList<>();
                        ss.add("News");
                        ss.add("Domestic Conversations");
                        ss.add("Unions");
                        ss.add("Panchayat");
                        ss.add("Devotional");
                        ss.add("Arts & Entertainment");
                        ss.add("Health & Beauty");
                        ss.add("Science & Tech");
                        ss.add("Politics");
                        ss.add("Business");
                        ss.add("Books & Information");


                        tagFlowLayout.setAdapter(new TagAdapter<String>(ss)
                        {
                            @Override
                            public View getView(FlowLayout parent, int position, String s)
                            {
                                TextView tv = (TextView) getLayoutInflater().inflate(R.layout.tvf_another,
                                        tagFlowLayout, false);
                                tv.setText(s);
                                return tv;
                            }
                        });

                        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                            @Override
                            public boolean onTagClick(View view, int position, FlowLayout parent) {
                                ACProgressFlower dialog = new ACProgressFlower.Builder(table_setting_page.this)
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
                                List<String> SS = new ArrayList<>();
                                SS.add(ss.get(position));
                                object.put("category", SS);
                                object.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        which_cat.setText(ss.get(position));
                                        Toast.makeText(table_setting_page.this, "Saved", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        roundedBottomSheetDialog.dismiss();
                                    }
                                });
                                return false;
                            }
                        });


                        roundedBottomSheetDialog.show();

                    }
                });




                allow_recording_switch.setChecked(object.getBoolean("ALLOW_RECORDING"), true);

                allow_recording_switch.setOnCheckedChangeListener(checked ->
                {
                    allow_recording_switch.setChecked(checked, true);
                    object.put("ALLOW_RECORDING", checked);
                    object.saveInBackground();

                    Toast.makeText(table_setting_page.this, "Saved", Toast.LENGTH_SHORT).show();

                    return Unit.INSTANCE;
                });


                privateSwitchSwitch.setOnCheckedChangeListener(checked ->
                {

                    privateSwitchSwitch.setChecked(checked, true);
                    object.put("IS_PRIVATE", checked);
                    object.saveInBackground();

                    Toast.makeText(table_setting_page.this, "Saved", Toast.LENGTH_SHORT).show();
                    return Unit.INSTANCE;

                });

                allowMediaSwitch.setOnCheckedChangeListener(checked ->
                {

                    allowMediaSwitch.setChecked(checked, true);
                    object.put("Allow_Media", checked);
                    object.saveInBackground();

                    Toast.makeText(table_setting_page.this, "Saved", Toast.LENGTH_SHORT).show();
                    return Unit.INSTANCE;

                });

                sneaking_switch.setChecked(object.getParseGeoPoint("Location") != null, true);

                sneaking_switch.setOnCheckedChangeListener(checked ->
                {
                    sneaking_switch.setChecked(checked, true);

                    if (checked) {

                        saveCurrentUserLocation();
                        object.put("Location", getCurrentUserLocation());
                        object.saveInBackground();
                        Toast.makeText(table_setting_page.this, "Saved", Toast.LENGTH_SHORT).show();
                    } else {
                        object.put("Location", null);
                        object.saveInBackground();

                        Toast.makeText(table_setting_page.this, "Saved", Toast.LENGTH_SHORT).show();
                    }

                    return Unit.INSTANCE;
                });

                if (object.getInt("HOW_MANY_ALLOWED") == 1) {

                    howInteger.setText("Only You");
                } else {
                    howInteger.setText(Integer.toString(object.getInt("HOW_MANY_ALLOWED")));
                }


                seekbar.setProgress(object.getInt("HOW_MANY_ALLOWED"));

                seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        if (progress == 1) {

                            howInteger.setText("Only You");
                            object.put("HOW_MANY_ALLOWED", progress);
                            object.saveInBackground();

                        } else {
                            howInteger.setText(Integer.toString(progress));

                            object.put("HOW_MANY_ALLOWED", progress);
                            object.saveInBackground();

                        }


                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                PushDownAnim.setPushDownAnimTo(firstname).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        TimePickerDialog timePickerDialog = TimePickerDialog
                                .newInstance(new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {


                                        Calendar calendar = Calendar.getInstance();

                                        calendar.set(0, 0, 0, hourOfDay, minute);

                                        firstname.setText(DateFormat.format("hh:mm a", calendar));

                                        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm:ss");
                                        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
                                        Date date = null;
                                        try {
                                            date = parseFormat.parse(firstname.getText().toString());
                                            String start_back_time = displayFormat.format(date);

                                            ParseQuery<ParseObject> SETTING = new ParseQuery<ParseObject>("live_tables");
                                            SETTING.getInBackground(TABLE_ID, new GetCallback<ParseObject>() {
                                                @Override
                                                public void done(ParseObject object, ParseException e) {

                                                    object.put("START_BACK", start_back_time);
                                                    object.put("start_time", firstname.getText().toString());
                                                    object.saveInBackground(new SaveCallback() {
                                                        @Override
                                                        public void done(ParseException e) {


                                                            Toast.makeText(table_setting_page.this, "Starting Time Updated", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                                }
                                            });

                                        } catch (java.text.ParseException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, 12, 0, false);


                        timePickerDialog.setAccentColor(getResources().getColor(R.color.colorAccent));
                        timePickerDialog.show(getSupportFragmentManager(), "ABC");

                    }
                });

                PushDownAnim.setPushDownAnimTo(lastname).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        TimePickerDialog timePickerDialog = TimePickerDialog
                                .newInstance(new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {


                                        Calendar calendar = Calendar.getInstance();

                                        calendar.set(0, 0, 0, hourOfDay, minute);

                                        lastname.setText(DateFormat.format("hh:mm a", calendar));

                                        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm:ss");
                                        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
                                        Date date = null;
                                        try {
                                            date = parseFormat.parse(lastname.getText().toString());
                                            String start_back_time = displayFormat.format(date);
                                            if (start_back_time.equals("24:00:00")) {
                                                start_back_time = "23:59:59";
                                            }
                                            ParseQuery<ParseObject> SETTING = new ParseQuery<ParseObject>("live_tables");
                                            String finalStart_back_time = start_back_time;
                                            SETTING.getInBackground(TABLE_ID, new GetCallback<ParseObject>() {
                                                @Override
                                                public void done(ParseObject object, ParseException e) {


                                                    object.put("END_BACK", finalStart_back_time);
                                                    object.put("end_time", lastname.getText().toString());
                                                    object.saveInBackground(new SaveCallback() {
                                                        @Override
                                                        public void done(ParseException e) {

                                                            Toast.makeText(table_setting_page.this, "Ending Time Updated", Toast.LENGTH_SHORT).show();

                                                        }
                                                    });

                                                }
                                            });

                                        } catch (java.text.ParseException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, 12, 0, false);


                        timePickerDialog.setAccentColor(getResources().getColor(R.color.colorAccent));
                        timePickerDialog.show(getSupportFragmentManager(), "ABC");

                    }
                });

                PushDownAnim.setPushDownAnimTo(dismissTableRelative).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        AlertDialog.Builder builder1 = new AlertDialog.Builder(table_setting_page.this, R.style.AlertDialogCustom);
                        builder1.setMessage(resources.getString(R.string.sure_to_dismmiss_table));
                        builder1.setTitle(resources.getString(R.string.dismiss_table));
                        builder1.setCancelable(false);

                        builder1.setPositiveButton(
                                resources.getString(R.string.YES),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        ACProgressFlower dialogp = new ACProgressFlower.Builder(table_setting_page.this)
                                                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                                                .themeColor(Color.WHITE)
                                                .bgColor(R.color.card_view_color)
                                                .fadeColor(R.color.card_view_color)
                                                .bgAlpha(0)
                                                .petalThickness(6)
                                                .petalCount(20)
                                                .speed(25)
                                                .fadeColor(Color.DKGRAY).build();
                                        dialogp.show();
                                        object.put("Active", false);
                                        object.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {

                                                if (e == null) {

                                                    try {

                                                        ParseQuery<ParseObject> user_data_live = new ParseQuery<ParseObject>("roundtable_user_data");
                                                        user_data_live.whereEqualTo("User_ID", ParseUser.getCurrentUser().getObjectId());
                                                        user_data_live.findInBackground(new FindCallback<ParseObject>() {
                                                            @Override
                                                            public void done(List<ParseObject> objectsX, ParseException e) {

                                                                List<String> sub = objectsX.get(0).getList("Subscriptions");
                                                                sub.remove(object.getObjectId());

                                                                objectsX.get(0).put("Subscriptions", sub);
                                                                objectsX.get(0).saveInBackground(new SaveCallback() {
                                                                    @Override
                                                                    public void done(ParseException e) {
                                                                        dialogp.dismiss();
                                                                        finish();
                                                                        App.WHICH_TABLE = "";
                                                                        App.IS_LISTENING = false;
                                                                        dialog.cancel();
                                                                        Toast.makeText(table_setting_page.this, "Table Dismissed", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });

                                                            }
                                                        });


                                                    } catch (Exception fd) {

                                                        fd.printStackTrace();
                                                    }

                                                } else {

                                                    Toast.makeText(table_setting_page.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                                }

                                            }
                                        });

                                    }
                                });

                        builder1.setNegativeButton(
                                resources.getString(R.string.NO),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();

                    }
                });

                PushDownAnim.setPushDownAnimTo(invitePeopleRelative).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(table_setting_page.this, invite_people_activity.class);
                        intent.putExtra("ID", TABLE_ID);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_from_end, R.anim.stationary);


                    }
                });

                PushDownAnim.setPushDownAnimTo(banPeopleRelative).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(table_setting_page.this, ban_peoples_activity.class);
                        intent.putExtra("ID", TABLE_ID);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_from_end, R.anim.stationary);
                    }
                });

                PushDownAnim.setPushDownAnimTo(tableImage).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        RxPermissions rxPermissions = new RxPermissions(table_setting_page.this);

                        if (!rxPermissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                            rxPermissions
                                    .request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    .subscribe(granted -> {
                                        if (granted) {
                                            Matisse.from(table_setting_page.this)
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

                            Matisse.from(table_setting_page.this)
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

            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.alerter_slide_out_to_bottom);
    }

    private void saveCurrentUserLocation() {
        // requesting permission to get user's location
        if (ActivityCompat.checkSelfPermission(table_setting_page.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(table_setting_page.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(table_setting_page.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {

            try {
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    ParseGeoPoint currentUserLocation = new ParseGeoPoint(location.getLatitude(), location.getLongitude());

                    ParseUser currentUser = ParseUser.getCurrentUser();

                    if (currentUser != null) {
                        currentUser.put("Location", currentUserLocation);
                        currentUser.saveInBackground();
                    } else {
                    }
                } else {
                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
    }

    private ParseGeoPoint getCurrentUserLocation() {

        // finding currentUser
        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser == null) {
            return null;
        }
        // otherwise, return the current user location
        return currentUser.getParseGeoPoint("Location");

    }
}
