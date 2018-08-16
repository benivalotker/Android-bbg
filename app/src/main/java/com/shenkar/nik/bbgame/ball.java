package com.shenkar.nik.bbgame;

import android.graphics.RectF;

import java.util.Random;

public class ball {
    private RectF rect;
    private float xVlo;
    private float yVlo;
    private float ballWidth = 10;
    private float ballHeight = 10;

    ball(){
        xVlo = 200;
        yVlo = -400;

        rect = new RectF();
    }

    RectF getRect(){
        return rect;
    }

    void update(long fps){
        rect.left = rect.left + (xVlo  / fps);
        rect.top = rect.top + (yVlo / fps);
        rect.right = rect.left + ballWidth;
        rect.bottom = rect.top + ballHeight;
    }

    void reverseY(){
        yVlo = -yVlo;
    }

    void reversrX(){
        xVlo = -xVlo;
    }

    void setRandomX(){
        Random gen = new Random();
    }

    void clearY(float y){
        rect.bottom = y;
        rect.top = y - ballHeight;
    }

    void clearX(float x){
        rect.left = x;
        rect.right = x + ballWidth;
    }

    void reset(int x, int y){
        rect.left = x / 2;
        rect.top = y - 20;
        rect.right = x/2 + ballWidth;
        rect.bottom = y - 20  - ballHeight;
        rect.offset(-200, -200);
    }
}
