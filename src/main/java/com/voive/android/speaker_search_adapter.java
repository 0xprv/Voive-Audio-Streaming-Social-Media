package com.voive.android;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.tapadoo.alerter.Alerter;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

class speaker_search_adapter extends RecyclerView.Adapter<speaker_search_adapter.ViewHolder> {

    List<ParseUser> parseObjects;
    Context context;

    public speaker_search_adapter( List<ParseUser> parseObjects, Context context) {
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

                ACProgressFlower dialog = new ACProgressFlower.Builder(context)
                        .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                        .themeColor(Color.WHITE)
                        .bgColor(R.color.card_view_color)
                        .fadeColor(R.color.card_view_color)
                        .bgAlpha(0)
                        .petalThickness(6)
                        .petalCount(20)
                        .speed(25)
                        .fadeColor(Color.DKGRAY).build();
                dialog.show();
                ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("backroom_talk");
                parseObjectParseQuery.whereEqualTo("involver", ParseUser.getCurrentUser().getObjectId());
                parseObjectParseQuery.whereEqualTo("involver", parseObjects.get(position).getObjectId());
              parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                  @Override
                  public void done(List<ParseObject> objects, ParseException e) {


                          if(objects.size()>0){


                             // Toast.makeText(context, "OLD", Toast.LENGTH_SHORT).show();

                          }
                          else {

                           //   Toast.makeText(context, "NEW", Toast.LENGTH_SHORT).show();

                              List<String> inv=new ArrayList<>();
                              inv.add(ParseUser.getCurrentUser().getObjectId());
                              inv.add(parseObjects.get(position).getObjectId());
                              ParseObject parseObject=new ParseObject("backroom_talk");
                              parseObject.put("involver",inv);

                              parseObject.saveInBackground(new SaveCallback() {
                                  @Override
                                  public void done(ParseException e) {


                                      if(e==null){


                                          ParseQuery<ParseObject> ppll=new ParseQuery<ParseObject>("roundtable_user_data");
                                          ppll.whereEqualTo("User_ID",ParseUser.getCurrentUser().getObjectId());
                                          ppll.findInBackground(new FindCallback<ParseObject>() {
                                              @Override
                                              public void done(List<ParseObject> objects, ParseException e) {


                                                  for(ParseObject ppx:objects){
                                                      List<String> talks=ppx.getList("talk_list");
                                                      talks.add(parseObjects.get(position).getObjectId());

                                                      ppx.put("talk_list",talks);
                                                      ppx.saveInBackground(new SaveCallback() {
                                                          @Override
                                                          public void done(ParseException e) {
                                                              ParseQuery<ParseObject> ppll_2=new ParseQuery<ParseObject>("roundtable_user_data");
                                                              ppll_2.whereEqualTo("User_ID",parseObjects.get(position).getObjectId());
                                                              ppll_2.findInBackground(new FindCallback<ParseObject>() {
                                                                  @Override
                                                                  public void done(List<ParseObject> objects, ParseException e) {


                                                                      for(ParseObject ppx:objects){
                                                                          List<String> talks=ppx.getList("talk_list");
                                                                          talks.add(ParseUser.getCurrentUser().getObjectId());

                                                                          ppx.put("talk_list",talks);
                                                                          ppx.saveInBackground(new SaveCallback() {
                                                                              @Override
                                                                              public void done(ParseException e) {




                                                                              }
                                                                          });

                                                                      }



                                                                  }
                                                              });

                                                          }
                                                      });

                                                  }


                                              }
                                          });



                                          SharedPreferences sharedPreferences = context.getSharedPreferences("LANG", MODE_PRIVATE);
                                          String lng = sharedPreferences.getString("lng", "en");
                                          Context contextft = LocaleHelper.setLocale( context, lng);
                                          Resources resources = contextft.getResources();
                                          dialog.dismiss();
                                          Alerter.create((Activity) context)
                                                  .setTitle("🎉 "+resources.getString(R.string.Congo_ratu))
                                                  .setText(resources.getString(R.string.a_new_baccrom_talk_created))
                                                  .setBackgroundColorInt(context.getResources().getColor(android.R.color.holo_green_dark))
                                                  .hideIcon()
                                                  .enableSwipeToDismiss()
                                                  .setDuration(5000)
                                                  .show();

                                      }
                                      else {

                                          Alerter.create((Activity) context)
                                                  .setTitle(e.getMessage())
                                                  .setBackgroundColorInt(context.getResources().getColor(android.R.color.holo_red_light))
                                                  .hideIcon()
                                                  .enableSwipeToDismiss()
                                                  .setDuration(5000)
                                                  .show();

                                      }

                                  }
                              });

                          }


                  }
              });


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
