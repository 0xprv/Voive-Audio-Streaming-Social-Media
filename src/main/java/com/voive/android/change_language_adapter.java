package com.voive.android;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.processphoenix.ProcessPhoenix;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


import static android.content.Context.MODE_PRIVATE;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class change_language_adapter extends RecyclerView.Adapter<change_language_adapter.demoholder> {



    Context context;
    List<recommanded_language_modal_class> strs;

    public change_language_adapter(Context context, List<recommanded_language_modal_class> strs) {
        this.context = context;
        this.strs = strs;
    }

    @NonNull
    @Override
    public demoholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.language_selector_layout, parent, false);

        return new demoholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull demoholder holder, int position) {

        holder.mainlanguage.setText(strs.get(position).getName());
        holder.accentLanguage.setText(strs.get(position).getTranditional());
        holder.textView30.setText(strs.get(position).getCode());

    }

    @Override
    public int getItemCount() {
        return strs.size();
    }



    public class demoholder extends RecyclerView.ViewHolder {

        TextView mainlanguage;
        TextView accentLanguage;
        ImageView imageView9;
        CardView radioButton;
        TextView textView30;

        public demoholder(View itemView) {
            super(itemView);
            mainlanguage = itemView.findViewById(R.id.mainlanguage);
            accentLanguage = itemView.findViewById(R.id.accent_language);
            radioButton=itemView.findViewById(R.id.cardview);
            textView30=itemView.findViewById(R.id.textView30);

            PushDownAnim.setPushDownAnimTo(radioButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences languagepref=context.getSharedPreferences("LANG",MODE_PRIVATE);
                    SharedPreferences.Editor editor = languagepref.edit();
                    editor.putString("lng", textView30.getText().toString());
                    editor.apply();

                    ACProgressFlower dialog = new ACProgressFlower.Builder(change_language.activity)
                            .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                            .themeColor(Color.WHITE)
                            .bgColor(R.color.card_view_color)
                            .fadeColor(R.color.card_view_color)
                            .bgAlpha(0)
                            .petalThickness(6)
                            .petalCount(20)
                            .speed(25)
                            .fadeColor(Color.DKGRAY).build();

                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    new CountDownTimer(3000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {


                        }

                        @Override
                        public void onFinish() {
                            dialog.dismiss();
                            ProcessPhoenix.triggerRebirth(context.getApplicationContext());
                        }
                    }.start();

                }
            });


        }
    }
}
