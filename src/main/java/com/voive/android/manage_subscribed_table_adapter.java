package com.voive.android;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.tapadoo.alerter.Alerter;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import de.hdodenhof.circleimageview.CircleImageView;
import io.agora.rtc.RtcEngine;

public class manage_subscribed_table_adapter extends RecyclerView.Adapter<manage_subscribed_table_adapter.ViewHolder> {
    Context context;
    List<ParseObject> parseObjects;

    public manage_subscribed_table_adapter(Context context, List<ParseObject> parseObjects) {
        this.context = context;
        this.parseObjects = parseObjects;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.full_subscribed_tables_horizonatl_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng = sharedPreferences.getString("lng", "en");
        Context language_context = LocaleHelper.setLocale(context, lng);
        Resources resources = language_context.getResources();
        if (parseObjects.size() > 1) {
            holder.viewD.setVisibility(position == parseObjects.size() - 1 ? View.GONE : View.VISIBLE);

        } else {
            holder.viewD.setVisibility(position == 0 || position == parseObjects.size() - 1 ? View.GONE : View.VISIBLE);
        }

        Glide.with(context).load(parseObjects.get(position).getParseFile("table_image").getUrl()).centerInside().
                thumbnail(0.3f).
                into(holder.circleImageView);

        holder.title.setText(parseObjects.get(position).getString("table_name"));


        holder.notification.setIcon(parseObjects.get(position).getList("NOTIFY").contains(ParseUser.getCurrentUser().getObjectId()) ?
                context.getDrawable(R.drawable.notification_on) :
                context.getDrawable(R.drawable.notification_off));

        PushDownAnim.setPushDownAnimTo(holder.notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (parseObjects.get(position).getList("NOTIFY").contains(ParseUser.getCurrentUser().getObjectId())) {

                    List<String> NTF = parseObjects.get(position).getList("NOTIFY");
                    NTF.remove(ParseUser.getCurrentUser().getObjectId());
                    parseObjects.get(position).put("NOTIFY", NTF);
                    parseObjects.get(position).saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Snackbar snackbar = Snackbar.make(v, parseObjects.get(position).getString("table_name") + " Muted", Snackbar.LENGTH_SHORT);
                            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                            snackbar.setDuration(6000);
                            snackbar.setBackgroundTint(context.getResources().getColor(R.color.themetextcolor));
                            snackbar.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                            snackbar.show();
                            holder.notification.setIcon(parseObjects.get(position).getList("NOTIFY").contains(ParseUser.getCurrentUser().getObjectId()) ?
                                    context.getResources().getDrawable(R.drawable.notification_on) :
                                    context.getResources().getDrawable(R.drawable.notification_off));
                        }
                    });
                } else {

                    List<String> NTF = parseObjects.get(position).getList("NOTIFY");
                    NTF.add(ParseUser.getCurrentUser().getObjectId());
                    parseObjects.get(position).put("NOTIFY", NTF);
                    parseObjects.get(position).saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Snackbar snackbar = Snackbar.make(v, parseObjects.get(position).getString("table_name") + " Unmuted", Snackbar.LENGTH_SHORT);
                            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                            snackbar.setDuration(6000);
                            snackbar.setBackgroundTint(context.getResources().getColor(R.color.themetextcolor));
                            snackbar.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                            snackbar.show();
                            holder.notification.setIcon(parseObjects.get(position).getList("NOTIFY").contains(ParseUser.getCurrentUser().getObjectId()) ?
                                    context.getResources().getDrawable(R.drawable.notification_on) :
                                    context.getResources().getDrawable(R.drawable.notification_off));
                        }
                    });
                }


            }
        });

        PushDownAnim.setPushDownAnimTo(holder.more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if( parseObjects.get(position).getString("table_creator").equals(ParseUser.getCurrentUser().getObjectId())){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
                    builder1.setMessage(resources.getString(R.string.sure_to_dismmiss_table));
                    builder1.setTitle(resources.getString(R.string.dismiss_table));
                    builder1.setCancelable(false);

                    builder1.setPositiveButton(
                            resources.getString(R.string.YES),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    ACProgressFlower dialogp = new ACProgressFlower.Builder(context)
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

                                    List<String> subscriber = parseObjects.get(position).getList("table_subscribers");
                                    subscriber.remove(ParseUser.getCurrentUser().getObjectId());
                                    parseObjects.get(position).put("table_subscribers", subscriber);


                                    List<String> notifyr = parseObjects.get(position).getList("NOTIFY");
                                    notifyr.remove(ParseUser.getCurrentUser().getObjectId());
                                    parseObjects.get(position).put("NOTIFY", notifyr);


                                    List<String> speakers = parseObjects.get(position).getList("table_speakers");
                                    speakers.remove(ParseUser.getCurrentUser().getObjectId());
                                    parseObjects.get(position).put("table_speakers", speakers);


                                    List<String> raise_hand = parseObjects.get(position).getList("ONLINE");
                                    raise_hand.remove(ParseUser.getCurrentUser().getObjectId());
                                    parseObjects.get(position).put("ONLINE", raise_hand);


                                    parseObjects.get(position).put("Active", false);
                                    parseObjects.get(position).saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {

                                            if (e == null) {

                                                dialogp.cancel();
                                                Toast.makeText(context, "Dismissed", Toast.LENGTH_SHORT).show();
                                                ParseQuery<ParseObject> parseObjectParseQuery=new ParseQuery<ParseObject>("roundtable_user_data");
                                                parseObjectParseQuery.whereEqualTo("User_ID",ParseUser.getCurrentUser().getObjectId());
                                                parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                                                    @Override
                                                    public void done(List<ParseObject> objects, ParseException e) {

                                                        for(ParseObject parseObject:objects){

                                                            List<String> ss=parseObject.getList("Subscriptions");
                                                            ss.remove(parseObjects.get(position).getObjectId());
                                                            parseObject.put("Subscriptions",ss);
                                                            parseObject.saveInBackground(new SaveCallback() {
                                                                @Override
                                                                public void done(ParseException e) {
                                                                    remove(position);
                                                                }
                                                            });

                                                        }

                                                    }
                                                });

                                            } else {

                                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

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
                else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
                    builder1.setMessage(resources.getString(R.string.sure_to_leave_tavle));
                    builder1.setTitle(resources.getString(R.string.Unsubscribe));
                    builder1.setCancelable(false);

                    builder1.setPositiveButton(
                            resources.getString(R.string.YES),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    ParseQuery<ParseObject> parseObjectParseQuery=new ParseQuery<ParseObject>("roundtable_user_data");
                                    parseObjectParseQuery.whereEqualTo("User_ID",ParseUser.getCurrentUser().getObjectId());
                                    parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> objects, ParseException e) {

                                            for(ParseObject parseObject:objects){

                                                List<String> ss=parseObject.getList("Subscriptions");
                                                ss.remove(parseObjects.get(position).getObjectId());
                                                parseObject.put("Subscriptions",ss);
                                                parseObject.saveInBackground();

                                            }

                                        }
                                    });
                                    List<String> subscriber = parseObjects.get(position).getList("table_subscribers");
                                    subscriber.remove(ParseUser.getCurrentUser().getObjectId());
                                    parseObjects.get(position).put("table_subscribers", subscriber);


                                    List<String> notifyr = parseObjects.get(position).getList("NOTIFY");
                                    notifyr.remove(ParseUser.getCurrentUser().getObjectId());
                                    parseObjects.get(position).put("NOTIFY", notifyr);


                                    List<String> speakers = parseObjects.get(position).getList("table_speakers");
                                    speakers.remove(ParseUser.getCurrentUser().getObjectId());
                                    parseObjects.get(position).put("table_speakers", speakers);


                                    List<String> raise_hand = parseObjects.get(position).getList("ONLINE");
                                    raise_hand.remove(ParseUser.getCurrentUser().getObjectId());
                                    parseObjects.get(position).put("ONLINE", raise_hand);


                                    parseObjects.get(position).saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {

                                            if (App.WHICH_TABLE.equals(parseObjects.get(position).getObjectId())) {
                                                App.IS_LISTENING = false;
                                                App.WHICH_TABLE = "";
                                                if (special_speaker_live_table.mRtcEngine != null) {
                                                    special_speaker_live_table.mRtcEngine.leaveChannel();
                                                }
                                                RtcEngine.destroy();
                                            }
                                            remove(position);
                                            Toast.makeText(context, "Unsubscribed", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                    dialog.cancel();
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

            }
        });


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

    public void remove(int pos) {

        parseObjects.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeRemoved(pos, parseObjects.size());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {



        TextView title;
        CircleImageView circleImageView;
        View viewD;
        MaterialButton more, notification;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            circleImageView = itemView.findViewById(R.id.circleImageView2);
            title = itemView.findViewById(R.id.textView5);
            notification = itemView.findViewById(R.id.notification);
            viewD = itemView.findViewById(R.id.view);
            more = itemView.findViewById(R.id.button6);

        }
    }
}
