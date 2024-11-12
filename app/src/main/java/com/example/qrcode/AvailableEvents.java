package com.example.qrcode;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AvailableEvents extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_events);

        recyclerView = findViewById(R.id.recyclerViewEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        eventList = new ArrayList<>();
        eventList.add(new Event("Evento 1", "Descrição do Evento 1"));
        eventList.add(new Event("Evento 2", "Descrição do Evento 2"));
        eventList.add(new Event("Evento 3", "Descrição do Evento 3"));

        eventAdapter = new EventAdapter(eventList, event -> {
            // Ação quando o evento for clicado (por exemplo, abrir detalhes do evento)
        });
        recyclerView.setAdapter(eventAdapter);
    }
}
