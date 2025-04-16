package com.voive.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class categories_in_table extends Fragment {


    String id;
    @BindView(R.id.table_recyler)
    RecyclerView tableRecyler;


    LinearLayoutManager staggeredGridLayoutManager;


    Palette.Swatch swatch;

    @BindView(R.id.shimmer)
    ShimmerFrameLayout shimmer;
    full_width_table_adapter full_width_table_adapter_color;
    List<ParseObject> parseObjects=new ArrayList<>();
    int cont=0;
    int SKIP=0;


    public categories_in_table(String id) {
        this.id = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.fragment_categories_table, null);

        ButterKnife.bind(this, viewGroup);


        SharedPreferences sharedPreferences = getContext().getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng = sharedPreferences.getString("lng", "en");
        Context language_context = LocaleHelper.setLocale(getContext(), lng);
        Resources resources = language_context.getResources();


        ParseQuery<ParseObject> In_PIENCE = new ParseQuery<ParseObject>("live_tables");
        In_PIENCE.setLimit(20);
        In_PIENCE.addDescendingOrder("createdAt");
        In_PIENCE.whereNotEqualTo("IS_PRIVATE",true);
        //      In_PIENCE.whereEqualTo("Category", id);
        In_PIENCE.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if(objects.size()>0){
                    parseObjects.addAll(objects);

                    staggeredGridLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

                    tableRecyler.setLayoutManager(staggeredGridLayoutManager);
                    full_width_table_adapter_color=new full_width_table_adapter(getContext(), parseObjects);
                    tableRecyler.setAdapter(full_width_table_adapter_color);
                    shimmer.stopShimmer();
                    shimmer.setVisibility(View.GONE);

                    tableRecyler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                        }

                        @Override
                        public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            if(!tableRecyler.canScrollVertically(1)){


                                SKIP+=20;

                                ParseQuery<ParseObject> In_PIENCE = new ParseQuery<ParseObject>("live_tables");
                                In_PIENCE.setLimit(20);
                                In_PIENCE.setSkip(SKIP);
                                In_PIENCE.whereNotEqualTo("IS_PRIVATE",true);
                                In_PIENCE.addDescendingOrder("createdAt");
                                //   In_PIENCE.whereEqualTo("Category", id);
                                In_PIENCE.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> objects, ParseException e) {


                                        if(objects.size()>0){

                                            parseObjects.addAll(SKIP,objects);
                                            full_width_table_adapter_color.notifyItemInserted(SKIP);
                                            full_width_table_adapter_color.notifyItemRangeInserted(SKIP,parseObjects.size());
                                        }


                                    }
                                });

                            }
                        }
                    });

                }
                else {
                    tableRecyler.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    tableRecyler.setAdapter(new if_not_found_adapter(getContext(), "No Tables", "Unable To Find table of category " + id, 0));
                    shimmer.stopShimmer();
                    shimmer.setVisibility(View.GONE);
                }



            }
        });


        return viewGroup;
    }


}
