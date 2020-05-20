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
import com.example.Business.Brick;
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
        volatile boolean running = true;
        boolean paused = true;
        Canvas canvas;
        Paint paint;
        //
        Brick[] bricks = new Brick[200];
        int numBricks = 0;
        int brickWidth ;
        int brickHeight;

        //
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
            //

            //

            //los param son para ubicar los dibujos en la pantalla
            paddle = new Paddle(screenX, screenY);
            ball = new Ball(screenX, screenY);


            running = true;
            createBricksAndRestart();
        }
        public void createBricksAndRestart(){
            ball.reset(screenX, screenY);

            brickWidth = screenX / 8;
            brickHeight = screenY / 30;
            numBricks = 0;

            for(int column = 0; column < 8; column ++ ){
                for(int row = 0; row < 12; row ++ ){
                    bricks[numBricks] = new Brick(row, column, brickWidth, brickHeight);
                    numBricks ++;
                }
            }
        }

        @Override
        public void run() {

            long initialTime = System.nanoTime();
            final double timeF = 1000000000 / 60;
            double  deltaF = 0;
            int frames = 0, ticks = 0;
            long timer = System.currentTimeMillis();

            while (running) {

                long currentTime = System.nanoTime();
                deltaF += (currentTime - initialTime) / timeF;
                initialTime = currentTime;

                if (deltaF >= 1 && !paused) {
                    update((float)deltaF/1000);
                    frames++;
                    deltaF--;
                }
                draw();

                if (System.currentTimeMillis() - timer > 1000) {
                    System.out.println(String.format("FPS: %s", frames));
                    frames = 0;
                    ticks = 0;
                    timer += 1000;
                }
            }

        }


        public void update(float deltaTime) {
            paddle.update(deltaTime);
            ball.update(deltaTime);
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
                // Change the brush color for drawing

                paint.setColor(Color.argb(255,  123, 212, 111));
                // Draw the bricks if visible
                for(int i = 0; i < numBricks; i++){
                    if(bricks[i].getVisibility()) {

                        canvas.drawRect(bricks[i].getRect(), paint);
                    }
                }
                // Draw the HUD


                ourHolder.unlockCanvasAndPost(canvas);
            }

        }

        // If SimpleGameEngine Activity is paused/stopped
        // shutdown our thread.
        public void pause() {
            running = false;
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
            running = true;
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
