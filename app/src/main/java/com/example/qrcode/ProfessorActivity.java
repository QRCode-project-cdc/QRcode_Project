package com.example.qrcode;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

        Button buttonQuit = findViewById(R.id.buttonQuit);

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
        // Implementando o logout
        buttonQuit.setOnClickListener(v -> logout());
    }
    private void logout() {
        // Caso você esteja utilizando SharedPreferences para armazenar dados de login
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Limpa todos os dados armazenados
        editor.apply(); // Aplica a limpeza

        // Log para confirmar que os dados foram limpos
        Log.d("Logout", "Usuário desconectado e dados limpos.");

        // Redireciona para a LoginActivity (ou a tela de login do seu app)
        Intent intent = new Intent(ProfessorActivity.this, LoginActivity.class);
        startActivity(intent);

        // Finaliza a AlunoActivity para que o usuário não consiga voltar para ela após o logout
        finish();
    }
}
