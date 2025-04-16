package com.voive.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.List;
import java.util.Random;

public class taste_and_genre_table_adapter extends RecyclerView.Adapter<taste_and_genre_table_adapter.ViewHolder> {
    Context context;
    List<String> strings;
    String id;

    public taste_and_genre_table_adapter(Context context, List<String> strings, String id) {
        this.context = context;
        this.strings = strings;
        this.id = id;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.tables_suggested_categories,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.textView.setText(strings.get(position));


        PushDownAnim.setPushDownAnimTo(holder.linear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





            }
        });


    }

    @Override
    public int getItemCount() {
        return strings.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


       TextView textView;
       LinearLayout linear;
     //  CardView cardView;
     //  RoundedFrameLayout roundedFrameLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

          //  roundedFrameLayout=itemView.findViewById(R.id.refresh_button_container);
            textView=itemView.findViewById(R.id.textView9);
            linear=itemView.findViewById(R.id.linear);
           // cardView=itemView.findViewById(R.id.cardview);
        }
    }
}
