package com.shenkar.nik.bbgame;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.text.TextPaint;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;


@SuppressLint("ViewConstructor")
public class BreakoutEngine extends SurfaceView implements Runnable {

    //out thread
    private Thread gameThread = null;

    //when we use canvas or paint in thread
    private SurfaceHolder ourHolder;

    //game is running or not (bool)
    private volatile boolean playing;

    //game is paused at start
    private boolean paused = true;

    private Paint paint;
    private Paint paint1;

    //color dynamic
    private int [] color = new int[8];

    //screen width and height
    private int screenX;
    private int screenY;

    //
    private long fps;

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

    RectF exception = null;

    //bitmap
    Bitmap  bitmap;
    Bitmap  bitmapBackground;
    Bitmap  ScaleBitmap;
    Bitmap  backgroundConfig;

    //array of the words that the player has to guess
    String [][] textarray = {{"c","a","t"}, {"b","a","l","l"},{"u", "m", "b", "r", "e", "l", "l", "a"},{"b"}};

    //random
    Random rnd = new Random();

    int level = 0;

    //context
    private Context mContext;

    public BreakoutEngine(Context context, int x, int y){
        super(context);

        this.mContext = context;


        //initilaize ourHolder and paint object
        ourHolder = getHolder();
        paint = new Paint();
        paint1= new Paint();
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

        //paint1
        paint1.setColor(Color.GREEN);
        paint1.setTextSize(60);
        paint1.setTypeface(Typeface.create("Arial",Typeface.BOLD_ITALIC));

        //background
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);


        assert wm != null;
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        int width = display.getWidth();  // deprecated
        int height = display.getHeight();

