package com.example.Business;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.IBinder;
import android.provider.Settings;

import androidx.annotation.Nullable;

import com.example.arkanoid_soa.R;

public class ServicioMusica extends Service {

  //  SoundPool soundPool;// = new SoundPool(1,AudioManager.STREAM_MUSIC,0);
    //int sonidoCandy = -1;

    MediaPlayer player;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
      //  return super.onStartCommand(intent, flags, startId);
        player = MediaPlayer.create(this, R.raw.candy2 );
        player.setLooping(true);
        player.start();
        //mediaPlayer.start(); // no need to call prepare(); create() does that for you

       // soundPool = new SoundPool(1,AudioManager.STREAM_MUSIC,0);
      //  sonidoCandy = soundPool.load(this, R.raw.candy, 1);
      //  soundPool.play(sonidoCandy, 1, 1, 0, 1, 1);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.stop();
      //  soundPool.stop(sonidoCandy);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
