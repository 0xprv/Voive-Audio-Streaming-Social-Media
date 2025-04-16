package com.voive.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class speaker_search_fragment extends Fragment {


    String query;

    public speaker_search_fragment(String qq) {
        query=qq;
    }

    @BindView(R.id.recyler)
    RecyclerView recyler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_speaker_search_fragment, null);

        ButterKnife.bind(this,viewGroup);


        ParseQuery<ParseUser> lotsOfWins = ParseUser.getQuery();
        lotsOfWins.whereContains("username",query);

        ParseQuery<ParseUser> first_last = ParseUser.getQuery();
        first_last.whereContains("First_and_last_name",query);


        ParseQuery<ParseUser> bio = ParseUser.getQuery();
        bio.whereContains("speaker_information",query);


        List<ParseQuery<ParseUser>> queries = new ArrayList<ParseQuery<ParseUser>>();
        queries.add(lotsOfWins);
        queries.add(first_last);
        queries.add(bio);

        ParseQuery<ParseUser> mainQuery = ParseQuery.or(queries);

        mainQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {


                recyler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                recyler.setAdapter(new speaker_search_adapter(objects,getContext()));
            }
        });





        // Inflate the layout for this fragment
        return viewGroup;
    }


}
