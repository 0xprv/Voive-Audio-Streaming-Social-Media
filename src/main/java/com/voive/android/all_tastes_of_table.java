package com.voive.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.button.MaterialButton;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class all_tastes_of_table extends AppCompatActivity {

    @BindView(R.id.another_setting_textview)
    TextView anotherSettingTextview;
    @BindView(R.id.back_3)
    MaterialButton back3;
    @BindView(R.id.recyler_view)
    RecyclerView recylerView;
    @BindView(R.id.editText4)
    EditText editText4;

    List<String> strings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tastes_of_table);
        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng = sharedPreferences.getString("lng", "en");
        Context language_context = LocaleHelper.setLocale(this, lng);
        Resources resources = language_context.getResources();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        anotherSettingTextview.setText(resources.getString(R.string.Taste_and_Genres));


        String[] sportsstring = {"Football",
                "Badminton",
                "Field Hockey",
                "Volleyball",
                "Basketball",
                "Tennis",
                "Cricket",
                "Table Tennis",
                "Boxing",
                "Wrestling",
                "Chess",
                "Board Game",
                "Cycling",
                "Shooting",
                "Snooker",
                "Baseball",
                "Golf",
                "Art",
                "Business",
                "Citizenship",
                "Reading",
                "Design",
                "Technology",
                "English",
                "Geography",
                "Psychology",
                "History",
                "Civics",
                "Podcasts",
                "Maths",
                "Space",
                "Physical",
                "Religious",
                "Science",
                "Physics",
                "Sociology",
                "Commerce",
                "Chemistry",
                "Computer",
                "Wearable devices",
                "Cryptocurrenccy",
                "3D printing",
                "Genomic",
                "AI",
                "Networking",
                "Robots",
                "Digital Devices",
                "Future Technology",
                "Internet Of Things",
                "Data Science",
                "Hacking",
                "Nuclear Power",
                "Space",
                "Dreams",
                "5G",
                "Biometrics",
                "Blockchain",
                "Quantum Computing",
                "Robotics",
                "Engineering",
                "Angel Investing",
                "Coding",
                "Web Developer",
                "Virtual Reality",
                "Data Analytics",
                "Movies",
                "TV Shows",
                "Books",
                "Games",
                "Comedy",
                "Circus",
                "Painting",
                "Dance",
                "Music",
                "Celebrities",
                "Story",
                "Photography",
                "Drawing",
                "Painting",
                "Anime or Manga",
                "Theater",
                "Carnival",
                "Magic",
                "Concerts",
                "Radio",
                "Festivals",
                "Food",
                "Shopping",
                "Travel",
                "Pet Shows",
                "Politics",
                "Economy",
                "Newspapers",
                "Current Events",
                "Social Issues",
                "Indian Government",
                "COVID-19",
                "BJP",
                "Congress",
                "Security",
                "Agriculture",
                "Farmers",
                "Election",
                "Food",
                "Yoga",
                "Meditation",
                "Nutrition",
                "Medicine",
                "Health",
                "Fashion",
                "Travelling",
                "Parenting",
                "Sleep",
                "Family",
                "Beauty",
                "Ayurved",
                "Fitness",
                "Startups",
                "Share Market",
                "Investment",
                "Mutual Funds",
                "Stock Market",
                "Bitcoin",
                "Accounting",
                "Affiliate Market",
                "Freelance",
                "MLM",
                "Communication",
                "Leadership",
                "Digital Marketing",
                "Strategy",
                "Fund Raising"};


        strings = new ArrayList<>();

        for (int i = 0; i < sportsstring.length; i++) {

            strings.add(sportsstring[i]);

        }


        recylerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recylerView.setAdapter(new main_genre_adater(this, strings));


        PushDownAnim.setPushDownAnimTo(back3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.stationary, R.anim.slide_to_start);
            }
        });

        editText4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if(!s.toString().trim().isEmpty()){
                    List<String> new_list=new ArrayList<>();

                    for(String query:strings){

                        if(query.contains(s.toString())){

                            new_list.add(query);

                        }

                    }



                    recylerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                    recylerView.setAdapter(new main_genre_adater(all_tastes_of_table.this, new_list));

                }

                else {

                    String[] sportsstring = {"Football",
                            "Badminton",
                            "Field Hockey",
                            "Volleyball",
                            "Basketball",
                            "Tennis",
                            "Cricket",
                            "Table Tennis",
                            "Boxing",
                            "Wrestling",
                            "Chess",
                            "Board Game",
                            "Cycling",
                            "Shooting",
                            "Snooker",
                            "Baseball",
                            "Golf",
                            "Art",
                            "Business",
                            "Citizenship",
                            "Reading",
                            "Design",
                            "Technology",
                            "English",
                            "Geography",
                            "Psychology",
                            "History",
                            "Civics",
                            "Podcasts",
                            "Maths",
                            "Space",
                            "Physical",
                            "Religious",
                            "Science",
                            "Physics",
                            "Sociology",
                            "Commerce",
                            "Chemistry",
                            "Computer",
                            "Wearable devices",
                            "Cryptocurrenccy",
                            "3D printing",
                            "Genomic",
                            "AI",
                            "Networking",
                            "Robots",
                            "Digital Devices",
                            "Future Technology",
                            "Internet Of Things",
                            "Data Science",
                            "Hacking",
                            "Nuclear Power",
                            "Space",
                            "Dreams",
                            "5G",
                            "Biometrics",
                            "Blockchain",
                            "Quantum Computing",
                            "Robotics",
                            "Engineering",
                            "Angel Investing",
                            "Coding",
                            "Web Developer",
                            "Virtual Reality",
                            "Data Analytics",
                            "Movies",
                            "TV Shows",
                            "Books",
                            "Games",
                            "Comedy",
                            "Circus",
                            "Painting",
                            "Dance",
                            "Music",
                            "Celebrities",
                            "Story",
                            "Photography",
                            "Drawing",
                            "Painting",
                            "Anime or Manga",
                            "Theater",
                            "Carnival",
                            "Magic",
                            "Concerts",
                            "Radio",
                            "Festivals",
                            "Food",
                            "Shopping",
                            "Travel",
                            "Pet Shows",
                            "Politics",
                            "Economy",
                            "Newspapers",
                            "Current Events",
                            "Social Issues",
                            "Indian Government",
                            "COVID-19",
                            "BJP",
                            "Congress",
                            "Security",
                            "Agriculture",
                            "Farmers",
                            "Election",
                            "Food",
                            "Yoga",
                            "Meditation",
                            "Nutrition",
                            "Medicine",
                            "Health",
                            "Fashion",
                            "Travelling",
                            "Parenting",
                            "Sleep",
                            "Family",
                            "Beauty",
                            "Ayurved",
                            "Fitness",
                            "Startups",
                            "Share Market",
                            "Investment",
                            "Mutual Funds",
                            "Stock Market",
                            "Bitcoin",
                            "Accounting",
                            "Affiliate Market",
                            "Freelance",
                            "MLM",
                            "Communication",
                            "Leadership",
                            "Digital Marketing",
                            "Strategy",
                            "Fund Raising"};


                    strings = new ArrayList<>();

                    for (int i = 0; i < sportsstring.length; i++) {

                        strings.add(sportsstring[i]);

                    }


                    recylerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                    recylerView.setAdapter(new main_genre_adater(all_tastes_of_table.this, strings));



                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.slide_to_start);
    }
}
