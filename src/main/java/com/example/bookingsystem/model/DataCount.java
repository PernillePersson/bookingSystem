package com.example.bookingsystem.model;

import javafx.scene.chart.PieChart;

public class DataCount {

//

    private int boFoID;

    private int boID;

    private int foID;

    public DataCount(int boFoID, int boID, int foID){
        this.boFoID = boFoID;
        this.boID = boID;
        this.foID = foID;
    }

    public int getBoFoID() {
        return boFoID;
    }

    public void setBoFoID(int boFoID) {
        this.boFoID = boFoID;
    }

    public int getBoID() {
        return boID;
    }

    public void setBoID(int boID) {
        this.boID = boID;
    }

    public int getFoID() {
        return foID;
    }

    public void setFoID(int foID) {
        this.foID = foID;
    }
}