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

    public void ingresar(View view){

        boolean emailValidaOK = true;
        boolean passwordValidaOK = true;
        Retrofit retrofit;
        HttpLoggingInterceptor loggingInterceptor;
        Builder httpClientBuilder;
        Gson miGsonConverter = new GsonBuilder().disableHtmlEscaping().create();


        loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .callTimeout(GameGlobalData.timeout, TimeUnit.SECONDS)
                .readTimeout(GameGlobalData.timeout,TimeUnit.SECONDS)
                .build();


        httpClientBuilder = new Builder().addInterceptor(loggingInterceptor);
        retrofit = new Retrofit.Builder().baseUrl(GameGlobalData.urlBase)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient).build();
                //.client(httpClientBuilder.build()).build();

        Webservice_UNLAM webserviceUNLAM = retrofit.create(Webservice_UNLAM.class);
        Body_Login bl = new Body_Login();

        EditText email=(EditText)findViewById(R.id.inputEmail);
        EditText password=(EditText)findViewById(R.id.inputPassword);
        if(email.getText() != null && email.getText().toString()!=null && email.getText().toString().length() > 5){
            //validarEmail
            bl.setEmail(email.getText().toString());
        }else{
            email.setText("");
            email.setHint("Mail invalido");
            emailValidaOK = false;
        }
        bl.setEnv("DEV");
        if(password.getText() != null && password.getText().toString()!=null && password.getText().toString().trim().length() > 8){
            bl.setPassword(password.getText().toString().trim());
            bl.setEmail(email.getText().toString());
        }else{
            password.setText("");
            password.setHint("Password invalido");
            passwordValidaOK = false;
        }
        boolean validaOK = (emailValidaOK && passwordValidaOK);

        if(validaOK){
            Call<Respuesta_Webservice> llamadoLogin = webserviceUNLAM.llamar_servicio_Login(bl);
            llamadoLogin.enqueue(new Callback<Respuesta_Webservice>(){


                @Override
                public void onResponse(Call<Respuesta_Webservice> call, Response<Respuesta_Webservice> response) {

                    if(response.isSuccessful()){
                  //  if(response != null) {
                      //  if(response != null && response.body() != null && response.body().getState() != null && response.body().getState().equals("success")){
                           // startService(new Intent(getApplicationContext(), ServicioMusica.class));
                            String token = response.body().getToken();
                            GameGlobalData.token = token;
                            System.out.println("XXXXXXXXXXXXXXX:  "+token);
                            registrarLogin();//guarda el evento
                            enviarRegistroLoginAServidor();

                            Intent intent = new Intent( getBaseContext() , MainMenu_Activity.class);
                            startActivity(intent);
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

                void registrarLogin (){

                    SharedPreferences sp = getSharedPreferences(GameGlobalData.preferenciasLogs, MODE_PRIVATE);
                     SharedPreferences.Editor editorSP = sp.edit();
                     if(sp.getAll().size()>GameGlobalData.Cantidad_Maxima_De_Registros){
                         editorSP.clear();//Borro logs para mantener archivo de registros pequeño.
                     }
                     if(GameGlobalData.limpiarLogs) {
                         editorSP.clear();
                         editorSP.commit();
                     }
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
                    Date date =  new Date(System.currentTimeMillis() - 3600 * 3000);//resta 3 horas
                    String fecha = formatter.format(date);
                    String entrada = "Login exitoso\n";

                    editorSP.putString(fecha, entrada);
                    editorSP.apply();
                }
                void enviarRegistroLoginAServidor(){
                    Body_Evento be = new Body_Evento();
                    be.setEnv("DEV");
                    be.setType_events("Login");
                    be.setState("Activo");
                    be.setDescription("Usuario Ingreso Correctamente");

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
                            System.out.println("RESPONESE REGISTRAREVENTO");
                            //System.out.println(response.body().toString());
                            if(response.isSuccessful()){
                            //if(response != null && response.body() != null && response.body().getState() != null && response.body().getState().equals("success")){
                                System.out.println("ENVIAR EVENTO LOGIN DIO SUCCESS");
                            }

                            if(!response.isSuccessful()){
                            //if(response == null || response.body() == null || response.body().getState().equals("error")){
                                if(response.body().getState().equals("error")) {
                                    System.out.println("ENVIAR EVENTO LOGIN DIO ERROR");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Respuesta_RegistrarEvento> call, Throwable t) {
                            //Toast.makeText(getBaseContext(), "Revise su conexion o vuelva a intentarlo", Toast.LENGTH_LONG).show();
                            System.out.println("*********************************** SALIO POR ON FAILURE");
                            System.out.println(t.getMessage()+": "+call.toString());
                        }

                    });
                }
            });
        }else{
            email.setText("");
            email.setHint("Mail invalido");
            password.setText("");
            password.setHint("Password invalido");
            passwordValidaOK=true;
            emailValidaOK = true;
            Toast.makeText(getBaseContext(), "Credenciales Incorrectas", Toast.LENGTH_LONG).show();
        }





    }

}
