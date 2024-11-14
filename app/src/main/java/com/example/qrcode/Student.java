package com.example.qrcode;

public class Student {
    private String rgm;
    private String name;
    private String email;

    // Constructor
    public Student(String rgm, String name, String email) {
        this.rgm = rgm;
        this.name = name;
        this.email = email;
    }

    // Getters
    public String getRgm() {
        return rgm;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}

