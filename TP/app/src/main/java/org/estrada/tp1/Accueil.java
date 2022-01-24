package org.estrada.tp1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.estrada.tp1.databinding.ActivityAccueilBinding;
import org.estrada.tp1.databinding.ActivityMainBinding;

public class Accueil extends AppCompatActivity {

    private ActivityAccueilBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAccueilBinding.inflate(getLayoutInflater());
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

