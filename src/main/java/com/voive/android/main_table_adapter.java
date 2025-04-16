package com.voive.android;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.LocationManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class main_table_adapter extends RecyclerView.Adapter<main_table_adapter.ViewHolder> {
    Context context;
    List<String> strings;
    ParseGeoPoint parseGeoPointfinal;

    public main_table_adapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = null;

        switch (viewType) {


            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscriptions_tables_tables_container, parent, false);
                return new ViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggested_tables_container, parent, false);
                return new ViewHolder(view);
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.taste_and_genres_container, parent, false);
                return new ViewHolder(view);


        }
        throw new IllegalStateException("No_View_founded");

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        //getting languages
        SharedPreferences sharedPreferences = context.getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng = sharedPreferences.getString("lng", "hi");
        Context language_context = LocaleHelper.setLocale(context, lng);
        Resources resources = language_context.getResources();



        switch (holder.getItemViewType()) {


            case 0:
                holder.narby_textview.setText(resources.getString(R.string.Recent_Listened));
                PushDownAnim.setPushDownAnimTo(holder.anoclickable_part).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, recent_listened.class);
                        context.startActivity(intent);
                        ((Activity) context).overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
                    }
                });



                ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("live_tables");
                parseObjectParseQuery.whereContainedIn("objectId", ParseUser.getCurrentUser().getList("LAST_VISITED"));
                parseObjectParseQuery.setLimit(20);
                parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (objects.size() > 0) {

                            holder.anhow_many.setText(Integer.toString(objects.size())+" "+resources.getString(R.string.Recent_Listened));

                            SlideInUpAnimator animator = new SlideInUpAnimator(new OvershootInterpolator(1f));
                            holder.nearby_recyler.setItemAnimator(animator);
                            holder.nearby_recyler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                            holder.nearby_recyler.setAdapter(new last_visited_on_adapter(context, objects));
                            holder.nearby_recyler.setHasFixedSize(true);
                            holder.if_not.setVisibility(View.GONE);
                            holder.recyclerView.setNestedScrollingEnabled(false);
                            holder.shimmer_nearby.stopShimmer();
                            holder.shimmer_nearby.setVisibility(View.GONE);

                        } else {
                            holder.recyclerView.setVisibility(View.GONE);
                            holder.shimmer_nearby.stopShimmer();
                            holder.shimmer_nearby.setVisibility(View.GONE);
                            PushDownAnim.setPushDownAnimTo(holder.create_account).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(context, main_explore_page_of_app.class);
                                    context.startActivity(intent);
                                    ((Activity) context).overridePendingTransition(R.anim.slide_from_end, R.anim.stationary);

                                }
                            });

                          //  holder.anoclickable_part.setVisibility(View.GONE);
                            holder.create_account.setText(resources.getString(R.string.Find_conv));
                            holder.no_conv.setText(resources.getString(R.string.no_recent_conv));
                            holder.if_not.setVisibility(View.VISIBLE);
                            holder.shimmerFrameLayout1.stopShimmer();
                            holder.shimmerFrameLayout1.setVisibility(View.GONE);


                        }
                    }
                });

        break;

            case 1:
                holder.shimmerFrameLayout.setVisibility(View.VISIBLE);
                holder.shimmerFrameLayout.startShimmer();
                holder.suggested_textview.setText(resources.getString(R.string.Suggested_tables));
                PushDownAnim.setPushDownAnimTo(holder.clickable_part).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent intent = new Intent(context, main_explore_page_of_app.class);
                        context.startActivity(intent);
                        ((Activity) context).overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
                    }
                });

                ParseQuery<ParseObject> TT = new ParseQuery<ParseObject>("live_tables");
                TT.setLimit(20);
                TT.whereEqualTo("IS_PRIVATE",false);
                TT.addDescendingOrder("table_subscribers");
                TT.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                        holder.recyclerView.setAdapter(new suggested_table_adapter(context, objects));
                        holder.recyclerView.setHasFixedSize(true);

                        holder.recyclerView.setNestedScrollingEnabled(false);
                        holder.shimmerFrameLayout.stopShimmer();
                        holder.shimmerFrameLayout.setVisibility(View.GONE);


                    }
                });


                break;

            /*case 2:

                if (ParseUser.getCurrentUser().getParseGeoPoint("Location") != null) {

                    holder.narby_textview.setText(resources.getString(R.string.Nearby));

                    PushDownAnim.setPushDownAnimTo(holder.anoclickable_part).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
                            boolean gps_enabled = false;
                            boolean network_enabled = false;

                            try {
                                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                            } catch(Exception ex) {}

                            try {
                                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                            } catch(Exception ex) {}

                            if(!gps_enabled && !network_enabled) {


                                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.activity, R.style.AlertDialogCustom);
                                builder1.setMessage(resources.getString(R.string.GPS_NO));
                                builder1.setTitle("GPS :(");
                                builder1.setCancelable(false);

                                builder1.setPositiveButton(
                                        resources.getString(R.string.OK),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                            }
                                        });


                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                            }
                            else {
                                Intent intent = new Intent(context, Nearby_conversation_in_mao.class);
                                context.startActivity(intent);
                                ((Activity) context).overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
                            }


                        }
                    });
                    ParseQuery<ParseObject> parseObjectParseQuerynr = new ParseQuery<ParseObject>("live_tables");
                    parseObjectParseQuerynr.whereWithinKilometers("Location", ParseUser.getCurrentUser().getParseGeoPoint("Location"), 5.0);
                    parseObjectParseQuerynr.setLimit(20);
                    parseObjectParseQuerynr.whereEqualTo("IS_PRIVATE",false);
                    parseObjectParseQuerynr.addDescendingOrder("table_subscribers");
                    parseObjectParseQuerynr.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {

                            if (objects.size() > 0) {
                                holder.anhow_many.setText(Integer.toString(objects.size())+" "+resources.getString(R.string.CONVO));
                                holder.nearby_recyler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                                holder.nearby_recyler.setAdapter(new suggested_table_adapter(context, objects));
                                holder.nearby_recyler.setHasFixedSize(true);

                                holder.recyclerView.setNestedScrollingEnabled(false);
                                holder.shimmer_nearby.stopShimmer();
                                holder.shimmer_nearby.setVisibility(View.GONE);

                            } else {
                                holder.recyclerView.setVisibility(View.GONE);
                                holder.shimmer_nearby.stopShimmer();
                                holder.shimmer_nearby.setVisibility(View.GONE);
                                holder.nrby_container.getLayoutParams().height = 1;
                            }


                        }
                    });

                } else {
                    holder.nrby_container.getLayoutParams().height = 1;
                }


                break;*/

            case 2:



                holder.taste_genre_textview.setText(resources.getString(R.string.Categories));
                  holder.des_taste.setText(resources.getString(R.string.get_cat_conv));


                List<category_modal> category_modals = new ArrayList<>();

                category_modal category_moda7 = new category_modal(resources.getString(R.string.News), "News");
                category_modals.add(category_moda7);

                category_modal category_modal5 = new category_modal(resources.getString(R.string.Unions), "Unions");
                category_modals.add(category_modal5);
                category_modal category_modal2 = new category_modal(resources.getString(R.string.Panchayat), "Panchayats");
                category_modals.add(category_modal2);
                category_modal category_modal3 = new category_modal(resources.getString(R.string.Devotional), "Devotional");
                category_modals.add(category_modal3);
                category_modal category_modal = new category_modal(resources.getString(R.string.arts_enter), "Arts & Entertainment");
                category_modals.add(category_modal);
                category_modal category_moda3 = new category_modal(resources.getString(R.string.health_beauty), "Health & Beauty");
                category_modals.add(category_moda3);
                category_modal category_moda4 = new category_modal(resources.getString(R.string.sci_tech), "Science & Tech");
                category_modals.add(category_moda4);
                category_modal category_moda5 = new category_modal(resources.getString(R.string.Politics), "Politics");
                category_modals.add(category_moda5);
                category_modal category_moda6 = new category_modal(resources.getString(R.string.Buisness), "Business");
                category_modals.add(category_moda6);
                category_modal category_modal4 = new category_modal(resources.getString(R.string.gossips), "Domestic Conversations");
                category_modals.add(category_modal4);
                category_modal category_moda9 = new category_modal(resources.getString(R.string.Books_Information), "Books & Information");
                category_modals.add(category_moda9);

                holder.taste_genre_recylerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                holder.taste_genre_recylerview.setNestedScrollingEnabled(false);
                holder.taste_genre_recylerview.setAdapter(new squared_my_tastes_adapter(context, category_modals));

                break;
           /* case 4:

                holder.according_to_lang.setText(resources.getString(R.string.NSFW_CONV));
                String finaltext = resources.getString(R.string.get_conv_in7_lng, ParseUser.getCurrentUser().getString("PREF"));
                holder.how_many.setText(finaltext);


                ParseQuery<ParseObject> parseObjectParseQueryL = new ParseQuery<ParseObject>("live_tables");
                parseObjectParseQueryL.setLimit(20);
                parseObjectParseQueryL.whereEqualTo("Language", ParseUser.getCurrentUser().getString("PREF"));
                parseObjectParseQueryL.whereEqualTo("IS_PRIVATE",false);
                parseObjectParseQueryL.addDescendingOrder("table_subscribers");
                parseObjectParseQueryL.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        holder.language_recyler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                        holder.language_recyler.setAdapter(new suggested_table_adapter(context, objects));
                        holder.language_recyler.setHasFixedSize(true);

                        holder.language_recyler.setNestedScrollingEnabled(false);
                        holder.shimmerL.stopShimmer();
                        holder.shimmerL.setVisibility(View.GONE);


                    }
                });


                break;*/

        }


    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {


            return 0;
        } else if (position == 1) {

            return 1;
        } else  {

            return 2;

        }

    }

    @Override
    public int getItemCount() {
        return 3;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        RecyclerView subscribed_container_recyler;
        ShimmerFrameLayout shimmerFrameLayout1;
        LinearLayout main_container;
        MaterialButton clear_all;
        TextView subscribed_textview;

        // Position 1 items
        TextView suggested_textview;
        RecyclerView recyclerView;
        ShimmerFrameLayout shimmerFrameLayout;

        //Position 2 items
        TextView taste_genre_textview, des_taste;
        RecyclerView taste_genre_recylerview;
        LinearLayout whole_click, nrby_container;
        MaterialButton see_all;

        //position 3 items
        TextView according_to_lang, lng_not_a_barrier, narby_textview, how_many,anhow_many,no_conv;
        RecyclerView language_recyler, nearby_recyler;

        LinearLayout clickable_part;
        LinearLayout anoclickable_part,if_not;
        ShimmerFrameLayout shimmerL;


        MaterialButton create_account;

        ShimmerFrameLayout shimmer_nearby;
        RelativeLayout recent_click;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Position 0 findview
            subscribed_container_recyler = itemView.findViewById(R.id.subscribed_container_recyler);
            shimmerFrameLayout1 = itemView.findViewById(R.id.shimmer);
            main_container = itemView.findViewById(R.id.main_container);
            narby_textview = itemView.findViewById(R.id.suggested_textview);
            subscribed_textview = itemView.findViewById(R.id.subscribed_textview);
            //clear_all=itemView.findViewById(R.id.clear_all);
            how_many = itemView.findViewById(R.id.how_many);
            anhow_many=itemView.findViewById(R.id.how_many);
            recent_click=itemView.findViewById(R.id.recent_click);
            no_conv=itemView.findViewById(R.id.no_conv);
            nrby_container = itemView.findViewById(R.id.nrby_container);
            create_account=itemView.findViewById(R.id.create_account);
            if_not=itemView.findViewById(R.id.if_not);
            shimmer_nearby = itemView.findViewById(R.id.shimmer);
            nearby_recyler = itemView.findViewById(R.id.container_recyler);
            shimmerL = itemView.findViewById(R.id.shimmerL);
            anoclickable_part=itemView.findViewById(R.id.clickable_part);
            clickable_part = itemView.findViewById(R.id.clickable_part);
            whole_click = itemView.findViewById(R.id.whole_click);
            //Position 1 findview
            suggested_textview = itemView.findViewById(R.id.suggested_textview);
            recyclerView = itemView.findViewById(R.id.container_recyler);
            shimmerFrameLayout = itemView.findViewById(R.id.shimmer);

            //position 2 findview

            taste_genre_textview = itemView.findViewById(R.id.taste_genre_textview);
            taste_genre_recylerview = itemView.findViewById(R.id.taste_genre_recyler);
            des_taste = itemView.findViewById(R.id.des_taste);
            see_all = itemView.findViewById(R.id.see_all);

            //position 3 findviewbyids

            according_to_lang = itemView.findViewById(R.id.according_to_lang_textview);
            lng_not_a_barrier = itemView.findViewById(R.id.language_not_a_barrier);
            language_recyler = itemView.findViewById(R.id.language_recyler);


        }
    }
}
