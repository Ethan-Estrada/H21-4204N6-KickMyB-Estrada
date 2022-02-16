package org.estrada.tp1;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.estrada.tp1.databinding.ActivityCreationBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Creation extends Accueil{

    CalendarView calendarView;
    String date;

    private ActivityCreationBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setTitle("Activité de création");

        calendarView = (CalendarView) findViewById(R.id.calenderview);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date = year + "/" + month+ "/" + dayOfMonth;
            }
        });

        Tache t = new Tache();
        t.Nom = binding.editTache.toString();
        t.DateLimite = date;
        t.Temps=0;
        t.Pourcentage=0;
        /// Il faut l'ajouter a la liste de taches de l'utilsateur
        adapter.list.add(t);



        binding.btnCreer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Creation.this, Accueil.class);
                v.getContext().startActivity(i);
                adapter.notifyDataSetChanged();
            }
        });


    }


}
