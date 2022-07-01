package com.devhind.qibla.refg.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Request implements Parcelable {

    private String requestId ;
    private Doctor doctorRequest ;
    private Boolean isAccept ;
    private String oldId;
    private String doctorId;
    private String orderID ;

    public Request() {
    }

    protected Request(Parcel in) {
        requestId = in.readString();
        doctorRequest = in.readParcelable(Doctor.class.getClassLoader());
        byte tmpIsAccept = in.readByte();
        isAccept = tmpIsAccept == 0 ? null : tmpIsAccept == 1;
        oldId = in.readString();
        doctorId = in.readString();
        orderID = in.readString();
    }

    public static final Creator<Request> CREATOR = new Creator<Request>() {
        @Override
        public Request createFromParcel(Parcel in) {
            return new Request(in);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public Request(String requestId, Doctor doctorRequest, Boolean isAccept, String oldId, String orderID) {
        this.requestId = requestId;
        this.doctorRequest = doctorRequest;
        this.isAccept = isAccept;
        this.oldId = oldId;
        this.orderID = orderID;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Doctor getDoctorRequest() {
        return doctorRequest;
    }

    public void setDoctorRequest(Doctor doctorRequest) {
        this.doctorRequest = doctorRequest;
    }

    public Boolean getAccept() {
        return isAccept;
    }

    public void setAccept(Boolean accept) {
        isAccept = accept;
    }

    public String getOldId() {
        return oldId;
    }

    public void setOldId(String oldId) {
        this.oldId = oldId;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(requestId);
        parcel.writeParcelable(doctorRequest, i);
        parcel.writeByte((byte) (isAccept == null ? 0 : isAccept ? 1 : 2));
        parcel.writeString(oldId);
        parcel.writeString(doctorId);
        parcel.writeString(orderID);
    }
}
