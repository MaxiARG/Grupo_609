package com.example.arkanoid_soa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Business.GameGlobalData;
import com.example.servicios.Body_registrarse;
import com.example.servicios.Respuesta_Webservice;
import com.example.servicios.Webservice_UNLAM;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Registrarse_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse_);
        TextView info = (TextView) findViewById(R.id.info);
        info.setText("");
    }

    //Hace el webService y espera una respuesta
    public void solicitarRegistro(View view) {

        boolean todoOK = true;
        EditText inputNombre = (EditText) findViewById(R.id.inputNombre);
        EditText inputApellido = (EditText) findViewById(R.id.inputApellido);
        EditText inputEmail = (EditText) findViewById(R.id.inputEmail);
        EditText inputPassword = (EditText) findViewById(R.id.inputPassword);
        EditText inputComision = (EditText) findViewById(R.id.inpuComision);
        EditText inputDNI = (EditText) findViewById(R.id.inputDNI);
        EditText inputGroup = (EditText) findViewById(R.id.inputGroup);
        TextView info = (TextView) findViewById(R.id.info);

        String nombre = inputNombre.getText().toString().trim();
        String apellido = inputApellido.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String pass = inputPassword.getText().toString().trim();
        String dni = inputDNI.getText().toString().trim();
        String grupo = inputGroup.getText().toString().trim();
        String comision = inputComision.getText().toString().trim();

        todoOK = todoOK && validarMail(inputEmail) && validarPassword(inputPassword);
        todoOK = todoOK && validarString(inputNombre) && validarString(inputApellido) && validarString(inputComision);
        todoOK = todoOK && validarString(inputDNI) && validarString(inputGroup);

        if (todoOK) {
            info.setText("");
            todoOK = false;
            info.setText("Datos Validados Correctamente");

            ///////////////

            Retrofit retrofit;
            HttpLoggingInterceptor loggingInterceptor;
            OkHttpClient.Builder httpClientBuilder;


            loggingInterceptor = new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);

            httpClientBuilder = new OkHttpClient.Builder().addInterceptor(loggingInterceptor);
            retrofit = new Retrofit.Builder().baseUrl("http://so-unlam.net.ar/api/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClientBuilder.build())
                    .build();
            Webservice_UNLAM webserviceUNLAM = retrofit.create(Webservice_UNLAM.class);
            Body_registrarse br = new Body_registrarse();

            br.setEnv("DEV");
            br.setGroup(Integer.parseInt(grupo));
            br.setDni(Integer.parseInt(dni));
            br.setCommission(Integer.parseInt(comision));
            br.setPassword(pass);
            br.setEmail(email);
            br.setLastname(apellido);
            br.setName(nombre);

            Call<Respuesta_Webservice> llamadoRegistrarse = webserviceUNLAM.llamar_servicio_registrarse(br);

            llamadoRegistrarse.enqueue(new Callback<Respuesta_Webservice>() {


                @Override
                public void onResponse(Call<Respuesta_Webservice> call, Response<Respuesta_Webservice> response) {

                    if (response != null) {
                        if (response.body() != null && response.body().getState() != null && response.body().getState().equals("success")) {
                            String token = response.body().getToken();
                            GameGlobalData.token = token;

                            Intent intent = new Intent(getBaseContext(), RegistroExitoso.class);
                            startActivity(intent);
                        }

                    } else
                        Toast.makeText(getBaseContext(), response.body().getMsg(), Toast.LENGTH_LONG);
                }

                @Override
                public void onFailure(Call<Respuesta_Webservice> call, Throwable t) {
                    Toast.makeText(getBaseContext(), "Revise su conexion o vuelva a intentarlo", Toast.LENGTH_LONG);
                }
            });

        }else if (!todoOK) {
            info.setText("");
            info.setText("Los Datos ingresados son Invalidos.");
            todoOK = true;
            inputNombre.setText("");
            inputApellido.setText("");
            inputEmail.setText("");
            inputPassword.setText("");
            inputComision.setText("");
            inputDNI.setText("");
            inputGroup.setText("");
        }

    }

    private boolean validarString(EditText e){
        boolean esValido = true;
        String p = e.getText().toString().trim();
        if(p==null || p.length() == 0 || p.equals("")){
            e.setHint("Este Campo No Puede Estar Vacio");
            esValido=false;
        }
        return esValido;
    }


    private boolean validarPassword(EditText e){
        boolean esValido = true;
        String p = e.getText().toString().trim();
        if(p==null || p.length() == 0 || p.equals("")){
            e.setHint("Este Campo No Puede Estar Vacio");
            esValido=false;
        }
        if(p!=null && !p.equals("") && p.length() <8 ){
            e.setHint("El Password Debe Tener Almenos 8 Caracteres");
            esValido=false;
        }
        return esValido;

    }

    private boolean validarMail(EditText email){
        boolean esValido = true;
        //para validar email.
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);

        String m = email.getText().toString().trim();

        if(m==null || m.length() < 5 || m.equals("")){ //4 porque un email tiene minimo 5 caracteres: A@B.c
            email.setHint("Este Campo No Puede Estar Vacio");
            esValido=false;
        }
        if(m!=null && m.length()>=5){
            Matcher matcher = pattern.matcher(m);
            if(!matcher.matches()){
                email.setHint("Formato email invalido");
                esValido=false;
            }
        }
        return esValido;
    }


}