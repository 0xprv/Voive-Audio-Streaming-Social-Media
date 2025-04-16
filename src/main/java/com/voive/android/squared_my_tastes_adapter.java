package com.voive.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;

import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class squared_my_tastes_adapter extends RecyclerView.Adapter<squared_my_tastes_adapter.ViewHolder>  {


    Context context;
    List<category_modal> stringList;
    RequestQueue requestQueue;
    public squared_my_tastes_adapter(Context context, List<category_modal> stringList) {
        this.context = context;
        this.stringList = stringList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.squared_particular,parent,false);
     return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SharedPreferences languagepref = context.getSharedPreferences("LANG", MODE_PRIVATE);
        String languagecode = languagepref.getString("lng", "en");
        Context contextT = LocaleHelper.setLocale(context, languagecode);
        Resources resources = contextT.getResources();


        SpannableString spannableString=new SpannableString(stringList.get(position).getConverted());

        spannableString.setSpan(new UnderlineSpan(),0,spannableString.length(),0);
       // holder.textView9.setText(spannableString);

        holder.textView9.setText(stringList.get(position).getConverted());


       // holder.fav.setVisibility(ParseUser.getCurrentUser().getList("Tastes_list").contains(stringList.get(position))
        //?View.VISIBLE : View.GONE);

        PushDownAnim.setPushDownAnimTo(holder.linearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,when_taste_clicked_activity.class);
                intent.putExtra("NAME",stringList.get(position).getConverted());
                intent.putExtra("ID",stringList.get(position).getOringnal_english());
                context.startActivity(intent);
                ((Activity)context).overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
            }
        });


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView9;
        RelativeLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            textView9=itemView.findViewById(R.id.textView9);
            linearLayout=itemView.findViewById(R.id.linear);


        }
    }


    public void remove(int pos){

        stringList.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeRemoved(pos,stringList.size());

    }

}
