package com.example.ptc_trial.Models;

public class Message {
    String uid,message,messageId;
    Long time;

    public Message(String uid, String message, String messageId, Long time) {
        this.uid = uid;
        this.message = message;
        this.messageId = messageId;
        this.time = time;
    }

    public Message(String uid, String message) {
        this.uid = uid;
        this.message = message;
    }

    public Message() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
