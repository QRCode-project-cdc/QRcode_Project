package com.example.qrcode;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SubdEvents extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RegisteredEventAdapter eventAdapter; // Alterado para RegisteredEventAdapter
    private DatabaseHelper dbHelper;
    private String currentStudentRGM; // RGM do aluno atual

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subd_events);

        // Obtenha o RGM do aluno da Intent
        currentStudentRGM = getIntent().getStringExtra("RGM"); // Obtenha o RGM do aluno

        recyclerView = findViewById(R.id.recyclerViewRegisteredEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = DatabaseHelper.getInstance(this); // Usando DatabaseHelper

        // Inicializa o adapter com uma lista vazia e configura o RecyclerView
        eventAdapter = new RegisteredEventAdapter(new ArrayList<>(), new RegisteredEventAdapter.OnEventClickListener() {
            @Override
            public void onUnregisterClick(Event event) {
                // Lógica para desinscrever o aluno do evento
                boolean success = dbHelper.removeRegistration(currentStudentRGM, event.getId());
                if (success) {
                    Toast.makeText(SubdEvents.this, "Desinscrição realizada do evento: " + event.getTitle(), Toast.LENGTH_SHORT).show();
                    loadRegisteredEvents(); // Atualiza a lista após a desinscrição
                } else {
                    Toast.makeText(SubdEvents.this, "Erro ao desinscrever do evento.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onGenerateQRCodeClick(Event event) {
                Intent intent = new Intent(SubdEvents.this, QRCodeActivity.class);
                intent.putExtra("EVENT_ID", event.getId()); // Passa o ID do evento
                intent.putExtra("EVENT_TITLE", event.getTitle()); // Passa o título do evento
                intent.putExtra("USER_RGM", currentStudentRGM); // Passa o RGM do aluno
                startActivity(intent); // Inicia a QRCodeActivity
            }
        });
        recyclerView.setAdapter(eventAdapter);

        // Carrega os eventos inscritos do DatabaseHelper
        loadRegisteredEvents();
    }

    private void loadRegisteredEvents() {
        try {
            List<Event> eventList = new ArrayList<>();
            // Obtenha os eventos registrados usando o RGM
            Cursor cursor = dbHelper.getRegisteredEvents(currentStudentRGM); // Alterado para passar currentStudentRGM

            if (cursor != null) {
                Log.d("SubdEvents", "Cursor obtido, número de eventos: " + cursor.getCount()); // Log do número de eventos

                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_ID));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_TITLE));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_DESCRIPTION));
                    // Adicione o evento à lista
                    eventList.add(new Event(id, title, description));

                    // Log do ID do evento
                    Log.d("SubdEvents", "Evento ID: " + id + ", Título: " + title);
                }
                cursor.close(); // Fechar o cursor após o uso
            } else {
                Log.d("SubdEvents", "Cursor é nulo."); // Log se o cursor for nulo
            }

            Log.d("SubdEvents", "Eventos inscritos carregados: " + eventList.size());
            if (!eventList.isEmpty()) {
                eventAdapter.setEvents(eventList); // Atualiza a lista de eventos no adapter
            } else {
                Toast.makeText(this, "Nenhum evento inscrito.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao carregar eventos inscritos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("SubdEvents", "Erro ao carregar eventos inscritos", e);
        }
    }
}