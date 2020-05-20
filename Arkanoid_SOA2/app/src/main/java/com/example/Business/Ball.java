package com.example.Business;

import android.graphics.RectF;

import java.util.Random;

public class Ball {

    RectF rect;
    float xVelocity=1f;
    float yVelocity=-1f;

    float ballWidth = 40;
    float ballHeight = 40;

    public Ball(int screenX, int screenY){

        // Start the ball travelling straight up at 100 pixels per second
       // xVelocity = 200;
       // yVelocity = -400;
        xVelocity = 500f;
        yVelocity = -500f;

        // Place the ball in the centre of the screen at the bottom
        // Make it a 10 pixel x 10 pixel square
        rect = rect = new RectF(500, 1370, 500 + ballWidth, 1370 + ballHeight);
        //rect = new RectF();

    }



    public void update(float deltaTime){
        System.out.println("DELTA ES: "+ deltaTime);
        rect.left = rect.left + (xVelocity * deltaTime);
        rect.top = rect.top + (yVelocity * deltaTime);
        rect.right = rect.left + ballWidth;
        rect.bottom = rect.top - ballHeight;
        System.out.println(xVelocity+" "+yVelocity);
        System.out.println(rect.left +" "+rect.right);
    }

    public void reverseYVelocity(){
        yVelocity = -yVelocity;
    }

    public void reverseXVelocity(){
        xVelocity = - xVelocity;
    }

    public void setRandomXVelocity(){
       /* Random generator = new Random();
        int answer = generator.nextInt(2);

        if(answer == 0){
            reverseXVelocity();
        }*/
         xVelocity=500;
         yVelocity=-500;
    }

    public void clearObstacleY(float y){
        rect.bottom = y;
        rect.top = y - ballHeight;
    }

    public void clearObstacleX(float x){
        rect.left = x;
        rect.right = x + ballWidth;
    }

    public void reset(int x, int y){
       // rect.left = x / 2;
     //   rect.top = y - 20;
        rect.left = 500;//esto seria posicion X e Y.
        rect.top = 1370;
      //  rect.right = x / 2 + ballWidth;
        //rect.bottom = y - 20 - ballHeight;
        rect.right = 500 + ballWidth;
        rect.bottom = 1370 - ballHeight;
    }

    public RectF getRect(){
        return rect;
    }

}
