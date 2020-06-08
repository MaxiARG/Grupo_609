package com.example.servicios;

import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Webservice_UNLAM {

    @POST("login")
    Call<Respuesta_Webservice> llamar_servicio_Login(@Body Body_Login parametrosAEnviar);

    @POST("register")
    Call<Respuesta_Webservice> llamar_servicio_registrarse(@Body Body_registrarse parametrosAEnviar);

    @POST("event")
    Call<Respuesta_RegistrarEvento> llamar_servicio_evento( @Header("token") String token, @Body Body_Evento parametrosAEnviar);
/*
 @FormUrlEncoded
 @Headers("Content-Type: application/json")
 @POST("login")
 Call<Respuesta_Webservice> hacer_login(
         @Field("env") String env,
         @Field("name") String name,
         @Field("lastname") String lastname,
         @Field("dni") String dni,
         @Field("email") String email,
         @Field("password") String password,
         @Field("commission") String commission,
         @Field("group") String group
 );

 @FormUrlEncoded
 @Headers("Content-Type: application/json")
 @POST("event")
 Call<Respuesta_RegistrarEvento> hacer_evento(
         @Header("token") String token,
         @Field("env") String env,
         @Field("type_events") String type_events,
         @Field("state") String state,
         @Field("description") String description
 );
*/

}
