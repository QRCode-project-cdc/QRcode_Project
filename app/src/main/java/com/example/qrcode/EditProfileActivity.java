package com.example.qrcode;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {

    private EditText inputName;
    private EditText inputEmail;
    private EditText inputPassword;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        inputName = findViewById(R.id.inputName);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        buttonSave = findViewById(R.id.buttonSave);

        // Aqui você pode carregar as informações do usuário e preencher os EditTexts

        buttonSave.setOnClickListener(v -> {
            // Aqui você pode salvar as informações e retornar para a tela de perfil
            // Por exemplo, você pode usar Intent para voltar à tela de perfil
            finish(); // Fecha a atividade atual e retorna à anterior
        });
    }
}