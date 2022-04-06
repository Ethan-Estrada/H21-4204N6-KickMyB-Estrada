package org.estrada.tp1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import org.estrada.tp1.databinding.ActivityConsultationBinding;
import org.estrada.tp1.http.RetrofitCookie;
import org.estrada.tp1.http.ServiceCookie;
import org.estrada.tp1.transfer.HomeItemResponse;
import org.estrada.tp1.transfer.TaskDetailResponse;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Consultation extends BaseActivity {
    private ActivityConsultationBinding binding;
    public Integer pourcentage2;
    public Integer jours2;
    public Integer idTask;
    public Long idRecu;
    public Integer progression;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityConsultationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setTitle("Activité de consultation");
        currentActivity = "Consultation";
        getIncomingIntent();

        // binding
        SeekBar seekBar = binding.seekBar3;
        TextView textPourcentage = binding.textePourcentage;
        TextView textJours = binding.txtJours;
        ProgressBar progressBarHori = binding.progressHorizontal;
        ProgressBar progressBar = binding.progressCircular;

        // on below line we are executing our method.
        ServiceCookie service = RetrofitCookie.get();
        service.detailResponse(idTask.longValue()).enqueue(new Callback<TaskDetailResponse>() {
            @Override
            public void onResponse(Call<TaskDetailResponse> call, Response<TaskDetailResponse> response) {
                if(response.isSuccessful()){
                    String nomRecu = response.body().name;
                    Integer pourcentageRecu = response.body().percentageDone;
                    Integer joursFaitRecu = response.body().percentageTimeSpent;
                    Date deadlineRecu = response.body().deadline;
                    idRecu = response.body().id;
                    setTache(nomRecu,pourcentageRecu,joursFaitRecu,deadlineRecu.toString(),idRecu.toString());

                    // set donnees pour les bars
                    textJours.setText(jours2 +" / 7");
                    progressBarHori.setProgress(jours2);
                    textPourcentage.setText(pourcentage2+"%");
                    progressBar.setProgress(pourcentage2);
                    seekBar.setProgress(pourcentage2);

                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            textPourcentage.setText(String.valueOf(progress +"%"));
                            progressBar.setProgress(progress);
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
            }
        });

        binding.btnMettreAJour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( progression >0 && progression <100){
                    ServiceCookie service = RetrofitCookie.get();
                    service.avancementResponse(idRecu,progression).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if(response.isSuccessful()){
                                Intent i = new Intent(Consultation.this, Accueil.class);
                                v.getContext().startActivity(i);
                                Log.i("RETROFIT",response.code()+"");
                            }
                            else {
                                // cas d'erreur http 400 404
                                Log.i("RETROFIT",response.code()+"");
                            }
                        }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.i("RETROFIT",t.getMessage());
                        }
                    });
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