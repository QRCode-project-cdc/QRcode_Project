package com.example.qrcode;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AvailableEvents extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private SQLiteHelper dbHelper;
    private List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_events);

        recyclerView = findViewById(R.id.recyclerViewEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new SQLiteHelper(this);

        // Carrega os eventos do SQLite
        loadEvents();
    }

    private void loadEvents() {
        eventList = dbHelper.getAllEvents();
        eventAdapter = new EventAdapter(eventList, event -> {

            // Pode implementar a inscrição aqui
            Toast.makeText(this, "Inscrição realizada no evento!", Toast.LENGTH_SHORT).show();
        });
        recyclerView.setAdapter(eventAdapter);
    }

    private void refreshEvents() {
        eventList.clear();
        eventList.addAll(dbHelper.getAllEvents());
        eventAdapter.notifyDataSetChanged();
    }
}
