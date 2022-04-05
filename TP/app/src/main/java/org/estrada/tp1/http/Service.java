package org.estrada.tp1.http;

import org.estrada.tp1.transfer.SigninRequest;
import org.estrada.tp1.transfer.SigninResponse;
import org.estrada.tp1.transfer.SignupRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Service {

    /// Inscription
    @POST("/api/id/signup")
    Call<SigninResponse> signupResponse(@Body SignupRequest request );

    /// Connexion
    @POST("/api/id/signin")
    Call<SigninResponse> signinResponse(@Body SigninRequest request );

    /// Deconnexion
    @POST("/api/id/signout")
    Call<String> signoutResponse(@Body String response);

}
