package org.estrada.tp1.http;

import org.estrada.tp1.transfer.SigninRequest;
import org.estrada.tp1.transfer.SigninResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Service {

    @GET("users/{utilisateur}/repos")
    Call<String> listReposString(@Path("utilisateur") String utilisateur);

    /// Connexion
    @POST("/api/id/signin")
    Call<SigninResponse> signin(@Path("username") SigninRequest request);

    /// Inscription
    @POST("/api/id/signup")
    Call<SigninResponse> signup(@Path("username") SigninRequest request);

}
