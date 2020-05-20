package com.example.Business;

import android.graphics.RectF;

public class Paddle {

    // Guarda 4 coordenadas
    private RectF rect;

    private float length;
    private float height;

    //Coord del lado izquierdo del Paddle
    private float x;

    // Coord de arriba del Paddle
    private float y;

    // En pixeles por segundo
    private float paddleSpeed;

    // Indica las direcciones de movimiento
    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;

    private int paddleMoving = STOPPED;


    public Paddle(int screenX, int screenY){
        // Tam en pixeles del Paddle
        length = 130;
        height = 20;


        // Pone la posicion en el medio
        //x = screenX / 2;
       // y = screenY - 20;
        x = 500;
        y = 1400;

        //Con esto se hacen las colisiones
        rect = new RectF(x, y, x + length, y + height);

        paddleSpeed = 350;
    }

    //Lo voy a llamar desde la clase GameLoop
    public RectF getRect(){
        return rect;
    }

    //Cambia en que dir se mueve
    public void setMovementState(int state){
        paddleMoving = state;
    }

    //Mover y actualizar rec, llamado desde GameLoop
    public void update(long fps){

        if(paddleMoving == LEFT){
            x = x - paddleSpeed / fps;
        }

        if(paddleMoving == RIGHT){
            x = x + paddleSpeed / fps;
        }

        rect.left = x;
        rect.right = x + length;
       // System.out.println(rect.left+" "+rect.right+" "+x);
    }
}
