package com.shashank.root.myapp.model;

public class Message {

    String message;
    String from;
    String type;



    public Message(){

    }

    public Message(String message, String from, String type) {
        this.message = message;
        this.from = from;
        this.type = type;

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
