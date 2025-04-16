package com.voive.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class BACKROOM_RTC_ENGINE_NOTIFICATION_SERVICE extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        if(ParseUser.getCurrentUser()!=null){

            ParseQuery<ParseObject> ppp = new ParseQuery<ParseObject>("roundtable_user_data");
            ppp.whereEqualTo("User_ID", ParseUser.getCurrentUser().getObjectId());
            ppp.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {

                    for (ParseObject parseObject : objects) {


                        parseObject.put("Backroom_Connected", false);
                        parseObject.saveInBackground();

                    }

                }
            });
        }

    }
}
