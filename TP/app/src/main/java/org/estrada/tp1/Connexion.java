package org.estrada.tp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.estrada.tp1.databinding.ActivityMainBinding;
import org.estrada.tp1.http.RetrofitCookie;
import org.estrada.tp1.http.ServiceCookie;
import org.estrada.tp1.transfer.SigninRequest;
import org.estrada.tp1.transfer.SigninResponse;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Connexion extends AppCompatActivity {

    private ActivityMainBinding binding;
    private EditText nomUsager;
    private EditText motPasse;
    public boolean etatConnexion =false;

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
                if (!etatConnexion){
                    Toast.makeText(getApplicationContext(),"Verification en cours ...", Toast.LENGTH_LONG).show();
                    postData();
                }
                else {
                    if (etatConnexion){
                        Toast.makeText(getApplicationContext(),"Connexion réussie", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Connexion.this, Accueil.class));
                    }
                }
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
        SigninRequest resp = new SigninRequest();

        resp.username = nomUsager.getText().toString();
        resp.password = motPasse.getText().toString();

        Singleton instance = Singleton.getInstance();
        instance.setText(resp.username);

        // on below line we are executing our method.
        ServiceCookie service = RetrofitCookie.get();
         service.signinResponse(resp).enqueue(new Callback<SigninResponse>() {
            @Override
            public void onResponse(Call<SigninResponse> call, Response<SigninResponse> response) {
                if(response.isSuccessful()){
                    etatConnexion =true;
                    Log.i("RETROFIT",response.code()+"");
                }
                else {
                    etatConnexion =false;
                    // cas d'erreur http 400 404
                    Log.i("RETROFIT",response.code()+"");
                }
            }

            @Override
            public void onFailure(Call<SigninResponse> call, Throwable t) {
                etatConnexion =false;
                Log.i("RETROFIT",t.getMessage());
            }
        });
    }
}