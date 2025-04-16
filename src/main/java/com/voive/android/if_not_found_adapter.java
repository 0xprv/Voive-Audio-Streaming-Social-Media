package com.voive.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class if_not_found_adapter extends RecyclerView.Adapter<if_not_found_adapter.ViewHolder>  {

    Context context;
    String title;
    String des;
    int DRAWABLE;

    public if_not_found_adapter(Context context, String title, String des, int DRAWABLE) {
        this.context = context;
        this.title = title;
        this.des = des;
        this.DRAWABLE = DRAWABLE;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.if_something_not_found_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.textView.setText(title);

        holder.DESCR.setText(des);



    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView textView,DESCR;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            textView=itemView.findViewById(R.id.title);
            DESCR=itemView.findViewById(R.id.description);



        }
    }



}
