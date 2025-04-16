package com.voive.android;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
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
import com.parse.ParseObject;
import com.tapadoo.alerter.Alerter;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class full_width_table_adapter_color extends RecyclerView.Adapter<full_width_table_adapter_color.ViewHolder> {
    Context context;
    List<ParseObject> parseObjects;
    Palette.Swatch COLOR;


    public full_width_table_adapter_color(Context context, List<ParseObject> parseObjects, Palette.Swatch COLOR) {
        this.context = context;
        this.parseObjects = parseObjects;
        this.COLOR = COLOR;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.full_width_table_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng = sharedPreferences.getString("lng", "en");
        Context language_context = LocaleHelper.setLocale(context, lng);
        Resources resources = language_context.getResources();


        Glide.with(context).load(parseObjects.get(position).getParseFile("table_image").getUrl()).centerCrop().into(holder.circleImageView);

        holder.title.setText(parseObjects.get(position).getString("TALKING_TITLE"));

        holder.creator.setText(parseObjects.get(position).getString("table_name"));

        holder.SUBS.setText(Integer.toString(parseObjects.get(position).getList("table_subscribers").size()) + " " + resources.getString(R.string.Subscribers));


        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        double darkness = 1 - (0.299 * Color.red(COLOR.getRgb()) + 0.587 * Color.green(COLOR.getRgb()) + 0.114 * Color.blue(COLOR.getRgb())) / 255;
        if (darkness < 0.5) {

            holder.creator.setTextColor(context.getResources().getColor(android.R.color.black));


        } else {
            holder.creator.setTextColor(context.getResources().getColor(android.R.color.white));

        }


        //  holder.creator.setTextColor(COLOR.getTitleTextColor());
        holder.SUBS.setTextColor(COLOR.getBodyTextColor());
        holder.title.setTextColor(COLOR.getBodyTextColor());
        holder.dot.setIconTint(ColorStateList.valueOf(COLOR.getBodyTextColor()));


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

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ConstraintLayout constraintLayout;
        TextView title, creator, SUBS;
        CircleImageView circleImageView;
        MaterialButton dot;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            constraintLayout = itemView.findViewById(R.id.clickable_part);
            circleImageView = itemView.findViewById(R.id.avtar);
            dot = itemView.findViewById(R.id.dot);
            title = itemView.findViewById(R.id.textView32);
            SUBS = itemView.findViewById(R.id.total_subscriber);
            creator = itemView.findViewById(R.id.textView33);
        }
    }
}
