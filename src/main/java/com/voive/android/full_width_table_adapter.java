package com.voive.android;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.ColorUtils;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.andexert.library.RippleView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.tapadoo.alerter.Alerter;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class full_width_table_adapter extends RecyclerView.Adapter<full_width_table_adapter.ViewHolder> {
    Context context;
    List<ParseObject> parseObjects;

    public full_width_table_adapter(Context context, List<ParseObject> parseObjects) {
        this.context = context;
        this.parseObjects = parseObjects;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.full_width_table_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng = sharedPreferences.getString("lng", "en");
        Context language_context = LocaleHelper.setLocale(context, lng);
        Resources resources = language_context.getResources();


        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");


        holder.subs.setText(Integer.toString(parseObjects.get(position).
                getList("table_subscribers").size()) + " " + resources.getString(R.string.Subscribers));

        holder.creator.setText(parseObjects.get(position).getString("table_name"));

        if(parseObjects.get(position).getBoolean("Active")){


            try {

                Calendar calendar1,calendar2;
                String string1 = parseObjects.get(position).getString("START_BACK");
                Date time1 = new SimpleDateFormat("HH:mm:ss").parse(string1);

                String string2 = parseObjects.get(position).getString("END_BACK");
                Date time2 = new SimpleDateFormat("HH:mm:ss").parse(string2);

                if(time1.getHours()>time2.getHours()){
                    time1 = dateFormat.parse("02/26/2099 "+string1);
                    time2 = dateFormat.parse("02/27/2099 "+string2);

                    calendar1 = Calendar.getInstance();
                    calendar1.setTime(time1);
                    calendar1.add(Calendar.DATE, 1);

                    calendar2 = Calendar.getInstance();
                    calendar2.setTime(time2);
                    calendar2.add(Calendar.DATE, 1);
                }
                else {

                    time1 = dateFormat.parse("02/27/2099 "+string1);
                    time2 = dateFormat.parse("02/27/2099 "+string2);

                    calendar1 = Calendar.getInstance();
                    calendar1.setTime(time1);
                    calendar1.add(Calendar.DATE, 1);

                    calendar2 = Calendar.getInstance();
                    calendar2.setTime(time2);
                    calendar2.add(Calendar.DATE, 1);

                }


                Date currentTime = Calendar.getInstance().getTime();
                String someRandomTime = currentTime.getHours()+":"+currentTime.getMinutes()+":"+currentTime.getSeconds();
                Calendar calendar3 = Calendar.getInstance();
                if(time1.getHours()>currentTime.getHours() || time1.getDay()==time2.getDay()){
                    Date d = dateFormat.parse("02/27/2099 "+someRandomTime);

                    calendar3.setTime(d);
                    calendar3.add(Calendar.DATE, 1);
                }

                else {
                    Date d = dateFormat.parse("02/26/2099 "+someRandomTime);

                    calendar3.setTime(d);
                    calendar3.add(Calendar.DATE, 1);
                }


                Date x = calendar3.getTime();

            }
            catch (java.text.ParseException parseException) {
                parseException.printStackTrace();
            }



            Glide.with(context).load(parseObjects.get(position).getParseFile("table_image").getUrl()).centerInside().into(holder.circleImageView);
            if(parseObjects.get(position).getString("TALKING_TITLE").trim().isEmpty()){
                holder.if_title_empty_linear.setVisibility(View.VISIBLE);
                holder.title.setVisibility(View.GONE);
                holder.no_title.setText(resources.getString(R.string.No_Title));
            }
            else {
                holder.title.setText(parseObjects.get(position).getString("TALKING_TITLE"));
                holder.if_title_empty_linear.setVisibility(View.GONE);
                holder.title.setVisibility(View.VISIBLE);
            }
        }
        else {

            holder.if_title_empty_linear.setVisibility(View.VISIBLE);
            holder.title.setVisibility(View.GONE);
            holder.no_title.setText(resources.getString(R.string.dismissed_table));
            Glide.with(context).asGif().load("https://i.ytimg.com/vi/a1AJN6RL4wc/maxresdefault.jpg").into(holder.circleImageView);
        }

        Glide.with(context.getApplicationContext()).asBitmap().load(parseObjects.get(position).getParseFile("table_image").getUrl())
                .centerInside().thumbnail(0.3f).placeholder(R.drawable.main_placeholder).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(@Nullable Palette palette) {

                                Palette.Swatch swatch = palette.getLightVibrantSwatch() != null
                                        ? palette.getLightVibrantSwatch() : palette.getDominantSwatch();
                                if (swatch != null  && !isColorDark(swatch.getRgb())) {
                                    holder.creator.setTextColor(swatch.getRgb());
                                } else {
                                    holder.creator.setTextColor(context.getColor(R.color.themetextsecondarycolor));
                                }


                            }
                        });
                    }
                });
        if (ParseUser.getCurrentUser().getString("WHAT_LISTENING").equals(parseObjects.get(position).getObjectId())) {

            holder.is_live_relative.setVisibility(View.VISIBLE);
        } else {
            holder.is_live_relative.setVisibility(View.GONE);

        }

        PushDownAnim.setPushDownAnimTo(holder.constraintLayout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(context, manage_notification.class);
                intent.putExtra("ID", parseObjects.get(position).getObjectId());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
                return false;
            }
        });
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (parseObjects.get(position).getBoolean("Active")) {

                    if (parseObjects.get(position).getList("co_creator").contains(ParseUser.getCurrentUser().getObjectId()) || parseObjects.get(position).getString("table_creator").equals(ParseUser.getCurrentUser().getObjectId())
                            || parseObjects.get(position).getList("table_subscribers").contains(ParseUser.getCurrentUser().getObjectId())) {

                        Intent intent = new Intent(context, special_speaker_live_table.class);
                        intent.putExtra("ID", parseObjects.get(position).getObjectId());
                        MainActivity.activity.startActivity(intent);
                        MainActivity.activity.overridePendingTransition(R.anim.slide_from_end, R.anim.stationary);
                    } else {

                        if (!parseObjects.get(position).getList("BANS").contains(ParseUser.getCurrentUser().getObjectId())) {
                            Intent intent = new Intent(context, sound_preview.class);
                            intent.putExtra("ID", parseObjects.get(position).getObjectId());
                            MainActivity.activity.startActivity(intent);
                            MainActivity.activity.overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
                        } else {


                            AlertDialog.Builder builder1 = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
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

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.activity, R.style.AlertDialogCustom);
                    builder1.setMessage(resources.getString(R.string.SORRY_CREATOR_DISMISSED));
                    builder1.setTitle(resources.getString(R.string.dismissed_table));
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

        PushDownAnim.setPushDownAnimTo(holder.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String QR="https://voive.in/tables/invites/?id="+parseObjects.get(position).getObjectId();
                String main_string="सुनने "+parseObjects.get(position).getString("table_name")+ " में क्या बात चित हो रही है। " + QR;
                RoundedBottomSheetDialog roundedBottomSheetDialog = new RoundedBottomSheetDialog(context);

                View view = LayoutInflater.from(context).inflate(R.layout.bottomsheet_comment_layout, null, false);
                roundedBottomSheetDialog.setContentView(view);

                SharedPreferences sharedPreferences_lan = context.getSharedPreferences("LANG", Context.MODE_PRIVATE);
                String lng = sharedPreferences_lan.getString("lng", "en");
                Context language_context = LocaleHelper.setLocale(context, lng);
                Resources resources = language_context.getResources();


                LinearLayout twitter = view.findViewById(R.id.lr1);
                LinearLayout whtsapp = view.findViewById(R.id.lr2);
                LinearLayout copy = view.findViewById(R.id.lr3);
                LinearLayout msg = view.findViewById(R.id.lr4);
                LinearLayout more = view.findViewById(R.id.lr5);

                PushDownAnim.setPushDownAnimTo(msg).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                        sendIntent.setData(Uri.parse("sms:"));
                        sendIntent.putExtra("sms_body", main_string);
                        context.startActivity(sendIntent);

                    }
                });
                PushDownAnim.setPushDownAnimTo(copy).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText("Sneak Link", main_string);
                        clipboardManager.setPrimaryClip(clipData);
                        Toast.makeText(context, "Link Copied", Toast.LENGTH_SHORT).show();
                        roundedBottomSheetDialog.dismiss();

                    }
                });
                PushDownAnim.setPushDownAnimTo(whtsapp).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                        whatsappIntent.setType("text/plain");
                        whatsappIntent.setPackage("com.whatsapp");
                        whatsappIntent.putExtra(Intent.EXTRA_TEXT, main_string);
                        try {
                            context.startActivity(whatsappIntent);
                        } catch (ActivityNotFoundException ex) {
                            final String appPackageName = "com.whatsapp"; // getPackageName() from Context or Activity object
                            try {
                                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            } catch (ActivityNotFoundException anfe) {
                                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }
                        }

                    }
                });
                PushDownAnim.setPushDownAnimTo(twitter).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareTwitter(main_string);

                    }
                });
                PushDownAnim.setPushDownAnimTo(more).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Share Bite Of Conversation");
                        intent.putExtra(Intent.EXTRA_TEXT, main_string);
                        context.startActivity(Intent.createChooser(intent, "Share Via"));
                    }
                });
                roundedBottomSheetDialog.show();



            }
        });

        ParseQuery<ParseObject> user_data = new ParseQuery<ParseObject>("roundtable_user_data");
        user_data.whereEqualTo("User_ID", ParseUser.getCurrentUser().getObjectId());
        user_data.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                List<String> SUBSCRIPTIONS_LIST = objects.get(0).getList("Subscriptions");

                if(SUBSCRIPTIONS_LIST.contains(parseObjects.get(position).getObjectId())){
                    holder.indicator.setVisibility(View.VISIBLE);
                }
                else {
                    holder.indicator.setVisibility(View.GONE);
                }
            }
        });

    }

    private void shareTwitter(String message) {
        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        tweetIntent.putExtra(Intent.EXTRA_TEXT, message);
        tweetIntent.setType("text/plain");

        PackageManager packManager = context.getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for (ResolveInfo resolveInfo : resolvedInfoList) {
            if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")) {
                tweetIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name);
                resolved = true;
                break;
            }
        }
        if (resolved) {
            context.startActivity(tweetIntent);
        } else {
            final String appPackageName = "com.twitter.android"; // getPackageName() from Context or Activity object
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (ActivityNotFoundException anfe) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }


        }
    }

    @Override
    public int getItemCount() {
        try {
            return parseObjects.size();
        } catch (NullPointerException ex) {


            Alerter.create((Activity) context)
                    .setBackgroundColorInt(context.getResources().getColor(android.R.color.holo_red_light))
                    .setTitle("Failed To Load tables :(")
                    .hideIcon()
                    .enableSwipeToDismiss()
                    .setDuration(10000)
                    .show();

            return 0;


        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ConstraintLayout constraintLayout;
        TextView title, creator, subs;
        LinearLayout if_title_empty_linear;
        RippleView rippleView;
        RelativeLayout is_live_relative;
        TextView no_title;
        ImageView indicator;
        CircleImageView circleImageView;
        MaterialButton menu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            constraintLayout = itemView.findViewById(R.id.clickable_part);
            circleImageView = itemView.findViewById(R.id.avtar);
            title = itemView.findViewById(R.id.textView32);
            rippleView=itemView.findViewById(R.id.more);
            is_live_relative=itemView.findViewById(R.id.is_live_relative);
            if_title_empty_linear=itemView.findViewById(R.id.if_title_empty_linear);
            indicator = itemView.findViewById(R.id.already_subscribed_indicator);
            subs = itemView.findViewById(R.id.total_subscriber);
            menu = itemView.findViewById(R.id.menu);
            no_title=itemView.findViewById(R.id.no_title);
            creator = itemView.findViewById(R.id.textView33);
        }
    }
    public boolean isColorDark(int color){
        double darkness = 1-(0.299* Color.red(color) + 0.587*Color.green(color) + 0.114*Color.blue(color))/255;
        if(darkness<0.5){
            return false; // It's a light color
        }else{
            return true; // It's a dark color
        }
    }
}
