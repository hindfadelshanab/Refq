package com.devhind.qibla.refg.model;

import java.util.Date;

public class ChatMessage {
    public String senderId , receiverId ,receiverName , receiverImage, message ,messageId,messageImage, dateTime;

    public Long time ;
    private Request request ;
    public Date dateObject;

    public  String converstionId , converstionName , converstionImage;

    public ChatMessage() {
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverImage() {
        return receiverImage;
    }

    public void setReceiverImage(String receiverImage) {
        this.receiverImage = receiverImage;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public ChatMessage(String senderId, String receiverId, String message, String messageId, String messageImage, String dateTime,
                       Date dateObject, String converstionId, String converstionName, String converstionImage) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.messageId = messageId;
        this.messageImage = messageImage;
        this.dateTime = dateTime;
        this.dateObject = dateObject;
        this.converstionId = converstionId;
        this.converstionName = converstionName;
        this.converstionImage = converstionImage;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageImage() {
        return messageImage;
    }

    public void setMessageImage(String messageImage) {
        this.messageImage = messageImage;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Date getDateObject() {
        return dateObject;
    }

    public void setDateObject(Date dateObject) {
        this.dateObject = dateObject;
    }

    public String getConverstionId() {
        return converstionId;
    }

    public void setConverstionId(String converstionId) {
        this.converstionId = converstionId;
    }

    public String getConverstionName() {
        return converstionName;
    }

    public void setConverstionName(String converstionName) {
        this.converstionName = converstionName;
    }

    public String getConverstionImage() {
        return converstionImage;
    }

    public void setConverstionImage(String converstionImage) {
        this.converstionImage = converstionImage;
    }
}
