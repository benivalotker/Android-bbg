package com.shenkar.nik.bbgame;

import android.graphics.RectF;

public class Bat {

    //is an object that holder four coordinats
    private RectF rect;

    //how long will our paddle will be
    private float length;

    //X is the far left of the rectangle which forms our padding
    private float x;

    //hold pixle per seconde speed that the paddle will move
    private float paddleSpeed;

    //which way paddale can move
    final int STOP = 0;
    final int LEFT = 1;
    final int RIGHT = 2;

    //paddale moving and which direction
    private int paddaleMoveing = STOP;

    //constractor paddale
    Bat(int screenX, int screenY){

        length = 130;               //width
        float height = 20;          //height

        //start paddle
        x = screenX / 2;

        //top coordinate
        float y = screenY - 20;
        rect = new RectF(x,y,x+length,y + height);


        //paddale fast in pixle
        paddleSpeed = 350;

    }

    //return react to definr our paddale in breakoutView class
    RectF getRect(){
        return rect;
    }

    //change/set if the paddale is going left or else
    void setMovmentState(int state){
        paddaleMoveing = state;
    }

    //update paddale move
    void update(long fps){
        if(paddaleMoveing == LEFT)
            x = x - paddleSpeed / fps;

        if(paddaleMoveing == RIGHT)
            x = x + paddleSpeed / fps;

        rect.left = x;
        rect.right = x + length;
    }

    void reset(int screenX, int screenY){
        float height = 20;
        x = screenX / 2;
        float y = screenY - 20;
        rect = new RectF(x,y,x+length,y + height);
        rect.offset(-200, -200);
    }
}
