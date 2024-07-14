package com.example.hairmosa.models;

public class Branch {

    public String branchName;
    public Integer id;
    public String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }



    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    @Override
    public String toString() {
        return branchName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Branch() {
    }


    public Branch(String branchName, Integer id) {
        this.branchName = branchName;
        this.id = id;
    }
}
