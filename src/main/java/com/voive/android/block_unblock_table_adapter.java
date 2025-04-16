package com.voive.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.button.MaterialButton;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class block_unblock_table_adapter extends RecyclerView.Adapter<block_unblock_table_adapter.ViewHolder> {

    List<ParseObject> strings;
    Context context;

    public block_unblock_table_adapter(List<ParseObject> strings, Context context) {
        this.strings = strings;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.block_unblock_table_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {



        holder.textView5.setText(strings.get(position).getString("table_name"));
        Glide.with(context.getApplicationContext()).asBitmap().load(strings.get(position).getParseFile("table_image").getUrl())
                .centerInside().into(holder.circleImageView);


        Glide.with(context.getApplicationContext()).asBitmap().load(strings.get(position).getParseFile("table_image")
                .getUrl()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                Palette.generateAsync(resource, new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(@Nullable Palette palette) {

                        Palette.Swatch swatch=palette.getDominantSwatch();
                        if(swatch!=null){

                            holder.textView5.setTextColor(swatch.getRgb());

                        }
                        else {

                            holder.textView5.setTextColor(context.getResources().getColor(R.color.themetextcolor));
                        }

                    }
                });

            }
        });

        PushDownAnim.setPushDownAnimTo(holder.UNBAN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                List<String> stringss=ParseUser.getCurrentUser().getList("BLOCKED_TABLE");
                stringss.remove(strings.get(position).getObjectId());


                ParseUser.getCurrentUser().put("BLOCKED_TABLE",stringss);
                ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        if(e==null){
                            Toast.makeText(context, "Table Unban 😏", Toast.LENGTH_SHORT).show();
                            REMOVE(position);

                        }

                    }
                });

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

    public void REMOVE(int position){

        strings.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position,strings.size());

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView textView5;
        MaterialButton UNBAN;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.cover_image);
            textView5=itemView.findViewById(R.id.table_name);
            UNBAN=itemView.findViewById(R.id.reject_invitation);

        }
    }
}
