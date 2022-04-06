package org.estrada.tp1;
import android.content.Intent;
import android.icu.util.LocaleData;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.estrada.tp1.databinding.ActivityAccueilBinding;
import org.estrada.tp1.http.RetrofitCookie;
import org.estrada.tp1.http.ServiceCookie;
import org.estrada.tp1.transfer.HomeItemResponse;
import org.estrada.tp1.transfer.SigninRequest;
import org.estrada.tp1.transfer.SigninResponse;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Accueil extends BaseActivity{

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
        currentActivity= "Accueil";

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
        // on below line we are executing our method.
        ServiceCookie service = RetrofitCookie.get();
        service.getTaskList().enqueue(new Callback<List<HomeItemResponse>>() {
            @Override
            public void onResponse(Call<List<HomeItemResponse>> call, Response<List<HomeItemResponse>> response) {
                if(response.isSuccessful()){
                    for (int i = 0 ; i < response.body().size(); i++) {
                        Tache t = new Tache();
                        t.ID = (response.body().get(i).id).intValue();
                        t.Nom = response.body().get(i).name;
                        t.DateLimite = response.body().get(i).deadline;
                        t.Pourcentage = response.body().get(i).percentageDone;
                        t.Temps = response.body().get(i).percentageTimeSpent;
                        adapter.list.add(t);
                    }
                        adapter.notifyDataSetChanged();
                    Log.i("RETROFIT",response.code()+"");
                }
                else {
                    // cas d'erreur http 400 404
                    Log.i("RETROFIT",response.code()+"");
                }
            }
            @Override
            public void onFailure(Call<List<HomeItemResponse>> call, Throwable t) {
                Log.i("RETROFIT",t.getMessage());
            }
        });
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

