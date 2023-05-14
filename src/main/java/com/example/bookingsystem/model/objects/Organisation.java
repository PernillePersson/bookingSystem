package com.example.bookingsystem.model.objects;

public class Organisation {
    private int id;
    private String organisation;

    public Organisation(int id, String org) {
        this.id = id;
        this.organisation = org;
    }

    @Override
    public String toString() {
        return organisation;
    }

    public int getId() {
        return id;
    }

    public String getOrganisation() {
        return organisation;
    }

}
