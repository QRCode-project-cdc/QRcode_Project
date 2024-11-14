package com.example.qrcode;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AttendanceList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AttendanceAdapter adapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list);

        recyclerView = findViewById(R.id.recyclerViewAttendance);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = DatabaseHelper.getInstance(this);

        // Recupera o evento selecionado (substituir eventId com a lógica correta)
        int eventId = getIntent().getIntExtra("EVENT_ID", -1);

        if (eventId == -1) {
            Toast.makeText(this, "Evento inválido!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        List<String> scannedStudents = databaseHelper.getScannedStudents(eventId);

        if (scannedStudents != null && !scannedStudents.isEmpty()) {
            adapter = new AttendanceAdapter(scannedStudents);
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Nenhum aluno presente no evento.", Toast.LENGTH_SHORT).show();
        }
    }
}
