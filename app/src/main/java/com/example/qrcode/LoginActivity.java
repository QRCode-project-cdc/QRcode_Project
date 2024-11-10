package com.example.qrcode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private Button buttonLogin;
    private TextView textRegisterLink;
    private DatabaseHelper databaseHelper; // Adicionando o DatabaseHelper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Referencia os componentes do layout
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textRegisterLink = findViewById(R.id.textRegisterLink);

        // Inicializa o DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Ação do botão de login
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verifica se o e-mail e senha não estão vazios
                if (validateInputs()) {
                    String email = inputEmail.getText().toString().trim();
                    String password = inputPassword.getText().toString().trim();

                    // Verifica as credenciais no banco de dados
                    if (databaseHelper.checkUser (email, password)) {
                        // Se o usuário for encontrado, navega para a tela de perfil
                        Intent intent = new Intent(LoginActivity.this, UserProfileActivity.class);
                        intent.putExtra("EMAIL", email);
                        intent.putExtra("PASSWORD", password); // Opcional, se você quiser mostrar a senha censurada
                        startActivity(intent);
                        finish(); // Fecha a tela de login
                    } else {
                        // Caso o e-mail ou senha não sejam encontrados
                        Toast.makeText(LoginActivity.this, "E-mail ou senha inválidos", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Ação do link de cadastro
        textRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abre a tela de cadastro
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    // Método para verificar se os campos foram preenchidos
    private boolean validateInputs() {
        if (inputEmail.getText().toString().isEmpty()) {
            inputEmail.setError("Insira o e-mail");
            return false;
        }
        if (inputPassword.getText().toString().isEmpty()) {
            inputPassword.setError("Insira a senha");
            return false;
        }
        return true;
    }
}