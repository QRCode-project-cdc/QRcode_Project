package com.example.qrcode;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RegisteredEventAdapter extends RecyclerView.Adapter<RegisteredEventAdapter.EventViewHolder> {
    private List<Event> events;
    private OnEventClickListener listener;

    public RegisteredEventAdapter(List<Event> events, OnEventClickListener listener) {
        this.events = events;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_registered, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = events.get(position);
        holder.textViewEventTitle.setText(event.getTitle());

        holder.buttonUnregister.setOnClickListener(v -> {
            // Chame o listener para desinscrever
            listener.onUnregisterClick(event);
        });

        holder.buttonGenerateQRCode.setOnClickListener(v -> {
            // Chame o listener para gerar QR Code
            listener.onGenerateQRCodeClick(event);
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void setEvents(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView textViewEventTitle;
        Button buttonUnregister;
        Button buttonGenerateQRCode;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewEventTitle = itemView.findViewById(R.id.textViewEventTitle);
            buttonUnregister = itemView.findViewById(R.id.buttonUnregister);
            buttonGenerateQRCode = itemView.findViewById(R.id.buttonGenerateQRCode);
        }
    }

    public interface OnEventClickListener {
        void onUnregisterClick(Event event);
        void onGenerateQRCodeClick(Event event);
    }
}