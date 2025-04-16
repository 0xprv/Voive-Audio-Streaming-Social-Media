package com.voive.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class live_table_creators_adapter extends RecyclerView.Adapter<live_table_creators_adapter.ViewHolder> {

    List<ParseUser> strings;
    Context context;
    String table_id;

    public live_table_creators_adapter(List<ParseUser> strings, Context context, String table_id) {
        this.strings = strings;
        this.context = context;
        this.table_id = table_id;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ceators_of_table_item_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        try {


            if (strings.get(position).getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {

                holder.first_last.setText("You");

            } else {

                holder.first_last.setText(strings.get(position).getString("First_and_last_name"));
            }
            Glide.with(context).asBitmap().load(strings.get(position).getParseFile("speaker_profile_pic").getUrl())
                    .thumbnail(0.3f).centerInside().into(holder.circleImageView);
        } catch (Exception vf) {

            vf.printStackTrace();
        }

        PushDownAnim.setPushDownAnimTo(holder.cardView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (strings.get(position).getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {

                    Intent intent = new Intent(context, my_own_account.class);
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.slide_from_start, R.anim.stationary);


                } else {

                    Intent intent = new Intent(context, other_speaker_account.class);
                    intent.putExtra("ID", strings.get(position).getObjectId());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.slide_from_start, R.anim.stationary);


                }


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

    public void remove(int POSITION) {
        strings.remove(POSITION);
        notifyItemRemoved(POSITION);
        notifyItemRangeRemoved(POSITION, strings.size());

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView textView5, first_last;
        ConstraintLayout constraintLayout;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.circleImageView5);
            textView5 = itemView.findViewById(R.id.textView10);
            first_last = itemView.findViewById(R.id.textView11);
            cardView = itemView.findViewById(R.id.card);
            constraintLayout = itemView.findViewById(R.id.clickable_part);
        }
    }
}
