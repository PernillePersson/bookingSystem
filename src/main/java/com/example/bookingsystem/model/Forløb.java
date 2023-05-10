package com.example.bookingsystem.model;

public class Forløb {
    private int id;
    private String forløb;

    public Forløb(int id, String forløb) {
        this.id = id;
        this.forløb = forløb;
    }

    public int getId() {
        return id;
    }

    public String getForløb() {
        return forløb;
    }
}
