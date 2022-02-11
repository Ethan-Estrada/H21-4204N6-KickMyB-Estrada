package org.estrada.tp1;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.estrada.tp1.databinding.ActivityAccueilBinding;

import java.util.Date;
import java.util.Random;

public class Accueil extends AppCompatActivity {

    private ActivityAccueilBinding binding;
    TacheAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAccueilBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setTitle("Activité d'accueil");

        this.initRecycler();
        this.remplirRecycler();

        binding.floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Accueil.this, Creation.class));
            }
        });


    }

    private void remplirRecycler() {
        for (int i = 0 ; i < 200 ; i++) {
            Tache t = new Tache();
            Random r = new Random();
            t.Nom = "J'aime pas les papayas  # " + i;
            Date d = new Date();
            t.DateLimite = d;
            t.Pourcentage =  r.nextInt(100);
            t.Temps = r.nextInt(7);
            adapter.list.add(t);
        }
        adapter.notifyDataSetChanged();
    }

    private void initRecycler(){
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        adapter = new TacheAdapter();
        recyclerView.setAdapter(adapter);
    }
}

