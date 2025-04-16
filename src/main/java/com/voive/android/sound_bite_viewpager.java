package com.voive.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;
import com.thekhaeng.pushdownanim.PushDownAnim;
import com.voive.android.media.RtcTokenBuilder;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;

public class sound_bite_viewpager extends FragmentStatePagerAdapter {


    List<ParseObject> parseObjects;
    String AUDIO_KEY,CERTIFICATE;


    public sound_bite_viewpager(@NonNull FragmentManager fm, List<ParseObject> parseObjects, String AUDIO_KEY, String CERTIFICATE) {
        super(fm);
        this.parseObjects = parseObjects;
        this.AUDIO_KEY = AUDIO_KEY;
        this.CERTIFICATE = CERTIFICATE;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        preview_of_talking_two_minutes preview_of_talking_two_minutes=new preview_of_talking_two_minutes(AUDIO_KEY,CERTIFICATE);
        Bundle bundle=new Bundle();
        bundle.putString("ID",parseObjects.get(position).getObjectId());
        preview_of_talking_two_minutes.setArguments(bundle);
        return preview_of_talking_two_minutes;
    }

    @Override
    public int getCount() {
        return parseObjects.size();
    }
}
