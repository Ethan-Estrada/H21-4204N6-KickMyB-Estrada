package org.estrada.tp1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.estrada.tp1.databinding.ActivityInscriptionBinding;
import org.estrada.tp1.http.RetrofitCookie;
import org.estrada.tp1.http.ServiceCookie;
import org.estrada.tp1.transfer.SigninResponse;
import org.estrada.tp1.transfer.SignupRequest;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Inscription extends AppCompatActivity{

    private ActivityInscriptionBinding binding;
    private EditText nomUsager;
    private EditText motPasse2;
    private EditText motPasse1;
    final LoadingDialog loadingDialog = new LoadingDialog(Inscription.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInscriptionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setTitle(R.string.activité_ins);

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
                    loadingDialog.startLoadingDialog();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.dismissDialog();
                        }
                    },3000);
                }
                else {
                    Toast.makeText(getApplicationContext(),R.string.er_mdpNonPareil, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void postdata(){
        SignupRequest resp = new SignupRequest();

        resp.password = motPasse2.getText().toString();
        resp.username = nomUsager.getText().toString();

        Singleton instance = Singleton.getInstance();
        instance.setText(resp.username);

        // on below line we are executing our method.
        ServiceCookie service = RetrofitCookie.get();
        service.signupResponse(resp).enqueue(new Callback<SigninResponse>() {
            @Override
            public void onResponse(Call<SigninResponse> call, Response<SigninResponse> response) {
                if(response.isSuccessful()){
                    Log.i("RETROFIT",response.code()+"");
                    Toast.makeText(getApplicationContext(),R.string.InscriptionReussie, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Inscription.this, Accueil.class));
                    binding.btnInscription1.setEnabled(true);
                }
                else {
                    try {
                        String corpsErreur = response.errorBody().string();
                        if (corpsErreur!=null) {
                            if (corpsErreur.contains("UsernameTooShort")) {
                                Toast.makeText(getApplicationContext(),R.string.er_usernameCourt, Toast.LENGTH_SHORT).show();
                            }
                            else if (corpsErreur.contains("PasswordTooShort")) {
                                Toast.makeText(getApplicationContext(),R.string.er_mdpCourt, Toast.LENGTH_SHORT).show();
                            }
                            else if (corpsErreur.contains("InternalAuthenticationServiceException")) {
                                Toast.makeText(getApplicationContext(),R.string.er_internalAuthentification, Toast.LENGTH_SHORT).show();
                            }
                            else if (corpsErreur.contains("DataIntegrityViolationException")) {
                                Toast.makeText(getApplicationContext(),R.string.er_dataIntegrity, Toast.LENGTH_SHORT).show();
                            }
                            else if (corpsErreur.contains("UsernameAlreadyTaken")) {
                                Toast.makeText(getApplicationContext(),R.string.er_nameDejaPris, Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),corpsErreur, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    binding.btnInscription1.setEnabled(true);
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
                binding.btnInscription1.setEnabled(true);
            }
        });
        binding.btnInscription1.setEnabled(false);
    }
}


