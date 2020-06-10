package com.example.Business;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.servicios.Body_Evento;
import com.example.servicios.Respuesta_RegistrarEvento;
import com.example.servicios.Webservice_UNLAM;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GameGlobalData {

    public static String token;
    public final static String preferenciasLogs = "preferenciasLogs";
    public static boolean gameIsRunning = false;
    public final static int Cantidad_Maxima_De_Registros=90;
    public final static String urlBase="http://so-unlam.net.ar/api/api/";
    public final static int timeout =950;
    public final static int bullet_cooldown = 2; //seg

    public static String fechaHora(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
        Date date =  new Date(System.currentTimeMillis() - 3600 * 3000);//resta 3 horas
        String fecha = formatter.format(date);
        return fecha;
    }

    public static void guardarEvento(Context context, String key, String value){
        SharedPreferences sp = context.getSharedPreferences(GameGlobalData.preferenciasLogs, context.MODE_PRIVATE);
        SharedPreferences.Editor editorSP = sp.edit();

        if(sp.getAll().size()>=Cantidad_Maxima_De_Registros){
            editorSP.clear();
            editorSP.apply();
        }

        editorSP.putString(key, value);
        editorSP.apply();
    }

    public static void limpiarLogs(Context context){
        SharedPreferences sp = context.getSharedPreferences(GameGlobalData.preferenciasLogs, context.MODE_PRIVATE);
        SharedPreferences.Editor editorSP = sp.edit();
        editorSP.clear();
        editorSP.apply();
    }

    public static void enviarEvento(Context context, String tipoEvento, String descripcion){
        //El TOKEN ya esta escrito abajo. No se necesita pasar como parametro.
        Body_Evento be = new Body_Evento();
        be.setEnv("DEV");
        be.setType_events(tipoEvento);
        be.setState("Activo");
        be.setDescription(descripcion);


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
                if(!response.isSuccessful()){
                    System.out.println("FRACASO WS: "+ tipoEvento+" "+descripcion);
                    GameGlobalData.guardarEvento(context ,"Repuesta 404: "+tipoEvento , descripcion);
                }
            }

            @Override
            public void onFailure(Call<Respuesta_RegistrarEvento> call, Throwable t) {
                GameGlobalData.guardarEvento(context ,"Fallo Evento: "+tipoEvento , "Sin Conexion");
                Toast.makeText(context,"Verifique su conexion a internet",Toast.LENGTH_LONG).show();
            }

        });
    }
}
