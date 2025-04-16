package com.voive.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.parse.SaveCallback;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ban_unban_people_adapter extends RecyclerView.Adapter<ban_unban_people_adapter.ViewHolder> {

    List<ParseUser> strings;
    Context context;
    String ID;

    public ban_unban_people_adapter(List<ParseUser> strings, Context context, String ID) {
        this.strings = strings;
        this.context = context;
        this.ID = ID;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.ban_unban_people,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SharedPreferences sharedPreferences=context.getSharedPreferences("LANG",Context.MODE_PRIVATE);
        String lng=sharedPreferences.getString("lng","en");
        Context language_context= LocaleHelper.setLocale(context,lng);
        Resources resources=language_context.getResources();

        holder.textView5.setText(strings.get(position).getUsername());
        Glide.with(context.getApplicationContext()).asBitmap().load(strings.get(position).getParseFile("speaker_profile_pic").getUrl()).centerInside().into(holder.circleImageView);



        holder.UNBAN.setText(resources.getString(R.string.UNBANN));

        PushDownAnim.setPushDownAnimTo(holder.constraintLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context,my_own_account.class);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.slide_from_start,R.anim.stationary);



            }
        });

        PushDownAnim.setPushDownAnimTo(holder.UNBAN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseQuery<ParseObject> GETTED=new ParseQuery<ParseObject>("live_tables");
                GETTED.getInBackground(ID, new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {

                        List<String> BANS=object.getList("BANS");
                        BANS.remove(strings.get(position).getObjectId());

                        object.put("BANS",BANS);
                        object.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {



                                REMOVE(position);
                                Toast.makeText(context, "Speaker Unbanned", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                });

            }
        });



    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return strings.size();

    }

    public void REMOVE(int position){

        strings.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position,strings.size());

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView textView5;
        ConstraintLayout constraintLayout;
        ImageView status;
        MaterialButton UNBAN;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.circleImageView5);
            textView5=itemView.findViewById(R.id.textView10);
            UNBAN=itemView.findViewById(R.id.materialButton8);
            status=itemView.findViewById(R.id.status);
            constraintLayout=itemView.findViewById(R.id.clickable_part);
        }
    }
}
