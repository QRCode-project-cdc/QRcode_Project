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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Referencia os componentes do layout
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textRegisterLink = findViewById(R.id.textRegisterLink);

        // Ação do botão de login
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verifica se o e-mail e senha não estão vazios
                if (validateInputs()) {
                    String email = inputEmail.getText().toString();

                    // Simula a verificação de login (apenas para teste)
                    if (email.equals("1")) {
                        // Se for o email do professor, abre a ProfessorActivity
                        Intent intent = new Intent(LoginActivity.this, NextActivity.class);
                        startActivity(intent);
                    } else if (email.equals("2")) {
                        // Se for o email do aluno, abre a AlunoActivity
                        Intent intent = new Intent(LoginActivity.this, NextActivity.class);
                        startActivity(intent);
                    } else {
                        // Caso o e-mail não seja encontrado
                        Toast.makeText(LoginActivity.this, "Usuário não encontrado", Toast.LENGTH_SHORT).show();
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
