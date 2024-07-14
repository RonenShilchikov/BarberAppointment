package com.example.hairmosa.models;

public class Empolyee {

    public Integer id;
    public Integer branchId;
    public String fullName;

    public Empolyee(Integer id, Integer branchId, String fullName) {
        this.id = id;
        this.branchId = branchId;
        this.fullName = fullName;
    }




    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return fullName;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Empolyee() {
    }


}
