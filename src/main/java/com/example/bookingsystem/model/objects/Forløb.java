package com.example.bookingsystem.model.objects;

public class Forløb {
    private int id;
    private String forløb;

    public Forløb(int id, String forløb) {
        this.id = id;
        this.forløb = forløb;
    }

    @Override
    public String toString() {
        return forløb;
    }

    public int getId() {
        return id;
    }

    public String getForløb() {
        return forløb;
    }
}
