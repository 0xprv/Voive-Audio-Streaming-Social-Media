package com.voive.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.parse.ParseUser;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class my_tastes_adapter extends RecyclerView.Adapter<my_tastes_adapter.ViewHolder>  {


    Context context;
    List<String> stringList;
    RequestQueue requestQueue;
    public my_tastes_adapter(Context context, List<String> stringList) {
        this.context = context;
        this.stringList = stringList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.particualr_taste_item_layout,parent,false);
     return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SharedPreferences languagepref = context.getSharedPreferences("LANG", MODE_PRIVATE);
        String languagecode = languagepref.getString("lng", "en");
        Context contextT = LocaleHelper.setLocale(context, languagecode);
        Resources resources = contextT.getResources();


        holder.textView9.setText(stringList.get(position));


        requestQueue= Volley.newRequestQueue(context);

        String URL="https://api.giphy.com/v1/gifs/search?api_key=hDsgK2BLgOHyNJ9RyR1MGzFl70Kt27Z6&limit=1&q="+holder.textView9.getText().toString();

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET
                , URL
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray=response.getJSONArray("data");
                    JSONObject jsonObject=jsonArray.getJSONObject(0);

                    Glide.with(context.getApplicationContext()).asGif().load("https://i.giphy.com/media/" + jsonObject.getString("id")+ "/giphy.gif").centerCrop().
                            thumbnail(0.3f).
                            placeholder(R.drawable.main_placeholder).
                            centerCrop().
                            apply(new RequestOptions().override(50, 50)).
                            into(holder.circleImageView);

                    Glide.with(context.getApplicationContext()).asGif().placeholder(R.drawable.main_placeholder).load("https://i.giphy.com/media/" + jsonObject.getString("id")+ "/giphy.gif")
                            .thumbnail(0.3f)
                            .apply(new RequestOptions().override(100, 100))
                           .centerCrop().into(holder.top_image);

                } catch (JSONException e) {

                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
        requestQueue.cancelAll(context);

        holder.fav.setVisibility(ParseUser.getCurrentUser().getList("Tastes_list").contains(stringList.get(position))
        ?View.VISIBLE : View.GONE);

        PushDownAnim.setPushDownAnimTo(holder.linearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,when_taste_clicked_activity.class);
                intent.putExtra("NAME",stringList.get(position));
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
        ImageView top_image,circleImageView,fav;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            textView9=itemView.findViewById(R.id.textView9);
            fav=itemView.findViewById(R.id.imageView5);
            linearLayout=itemView.findViewById(R.id.linear);
            circleImageView=itemView.findViewById(R.id.circleImageView12);
            top_image=itemView.findViewById(R.id.top_image);


        }
    }


    public void remove(int pos){

        stringList.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeRemoved(pos,stringList.size());

    }

}
