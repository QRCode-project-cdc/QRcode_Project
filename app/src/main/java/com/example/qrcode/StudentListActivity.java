package com.example.qrcode;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StudentListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private List<String> studentsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewStudentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load student data (for now it's hardcoded, replace with your actual data source)
        studentsList = loadStudentData();

        // Set the adapter for RecyclerView
        adapter = new StudentAdapter(studentsList);
        recyclerView.setAdapter(adapter);
    }

    // Example method to load student data (replace with your database logic)
    private List<String> loadStudentData() {
        List<String> students = new ArrayList<>();
        students.add("Student 1");
        students.add("Student 2");
        students.add("Student 3");
        return students;
    }
}
