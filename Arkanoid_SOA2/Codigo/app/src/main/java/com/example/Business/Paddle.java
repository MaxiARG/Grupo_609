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

    private int screenWidth;
    private int screenHeight;
    private int posInicialX;
    private int posInicialY;

    public Paddle(int screenX, int screenY){
        // Tam en pixeles del Paddle
        ancho = 130;
        alto = 20;
        screenHeight=screenY;
        screenWidth = screenX;
        //0.5 es el 50% del ancho de la pantalla
        //0.8 es el 80% del alto. Ambos para posicionar el paddle bien, pensando en pantallas de variadas dimensiones.
        posInicialX = Math.round(screenWidth * 0.5f - ancho/2);;
        posInicialY = Math.round(screenHeight * 0.8f);

        left = posInicialX;
        top = posInicialY;

        rect = rect = new Rect(posInicialX,posInicialY, posInicialX + ancho, posInicialY + alto);
    }

    public void resetPosition(){
        posInicialX = Math.round(screenWidth * 0.5f - ancho/2);;
        posInicialY = Math.round(screenHeight * 0.8f);

        left = posInicialX;
        top = posInicialY;

        rect.left = posInicialX;
        rect.right = posInicialX + ancho;
        rect.top = posInicialY;
        rect.bottom = posInicialY + alto;
    }

    public Rect getRect(){
        return rect;
    }

    public void setMovementState(int state){
        paddleMoving = state;
    }

    public void update(){
        if(paddleMoving == LEFT && left > 0 ) left = left - dx;
        if(paddleMoving == RIGHT && (left+ancho) < screenWidth) left = left + dx;
        rect.left = left;
        rect.right = left + ancho;
    }

}