        bitmapBackground = BitmapFactory.decodeResource(getResources(), R.drawable.city);
        backgroundConfig = Bitmap.createScaledBitmap(bitmapBackground, width, height, true);

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
            long timeThisFrame = System.currentTimeMillis() - startFrameTime;
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
        for(int i=0; i< numBrick;i++){
            try{
                if (RectF.intersects(brick[i].getRect(), ball.getRect())) {
                    if(brick[i].getRect() == exception){

                       continue;
                    }

                    exception = brick[i].getRect();

                    if (ourHolder.getSurface().isValid()) {

                        if (brick[i].setInvisable().equals("red"))
                            color[i] = Color.RED;
                        else
                            color[i] = Color.GREEN;

                        paint1.setColor(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
                        ball.reverseY();
                        score++;
                    }
                }
            }catch (Exception e){
                Log.e("errorrrrrrrrrrrrrrr",e.getMessage());

            }
        }
        System.out.println("000000000000000000000000: continu");


        //chack for the ball touch in bat
        if(RectF.intersects(bat.getRect(), ball.getRect())){
            ball.setRandomX();
            ball.reverseY();
            ball.clearY(bat.getRect().top - 2);
        }


        //bounce the ball back if is touch the bottom of the screen & debut a life
        if(ball.getRect().bottom > screenY){
            ball.reverseY();
            ball.clearY(screenY - 2);

            //lose a life
            lives--;

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
        int level = getLevel();
        int size = textarray[level].length;
        //put the ball back to the start
        ball.reset(screenX, screenY);
        bat.reset(screenX, screenY);

        int brickWidth = screenX / size;
        int brickHeight = screenY / 10;

        Arrays.fill(color, Color.GREEN);

        //build a wall of brick
        numBrick = 0;

        //row position | col position | brick width | brick height
        for(int col = 0; col < size;col++){
            brick[numBrick] = new Brick(0, col, brickWidth, brickHeight);
            numBrick++;
        }

        score = 0;
        lives = 3;
        hits = 20;
    }


    void levelUp(){
        //int level = getLevel();
        level++;

        int size = textarray[level].length;
        //put the ball back to the start
        ball.reset(screenX, screenY);
        bat.reset(screenX, screenY);

        int brickWidth = screenX / size;
        int brickHeight = screenY / 10;

        Arrays.fill(color, Color.GREEN);

        //build a wall of brick
        numBrick = 0;

        for(int col = 0; col < size;col++){
            brick[numBrick] = new Brick(0, col, brickWidth, brickHeight);
            numBrick++;
        }

        if(level == 3){
            mContext = getContext();
            //Intent intent = new Intent(mContext, bbgActivityLevel2.class);
            pause();
            Intent intent = new Intent(mContext, FinalLevel1.class);
            mContext.startActivity(intent);
            ((Activity)mContext).finish();
        }

        score = 0;
        lives = 3;
        hits = 20;
    }





    private void draw() {
        //chack if out game is valid or crash
        if(ourHolder.getSurface().isValid()){
            //ready to drew
            Canvas canvas = ourHolder.lockCanvas();

            //drew the background color
            canvas.drawColor(Color.argb(255, 26, 128, 182));
            canvas.drawBitmap(backgroundConfig, 0, 0 , null);


            //////drew all to screen
            // ball & bat color
            paint.setColor(Color.argb(255,  133, 255, 144));

            //draw bat
            canvas.drawRoundRect(bat.getRect(),10,10, paint);

            //draw ball
            canvas.drawRect(ball.getRect(), paint);

            // Change the brush color for drawing
            //draw brick
            RectF rect;

            int level = getLevel();
            //int size = textarray[level].length;

            for(int i=0; i < numBrick; i++){
                rect = brick[i].getRect();

                paint.setColor(Color.GREEN);
                TextPaint textPaint = new TextPaint();
                textPaint.setColor(color[i]);
                textPaint.setTextAlign(Paint.Align.CENTER);
                textPaint.setTextSize(80);
                textPaint.setTypeface(Typeface.create(  "dancing_script", Typeface.BOLD));

                float textHeight = textPaint.descent() - textPaint.ascent();
                float textOffset = (textHeight / 2) - textPaint.descent();

                canvas.drawRoundRect(rect,20,20,paint);
               // canvas.drawRect(rect, paint);
                canvas.drawText(textarray[level][i], rect.centerX(), rect.centerY() + textOffset, textPaint);

            }

            //draw the resualt button
            canvas.drawText("GUESS", (int)(screenX*0.88), (int)(screenY*0.95), paint1);

            //draw the score
            paint.setTextSize(60);
            paint.setColor(Color.argb(255,180,135,255));
            canvas.drawText("Score " + score +"/"+hits + "     Lives " + lives,(int)(screenX*0.3), (int)(screenY*0.95), paint);

            //pause button
            canvas.drawBitmap(ScaleBitmap, 0, (int)(screenY * 0.9) , null);



            //unlock the thread
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }


    /*void setLevel(){
        level++;
    }*/

    int getLevel(){
        return level;
    }

    void finish(){
        ((Activity)mContext).finish();
    }


    //screen touch
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        try{
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
                //touch in the screen
                case MotionEvent.ACTION_DOWN:
                    paused = false;
                    if(motionEvent.getX() > screenX / 2 ) {
                        bat.setMovmentState(bat.RIGHT);
                    }else {
                        bat.setMovmentState(bat.LEFT);
                    }

                    if(motionEvent.getX() < 100 && motionEvent.getY() > screenY * 0.9 && motionEvent.getY() < screenY ){
                        if(!playing){
                            playing = true;
                            resume();
                        }else{
                            pause();
                            mContext = getContext();
                            Intent intent = new Intent(mContext, Dialog.class);
                            // Intent intent = new Intent(mContext, Guess.class);
                            mContext.startActivity(intent);
                        }
                    }

                    if(motionEvent.getY() > screenY * 0.9 && motionEvent.getX() > screenX * 0.88 && motionEvent.getX() < screenX ){
                        if(!playing){
                            playing = true;
                            resume();
                        }else{
                            pause();
                            mContext = getContext();
                            Intent intent = new Intent(mContext, Guess.class);
                            mContext.startActivity(intent);
                        }
                    }


                    break;

                case MotionEvent.ACTION_UP:
                    bat.setMovmentState(bat.STOP);
                    break;
            }
        }catch (NullPointerException e){
            Log.e("errorrrrrrrrrrrrrrr",e.getMessage());
        }


        return true;
    }
}