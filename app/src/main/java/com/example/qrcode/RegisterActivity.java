package com.example.qrcode;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qrcode.AlunoActivity;
import com.example.qrcode.ProfessorActivity;

public class RegisterActivity<CharSequenceSurname> extends AppCompatActivity {

    private EditText inputName, inputEmail, inputDateOfBirth, inputPassword;
    private RadioGroup radioGroupType;
    private Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Referencia os componentes do layout
        inputName = findViewById(R.id.inputName);
        inputEmail = findViewById(R.id.inputEmail);
        inputDateOfBirth = findViewById(R.id.inputDateOfBirth);
        inputPassword = findViewById(R.id.inputPassword);
        radioGroupType = findViewById(R.id.radioGroupType);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // Aplica a máscara de data para o campo de Data de Nascimento
        applyDateMask();

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verifica se todos os campos foram preenchidos
                if (validateInputs()) {
                    int selectedId = radioGroupType.getCheckedRadioButtonId();

                    if (selectedId == R.id.radioProfessor) {
                        // Se "Professor" for selecionado, vai para a ProfessorActivity
                        Intent intent = new Intent(RegisterActivity.this, ProfessorActivity.class);
                        startActivity(intent);
                    } else if (selectedId == R.id.radioAluno) {
                        // Se "Aluno" for selecionado, vai para a AlunoActivity
                        Intent intent = new Intent(RegisterActivity.this, AlunoActivity.class);
                        startActivity(intent);
                    } else {
                        // Exibe uma mensagem de erro se nenhum tipo for selecionado
                        Toast.makeText(RegisterActivity.this, "Por favor, selecione 'Professor' ou 'Aluno'", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        inputName.addTextChangedListener(new TextWatcher() {
            private boolean isFormatting;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
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



    // Método para aplicar a máscara de data dd/mm/yyyy
    private void applyDateMask() {
        inputDateOfBirth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int after) {
                if (charSequence.length() == 2 || charSequence.length() == 5) {
                    if (start < charSequence.length()) {
                        // Insere as barras automaticamente
                        inputDateOfBirth.setText(charSequence + "/");
                        inputDateOfBirth.setSelection(inputDateOfBirth.getText().length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    // Método para verificar se todos os campos estão preenchidos
    private boolean validateInputs() {
        if (inputName.getText().toString().isEmpty()) {
            inputName.setError("Insira o nome");
            return false;
        }
        if (inputEmail.getText().toString().isEmpty()) {
            inputEmail.setError("Insira o e-mail");
            return false;
        }
        if (inputDateOfBirth.getText().toString().isEmpty()) {
            inputDateOfBirth.setError("Insira a data de nascimento");
            return false;
        } else if (!isValidDate(inputDateOfBirth.getText().toString())) {
            inputDateOfBirth.setError("Data inválida, use o formato dd/mm/yyyy");
            return false;
        }
        if (inputPassword.getText().toString().isEmpty()) {
            inputPassword.setError("Insira a senha");
            return false;
        }
        return true;
    }

    // Método para validar se a data está no formato correto (dd/mm/yyyy)
    private boolean isValidDate(String date) {
        String[] parts = date.split("/");
        if (parts.length != 3) return false;
        int day, month, year;
        try {
            day = Integer.parseInt(parts[0]);
            month = Integer.parseInt(parts[1]);
            year = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            return false;
        }

        if (month < 1 || month > 12) return false;
        if (day < 1 || day > 31) return false;
        if (year < 1900 || year > 2100) return false;
        return true;
    }
}
