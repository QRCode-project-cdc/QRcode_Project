package com.example.qrcode;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserProfileActivity extends AppCompatActivity {

    private static final int EDIT_PROFILE_REQUEST = 1; // Código de solicitação para identificar a atividade de edição
    private TextView textName;
    private TextView textEmail;
    private TextView textPassword;
    private String password; // Variável para armazenar a senha
    private DatabaseHelper databaseHelper; // Adicionando o DatabaseHelper

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.userprofile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textName = findViewById(R.id.textName);
        textEmail = findViewById(R.id.textEmail);
        textPassword = findViewById(R.id.textPassword);

        // Inicializa o DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Receber os dados da Intent
        Intent intent = getIntent();
        String email = intent.getStringExtra("EMAIL");
        password = intent.getStringExtra("PASSWORD"); // Armazenar a senha recebida

        // Buscar o nome do usuário no banco de dados
        String name = databaseHelper.getUserName(email);

        // Atualizar os TextViews com os dados do usuário
        textName.setText("Nome e Sobrenome: " + (name != null ? name : "Desconhecido"));
        textEmail.setText("E-mail: " + email);
        textPassword.setText("Senha: " + getCensoredPassword(password)); // Exibir a senha censurada

        Button buttonEdit = findViewById(R.id.buttonEdit);
        buttonEdit.setOnClickListener(v -> {
            Intent editIntent = new Intent(UserProfileActivity.this, EditProfileActivity.class);
            // Passar o e-mail e o nome para a EditProfileActivity
            editIntent.putExtra("email", email);
            editIntent.putExtra("name", name);
            startActivityForResult(editIntent, EDIT_PROFILE_REQUEST); // Inicia a atividade de edição com um código de solicitação
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_PROFILE_REQUEST && resultCode == RESULT_OK) {
            // Receber os dados do Intent
            String name = data.getStringExtra("name");
            String email = data.getStringExtra("email");
            password = data.getStringExtra("password"); // Armazenar a senha recebida

            // Atualizar os TextViews com os novos dados
            textName.setText("Nome e Sobrenome: " + name);
            textEmail.setText("E-mail: " + email);
            textPassword.setText("Senha: " + getCensoredPassword(password)); // Exibir a senha censurada
        }
    }

    // Método para gerar a string de asteriscos com base no comprimento da senha
    private String getCensoredPassword(String password) {
        if (password == null) {
            return "********"; // Retorna a string padrão se a senha for nula
        }
        StringBuilder censored = new StringBuilder();
        for (int i = 0; i < password.length(); i++) {
            censored.append('*'); // Adiciona um asterisco para cada caractere da senha
        }
        return censored.toString();
    }
}