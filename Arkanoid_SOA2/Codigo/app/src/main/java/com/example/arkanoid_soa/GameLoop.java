package com.example.arkanoid_soa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.Business.Ball;
import com.example.Business.Brick;
import com.example.Business.Bullet;
import com.example.Business.GameGlobalData;
import com.example.Business.Paddle;
import com.example.Business.ServicioMusica;
import com.example.servicios.Body_Evento;
import com.example.servicios.Respuesta_RegistrarEvento;
import com.example.servicios.Webservice_UNLAM;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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


    class BreakoutView extends SurfaceView implements Runnable, SensorEventListener {
        SensorManager sensorManager;
        Sensor accelerometer;
        Sensor proximity;
        Point size;
        Paddle paddle=null;
        int max_bullet_count = 5;
        int spawnedBullets =0;
        List<Bullet> bullets = new ArrayList<Bullet>(max_bullet_count);
        float cooldown_counter = 0;

        Thread gameThread = null;
        SurfaceHolder ourHolder;
        boolean paused = true;
        Canvas canvas;
        Paint paint;

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

        int screenWidth;//tamaño de pantalla en pixeles
        int screenHeight;
        Ball ball;

        public BreakoutView(Context context) {

            super(context);

            Display display = getWindowManager().getDefaultDisplay();
            size = new Point();
            display.getSize(size);

            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, sensorManager.SENSOR_DELAY_GAME);
            proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            sensorManager.registerListener(this, proximity, sensorManager.SENSOR_DELAY_GAME);

            ourHolder = getHolder();
            paint = new Paint();
            Point size = new Point();
            display.getSize(size);

            screenWidth = size.x;
            screenHeight = size.y;

            paddle = new Paddle(screenWidth, screenHeight);
            ball = new Ball(screenWidth, screenHeight);

            GameGlobalData.gameIsRunning = true;

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
            //mejorar este desastre
            paint.setColor(Color.argb(255,  123, 212, 111));
            for(int i = 0; i < numBricks; i++){
                if(bricks[i].getVisibility()) {
                    if(i>=0 && i < 5){
                        paint.setColor(Color.argb(255,  204, 71, 72));
                        canvas.drawRect(bricks[i].getRect(), paint);
                    }
                    if(i>=5&& i < 10){
                        paint.setColor(Color.argb(255,  198, 108, 58));
                        canvas.drawRect(bricks[i].getRect(), paint);
                    }
                    if(i>=10 && i < 15){
                        paint.setColor(Color.argb(255,  180, 124, 47));
                        canvas.drawRect(bricks[i].getRect(), paint);
                    }
                    if(i>=15 && i < 20){
                        paint.setColor(Color.argb(255,  162, 162, 42));
                        canvas.drawRect(bricks[i].getRect(), paint);
                    }
                    if(i>=20 && i < 25){
                        paint.setColor(Color.argb(255,  69, 162, 73));
                        canvas.drawRect(bricks[i].getRect(), paint);
                    }
                    if(i>=25 && i < 30){
                        paint.setColor(Color.argb(255,  67, 72, 200));
                        canvas.drawRect(bricks[i].getRect(), paint);
                    }

                }
            }
        }

        private void dibujarBullets() {

            for (Bullet b : bullets){
                if (b.shouldMove()) {
                    paint.setColor(Color.argb(255, 51, 102, 0));
                    canvas.drawRect(b.getRect(), paint);
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

            bullets.clear();
            cooldown_counter = GameGlobalData.bullet_cooldown;
            spawnedBullets=0;
            score = 0;
            lives = 3;
        }

        @Override
        public void run() {

            long initialTime = System.nanoTime();
            final double timeF = 1000000000 / 60;
            double  deltaF = 0;
            long timer = System.currentTimeMillis();

            while (GameGlobalData.gameIsRunning) {

                long currentTime = System.nanoTime();
                deltaF += (currentTime - initialTime) / timeF;
                initialTime = currentTime;

                if (deltaF >= 1 && !paused) {
                    cooldown_counter -= (float)deltaF/100;
                    if(cooldown_counter <= 0.09f)
                        cooldown_counter=0;
                    update((float)deltaF/100);//lo convierto a milisegundos
                    deltaF--;
                }
                draw();
                if (System.currentTimeMillis() - timer > 1000)
                    timer += 1000;
            }

        }

        public void update(float deltaTime) {

            paddle.update();

            ball.stepDX();
            checkear_Colision_BallPared_X(); //Colision Con Pared al moverse en X
            checkear_Colision_Ball_Paddle_X(); //Colision con Paddle al moverse en X
            checkear_Colision_BallBrick_X(); //Colision con Bricks al moverse en X

            ball.stepDY();
            checkear_Colision_BallPared_Y();//Colision Con Pared al moverse en Y
            checkear_Colision_PaddleBall_Y(); //Colision con Paddle al moverse en Y
            checkear_Colision_BallBrick_Y();//Colision con Bricks al moverse en Y


            checkear_Colision_BulletBrick(); //Colision con Bullet-Brick

            moverUnStep_Bullets();
            checkear_Colision_BallSuelo();
            verificar_CondicionDeVictoria();
        }

        private void  checkear_Colision_BulletBrick()  {

            for (Iterator<Bullet> it = bullets.iterator(); it.hasNext();) {
                Bullet b = it.next();
                for(int i = 0; i<numBricks; i++){
                    if (bricks[i].getVisibility() && Rect.intersects(bricks[i].getRect(),  b.getRect())){
                        b.eliminarBullet();
                        bricks[i].setInvisible();
                        soundPool.play(explode_id, 1, 1, 0, 0, 1);
                        score = score + 10;
                    }
                }
            }
        }

        private void moverUnStep_Bullets() {
            for (Bullet b : bullets){
                if(b.shouldMove()){
                    b.stepDY();
                }
            }
        }

        private void verificar_CondicionDeVictoria() {
            if(score == numBricks * 10){ //VERIFICAR ESE NUMERO!
                paused = true;
                soundPool.play(win_id, 1, 1, 0, 0, 1);
                paddle.resetPosition();
                GameGlobalData.guardarEvento(getBaseContext() ,GameGlobalData.fechaHora(), "Fin del Juego. GANASTE\n");
                GameGlobalData.enviarEvento(getBaseContext(), "Fin-Juego", "El Jugador ah GANADO");

                createBricksAndRestart();

                Intent intent = new Intent(this.getContext() , Ganaste_Activity.class);
                startActivity(intent);
            }
        }

        private void checkear_Colision_BallPared_X() {
            if(ball.getRect().left<0 || ball.getRect().right > screenWidth){
                soundPool.play(beep3_id, 1, 1, 0, 0, 1);
                ball.stepBackDX();
                ball.invertirDX();
            }
        }

        private void checkear_Colision_Ball_Paddle_X() {
            if(Rect.intersects(paddle.getRect(), ball.getRect())){
                soundPool.play(beep2_id, 1, 1, 0, 0, 1);
                ball.stepBackDX();
                ball.invertirDX();
                ball.randomizeDY();
            }
        }

        private void checkear_Colision_BallBrick_X() {
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
        }

        private void checkear_Colision_BallPared_Y() {
            if(ball.getRect().top<0 ){
                soundPool.play(beep3_id, 1, 1, 0, 0, 1);
                ball.stepBackDY();
                ball.invertirDY();
            }
        }

        private void checkear_Colision_PaddleBall_Y() {
            if(Rect.intersects(paddle.getRect(), ball.getRect())){
                soundPool.play(beep2_id, 1, 1, 0, 0, 1);
                ball.stepBackDY();
                ball.randomizeDY();
            }
        }

        private void checkear_Colision_BallBrick_Y() {
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
        }

        private void checkear_Colision_BallSuelo() {
            if( ball.getRect().bottom > screenHeight-ball.getRect().height() ){
                lives --;
                ball.stepBackDY();
                ball.invertirDY();
                soundPool.play(lose_life_id, 1, 1, 0, 0, 1);
                paused = true;
                ball.reset();
                paddle.resetPosition();
                GameGlobalData.guardarEvento(getBaseContext() ,GameGlobalData.fechaHora(), "Pierde 1 Vida\n");
                GameGlobalData.enviarEvento(getBaseContext(), "Pierde-Vida", "Pierde 1 Vida");

                verificar_SinVidasRestantes();
            }
        }

        private void verificar_SinVidasRestantes() {
            if(lives == 0){
                paused = true;
                paddle.resetPosition();
                soundPool.play(explode_id, 1, 1, 0, 0, 1);

                GameGlobalData.guardarEvento(getBaseContext() ,GameGlobalData.fechaHora(), "Fin del Juego. Perdio\n");
                GameGlobalData.enviarEvento(getBaseContext(), "Fin-Juego", "El Jugador Perdio Todas Sus Vidas");

                Intent intent = new Intent(this.getContext() , Perdiste_Activity.class );
                startActivity(intent);

                createBricksAndRestart();
            }
        }

        public void draw() {

            if (ourHolder.getSurface().isValid()) {
                canvas = ourHolder.lockCanvas();

                canvas.drawColor(Color.argb(255,  0, 0, 0));//Fondo Negro

                paint.setColor(Color.argb(255,  200, 70, 72));//Color rojo gastado de Atari
                canvas.drawRect(paddle.getRect(), paint);
                canvas.drawRect(ball.getRect(), paint);

                dibujarBricks();
                dibujarScore();
                dibujarBullets();
                dibujarCondicionDeVictoria();


                ourHolder.unlockCanvasAndPost(canvas);
            }

        }

        private void dibujarCondicionDeVictoria() {
            if(score == numBricks * 10){
                paint.setTextSize(90);
                paint.setColor(Color.RED);
                GameGlobalData.guardarEvento(getContext() , GameGlobalData.fechaHora(),"Victoria Ganaste!\n");
                GameGlobalData.enviarEvento(getBaseContext(), "Fin-Juego", "El jugador Ha Ganado");
                canvas.drawText("VICTORIA!", screenWidth / 2,screenHeight / 2, paint);
            }

            if(lives <= 0){
                paint.setTextSize(90);
                paint.setColor(Color.RED);
                GameGlobalData.guardarEvento(getContext() , GameGlobalData.fechaHora(),"Perdiste!\n");
                GameGlobalData.enviarEvento(getBaseContext(), "Fin-Juego", "El jugador Ha Perdido");
                canvas.drawText("PERDISTE!", screenWidth / 2,screenHeight / 2, paint);
            }
    }

        private void dibujarScore() {
            paint.setColor(Color.argb(255,  255, 255, 255));
            paint.setTextSize(40);
            canvas.drawText("Puntaje: " + score + "   Vidas: " + lives+" Disparos: "+ (max_bullet_count - spawnedBullets), 10,50, paint);
        }

        public void pause() {
            GameGlobalData.gameIsRunning = false;

            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Log.e("Error:", "joining thread");
            }

        }

        public void resume() {
            paused = false;
            GameGlobalData.gameIsRunning = true;
            gameThread = new Thread(this);
            gameThread.start();
        }

        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {
            switch (motionEvent.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    paused = false;
            }
            return true;
        }

        @Override
        public void onSensorChanged(SensorEvent event) {

            if( event.sensor.getType() == Sensor.TYPE_PROXIMITY)
            {
                if (event.values[0] <= 0.09f) {
                    if(cooldown_counter <= 0.09f && spawnedBullets<max_bullet_count) {
                        Bullet b = new Bullet(screenWidth, screenHeight, paddle.getRect().left);
                        b.setShouldMove(true);
                        bullets.add(b);
                        spawnedBullets++;
                        cooldown_counter = GameGlobalData.bullet_cooldown;
                    }

                }
            }
            if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                if (paddle != null && event.values[0] > 0.7f && paddle.getRect().left > 0) {//inclinado a izquierda
                    paddle.setMovementState(paddle.LEFT);
                } else if (paddle != null && event.values[0] < -0.7f && (paddle.getRect().left + paddle.getRect().width()) < size.x) {//inclinado a derecha
                    paddle.setMovementState(paddle.RIGHT);
                } else {
                    paddle.setMovementState(paddle.STOPPED);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            return;
        }
    }

}
