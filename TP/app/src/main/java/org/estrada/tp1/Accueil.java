package org.estrada.tp1;
import android.content.Intent;
import android.icu.util.LocaleData;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.estrada.tp1.databinding.ActivityAccueilBinding;

import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Accueil extends AppCompatActivity {

    private ActivityAccueilBinding binding;
    TacheAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
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

    @RequiresApi(api = Build.VERSION_CODES.O)

    private void remplirRecycler() {
        for (int i = 0 ; i < 20 ; i++) {
            Tache t = new Tache();
            Random r = new Random();
            t.Nom = "J'aime pas les papayas  # " + i;
            LocalDate d = LocalDate.now();
            t.DateLimite = d.toString();
            t.Pourcentage =  r.nextInt(100);
            t.Temps = r.nextInt(7);
            adapter.list.add(t);
        }
        adapter.notifyDataSetChanged();
    }

    public void initRecycler(){
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        adapter = new TacheAdapter(this);
        recyclerView.setAdapter(adapter);
    }
}

