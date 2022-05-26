package org.estrada.tp1;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import org.estrada.tp1.databinding.ActivityAccueilBinding;
import org.estrada.tp1.http.RetrofitCookie;
import org.estrada.tp1.http.ServiceCookie;
import org.estrada.tp1.transfer.HomeItemResponse;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Accueil extends BaseActivity{

    private ActivityAccueilBinding binding;
    TacheAdapter adapter;
    final LoadingDialog loadingDialog = new LoadingDialog(Accueil.this);


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAccueilBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setTitle(R.string.activité_ac);
        currentActivity= String.valueOf(R.string.Accueil);

        this.initRecycler();
        loadingDialog.startLoadingDialog();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismissDialog();
            }
        },3000);
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
                        t.TimeSpent = response.body().get(i).percentageTimeSpent;
                        adapter.list.add(t);
                    }
                        adapter.notifyDataSetChanged();
                    Log.i("RETROFIT",response.code()+"");
                }
                else {
                    // cas d'erreur http 400 404
                    try {
                        String statusErreur = response.code()+"";
                        String corpsErreur = response.errorBody().string();
                        if (corpsErreur!=null) {
                            if (corpsErreur.contains("BadCredentialsException")) {
                                Toast.makeText(getApplicationContext(),R.string.er_badCredentials, Toast.LENGTH_SHORT).show();
                            }
                            else if (corpsErreur.contains("InternalAuthenticationServiceException")) {
                                Toast.makeText(getApplicationContext(),R.string.er_internalAuthentification, Toast.LENGTH_SHORT).show();
                            }
                            else if (corpsErreur.contains("DataIntegrityViolationException")) {
                                Toast.makeText(getApplicationContext(),R.string.er_dataIntegrity, Toast.LENGTH_SHORT).show();
                            }
                            else if (statusErreur.contains("403") ) {
                                Toast.makeText(getApplicationContext(),R.string.er_403, Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),corpsErreur, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i("RETROFIT",response.code()+"");
                }
            }
            @Override
            public void onFailure(Call<List<HomeItemResponse>> call, Throwable t) {
                Log.i("RETROFIT",t.getMessage());
                String corpsErreur = t.getMessage();
                if (corpsErreur!=null) {
                    if (corpsErreur.contains("Unable to resolve host")) {
                        Toast.makeText(getApplicationContext(), R.string.er_pasInternet, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initRecycler(){
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        adapter = new TacheAdapter(this);
        recyclerView.setAdapter(adapter);

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Snackbar snackbar = Snackbar
                        .make(binding.getRoot(),R.string.loadingfr, 3000);
                snackbar.show();

                initRecycler();
                remplirRecycler();
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        // Stop animation (This will be after 3 seconds)
                        binding.swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000); // Delay in millis

            }
        });
    }
}

