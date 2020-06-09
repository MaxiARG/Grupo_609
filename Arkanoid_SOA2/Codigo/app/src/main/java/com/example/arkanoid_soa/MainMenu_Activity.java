package com.example.arkanoid_soa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.Business.GameGlobalData;
import com.example.servicios.Body_Evento;
import com.example.servicios.Respuesta_RegistrarEvento;
import com.example.servicios.Webservice_UNLAM;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainMenu_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_);

    }
    public void StartGameButton(View view){
        GameGlobalData.guardarEvento(getBaseContext(), GameGlobalData.fechaHora(), "Inicia Juego");
        GameGlobalData.enviarEvento("Inicia-Juego", "El juego ha comenzado");
        Intent intent = new Intent(this, GameLoop.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        return;
    }

    public void abrirRegistroDeEventos(View view){
        Intent intent = new Intent(this, RegistroDeEventos.class);
        startActivity(intent);
    }
}
