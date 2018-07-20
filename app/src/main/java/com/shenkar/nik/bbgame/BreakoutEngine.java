package com.shenkar.nik.bbgame;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.text.TextPaint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;

import static android.content.ContentValues.TAG;

public class BreakoutEngine extends SurfaceView implements Runnable {

    //out thread
    private Thread gameThread = null;

    //when we use canvas or paint in thread
    private SurfaceHolder ourHolder;

    //game is running or not (bool)
    private volatile boolean playing;

    //game is paused at start
    private boolean paused = true;

    //cnvas & paint object
    private Canvas canvas;
    private Paint paint;

    //color dynamic
    private int [] color = new int[8];

    //screen width and height
    private int screenX;
    private int screenY;

    //
    private long fps;

    //calculate the fps
    private long timeThisFrame;

    //player bat
    Bat bat;

    //ball
    ball ball;

    //brick
    Brick[] brick = new Brick[200];
    int numBrick = 0;

    //sound
    SoundPool soundPool;
    int deep1ID = -1;

    //the score
    int score = 0;

    //lives
    int lives = 3;

    //hits
    int hits = 20;

    //bitmap
    Bitmap  bitmap;
    Bitmap ScaleBitmap;

    //text array
    String [] textarray = {"u", "m", "b", "r", "e", "l", "l", "a"};

    public BreakoutEngine(Context context, int x, int y){
        super(context);

        //initilaize ourHolder and paint object
        ourHolder = getHolder();
        paint = new Paint();
        Arrays.fill(color, Color.GREEN);

        screenX = x;
        screenY = y;

        //create bat
        bat = new Bat(screenX, screenY);

        //create ball
        ball = new ball();

        //create pause button
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.puse);
        ScaleBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);

        //load sound
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        try{
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            //load out sound
            descriptor = assetManager.openFd("A-Tone-His_Self-1266414414.mp3");
            deep1ID = soundPool.load(descriptor,0);
        }catch (IOException e){
            Log.e("error", "faild to load sound files");

        }

        restart();
    }


    //runs when the os calls on Pause on breakoutActivity method.
    public void pause(){
        playing = false;
        try{
            gameThread.join();
        }catch (InterruptedException e){
            Log.e("Error:", "joining thread");
        }
    }

    //runs when the os calls onResume on breakActivity method
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (playing){
            long startFrameTime = System.currentTimeMillis();

            //update the frame
            if(!paused){
                update();
            }

            //draw the frame
            draw();

            //calculate the fps to use to resualt to time anomation and more
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if(timeThisFrame >= 1){
                fps = 1000 / timeThisFrame;
            }
        }
    }

    private void update() {
        //move bat
        bat.update(fps);

        //update the ball
        ball.update(fps);

        //chack for ball toch on brick
        RectF rect;



        for(int i=0; i< numBrick;i++){
            if(RectF.intersects(brick[i].getRect(), ball.getRect())) {
                if(ourHolder.getSurface().isValid()) {
                    if(brick[i].setInvisable() == "red")
                        color[i] = Color.RED;
                    else
                        color[i] = Color.GREEN;

                    ball.reverseY();
                    score++;
                    soundPool.play(deep1ID, 1, 1, 0, 0, 1);
                }
            }
        }

        //chack for the ball touch in bat
        if(RectF.intersects(bat.getRect(), ball.getRect())){
            ball.setRandomX();
            ball.reverseY();
            ball.clearY(bat.getRect().top - 2);
            soundPool.play(deep1ID, 1, 1, 0,0,1);
        }


        //bounce the ball back if is touch the bottom of the screen & debut a life
        if(ball.getRect().bottom > screenY){
            ball.reverseY();
            ball.clearY(screenY - 2);

            //lose a life
            lives--;
            soundPool.play(deep1ID, 1, 1, 0,0,1);

            if(lives == 0){
                paused = true;
                restart();
            }
        }

        //ball back whean it hit the top screen
        if(ball.getRect().top < 0){
            ball.reverseY();
            ball.clearY(12);
            soundPool.play(deep1ID, 1, 1, 0,0,1);
        }

        //if ball hit left
        if(ball.getRect().left < 0){
            ball.reversrX();
            ball.clearX(2);
            soundPool.play(deep1ID, 1, 1, 0,0,1);
        }

        //ball hit right
        if(ball.getRect().right > screenX - 10){
            ball.reversrX();
            ball.clearX(screenX - 22);
            soundPool.play(deep1ID, 1, 1, 0,0,1);
        }

        //pause if clear screen
        if(score == hits){
            paused = true;
            restart();
        }
    }

    void restart(){
        //put the ball back to the start
        ball.reset(screenX, screenY);
        bat.reset(screenX, screenY);

        int brickWidth = screenX / 8;
        int brickHeight = screenY / 10;

        Arrays.fill(color, Color.GREEN);

        //build a wall of brick
        numBrick = 0;

        for(int col = 0; col < 8;col++){
            brick[numBrick] = new Brick(0, col, brickWidth, brickHeight);
            numBrick++;
        }

        score = 0;
        lives = 3;
        hits = 20;
    }


    private void draw() {
        //chack if out game is valid or crash
        if(ourHolder.getSurface().isValid()){
            //ready to drew
            canvas = ourHolder.lockCanvas();

            //drew the background color
            canvas.drawColor(Color.argb(255, 26, 128, 182));


            //////drew all to screen
            // ball & bat color
            paint.setColor(Color.argb(255,  133, 255, 144));
            //draw bat
            canvas.drawRect(bat.getRect(), paint);

            //draw ball
            canvas.drawRect(ball.getRect(), paint);

            // Change the brush color for drawing
            //draw brick
            RectF rect;

            for(int i=0; i < numBrick; i++){
                rect = brick[i].getRect();

                paint.setColor(Color.GREEN);
                TextPaint textPaint = new TextPaint();
                textPaint.setColor(color[i]);
                textPaint.setTextAlign(Paint.Align.CENTER);
                textPaint.setTextSize(60);
                textPaint.setTypeface(Typeface.create("Arial", Typeface.BOLD));

                float textHeight = textPaint.descent() - textPaint.ascent();
                float textOffset = (textHeight / 2) - textPaint.descent();

                canvas.drawRect(rect, paint);
                canvas.drawText(textarray[i], rect.centerX(), rect.centerY() + textOffset, textPaint);
            }


            //draw the HUB
            paint.setColor(Color.argb(255,180,135,255));

            //draw the score
            paint.setTextSize(60);
            canvas.drawText("Your Answer: " + lives, 150, 1020, paint);
            canvas.drawText("Score " + score +"/"+hits + "     Lives " + lives, 1200, 1020, paint);

            //pause button
            canvas.drawBitmap(ScaleBitmap, 10, 940 , null);

            //show all
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }


    //screen touch
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
            //touch in the screen
            case MotionEvent.ACTION_DOWN:
                paused = false;

                if(motionEvent.getX() > screenX / 2 ) {
                    bat.setMovmentState(bat.RIGHT);
                }else {
                    bat.setMovmentState(bat.LEFT);
                }

                break;

            case MotionEvent.ACTION_UP:
                bat.setMovmentState(bat.STOP);
                break;
        }

        return true;
    }
}