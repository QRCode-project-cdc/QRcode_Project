package com.example.qrcode;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateEvent extends AppCompatActivity{

    private EditText edtNomeEvento, edtTempoEvento, edtDescEvento;
    private Button buttonCadEvento;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        edtNomeEvento = findViewById(R.id.edtNomeEvento);
        edtTempoEvento = findViewById(R.id.edtTempoEvento);
        edtDescEvento = findViewById(R.id.edtDescEvento);
        // esses dados devem ir para o banco de dados

        Button buttonCadEvento = findViewById(R.id.buttonCadEvento);
        buttonCadEvento.setOnClickListener(v -> {
            Toast.makeText(CreateEvent.this, "Evento criado com sucesso'", Toast.LENGTH_SHORT).show();
            //Intent CreateEventIntent = new Intent(CreateEvent.this, eventos_disponiveis.class); ainda a ser implementado
            //startActivity(eventos_disponiveis);
        });
    }
}
