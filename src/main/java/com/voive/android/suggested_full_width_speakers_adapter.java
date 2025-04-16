package com.voive.android;

import static android.content.Context.VIBRATOR_SERVICE;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
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

import net.igenius.customcheckbox.CustomCheckBox;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import de.hdodenhof.circleimageview.CircleImageView;

public class suggested_full_width_speakers_adapter extends RecyclerView.Adapter<suggested_full_width_speakers_adapter.ViewHolder> {
    Context context;
    List<ParseUser> strings;
    Activity activity;


    public suggested_full_width_speakers_adapter(Context context, List<ParseUser> strings, Activity activity) {
        this.context = context;
        this.strings = strings;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.full_width_speaker_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).asBitmap().load(strings.get(position).
                        getParseFile("speaker_profile_pic")
                        .getUrl()).thumbnail(0.3f).
                centerInside().into(holder.circleImageView5);

        holder.user.setText("@"+strings.get(position).getUsername());
        holder.textView11.setText(strings.get(position).getString("First_and_last_name"));
        if (!strings.get(position).getString("WHAT_LISTENING").trim().isEmpty()) {

            holder.what_listening_circular_imageview.setVisibility(View.VISIBLE);
            ParseQuery<ParseObject> pp = new ParseQuery<ParseObject>("live_tables");
            pp.getInBackground(strings.get(position).getString("WHAT_LISTENING"), new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {

                    if(e==null){

                        Glide.with(context.getApplicationContext()).asBitmap()
                                .load(object.getParseFile("table_image").getUrl()).centerCrop().into(holder.what_listening_circular_imageview);
                        Glide.with(context.getApplicationContext()).asBitmap()
                                .load(object.getParseFile("table_image").getUrl())
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                                        Palette.generateAsync(resource, new Palette.PaletteAsyncListener() {
                                            @Override
                                            public void onGenerated(@Nullable Palette palette) {

                                                Palette.Swatch swatch = palette.getLightVibrantSwatch() != null ? palette.getLightVibrantSwatch() : palette.getDominantSwatch();
                                                holder.circleImageView5.setBorderWidth(3);
                                                holder.circleImageView5.setBorderColor(swatch.getRgb());
                                                holder.textView11.setTextColor(swatch.getRgb());

                                            }
                                        });
                                    }
                                });

                    }

                }
            });

        }
        PushDownAnim.setPushDownAnimTo(holder.clockable_part).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(strings.get(position).getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){
                    Intent intent=new Intent(context,my_own_account.class);
                    context.startActivity(intent);
                    activity.overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
                }
                else {
                    Intent intent=new Intent(context,other_speaker_account.class);
                    intent.putExtra("ID",strings.get(position).getObjectId());
                    context.startActivity(intent);
                    activity.overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
                }


            }
        });



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

        CircleImageView circleImageView5,what_listening_circular_imageview;
        TextView textView11,user;
        ConstraintLayout clockable_part;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView5=itemView.findViewById(R.id.circleImageView5);
            textView11=itemView.findViewById(R.id.textView11);
            user=itemView.findViewById(R.id.user);
            what_listening_circular_imageview=itemView.findViewById(R.id.what_listening_circular_imageview);
            clockable_part=itemView.findViewById(R.id.clockable_part);
        }
    }
}
