package com.example.qrcode;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> eventList;
    private final OnEventClickListener listener;

    public EventAdapter(List<Event> eventList, OnEventClickListener listener) {
        this.eventList = eventList;
        this.listener = listener;
    }

    public void setEvents(List<Event> newEventList) {
        this.eventList = newEventList;
        notifyDataSetChanged(); // Atualiza a RecyclerView com a nova lista
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.bind(event, listener);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView descriptionTextView;
        private final Button subscribeButton;

        public EventViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textViewEventTitle);
            descriptionTextView = itemView.findViewById(R.id.textViewEventDescription);
            subscribeButton = itemView.findViewById(R.id.buttonSubscribe);
        }

        public void bind(Event event, OnEventClickListener listener) {
            titleTextView.setText(event.getTitle());
            descriptionTextView.setText(event.getDescription());
            subscribeButton.setOnClickListener(v -> listener.onEventClick(event));
        }
    }

    public interface OnEventClickListener {
        void onEventClick(Event event);
    }
}
