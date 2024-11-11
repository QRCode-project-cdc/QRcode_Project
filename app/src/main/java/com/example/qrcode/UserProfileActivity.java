package com.example.qrcode;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);

        // Configuração de padding para o layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.userprofile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializa os TextViews
        textName = findViewById(R.id.textName);
        textEmail = findViewById(R.id.textEmail);
        textPassword = findViewById(R.id.textPassword);

        // Inicializa o DatabaseHelper
        databaseHelper = DatabaseHelper.getInstance(this);

        // Receber os dados da Intent
        Intent intent = getIntent();
        String rgm = intent.getStringExtra("RGM"); // Recebendo o RGM
        password = intent.getStringExtra("PASSWORD"); // Armazenar a senha recebida

        // Log para verificar o RGM recebido
        Log.d("User ProfileActivity", "RGM recebido: " + rgm);

        // Verificar se o RGM é nulo
        if (rgm == null) {
            Log.e("User ProfileActivity", "RGM é nulo!");
            updateUserProfile(null, null);
            return;
        }

        // Buscar o nome do usuário no banco de dados usando o RGM
        String name = databaseHelper.getStudentNameByRGM(rgm); // Método para buscar pelo RGM

        // Log para verificar o nome retornado
        Log.d("User ProfileActivity", "Nome retornado: " + name);

        // Atualizar os TextViews com os dados do usuário
        updateUserProfile(name, rgm);

        // Configurar o botão de edição
        Button buttonEdit = findViewById(R.id.buttonEdit);
        buttonEdit.setOnClickListener(v -> {
            Intent editIntent = new Intent(UserProfileActivity.this, EditProfileActivity.class);
            // Passar o RGM e o nome para a EditProfileActivity
            editIntent.putExtra("rgm", rgm);
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
            String rgm = data.getStringExtra("rgm");
            password = data.getStringExtra("password"); // Armazen ar a senha recebida

            // Atualizar os TextViews com os novos dados
            updateUserProfile(name, rgm);
        }
    }

    // Método para atualizar os TextViews com os dados do usuário
    private void updateUserProfile(String name, String rgm) {
        textName.setText(String.format("Nome e Sobrenome: %s", name != null ? name : "Desconhecido"));
        textEmail.setText(String.format("RGM: %s", rgm != null ? rgm : "Desconhecido"));
        textPassword.setText(String.format("Senha: %s", getCensoredPassword(password))); // Exibir a senha censurada
    }

    // Método para gerar a string de asteriscos com base no comprimento da senha
    private String getCensoredPassword(String password) {
        return password == null ? "********" : "*".repeat(password.length()); // Retorna a string de asteriscos com base no comprimento da senha
    }
}