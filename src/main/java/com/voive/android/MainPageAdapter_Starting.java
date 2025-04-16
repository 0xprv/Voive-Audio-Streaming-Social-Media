package com.voive.android;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.sanojpunchihewa.glowbutton.GlowButton;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.w3c.dom.Text;

public class MainPageAdapter_Starting extends RecyclerView.Adapter<MainPageAdapter_Starting.ViewHolder>  {



    Context context;

    public MainPageAdapter_Starting(Context context) {
        this.context = context;
    }

    @NonNull
    @Override



    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

                view= LayoutInflater.from(parent.getContext()).inflate(R.layout.current_active_table_conatiner,parent,false);

                return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LANG", MODE_PRIVATE);
        String lng = sharedPreferences.getString("lng", "en");
        Context language_context = LocaleHelper.setLocale(context, lng);
        Resources resources = language_context.getResources();

        holder.textView48.setText(resources.getString(R.string.talk_anywhere_anoyone));
        PushDownAnim.setPushDownAnimTo(holder.i_have_invitation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context,main_explore_page_of_app.class);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.slide_from_end, R.anim.stationary);

            }
        });


    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        MaterialButton i_have_invitation;
        TextView textView14,textView48;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            i_have_invitation=itemView.findViewById(R.id.i_have_invitation);
            textView14=itemView.findViewById(R.id.textView14);
            textView48=itemView.findViewById(R.id.textView48);

        }
    }

}
