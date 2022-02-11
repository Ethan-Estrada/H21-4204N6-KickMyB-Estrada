package org.estrada.tp1;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import org.estrada.tp1.databinding.ActivityConsultationBinding;

public class Consultation extends AppCompatActivity {
    private ActivityConsultationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityConsultationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setTitle("Activité de consultation");

    }
}