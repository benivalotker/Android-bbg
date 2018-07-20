package com.shenkar.nik.bbgame;


import android.graphics.RectF;
import android.widget.TextView;

public class Brick {
    private RectF rect;
    private boolean isVisable;
    public TextView T;

    Brick(int row, int col, int width, int height){
        isVisable = true;
        int padding = 1;
        rect = new RectF(col * width + padding,
                row * height +padding,
                col * width + width - padding,
                row * height + height - padding);

    }


    RectF getRect(){
        return this.rect;
    }

    //on touch the color change
    String setInvisable(){
        if(isVisable == false){
            isVisable = true;
            return "green";
        }
        else{
            isVisable  = false;
            return "red";
        }

    }

    boolean getVisibla(){
        return isVisable;
    }



}
