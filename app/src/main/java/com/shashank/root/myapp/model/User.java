package com.shashank.root.myapp.model;

public class User {

    String address;
    String email;
    String name;
    String password;
    String status;
    String date;
    String profile_img;
    String compressor_image;

    public User() {
    }




    public User(String address, String email, String name, String password, String status,
                String date, String profile_img, String compressor_image) {
        this.address = address;
        this.email = email;
        this.name = name;
        this.password = password;
        this.status = status;
        this.date = date;
        this.profile_img = profile_img;
        this.compressor_image = compressor_image;


    }

    public String getCompressor_image() {
        return compressor_image;
    }

    public void setCompressor_image(String compressor_image) {
        this.compressor_image = compressor_image;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
