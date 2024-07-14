package com.example.hairmosa;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hairmosa.db.Consts;
import com.example.hairmosa.models.Appointment;
import com.example.hairmosa.models.Branch;
import com.example.hairmosa.models.EmployeDetailsWrapper;
import com.example.hairmosa.models.Empolyee;
import com.example.hairmosa.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyViewModel extends ViewModel {
    private static final String TAG = "MyViewModel";
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private MutableLiveData<List<Empolyee>> employess;
    private MutableLiveData<List<String>> hours;

    public LiveData<User> getUser() {
        final MutableLiveData<User> user = new MutableLiveData<>();
        db.collection(Consts.USERS_DB).document(firebaseAuth.getUid()).get().addOnSuccessListener(documentSnapshot -> user.postValue(documentSnapshot.toObject(User.class)));
        return user;
    }

    public LiveData<List<String>> observerHoursChange() {
        hours = new MutableLiveData<>();
        return hours;
    }

    public LiveData<List<Empolyee>> observerEmployeeChange() {
        employess = new MutableLiveData<>();
        return employess;
    }


    public void fetchEmployes(Integer branchId) {
        db.collection(Consts.EMPLOYESS_DB).whereEqualTo(Consts.BRANCH_ID_COLUMN, branchId).get().addOnCompleteListener(task -> employess.postValue(extractEmployes(task)));
    }

    public LiveData<List<Empolyee>> getEmployes() {
        final MutableLiveData<List<Empolyee>> liveData = new MutableLiveData();
        db.collection(Consts.EMPLOYESS_DB).get().addOnCompleteListener(task -> liveData.postValue(extractEmployes(task)));
        return liveData;
    }

    private List<Empolyee> extractEmployes(Task<QuerySnapshot> task) {
        List<Empolyee> list = new ArrayList<>();
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
                list.add(document.toObject(Empolyee.class));
            }
        }
        return list;
    }

    public LiveData<List<Branch>> getAllBranches() {
        final MutableLiveData<List<Branch>> branches = new MutableLiveData<>();
        db.collection(Consts.BRANCHES_DB)
                .get().addOnCompleteListener(task -> {
                    List<Branch> list = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Branch branch = document.toObject(Branch.class);
                            list.add(branch);
                        }
                        //when it ready...
                        branches.postValue(list);
                    }
                });
        return branches;
    }


    public LiveData<List<Appointment>> getAllMyAppointments() {
        final MutableLiveData<List<Appointment>> appointments = new MutableLiveData<>();
        db.collection(Consts.APPOINTEMNTS_DB)
                .whereEqualTo(Consts.CLIENT_UID_COLUMN, FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(task -> {
                    List<Appointment> list = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Appointment appointment = document.toObject(Appointment.class);
                            appointment.setUid(document.getId());
                            list.add(appointment);
                        }
                        appointments.postValue(list);
                    }
                });
        return appointments;
    }


    public void getAvailableHoursOfEmployee(Integer id, String date) {
        List<String> list = new ArrayList<>(Consts.AVAILABLE_HOURS);
        db.collection(Consts.APPOINTEMNTS_DB)
                .whereEqualTo(Consts.ID_OF_WORKER_COLUMN, id)
                .whereEqualTo(Consts.DATE_COLUMN, date)
                .get().addOnCompleteListener(task -> {
                    List<String> hoursList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Appointment appointment = document.toObject(Appointment.class);
                        hoursList.add(appointment.hour);
                    }
                    if (hoursList.size() > 0) {
                        list.removeAll(hoursList);
                    }
                    Log.d(TAG, "onComplete: " + list);
                    hours.postValue(list);
                });
    }

    public LiveData<Boolean> deleteAppointment(String uid) {
        final MutableLiveData<Boolean> liveData = new MutableLiveData();
        db.collection(Consts.APPOINTEMNTS_DB).document(uid)
                .delete().addOnCompleteListener(task -> liveData.postValue(task.isSuccessful()));
        return liveData;
    }


    public LiveData<List<EmployeDetailsWrapper>> getAllWorkersEmployeDetails(ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        final MutableLiveData<List<EmployeDetailsWrapper>> liveData = new MutableLiveData();

        db.collection(Consts.EMPLOYESS_DB).get().addOnCompleteListener(task -> {
            final List<EmployeDetailsWrapper> list = new ArrayList<>();
            for (QueryDocumentSnapshot document : task.getResult()) {
                list.add(new EmployeDetailsWrapper(document.toObject(Empolyee.class)));
            }
            db.collection(Consts.APPOINTEMNTS_DB)
                    .get().addOnCompleteListener(task2 -> {
                        for (QueryDocumentSnapshot document : task2.getResult()) {
                            Appointment appointment = document.toObject(Appointment.class);
                            for (EmployeDetailsWrapper employeDetailsWrapper : list) {
                                if (employeDetailsWrapper.getEmployeeId() == appointment.idOfWorker) {
                                    employeDetailsWrapper.addAppointment(appointment);
                                    appointment.setUid(document.getId());
                                    break;
                                }
                            }
                        }
                        liveData.postValue(list);
                    });
        });

        return liveData;
    }


}
