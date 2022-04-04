package org.estrada.tp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.estrada.tp1.databinding.ActivityMainBinding;
import org.estrada.tp1.http.RetrofitUtil;
import org.estrada.tp1.http.Service;
import org.estrada.tp1.transfer.SignupRequest;


import java.text.BreakIterator;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Connexion extends AppCompatActivity {

    private ActivityMainBinding binding;
    private EditText  nomUsager;
    private EditText motPasse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setTitle("Activité de connexion");

        nomUsager = binding.NomUtilisateur;
        motPasse = binding.mdp;
        binding.btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    postData();
                    Toast.makeText(getApplicationContext(),"Connexion réussie", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Connexion.this, Accueil.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("Nom Usager", nomUsager.getText().toString());
                getApplicationContext().startActivity(i);


            }
        });

        binding.btnInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Connexion.this, Inscription.class));
            }
        });

    }

    private void postData(){


        // passing data from our text fields to our modal class.
        SignupRequest modal = new SignupRequest();
        
        modal.username = nomUsager.getText().toString();
        modal.password = motPasse.getText().toString();

        // on below line we are executing our method.
        Service service = RetrofitUtil.get();
         service.signupResponse(modal).enqueue(new Callback<SignupRequest>() {
            @Override
            public void onResponse(Call<SignupRequest> call, Response<SignupRequest> response) {
                // this method is called when we get response from our api.
                Toast.makeText(Connexion.this, "Data added to API", Toast.LENGTH_SHORT).show();

                if(response.isSuccessful()){
                    Log.i("RETROFIT",response.code()+"");
                    Toast.makeText(Connexion.this, "Data added to API", Toast.LENGTH_SHORT).show();
                }
                else {
                    // cas d'erreur http 400 404
                    Log.i("RETROFIT",response.code()+"");
                }
            }

            @Override
            public void onFailure(Call<SignupRequest> call, Throwable t) {
                Log.i("RETROFIT",t.getMessage());
            }
        });
    }
}