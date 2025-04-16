package com.voive.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.parse.ParseUser;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class all_member_creator__special_speaker_adapter_alert_dialog extends RecyclerView.Adapter<all_member_creator__special_speaker_adapter_alert_dialog.VireHolder> {

    List<ParseUser> parseObjects;
    Context context;
    String createor_id;
    String table_id;

    Handler handler = new Handler();
    Runnable runnable;


    public all_member_creator__special_speaker_adapter_alert_dialog(List<ParseUser> parseObjects, Context context, String createor_id, String table_id) {
        this.parseObjects = parseObjects;
        this.context = context;
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


        Toast.makeText(context, Integer.toString(parseObjects.size()), Toast.LENGTH_SHORT).show();


        Glide.with(context.getApplicationContext()).asBitmap().
                placeholder(context.getResources().getDrawable(R.drawable.theme_placeholder)).
                thumbnail(0.1f).load(parseObjects.get(holder.getAdapterPosition()).getParseFile("speaker_profile_pic").getUrl()).
                centerInside().into(holder.circleImageView);


        PushDownAnim.setPushDownAnimTo(holder.constraintLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (parseObjects.get(position).getObjectId() != ParseUser.getCurrentUser().getObjectId()) {


                    Intent intent = new Intent(context, other_speaker_account.class);
                    intent.putExtra("ID", parseObjects.get(position));
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.slide_from_start, R.anim.stationary);

                } else {


                    Intent intent = new Intent(context, my_own_account.class);
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.slide_from_start, R.anim.stationary);

                }


            }
        });


    }

    public void ref(List<ParseUser> newList) {

        DiffUtils diffUtils = new DiffUtils(parseObjects, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtils);

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

    public void remove(int POSITION) {
        parseObjects.remove(POSITION);
        notifyItemRemoved(POSITION);
        notifyItemRangeRemoved(POSITION, parseObjects.size());

    }

    public class VireHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        MaterialButton mic;
        //  MaterialButton moderator;
        ConstraintLayout constraintLayout;

        public VireHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.circle_image);
            constraintLayout = itemView.findViewById(R.id.clickable_part);
            //  moderator=itemView.findViewById(R.id.imageView6);
        }
    }
}
