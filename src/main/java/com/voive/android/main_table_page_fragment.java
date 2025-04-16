package com.voive.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class main_table_page_fragment extends Fragment {


    public static String id;


    private static final int sColumnWidth = 120; // assume cell width of 120dp


    List<ParseUser> creators = new ArrayList<>();
    @BindView(R.id.image)
    CircleImageView image;
    @BindView(R.id.textView15)
    TextView textView15;
    @BindView(R.id.subs_text)
    TextView subsText;
    @BindView(R.id.time_of_table)
    TextView timeOfTable;
    @BindView(R.id.linearLayout2)
    LinearLayout linearLayout2;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.id_flowlayout)
    TagFlowLayout idFlowlayout;
    @BindView(R.id.creator_textview)
    TextView creatorTextview;
    @BindView(R.id.linearLayout4)
    LinearLayout linearLayout4;
    @BindView(R.id.creator_recyler)
    RecyclerView creatorRecyler;
    @BindView(R.id.shimer)
    ShimmerFrameLayout shimmerFrameLayout;
    @BindView(R.id.shimer_linear)
    LinearLayout shimer_linear;
    @BindView(R.id.main)
    ConstraintLayout main;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_main_table_page_fragment, null);
        // Inflate the layout for this fragment

        ButterKnife.bind(this, viewGroup);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng = sharedPreferences.getString("lng", "en");
        Context language_context = LocaleHelper.setLocale(getContext(), lng);
        Resources resources = language_context.getResources();


        shimer_linear.setVisibility(View.VISIBLE);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
        main.setVisibility(View.GONE);
        creators.clear();
        ParseQuery<ParseObject> parseObjectParseQuery = ParseQuery.getQuery("live_tables");
        parseObjectParseQuery.getInBackground(id, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {


                if(object.getList("category").size()>0){

                    List<String> mVals = object.getList("category");

                    idFlowlayout.setAdapter(new TagAdapter<String>(mVals) {
                        @Override
                        public View getView(FlowLayout parent, int position, String s) {
                            TextView tv = (TextView) getLayoutInflater().inflate(R.layout.tvf,
                                    idFlowlayout, false);
                            tv.setText(s);
                            return tv;
                        }
                    });


                    idFlowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                        @Override
                        public boolean onTagClick(View view, int position, FlowLayout parent) {


                            Intent intent = new Intent(getActivity(), when_taste_clicked_activity.class);
                            intent.putExtra("ID", mVals.get(position));
                            startActivity(intent);
                           getActivity().overridePendingTransition(R.anim.alerter_slide_in_from_bottom, R.anim.stationary);
                            return false;
                        }
                    });

                }
                else {
                    idFlowlayout.setVisibility(View.GONE);
                }

                subsText.setText(Integer.toString(object.getList("table_subscribers").size()) + " " + resources.getString(R.string.Subscribers));

                timeOfTable.setText(object.getString("start_time") + " To " + object.getString("end_time"));

                Glide.with(getContext().getApplicationContext()).asBitmap().load(object.getParseFile("table_image").getUrl()).centerInside().into(image);


                Glide.with(getContext().getApplicationContext()).asBitmap().load(object.getParseFile("table_image").getUrl()).centerInside()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                                Palette.generateAsync(resource, new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(@Nullable Palette palette) {

                                        Palette.Swatch swatch = palette.getDominantSwatch();
                                        if (swatch != null) {

                                            image.setCircleBackgroundColor(swatch.getRgb());
                                        } else {
                                            image.setCircleBackgroundColor(getContext().getColor(R.color.card_view_color));

                                        }
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        shimmerFrameLayout.stopShimmer();
                                        shimer_linear.setVisibility(View.GONE);
                                        main.setVisibility(View.VISIBLE);
                                    }
                                });

                            }
                        });

                textView15.setText(object.getString("table_name"));

                description.setText(object.getString("table_description"));

                ParseQuery<ParseUser> parseUserParseQuery_X = ParseUser.getQuery();
                parseUserParseQuery_X.whereEqualTo("objectId", object.getString("table_creator"));
                parseUserParseQuery_X.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {

                        creators.addAll(objects);

                        creatorRecyler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

                        creatorRecyler.setAdapter(new live_table_creators_adapter(creators, getContext(), id));
                    }
                });


            }
        });


        return viewGroup;
    }


}


