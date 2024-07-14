package com.example.hairmosa.fragments;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.hairmosa.MyViewModel;
import com.example.hairmosa.R;
import com.example.hairmosa.databinding.FragmentMyAppointmentsBinding;
import com.example.hairmosa.models.Appointment;
import com.example.hairmosa.models.Empolyee;
import com.example.hairmosa.models.MyAppointmentWrapper;
import com.example.hairmosa.recycles.MyAppointmentAdapter;
import com.example.hairmosa.utilities.FragmentUtility;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class MyAppoinmentsFragment extends Fragment implements MyAppointmentAdapter.Listener {
    private FragmentMyAppointmentsBinding binding;
    private MyViewModel viewModel;

    private MyAppointmentAdapter adapter;

    public static final String EDIT_TYPE = "EDIT";
    public static final String REMOVE_TYPE = "REMOVE";
    public static final String REGULAR_TYPE = "REGULAR";

    private static final String TYPE_KEY = "TYPE";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyAppointmentsBinding.inflate(inflater, container, false);
        binding.recycleView.setHasFixedSize(true);
        binding.recycleView.setLayoutManager(new LinearLayoutManager(requireContext()));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
        binding.progressBar.setVisibility(View.VISIBLE);
        String type = getArguments().getString(TYPE_KEY);
        viewModel.getAllMyAppointments().observe(getViewLifecycleOwner(), appointments -> {
            viewModel.getEmployes().observe(getViewLifecycleOwner(), empolyees -> {
                List<MyAppointmentWrapper> appointmentWrappers = new ArrayList<>();
                for (Appointment appointment : appointments) {
                    for (Empolyee empolyee : empolyees) {
                        if (empolyee.id.equals(appointment.idOfWorker)) {
                            appointmentWrappers.add(new MyAppointmentWrapper(appointment, empolyee.fullName));
                            break;
                        }
                    }
                }
                adapter = new MyAppointmentAdapter(appointmentWrappers, type, this);
                binding.recycleView.setAdapter(adapter);
                binding.progressBar.setVisibility(View.INVISIBLE);
            });
        });
    }


    public static MyAppoinmentsFragment newInstance(String type) {
        MyAppoinmentsFragment fragment = new MyAppoinmentsFragment();
        Bundle args = new Bundle();
        args.putString(TYPE_KEY, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAppointmentClicked(MyAppointmentWrapper myAppointmentWrapper, String type) {
        if (TextUtils.equals(type, REMOVE_TYPE)) {
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getResources().getString(R.string.warning))
                    .setMessage(getResources().getString(R.string.are_you_sure))
                    .setNegativeButton(getResources().getString(R.string.decline), (dialogInterface, i) -> dialogInterface.dismiss())
                    .setPositiveButton(getResources().getString(R.string.accept), (dialogInterface, i) -> {
                        viewModel.deleteAppointment(myAppointmentWrapper.appointment.uid).observe(getViewLifecycleOwner(), aBoolean -> {
                            if (aBoolean) {
                                Toast.makeText(requireContext(), getResources().getString(R.string.item_deleted), Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                                requireActivity().onBackPressed();
                            } else {
                                Toast.makeText(requireContext(), getResources().getString(R.string.item_not_deleted), Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                            }
                        });
                    }).show();
        } else if (TextUtils.equals(type, EDIT_TYPE)) {
            FragmentUtility.addFragmentToBackStack(AppointmentFragment.newInstance(myAppointmentWrapper), requireActivity().getSupportFragmentManager().beginTransaction());
        }
    }


}