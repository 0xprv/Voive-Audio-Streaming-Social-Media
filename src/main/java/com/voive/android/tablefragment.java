package com.voive.android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.discord.panels.PanelsChildGestureRegionObserver;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButton;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class tablefragment extends Fragment implements PanelsChildGestureRegionObserver.GestureRegionsListener {


    @BindView(R.id.textView34)
    TextView textView34;
    @BindView(R.id.appBarLayout3)
    AppBarLayout appBarLayout3;
    @BindView(R.id.opionionrecyle)
    RecyclerView opionionrecyle;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.divider8)
    View divider8;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_tablefragment, null);
        // Inflate the layout for this fragment
        ButterKnife.bind(this, viewGroup);

        MainActivity.top_toolbar.setElevation(0);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng = sharedPreferences.getString("lng", "en");
        boolean FIRST_TIME = sharedPreferences.getBoolean("IS_FIRST_DISCOVER", true);
        Context language_context = LocaleHelper.setLocale(getContext(), lng);
        Resources resources = language_context.getResources();

        if (FIRST_TIME) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setView(R.layout.explore_tables_alert_introduction);

            AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            alertDialog.show();

            TextView deactivate_textview = alertDialog.findViewById(R.id.deactivate_textview);
            TextView d_message = alertDialog.findViewById(R.id.deactivate_message_textview);
            MaterialButton deactivate = alertDialog.findViewById(R.id.deactivate);


            d_message.setText(resources.getString(R.string.Disc_des));
            deactivate.setText(resources.getString(R.string.LET_GO));
            deactivate_textview.setText(resources.getString(R.string.Discv));


            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("IS_FIRST_DISCOVER", false);
                    editor.apply();
                }
            });
            PushDownAnim.setPushDownAnimTo(deactivate).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    alertDialog.cancel();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("IS_FIRST_DISCOVER", false);
                    editor.apply();
                }
            });
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            double dd = 0.30 * width;
            int new_width = width - (int) dd;
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
            layoutParams.width = new_width;
            alertDialog.getWindow().setAttributes(layoutParams);


        }
      /*  appBarLayout3.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {

                    MainActivity.top_toolbar.setElevation(4);


                } else {


                    MainActivity.top_toolbar.setElevation(0);
                    //not
                }
            }
        });*/

        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setColorSchemeColors(Color.WHITE);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                opionionrecyle.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                opionionrecyle.setAdapter(new main_table_adapter(getContext()));
                swipeRefreshLayout.setRefreshing(false);

            }
        });
        swipeRefreshLayout.setRefreshing(false);


        opionionrecyle.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        opionionrecyle.setAdapter(new main_table_adapter(getContext()));

    //    preview_of_talking_two_minutes preview_of_talking_two_minutes=new preview_of_talking_two_minutes(getContext());
     //   preview_of_talking_two_minutes.show(getActivity().getSupportFragmentManager(),"Show");


        PanelsChildGestureRegionObserver.Provider.get().addGestureRegionsUpdateListener(this);

        return viewGroup;
    }


    @Override
    public void onGestureRegionsUpdate(@NotNull List<Rect> list) {
    }
}
