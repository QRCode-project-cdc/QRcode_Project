package com.example.qrcode;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {

    private EditText inputName;
    private EditText inputEmail;
    private EditText inputPassword;
    private Button buttonSave;
    private DatabaseHelper databaseHelper; // Adicionando o DatabaseHelper
    private String currentEmail; // E-mail do usuário atual

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        inputName = findViewById(R.id.inputName);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        buttonSave = findViewById(R.id.buttonSave);
        databaseHelper = new DatabaseHelper(this); // Inicializando o DatabaseHelper

        // Receber dados do Intent
        Intent intent = getIntent();
        currentEmail = intent.getStringExtra("email"); // E-mail do usuário atual
        inputName.setText(intent.getStringExtra("name")); // Nome atual
        inputEmail.setText(currentEmail); // E-mail atual

        // Verifica se o currentEmail é nulo
        if (currentEmail == null) {
            Toast.makeText(this, "E-mail atual não encontrado.", Toast.LENGTH_SHORT).show();
            finish(); // Fecha a atividade se o e-mail não for encontrado
            return;
        }

        buttonSave.setOnClickListener(v -> {
            String name = inputName.getText().toString().trim();
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            if (isValidEmail(email)) {
                // Verifica se o usuário existe
                if (databaseHelper.userExists(currentEmail.trim())) {
                    // Atualiza os dados no banco de dados
                    boolean isUpdated = databaseHelper.updateUser (currentEmail, name, email, password);
                    if (isUpdated) {
                        Toast.makeText(EditProfileActivity.this, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                        // Retorna os dados atualizados para a tela anterior
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("name", name);
                        resultIntent.putExtra("email", email);
                        resultIntent.putExtra("password", password);
                        setResult(RESULT_OK, resultIntent);
                        finish(); // Fecha a atividade
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Erro ao atualizar perfil. Tente novamente.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("EditProfileActivity", "Usuário não encontrado no banco de dados para o e-mail: " + currentEmail);
                    Toast.makeText(EditProfileActivity.this, "Usuário não encontrado. Verifique o e-mail.", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Se o e-mail não for válido, mostra uma mensagem de erro
                Toast.makeText(EditProfileActivity.this, "Por favor, insira um e-mail válido (@cs.unicid.edu.br)", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._-]+@cs\\.unicid\\.edu\\.br$"; // Validação para e-mail @cs.unicid.edu.br
        return email.matches(emailPattern);
    }
}