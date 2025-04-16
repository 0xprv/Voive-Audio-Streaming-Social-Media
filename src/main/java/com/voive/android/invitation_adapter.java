package com.voive.android;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.shimmer.ShimmerFrameLayout;
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
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;


public class invitation_adapter extends RecyclerView.Adapter<invitation_adapter.ViewHolder> {


    List<ParseObject> id_s;
    Context context;

    public invitation_adapter(List<ParseObject> id_s, Context context) {
        this.id_s = id_s;
        this.context = context;
    }

    public static int manipulateColor(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r, 255),
                Math.min(g, 255),
                Math.min(b, 255));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invitation_layout, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng = sharedPreferences.getString("lng", "en");
        Context language_context = LocaleHelper.setLocale(context, lng);
        Resources resources = language_context.getResources();
        if (id_s.size() > 1) {
            holder.viewD.setVisibility(position == id_s.size() - 1 ? View.GONE : View.VISIBLE);

        } else {
            holder.viewD.setVisibility(position == 0 || position == id_s.size() - 1 ? View.GONE : View.VISIBLE);
        }

        Glide.with(context).load(id_s.get(position).getParseFile("table_image").getUrl()).centerInside().
                thumbnail(0.3f).
                into(holder.circleImageView);

        holder.title.setText(id_s.get(position).getString("table_name"));


        PushDownAnim.setPushDownAnimTo(holder.acceeptInvitation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseQuery<ParseObject> parseObjectParseQuery1 = new ParseQuery<ParseObject>("Notifications");
                parseObjectParseQuery1.whereEqualTo("USER_ID", ParseUser.getCurrentUser().getObjectId());
                parseObjectParseQuery1.whereEqualTo("TYPE", Constant.SOME_ONE_SEND_INVITATION);
                parseObjectParseQuery1.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {

                        for(ParseObject pp:objects){
                            pp.deleteInBackground();
                        }
                    }
                });

                if (id_s.get(position).getBoolean("Active")) {
                    if (id_s.get(position).getList("BANS").contains(ParseUser.getCurrentUser().getObjectId())) {

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
                        alert11.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        alert11.show();


                    }
                    else {



                        ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("roundtable_user_data");
                        parseObjectParseQuery.whereEqualTo("User_ID", ParseUser.getCurrentUser().getObjectId());
                        parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {

                                for (ParseObject parseObject : objects) {

                                    List<String> ss = id_s.get(position).getList("Subscriptions");
                                    ss.add(id_s.get(position).getObjectId());
                                    parseObject.put("Subscriptions", ss);
                                    parseObject.saveInBackground();

                                }

                            }
                        });

                        List<String> Subscribers = new ArrayList<String>();
                        Subscribers = id_s.get(position).getList("table_subscribers");
                        if(!Subscribers.contains(ParseUser.getCurrentUser().getObjectId())){
                            Subscribers.add(ParseUser.getCurrentUser().getObjectId());
                            id_s.get(position).put("table_subscribers", Subscribers);

                        }

                        List<String> NOTIFY = new ArrayList<String>();
                        NOTIFY = id_s.get(position).getList("NOTIFY");
                        NOTIFY.add(ParseUser.getCurrentUser().getObjectId());

                        id_s.get(position).put("NOTIFY", NOTIFY);

                        id_s.get(position).saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                ParseQuery<ParseInstallation> parseInstallationParseQuery = ParseInstallation.getQuery();
                                parseInstallationParseQuery.whereEqualTo("UserId", id_s.get(position).getString("table_creator"));
                                parseInstallationParseQuery.findInBackground(new FindCallback<ParseInstallation>() {
                                    @Override
                                    public void done(List<ParseInstallation> objects, ParseException e) {


                                        JSONObject data = new JSONObject();
                                        try {
                                            data.put("alert", ParseUser.getCurrentUser().getString("First_and_last_name") + " Subscribe Your Table");
                                            data.put("title", ParseUser.getCurrentUser().getString("First_and_last_name"));
                                            data.put("Id", id_s.get(position).getObjectId());
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

                                ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("roundtable_user_data");
                                parseObjectParseQuery.whereEqualTo("User_ID", ParseUser.getCurrentUser().getObjectId());
                                parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> objects, ParseException e) {


                                        for (ParseObject parseObject : objects) {


                                            List<String> INVITES = parseObject.getList("Invitations");
                                            INVITES.remove(parseObject.getObjectId());
                                            parseObject.put("Invitations", INVITES);
                                            parseObject.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {


                                                    removeAt(position);
                                                    Intent intent = new Intent(context, special_speaker_live_table.class);
                                                    intent.putExtra("ID", id_s.get(position).getObjectId());
                                                    context.startActivity(intent);
                                                }
                                            });

                                        }

                                    }
                                });

                            }
                        });

                    }

                }
                else {

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
                    alert11.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    alert11.show();

                }

            }
        });
        PushDownAnim.setPushDownAnimTo(holder.clk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, invitation_of_table_activity.class);
                intent.putExtra("ID",id_s.get(position).getObjectId());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
            }
        });
    }

    @Override
    public int getItemCount() {
        return id_s.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void removeAt(int position) {
        id_s.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, id_s.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        CircleImageView circleImageView;
        View viewD;
        ConstraintLayout clk;
        MaterialButton acceeptInvitation;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.circleImageView2);
            title = itemView.findViewById(R.id.textView5);
            acceeptInvitation = itemView.findViewById(R.id.notification);
            viewD = itemView.findViewById(R.id.view);
            clk=itemView.findViewById(R.id.clk);
        }
    }
}
