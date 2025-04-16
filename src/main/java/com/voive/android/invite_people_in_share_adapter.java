package com.voive.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.button.MaterialButton;
import com.parse.ParseUser;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import de.hdodenhof.circleimageview.CircleImageView;

public class invite_people_in_share_adapter extends RecyclerView.Adapter<invite_people_in_share_adapter.ViewHolder>  {


    List<ParseUser> objects;
    Context context;
    String TABLE_ID;


    public invite_people_in_share_adapter(List<ParseUser> objects, Context context, String TABLE_ID) {
        this.objects = objects;
        this.context = context;
        this.TABLE_ID = TABLE_ID;
    }

    @NonNull
    @Override



    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
                view= LayoutInflater.from(parent.getContext()).inflate(R.layout.share_table_to_people_item_layout,parent,false);

                return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView5;
        ConstraintLayout clockable_part;
        CircleImageView status;

        TextView textView10;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            circleImageView5=itemView.findViewById(R.id.circleImageView5);
            status=itemView.findViewById(R.id.status);
            clockable_part=itemView.findViewById(R.id.clockable_part);


            textView10=itemView.findViewById(R.id.textView10);
        }
    }


}
