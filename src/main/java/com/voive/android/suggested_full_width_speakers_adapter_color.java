package com.voive.android;

import static android.content.Context.VIBRATOR_SERVICE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
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

import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import de.hdodenhof.circleimageview.CircleImageView;

public class suggested_full_width_speakers_adapter_color extends RecyclerView.Adapter<suggested_full_width_speakers_adapter_color.ViewHolder> {
    Context context;
    List<ParseUser> strings;
    Palette.Swatch COLOR;


    public suggested_full_width_speakers_adapter_color(Context context, List<ParseUser> strings, Palette.Swatch COLOR) {
        this.context = context;
        this.strings = strings;
        this.COLOR = COLOR;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.full_width_speaker_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng = sharedPreferences.getString("lng", "en");
        Context language_context = LocaleHelper.setLocale(context, lng);
        Resources resources = language_context.getResources();

        if (!strings.get(position).getString("speaker_information").trim().isEmpty()) {

            holder.des.setText(strings.get(position).getString("speaker_information"));
            holder.des.setVisibility(View.VISIBLE);
        } else {
            holder.des.setVisibility(View.GONE);

        }
        holder.des.setTextColor(COLOR.getTitleTextColor());
        holder.textView10.setText(strings.get(position).getUsername());


       /* if(strings.get(position).getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){

            holder.button5.setVisibility(View.GONE);
        }
        else {
            holder.button5.setVisibility(View.VISIBLE);
        }
*/


        PushDownAnim.setPushDownAnimTo(holder.clickable_part).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (strings.get(position).getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {

                    Intent intent = new Intent(context, my_own_account.class);
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.slide_from_start, R.anim.stationary);


                } else {

                    Intent intent = new Intent(context, other_speaker_account.class);
                    intent.putExtra("ID", strings.get(position));
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.slide_from_start, R.anim.stationary);


                }

            }
        });


        ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("roundtable_user_data");
        parseObjectParseQuery.whereEqualTo("User_ID", ParseUser.getCurrentUser().getObjectId());
        parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                for (ParseObject parseObject : objects) {


                    if (parseObject.getList("Following").contains(strings.get(position).getObjectId())) {


                        holder.button5.setText(resources.getString(R.string.Following));
                        holder.button5.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.transparent)));
                        holder.button5.setStrokeWidth(2);
                        holder.button5.setStrokeColor(ColorStateList.valueOf(COLOR.getBodyTextColor()));


                    } else {

                        holder.button5.setText(resources.getString(R.string.Follow));
                        holder.button5.setStrokeWidth(0);
                        holder.button5.setBackgroundTintList(ColorStateList.valueOf(COLOR.getBodyTextColor()));

                        double darkness = 1 - (0.299 * Color.red(COLOR.getBodyTextColor()) + 0.587 * Color.green(COLOR.getBodyTextColor())
                                + 0.114 * Color.blue(COLOR.getBodyTextColor()) / 255);
                        if (darkness < 0.5) {

                            holder.button5.setTextColor(context.getResources().getColor(R.color.black));
                        } else {

                            holder.button5.setTextColor(context.getResources().getColor(R.color.white));

                        }


                    }

                }

            }
        });

        PushDownAnim.setPushDownAnimTo(holder.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("roundtable_user_data");
                parseObjectParseQuery.whereEqualTo("User_ID", ParseUser.getCurrentUser().getObjectId());
                parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {


                        for (ParseObject parseObject : objects) {


                            if (parseObject.getList("Following").contains(strings.get(position).getObjectId())) {


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

                                dialog.setCancelable(false);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.show();

                                List<String> NEWF = parseObject.getList("Following");
                                NEWF.remove(strings.get(position).getObjectId());

                                parseObject.put("Following", NEWF);
                                parseObject.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {

                                        ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("roundtable_user_data");
                                        parseObjectParseQuery.whereEqualTo("User_ID", strings.get(position).getObjectId());
                                        parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                                            @Override
                                            public void done(List<ParseObject> objects, ParseException e) {

                                                for (ParseObject parseObject1 : objects) {

                                                    List<String> NEWF = parseObject1.getList("Followers");
                                                    NEWF.remove(ParseUser.getCurrentUser().getObjectId());

                                                    List<String> NFT = parseObject1.getList("NOTIFY");
                                                    NFT.remove(ParseUser.getCurrentUser().getObjectId());

                                                    parseObject1.put("Followers", NEWF);
                                                    parseObject1.put("NOTIFY", NFT);
                                                    parseObject1.saveInBackground(new SaveCallback() {
                                                        @Override
                                                        public void done(ParseException e) {

                                                            if (e == null) {


                                                                holder.button5.setText(resources.getString(R.string.Follow));
                                                                holder.button5.setBackgroundTintList(ColorStateList.valueOf(COLOR.getBodyTextColor()));
                                                                holder.button5.setStrokeWidth(0);

                                                                double darkness = 1 - (0.299 * Color.red(COLOR.getBodyTextColor()) + 0.587 * Color.green(COLOR.getBodyTextColor())
                                                                        + 0.114 * Color.blue(COLOR.getBodyTextColor()) / 255);
                                                                if (darkness < 0.5) {

                                                                    holder.button5.setTextColor(context.getResources().getColor(R.color.black));
                                                                } else {

                                                                    holder.button5.setTextColor(context.getResources().getColor(R.color.white));

                                                                }

                                                                Snackbar snackbar = Snackbar.make(v, holder.textView10.getText().toString() + " Unfollowed", Snackbar.LENGTH_SHORT);
                                                                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                                                                snackbar.setDuration(6000);
                                                                snackbar.setBackgroundTint(context.getResources().getColor(R.color.themetextcolor));
                                                                snackbar.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                                                                snackbar.show();
                                                                Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);

                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                                                                    vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
                                                                } else {

                                                                    vibrator.vibrate(300);
                                                                }

                                                                dialog.dismiss();

                                                            }

                                                        }
                                                    });
                                                }

                                            }
                                        });

                                    }
                                });


                            } else {


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

                                dialog.setCancelable(false);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.show();
                                List<String> NEWF = parseObject.getList("Following");
                                NEWF.add(strings.get(position).getObjectId());

                                parseObject.put("Following", NEWF);
                                parseObject.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {

                                        ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("roundtable_user_data");
                                        parseObjectParseQuery.whereEqualTo("User_ID", strings.get(position).getObjectId());
                                        parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                                            @Override
                                            public void done(List<ParseObject> objects, ParseException e) {

                                                for (ParseObject parseObject1 : objects) {

                                                    List<String> NEWF = parseObject1.getList("Followers");
                                                    NEWF.add(ParseUser.getCurrentUser().getObjectId());

                                                    List<String> NFT = parseObject1.getList("NOTIFY");
                                                    NFT.add(ParseUser.getCurrentUser().getObjectId());

                                                    parseObject1.put("Followers", NEWF);
                                                    parseObject1.put("NOTIFY", NFT);
                                                    parseObject1.saveInBackground(new SaveCallback() {
                                                        @Override
                                                        public void done(ParseException e) {

                                                            if (e == null) {

                                                                ParseQuery<ParseInstallation> parseInstallationParseQuery = ParseInstallation.getQuery();
                                                                parseInstallationParseQuery.whereEqualTo("UserId", strings.get(position).getObjectId());
                                                                parseInstallationParseQuery.findInBackground(new FindCallback<ParseInstallation>() {
                                                                    @Override
                                                                    public void done(List<ParseInstallation> objects, ParseException e) {


                                                                        JSONObject data = new JSONObject();

                                                                        try {
                                                                            data.put("alert", ParseUser.getCurrentUser().getUsername() + " Follows You 🎉🎉");
                                                                            data.put("title", "Someone Follows");
                                                                            data.put("USER_ID", ParseUser.getCurrentUser().getObjectId());
                                                                            data.put("TYPE", Constant.SOME_ONE_FOLLOW);
                                                                        } catch (JSONException te) {
                                                                            throw new IllegalArgumentException("unexpected parsing error", te);
                                                                        }

                                                                        ParsePush push = new ParsePush();
                                                                        push.setQuery(parseInstallationParseQuery);
                                                                        push.setData(data);
                                                                        push.sendInBackground();


                                                                    }
                                                                });


                                                                dialog.dismiss();
                                                                Snackbar snackbar = Snackbar.make(v, holder.textView10.getText().toString() + " Followed", Snackbar.LENGTH_SHORT);
                                                                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                                                                snackbar.setDuration(6000);
                                                                snackbar.setBackgroundTint(context.getResources().getColor(R.color.themetextcolor));
                                                                snackbar.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                                                                snackbar.show();

                                                                Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);

                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                                                                    vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
                                                                } else {

                                                                    vibrator.vibrate(300);
                                                                }
                                                                holder.button5.setText(resources.getString(R.string.Following));
                                                                holder.button5.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.transparent)));
                                                                holder.button5.setStrokeWidth(2);
                                                                holder.button5.setTextColor(COLOR.getBodyTextColor());
                                                                holder.button5.setStrokeColor(ColorStateList.valueOf(COLOR.getBodyTextColor()));


                                                            }

                                                        }
                                                    });
                                                }

                                            }
                                        });

                                    }
                                });
                            }

                        }

                    }
                });


            }
        });


        holder.textView11.setText(strings.get(position).getString("First_and_last_name"));

        Glide.with(context.getApplicationContext()).asBitmap().load(strings.get(position).getParseFile("speaker_profile_pic").getUrl()).circleCrop()
                .placeholder(R.drawable.main_placeholder).thumbnail(0.3f)
                .into(holder.circleImageView5);

        holder.button5.setStrokeColor(ColorStateList.valueOf(COLOR.getBodyTextColor()));
        holder.button5.setTextColor(COLOR.getBodyTextColor());
        holder.textView10.setTextColor(COLOR.getBodyTextColor());
        holder.textView11.setTextColor(COLOR.getBodyTextColor());

    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView11, textView10, des;
        CircleImageView circleImageView5;
        ConstraintLayout clickable_part;
        MaterialButton button5;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView10 = itemView.findViewById(R.id.textView10);
            textView11 = itemView.findViewById(R.id.textView11);
            clickable_part = itemView.findViewById(R.id.clickable_part);
            des = itemView.findViewById(R.id.info);
            button5 = itemView.findViewById(R.id.button5);
            circleImageView5 = itemView.findViewById(R.id.circleImageView5);
        }
    }
}
