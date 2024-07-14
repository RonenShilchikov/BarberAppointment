package com.example.hairmosa.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.hairmosa.MyViewModel;
import com.example.hairmosa.R;
import com.example.hairmosa.databinding.FragmentAppointmentBinding;
import com.example.hairmosa.db.Consts;
import com.example.hairmosa.models.Appointment;
import com.example.hairmosa.models.Branch;
import com.example.hairmosa.models.Empolyee;
import com.example.hairmosa.models.MyAppointmentWrapper;
import com.example.hairmosa.models.User;
import com.example.hairmosa.utilities.TextUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

public class AppointmentFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private ArrayAdapter hoursAdapter;
    private ArrayAdapter branchesAdapter;
    private ArrayAdapter employesAdapter;
    private FragmentAppointmentBinding binding;
    private MyViewModel viewModel;
    private DatePickerDialog datePickerDialog;
    private Empolyee choosenEmploye;
    private Branch choosenBranch;

    private static String APPOINTMENT_KEY = "APPOINTMENT";
    private Boolean firstTimeEdit = true;

    private MyAppointmentWrapper editAppointment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAppointmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
        editAppointment = TextUtility.fromJson(getArguments().getString(APPOINTMENT_KEY, ""), MyAppointmentWrapper.class);
        if (isEdit()) {
            binding.createButton.setText(getResources().getString(R.string.update));
            binding.header.setText(getResources().getString(R.string.update_appointment));
        }
        viewModel.getAllBranches().observe(getViewLifecycleOwner(), branches -> {
                    branchesAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, branches);
                    binding.branchChoose.setAdapter(branchesAdapter);
                    binding.branchChoose.setOnItemClickListener((parent, view13, position, l) -> {
                        Object item = parent.getItemAtPosition(position);
                        clearEmployes();
                        clearHours();
                        if (item instanceof Branch) {
                            choosenBranch = (Branch) item;
                            fetchEmployess(choosenBranch.id);
                        }
                    });
                    if (isEdit() && firstTimeEdit) {
                        binding.dateEditText.setText(editAppointment.appointment.date);
                        binding.branchChoose.setText(editAppointment.appointment.branchName, false);
                        for (Branch branch : branches) {
                            if (branch.branchName.equals(editAppointment.appointment.branchName)) {
                                choosenBranch = branch;
                                fetchEmployess(branch.id);
                                break;
                            }
                        }

                    }
                }
        );

        viewModel.observerEmployeeChange().observe(getViewLifecycleOwner(), empolyees -> {
                    showProgressBar(false);
                    employesAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, empolyees);
                    binding.empolyeeChoose.setAdapter(employesAdapter);
                    binding.empolyeeChoose.setOnItemClickListener((parent, view13, position, l) -> {
                        clearHours();
                        Object item = parent.getItemAtPosition(position);
                        if (item instanceof Empolyee) {
                            choosenEmploye = (Empolyee) item;
                            fetchHours(choosenEmploye.id);
                        }
                    });
                    if (isEdit() && firstTimeEdit) {
                        binding.empolyeeChoose.setText(editAppointment.nameOfWorker, false);
                        for (Empolyee empolyee : empolyees) {
                            if (empolyee.fullName.equals(editAppointment.nameOfWorker)) {
                                choosenEmploye = empolyee;
                            }
                        }
                        fetchHours(editAppointment.appointment.idOfWorker);
                    }
                }
        );
        viewModel.observerHoursChange().observe(getViewLifecycleOwner(), hours -> {
            showProgressBar(false);
            hoursAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, hours);
            binding.hourChoose.setAdapter(hoursAdapter);
            if (isEdit() && firstTimeEdit) {
                binding.hourChoose.setText(editAppointment.appointment.hour, false);
                firstTimeEdit = false;
            }
        });

        binding.calenderIcon.setOnClickListener(view1 -> showDateTimePicker());
        binding.createButton.setOnClickListener(view12 -> createAppointment());

    }

    private boolean isEdit() {
        return editAppointment != null;
    }

    private void createAppointment() {
        if (!TextUtility.isValid(binding.dateEditText, binding.hourChoose, binding.empolyeeChoose, binding.branchChoose)) {
            Toast.makeText(requireContext(), getString(R.string.must_to_fill), Toast.LENGTH_SHORT).show();
        } else {
            FirebaseFirestore.getInstance().collection(Consts.USERS_DB).document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        User user = task.getResult().toObject(User.class);
                        Appointment appointment = new Appointment(choosenEmploye.id, binding.hourChoose.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), binding.dateEditText.getText().toString(), binding.branchChoose.getText().toString(), user.fullName);
                        CollectionReference ref = FirebaseFirestore.getInstance().collection(Consts.APPOINTEMNTS_DB);
                        if (isEdit()) {
                            ref.document(editAppointment.appointment.uid).set(appointment).addOnCompleteListener(this::onCompletedFireBase);
                        } else {
                            ref.add(appointment).addOnCompleteListener(this::onCompletedFireBase);
                        }
                    }
                }

                private void onCompletedFireBase(Task task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(requireContext(), !isEdit() ? getString(R.string.appointment_created) : getResources().getString(R.string.edit_appointemtn_sucsses), Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    } else {
                        Toast.makeText(requireContext(), !isEdit() ? getString(R.string.appointment_failed) : getResources().getString(R.string.edit_appointemtn_fail), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    private void fetchEmployess(Integer branchId) {
        showProgressBar(true);
        viewModel.fetchEmployes(branchId);
    }

    private void fetchHours(Integer employeId) {
        showProgressBar(true);
        viewModel.getAvailableHoursOfEmployee(employeId, binding.dateEditText.getText().toString());
    }

    private void showDateTimePicker() {
        final Calendar min_date_c = Calendar.getInstance();
        int year = min_date_c.get(Calendar.YEAR);
        int month = min_date_c.get(Calendar.MONTH);
        int day = min_date_c.get(Calendar.DAY_OF_MONTH);
        if (datePickerDialog == null) {
            datePickerDialog = DatePickerDialog.newInstance(this, year, month, day);
            datePickerDialog.setThemeDark(false);
            datePickerDialog.showYearPickerFirst(false);
            datePickerDialog.setTitle("Date Picker");
            datePickerDialog.setMinDate(min_date_c);
            Calendar max_date_c = Calendar.getInstance();
            max_date_c.set(Calendar.YEAR, min_date_c.get(Calendar.YEAR) + 1);
            datePickerDialog.setMaxDate(max_date_c);
            for (Calendar loopdate = min_date_c; min_date_c.before(max_date_c); min_date_c.add(Calendar.DATE, 1), loopdate = min_date_c) {
                int dayOfWeek = loopdate.get(Calendar.DAY_OF_WEEK);
                if (dayOfWeek == Calendar.FRIDAY || dayOfWeek == Calendar.SATURDAY) {
                    Calendar[] disabledDays = new Calendar[1];
                    disabledDays[0] = loopdate;
                    datePickerDialog.setDisabledDays(disabledDays);
                }
            }
        }
        if (!datePickerDialog.isVisible()) {
            datePickerDialog.show(requireActivity().getSupportFragmentManager(), "DatePickerDialog");
        }
    }

    public AppointmentFragment() {
        // Required empty public constructor
    }

    public static AppointmentFragment newInstance(MyAppointmentWrapper appoinment) {
        AppointmentFragment fragment = new AppointmentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(APPOINTMENT_KEY, TextUtility.toJson(appoinment));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        binding.dateEditText.setText(String.format("%d/%d/%d", dayOfMonth, monthOfYear, year));

        clearEmployes();
        clearHours();
        showProgressBar(false);
    }

    private void showProgressBar(boolean visible) {
        binding.progressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void clearEmployes() {
        choosenEmploye = null;
        binding.empolyeeChoose.setText("");
        if (employesAdapter != null) {
            employesAdapter.clear();
            employesAdapter.notifyDataSetChanged();
        }
    }

    private void clearHours() {
        binding.hourChoose.setText("");
        if (hoursAdapter != null) {
            hoursAdapter.clear();
            hoursAdapter.notifyDataSetChanged();
        }
    }

}