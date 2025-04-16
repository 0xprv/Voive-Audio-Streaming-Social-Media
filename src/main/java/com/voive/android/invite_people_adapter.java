package com.voive.android;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseUser;

import net.igenius.customcheckbox.CustomCheckBox;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class invite_people_adapter extends RecyclerView.Adapter<invite_people_adapter.ViewHolder> {


    List<ParseUser> objects;
    Context context;
    String TABLE_ID;
    Resources resources;

    public invite_people_adapter(List<ParseUser> objects, Context context, String TABLE_ID) {
        this.objects = objects;
        this.context = context;
        this.TABLE_ID = TABLE_ID;
    }

    @NonNull
    @Override


    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invite_people_layout, parent, false);

        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        Glide.with(context).asBitmap().load(objects.get(position).
                        getParseFile("speaker_profile_pic")
                        .getUrl()).thumbnail(0.3f).
                centerInside().into(holder.circleImageView5);

        holder.textView11.setText(objects.get(position).getString("First_and_last_name"));

        holder.clockable_part.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.customCheckBox.toggle();
            }
        });
        holder.customCheckBox.setOnCheckedChangeListener(new CustomCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CustomCheckBox checkBox, boolean isChecked) {

                if (isChecked) {
                    if (invite_people_activity.ss.size() > 100) {
                        holder.customCheckBox.setChecked(false);
                        Toast.makeText(context, resources.getString(R.string.invitation_limit), Toast.LENGTH_SHORT).show();
                    } else {
                        invite_people_activity.ss.add(objects.get(position).getObjectId());
                    }

                } else {
                    invite_people_activity.ss.remove(objects.get(position).getObjectId());
                }

            }
        });


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
        TextView textView11;
        ConstraintLayout clockable_part;
        CustomCheckBox customCheckBox;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            circleImageView5 = itemView.findViewById(R.id.circleImageView5);
            textView11 = itemView.findViewById(R.id.textView11);
            customCheckBox = itemView.findViewById(R.id.checkbox);
            clockable_part = itemView.findViewById(R.id.clockable_part);


        }
    }


}
