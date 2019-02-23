package com.elenaneacsu.tripjournal.trips.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Trip implements Parcelable {

    public enum TripType {Mountains, Seaside, City_Break;}

    private String name;

    private String destination;
    private TripType type;
    private long price;
    private long startDate;
    private long endDate;
    private long rating;
    private String image;
    private boolean isFavourite;

    public Trip() {

    }

    public Trip(String name, String destination, TripType type, long price, long startDate, long endDate, long rating, String image, boolean isFavourite) {
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

    public Trip(String name, String destination, TripType tripType, long price, long startDate, long endDate, long rating, String image) {
        this.name = name;
        this.destination = destination;
        this.type = tripType;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rating = rating;
        this.image = image;
    }

    public Trip(String name, String destination, long price, long rating, String image) {
        this.name = name;
        this.destination = destination;
        this.price = price;
        this.rating = rating;
        this.image = image;
    }

    public Trip(String name, String destination, TripType type, long price, long startDate, long endDate, long rating) {
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

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
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
        price = in.readLong();
        startDate = in.readLong();
        endDate = in.readLong();
        rating = in.readLong();
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
        dest.writeLong(price);
        dest.writeLong(startDate);
        dest.writeLong(endDate);
        dest.writeLong(rating);
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

    @Override
    public String toString() {
        return "Trip{" +
                "name='" + name + '\'' +
                ", destination='" + destination + '\'' +
                ", type=" + type +
                ", price=" + price +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", rating=" + rating +
                ", image='" + image + '\'' +
                ", isFavourite=" + isFavourite +
                '}';
    }
}