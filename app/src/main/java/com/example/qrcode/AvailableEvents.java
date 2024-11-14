package com.example.qrcode;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AvailableEvents extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private DatabaseHelper dbHelper;
    private Button buttonViewRegisteredEvents; // Novo botão
    private String currentStudentRGM; // Alterado para RGM

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_events);

        // Supondo que você tenha o RGM do aluno de alguma forma (por exemplo, passado pela Intent)
        currentStudentRGM = getIntent().getStringExtra("RGM"); // Obtenha o RGM do aluno

        recyclerView = findViewById(R.id.recyclerViewEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = DatabaseHelper.getInstance(this); // Usando DatabaseHelper

        // Inicializa o adapter com uma lista vazia e configura o RecyclerView
        eventAdapter = new EventAdapter(new ArrayList<>(), event -> {
            // Inscrição no evento
            if (currentStudentRGM != null) {
                // Verifica se o aluno já está inscrito
                if (dbHelper.isStudentRegistered(currentStudentRGM, event.getId())) {
                    Toast.makeText(this, "Você já está inscrito neste evento.", Toast.LENGTH_SHORT).show();
                } else {
                    boolean success = dbHelper.addRegistration(currentStudentRGM, event.getId()); // Alterado para usar currentStudentRGM
                    if (success) {
                        Toast.makeText(this, "Inscrição realizada no evento: " + event.getTitle(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Erro ao se inscrever no evento.", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(this, "Aluno não encontrado.", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(eventAdapter);

        // Configura o botão para ver eventos inscritos
        buttonViewRegisteredEvents = findViewById(R.id.buttonViewRegisteredEvents);
        buttonViewRegisteredEvents.setOnClickListener(v -> {
            if (currentStudentRGM != null) {
                Intent intent = new Intent(AvailableEvents.this, SubdEvents.class);
                intent.putExtra("RGM", currentStudentRGM); // Passa o RGM do aluno
                startActivity(intent);
            } else {
                Toast.makeText(this, "Aluno não encontrado.", Toast.LENGTH_SHORT).show();
            }
        });

        // Carrega os eventos do DatabaseHelper
        loadEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Atualiza a lista de eventos sempre que a Activity é retomada
        loadEvents();
    }

    private void loadEvents() {
        try {
            List<Event> eventList = new ArrayList<>();
            Cursor cursor = dbHelper.getAllEvents(); // Obtendo o cursor de eventos

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_ID));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_TITLE));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_DESCRIPTION));
                    // Adicione o evento à lista
                    eventList.add(new Event(id, title, description));
                }
                cursor.close(); // Fechar o cursor após o uso
            }

            Log.d("AvailableEvents", "Eventos carregados: " + eventList.size());
            if (!eventList.isEmpty()) {
                eventAdapter.setEvents(eventList); // Atualiza a lista de eventos no adapter
            } else {
                Toast.makeText(this, "Nenhum evento disponível.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao carregar eventos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("AvailableEvents", "Erro ao carregar eventos", e);
        }
    }
}