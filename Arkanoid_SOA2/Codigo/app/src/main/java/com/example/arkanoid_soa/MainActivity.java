package com.example.arkanoid_soa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.Business.GameGlobalData;
import com.example.Business.ServicioMusica;
import com.example.servicios.Body_Evento;
import com.example.servicios.Body_Login;
import com.example.servicios.Respuesta_RegistrarEvento;
import com.example.servicios.Respuesta_Webservice;
import com.example.servicios.Webservice_UNLAM;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
public class MainActivity extends AppCompatActivity {
    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        stopService(new Intent(getApplicationContext(), ServicioMusica.class));

    }

    public void registrarse(View view){
        Intent intent = new Intent(this, Registrarse_Activity.class);
        startActivity(intent);
    }

    private boolean validarMailPass(EditText email, EditText password){
            boolean esValido = true;
            String m = email.getText().toString().trim();
            String p = password.getText().toString().trim();
            if(m==null || m.length() == 0 || m.equals("")){
                email.setText("Este Campo No Puede Estar Vacio");
                esValido=false;
            }
            if(p==null || p.length() == 0 || p.equals("")){
                password.setText("Este Campo No Puede Estar Vacio");
                esValido=false;
            }
            if(p!=null && !p.equals("") && p.length() <8 ){
                password.setText("El Password Debe Tener Almenos 8 Caracteres");
                esValido=false;
            }
            //Validar con regex formato de mail.
            return esValido;
    }

    public void ingresar(View view){
        Retrofit retrofit;

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .callTimeout(GameGlobalData.timeout, TimeUnit.SECONDS)
                .readTimeout(GameGlobalData.timeout,TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder().baseUrl(GameGlobalData.urlBase)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient).build();

        Webservice_UNLAM webserviceUNLAM = retrofit.create(Webservice_UNLAM.class);
        Body_Login bl = new Body_Login();
        bl.setEnv("DEV");

        email=(EditText)findViewById(R.id.inputEmail);
        password=(EditText)findViewById(R.id.inputPassword);
        boolean validaOK = validarMailPass(email,password);

        if(validaOK){
            bl.setPassword(password.getText().toString().trim());
            bl.setEmail(email.getText().toString().trim());
            Call<Respuesta_Webservice> llamadoLogin = webserviceUNLAM.llamar_servicio_Login(bl);
            llamadoLogin.enqueue(new Callback<Respuesta_Webservice>(){


                @Override
                public void onResponse(Call<Respuesta_Webservice> call, Response<Respuesta_Webservice> response) {

                    if(response.isSuccessful()){
                        if(response != null && response.body() != null && response.body().getState() != null && response.body().getState().equals("success")){
                           // startService(new Intent(getApplicationContext(), ServicioMusica.class));
                            GameGlobalData.limpiarLogs(getBaseContext());
                            GameGlobalData.token =  response.body().getToken();
                            GameGlobalData.guardarEvento(getBaseContext(),GameGlobalData.fechaHora(),"Login Exitoso\n");
                            GameGlobalData.enviarEvento("LOGIN", "Usuario se loguea al sistema");

                            Intent intent = new Intent( getBaseContext() , MainMenu_Activity.class);
                            startActivity(intent);
                        }
                    }

                        if(response == null || response.body() == null){
                            Intent intent = new Intent(getBaseContext(), ErrorDeAutenticacion.class);
                            startActivity(intent);
                        }

                }
                @Override
                public void onFailure(Call<Respuesta_Webservice> call, Throwable t) {
                    System.out.println(t.getMessage());
                    Toast.makeText(getBaseContext(), "Revise su conexion o vuelva a intentarlo", Toast.LENGTH_LONG).show();
                }
            });
        }else{
            email.setText("");
            email.setHint("Mail invalido");
            password.setText("");
            password.setHint("Password invalido");
            Toast.makeText(getBaseContext(), "Credenciales Incorrectas", Toast.LENGTH_LONG).show();
        }





    }

}
