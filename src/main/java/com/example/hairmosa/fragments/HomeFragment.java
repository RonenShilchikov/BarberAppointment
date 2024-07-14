package com.example.hairmosa.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hairmosa.databinding.FragmentHomeBinding;
import com.example.hairmosa.utilities.FragmentUtility;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.appointment.setOnClickListener(view1 -> FragmentUtility.addFragmentToBackStack(AppointmentFragment.newInstance(null), requireActivity().getSupportFragmentManager().beginTransaction()));
        binding.myAppointments.setOnClickListener(view12 -> FragmentUtility.addFragmentToBackStack(MyAppoinmentsFragment.newInstance(MyAppoinmentsFragment.REGULAR_TYPE), requireActivity().getSupportFragmentManager().beginTransaction()));
        binding.deleteAppointment.setOnClickListener(view13 -> FragmentUtility.addFragmentToBackStack(MyAppoinmentsFragment.newInstance(MyAppoinmentsFragment.REMOVE_TYPE), requireActivity().getSupportFragmentManager().beginTransaction()));
        binding.changeAppointment.setOnClickListener(view13 -> FragmentUtility.addFragmentToBackStack(MyAppoinmentsFragment.newInstance(MyAppoinmentsFragment.EDIT_TYPE), requireActivity().getSupportFragmentManager().beginTransaction()));
        binding.waze.setOnClickListener(view13 -> FragmentUtility.addFragmentToBackStack(NavigationFragment.newInstance(), requireActivity().getSupportFragmentManager().beginTransaction()));

    }


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

}