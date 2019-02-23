package com.elenaneacsu.tripjournal.trips.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

public class Trip implements Parcelable {
    public enum TripType {Mountains, Seaside, City_Break};

    private String name;
    private String destination;
    private TripType type;
    private double price;
    private Calendar startDate;
    private Calendar endDate;
    private float rating;
    private String image;
    private boolean isFavourite;

    public Trip(String name, String destination, TripType type, double price, Calendar startDate, Calendar endDate, float rating, String image, boolean isFavourite) {
        this.name = name;
        this.destination = destination;
        this.type = type;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rating = rating;
        this.image = image;
        this.isFavourite = isFavourite;
    }

    public Trip(String name, String destination, TripType tripType, double price, Calendar startDate, Calendar endDate, float rating, String image) {
        this.name = name;
        this.destination = destination;
        this.type = tripType;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rating = rating;
        this.image = image;
    }

    public Trip(String name, String destination, double price, float rating, String image) {
        this.name = name;
        this.destination = destination;
        this.price = price;
        this.rating = rating;
        this.image = image;
    }

    public Trip(String name, String destination, TripType type, double price, Calendar startDate, Calendar endDate, float rating) {
        this.name = name;
        this.destination = destination;
        this.type = type;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rating = rating;
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

    public TripType getType() {
        return type;
    }

    public void setType(TripType tripType) {
        this.type = tripType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    protected Trip(Parcel in) {
        name = in.readString();
        destination = in.readString();
        type = (TripType) in.readValue(TripType.class.getClassLoader());
        price = in.readDouble();
        startDate = (Calendar) in.readValue(Calendar.class.getClassLoader());
        endDate = (Calendar) in.readValue(Calendar.class.getClassLoader());
        rating = in.readFloat();
        image = in.readString();
        isFavourite = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(destination);
        dest.writeValue(type);
        dest.writeDouble(price);
        dest.writeValue(startDate);
        dest.writeValue(endDate);
        dest.writeFloat(rating);
        dest.writeString(image);
        dest.writeByte((byte) (isFavourite ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Trip> CREATOR = new Parcelable.Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };
}