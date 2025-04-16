package com.voive.android;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.IOException;



public class voice_mediaplayer_handler extends Service {

   public static MediaPlayer mediaPlayer=new MediaPlayer();

    private final IBinder iBinder=new MyBinder();
    public class MyBinder extends Binder{

        voice_mediaplayer_handler getService(){

            return voice_mediaplayer_handler.this;
        }

    }

    public voice_mediaplayer_handler() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        play_stream(intent.getStringExtra("url"));



        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }
    public void play_player(){

        mediaPlayer.start();
        flip_toggling(true);

    }
    public void pause_player(){

        mediaPlayer.pause();
        flip_toggling(false);

    }
    public MediaPlayer getted(){

        return mediaPlayer;

    }
    public void toggling(){

        if(mediaPlayer.isPlaying()){
            pause_player();
        }
        else {

            play_player();
        }
    }
    public void getMax(){

        Intent intent=new Intent("Duration");
        intent.putExtra("duration",mediaPlayer.getDuration());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }



    public void play_stream(String URL){
        mediaPlayer=new MediaPlayer();

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(URL);
            mediaPlayer.prepare();
            getMax();
            //blast.setAudioSessionId(mediaPlayer.getAudioSessionId());
           play_player();


        } catch (IOException et) {
            et.printStackTrace();
        }

    }


    public void flip_toggling(boolean isPlay){

        Intent intent=new Intent("changePlay");
        intent.putExtra("isplay",isPlay);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }
}
