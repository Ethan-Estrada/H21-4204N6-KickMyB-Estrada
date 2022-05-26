package org.estrada.tp1;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import org.estrada.tp1.databinding.ActivityConsultationBinding;
import org.estrada.tp1.http.RetrofitCookie;
import org.estrada.tp1.http.ServiceCookie;
import org.estrada.tp1.transfer.TaskDetailResponse;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Consultation extends BaseActivity {
    private ActivityConsultationBinding binding;
    public Integer pourcentage2;
    public Integer jours2;
    public Integer idTask;
    public Long idRecu;
    public Integer progression = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityConsultationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        final LoadingDialog loadingDialog = new LoadingDialog(Consultation.this);


        setTitle(R.string.activité_consult);
        currentActivity = "Consultation";
        getIncomingIntent();

        // binding
        SeekBar seekBar = binding.seekBar3;
        TextView textPourcentage = binding.textePourcentage;
        TextView textJours = binding.txtJours;
        ProgressBar progressBarHori = binding.progressHorizontal;
        ProgressBar progressCircular = binding.progressCircular;

        // on below line we are executing our method.
        loadingDialog.startLoadingDialog();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismissDialog();
            }
        },3000);
        ServiceCookie service = RetrofitCookie.get();
        service.detailResponse(idTask.longValue()).enqueue(new Callback<TaskDetailResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<TaskDetailResponse> call, Response<TaskDetailResponse> response) {
                if(response.isSuccessful()){

                    // Nombre de jours entre today et la datelimite
                    Date aujDate = new Date();
                    Date Finale = response.body().deadline;
                    Long joursAuj = Finale.getTime()-aujDate.getTime();
                    Long joursRestant =  TimeUnit.DAYS.convert(joursAuj, TimeUnit.MILLISECONDS);
                    // Le Calcul
                    Long jourPasser = (response.body().percentageTimeSpent * joursRestant) / (100 - response.body().percentageTimeSpent);
                    Long jourTotaux = joursRestant + jourPasser;
                    // Set la progressbarHori des jours
                    progressBarHori.setMin(0);
                    progressBarHori.setProgress((jourPasser).intValue());
                    progressBarHori.setMax((jourTotaux).intValue());
                    textJours.setText(jourPasser+"J / "+ jourTotaux+"J");

                    // set les donnees de la tache
                    String nomRecu = response.body().name;
                    Integer pourcentageRecu = response.body().percentageDone;
                    Integer joursFaitRecu = response.body().percentageTimeSpent;
                    Date deadlineRecu = response.body().deadline;
                    idRecu = response.body().id;
                    setTache(nomRecu,pourcentageRecu,joursFaitRecu,deadlineRecu.toString(),idRecu.toString());

                    // set donnees pour les bars
                    textPourcentage.setText(pourcentage2+"%");
                    progressCircular.setProgress(pourcentageRecu);
                    seekBar.setProgress(pourcentageRecu);
                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            textPourcentage.setText(String.valueOf(progress +"%"));
                            progressCircular.setProgress(progress);
                            progression = progress;
                        }
                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                        }
                    });
                    Log.i("RETROFIT",response.code()+"");
                }
                else {
                    // cas d'erreur http 400 404
                    Log.i("RETROFIT",response.code()+"");

                }
            }
            @Override
            public void onFailure(Call<TaskDetailResponse> call, Throwable t) {
                Log.i("RETROFIT",t.getMessage());
                String corpsErreur = t.getMessage();
                if (corpsErreur!=null) {
                    if (corpsErreur.contains("Unable to resolve host")) {
                        Toast.makeText(getApplicationContext(), R.string.er_pasInternet, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        binding.btnMettreAJour.setOnClickListener(new View.OnClickListener() {
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

                if ( progression >0  && progression < 100){
                    ServiceCookie service = RetrofitCookie.get();
                    service.avancementResponse(idRecu,progression).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if(response.isSuccessful()){
                                Intent i = new Intent(Consultation.this, Accueil.class);
                                v.getContext().startActivity(i);
                                Log.i("RETROFIT",response.code()+"");
                                binding.btnMettreAJour.setEnabled(true);
                            }
                            else {
                                // cas d'erreur http 400 404
                                try {
                                    String statusErreur = response.code()+"";
                                    String corpsErreur = response.errorBody().string();
                                    if (corpsErreur!=null) {
                                        if (corpsErreur.contains("InternalAuthenticationServiceException")) {
                                            Toast.makeText(getApplicationContext(),R.string.er_internalAuthentification, Toast.LENGTH_SHORT).show();
                                        }
                                        else if (corpsErreur.contains("DataIntegrityViolationException")) {
                                            Toast.makeText(getApplicationContext(),R.string.er_dataIntegrity, Toast.LENGTH_SHORT).show();
                                        }
                                        else if (statusErreur.contains("403") ) {
                                            Toast.makeText(getApplicationContext(),"l'utilisateur n'est plus authentifié ou a été déconnecté", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(),corpsErreur, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Log.i("RETROFIT",response.code()+"");
                                binding.btnMettreAJour.setEnabled(true);
                            }
                        }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.i("RETROFIT",t.getMessage());
                            String corpsErreur = t.getMessage();
                            if (corpsErreur!=null) {
                                if (corpsErreur.contains("Unable to resolve host")) {
                                    Toast.makeText(getApplicationContext(), "Pas de connection internet", Toast.LENGTH_LONG).show();
                                }
                            }
                            binding.btnMettreAJour.setEnabled(true);
                        }
                    });
                    binding.btnMettreAJour.setEnabled(false);
                }
            }
        });

    }
    public String getIncomingIntent(){
        if(getIntent().hasExtra("Nom tache")&& getIntent().hasExtra("Pourcentage")&&getIntent().hasExtra("Temps ecouler")&&getIntent().hasExtra("Date limite")&&getIntent().hasExtra("ID")){
            String nomTache = getIntent().getStringExtra("Nom tache");
            Integer pourcentageTache = getIntent().getIntExtra("Pourcentage",0);
            Integer tempsTache = getIntent().getIntExtra("Temps ecouler",0);
            String dateTache = getIntent().getStringExtra("Date limite");
            Integer ID= getIntent().getIntExtra("ID",0);

            idTask = getIntent().getIntExtra("ID",0);
        }
        return null;
    }
    private void setTache(String pNom,Integer pPourcentage,Integer pTemps,String pDate,String pID) {
        TextView Nom = binding.txtNomTache;
        Nom.setText(pNom);
        pourcentage2 = pPourcentage;
        jours2 = pTemps;
        TextView date = binding.txtDateLimite;
        date.setText(pDate);
        TextView ID = binding.txtID;
        ID.setText(pID);
    }

}