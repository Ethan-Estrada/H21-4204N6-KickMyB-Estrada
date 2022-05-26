package org.estrada.tp1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import org.estrada.tp1.databinding.ActivityMainBinding;
import org.estrada.tp1.http.RetrofitCookie;
import org.estrada.tp1.http.ServiceCookie;
import org.estrada.tp1.transfer.SigninRequest;
import org.estrada.tp1.transfer.SigninResponse;


import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Connexion extends AppCompatActivity {

    ActivityMainBinding binding;

    private EditText nomUsager;
    private EditText motPasse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        final LoadingDialog loadingDialog = new LoadingDialog(Connexion.this);

        setTitle(R.string.activité_co);

        nomUsager = binding.NomUtilisateur;
        motPasse = binding.mdp;


        binding.btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    loadingDialog.startLoadingDialog();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.dismissDialog();
                        }
                    },3000);

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
                                Log.i("RETROFIT",response.code()+"");
                                Toast.makeText(getApplicationContext(),R.string.co_reussie, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Connexion.this, Accueil.class));
                                binding.btnConnexion.setEnabled(true);
                            }
                            else {
                                // cas d'erreur http 400 404
                                try {
                                    String corpsErreur = response.errorBody().string();
                                    if (corpsErreur!=null) {
                                         if (corpsErreur.contains("BadCredentialsException")) {
                                             Toast.makeText(getApplicationContext(),R.string.er_badCredentials, Toast.LENGTH_SHORT).show();
                                         }
                                        else if (corpsErreur.contains("InternalAuthenticationServiceException")) {
                                            Toast.makeText(getApplicationContext(),R.string.er_internalAuthentification, Toast.LENGTH_SHORT).show();
                                        }
                                         else if (corpsErreur.contains("DataIntegrityViolationException")) {
                                             Toast.makeText(getApplicationContext(),R.string.er_dataIntegrity, Toast.LENGTH_SHORT).show();
                                         }
                                         else{
                                             Toast.makeText(getApplicationContext(),corpsErreur, Toast.LENGTH_SHORT).show();
                                         }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                binding.btnConnexion.setEnabled(true);
                            }
                        }
                        @Override
                        public void onFailure(Call<SigninResponse> call, Throwable t) {
                            String corpsErreur = t.getMessage();
                            if (corpsErreur!=null) {
                                if (corpsErreur.contains("Unable to resolve host")) {
                                    Toast.makeText(getApplicationContext(), R.string.er_pasInternet, Toast.LENGTH_LONG).show();
                                }
                            }
                            Log.i("RETROFIT",t.getMessage());
                            binding.btnConnexion.setEnabled(true);
                        }
                    });
                    binding.btnConnexion.setEnabled(false);
            }
        });

        binding.btnInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Connexion.this, Inscription.class));
            }
        });
    }
}