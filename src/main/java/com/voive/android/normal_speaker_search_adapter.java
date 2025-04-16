package com.voive.android;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseUser;
import com.thekhaeng.pushdownanim.PushDownAnim;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

class normal_speaker_search_adapter extends RecyclerView.Adapter<normal_speaker_search_adapter.ViewHolder> {

    List<ParseUser> parseObjects;
    Context context;

    public normal_speaker_search_adapter(List<ParseUser> parseObjects, Context context) {
        this.parseObjects = parseObjects;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_speakers, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        Glide.with(context).asBitmap().load(parseObjects.get(position).getParseFile("speaker_profile_pic").getUrl()).centerInside().into(holder.circleImageView5);

        holder.textView10.setText(parseObjects.get(position).getUsername());

        holder.textView11.setText(parseObjects.get(position).getString("First_and_last_name"));


        PushDownAnim.setPushDownAnimTo(holder.clockable_part).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(parseObjects.get(position).getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){

                    Intent intent=new Intent(context,my_own_account.class);
                    context.startActivity(intent);

                }
                else {

                    Intent intent=new Intent(context,other_speaker_account.class);
                    intent.putExtra("ID",parseObjects.get(position).getObjectId());
                    context.startActivity(intent);
                }



            }
        });


    }

    @Override
    public int getItemCount() {
        return parseObjects.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView5;
        TextView textView11;
        CardView clockable_part;


        TextView textView10;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView5=itemView.findViewById(R.id.circleImageView8);
            textView11=itemView.findViewById(R.id.mainlanguage);

            clockable_part=itemView.findViewById(R.id.cardview);


            textView10=itemView.findViewById(R.id.accent_language);

        }
    }
}
