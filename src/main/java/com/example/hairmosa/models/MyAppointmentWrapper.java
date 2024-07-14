package com.example.hairmosa.models;

import java.io.Serializable;

public class MyAppointmentWrapper implements Serializable {

    public final Appointment appointment;
    public final String nameOfWorker;

    public MyAppointmentWrapper(Appointment appointment, String nameOfWorker) {
        this.appointment = appointment;
        this.nameOfWorker = nameOfWorker;
    }
}
