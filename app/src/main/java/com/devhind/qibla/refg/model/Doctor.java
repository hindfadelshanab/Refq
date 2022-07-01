package com.devhind.qibla.refg.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Doctor implements Parcelable {

    String id ;
    String name ;
    String image ;
    String email ;
    String firstName ;
    String lastName;
    String birthday;
    String phoneNumber ;
    String  speacliztion ;
    String city ;
    String address;
    String cv ;

    String fcmToken;

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public Doctor(String id, String name, String image, String email, String firstName, String lastName, String birthday,
                  String phoneNumber, String speacliztion, String city, String address, String cv) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.speacliztion = speacliztion;
        this.city = city;
        this.address = address;
        this.cv = cv;
    }

    public Doctor() {
    }

    protected Doctor(Parcel in) {
        id = in.readString();
        name = in.readString();
        image = in.readString();
        email = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        birthday = in.readString();
        phoneNumber = in.readString();
        speacliztion = in.readString();
        city = in.readString();
        address = in.readString();
        cv = in.readString();
    }

    public static final Creator<Doctor> CREATOR = new Creator<Doctor>() {
        @Override
        public Doctor createFromParcel(Parcel in) {
            return new Doctor(in);
        }

        @Override
        public Doctor[] newArray(int size) {
            return new Doctor[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSpeacliztion() {
        return speacliztion;
    }

    public void setSpeacliztion(String speacliztion) {
        this.speacliztion = speacliztion;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(image);
        parcel.writeString(email);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(birthday);
        parcel.writeString(phoneNumber);
        parcel.writeString(speacliztion);
        parcel.writeString(city);
        parcel.writeString(address);
        parcel.writeString(cv);
    }
}
