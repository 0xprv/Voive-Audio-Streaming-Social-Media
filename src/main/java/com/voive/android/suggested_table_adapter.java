package com.voive.android;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;
import com.tapadoo.alerter.Alerter;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class suggested_table_adapter extends RecyclerView.Adapter<suggested_table_adapter.ViewHolder> {
    Context context;
    List<ParseObject> parseObjects;

    public suggested_table_adapter(Context context, List<ParseObject> parseObjects) {
        this.context = context;
        this.parseObjects = parseObjects;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tables_item_ayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng = sharedPreferences.getString("lng", "en");
        Context language_context = LocaleHelper.setLocale(context, lng);
        Resources resources = language_context.getResources();

      /*  Glide.with(context).load(parseObjects.get(position).getParseFile("table_image").getUrl())
                .centerCrop()
                .placeholder(R.drawable.theme_placeholder)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(12, 2)))
                .into(holder.roundedImage);*/


        ParseObject.registerSubclass(notification_modal.class);


        Glide.with(context).load(parseObjects.get(position).getParseFile("table_image").getUrl()).centerCrop().into(holder.circleImageView);

        holder.title.setText(parseObjects.get(position).getList("table_subscribers").size() + " " + resources.getString(R.string.Subscribers));

        holder.creator.setText(parseObjects.get(position).getString("table_name"));
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

        PushDownAnim.setPushDownAnimTo(holder.constraintLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (parseObjects.get(position).getBoolean("Active")) {

                    if (parseObjects.get(position).getList("co_creator").contains(ParseUser.getCurrentUser().getObjectId()) ||
                            parseObjects.get(position).getString("table_creator").equals(ParseUser.getCurrentUser().getObjectId()) ||
                            parseObjects.get(position).getList("table_subscribers").contains(ParseUser.getCurrentUser().getObjectId())) {

                        Intent intent = new Intent(context, special_speaker_live_table.class);
                        intent.putExtra("ID", parseObjects.get(position).getObjectId());
                        context.startActivity(intent);
                        ((Activity) context).overridePendingTransition(R.anim.slide_from_end, R.anim.stationary);

                    } else {

                        if (!parseObjects.get(position).getList("BANS").contains(ParseUser.getCurrentUser().getObjectId())) {
                            Intent intent = new Intent(context, sound_preview.class);
                            intent.putExtra("ID", parseObjects.get(position).getObjectId());
                            context.startActivity(intent);
                            ((Activity) context).overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
                        } else {


                            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.activity, R.style.AlertDialogCustom);
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

                } else {

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

                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            if (object.getString("WHAT_LISTENING").equals(parseObjects.get(position).getObjectId())) {


                                holder.is_live_relative.setVisibility(View.VISIBLE);

                            } else {
                                holder.is_live_relative.setVisibility(View.GONE);
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


    public class ViewHolder extends RecyclerView.ViewHolder {


        ConstraintLayout constraintLayout;
        TextView title, creator;
        CircleImageView circleImageView;
        RelativeLayout is_live_relative;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            constraintLayout = itemView.findViewById(R.id.clickable_part);
            circleImageView = itemView.findViewById(R.id.avtar);
            is_live_relative=itemView.findViewById(R.id.is_live_relative);
            title = itemView.findViewById(R.id.textView32);
            creator = itemView.findViewById(R.id.textView33);
        }
    }
}
