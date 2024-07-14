package com.example.hairmosa.models;

public class User {

    public String fullName;
    public UserType userType;

    public User() {
    }


    public User(String fullName, UserType userType) {
        this.fullName = fullName;
        this.userType = userType;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

}
