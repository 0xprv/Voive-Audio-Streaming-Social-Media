package com.voive.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.List;

public class table_small_drawable_adapter extends RecyclerView.Adapter<table_small_drawable_adapter.ViewHolder> {
    Context context;
    List<String> parseObjects;

    public table_small_drawable_adapter(Context context, List<String> parseObjects) {
        this.context = context;
        this.parseObjects = parseObjects;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_taste_small_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.taste_name.setText(parseObjects.get(position));

        PushDownAnim.setPushDownAnimTo(holder.taste_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, when_taste_clicked_activity.class);
                intent.putExtra("taste_name", holder.taste_name.getText().toString());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.slide_from_end, R.anim.stationary);

            }
        });

    }

    @Override
    public int getItemCount() {

        return parseObjects.size();

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView taste_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            taste_name = itemView.findViewById(R.id.table_title);


        }
    }
}
