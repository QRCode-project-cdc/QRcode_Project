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

    private EditText inputStudentName, inputTeacherName, inputStudentEmail, inputTeacherEmail, inputRGM, inputStudentPassword, inputTeacherPassword;
    private RadioGroup radioGroupType;
    private Button buttonSubmit, buttonContinue;
    private View questionText;
    private DatabaseHelper databaseHelper;
    private boolean isFormatting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputStudentName = findViewById(R.id.inputStudentName);
        inputTeacherName = findViewById(R.id.inputTeacherName);
        inputStudentEmail = findViewById(R.id.inputStudentEmail);
        inputTeacherEmail = findViewById(R.id.inputTeacherEmail);
        inputRGM = findViewById(R.id.inputRGM);
        inputStudentPassword = findViewById(R.id.inputStudentPassword);
        inputTeacherPassword = findViewById(R.id.inputTeacherPassword);
        radioGroupType = findViewById(R.id.radioGroupType);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonContinue = findViewById(R.id.buttonContinue);
        questionText = findViewById(R.id.questionText);

        // Inicializa o DatabaseHelper usando o método getInstance
        databaseHelper = DatabaseHelper.getInstance(this);

        // Inicializa os campos como invisíveis
        initializeFields();

        // Configuração do botão "Continuar"
        buttonContinue.setOnClickListener(v -> {
            int selectedId = radioGroupType.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(RegisterActivity.this, "Por favor, selecione 'Professor' ou 'Aluno'", Toast.LENGTH_SHORT).show();
                return;
            }

            questionText.setVisibility(View.GONE);
            radioGroupType.setVisibility(View.GONE);
            toggleInputFields(selectedId);
            buttonContinue.setVisibility(View.GONE);
            buttonSubmit.setVisibility(View.VISIBLE);
        });

        // Ação do botão "Cadastrar"
        buttonSubmit.setOnClickListener(v -> {
            if (validateInputs()) {
                String name;
                String email;
                String password;
                String rgm = inputRGM.getText().toString().trim();

                if (radioGroupType.getCheckedRadioButtonId() == R.id.radioProfessor) {
                    name = inputTeacherName.getText().toString().trim();
                    email = inputTeacherEmail.getText().toString().trim();
                    password = inputTeacherPassword.getText().toString().trim();
                } else {
                    name = inputStudentName.getText().toString().trim();
                    email = inputStudentEmail.getText().toString().trim();
                    password = inputStudentPassword.getText().toString().trim();
                }

                // Verifica se o e-mail já está cadastrado
                if (databaseHelper.emailExists(email)) {
                    Toast.makeText(RegisterActivity.this, "E-mail já cadastrado. Tente outro.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Verifica se o RGM já está cadastrado
                if (radioGroupType.getCheckedRadioButtonId() == R.id.radioAluno && databaseHelper.rgmExists(rgm)) {
                    Toast.makeText(RegisterActivity.this, "RGM já cadastrado. Tente outro.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Salva o usuário no banco de dados
                boolean isInserted;
                if (radioGroupType.getCheckedRadioButtonId() == R.id.radioProfessor) {
                    isInserted = databaseHelper.addTeacher(name, email, password);
                } else {
                    isInserted = databaseHelper.addStudent(name, email, rgm, password);
                }

                if (isInserted) {
                    Toast.makeText(RegisterActivity.this, "Usuário registrado com sucesso!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Erro ao registrar usuário. Tente novamente.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Configuração do TextWatcher para formatação de nomes
        setupTextWatchers();
    }

    private void initializeFields() {
        inputStudentName.setVisibility(View.GONE);
        inputTeacherName.setVisibility(View.GONE);
        inputStudentEmail.setVisibility(View.GONE);
        inputTeacherEmail.setVisibility(View.GONE);
        inputRGM.setVisibility(View.GONE);
        inputStudentPassword.setVisibility(View.GONE);
        inputTeacherPassword.setVisibility(View.GONE);
        buttonSubmit.setVisibility(View.GONE);
    }

    private void toggleInputFields(int selectedId) {
        if (selectedId == R.id.radioProfessor) {
            inputTeacherName.setVisibility(View.VISIBLE);
            inputTeacherEmail.setVisibility(View.VISIBLE);
            inputTeacherPassword.setVisibility(View.VISIBLE);
            inputRGM.setVisibility(View.GONE);
            inputStudentName.setVisibility(View.GONE);
            inputStudentEmail.setVisibility(View.GONE);
            inputStudentPassword.setVisibility(View.GONE);
        } else {
            inputStudentName.setVisibility(View.VISIBLE);
            inputStudentEmail.setVisibility(View.VISIBLE);
            inputStudentPassword.setVisibility(View.VISIBLE);
            inputRGM.setVisibility(View.VISIBLE);
            inputTeacherName.setVisibility(View.GONE);
            inputTeacherEmail.setVisibility(View.GONE);
            inputTeacherPassword.setVisibility(View.GONE);
        }
    }

    private void setupTextWatchers() {
        inputStudentName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                formatName(s);
            }
        });

        inputTeacherName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                formatName(s);
            }
        });
    }

    private void formatName(Editable s) {
        String input = s.toString();
        String cleanedInput = input.replaceAll("[0-9]", "").replaceAll("[^a-zA-Z\\s]", "");

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

    private boolean validateInputs() {
        int selectedId = radioGroupType.getCheckedRadioButtonId();

        if (selectedId == R.id.radioProfessor) {
            if (inputTeacherName.getText().toString().isEmpty()) {
                inputTeacherName.setError("Insira o nome do professor");
                return false;
            }
            if (inputTeacherEmail.getText().toString().isEmpty()) {
                inputTeacherEmail.setError("Insira o e-mail do professor");
                return false;
            }
            if (!isValidEmail(inputTeacherEmail.getText().toString())) {
                inputTeacherEmail.setError("E-mail inválido. Por favor, use um e-mail ACADÊMICO");
                return false;
            }
            if (inputTeacherPassword.getText().toString().isEmpty()) {
                inputTeacherPassword.setError("Insira a senha do professor");
                return false;
            }
        } else if (selectedId == R.id.radioAluno) {
            if (inputStudentName.getText().toString().isEmpty()) {
                inputStudentName.setError(" Insira o nome do aluno");
                return false;
            }
            if (inputStudentEmail.getText().toString().isEmpty()) {
                inputStudentEmail.setError("Insira o e-mail do aluno");
                return false;
            }
            if (!isValidEmail(inputStudentEmail.getText().toString())) {
                inputStudentEmail.setError("E-mail inválido. Por favor, use um e-mail ACADÊMICO");
                return false;
            }
            if (inputRGM.getText().toString().isEmpty()) {
                inputRGM.setError("Insira o RGM");
                return false;
            }
            if (inputStudentPassword.getText().toString().isEmpty()) {
                inputStudentPassword.setError("Insira a senha do aluno");
                return false;
            }
        }

        return true; // Todos os campos estão válidos
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._-]+@cs\\.unicid\\.edu\\.br$"; // Validação para e-mail @cs.unicid.edu.br
        return email.matches(emailPattern);
    }
}