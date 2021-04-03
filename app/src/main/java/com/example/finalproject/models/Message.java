package com.example.finalproject.models;

/**
 * Message class is used to store information about the NASA earth image
 */
public class Message {

    // Here we make private variables for each data point
    private final String date;
    private final String longitude;
    private final String latitude;
    private final String imageName;
    private final long ID;

    // Constructor used to add the values to the object of this class
    public Message(String date, String longitude, String latitude, String imageName, long ID) {
        this.date = date;
        this.longitude = longitude;
        this.latitude = latitude;
        this.imageName = imageName;
        this.ID = ID;
    }

    // Various methods that return the data stored in the objects of this class.
    public String getDate() {
        return date;
    }
    public String getLongitude() {
        return longitude;
    }
    public String getLatitude() {
        return latitude;
    }
    public String getImageName() {
        return imageName;
    }
    public long getId() {
        return ID;
    }
}