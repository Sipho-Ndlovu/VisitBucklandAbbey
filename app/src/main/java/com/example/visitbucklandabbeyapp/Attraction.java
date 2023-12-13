package com.example.visitbucklandabbeyapp;

public class Attraction {
    private int id;
    private String name;
    private String facilities;
    private String openingTime;
    private String ticketPrices;

    public Attraction(){}
    public Attraction(int id , String name, String facilities, String openingTime, String ticketPrices){
        this.id = id;
        this.name = name;
        this.facilities = facilities;
        this.openingTime = openingTime;
        this.ticketPrices = ticketPrices;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getTicketPrices() {
        return ticketPrices;
    }

    public void setTicketPrices(String ticketPrices) {
        this.ticketPrices = ticketPrices;
    }
}
