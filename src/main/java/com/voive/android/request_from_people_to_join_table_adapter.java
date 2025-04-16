package com.voive.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.button.MaterialButton;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class request_from_people_to_join_table_adapter extends RecyclerView.Adapter<request_from_people_to_join_table_adapter.ViewHolder>  {


    List<ParseUser> objects;
    Context context;
    String TABLE_ID;

    public request_from_people_to_join_table_adapter(List<ParseUser> objects, Context context, String TABLE_ID) {
        this.objects = objects;
        this.context = context;
        this.TABLE_ID = TABLE_ID;
    }

    @NonNull
    @Override



    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

                view= LayoutInflater.from(parent.getContext()).inflate(R.layout.request_of_people_layout,parent,false);

                return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        SharedPreferences languagepref = context.getSharedPreferences("LANG", MODE_PRIVATE);
        String languagecode = languagepref.getString("lng", "en");
        Context contextf = LocaleHelper.setLocale(context, languagecode);
        Resources resources = contextf.getResources();

        holder.top_name.setText(objects.get(position).getString("First_and_last_name"));

        Glide.with(context.getApplicationContext()).asBitmap().load(objects.get(position).getParseFile("speaker_profile_pic").getUrl()).centerInside().into(holder.circleImageView);



        Glide.with(context.getApplicationContext()).asBitmap().load(objects.get(position).getParseFile("speaker_profile_pic").getUrl())
                .centerInside().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(@Nullable Palette palette) {

                        Palette.Swatch swatch = null;
                        swatch = palette.getDominantSwatch();

                        if (swatch != null) {

                           holder.top_name.setTextColor(swatch.getRgb());

                        } else {

                            holder.top_name.setTextColor(context.getResources().getColor(R.color.themetextcolor));

                        }


                    }
                });
            }
        });

        ParseObject.registerSubclass(notification_modal.class);


        PushDownAnim.setPushDownAnimTo(holder.tick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseQuery<ParseObject> parseObjectParseQuery=new ParseQuery<ParseObject>("live_tables");
                parseObjectParseQuery.getInBackground(TABLE_ID, new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {


                        List<String> SUBS=object.getList("table_subscribers");

                        List<String> W_SPEAK=object.getList("WHO_SPEAK");

                        List<String> NOTIFY=object.getList("NOTIFY");

                        SUBS.add(ParseUser.getCurrentUser().getObjectId());
                        object.put("table_subscribers",SUBS);

                        NOTIFY.add(ParseUser.getCurrentUser().getObjectId());
                        object.put("NOTIFY",NOTIFY);

                        if(object.getBoolean("EVERYONE_CAN_TALK")){

                            W_SPEAK.add(ParseUser.getCurrentUser().getObjectId());
                            object.put("WHO_SPEAK",W_SPEAK);
                        }

                        ACProgressFlower dialog = new ACProgressFlower.Builder(context)
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


                    }
                });

            }
        });

        PushDownAnim.setPushDownAnimTo(holder.cross).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                removeAt(position);
                ParseQuery<ParseUser> FOR_NOTIFY=ParseUser.getQuery();
                FOR_NOTIFY.getInBackground(objects.get(position).getObjectId(), new GetCallback<ParseUser>() {
                    @Override
                    public void done(ParseUser object, ParseException e) {

                        removeAt(position);

                        ParseQuery<ParseInstallation> parseInstallationParseQuery = ParseInstallation.getQuery();
                        parseInstallationParseQuery.whereEqualTo("UserId",objects.get(position).getObjectId());
                        parseInstallationParseQuery.findInBackground(new FindCallback<ParseInstallation>() {
                            @Override
                            public void done(List<ParseInstallation> objects, ParseException e) {


                                JSONObject data = new JSONObject();
                                try {
                                    data.put("alert", object.getString("table_name")+" Rejected your request to be part of the table");
                                    data.put("title",object.getString("table_name"));
                                } catch ( JSONException eT) {

                                    Log.i("ERROR", eT.getLocalizedMessage());
                                    // should not happen
                                }
                                ParsePush push = new ParsePush();
                                push.setQuery(parseInstallationParseQuery);
                                push.setData(data);
                                push.sendInBackground();

                            }
                        });
                        if(object.getBoolean("In_App_updates")){


                            notification_modal notification_modal=new notification_modal();
                            notification_modal.setREAD(false);
                            notification_modal.setCONTENT(object.getString("table_name")+" Rejected your request to be part of the table.");
                            notification_modal.setSENDER(ParseUser.getCurrentUser().getObjectId());
                            notification_modal.setRECEIVER(objects.get(position).getObjectId());
                            notification_modal.saveInBackground();


                        }


                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        CircleImageView circleImageView;
        TextView top_name;
        MaterialButton tick,cross;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            circleImageView=itemView.findViewById(R.id.circleImageView8);
            top_name=itemView.findViewById(R.id.mainlanguage);
            tick=itemView.findViewById(R.id.acceept_invitation);
            cross=itemView.findViewById(R.id.reject_invitation);


        }
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public void removeAt(int position) {
        objects.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,objects.size());
    }
}
