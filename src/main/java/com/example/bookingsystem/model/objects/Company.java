package com.example.bookingsystem.model.objects;

public class Company {
    private int id;
    private String company;

    public Company(int id, String company) {
        this.id = id;
        this.company = company;
    }

    @Override
    public String toString() {
        return company;
    }

    public int getId() {
        return id;
    }

    public String getCompany() {
        return company;
    }
}
