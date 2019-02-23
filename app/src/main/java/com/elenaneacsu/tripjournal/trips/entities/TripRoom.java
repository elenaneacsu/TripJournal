package com.elenaneacsu.tripjournal.trips.entities;

import java.util.Calendar;

public class TripRoom {
    private String name;
    private String destination;
    private String type;
    private double price;
    private float startDate;
    private float endDate;
    private float rating;
    private byte[] image;

    public TripRoom(String name, String destination, String type, double price, float startDate, float endDate, float rating, byte[] image) {
        this.name = name;
        this.destination = destination;
        this.type = type;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rating = rating;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public float getStartDate() {
        return startDate;
    }

    public void setStartDate(float startDate) {
        this.startDate = startDate;
    }

    public float getEndDate() {
        return endDate;
    }

    public void setEndDate(float endDate) {
        this.endDate = endDate;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
