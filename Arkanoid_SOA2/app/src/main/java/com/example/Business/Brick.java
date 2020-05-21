package com.example.Business;

import android.graphics.Rect;
import android.graphics.RectF;

public class Brick {
    private Rect rect;

    private boolean isVisible;

    public Brick(int row, int column, int width, int height){

        isVisible = true;

        int padding = 2;

        rect = new Rect(column * width + padding, row * height + padding,
                column * width + width - padding, row * height + height - padding);
    }

    public Rect getRect(){
        return this.rect;
    }

    public void setInvisible(){
        isVisible = false;
    }

    public boolean getVisibility(){
        return isVisible;
    }
}
