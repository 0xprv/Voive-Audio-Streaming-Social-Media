package com.voive.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.parse.ParseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class suggested_people_in_profile_adapter extends RecyclerView.Adapter<suggested_people_in_profile_adapter.ViewHolder> {

    List<ParseUser> parseUsers;
    Context context;

    public suggested_people_in_profile_adapter(List<ParseUser> parseUsers, Context context) {
        this.parseUsers = parseUsers;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggested_peoples_in_profile, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng = sharedPreferences.getString("lng", "en");
        Context language_context = LocaleHelper.setLocale(context, lng);
        Resources resources = language_context.getResources();

        Glide.with(context.getApplicationContext()).asBitmap().load(parseUsers.get(position).getParseFile("speaker_profile_pic").getUrl()).centerInside().into(holder.circle_image);

        holder.user_name.setText(parseUsers.get(position).getUsername());

        holder.first_last.setText(parseUsers.get(position).getString("First_and_last_name"));

    }

    @Override
    public int getItemCount() {
        return parseUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circle_image;
        MaterialButton materialButton;
        TextView user_name, first_last;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circle_image = itemView.findViewById(R.id.circle_image);
            first_last = itemView.findViewById(R.id.first_last_name);
            user_name = itemView.findViewById(R.id.followers);
            materialButton = itemView.findViewById(R.id.button5);
        }
    }
}
