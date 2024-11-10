package com.example.qrcode;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText inputName, inputEmail, inputRGM, inputPassword;
    private RadioGroup radioGroupType;
    private Button buttonSubmit, buttonContinue;
    private View questionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputName = findViewById(R.id.inputName);
        inputEmail = findViewById(R.id.inputEmail);
        inputRGM = findViewById(R.id.inputRGM);
        inputPassword = findViewById(R.id.inputPassword);
        radioGroupType = findViewById(R.id.radioGroupType);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonContinue = findViewById(R.id.buttonContinue);
        questionText = findViewById(R.id.questionText);


        // Inicializa os campos como invisíveis
        inputName.setVisibility(View.GONE);
        inputEmail.setVisibility(View.GONE);
        inputRGM.setVisibility(View.GONE);
        inputPassword.setVisibility(View.GONE);
        buttonSubmit.setVisibility(View.GONE);

        // Configuração do botão "Continuar"
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verifica se o usuário selecionou uma opção no radioGroup
                int selectedId = radioGroupType.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    // Nenhuma opção foi selecionada
                    Toast.makeText(RegisterActivity.this, "Por favor, selecione 'Professor' ou 'Aluno'", Toast.LENGTH_SHORT).show();
                    return; // Retorna sem prosseguir
                }

                // Esconde o título e radion buttons
                questionText.setVisibility(View.GONE);
                radioGroupType.setVisibility(View.GONE);

                // Exibe os campos de entrada
                inputName.setVisibility(View.VISIBLE);
                inputEmail.setVisibility(View.VISIBLE);
                inputPassword.setVisibility(View.VISIBLE);

                // Exibe os campos necessários para Professor
                if (selectedId == R.id.radioProfessor) {

                    inputName.setVisibility(View.VISIBLE);
                    inputEmail.setVisibility(View.VISIBLE);
                    inputPassword.setVisibility(View.VISIBLE);
                }

                // Exibe os campos necessários para Aluno
                else if (selectedId == R.id.radioAluno) {

                    inputRGM.setVisibility(View.VISIBLE);
                    inputName.setVisibility(View.VISIBLE);
                    inputEmail.setVisibility(View.VISIBLE);
                    inputPassword.setVisibility(View.VISIBLE);
                }

                // Esconde o botão "Continuar" e exibe o botão "Cadastrar"
                buttonContinue.setVisibility(View.GONE);
                buttonSubmit.setVisibility(View.VISIBLE);
            }
        });


        // Ação do botão "Cadastrar"
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    int selectedId = radioGroupType.getCheckedRadioButtonId();

                    // Verifica qual opção foi selecionada e navega para a tela correspondente
                    if (selectedId == R.id.radioProfessor) {
                        // Se "Professor" for selecionado
                        Intent intent = new Intent(RegisterActivity.this, ProfessorActivity.class);
                        startActivity(intent);
                    } else if (selectedId == R.id.radioAluno) {
                        // Se "Aluno" for selecionado
                        Intent intent = new Intent(RegisterActivity.this, AlunoActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });


        // Configuração do botão "Cadastrar"
        inputName.addTextChangedListener(new TextWatcher() {
            private boolean isFormatting;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Bloqueia números e caracteres especiais no campo
                String input = s.toString();
                String cleanedInput = input.replaceAll("[0-9]", "");  // Remove números
                cleanedInput = cleanedInput.replaceAll("[^a-zA-Z\\s]", "");  // Remove caracteres especiais

                if (!input.equals(cleanedInput)) {
                    s.replace(0, s.length(), cleanedInput);
                }

                if (isFormatting) return;

                isFormatting = true;
                StringBuilder formatted = new StringBuilder();
                boolean capitalizeNext = true;

                for (char c : s.toString().toCharArray()) {
                    if (capitalizeNext && Character.isLetter(c)) {
                        formatted.append(Character.toUpperCase(c));
                        capitalizeNext = false;
                    } else {
                        formatted.append(c);
                    }
                    if (Character.isWhitespace(c)) capitalizeNext = true;
                }

                if (!s.toString().equals(formatted.toString())) {
                    s.replace(0, s.length(), formatted.toString());
                }
                isFormatting = false;
            }
        });
    }


    // Método para verificar se todos os campos estão preenchidos
    private boolean validateInputs() {
        int selectedId = radioGroupType.getCheckedRadioButtonId();

        // Validações para Professor
        if (selectedId == R.id.radioProfessor) {
            if (inputName.getText().toString().isEmpty()) {
                inputName.setError("Insira o nome");
                return false;
            }
            if (inputEmail.getText().toString().isEmpty()) {
                inputEmail.setError("Insira o e-mail");
                return false;
            }
            if (!isValidEmail(inputEmail.getText().toString())) {
                inputEmail.setError("E-mail inválido. Por favor, use um e-mail ACADÊMICO");
                return false;
            }
            if (inputPassword.getText().toString().isEmpty()) {
                inputPassword.setError("Insira a senha");
                return false;
            }
        }

        // Validações para Aluno
        else if (selectedId == R.id.radioAluno) {
            if (inputName.getText().toString().isEmpty()) {
                inputName.setError("Insira o nome");
                return false;
            }
            if (inputEmail.getText().toString().isEmpty()) {
                inputEmail.setError("Insira o e-mail");
                return false;
            }
            if (!isValidEmail(inputEmail.getText().toString())) {
                inputEmail.setError("E-mail inválido. Por favor, use um e-mail ACADÊMICO");
                return false;
            }
            if (inputRGM.getText().toString().isEmpty()) {
                inputRGM.setError("Insira o RGM");
                return false;
            }
            if (inputPassword.getText().toString().isEmpty()) {
                inputPassword.setError("Insira a senha");
                return false;
            }
        }

        return true; // Todos os campos estão válidos
    }

    // Método para validar o formato do e-mail
    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._-]+@cs\\.unicid\\.edu\\.br$"; // Validação para e-mail @cs.unicid.edu.br
        return email.matches(emailPattern);
    }
}
