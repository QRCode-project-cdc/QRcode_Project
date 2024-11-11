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
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Referencia os componentes do layout
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textRegisterLink = findViewById(R.id.textRegisterLink);

        // Inicializa o DatabaseHelper usando o método getInstance
        databaseHelper = DatabaseHelper.getInstance(this);

        // Ação do botão de login
        buttonLogin.setOnClickListener(v -> {
            if (validateInputs()) {
                String emailOrRGM = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Verifica se o usuário está tentando logar como aluno ou professor
                if (isRGM(emailOrRGM)) {
                    loginAsStudent(emailOrRGM, password);
                } else {
                    // Adiciona uma verificação para avisar que alunos devem usar RGM
                    if (databaseHelper.studentEmailExists(emailOrRGM)) {
                        Toast.makeText(LoginActivity.this, "Alunos devem entrar com RGM", Toast.LENGTH_SHORT).show();
                    } else {
                        loginAsTeacher(emailOrRGM, password);
                    }
                }
            }
        });

        // Ação do link de cadastro
        textRegisterLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    // Método para verificar se os campos foram preenchidos
    private boolean validateInputs() {
        if (inputEmail.getText().toString().isEmpty()) {
            inputEmail.setError("Insira o e-mail ou RGM");
            return false;
        }
        if (inputPassword.getText().toString().isEmpty()) {
            inputPassword.setError("Insira a senha");
            return false;
        }
        return true;
    }

    // Método para verificar se a entrada é um RGM
    private boolean isRGM(String input) {
        // Aqui você pode adicionar lógica para validar o formato do RGM, se necessário
        return !input.contains("@");
    }

    // Método para login como aluno
    private void loginAsStudent(String rgm, String password) {
        if (databaseHelper.checkStudent(rgm, password)) {
            Intent intent = new Intent(LoginActivity.this, AlunoActivity.class);
            intent.putExtra("RGM", rgm); // Passando o RGM
            intent.putExtra("PASSWORD", password); // Passando a senha
            startActivity(intent);
            finish(); // Fecha a tela de login
        } else {
            Toast.makeText(LoginActivity.this, "RGM ou senha inválidos", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para login como professor
    private void loginAsTeacher(String email, String password) {
        if (databaseHelper.checkTeacher(email, password)) {
            Intent intent = new Intent(LoginActivity.this, ProfessorActivity.class);
            intent.putExtra("EMAIL", email);
            startActivity(intent);
            finish(); // Fecha a tela de login
        } else {
            Toast.makeText(LoginActivity.this, "E-mail ou senha inválidos", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}