package com.example.arkanoid_soa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.Business.Ball;
import com.example.Business.Brick;
import com.example.Business.Paddle;

import java.io.IOException;

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
        //Rect(int left, int top, int right, int bottom)
        Rect testRect = new Rect(10,1100,1200,1180);

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

        SoundPool soundPool;
        int beep1ID = -1;
        int beep2ID = -1;
        int beep3ID = -1;
        int loseLifeID = -1;
        int explodeID = -1;

        int score = 0;
        int lives = 3;

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
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

            screenX = size.x;
            screenY = size.y;

            //los param son para ubicar los dibujos en la pantalla
            //renombrar los params por screenWidth y screenSize
            paddle = new Paddle(screenX, screenY);
            ball = new Ball(screenX, screenY);


            running = true;

            cargarSonidos(context);
          //  createBricksAndRestart();
        }

        private void cargarSonidos(Context context) {
            // This SoundPool is deprecated but don't worry
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);

            try{
                // Create objects of the 2 required classes
                AssetManager assetManager = context.getAssets();
                AssetFileDescriptor descriptor;

                // Load our fx in memory ready for use
                descriptor = assetManager.openFd("beep1.ogg");
                beep1ID = soundPool.load(descriptor, 0);

                descriptor = assetManager.openFd("beep2.ogg");
                beep2ID = soundPool.load(descriptor, 0);

                descriptor = assetManager.openFd("beep3.ogg");
                beep3ID = soundPool.load(descriptor, 0);

                descriptor = assetManager.openFd("loseLife.ogg");
                loseLifeID = soundPool.load(descriptor, 0);

                descriptor = assetManager.openFd("explode.ogg");
                explodeID = soundPool.load(descriptor, 0);

            }catch(IOException e){
                // Print an error message to the console
                Log.e("error", "failed to load sound files");
            }
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
            if (lives == 0) {
                score = 0;
                lives = 3;
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
                    update((float)deltaF/1000);//lo convierto a milisegundos
                    frames++;
                    deltaF--;
                }
                draw();

                if (System.currentTimeMillis() - timer > 1000) {
                    //System.out.println(String.format("FPS: %s", frames));
                    frames = 0;
                    ticks = 0;
                    timer += 1000;
                }
            }

        }


        public void update(float deltaTime) {
            paddle.update();
            //ball.update(deltaTime);

          //  colisionConBricks();

           // colisionConPaddle();

          //  colisionContraPiso();

           // colisionParedes();

            ball.stepDX();
            if(ball.getRect().left<0 || ball.getRect().right > screenX ){
                ball.stepBackDX();
                ball.invertirDX();
            }
            if(Rect.intersects(testRect, ball.getRect())){
                System.out.println("Colision detectada en DX");
                ball.stepBackDX();
                ball.invertirDX();
            }
            if(Rect.intersects(paddle.getRect(), ball.getRect())){
                System.out.println("Colision detectada en DX Paddle");
                ball.stepBackDX();
                ball.invertirDX();
            }

            ball.stepDY();
            if(ball.getRect().top<0 || ball.getRect().bottom > screenY + 10 ){
                ball.stepBackDY();
                ball.invertirDY();
            }
            if(Rect.intersects(testRect, ball.getRect())){
                System.out.println("Colision detectada en DY");
                ball.stepBackDY();
                ball.invertirDY();
            }
            if(Rect.intersects(paddle.getRect(), ball.getRect())){
                System.out.println("Colision detectada en DY Paddle");
                ball.stepBackDY();
                ball.invertirDY();
            }


            // Pause if cleared screen
           /* if(score == numBricks * 10){ //VERIFICAR ESE NUMERO!
                paused = true;
                createBricksAndRestart();
            }*/
        }

      /*  private void colisionParedes() {
            // Bounce the ball back when it hits the top of screen
            if(ball.getRect().top < 0){
                ball.invertirVelocidadY();
                ball.clearObstacleY(12);
              //  soundPool.play(beep2ID, 1, 1, 0, 0, 1);
            }

            // If the ball hits left wall bounce
            if(ball.getRect().left < 0){
                ball.invertirVelocidadX();
                ball.clearObstacleX(2);
               // soundPool.play(beep3ID, 1, 1, 0, 0, 1);
            }

            // If the ball hits right wall bounce
            if(ball.getRect().right > screenX - 10){
                ball.invertirVelocidadX();
                ball.clearObstacleX(screenX - 22);
              //  soundPool.play(beep3ID, 1, 1, 0, 0, 1);
            }
        }*/

        /*private void colisionContraPiso() {
            if(ball.getRect().bottom > screenY){
                ball.invertirVelocidadY();
                ball.clearObstacleY(screenY - 2);
               // lives --;
                // soundPool.play(loseLifeID, 1, 1, 0, 0, 1);

                if(lives == 0){
                    paused = true;
                   // createBricksAndRestart();
                }

            }
        }*/

        /*private void colisionConPaddle() {
            // Check for ball colliding with paddle
            if(RectF.intersects(paddle.getRect(),ball.getRect())) {
                ball.setRandomXVelocity();
                ball.invertirVelocidadY();
                ball.clearObstacleY(paddle.getRect().top - 2);
             //   soundPool.play(beep1ID, 1, 1, 0, 0, 1);
            }
        }*/

       /* private void colisionConBricks() {
            // Check for ball colliding with a brick
            for(int i = 0; i < numBricks; i++){

                if (bricks[i].getVisibility()){

                    if(RectF.intersects(bricks[i].getRect(),ball.getRect())) {
                        bricks[i].setInvisible();
                        ball.invertirVelocidadY();
                        score = score + 10;
                        //soundPool.play(explodeID, 1, 1, 0, 0, 1);
                    }
                }
            }
        }*/

        public void draw() {

            if (ourHolder.getSurface().isValid()) {
                canvas = ourHolder.lockCanvas();

                canvas.drawColor(Color.argb(255,  26, 128, 182));
                paint.setColor(Color.argb(255,  255, 255, 255));

                canvas.drawRect(paddle.getRect(), paint);
                canvas.drawRect(ball.getRect(), paint);

                canvas.drawRect(testRect, paint);

                //dibujarBricks();
                dibujarScore();
                dibujarCondicionDeVictoria();

                ourHolder.unlockCanvasAndPost(canvas);
            }

        }

        private void dibujarCondicionDeVictoria() {
            // Has the player cleared the screen?
            if(score == numBricks * 10){
               // paint.setTextSize(90);
               // canvas.drawText("YOU HAVE WON!", 10,screenY/2, paint);
            }

            // Has the player lost?
            if(lives <= 0){
              //  paint.setTextSize(90);
               // canvas.drawText("YOU HAVE LOST!", 10,screenY/2, paint);
            }
        }
        public boolean col(Rect a, Rect b){

            if(a.left < b.left + b.width() &&
                    a.left + a.width() > b.left &&
                    a.top < b.top + b.height() &&
                    a.top + a.height() > b.top)
            {
                System.out.println("Collision Detected");
                return true;
            }
            //System.out.println("NOOOOOOOOOOO Collision Detected");
            return false;
        }
        public boolean intersectan(Rect a, Rect b) {
            if(a.left > (b.left+ b.width())) {
                return false;
            }
            if(a.left+a.width() < b.left) {
                return false;
            }
            if(a.top > (b.top + b.height())) {
                return false;
            }
            if(a.top+a.height() < b.top){
                return false;
            }
            return true;
        }


        private void dibujarScore() {
            // Draw the HUD
            // Choose the brush color for drawing
            paint.setColor(Color.argb(255,  255, 255, 255));

            // Draw the score
            paint.setTextSize(40);
            canvas.drawText("Score: " + score + "   Lives: " + lives, 10,50, paint);
        }

        private void dibujarBricks() {
            paint.setColor(Color.argb(255,  123, 212, 111));

            for(int i = 0; i < numBricks; i++){
                if(bricks[i].getVisibility()) {

                    canvas.drawRect(bricks[i].getRect(), paint);
                }
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

        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {

           // switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            switch (motionEvent.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    paused = false;
                    if(motionEvent.getX() > screenX / 2){
                        paddle.setMovementState(paddle.RIGHT);
                    }
                    else{
                        paddle.setMovementState(paddle.LEFT);
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    paddle.setMovementState(paddle.STOPPED);
                    break;
            }
            return true;
        }

    }





}
