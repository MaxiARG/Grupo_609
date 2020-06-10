package com.example.Business;

import android.graphics.Rect;

public class Bullet {

    private Rect rect;
    private int dy = -3;
    private int ancho = 40;
    private int alto = 40;

    private boolean shouldMove = false;

    public Bullet(int screenX, int screenY, float posicionXdelPad){
        //Rect(int left, int top, int right, int bottom)
        int posInicialX = Math.round(posicionXdelPad);
        int posInicialY = Math.round(screenY * 0.7f);
        rect  = new Rect(posInicialX, posInicialY, posInicialX + ancho, posInicialY + alto);
        shouldMove = true;
    }

    public Rect getRect(){
        return rect;
    }
    public void stepDY(){
        rect.top = rect.top + dy;
        rect.bottom = rect.top + alto;
    }
    public void setShouldMove(boolean b){
        shouldMove = b;
    }
    public boolean shouldMove(){return shouldMove;}
    public void eliminarBullet(){
        shouldMove = false;
        rect = new Rect(-8000,-8000, -8000 + ancho, -8000 + alto);
        dy = 0;
    }
}
