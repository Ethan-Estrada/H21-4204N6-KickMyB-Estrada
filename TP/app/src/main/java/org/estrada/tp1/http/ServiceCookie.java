package org.estrada.tp1.http;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServiceCookie {
    // requetes a faires avec des cookies
    @GET("exos/cookie/echo")
    Call<String> cookieEcho();
}
