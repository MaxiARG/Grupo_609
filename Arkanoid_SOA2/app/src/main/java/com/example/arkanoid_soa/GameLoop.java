package com.example.arkanoid_soa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.example.Business.Ball;
import com.example.Business.Brick;
import com.example.Business.Paddle;

import java.io.IOException;

public class GameLoop extends AppCompatActivity implements SensorEventListener {

    BreakoutView breakoutView;
    SensorManager sensorManager;
    Sensor accelerometer;
    float xSensor=0;
    Point size;
    Paddle paddle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        breakoutView = new BreakoutView(this);
        setContentView(breakoutView);

        Display display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(GameLoop.this, accelerometer, sensorManager.SENSOR_DELAY_GAME);
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        System.out.println("X: "+event.values[0]);
        float ventanaDeteccion = 1;
        if( paddle != null && event.values[0] > 0.7f && paddle.getRect().left > 0){//inclinado a izquierda
            paddle.setMovementState(paddle.LEFT);
        }else
        if( paddle != null && event.values[0] < -0.7f && (paddle.getRect().left+paddle.getRect().width()) < size.x){//inclinado a derecha
            paddle.setMovementState(paddle.RIGHT);
        }else {
            paddle.setMovementState(paddle.STOPPED);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    class BreakoutView extends SurfaceView implements Runnable {
        //Rect(int left, int top, int right, int bottom)
       // Rect testRect = new Rect(10,1100,1200,1180);

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
        int alturaDeCadaBrick=75;

        SoundPool soundPool;
        int beep1_id = -1;
        int beep2_id = -1;
        int beep3_id = -1;
        int lose_life_id = -1;
        int explode_id = -1;
        int win_id = -1;

        int score = 0;
        int lives = 3;
        int filas = 6;
        int columnas = 5;

        //
        long fps;
        private long timeThisFrame;
        // The size of the screen in pixels
        int screenWidth;
        int screenHeight;


        Ball ball;

        private SensorManager sensorManager;
        double ax,ay,az;


        public BreakoutView(Context context) {

            super(context);
            ourHolder = getHolder();
            paint = new Paint();
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

            screenWidth = size.x;
            screenHeight = size.y;

            paddle = new Paddle(screenWidth, screenHeight);
            ball = new Ball(screenWidth, screenHeight);


            running = true;

            cargarSonidos(context);
            createBricksAndRestart();
        }

        private void cargarSonidos(Context context) {
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
            beep1_id = soundPool.load(context, R.raw.beep1, 1);
            beep2_id = soundPool.load(context, R.raw.beep2, 1);
            beep3_id = soundPool.load(context, R.raw.beep3, 1);
            lose_life_id = soundPool.load(context, R.raw.lose_life, 1);
            explode_id = soundPool.load(context, R.raw.explode, 1);
            win_id = soundPool.load(context, R.raw.win, 1);
        }

        private void dibujarBricks() {
            paint.setColor(Color.argb(255,  123, 212, 111));
            for(int i = 0; i < numBricks; i++){
                if(bricks[i].getVisibility()) {
                    canvas.drawRect(bricks[i].getRect(), paint);
                }
            }
        }

        public void createBricksAndRestart(){
            ball.reset();

            Display display = getWindowManager().getDefaultDisplay();
            Point screenSize = new Point();
            display.getSize(screenSize);

            brickWidth = screenSize.x / columnas; //Columnas
            numBricks = 0;

            for(int column = 0; column < columnas; column ++ ){
                for(int row = 0; row < filas; row ++ ){
                    bricks[numBricks] = new Brick(row, column,  brickWidth, alturaDeCadaBrick);
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

            ball.stepDX();
            //Colision Con Pared al moverse en X
            if(ball.getRect().left<0 || ball.getRect().right > screenWidth){
                soundPool.play(beep3_id, 1, 1, 0, 0, 1);
                ball.stepBackDX();
                ball.invertirDX();
            }
            //Colision con Paddle al moverse en X
            if(Rect.intersects(paddle.getRect(), ball.getRect())){
                soundPool.play(beep2_id, 1, 1, 0, 0, 1);
                ball.stepBackDX();
                ball.randomizeDY();
            }
            //Colision con Bricks al moverse en X
            for(int i = 0; i < numBricks; i++){
                if (bricks[i].getVisibility()){
                    if(Rect.intersects(bricks[i].getRect(), ball.getRect())) {
                        soundPool.play(explode_id, 1, 1, 0, 0, 1);
                        bricks[i].setInvisible();
                        ball.stepBackDX();
                        ball.invertirDX();
                        score = score + 10;
                    }
                }
            }

            ball.stepDY();
            //Colision Con Pared al moverse en Y
            if(ball.getRect().top<0 ){
                soundPool.play(beep3_id, 1, 1, 0, 0, 1);
                ball.stepBackDY();
                ball.invertirDY();
            }
            //Colision con Paddle al moverse en Y
            if(Rect.intersects(paddle.getRect(), ball.getRect())){
                soundPool.play(beep2_id, 1, 1, 0, 0, 1);
                ball.stepBackDY();
                ball.randomizeDY();
            }
            //Colision con Bricks al moverse en Y
            for(int i = 0; i < numBricks; i++){
                if (bricks[i].getVisibility()){

                    if(Rect.intersects(bricks[i].getRect(), ball.getRect())) {
                        soundPool.play(explode_id, 1, 1, 0, 0, 1);
                        bricks[i].setInvisible();
                        ball.stepBackDY();
                        ball.invertirDY();
                        score = score + 10;
                    }
                }
            }

            //Ball choca contra el suelo
            if( ball.getRect().bottom > screenHeight-ball.getRect().height() ){
                lives --;
                ball.stepBackDY();
                ball.invertirDY();
                soundPool.play(lose_life_id, 1, 1, 0, 0, 1);
                paused = true;
                ball.reset();
                paddle.resetPosition();

                if(lives == 0){
                    paused = true;
                    paddle.resetPosition();
                    soundPool.play(explode_id, 1, 1, 0, 0, 1);
                    Intent intent = new Intent(this.getContext() , Perdiste_Activity.class );
                    startActivity(intent);
                    createBricksAndRestart();
                }
            }

            //Verifica condicion de victoria
            if(score == numBricks * 10){ //VERIFICAR ESE NUMERO!
                paused = true;
                soundPool.play(win_id, 1, 1, 0, 0, 1);
                paddle.resetPosition();
                createBricksAndRestart();
                Intent intent = new Intent(this.getContext() , Ganaste_Activity.class);
                startActivity(intent);
            }
        }
        public void draw() {

            if (ourHolder.getSurface().isValid()) {
                canvas = ourHolder.lockCanvas();

                canvas.drawColor(Color.argb(255,  26, 128, 182));
                paint.setColor(Color.argb(255,  255, 255, 255));

                canvas.drawRect(paddle.getRect(), paint);
                canvas.drawRect(ball.getRect(), paint);

                dibujarBricks();
                dibujarScore();
                dibujarCondicionDeVictoria();

                ourHolder.unlockCanvasAndPost(canvas);
            }

        }

        private void dibujarCondicionDeVictoria() {

            if(score == numBricks * 10){
                paint.setTextSize(90);
                paint.setColor(Color.RED);
                canvas.drawText("VICTORIA!", screenWidth / 2,screenHeight / 2, paint);
            }

            if(lives <= 0){
                paint.setTextSize(90);
                paint.setColor(Color.RED);
                canvas.drawText("PERDISTE!", screenWidth / 2,screenHeight / 2, paint);
            }
    }

        private void dibujarScore() {
            paint.setColor(Color.argb(255,  255, 255, 255));
            paint.setTextSize(40);
            canvas.drawText("Score: " + score + "   Lives: " + lives, 10,50, paint);
        }

        public void pause() {
            running = false;
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Log.e("Error:", "joining thread");
            }

        }

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
                   // if(motionEvent.getX() > screenWidth / 2 && paused==false)
                 //       paddle.setMovementState(paddle.RIGHT);
                   // if(motionEvent.getX() < screenWidth / 2 && paused==false)
                   //     paddle.setMovementState(paddle.LEFT);
                   // break;
             //   case MotionEvent.ACTION_UP:
                   // paddle.setMovementState(paddle.STOPPED);
                  //  break;
            }
            return true;
        }


    }

}
