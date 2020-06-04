package com.example.arkanoid_soa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.servicios.Data;
import com.example.servicios.ServicioLogin;

import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void registrarse(View view){
        Intent intent = new Intent(this, Registrarse_Activity.class);
        startActivity(intent);
    }

    public void ingresar(View view){
       // Intent intent = new Intent(this, MainMenu_Activity.class);
        //startActivity(intent);
        Retrofit retrofit;
        HttpLoggingInterceptor loggingInterceptor;
        Builder httpClientBuilder;


        loggingInterceptor = new HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder = new Builder().addInterceptor(loggingInterceptor);
        retrofit = new Retrofit.Builder().baseUrl("https://swapi.dev/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClientBuilder.build())
                .build();
        ServicioLogin client = retrofit.create(ServicioLogin.class);
        Call<Data> call = client.getPersonaje();

        call.enqueue(new Callback<Data>(){


            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
               // System.out.println(response.message());

                System.out.println(response.body().getPersonaje().getName());
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                //se corta internet, no puede convertir, etc.
                Log.d("TAG1","ERROR: "+ t.getMessage());
            }
        });



    }

}
