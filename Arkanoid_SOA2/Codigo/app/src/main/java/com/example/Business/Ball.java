package com.example.Business;

import android.graphics.Rect;
import java.util.Random;

public class Ball {

    Rect rect;//sirve para ver colisiones

    int dx = 6; //cuantos pixeles se mueve por tick.
    int dy = -5;
    // ancho NO es relativo a 'left'
    // sino, es la coord global donde esta la anchura
    int ancho = 40;
    int alto = 40;
    int left = 500;
    int top = 1270;

    int screenWidth;
    int screenHeight;
    int posInicialX;
    int posInicialY;

    public Ball(int screenX, int screenY){
        //Rect(int left, int top, int right, int bottom)
        screenHeight=screenY;
        screenWidth = screenX;
        posInicialX = Math.round(screenWidth * 0.5f);;
        posInicialY = Math.round(screenHeight * 0.7f);
        rect = rect = new Rect(posInicialX,posInicialY, posInicialX + ancho, posInicialY + alto);

    }

    public void reset(){
        dy = -5;
        rect.left = posInicialX;
        rect.top = posInicialY;
        rect.right = posInicialX + ancho;
        rect.bottom = posInicialY + alto;
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
        dy = -dy;
        if(dx>0) dx = (generator.nextInt(5)+2);
        if(dx<0) dx = -(generator.nextInt(5)+2);

    }
}
