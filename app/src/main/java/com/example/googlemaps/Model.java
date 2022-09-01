package com.example.googlemaps;

public class Model {

    String Address, AddressTwo, HeadLine, description;
    Double latitude;
    Double longitude;


    public Model() {

    }



    public Model(String address, String addressTwo, String headLine, String description, Double latitude, Double longitude) {
        Address = address;
        AddressTwo = addressTwo;
        HeadLine = headLine;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getAddressTwo() {
        return AddressTwo;
    }

    public void setAddressTwo(String addressTwo) {
        AddressTwo = addressTwo;
    }

    public String getHeadLine() {
        return HeadLine;
    }

    public void setHeadLine(String headLine) {
        HeadLine = headLine;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
