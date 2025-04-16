package com.voive.android;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class when_search_is_happening extends RecyclerView.Adapter<when_search_is_happening.VireHolder> {


    Context context;
    String string;
    Activity activity;


    public when_search_is_happening(Context context, String string, Activity activity) {
        this.context = context;
        this.string = string;
        this.activity = activity;
    }

    @NonNull
    @Override
    public VireHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.both_table_and_speaker_search_tab_layout,parent,false);
        return new VireHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VireHolder holder, int position) {
        holder.progressBar.setVisibility(View.VISIBLE);

        ParseQuery<ParseObject> parseObjectParseQuery=new ParseQuery<ParseObject>("live_tables");
        parseObjectParseQuery.whereContains("table_name",string);

        ParseQuery<ParseObject> parseObjectParseQuery2=new ParseQuery<ParseObject>("live_tables");
        parseObjectParseQuery2.whereContains("table_description",string);

        ParseQuery<ParseObject> parseObjectParseQuery3=new ParseQuery<ParseObject>("live_tables");
        parseObjectParseQuery3.whereContains("TALKING_TITLE",string);

        List<ParseQuery<ParseObject>> parseQueries=new ArrayList<>();
        parseQueries.add(parseObjectParseQuery);
        parseQueries.add(parseObjectParseQuery2);
        parseQueries.add(parseObjectParseQuery3);

        ParseQuery<ParseObject> parseObjectParseQuery1=ParseQuery.or(parseQueries);
        parseObjectParseQuery1.whereEqualTo("IS_PRIVATE",false);
        parseObjectParseQuery1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {


                if(objects.size()>0){
                    holder.recyclerViewBouncy.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));

                    full_width_table_adapter featured_table_adapter=new full_width_table_adapter(context.getApplicationContext(),objects);

                    holder.recyclerViewBouncy.setAdapter(featured_table_adapter);

                    holder.progressBar.setVisibility(View.GONE);

                }
                else {
                    holder.recyclerViewBouncy.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));

                    if_not_found_adapter featured_table_adapter=new if_not_found_adapter(context,"","No Conversation Found With "+" ''"+string+"''",0);

                    holder.recyclerViewBouncy.setAdapter(featured_table_adapter);

                    holder.progressBar.setVisibility(View.GONE);

                }


            }
        });

        holder.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()){

                    case 0:
                        holder.progressBar.setVisibility(View.VISIBLE);

                        ParseQuery<ParseObject> parseObjectParseQuery=new ParseQuery<ParseObject>("live_tables");
                        parseObjectParseQuery.whereContains("table_name",string);

                        ParseQuery<ParseObject> parseObjectParseQuery2=new ParseQuery<ParseObject>("live_tables");
                        parseObjectParseQuery2.whereContains("table_description",string);


                        ParseQuery<ParseObject> parseObjectParseQuery3=new ParseQuery<ParseObject>("live_tables");
                        parseObjectParseQuery3.whereContains("TALKING_TITLE",string);

                        List<ParseQuery<ParseObject>> parseQueries=new ArrayList<>();
                        parseQueries.add(parseObjectParseQuery);
                        parseQueries.add(parseObjectParseQuery2);
                        parseQueries.add(parseObjectParseQuery3);

                        ParseQuery<ParseObject> parseObjectParseQuery1=ParseQuery.or(parseQueries);
                        parseObjectParseQuery1.whereEqualTo("IS_PRIVATE",false);
                        parseObjectParseQuery1.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {


                                if(objects.size()>0){
                                    holder.recyclerViewBouncy.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));

                                    full_width_table_adapter featured_table_adapter=new full_width_table_adapter(context.getApplicationContext(),objects);


                                    holder.recyclerViewBouncy.setAdapter(featured_table_adapter);

                                    holder.progressBar.setVisibility(View.GONE);

                                }
                                else {
                                    holder.recyclerViewBouncy.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));

                                    if_not_found_adapter featured_table_adapter=new if_not_found_adapter(context,"","No Conversation Found With "+" ''"+string+"''",0);

                                    holder.recyclerViewBouncy.setAdapter(featured_table_adapter);

                                    holder.progressBar.setVisibility(View.GONE);

                                }


                            }
                        });

                        break;

                    case 1:
                        holder.progressBar.setVisibility(View.VISIBLE);

                        ParseQuery<ParseUser> parseUserParseQuery=ParseUser.getQuery();
                        parseUserParseQuery.whereContains("username",string);


                        ParseQuery<ParseUser> parseUserParseQuery2=ParseUser.getQuery();
                        parseUserParseQuery2.whereContains("First_and_last_name",string);


                        ParseQuery<ParseUser> parseUserParseQuery3=ParseUser.getQuery();
                        parseUserParseQuery3.whereContains("speaker_information",string);

                        List<ParseQuery<ParseUser>> parseQueriesX=new ArrayList<>();
                        parseQueriesX.add(parseUserParseQuery);
                        parseQueriesX.add(parseUserParseQuery2);
                        parseQueriesX.add(parseUserParseQuery3);

                        ParseQuery<ParseUser> final_query=ParseQuery.or(parseQueriesX);

                        final_query.setLimit(20);

                        final_query.findInBackground(new FindCallback<ParseUser>() {
                            @Override
                            public void done(List<ParseUser> objects, ParseException e) {


                                holder.recyclerViewBouncy.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
                                suggested_full_width_speakers_adapter normal_speaker_search_adapter=new suggested_full_width_speakers_adapter(context,objects,activity);
                                normal_speaker_search_adapter.setHasStableIds(true);

                                holder.recyclerViewBouncy.setAdapter(normal_speaker_search_adapter);
                                holder.progressBar.setVisibility(View.GONE);

                            }
                        });

break;
                    case 2:

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class VireHolder extends RecyclerView.ViewHolder {

        TabLayout tabLayout;
        RecyclerView recyclerViewBouncy;
        ProgressBar progressBar;
        public VireHolder(@NonNull View itemView) {
            super(itemView);
            tabLayout=itemView.findViewById(R.id.tab_layout);
            progressBar=itemView.findViewById(R.id.pbProgress);
            recyclerViewBouncy=itemView.findViewById(R.id.searcher_recyler);

        }
    }

}
