package com.example.hairmosa.models;

import com.google.firebase.firestore.Exclude;

public class Appointment {


    public Appointment() {

    }

    public Appointment(Integer idOfWorker, String hour, String clientUID, String date, String branchName, String nameOfClient) {
        this.idOfWorker = idOfWorker;
        this.hour = hour;
        this.clientUID = clientUID;
        this.date = date;
        this.branchName = branchName;
        this.nameOfClient = nameOfClient;
    }

    public Integer getIdOfWorker() {
        return idOfWorker;
    }

    public void setIdOfWorker(Integer idOfWorker) {
        this.idOfWorker = idOfWorker;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getClientUID() {
        return clientUID;
    }

    public void setClientUID(String clientUID) {
        this.clientUID = clientUID;
    }

    public Integer idOfWorker;
    public String hour;
    public String clientUID;

    public String getNameOfClient() {
        return nameOfClient;
    }

    public void setNameOfClient(String nameOfClient) {
        this.nameOfClient = nameOfClient;
    }

    public String nameOfClient;

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String branchName;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String date;

    @Exclude
    public String getId() {
        return uid;
    }

    @Exclude
    public String uid;

    @Exclude
    public void setUid(String uid) {
        this.uid = uid;
    }

}
