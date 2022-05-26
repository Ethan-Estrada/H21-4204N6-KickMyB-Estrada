package org.estrada.tp1;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import org.estrada.tp1.databinding.ActivityCreationBinding;
import org.estrada.tp1.http.RetrofitCookie;
import org.estrada.tp1.http.ServiceCookie;
import org.estrada.tp1.transfer.AddTaskRequest;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Creation extends BaseActivity{
    CalendarView calendarView;
    String date1;
    Date dateFinale;
    final LoadingDialog loadingDialog = new LoadingDialog(Creation.this);

    private ActivityCreationBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setTitle("Activité de création");
        currentActivity="Creation";

        EditText nomTask = binding.editTache;
        calendarView = (CalendarView) findViewById(R.id.calenderview);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date1 = year + "/" +(month +1)+ "/" + dayOfMonth;
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                try {
                    Date dateTemp = formatter.parse(date1);
                    dateFinale = dateTemp;
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
        binding.btnCreer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismissDialog();
                    }
                },3000);

                AddTaskRequest resp = new AddTaskRequest();
                resp.name = nomTask.getText().toString();
                resp.deadline = dateFinale;

                // on below line we are executing our method.
                ServiceCookie service = RetrofitCookie.get();
                service.addTask(resp).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()){
                            startActivity(new Intent(Creation.this, Accueil.class));
                            Log.i("RETROFIT",response.code()+"");
                            binding.btnCreer.setEnabled(true);
                        }
                        else {
                            // cas d'erreur http 400 404
                            try {
                                String statusErreur = response.code()+"";
                                String corpsErreur = response.errorBody().string();
                                if (corpsErreur!=null) {
                                    if (corpsErreur.contains("Existing")) {
                                        Toast.makeText(getApplicationContext(),"Le nom de la tache existe déjà", Toast.LENGTH_SHORT).show();
                                    }
                                    else if (corpsErreur.contains("InternalAuthenticationServiceException")) {
                                        Toast.makeText(getApplicationContext(),"Impossible de communiquer avec le serveur", Toast.LENGTH_SHORT).show();
                                    }
                                    else if (statusErreur.contains("403") ) {
                                        Toast.makeText(getApplicationContext(),"l'utilisateur n'est plus authentifié ou a été déconnecté", Toast.LENGTH_SHORT).show();
                                    }
                                    else if (corpsErreur.contains("DataIntegrityViolationException")) {
                                        Toast.makeText(getApplicationContext(),"Les données fournies sont incompatibles avec le serveur", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),corpsErreur, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.i("RETROFIT",response.code()+"");
                            binding.btnCreer.setEnabled(true);
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.i("RETROFIT",t.getMessage());
                        String corpsErreur = t.getMessage();
                        if (corpsErreur!=null) {
                            if (corpsErreur.contains("Unable to resolve host")) {
                                Toast.makeText(getApplicationContext(), "Pas de connection internet", Toast.LENGTH_LONG).show();
                            }
                        }
                        binding.btnCreer.setEnabled(true);
                    }
                });
                binding.btnCreer.setEnabled(false);
            }
        });


    }


}
