package com.example.hairmosa.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EmployeDetailsWrapper implements Serializable {


    public EmployeDetailsWrapper(Empolyee empolyee) {
        this.empolyee = empolyee;
    }

    private Empolyee empolyee;
    private final List<Appointment> appointments = new ArrayList<>();

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    public int getEmployeeId() {
        return empolyee.id;
    }

    public String getEmployeName() {
        return empolyee.fullName;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }
}