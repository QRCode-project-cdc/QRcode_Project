package com.example.qrcode;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.client.android.Intents;

public class ProfessorActivity extends AppCompatActivity {

    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor);

        // Receber os dados da Intent
        Intent intent = getIntent();
        email = intent.getStringExtra("EMAIL");
        password = intent.getStringExtra("PASSWORD");

        Button buttonCreateEvent = findViewById(R.id.buttonCreateEvent);
        buttonCreateEvent.setOnClickListener(v -> {
            Intent CreateEventIntent = new Intent(ProfessorActivity.this, CreateEvent.class);
            CreateEventIntent.putExtra("EMAIL", email);
            CreateEventIntent.putExtra("PASSWORD", password);
            startActivity(CreateEventIntent);
        });

        Button buttonActiveEvents = findViewById(R.id.buttonActiveEvents);
        buttonActiveEvents.setOnClickListener(v -> {
            Intent ActiveEventsIntent = new Intent(ProfessorActivity.this, ActiveEventsActivity.class); // eventos ativos
            startActivity(ActiveEventsIntent);
        });

        Button ButtonScanQrcode = findViewById(R.id.ButtonScanQrcode);
        ButtonScanQrcode.setOnClickListener(v -> {
            Intent ScanQrcodeIntent = new Intent(ProfessorActivity.this, ScanQrcode.class);
            ScanQrcodeIntent.putExtra("USER_EMAIL", email); // Corrigido para usar a variável correta 'email'
            startActivity(ScanQrcodeIntent);
        });
        // esboço de informações necessárias, não sei quais exatamente serão usadas - lucas A
    }
}
