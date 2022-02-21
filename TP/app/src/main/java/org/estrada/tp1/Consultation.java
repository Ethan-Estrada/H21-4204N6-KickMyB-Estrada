package org.estrada.tp1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.estrada.tp1.databinding.ActivityConsultationBinding;

public class Consultation extends BaseActivity {
    private ActivityConsultationBinding binding;
    public Integer pourcentage2;
    public Integer jours2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityConsultationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setTitle("Activité de consultation");
        currentActivity = "Consultation";
        getIncomingIntent();

        SeekBar seekBar = binding.seekBar3;
        TextView textPourcentage = binding.textePourcentage;
        TextView textJours = binding.txtJours;
        ProgressBar progressBarHori = binding.progressHorizontal;
        ProgressBar progressBar = binding.progressCircular;

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
           }

           @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
       });

        binding.btnMettreAJour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Consultation.this, Accueil.class);
                v.getContext().startActivity(i);
            }
        });

    }
    private void getIncomingIntent(){
        if(getIntent().hasExtra("Nom tache")&& getIntent().hasExtra("Pourcentage")&&getIntent().hasExtra("Temps ecouler")&&getIntent().hasExtra("Date limite")){
            String nomTache = getIntent().getStringExtra("Nom tache");
            Integer pourcentageTache = getIntent().getIntExtra("Pourcentage",0);
            Integer tempsTache = getIntent().getIntExtra("Temps ecouler",0);
            String dateTache = getIntent().getStringExtra("Date limite");

            setTache(nomTache,pourcentageTache,tempsTache,dateTache);
        }
    }
    private void setTache(String pNom,Integer pPourcentage,Integer pTemps,String pDate) {
        TextView Nom = binding.txtNomTache;
        Nom.setText(pNom);

        pourcentage2 = pPourcentage;

        jours2 = pTemps;

        TextView date = binding.txtDateLimite;
        date.setText(pDate);

    }

}