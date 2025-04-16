package com.voive.android;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class all_member_creator_adapter extends RecyclerView.Adapter<all_member_creator_adapter.VireHolder> {

    List<ParseUser> parseObjects;
    Context context;
    String createor_id;


    public all_member_creator_adapter(List<ParseUser> parseObjects, Context context, String createor_id) {
        this.parseObjects = parseObjects;
        this.context = context;
        this.createor_id = createor_id;

    }

    @NonNull
    @Override
    public VireHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.creator_member_item,parent,false);
        return new VireHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VireHolder holder, int position) {


        Toast.makeText(context, Integer.toString(parseObjects.size()), Toast.LENGTH_SHORT).show();

        if(parseObjects.get(position).equals(createor_id)){
//holder.moderator.setVisibility(View.VISIBLE);

        }
        else {

          //  holder.moderator.setVisibility(View.GONE);
        }

        Glide.with(context.getApplicationContext()).asBitmap().placeholder(context.getResources().getDrawable(R.drawable.theme_placeholder)).load(parseObjects.get(position).getParseFile("speaker_profile_pic").getUrl()).centerInside().into(holder.circleImageView);
        holder.first_last.setText(parseObjects.get(position).getUsername());


        try {


            ParseLiveQueryClient parseLiveQueryClient = null;

            try {
                parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI("https://mmkk.b4a.io/"));
            } catch (URISyntaxException et) {
                et.printStackTrace();
            }


            ParseQuery<ParseObject> parseUserParseQuery1 = new ParseQuery<ParseObject>("roundtable_user_data");
            parseUserParseQuery1.whereEqualTo("User_ID", parseObjects.get(position).getObjectId());

            SubscriptionHandling<ParseObject> subscriptionHandling = parseLiveQueryClient.subscribe(parseUserParseQuery1);


            subscriptionHandling.handleEvents(new SubscriptionHandling.HandleEventsCallback<ParseObject>() {
                @Override
                public void onEvents(ParseQuery<ParseObject> query, SubscriptionHandling.Event event, ParseObject objectp) {

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {


                            ParseQuery<ParseObject> parseObjectParseQuery1 = new ParseQuery<ParseObject>("roundtable_user_data");
                            parseObjectParseQuery1.whereEqualTo("User_ID", parseObjects.get(position).getObjectId());
                            parseObjectParseQuery1.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {


                                    for (ParseObject parseObject : objects) {

                                        if (parseObject.getBoolean("mic_muted")) {

                                            holder.mic.setIcon(context.getResources().getDrawable(R.drawable.mic_off));

                                        } else {
                                            holder.mic.setIcon(context.getResources().getDrawable(R.drawable.mic_on));
                                        }

                                    }

                                }
                            });


                        }
                    });


                }
            });

        }
        catch (Exception fg){

            fg.printStackTrace();
        }




       /*PushDownAnim.setPushDownAnimTo(holder.constraintLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context,other_speaker_account.class);
                intent.putExtra("ID",parseObjects);
                context.startActivity(intent);
                ((Activity)context).overridePendingTransition(R.anim.slide_from_end, R.anim.stationary);

            }
        });*/


    }

    public void ref(List<ParseUser> newList){

        DiffUtils diffUtils=new DiffUtils(parseObjects,newList);
        DiffUtil.DiffResult diffResult=DiffUtil.calculateDiff(diffUtils);

        this.parseObjects.clear();
        parseObjects.addAll(newList);
        diffResult.dispatchUpdatesTo(this);


    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return parseObjects.size();
    }

    public class VireHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView first_last;
        MaterialButton mic;
      //  MaterialButton moderator;
        ConstraintLayout constraintLayout;
        public VireHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.circle_image);
            first_last=itemView.findViewById(R.id.first_last_name);
            constraintLayout=itemView.findViewById(R.id.clickable_part);
          //  moderator=itemView.findViewById(R.id.imageView6);
        }
    }
    public static int darken_color(int color, float factor) {


        int alpha = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);

        return Color.argb(alpha, r, g, b);

    }
}
