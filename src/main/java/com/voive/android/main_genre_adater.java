package com.voive.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class main_genre_adater extends RecyclerView.Adapter<main_genre_adater.ViewHolder> {
    Context context;
    List<String> strings;

    public main_genre_adater(Context context, List<String> strings) {
        this.context = context;
        this.strings = strings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.tasteheaderlayout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.textView.setText(strings.get(position));
        ParseQuery<ParseObject> parseObjectParseQuery=new ParseQuery<ParseObject>("live_tables");

        parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                holder.container_recyler.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
                holder.container_recyler.setAdapter(new suggested_table_adapter_small(context,objects));
                holder.container_recyler.setHasFixedSize(true);

                holder.container_recyler.setNestedScrollingEnabled(false);
                holder.shimmerFrameLayout.stopShimmer();
                holder.shimmerFrameLayout.setVisibility(View.GONE);



            }
        });


    }

    @Override
    public int getItemCount() {
        return strings.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


       TextView textView;
       ShimmerFrameLayout shimmerFrameLayout;
       RecyclerView container_recyler;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container_recyler=itemView.findViewById(R.id.container_recyler);
            textView=itemView.findViewById(R.id.suggested_textview);
            shimmerFrameLayout=itemView.findViewById(R.id.shimmer);
        }
    }
}
