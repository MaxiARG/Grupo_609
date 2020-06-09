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
       // registrarInicioDelJuego();
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


    void registrarInicioDelJuego (){

        SharedPreferences sp = getSharedPreferences(GameGlobalData.preferenciasLogs, MODE_PRIVATE);
        SharedPreferences.Editor editorSP = sp.edit();
        // editorSP.clear();
        // editorSP.commit();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
        Date date =  new Date(System.currentTimeMillis() - 3600 * 3000);//resta 3 horas
        String fecha = formatter.format(date);
        String entrada = "Inicia Juego: \n";
        editorSP.putString(fecha, entrada);
        editorSP.apply();
       // enviarRegistroIniciaJuego(entrada);
    }
    void enviarRegistroIniciaJuego(String evento){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
        Date date =  new Date(System.currentTimeMillis() - 3600 * 3000);//resta 3 horas
        String fecha = formatter.format(date);
        Body_Evento be = new Body_Evento();
        be.setEnv("DEV");
        be.setType_events("Inicia-Juego");
        be.setState("Activo");
        be.setDescription(evento+" "+fecha);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .callTimeout(GameGlobalData.timeout, TimeUnit.SECONDS)
                .readTimeout(GameGlobalData.timeout,TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(GameGlobalData.urlBase)
                .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build();

        Webservice_UNLAM webserviceUNLAM = retrofit.create(Webservice_UNLAM.class);

        Call<Respuesta_RegistrarEvento> llamadoEvento = webserviceUNLAM.llamar_servicio_evento(GameGlobalData.token, be);
        llamadoEvento.enqueue(new Callback<Respuesta_RegistrarEvento>(){


            @Override
            public void onResponse(Call<Respuesta_RegistrarEvento> call, Response<Respuesta_RegistrarEvento> response) {
                if(response.isSuccessful()){
                    if(response != null && response.body() != null && response.body().getState() != null && response.body().getState().equals("success")) {
                    System.out.println("ENVIAR EVENTO Inicia Juego DIO SUCCESS");
                    }
                }

                if(!response.isSuccessful()){
                    if(response == null || response.body() == null || response.body().getState().equals("error")) {

                            System.out.println("ENVIAR EVENTO Inicia Juego DIO ERROR");

                    }
                }
            }

            @Override
            public void onFailure(Call<Respuesta_RegistrarEvento> call, Throwable t) {
                //Toast.makeText(getBaseContext(), "Revise su conexion o vuelva a intentarlo", Toast.LENGTH_LONG).show();
                System.out.println("*********************************** Inicia Juego SALIO POR ON FAILURE");
                System.out.println(t.getMessage()+": "+call.toString());
            }

        });
    }


}
