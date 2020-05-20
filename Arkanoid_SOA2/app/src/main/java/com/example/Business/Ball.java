package com.example.Business;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.Random;

public class Ball {

    Rect rect;//sirve para ver colisiones

    //float vX =1f;
   // float vY =-1f;

    int dx = 6; //cuantos pixeles se mueve por tick.
    int dy = 5;

    int ancho = 40;
    int alto = 40;
    int left = 500;
    int top = 1370;

    public Ball(int screenX, int screenY){
        //Rect(int left, int top, int right, int bottom)
        rect = rect = new Rect(left,top, left + ancho, top + alto);

    }

    public void reset(int x, int y){
        rect.left = left;//esto seria posicion X e Y.
        rect.top = top;
        rect.right = left + ancho;
        rect.bottom = top - alto; //aca deberia ser + ???
    }

    public Rect getRect(){
        return rect;
    }

    public void stepDX(){
        rect.left = rect.left + dx;
        rect.right = rect.left + ancho;
    }
    public void stepDY(){
        rect.top = rect.top + dy;
        rect.bottom = rect.top + alto;
    }
    public void stepBackDX(){
        rect.left = rect.left - dx;
        rect.right = rect.left + ancho;
    }

    public void stepBackDY(){
        rect.top = rect.top - dy;
        rect.bottom = rect.top + alto;
    }

    public void invertirDX(){
        dx = -dx;
    }
    public void invertirDY(){
        dy = -dy;
    }

    public void randomizeDY(){
        Random generator = new Random();
        dy = -(generator.nextInt(5)+2);
    }
}
