package com.voive.android;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.tapadoo.alerter.Alerter;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class suggested_table_adapter_small extends RecyclerView.Adapter<suggested_table_adapter_small.ViewHolder> {
    Context context;
    List<ParseObject> parseObjects;

    public suggested_table_adapter_small(Context context, List<ParseObject> parseObjects) {
        this.context = context;
        this.parseObjects = parseObjects;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tables_item_layout_small, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng = sharedPreferences.getString("lng", "en");
        Context language_context = LocaleHelper.setLocale(context, lng);
        Resources resources = language_context.getResources();


        ParseObject.registerSubclass(notification_modal.class);


        Glide.with(context).load(parseObjects.get(position).getParseFile("table_image").getUrl()).centerCrop().into(holder.circleImageView);
        if (parseObjects.get(position).getList("table_subscribers").contains(ParseUser.getCurrentUser().getObjectId())) {
            holder.stream.setText(resources.getString(R.string.Subscribe));
        } else {
            holder.stream.setText(resources.getString(R.string.Unsubscribe));
        }


        holder.title.setText(parseObjects.get(position).getList("table_subscribers").size() + " " + resources.getString(R.string.Subscribers));
        ;
        ParseQuery<ParseUser> parseUserParseQuery = ParseUser.getQuery();


        holder.creator.setText(parseObjects.get(position).getString("table_name"));


        PushDownAnim.setPushDownAnimTo(holder.constraintLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (parseObjects.get(position).getBoolean("Active")) {

                    if (parseObjects.get(position).getList("co_creator").contains(ParseUser.getCurrentUser().getObjectId()) || parseObjects.get(position).getString("table_creator").equals(ParseUser.getCurrentUser().getObjectId())
                            || parseObjects.get(position).getList("table_subscribers").contains(ParseUser.getCurrentUser().getObjectId())) {

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
                } else {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
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
        PushDownAnim.setPushDownAnimTo(holder.stream).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> REQUESTS = parseObjects.get(position).getList("table_subscribers");
                REQUESTS.add(ParseUser.getCurrentUser().getObjectId());
                parseObjects.get(position).put("table_subscribers", REQUESTS);

                //NOTIFIcATIOn
                List<String> NOTIFY = parseObjects.get(position).getList("NOTIFY");
                NOTIFY.add(ParseUser.getCurrentUser().getObjectId());
                parseObjects.get(position).put("NOTIFY", NOTIFY);

                parseObjects.get(position).saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        holder.stream.setText(resources.getString(R.string.Unsubscribe));
                        List<String> tuned_ins = parseObjects.get(position).getList("ONLINE");
                        tuned_ins.remove(ParseUser.getCurrentUser().getObjectId());
                        parseObjects.get(position).put("ONLINE", tuned_ins);
                        parseObjects.get(position).saveInBackground();
                        Toast.makeText(context, "Subscribed", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(context, special_speaker_live_table.class);
                        intent1.putExtra("ID", parseObjects.get(position).getObjectId());
                        context.startActivity(intent1);
                        ((Activity) context).overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
                    }
                });

                ParseQuery<ParseInstallation> parseInstallationParseQuery = ParseInstallation.getQuery();
                parseInstallationParseQuery.whereEqualTo("UserId", parseObjects.get(position).getString("table_creator"));
                parseInstallationParseQuery.findInBackground(new FindCallback<ParseInstallation>() {
                    @Override
                    public void done(List<ParseInstallation> objects, ParseException e) {


                        JSONObject data = new JSONObject();
                        try {
                            data.put("alert", ParseUser.getCurrentUser().getString("First_and_last_name") + " Subscribe Your Table");
                            data.put("title", ParseUser.getCurrentUser().getString("First_and_last_name"));
                            data.put("Id", parseObjects.get(position).getObjectId());
                            data.put("TYPE", Constant.TABLE_OTHER_NOTIFICATION);
                        } catch (JSONException te) {
                            throw new IllegalArgumentException("unexpected parsing error", te);
                        }
                        ParsePush push = new ParsePush();
                        push.setQuery(parseInstallationParseQuery);
                        push.setData(data);
                        push.sendInBackground();


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
        MaterialButton stream;
        CircleImageView circleImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            constraintLayout = itemView.findViewById(R.id.clickable_part);
            circleImageView = itemView.findViewById(R.id.avtar);
            title = itemView.findViewById(R.id.textView32);
            stream = itemView.findViewById(R.id.stream);
            creator = itemView.findViewById(R.id.textView33);
        }
    }
}
