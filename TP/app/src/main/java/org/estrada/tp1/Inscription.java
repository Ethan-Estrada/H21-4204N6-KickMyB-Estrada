package org.estrada.tp1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.estrada.tp1.databinding.ActivityInscriptionBinding;
import org.estrada.tp1.http.RetrofitUtil;
import org.estrada.tp1.http.Service;
import org.estrada.tp1.transfer.SigninRequest;
import org.estrada.tp1.transfer.SigninResponse;
import org.estrada.tp1.transfer.SignupRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Inscription extends AppCompatActivity{

    private ActivityInscriptionBinding binding;
    private EditText nomUsager;
    private EditText motPasse2;
    private EditText motPasse1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInscriptionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setTitle("Activité d'inscription");

        nomUsager = binding.editNomUtilisateur;
        motPasse1 = binding.editMotDePasse0;
        motPasse2 = binding.editMotDePasse1;

        binding.btnInscription1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //verif mdp
                if (motPasse2.getText().toString().equals(motPasse1.getText().toString()))
                {
                    postdata();
                    Toast.makeText(getApplicationContext(),"Inscription réussie !", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Inscription.this, Accueil.class));
                }
                else {
                    Toast.makeText(getApplicationContext(),"mdp non pareil", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void postdata(){
        SignupRequest resp = new SignupRequest();

        resp.password = motPasse2.getText().toString();
        resp.username = nomUsager.getText().toString();

        // on below line we are executing our method.
        Service service = RetrofitUtil.get();
        service.signupResponse(resp).enqueue(new Callback<SigninResponse>() {
            @Override
            public void onResponse(Call<SigninResponse> call, Response<SigninResponse> response) {
                if(response.isSuccessful()){
                    Log.i("RETROFIT",response.code()+"");
                }
                else {
                    // cas d'erreur http 400 404
                    Log.i("RETROFIT",response.code()+"");
                }
            }

            @Override
            public void onFailure(Call<SigninResponse> call, Throwable t) {
                Log.i("RETROFIT",t.getMessage());
            }
        });

    }
}


