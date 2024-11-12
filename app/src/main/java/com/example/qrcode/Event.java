package com.example.qrcode;

public class Event {

    private String title;
    private String description;

    // Construtor
    public Event(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // Métodos getter
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
