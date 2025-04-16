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

public class language_adapter extends RecyclerView.Adapter<language_adapter.ViewHolder>  {

    Context context;
    List<other_language_modal_class> other_language_modal_classes;

    public language_adapter(Context context, List<other_language_modal_class> other_language_modal_classes) {
        this.context = context;
        this.other_language_modal_classes = other_language_modal_classes;
    }

    @NonNull
    @Override



    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

              view= LayoutInflater.from(parent.getContext()).inflate(R.layout.language_selector_in_main_page,parent,false);
              return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.display.setText(other_language_modal_classes.get(position).getDISPLAY());
        PushDownAnim.setPushDownAnimTo(holder.clickable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, particular_language.class);
                intent.putExtra("Name", other_language_modal_classes.get(position).getDISPLAY());
                context.startActivity(intent);
                ((Activity)context).overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
            }
        });

    }

    @Override
    public int getItemCount() {
        return other_language_modal_classes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView display;
        LinearLayout clickable;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            display=itemView.findViewById(R.id.display);
            clickable=itemView.findViewById(R.id.clickable);

        }
    }


}
