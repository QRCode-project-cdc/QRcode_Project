package com.example.qrcode;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<String> studentList;

    // Constructor
    public StudentAdapter(List<String> studentList) {
        this.studentList = studentList;
    }

    @Override
    public StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the item layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StudentViewHolder holder, int position) {
        // Get the current student name and bind it to the TextView
        String student = studentList.get(position);
        holder.textViewStudentName.setText(student);
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    // ViewHolder to hold the item views
    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView textViewStudentName;

        public StudentViewHolder(View itemView) {
            super(itemView);
            // Bind the TextView from your item layout
            textViewStudentName = itemView.findViewById(R.id.textViewStudentName);
        }
    }
}
