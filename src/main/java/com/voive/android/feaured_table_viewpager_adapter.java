package com.voive.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class feaured_table_viewpager_adapter extends RecyclerView.Adapter<feaured_table_viewpager_adapter.ViewHolder> {

    List<ParseObject> parseObjects;
    Context context;

    public feaured_table_viewpager_adapter(List<ParseObject> parseObjects, Context context) {
        this.parseObjects = parseObjects;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.current_active_table_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context.getApplicationContext()).load(parseObjects.get(position).getParseFile("table_image").getUrl()).centerInside().placeholder(R.drawable.theme_placeholder)
                .into(holder.circleImageView4);

        holder.textView9.setText(parseObjects.get(position).getString("table_name"));

        if(position==2){

            holder.view.setVisibility(View.INVISIBLE);

        }
    }

    @Override
    public int getItemCount() {
        return parseObjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView4;
        TextView textView9;
        View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView9=itemView.findViewById(R.id.textView9);
        }
    }
}
