package org.estrada.tp1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.estrada.tp1.databinding.ActivityInscriptionBinding;

public class Inscription extends AppCompatActivity {

    private ActivityInscriptionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInscriptionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setTitle("Activité d'inscription");

        binding.btnInscription1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Inscription réussie !", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Inscription.this, Accueil.class));
            }
        });
    }
}


