package com.shenkar.nik.bbgame;

import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.WindowManager;

public class bbgActivityLevel2 extends AppCompatActivity {

    public static lavel2Engine lavel2;
    MediaPlayer mMediaPlayer;
    int length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


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
        lavel2 = new lavel2Engine(this,size.x,size.y);
        setContentView(lavel2);
    }

    @Override
    protected void onDestroy() {
        //other codes
        super.onDestroy();
        mMediaPlayer.stop();
    }
    protected void onResume() {
        super.onResume();
        lavel2.resume();
        mMediaPlayer.seekTo(length);
        mMediaPlayer.start();

    }
    protected void onPause() {
        super.onPause();
        lavel2.pause();
        mMediaPlayer.pause();
        length = mMediaPlayer.getCurrentPosition();

    }

}
