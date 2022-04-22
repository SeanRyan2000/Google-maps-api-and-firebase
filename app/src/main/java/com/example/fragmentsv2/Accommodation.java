package com.example.fragmentsv2;

public class Accommodation {

    public String address, houseType, contact, imageUrl;
    //houseType = detatched, semi detatched, terrace, apartment
    //use a spinner same way I use year of study
    public int numOfBedrooms, spacesAvailable;
    public double price, longitude, latitude;



    public Accommodation(){
    }


    public Accommodation(String address, String houseType, String contact, String imageUrl, double price, int numOfBedrooms, int spacesAvailable, double longitude, double latitude) {
        this.address = address;     // got from auto complete fragment
        this.houseType = houseType;
        this.contact = contact;
        this.imageUrl =imageUrl;
        this.price = price;
        this.numOfBedrooms = numOfBedrooms;
        this.spacesAvailable = spacesAvailable;
        this.longitude = longitude; // got from auto complete fragment
        this.latitude = latitude;   // got from auto complete fragment
    }

    public String getAddress() {
        return address;
    }

    public String getHouseType() {
        return houseType;
    }

    public String getContact() {
        return contact;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getNumOfBedrooms() {
        return numOfBedrooms;
    }

    public int getSpacesAvailable() {
        return spacesAvailable;
    }

    public double getPrice() {
        return price;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
