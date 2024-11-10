package com.example.qrcode;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AlunoActivity extends AppCompatActivity {

    private String email; // Variável para armazenar o e-mail do usuário
    private String password; // Variável para armazenar a senha do usuário

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aluno);

        // Receber os dados da Intent
        Intent intent = getIntent();
        email = intent.getStringExtra("EMAIL");
        password = intent.getStringExtra("PASSWORD");

        Button buttonViewProfile = findViewById(R.id.buttonViewProfile);
        buttonViewProfile.setOnClickListener(v -> {
            Intent viewProfileIntent = new Intent(AlunoActivity.this, UserProfileActivity.class);
            // Passar o e-mail e a senha para a UserProfileActivity
            viewProfileIntent.putExtra("EMAIL", email);
            viewProfileIntent.putExtra("PASSWORD", password);
            startActivity(viewProfileIntent);
        });

        Button buttonQRCode = findViewById(R.id.buttonQRCode);
        buttonQRCode.setOnClickListener(v -> {
            Intent qrCodeIntent = new Intent(AlunoActivity.this, QRCodeActivity.class);
            qrCodeIntent.putExtra("USER_EMAIL", email); // Corrigido para usar a variável correta 'email'
            startActivity(qrCodeIntent);
        });
    }
}
