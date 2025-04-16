package com.voive.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.List;


import static android.content.Context.MODE_PRIVATE;

public class change_language_adapter_starting extends RecyclerView.Adapter<change_language_adapter_starting.demoholder> {


    List<recommanded_language_modal_class> lang;
    Context context;

    public change_language_adapter_starting(List<recommanded_language_modal_class> lang, Context context) {
        this.lang = lang;
        this.context = context;
    }

    @NonNull
    @Override
    public demoholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.language_selector_layout, parent, false);

        return new demoholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull demoholder holder, int position) {
        holder.mainlanguage.setText(lang.get(position).getName());
        holder.accentLanguage.setText(lang.get(position).getTranditional());

        PushDownAnim.setPushDownAnimTo(holder.cardView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                SharedPreferences languagepref=context.getApplicationContext().getSharedPreferences("LANG",0);
                SharedPreferences.Editor editor = languagepref.edit();
                editor.putString("lng", lang.get(position).getCode());
                editor.apply();

                Intent intent=new Intent(context,phonenumberforotp.class);
                context.startActivity(intent);
                ((Activity)context).overridePendingTransition(R.anim.slide_from_end, R.anim.stationary);





            }
        });
    }

    @Override
    public int getItemCount() {
        return lang.size();
    }




    public class demoholder extends RecyclerView.ViewHolder {

        TextView mainlanguage;
        TextView accentLanguage;
        CardView cardView;
        TextView textView30;
        private Context context;

        public demoholder(View itemView) {
            super(itemView);
            mainlanguage = itemView.findViewById(R.id.mainlanguage);
            accentLanguage = itemView.findViewById(R.id.accent_language);
            cardView=itemView.findViewById(R.id.cardview);
            textView30=itemView.findViewById(R.id.textView30);



        }
    }
}
