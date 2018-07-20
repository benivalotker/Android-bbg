package com.shenkar.nik.bbgame;
import android.graphics.RectF;
import android.widget.TextView;

public class Brick {
    private RectF rect;
    private boolean isVisible;

    Brick(int row, int col, int width, int height){
        isVisible = true;
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
        if(isVisible == false){
            isVisible = true;
            return "green";
        }
        else{
            isVisible  = false;
            return "red";
        }

    }

    //returns if the brick is visible
    boolean getVisibla(){
        return isVisible;
    }

}
