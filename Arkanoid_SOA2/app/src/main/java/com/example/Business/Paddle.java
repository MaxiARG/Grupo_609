package com.example.Business;

import android.graphics.Rect;

public class Paddle {

    private Rect rect;
    private int dx = 6;

    private int ancho;
    private int alto;
    private int left;
    private int top;

    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;

    private int paddleMoving = STOPPED;


    public Paddle(int screenX, int screenY){
        // Tam en pixeles del Paddle
        ancho = 130;
        alto = 20;
        left = 500;
        top = 1400;
       // Rect(int left, int top, int right, int bottom)
        rect = new Rect(left, top, left + ancho, top + alto);
    }

    public Rect getRect(){
        return rect;
    }

    public void setMovementState(int state){
        paddleMoving = state;
    }

    public void update(){

        if(paddleMoving == LEFT) left = left - dx;
        if(paddleMoving == RIGHT) left = left + dx;
        rect.left = left;
        rect.right = left + ancho;
    }

}
