package com.example.favrouter.backendRecycler;

public class DataProvider {
    String location;
    double latitude;
    double longitude;
    long id;

    public DataProvider(long id,String location, double latitude, double longitude) {
        this.location = location;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public DataProvider() {

    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitudo) {
        this.latitude = latitudo;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitudoe) {
        this.longitude = longitudoe;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
