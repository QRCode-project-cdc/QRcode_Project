package com.example.qrcode;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ActiveEventsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewEvents;
    private ActiveEventsAdapter adapter;
    private List<Event> eventList;
    private DatabaseHelper databaseHelper; // Adicionando a instância do DatabaseHelper

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_active_events);

        // Configuração do padding para o layout principal
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização do RecyclerView
        recyclerViewEvents = findViewById(R.id.recyclerViewEvents);
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(this));

        // Inicializa a lista de eventos
        eventList = new ArrayList<>();

        // Inicializa o DatabaseHelper
        databaseHelper = DatabaseHelper.getInstance(this);

        // Carrega eventos do banco de dados
        loadEventsFromDatabase();

        // Configura o adapter
        adapter = new ActiveEventsAdapter(this, eventList);
        recyclerViewEvents.setAdapter(adapter);
    }

    private void loadEventsFromDatabase() {
        Cursor cursor = databaseHelper.getAllEvents();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_TITLE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_DESCRIPTION));
                eventList.add(new Event(id, title, description));
            }
            cursor.close(); // Feche o cursor após o uso
        }
    }
}