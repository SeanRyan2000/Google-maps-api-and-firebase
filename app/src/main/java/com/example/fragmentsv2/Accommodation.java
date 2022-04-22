package com.example.fragmentsv2;

import android.os.Parcel;
import android.os.Parcelable;

public class Accommodation implements Parcelable {

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

    protected Accommodation(Parcel in) {
        address = in.readString();
        houseType = in.readString();
        contact = in.readString();
        imageUrl = in.readString();
        numOfBedrooms = in.readInt();
        spacesAvailable = in.readInt();
        price = in.readDouble();
        longitude = in.readDouble();
        latitude = in.readDouble();
    }

    public static final Creator<Accommodation> CREATOR = new Creator<Accommodation>() {
        @Override
        public Accommodation createFromParcel(Parcel in) {
            return new Accommodation(in);
        }

        @Override
        public Accommodation[] newArray(int size) {
            return new Accommodation[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(address);
        parcel.writeString(houseType);
        parcel.writeString(contact);
        parcel.writeString(imageUrl);
        parcel.writeInt(numOfBedrooms);
        parcel.writeInt(spacesAvailable);
        parcel.writeDouble(price);
        parcel.writeDouble(longitude);
        parcel.writeDouble(latitude);
    }
}
