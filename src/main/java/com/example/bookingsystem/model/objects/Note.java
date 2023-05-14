package com.example.bookingsystem.model.objects;

public class Note {
    private int id;
    private String note;

    public Note(int id, String note) {
        this.id = id;
        this.note = note;
    }

    @Override
    public String toString() {
        return note;
    }

    public int getId() {
        return id;
    }

    public String getNote() {
        return note;
    }
}
