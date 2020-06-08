package com.example.servicios;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Webservice_UNLAM {

   // @Headers("Content-Type: application/json")
    @POST("login")
    Call<Respuesta_Webservice> llamar_servicio_Login(@Body Body_Login parametrosAEnviar);

    @POST("register")
    Call<Respuesta_Webservice> llamar_servicio_registrarse(@Body Body_registrarse parametrosAEnviar);

    @POST("event")
    Call<Respuesta_RegistrarEvento> llamar_servicio_evento(@Body Body_Evento parametrosAEnviar, @Header("token") String token);
}
