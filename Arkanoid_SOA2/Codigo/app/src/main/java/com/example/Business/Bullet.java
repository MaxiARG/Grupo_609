package com.example.Business;

import android.graphics.Rect;

import java.util.Random;

public class Bullet {

    Rect rect;//sirve para ver colisiones
    int dy = -5;
    // ancho NO es relativo a 'left'
    // sino, es la coord global donde esta la anchura
    int ancho = 40;
    int alto = 40;
    int screenWidth;
    int screenHeight;
    int posInicialX;
    int posInicialY;

    boolean shouldMove = false;

    public Bullet(int screenX, int screenY, float posicionXdelPad){
        //Rect(int left, int top, int right, int bottom)
        screenHeight=screenY;
        screenWidth = screenX;
        posInicialX = Math.round(posicionXdelPad);;
        posInicialY = Math.round(screenHeight * 0.7f);
        rect  = new Rect(posInicialX,posInicialY, posInicialX + ancho, posInicialY + alto);
        shouldMove = true;
    }

    public Rect getRect(){
        return rect;
    }

    public void stepDY(){
        rect.top = rect.top + dy;
        rect.bottom = rect.top + alto;
    }

    public void stepBackDY(){
        rect.top = rect.top - dy;
        rect.bottom = rect.top + alto;
    }
    public void setShouldMove(boolean b){
        shouldMove = b;
    }
    public boolean shouldMove(){return shouldMove;}

    public void eliminarBullet(){
        rect = new Rect(-8000,-8000, 8000 + ancho, 8000 + alto);
        shouldMove = false;
    }
}
