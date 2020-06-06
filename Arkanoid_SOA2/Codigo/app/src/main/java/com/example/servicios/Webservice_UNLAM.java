package com.example.servicios;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Webservice_UNLAM {

   // @Headers("Content-Type: application/json")
    @POST("login")
    Call<Respuesta_Webservice> llamar_servicio_Login(@Body Body_Login parametrosAEnviar);

    @POST("register")
    Call<Respuesta_Webservice> llamar_servicio_registrarse(@Body Body_registrarse parametrosAEnviar);

}
