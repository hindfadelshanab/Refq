package com.devhind.qibla.refg.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    String id ;
    String name ;
    String image ;
    String email ;
    String accountType ;
    String token ;
    String address ;
    String age ;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getToken() {
        return token;
    }

    public User(String id, String name, String image, String email, String accountType, String token) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.email = email;
        this.accountType = accountType;
        this.token = token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User() {
    }

    public User(String id, String name, String image, String email, String accountType) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.email = email;
        this.accountType = accountType;
    }

    protected User(Parcel in) {
        id = in.readString();
        name = in.readString();
        image = in.readString();
        email = in.readString();
        accountType = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
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
        parcel.writeString(accountType);
    }
}
