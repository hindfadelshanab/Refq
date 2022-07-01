package com.devhind.qibla.refg.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Order implements Parcelable {

private String orderId ;
private String serviceType;
private  String city ;
private String startTime ;
private String endTime ;
private String date ;
private String oldName ;
private String oldId ;
private String oldImage ;
private Boolean isAccept;

    public Order() {
    }

    public Order(String orderId, String serviceType, String city, String startTime, String endTime, String date, String oldName, String oldId, String oldImage, Boolean isAccept) {
        this.orderId = orderId;
        this.serviceType = serviceType;
        this.city = city;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.oldName = oldName;
        this.oldId = oldId;
        this.oldImage = oldImage;
        this.isAccept = isAccept;
    }

    protected Order(Parcel in) {
        orderId = in.readString();
        serviceType = in.readString();
        city = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        date = in.readString();
        oldName = in.readString();
        oldId = in.readString();
        oldImage = in.readString();
        byte tmpIsAccept = in.readByte();
        isAccept = tmpIsAccept == 0 ? null : tmpIsAccept == 1;
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public String getOldId() {
        return oldId;
    }

    public void setOldId(String oldId) {
        this.oldId = oldId;
    }

    public String getOldImage() {
        return oldImage;
    }

    public void setOldImage(String oldImage) {
        this.oldImage = oldImage;
    }

    public Boolean getAccept() {
        return isAccept;
    }

    public void setAccept(Boolean accept) {
        isAccept = accept;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(orderId);
        parcel.writeString(serviceType);
        parcel.writeString(city);
        parcel.writeString(startTime);
        parcel.writeString(endTime);
        parcel.writeString(date);
        parcel.writeString(oldName);
        parcel.writeString(oldId);
        parcel.writeString(oldImage);
        parcel.writeByte((byte) (isAccept == null ? 0 : isAccept ? 1 : 2));
    }
}
