package com.example.qrcode;

import android.annotation.SuppressLint;
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
    private EditText inputRGM;
    private EditText inputPassword;
    private Button buttonSave;
    private Button buttonTogglePassword;
    private DatabaseHelper databaseHelper;
    private String currentRGM;
    private boolean isPasswordVisible = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Inicializa os componentes da interface
        inputName = findViewById(R.id.inputName);
        inputRGM = findViewById(R.id.inputRGM);
        inputPassword = findViewById(R.id.inputPassword);
        buttonSave = findViewById(R.id.buttonSave);
        buttonTogglePassword = findViewById(R.id.buttonTogglePassword);

        // Inicializa o DatabaseHelper
        databaseHelper = DatabaseHelper.getInstance(this);

        // Receber dados do Intent
        Intent intent = getIntent();
        currentRGM = intent.getStringExtra("rgm");
        String name = intent.getStringExtra("name");

        // Verifica se o currentRGM é nulo
        if (currentRGM == null) {
            Toast.makeText(this, "RGM atual não encontrado.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Preenche os campos com os dados existentes
        if (name != null) {
            inputName.setText(name);
        } else {
            inputName.setText(""); // Ou algum valor padrão
        }
        inputRGM.setText(currentRGM);

        // Configura o listener para o botão de alternância da senha
        buttonTogglePassword.setOnClickListener(v -> {
            if (isPasswordVisible) {
                // Se a senha está visível, ocultá-la
                inputPassword.setInputType(129); // 129 é o tipo de entrada para senha
                buttonTogglePassword.setText("Mostrar");
            } else {
                // Se a senha está oculta, mostrá-la
                inputPassword.setInputType(1); // 1 é o tipo de entrada para texto simples
                buttonTogglePassword.setText("Ocultar");
            }
            isPasswordVisible = !isPasswordVisible;
        });

        buttonSave.setOnClickListener(v -> {
            String newName = inputName.getText().toString().trim();
            String rgm = inputRGM.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            if (isValidRGM(rgm) && isValidName(newName) && isValidPassword(password)) {
                // Verifica se o usuário existe
                if (databaseHelper.rgmExists(currentRGM.trim())) {
                    // Atualiza os dados no banco de dados
                    boolean isUpdated = databaseHelper.updateStudent(currentRGM, newName, rgm, password);
                    if (isUpdated) {
                        Toast.makeText(EditProfileActivity.this, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                        // Retorna os dados atualizados para a tela anterior
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("name", newName);
                        resultIntent.putExtra("rgm", rgm);
                        resultIntent.putExtra("password", password);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Erro ao atualizar perfil. Tente novamente.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("EditProfileActivity", "Usuário não encontrado no banco de dados para o RGM: " + currentRGM);
                    Toast.makeText(EditProfileActivity.this, "Usuário não encontrado. Verifique o RGM.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(EditProfileActivity.this, "Por favor, insira dados válidos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidRGM(String rgm ) {
        // Lógica de validação do RGM
        return rgm != null && !rgm.isEmpty() && rgm.matches("\\d+"); // Exemplo: RGM deve ser numérico
    }

    private boolean isValidName(String name) {
        // Lógica de validação do nome
        return name != null && !name.isEmpty(); // O nome não pode ser vazio
    }

    private boolean isValidPassword(String password) {
        // Lógica de validação da senha
        return password != null && password.length() >= 6; // Exemplo: a senha deve ter pelo menos 6 caracteres
    }
}