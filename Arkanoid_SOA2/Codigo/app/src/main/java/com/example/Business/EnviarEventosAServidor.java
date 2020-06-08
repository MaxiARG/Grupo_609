package com.example.Business;

import com.example.servicios.Body_Evento;
import com.example.servicios.Respuesta_RegistrarEvento;
import com.example.servicios.Webservice_UNLAM;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EnviarEventosAServidor implements Runnable {

    Body_Evento bodyEvento;


    public EnviarEventosAServidor(Body_Evento body){
        bodyEvento = body;
    }

    @Override
    public void run() {
        Retrofit retrofit;
        HttpLoggingInterceptor loggingInterceptor;
        OkHttpClient.Builder httpClientBuilder;


        loggingInterceptor = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClientBuilder = new OkHttpClient.Builder().addInterceptor(loggingInterceptor);
        retrofit = new Retrofit.Builder().baseUrl(GameGlobalData.urlBase)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClientBuilder.build())
                .build();
        Webservice_UNLAM webserviceUNLAM = retrofit.create(Webservice_UNLAM.class);
        Body_Evento be = new Body_Evento();

        Call<Respuesta_RegistrarEvento> llamadoEvento = webserviceUNLAM.llamar_servicio_evento(be,GameGlobalData.token);

        llamadoEvento.enqueue(new Callback<Respuesta_RegistrarEvento>(){


            @Override
            public void onResponse(Call<Respuesta_RegistrarEvento> call, Response<Respuesta_RegistrarEvento> response) {

                if(response != null) {
                    if(response != null && response.body() != null && response.body().getState() != null && response.body().getState().equals("success")){
                    }

                    if(response == null || response.body() == null){

                    }

                }
            }

            @Override
            public void onFailure(Call<Respuesta_RegistrarEvento> call, Throwable t) {
                //Toast.makeText(getBaseContext(), "Revise su conexion o vuelva a intentarlo", Toast.LENGTH_LONG).show();
            }

        });
    }
}
