package com.example.servicios;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServicioLogin {

    @GET("people")
    Call<Data> getPersonaje();

}
