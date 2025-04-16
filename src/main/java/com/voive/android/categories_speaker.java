package com.voive.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class categories_speaker extends Fragment {

    String id;
    Palette.Swatch COLOR;
    @BindView(R.id.shimmer)
    ShimmerFrameLayout shimmer;


    public categories_speaker(String id) {
        this.id = id;
    }

    @BindView(R.id.speaker_recyler)
    RecyclerView speakerRecyler;


    List<ParseUser> GLOBAL_USER;

    int SKIP = 20;

    suggested_full_width_speakers_adapter live_table_subscribers_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.fragment_categories_speaker, null);
        ButterKnife.bind(this, viewGroup);


        GLOBAL_USER = new ArrayList<>();




        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        // parseQuery.whereEqualTo("Tastes_list",id);
        parseQuery.setLimit(20);
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {

                if(objects.size()>0){
                    GLOBAL_USER.clear();
                    GLOBAL_USER.addAll(objects);

                    speakerRecyler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    live_table_subscribers_adapter = new suggested_full_width_speakers_adapter(getContext(), GLOBAL_USER,getActivity());
                    speakerRecyler.setAdapter(live_table_subscribers_adapter);
                    shimmer.stopShimmer();
                    shimmer.setVisibility(View.GONE);

                    speakerRecyler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                        }

                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            if (!recyclerView.canScrollVertically(1)) {

                                SKIP = SKIP + 20;


                                ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                             //   parseQuery.whereEqualTo("Tastes_list", id);
                                parseQuery.setSkip(SKIP);
                                parseQuery.setLimit(20);
                                parseQuery.findInBackground(new FindCallback<ParseUser>() {
                                    @Override
                                    public void done(List<ParseUser> objects, ParseException e) {

                                        if(objects.size()>0){
                                            GLOBAL_USER.addAll(SKIP+1,objects);
                                            live_table_subscribers_adapter.notifyDataSetChanged();
                                            live_table_subscribers_adapter.notifyItemRangeInserted(SKIP+1,objects.size());

                                        }




                                    }
                                });


                            }
                        }
                    });
                }
                else {

                    speakerRecyler.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    speakerRecyler.setAdapter(new if_not_found_adapter(getContext(), "", "Unable To Find Speaker of category " + id, 0));
                    shimmer.stopShimmer();
                    shimmer.setVisibility(View.GONE);
                }


            }
        });









        // Inflate the layout for this fragment
        return viewGroup;
    }


}
