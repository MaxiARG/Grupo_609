package com.example.arkanoid_soa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.Business.Ball;
import com.example.Business.Paddle;

public class GameLoop extends AppCompatActivity {

    BreakoutView breakoutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        breakoutView = new BreakoutView(this);
        setContentView(breakoutView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        breakoutView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        breakoutView.pause();
    }

    class BreakoutView extends SurfaceView implements Runnable {

        Thread gameThread = null;
        SurfaceHolder ourHolder;
        volatile boolean playing = true;
        boolean paused = true;
        Canvas canvas;
        Paint paint;
        long fps;
        private long timeThisFrame;
        // The size of the screen in pixels
        int screenX;
        int screenY;

        Paddle paddle;
        Ball ball;

        public BreakoutView(Context context) {
            super(context);
            ourHolder = getHolder();
            paint = new Paint();
            // Get a Display object to access screen details
            Display display = getWindowManager().getDefaultDisplay();
            // Load the resolution into a Point object
            Point size = new Point();
            display.getSize(size);

            screenX = size.x;
            screenY = size.y;

            //los param son para ubicar los dibujos en la pantalla
            paddle = new Paddle(screenX, screenY);
            ball = new Ball(screenX, screenY);


            playing = true;
            createBricksAndRestart();
        }
        public void createBricksAndRestart(){
            ball.reset(screenX, screenY);
        }

        @Override
        public void run() {
          //  System.out.println("Run de GameLoop fuera del While");
            while (playing) {
                //System.out.println("Run de GameLoop DENTRO del While");
                long startFrameTime = System.currentTimeMillis();

                if(!paused){
                //    System.out.println("NO PAUSADO");
                    update();

                }
               // System.out.println("Fuera");
                draw();

                timeThisFrame = System.currentTimeMillis() - startFrameTime;
                if (timeThisFrame >= 1)
                    fps = 1000 / timeThisFrame;


            }

        }


        public void update() {
            paddle.update(fps);
            ball.update(fps);
        }

        public void draw() {
         //   System.out.println("DRAW");

            if (ourHolder.getSurface().isValid()) {
                canvas = ourHolder.lockCanvas();

                canvas.drawColor(Color.argb(255,  26, 128, 182));
                paint.setColor(Color.argb(255,  255, 255, 255));



                // Draw the paddle
                canvas.drawRect(paddle.getRect(), paint);


                // Draw the ball

                canvas.drawRect(ball.getRect(), paint);

                // Draw the bricks

                // Draw the HUD


                ourHolder.unlockCanvasAndPost(canvas);
            }

        }

        // If SimpleGameEngine Activity is paused/stopped
        // shutdown our thread.
        public void pause() {
            playing = false;
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Log.e("Error:", "joining thread");
            }

        }

        // If SimpleGameEngine Activity is started then
        // start our thread.
        public void resume() {
            paused = false;
            playing = true;
            gameThread = new Thread(this);
            gameThread.start();
        }

        // The SurfaceView class implements onTouchListener
        // So we can override this method and detect screen touches.
        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {

           // switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            switch (motionEvent.getActionMasked()) {
                // Player has touched the screen
                case MotionEvent.ACTION_DOWN:

                    paused = false;
                    //System.out.println("DOWNNNNNNNNNNNNNNN");
                    if(motionEvent.getX() > screenX / 2){
                        paddle.setMovementState(paddle.RIGHT);
                        System.out.println(motionEvent.getX()+" "+motionEvent.getY());
                    }
                    else{
                        paddle.setMovementState(paddle.LEFT);
                     //   System.out.println("LEFTTTTTTTTTTTTT");
                    }

                    break;

                // Player has removed finger from screen
                case MotionEvent.ACTION_UP:

                    paddle.setMovementState(paddle.STOPPED);
                    break;
            }
            return true;
        }

    }



}
