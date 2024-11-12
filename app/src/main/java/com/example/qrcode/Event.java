package com.example.qrcode;

public class Event {
    private int id;
    private String title;
    private String description;

    public Event(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    // Método para obter o ID do evento
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    // Outros métodos, se necessário
}