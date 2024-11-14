package com.example.qrcode;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AlunoActivity extends AppCompatActivity {

    private String email; // Variável para armazenar o e-mail do usuário
    private String password; // Variável para armazenar a senha do usuário
    private String rgm; // Variável para armazenar o RGM do usuário

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aluno);

        Button buttonQuit = findViewById(R.id.buttonQuit);

        // Receber os dados da Intent
        Intent intent = getIntent();
        email = intent.getStringExtra("EMAIL");
        password = intent.getStringExtra("PASSWORD");
        rgm = intent.getStringExtra("RGM"); // Recebendo o RGM

        // Log para verificar se os dados foram recebidos corretamente
        Log.d("AlunoActivity", "Email recebido: " + email);
        Log.d("AlunoActivity", "Senha recebida: " + password);
        Log.d("AlunoActivity", "RGM recebido: " + rgm); // Log do RGM

        Button buttonViewProfile = findViewById(R.id.buttonViewProfile);
        buttonViewProfile.setOnClickListener(v -> {
            Intent viewProfileIntent = new Intent(AlunoActivity.this, UserProfileActivity.class);
            // Passar o e-mail, a senha e o RGM para a UserProfileActivity
            viewProfileIntent.putExtra("EMAIL", email);
            viewProfileIntent.putExtra("PASSWORD", password);
            viewProfileIntent.putExtra("RGM", rgm); // Passando o RGM
            startActivity(viewProfileIntent);
        });

        // Botão para ver os eventos disponíveis
        Button buttonAvailableEvents = findViewById(R.id.buttonAvailableEvents);
        buttonAvailableEvents.setOnClickListener(v -> {
            if (rgm != null && !rgm.isEmpty()) {
                Intent availableEventsIntent = new Intent(AlunoActivity.this, AvailableEvents.class);
                availableEventsIntent.putExtra("RGM", rgm); // Passando o RGM
                startActivity(availableEventsIntent);
            }
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
        Intent intent = new Intent(AlunoActivity.this, LoginActivity.class);
        startActivity(intent);

        // Finaliza a AlunoActivity para que o usuário não consiga voltar para ela após o logout
        finish();
    }
}
