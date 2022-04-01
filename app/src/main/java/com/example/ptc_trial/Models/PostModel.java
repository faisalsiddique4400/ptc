package com.example.ptc_trial.Models;

import java.io.Serializable;

public class PostModel implements Serializable {
    String uid, name, age, description, location, color, price, pictureUri;

    public PostModel() {
    }

    public PostModel(String uid, String name, String age, String description, String location, String color, String price) {
        this.uid = uid;
        this.name = name;
        this.age = age;
        this.description = description;
        this.location = location;
        this.color = color;
        this.price = price;
    }

    public PostModel(String uid, String name, String age, String description, String location, String color, String price, String pictureUri) {
        this.uid = uid;
        this.name = name;
        this.age = age;
        this.description = description;
        this.location = location;
        this.color = color;
        this.price = price;
        this.pictureUri = pictureUri;
    }

    public String getPictureUri() {
        return pictureUri;
    }

    public void setPictureUri(String pictureUri) {
        this.pictureUri = pictureUri;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
