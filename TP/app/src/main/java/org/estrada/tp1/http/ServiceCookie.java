package org.estrada.tp1.http;

import org.estrada.tp1.transfer.AddTaskRequest;
import org.estrada.tp1.transfer.HomeItemResponse;
import org.estrada.tp1.transfer.SigninRequest;
import org.estrada.tp1.transfer.SigninResponse;
import org.estrada.tp1.transfer.SignupRequest;
import org.estrada.tp1.transfer.TaskDetailResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ServiceCookie {
    // requetes a faires avec des cookies
    @GET("/api/**")
    Call<String> cookieEcho();

    /// Inscription
    @POST("/api/id/signup")
    Call<SigninResponse> signupResponse(@Body SignupRequest request );

    /// Connexion
    @POST("/api/id/signin")
    Call<SigninResponse> signinResponse(@Body SigninRequest request );

    /// Deconnexion
    @POST("/api/id/signout")
    Call<String> signoutResponse(@Body String response);

    // Add Task
    @POST("/api/add")
    Call<TaskDetailResponse> addTask(@Body AddTaskRequest request);

    // Get TaskList
    @GET("/api/home")
    Call<List<HomeItemResponse>> getTaskList();

    // Recup Detail Tache
    @GET("")

}
