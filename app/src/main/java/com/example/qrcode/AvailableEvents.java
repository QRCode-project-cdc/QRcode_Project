package com.example.qrcode;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AvailableEvents extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_events); // Referencia o layout que vamos criar

        textView = findViewById(R.id.textView);  // A referência para o TextView que exibirá os eventos
        displayEvents();
    }

    private void displayEvents() {
        // Dados de exemplo (simulação de eventos)
        String eventos = "Evento 1: Nome\n\n" +
                "Data: 12/12/2024\n" +
                "Descrição: Evento tal.\n\n" ;

        // Exibe os eventos no TextView
        textView.setText(eventos);
    }
}
