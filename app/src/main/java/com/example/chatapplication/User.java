package com.example.chatapplication;

public class User {
    private String uID, Name, phoneNumber, profileImage;

    public User(){

    }

    public User(String uID, String name, String phoneNumber, String profileImage) {
        this.uID = uID;
        Name = name;
        this.phoneNumber = phoneNumber;
        this.profileImage = profileImage;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

}
