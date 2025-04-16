package com.voive.android;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.r0adkll.slidr.model.SlidrInterface;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class main_search_fragment extends Fragment {

    @BindView(R.id.back3)
    MaterialButton back3;
    SlidrInterface slidrInterface;

    List<String> RECENT_SEARCHES = new ArrayList<>();
    @BindView(R.id.if_not_any)
    LinearLayout ifNotAny;
    public static EditText editText4;

    @BindView(R.id.s_conv)
    TextView sConv;
    @BindView(R.id.part_of_them)
    TextView partOfThem;
    @BindView(R.id.qr)
    MaterialButton qr;
    @BindView(R.id.clear_text)
    MaterialButton clear_text;
    @BindView(R.id.when_s_happening)
    LinearLayout when_s_happening;

    RecyclerView recyclerViewBouncy;
    ProgressBar progressBar;
    public main_search_fragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_main_search_fragment, null);
        // Inflate the layout for this fragment
        ButterKnife.bind(this,viewGroup);
        progressBar=viewGroup.findViewById(R.id.pbProgress);
        recyclerViewBouncy=viewGroup.findViewById(R.id.searcher_recyler);
        editText4 = viewGroup.findViewById(R.id.editText4);
        SharedPreferences languagepref = getContext().getSharedPreferences("LANG", MODE_PRIVATE);
        String languagecode = languagepref.getString("lng", "en");
        Context context = LocaleHelper.setLocale(getContext(), languagecode);
        Resources resources = context.getResources();
        editText4.setHint(resources.getString(R.string.Search_Voive));

        sConv.setText(resources.getString(R.string.Search_for_conv));
        partOfThem.setText(resources.getString(R.string.and_be_part_of_them));

        PushDownAnim.setPushDownAnimTo(qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RxPermissions rxPermissions = new RxPermissions(main_search_fragment.this);

                rxPermissions
                        .requestEach(Manifest.permission.CAMERA)
                        .subscribe(permission -> {
                            if (permission.granted) {
                                Intent intent=new Intent(getContext(),QR_SCAN.class);
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
                            } else if (permission.shouldShowRequestPermissionRationale) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }
                        });


            }
        });
        clear_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText4.getText().clear();
            }
        });
        RecyclerView.OnItemTouchListener mScrollTouchListener = new RecyclerView.OnItemTouchListener() {

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                int action = e.getAction();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                }
                return false;
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        };
        editText4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().isEmpty()) {

                    ifNotAny.setVisibility(View.VISIBLE);
                    when_s_happening.setVisibility(View.GONE);
                    clear_text.setVisibility(View.GONE);
                  //  ss_result.setText("");
                    progressBar.setVisibility(View.GONE);

                } else {
                    clear_text.setVisibility(View.VISIBLE);
                    ifNotAny.setVisibility(View.GONE);
                    when_s_happening.setVisibility(View.VISIBLE);

                    progressBar.setVisibility(View.VISIBLE);
                    ParseQuery<ParseObject> parseObjectParseQuery=new ParseQuery<ParseObject>("live_tables");
                    parseObjectParseQuery.whereContains("table_name",editText4.getText().toString());

                    ParseQuery<ParseObject> parseObjectParseQuery2=new ParseQuery<ParseObject>("live_tables");
                    parseObjectParseQuery2.whereContains("table_description",editText4.getText().toString());

                    ParseQuery<ParseObject> parseObjectParseQuery3=new ParseQuery<ParseObject>("live_tables");
                    parseObjectParseQuery3.whereContains("TALKING_TITLE",editText4.getText().toString());

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


                              //  ss_result.setVisibility(View.VISIBLE);
                              //  ss_result.setText(Integer.toString(objects.size())+" Results Found");
                                progressBar.setVisibility(View.GONE);
                                full_width_table_adapter featured_table_adapter=new full_width_table_adapter(context.getApplicationContext(),objects);
                                recyclerViewBouncy.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                                recyclerViewBouncy.setAdapter(featured_table_adapter);




                            }
                            else {

                                progressBar.setVisibility(View.GONE);
                                if_not_found_adapter ifn=new if_not_found_adapter(context,"","No Conversation Found With "+" ''"+editText4.getText().toString()+"''",0);
                                recyclerViewBouncy.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                                recyclerViewBouncy.setAdapter(ifn);




                            }


                        }
                    });



                }

            }
        });
        back3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences languagepref = getContext().getSharedPreferences("LANG", MODE_PRIVATE);
                String languagecode = languagepref.getString("lng", "en");
                Context context = LocaleHelper.setLocale(getContext(), languagecode);
                Locale locale = new Locale(languagecode);
                Locale.setDefault(locale);


                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, languagecode);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Search For Anything");

                try {
                    startActivityForResult(intent, 1);
                } catch (ActivityNotFoundException d) {

                    Toast.makeText(context, d.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
        return viewGroup;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && data != null && resultCode == RESULT_OK) {

            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            editText4.setText(result.get(0));
            RECENT_SEARCHES.add(result.get(0));

        }
    }

    RecyclerView.OnItemTouchListener mScrollTouchListener = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            int action = e.getAction();
            switch (action) {
                case MotionEvent.ACTION_MOVE:
                    rv.getParent().requestDisallowInterceptTouchEvent(true);
                    break;
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    };


}