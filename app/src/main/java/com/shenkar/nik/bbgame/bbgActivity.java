package com.shenkar.nik.bbgame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;

public class bbgActivity extends Activity {

    public static BreakoutEngine breakoutEngine;
    MediaPlayer mMediaPlayer;
    int length;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_bbg);
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer = MediaPlayer.create(this,R.raw.song);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();

        //detect resolution of device display and respond to it
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        //initialize gameView and set it as a view
        breakoutEngine = new BreakoutEngine(this,size.x,size.y);
        setContentView(breakoutEngine);
    }
    @Override
    protected void onDestroy() {
        //other codes
        super.onDestroy();
        mMediaPlayer.stop();
    }
    protected void onResume() {
        super.onResume();
        breakoutEngine.resume();
        mMediaPlayer.seekTo(length);
        mMediaPlayer.start();

    }
    protected void onPause() {
        super.onPause();
        System.out.println("pauseeeeeee");
        breakoutEngine.pause();
        mMediaPlayer.pause();
        length = mMediaPlayer.getCurrentPosition();

    }
}
