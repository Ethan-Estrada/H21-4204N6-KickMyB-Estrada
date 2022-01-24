package org.estrada.tp1;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import org.estrada.tp1.databinding.ActivityAccueilBinding;
import org.estrada.tp1.databinding.ActivityInscriptionBinding;

public class Inscription extends AppCompatActivity {

    private ActivityInscriptionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInscriptionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setTitle("Activité d'accueil");

//        binding.btnConnexion.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"Vote ajouté!", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(MainActivity.this, accueil.class));
//            }
//        });
    }
}


