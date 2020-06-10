package com.example.servicios;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Webservice_UNLAM {

    @POST("login")
    Call<Respuesta_Webservice> llamar_servicio_Login(@Body Body_Login parametrosAEnviar);

    @POST("register")
    Call<Respuesta_Webservice> llamar_servicio_registrarse(@Body Body_registrarse parametrosAEnviar);

    @POST("event")
    Call<Respuesta_RegistrarEvento> llamar_servicio_evento( @Header("token") String token, @Body Body_Evento parametrosAEnviar);
}
