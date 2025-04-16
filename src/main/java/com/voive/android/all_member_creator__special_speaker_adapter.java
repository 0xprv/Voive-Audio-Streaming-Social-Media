package com.voive.android;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;
import com.tapadoo.alerter.Alerter;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class all_member_creator__special_speaker_adapter extends RecyclerView.Adapter<all_member_creator__special_speaker_adapter.VireHolder> {

    List<ParseUser> parseObjects;
    Context context;
    String createor_id;
    String table_id;
    Activity activity;

    Handler handler = new Handler();
    Runnable runnable;


    public all_member_creator__special_speaker_adapter(List<ParseUser> parseObjects, Context context, String createor_id, String table_id, Activity activity) {
        this.parseObjects = parseObjects;
        this.context = context;
        this.activity = activity;
        this.createor_id = createor_id;
        this.table_id = table_id;
    }

    public static int darken_color(int color, float factor) {


        int alpha = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);

        return Color.argb(alpha, r, g, b);

    }

    @NonNull
    @Override
    public VireHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.creator_member_item, parent, false);
        return new VireHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VireHolder holder, int position) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng = sharedPreferences.getString("lng", "en");
        Context language_context = LocaleHelper.setLocale(context, lng);
        Resources resources = language_context.getResources();

        try {

            Glide.with(context.getApplicationContext()).asBitmap().placeholder(context.getResources().getDrawable(R.drawable.main_placeholder))
                    .thumbnail(0.5f).load(parseObjects.get(position).getParseFile("speaker_profile_pic").getUrl()).centerInside().into(holder.circleImageView);
            holder.first_last.setText(parseObjects.get(position).getObjectId().equals(ParseUser.getCurrentUser().getObjectId()) ?
                    resources.getString(R.string.YOU) : parseObjects.get(position).getUsername());


            PushDownAnim.setPushDownAnimTo(holder.circleImageView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    if (parseObjects.get(position).getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {

                        Intent intent = new Intent(context, my_own_account.class);
                        context.startActivity(intent);
                        activity.overridePendingTransition(R.anim.slide_from_start, R.anim.stationary);


                    } else {

                        ParseQuery<ParseObject> TBLES = new ParseQuery<ParseObject>("live_tables");
                        TBLES.getInBackground(table_id, new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject object, ParseException e) {



                                if (object.getString("table_creator").equals(parseObjects.get(position).getObjectId())) {
                                    Intent intent = new Intent(context, other_speaker_account.class);
                                    intent.putExtra("ID", parseObjects.get(position));
                                    context.startActivity(intent);
                                    ((Activity) context).overridePendingTransition(R.anim.slide_from_start, R.anim.stationary);

                                } else {

                                    if (object.getString("table_creator").equals(ParseUser.getCurrentUser().getObjectId())) {
                                        SharedPreferences sharedPreferences = context.getSharedPreferences("LANG", Context.MODE_PRIVATE);
                                        String lng = sharedPreferences.getString("lng", "en");
                                        Context language_context = LocaleHelper.setLocale(context, lng);
                                        Resources resources = language_context.getResources();

                                        RoundedBottomSheetDialog roundedBottomSheetDialog = new RoundedBottomSheetDialog(special_speaker_live_table.activity);
                                        roundedBottomSheetDialog.setContentView(R.layout.action_in_user_in_special_speaker);

                                        Button visit_profile, ban;
                                        visit_profile = roundedBottomSheetDialog.findViewById(R.id.visit_profile);

                                        ban = roundedBottomSheetDialog.findViewById(R.id.dismiss_table);
                                        visit_profile.setText(resources.getString(R.string.visit_profile));
                                        ban.setText(resources.getString(R.string.ban));

                                        PushDownAnim.setPushDownAnimTo(visit_profile).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                if (parseObjects.get(position).getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {

                                                    Intent intent = new Intent(context, my_own_account.class);
                                                    context.startActivity(intent);
                                                    ((Activity) context).overridePendingTransition(R.anim.slide_from_start, R.anim.stationary);


                                                } else {

                                                    Intent intent = new Intent(context, other_speaker_account.class);
                                                    intent.putExtra("ID", parseObjects.get(position).getObjectId());
                                                    context.startActivity(intent);
                                                    ((Activity) context).overridePendingTransition(R.anim.slide_from_start, R.anim.stationary);


                                                }


                                            }
                                        });

                                        PushDownAnim.setPushDownAnimTo(ban).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {


                                                AlertDialog.Builder builder1 = new AlertDialog.Builder(special_speaker_live_table.activity, R.style.AlertDialogCustom);
                                                builder1.setMessage(resources.getString(R.string.sure_to_ban_user));
                                                builder1.setTitle(resources.getString(R.string.BANNED_SPEAKER));
                                                builder1.setCancelable(false);

                                                builder1.setPositiveButton(
                                                        resources.getString(R.string.YES),
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {

                                                                List<String> subscriber = object.getList("table_subscribers");
                                                                subscriber.remove(parseObjects.get(position).getObjectId());
                                                                object.put("table_subscribers", subscriber);


                                                                List<String> notifyr = object.getList("NOTIFY");
                                                                notifyr.remove(parseObjects.get(position).getObjectId());
                                                                object.put("NOTIFY", notifyr);


                                                                List<String> speakers = object.getList("table_speakers");
                                                                speakers.remove(parseObjects.get(position).getObjectId());
                                                                object.put("table_speakers", speakers);


                                                                List<String> bannedX = object.getList("BANS");
                                                                bannedX.add(parseObjects.get(position).getObjectId());
                                                                object.put("BANS", bannedX);

                                                                List<String> online = object.getList("ONLINE");
                                                                online.remove(parseObjects.get(position).getObjectId());
                                                                object.put("ONLINE", online);


                                                                object.saveInBackground(new SaveCallback() {
                                                                    @Override
                                                                    public void done(ParseException e) {

                                                                        ParseQuery<ParseInstallation> parseInstallationParseQuery = ParseInstallation.getQuery();
                                                                        parseInstallationParseQuery.whereEqualTo("UserId", parseObjects.get(position).getObjectId());
                                                                        parseInstallationParseQuery.findInBackground(new FindCallback<ParseInstallation>() {
                                                                            @Override
                                                                            public void done(List<ParseInstallation> objects, ParseException e) {


                                                                                JSONObject data = new JSONObject();
// Put data in the JSON object
                                                                                try {
                                                                                    data.put("alert", "You Are Banned From" + " " + object.getString("table_name") + " Due To Suspicious Activities.");
                                                                                    data.put("title", "Banned");
                                                                                    data.put("Type", Constant.BAN_OR_KICKED);
                                                                                    data.put("Id", object.getObjectId());
                                                                                } catch (JSONException eT) {

                                                                                    Log.i("ERROR", eT.getLocalizedMessage());
                                                                                    // should not happen
                                                                                }
// Configure the push
                                                                                ParsePush push = new ParsePush();
                                                                                push.setQuery(parseInstallationParseQuery);
                                                                                push.setData(data);
                                                                                push.sendInBackground();

                                                                            }
                                                                        });

                                                                        dialog.dismiss();
                                                                        Alerter.create((Activity) context)
                                                                                .setText(holder.first_last.getText().toString() + " " + resources.getString(R.string.succes_removed_from_table))
                                                                                .enableSwipeToDismiss()
                                                                                .setDuration(4000)
                                                                                .hideIcon()
                                                                                .setBackgroundColorInt(context.getColor(android.R.color.holo_red_dark))
                                                                                .show();

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
                                        });

                                        roundedBottomSheetDialog.show();

                                    } else {
                                        Intent intent = new Intent(context, other_speaker_account.class);
                                        intent.putExtra("ID", parseObjects.get(position).getObjectId());
                                        context.startActivity(intent);
                                        ((Activity) context).overridePendingTransition(R.anim.slide_from_start, R.anim.stationary);
                                    }

                                }


                            }
                        });

                    }


                }
            });

        }
        catch (Exception ddff){
            ddff.printStackTrace();
        }

        try {


            ParseLiveQueryClient parseLiveQueryClient = null;

            try {
                parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI(App.getLIVE()));
            } catch (URISyntaxException et) {
                et.printStackTrace();
            }

            ParseQuery<ParseObject> parseUserParseQuery12 = new ParseQuery<ParseObject>("_User");
            parseUserParseQuery12.whereEqualTo("objectId", parseObjects.get(position).getObjectId());

            SubscriptionHandling<ParseObject> subscriptionHandling2 = parseLiveQueryClient.subscribe(parseUserParseQuery12);

            subscriptionHandling2.handleEvent(SubscriptionHandling.Event.UPDATE, new SubscriptionHandling.HandleEventCallback<ParseObject>() {
                @Override
                public void onEvent(ParseQuery<ParseObject> query, ParseObject object) {

                    ((AppCompatActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            holder.is_lv.setVisibility(object.getInt("SOUND") > 10 ? View.VISIBLE:View.INVISIBLE);
                        }
                    });




                }
            });


        } catch (Exception fg) {

            fg.printStackTrace();
        }


    }

    public void ref(List<ParseUser> newList) {

        try {


            DiffUtils diffUtils = new DiffUtils(parseObjects, newList);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtils);

            this.parseObjects.clear();
            parseObjects.addAll(newList);
            diffResult.dispatchUpdatesTo(this);

        } catch (NullPointerException fgg) {

            fgg.printStackTrace();
        }


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return parseObjects.size();
    }

    public void remove(int POSITION) {
        parseObjects.remove(POSITION);
        notifyItemRemoved(POSITION);
        notifyItemRangeRemoved(POSITION, parseObjects.size());

    }

    public class VireHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView first_last;
        ImageView is_lv;
        ConstraintLayout constraintLayout;

        public VireHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.circle_image);
            first_last = itemView.findViewById(R.id.first_last_name);
            is_lv=itemView.findViewById(R.id.is_lv);
            constraintLayout = itemView.findViewById(R.id.clickable_part);
            //  moderator=itemView.findViewById(R.id.imageView6);
        }
    }
}
