package com.example.fragmentsv2;

public class Accommodation {

    public String address, houseType, contact;
    //houseType = detatched, semi detatched, terrace, apartment
    //use a spinner same way I use year of study
    public int price, numOfBedrooms, spacesAvailable;
    public float longitude, latitude;



    public Accommodation(){
    }

    public Accommodation(String address, String houseType, String contact, int price, int numOfBedrooms, int spacesAvailable, float longitude, float latitude) {
        this.address = address;     // got from auto complete fragment
        this.houseType = houseType;
        this.contact = contact;
        this.price = price;
        this.numOfBedrooms = numOfBedrooms;
        this.spacesAvailable = spacesAvailable;
        this.longitude = longitude; // got from auto complete fragment
        this.latitude = latitude;   // got from auto complete fragment
    }
}
