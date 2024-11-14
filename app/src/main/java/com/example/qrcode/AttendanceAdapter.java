package com.example.qrcode;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {

    private final List<String> scannedStudents;

    public AttendanceAdapter(List<String> scannedStudents) {
        this.scannedStudents = scannedStudents;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance, parent, false);
        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        String studentRgm = scannedStudents.get(position);
        holder.tvRGM.setText("RGM: " + studentRgm);
    }

    @Override
    public int getItemCount() {
        return scannedStudents.size();
    }

    static class AttendanceViewHolder extends RecyclerView.ViewHolder {
        TextView tvRGM;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRGM = itemView.findViewById(R.id.tvRGM);
        }
    }
}
