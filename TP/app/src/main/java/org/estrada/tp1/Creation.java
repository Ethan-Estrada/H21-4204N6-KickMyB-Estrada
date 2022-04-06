package org.estrada.tp1;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import org.estrada.tp1.databinding.ActivityCreationBinding;
import org.estrada.tp1.http.RetrofitCookie;
import org.estrada.tp1.http.ServiceCookie;
import org.estrada.tp1.transfer.AddTaskRequest;
import org.estrada.tp1.transfer.TaskDetailResponse;
import org.joda.time.LocalDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Creation extends BaseActivity{
    CalendarView calendarView;
    String date1;
    Date dateFinale;

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
                date1 = year + "/" +month+ "/" + dayOfMonth;
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                try {
                    Date dateTemp = formatter.parse(date1);
                    SimpleDateFormat formatter1 = new SimpleDateFormat("ddMMyyyy");
                    String l = formatter1.format(dateTemp);
                    Date d = formatter1.parse(l);
                    dateFinale = d;
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
        binding.btnCreer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTaskRequest resp = new AddTaskRequest();
                resp.name = nomTask.getText().toString();
                resp.deadline = dateFinale;

                // on below line we are executing our method.
                ServiceCookie service = RetrofitCookie.get();
                service.addTask(resp).enqueue(new Callback<TaskDetailResponse>() {
                    @Override
                    public void onResponse(Call<TaskDetailResponse> call, Response<TaskDetailResponse> response) {
                        if(response.isSuccessful()){
                            startActivity(new Intent(Creation.this, Accueil.class));
                            Log.i("RETROFIT",response.code()+"");
                        }
                        else {
                            // cas d'erreur http 400 404
                            Log.i("RETROFIT",response.code()+"");
                        }
                    }
                    @Override
                    public void onFailure(Call<TaskDetailResponse> call, Throwable t) {
                        Log.i("RETROFIT",t.getMessage());
                    }
                });
            }
        });


    }


}
