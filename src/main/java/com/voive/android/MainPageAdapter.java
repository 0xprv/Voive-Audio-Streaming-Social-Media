package com.voive.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.ColorUtils;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.andexert.library.RippleView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.button.MaterialButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainPageAdapter extends RecyclerView.Adapter<MainPageAdapter.ViewHolder> {


    List<ParseObject> objects;
    Context context;
    Activity activity;

    Resources resources;

    public MainPageAdapter(List<ParseObject> objects, Context context, Activity activity) {
        this.objects = objects;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override


    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.current_active_table_item_layout, parent, false);

        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SharedPreferences sharedPreferences_lan = context.getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng = sharedPreferences_lan.getString("lng", "en");
        Context language_context = LocaleHelper.setLocale(context, lng);
        resources = language_context.getResources();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");




        if(objects.get(position).getBoolean("Active")){


            if(objects.get(position).getString("TALKING_TITLE").trim().isEmpty()){


                holder.if_title_empty_linear.setVisibility(View.VISIBLE);
                holder.talking_title.setVisibility(View.GONE);
                holder.ttt.setText(resources.getString(R.string.No_Title));
            }
            else {
                holder.if_title_empty_linear.setVisibility(View.GONE);
                holder.talking_title.setVisibility(View.VISIBLE);
                holder.talking_title.setText(objects.get(position).getString("TALKING_TITLE"));
            }
            holder.TITLE.setText(objects.get(position).getString("table_name"));



            Glide.with(context.getApplicationContext()).asBitmap().load(objects.get(position).getParseFile("table_image").getUrl())
                    .centerInside().thumbnail(0.5f).placeholder(R.drawable.main_placeholder).into(holder.circleImageView2);

            try {

                Calendar calendar1,calendar2;
                String string1 = objects.get(position).getString("START_BACK");
                Date time1 = new SimpleDateFormat("HH:mm:ss").parse(string1);

                String string2 = objects.get(position).getString("END_BACK");
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
                if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {

                    holder.live_container.setVisibility(View.GONE);
                    holder.timer.setVisibility(View.GONE);

                    if(objects.get(position).getList("ONLINE").size()>0){
                        holder.total_subscriber.setText(Integer.toString(objects.get(position).getList("ONLINE").size()));
                    }
                    else {
                        holder.total_subscriber.setText("####");
                    }





                }
                else {
                    holder.circleImageView2.setBorderWidth(2);
                    holder.how_many.setVisibility(View.GONE);
                    holder.timer.setVisibility(View.VISIBLE);
                    holder.live_container.setVisibility(View.GONE);
                    // table is not live
                    String start_string = objects.get(position).getString("START_BACK");
                    Date start_time = new SimpleDateFormat("HH:mm:ss").parse(start_string);


                    String current_time_string =  currentTime.getHours()+":"+currentTime.getMinutes()+":"+currentTime.getSeconds();
                    Date final_date = new SimpleDateFormat("HH:mm:ss").parse(current_time_string);

                    long final_milis;
                    if(final_date.getHours()>start_time.getHours()){

                        Date date1 = dateFormat.parse("02/26/2099 "+current_time_string);
                        Date date2 = dateFormat.parse("02/27/2099 "+start_string);

                        long diff = date2.getTime() - date1.getTime();
                        final_milis=diff;

                    }
                    else {
                        long diffInMs = start_time.getTime() - final_date.getTime();
                        final_milis=diffInMs;
                    }

                    new CountDownTimer(final_milis, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                            int hours = (int) TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                            int minutes = (int) (TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % TimeUnit.HOURS.toMinutes(1));
                            int seconds = (int) (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % TimeUnit.MINUTES.toSeconds(1));
                            String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);

                            holder.timer.setText(timeLeftFormatted);

                        }

                        @Override
                        public void onFinish() {


                        }
                    }.start();

                }




            }
            catch (java.text.ParseException parseException) {
                parseException.printStackTrace();
            }
            PushDownAnim.setPushDownAnimTo(holder.back_click).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {

                        Calendar calendar1,calendar2;
                        String string1 = objects.get(position).getString("START_BACK");
                        Date time1 = new SimpleDateFormat("HH:mm:ss").parse(string1);

                        String string2 = objects.get(position).getString("END_BACK");
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
                        if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {

                            Intent intent=new Intent(context,special_speaker_live_table.class);
                            intent.putExtra("ID",objects.get(position).getObjectId());
                            MainActivity.activity.startActivity(intent);
                            MainActivity.activity.overridePendingTransition(R.anim.bottom_up, R.anim.stationary);

                        }
                        else {
                            Intent intent=new Intent(context,time_remain_to_live_activity.class);
                            intent.putExtra("ID",objects.get(position).getObjectId());
                            MainActivity.activity.startActivity(intent);
                            MainActivity.activity.overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
                        }
                    } catch (java.text.ParseException parseException) {
                        parseException.printStackTrace();
                    }





                }
            });

        }
        else {

            PushDownAnim.setPushDownAnimTo(holder.back_click).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    ACProgressFlower dialogp = new ACProgressFlower.Builder(MainActivity.activity)
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

                    List<String> subscriber = objects.get(position).getList("table_subscribers");
                    subscriber.remove(ParseUser.getCurrentUser().getObjectId());
                    objects.get(position).put("table_subscribers", subscriber);


                    List<String> notifyr = objects.get(position).getList("NOTIFY");
                    notifyr.remove(ParseUser.getCurrentUser().getObjectId());
                    objects.get(position).put("NOTIFY", notifyr);


                    List<String> speakers = objects.get(position).getList("table_speakers");
                    speakers.remove(ParseUser.getCurrentUser().getObjectId());
                    objects.get(position).put("table_speakers", speakers);


                    List<String> raise_hand = objects.get(position).getList("ONLINE");
                    raise_hand.remove(ParseUser.getCurrentUser().getObjectId());
                    objects.get(position).put("ONLINE", raise_hand);

                    objects.get(position).saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {

                            if(e==null){


                                ParseQuery<ParseObject> parseObjectParseQuery=new ParseQuery<ParseObject>("roundtable_user_data");
                                parseObjectParseQuery.whereEqualTo("User_ID", ParseUser.getCurrentUser().getObjectId());
                                parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> objectsT, ParseException e) {

                                        for(ParseObject parseObject:objectsT){

                                            List<String> ss=parseObject.getList("Subscriptions");
                                            ss.remove(objects.get(position).getObjectId());
                                            parseObject.put("Subscriptions",ss);
                                            parseObject.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    remove(position);
                                                    dialogp.dismiss();
                                                    Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }

                                    }
                                });
                            }
                        }
                    });


                }
            });
        }



        Glide.with(context.getApplicationContext()).asBitmap().load(objects.get(position).getParseFile("table_image").getUrl())
                .centerInside().thumbnail(0.3f).placeholder(R.drawable.main_placeholder).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(@Nullable Palette palette) {

                                Palette.Swatch swatch = palette.getLightVibrantSwatch() != null
                                        ? palette.getLightVibrantSwatch() : palette.getDominantSwatch();
                                if (swatch != null  && !isColorDark(swatch.getRgb())) {
                                    holder.TITLE.setTextColor(swatch.getRgb());
                                } else {
                                    holder.TITLE.setTextColor(Color.parseColor("#BEBEBE"));
                                }


                                if (ParseUser.getCurrentUser().getString("WHAT_LISTENING").equals(objects.get(position).getObjectId())) {

                                    @ColorInt
                                    int alphaColor = ColorUtils.setAlphaComponent(swatch.getRgb(), 50);
                                    holder.back_click.setBackgroundColor(alphaColor);
                                    holder.talking_title.setTextColor(manipulateColor(swatch.getRgb(), 0.8f));
                                    holder.ttt.setTextColor(manipulateColor(swatch.getRgb(), 0.8f));
                                    holder.people_icon.setIconTint(ColorStateList.valueOf(manipulateColor(swatch.getRgb(), 0.8f)));
                                    holder.total_subscriber.setTextColor(manipulateColor(swatch.getRgb(), 0.8f));
                                    holder.is_live_relative.setVisibility(View.VISIBLE);

                                } else {
                                    holder.is_live_relative.setVisibility(View.GONE);
                                    holder.ttt.setTextColor(resources.getColor(R.color.themetextsecondarycolor));
                                    holder.talking_title.setTextColor(Color.parseColor("#F8F7F7"));
                                    holder.back_click.setBackgroundColor(Color.parseColor("#121212"));
                                    holder.people_icon.setIconTint(ColorStateList.valueOf(Color.parseColor("#BEBEBE")));
                                    holder.total_subscriber.setTextColor(Color.parseColor("#BEBEBE"));
                                }


                                ParseLiveQueryClient parseLiveQueryClient = null;

                                try {
                                    parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI(App.getLIVE()));
                                } catch (URISyntaxException et) {
                                    et.printStackTrace();
                                }

                                ParseQuery<ParseObject> parseUserParseQuery12 = new ParseQuery<ParseObject>("_User");
                                parseUserParseQuery12.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());

                                SubscriptionHandling<ParseObject> subscriptionHandling2 = parseLiveQueryClient.subscribe(parseUserParseQuery12);

                                subscriptionHandling2.handleEvent(SubscriptionHandling.Event.UPDATE, new SubscriptionHandling.HandleEventCallback<ParseObject>() {
                                    @Override
                                    public void onEvent(ParseQuery<ParseObject> query, ParseObject object) {

                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                try {


                                                    if (object.getString("WHAT_LISTENING").equals(objects.get(position).getObjectId())) {

                                                        @ColorInt
                                                        int alphaColor = ColorUtils.setAlphaComponent(swatch.getRgb(), 50);
                                                        holder.back_click.setBackgroundColor(alphaColor);
                                                        holder.talking_title.setTextColor(manipulateColor(swatch.getRgb(), 0.8f));
                                                        holder.ttt.setTextColor(manipulateColor(swatch.getRgb(), 0.8f));
                                                        holder.people_icon.setIconTint(ColorStateList.valueOf(manipulateColor(swatch.getRgb(), 0.8f)));
                                                        holder.total_subscriber.setTextColor(manipulateColor(swatch.getRgb(), 0.8f));
                                                        holder.is_live_relative.setVisibility(View.VISIBLE);

                                                    } else {
                                                        holder.is_live_relative.setVisibility(View.GONE);
                                                        holder.ttt.setTextColor(resources.getColor(R.color.themetextsecondarycolor));
                                                        holder.talking_title.setTextColor(Color.parseColor("#F8F7F7"));
                                                        holder.back_click.setBackgroundColor(Color.parseColor("#121212"));
                                                        holder.people_icon.setIconTint(ColorStateList.valueOf(Color.parseColor("#BEBEBE")));
                                                        holder.total_subscriber.setTextColor(Color.parseColor("#BEBEBE"));
                                                    }
                                                }
                                                catch (IndexOutOfBoundsException ff){
                                                    ff.printStackTrace();
                                                }
                                            }
                                        });





                                    }
                                });


                            }
                        });
                    }
                });


    }

    public void ref(List<ParseObject> newList) {

        try {


            DiffUtils_Subscriptions diffUtils = new DiffUtils_Subscriptions(objects, newList);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtils);

            this.objects.clear();
            objects.addAll(newList);
            diffResult.dispatchUpdatesTo(this);

        } catch (NullPointerException fgg) {

            fgg.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void remove(int pos) {

        objects.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeRemoved(pos, objects.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView TITLE, talking_title, total_subscriber, timer,lv,ttt,dismissed,cl_to_r;
        CircleImageView circleImageView2;
        MaterialButton live,people_icon;
        LinearLayout live_container,how_many;
        RippleView more;
        RelativeLayout is_live_relative;
        LinearLayout if_title_empty_linear;
        ConstraintLayout back_click;
        ImageView is_lv;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            TITLE = itemView.findViewById(R.id.textView33);
            total_subscriber = itemView.findViewById(R.id.total_subscriber);
            circleImageView2 = itemView.findViewById(R.id.circular);
            live_container = itemView.findViewById(R.id.live_container);
            ttt=itemView.findViewById(R.id.ttt);
            dismissed=itemView.findViewById(R.id.dismissed);
            cl_to_r=itemView.findViewById(R.id.cl_to_r);
            timer = itemView.findViewById(R.id.timer);
            is_lv=itemView.findViewById(R.id.is_lv);
            is_live_relative=itemView.findViewById(R.id.is_live_relative);
            people_icon=itemView.findViewById(R.id.people_icon);
            lv=itemView.findViewById(R.id.lv);
            live = itemView.findViewById(R.id.live);
            more=itemView.findViewById(R.id.more);
            how_many=itemView.findViewById(R.id.how_many);
            if_title_empty_linear=itemView.findViewById(R.id.if_title_empty_linear);
            talking_title = itemView.findViewById(R.id.textView9);
            back_click = itemView.findViewById(R.id.clickable_part);
        }
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
    @ColorInt
    public static int adjustAlpha(@ColorInt int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }
    public boolean isColorDark(int color){
        double darkness = 1-(0.299*Color.red(color) + 0.587*Color.green(color) + 0.114*Color.blue(color))/255;
        if(darkness<0.5){
            return false; // It's a light color
        }else{
            return true; // It's a dark color
        }
    }
}
