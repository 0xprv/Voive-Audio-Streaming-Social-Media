package com.voive.android;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;


public class homefragment extends Fragment {


    @BindView(R.id.opionionrecyle)
    RecyclerView opionionrecyle;

    @BindView(R.id.appBarLayout3)
    AppBarLayout appBarLayout3;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.shimmer)
    ShimmerFrameLayout shimmer;


    Handler handler = new Handler();
    MainPageAdapter mainPageAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_homefragment, null);
        ButterKnife.bind(this, viewGroup);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("LANG", MODE_PRIVATE);
        String lng = sharedPreferences.getString("lng", "en");
        Context language_context = LocaleHelper.setLocale(getContext(), lng);
        Resources resources = language_context.getResources();

        //Initial Settings
        shimmer.startShimmer();
        ParseQuery<ParseObject> user_data = new ParseQuery<ParseObject>("roundtable_user_data");
        user_data.whereEqualTo("User_ID", ParseUser.getCurrentUser().getObjectId());
        user_data.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                try {

                    List<String> SUBSCRIPTIONS_LIST = objects.get(0).getList("Subscriptions");
                    assert SUBSCRIPTIONS_LIST != null;
                    Collections.reverse(SUBSCRIPTIONS_LIST);


                    ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("live_tables");
                    parseObjectParseQuery.whereContainedIn("objectId", SUBSCRIPTIONS_LIST);
                    parseObjectParseQuery.whereEqualTo("Active",true);
                    parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (objects.size() > 0) {


                                shimmer.stopShimmer();
                                shimmer.setVisibility(View.GONE);
                                swipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.card_view_color));
                                swipeRefreshLayout.setColorSchemeColors(Color.WHITE);

                                mainPageAdapter = new MainPageAdapter(objects, getContext().getApplicationContext(), getActivity());
                                opionionrecyle.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                                opionionrecyle.setAdapter(mainPageAdapter);
                                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                    @Override
                                    public void onRefresh() {


                                        ParseQuery<ParseObject> user_data = new ParseQuery<ParseObject>("roundtable_user_data");
                                        user_data.whereEqualTo("User_ID", ParseUser.getCurrentUser().getObjectId());
                                        user_data.findInBackground(new FindCallback<ParseObject>() {
                                            @Override
                                            public void done(List<ParseObject> objects, ParseException e) {
                                                List<String> SUBSCRIPTIONS_LIST = objects.get(0).getList("Subscriptions");
                                                assert SUBSCRIPTIONS_LIST != null;
                                                Collections.reverse(SUBSCRIPTIONS_LIST);

                                                ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("live_tables");
                                                parseObjectParseQuery.whereContainedIn("objectId", SUBSCRIPTIONS_LIST);
                                                parseObjectParseQuery.whereEqualTo("Active",true);
                                                parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                                                    @Override
                                                    public void done(List<ParseObject> objects, ParseException e) {
                                                        mainPageAdapter.ref(objects);
                                                        swipeRefreshLayout.setRefreshing(false);
                                                    }
                                                });

                                            }
                                        });

                                    }
                                });

                            }

                            else {
                                shimmer.stopShimmer();
                                shimmer.setVisibility(View.GONE);
                                MainPageAdapter_Starting mainPageAdapter_starting = new MainPageAdapter_Starting(getContext());
                                opionionrecyle.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                                opionionrecyle.setAdapter(mainPageAdapter_starting);

                            }

                        }
                    });
                }
                catch (NullPointerException ff){
                    ff.printStackTrace();
                }
            }
        });


        ParseLiveQueryClient parseLiveQueryClient = null;

        try {
            parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI(App.getLIVE()));
        } catch (URISyntaxException et) {
            et.printStackTrace();
        }


        ParseQuery<ParseObject> user_data_live = new ParseQuery<ParseObject>("roundtable_user_data");
        user_data_live.whereEqualTo("User_ID", ParseUser.getCurrentUser().getObjectId());

        SubscriptionHandling<ParseObject> subscriptionHandling_OTHER = parseLiveQueryClient.subscribe(user_data_live);


        subscriptionHandling_OTHER.handleEvent(SubscriptionHandling.Event.UPDATE, new SubscriptionHandling.HandleEventCallback<ParseObject>() {
            @Override
            public void onEvent(ParseQuery<ParseObject> query, ParseObject object) {

                List<String> SUBSCRIPTIONS_LIST = object.getList("Subscriptions");
                assert SUBSCRIPTIONS_LIST != null;
                Collections.reverse(SUBSCRIPTIONS_LIST);


                ParseQuery<ParseObject> parseObjectParseQuery = new ParseQuery<ParseObject>("live_tables");
                parseObjectParseQuery.whereContainedIn("objectId", SUBSCRIPTIONS_LIST);
                parseObjectParseQuery.whereEqualTo("Active",true);
                parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (objects.size() > 0) {

                            if (mainPageAdapter != null) {
                                mainPageAdapter.ref(objects);
                            } else {
                                mainPageAdapter = new MainPageAdapter(objects, getContext().getApplicationContext(), getActivity());
                                opionionrecyle.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                                opionionrecyle.setAdapter(mainPageAdapter);
                            }


                        } else {
                            mainPageAdapter=null;
                            MainPageAdapter_Starting mainPageAdapter_starting = new MainPageAdapter_Starting(getContext());
                            opionionrecyle.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                            opionionrecyle.setAdapter(mainPageAdapter_starting);

                        }
                    }
                });

            }
        });


        return viewGroup;
    }


}

