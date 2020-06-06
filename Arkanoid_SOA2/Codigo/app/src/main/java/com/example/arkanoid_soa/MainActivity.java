package com.example.arkanoid_soa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Business.Token;
import com.example.servicios.Body_Login;
import com.example.servicios.Respuesta_Webservice;
import com.example.servicios.Webservice_UNLAM;

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
        boolean emailValidaOK = true;
        boolean passwordValidaOK = true;
        Retrofit retrofit;
        HttpLoggingInterceptor loggingInterceptor;
        Builder httpClientBuilder;


        loggingInterceptor = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClientBuilder = new Builder().addInterceptor(loggingInterceptor);
        retrofit = new Retrofit.Builder().baseUrl("http://so-unlam.net.ar/api/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClientBuilder.build())
                .build();
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

                    if(response != null) {
                        if(response != null && response.body() != null && response.body().getState() != null && response.body().getState().equals("success")){
                            System.out.println(response.body().getState());
                            String token = response.body().getToken();
                            Token.token = token;
                            System.out.println( Token.token);

                            Intent intent = new Intent( getBaseContext() , MainMenu_Activity.class);
                            startActivity(intent);
                        }

                    }else
                        Toast.makeText(getBaseContext(), "Revise sus credenciales", Toast.LENGTH_LONG);
                }

                @Override
                public void onFailure(Call<Respuesta_Webservice> call, Throwable t) {
                    //se corta internet, no puede convertir, etc.
                    Log.d("TAG1","ERROR: "+ t.getMessage());
                    Toast.makeText(getBaseContext(), "Revise su conexion o vuelva a intentarlo", Toast.LENGTH_LONG);
                }
            });
        }else{
            email.setText("");
            email.setHint("Mail invalido");
            password.setText("");
            password.setHint("Password invalido");
            passwordValidaOK=true;
            emailValidaOK = true;
            Toast.makeText(getBaseContext(), "Credenciales Incorrectas", Toast.LENGTH_LONG);
        }





    }

}
