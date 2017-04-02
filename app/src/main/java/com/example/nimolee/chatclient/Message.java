package com.example.nimolee.chatclient;

public class Message {
    String name;
    String date;
    String massege;

    public Message(String msg) {
        date = msg.split("\n")[1];
        name = msg.split("\n")[2];
        massege = msg.split("\n")[3];
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getMassege() {
        return massege;
    }
}
