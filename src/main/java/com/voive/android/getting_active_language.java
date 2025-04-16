package com.voive.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import static android.content.Context.MODE_PRIVATE;

public class getting_active_language {


    Context context;

    public getting_active_language(Context context) {
        this.context = context;
    }

    public Resources getting_active_language() {

        SharedPreferences languagepref = context.getSharedPreferences("LANG", MODE_PRIVATE);
        String languagecode = languagepref.getString("lng", "en");
        Context context_second = LocaleHelper.setLocale(context, languagecode);
        Resources resources = context_second.getResources();

        return resources;
    }


}
