package com.voive.android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class handling_urls_activity extends AppCompatActivity {

    Resources resources;
    TextView invalid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handling_urls);
        SharedPreferences sharedPreferences = getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng = sharedPreferences.getString("lng", "en");
        Context language_context = LocaleHelper.setLocale(this, lng);
        resources = language_context.getResources();
        invalid=findViewById(R.id.invalid);

        Uri uri = getIntent().getData();


        if (ParseUser.getCurrentUser() == null) {

            startActivity(new Intent(handling_urls_activity.this, startingactivity.class));
            Toast.makeText(handling_urls_activity.this, "First Create Account", Toast.LENGTH_SHORT).show();
        } else {
            if (uri != null) {
                if (uri.getHost().equals("www.voive.in") || uri.getHost().equals("voive.in")) {
                    if (uri.getPath().equals("/tables/invites/")) {
                        if (uri.getQueryParameter("id") != null) {

                            String id = uri.getQueryParameter("id");

                            ParseQuery<ParseObject> parseObjectParseQuery = ParseQuery.getQuery("live_tables");
                            parseObjectParseQuery.getInBackground(id, new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject object, ParseException e) {

                                    if (e == null) {
                                        if (object.getBoolean("Active")) {

                                            if (object.getList("co_creator").contains(ParseUser.getCurrentUser().getObjectId()) || object.getString("table_creator").equals(ParseUser.getCurrentUser().getObjectId())
                                                    || object.getList("table_subscribers").contains(ParseUser.getCurrentUser().getObjectId())) {


                                                Intent intent = new Intent(handling_urls_activity.this, special_speaker_live_table.class);
                                                intent.putExtra("ID", object.getObjectId());
                                                startActivity(intent);
                                                finish();
                                                overridePendingTransition(R.anim.slide_from_end, R.anim.stationary);
                                            } else {

                                                if (!object.getList("BANS").contains(ParseUser.getCurrentUser().getObjectId())) {
                                                    Intent intent = new Intent(handling_urls_activity.this, sound_preview.class);
                                                    intent.putExtra("ID", object.getObjectId());
                                                    startActivity(intent);
                                                    finish();
                                                    overridePendingTransition(R.anim.bottom_up, R.anim.stationary);
                                                } else {


                                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(handling_urls_activity.this, R.style.AlertDialogCustom);
                                                    builder1.setMessage(resources.getString(R.string.staff_ban_you));
                                                    builder1.setTitle(resources.getString(R.string.Banned));
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

                                            }
                                        } else {

                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(handling_urls_activity.this, R.style.AlertDialogCustom);
                                            builder1.setMessage(resources.getString(R.string.SORRY_CREATOR_DISMISSED));
                                            builder1.setTitle(resources.getString(R.string.dismiss_table));
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
                                    } else {
                                        invalid.setText("Broken Or Invalid Link");
                                    }
                                }
                            });
                        } else {

                            invalid.setText("Broken Or Invalid Link");

                        }

                    } else {
                        invalid.setText("Broken Or Invalid Link");
                    }
                } else {
                    invalid.setText("Broken Or Invalid Link");
                }
            } else {
                invalid.setText("Broken Or Invalid Link");
            }
        }


    }
}