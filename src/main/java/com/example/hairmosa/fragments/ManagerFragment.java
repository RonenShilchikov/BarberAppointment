package com.example.hairmosa.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.hairmosa.MyViewModel;
import com.example.hairmosa.activities.DetailsActivity;
import com.example.hairmosa.databinding.FragmentManagerBinding;
import com.example.hairmosa.models.Appointment;
import com.example.hairmosa.models.EmployeDetailsWrapper;
import com.example.hairmosa.recycles.ManagerAdapter;
import com.example.hairmosa.utilities.TextUtility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class ManagerFragment extends Fragment implements ManagerAdapter.Listener {

    private MyViewModel viewModel;
    private FragmentManagerBinding binding;
    private List<EmployeDetailsWrapper> mEmployeDetailsWrappers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentManagerBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
        binding.recycleView.setHasFixedSize(true);
        binding.recycleView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.report.setOnClickListener(view -> shareCsvData());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getAllWorkersEmployeDetails(binding.progressBar).observe(getViewLifecycleOwner(), new Observer<List<EmployeDetailsWrapper>>() {
            @Override
            public void onChanged(List<EmployeDetailsWrapper> employeDetailsWrappers) {
                ManagerAdapter adapter = new ManagerAdapter(employeDetailsWrappers, ManagerFragment.this);
                mEmployeDetailsWrappers = employeDetailsWrappers;
                binding.recycleView.setAdapter(adapter);
                binding.progressBar.setVisibility(View.INVISIBLE);
                EmployeDetailsWrapper max = getMaxEmploye(employeDetailsWrappers);
                binding.content.setText("העובד המצטיין עם הכי הרבה תורים הוא - " + max.getEmployeName());
            }
        });
    }

    private EmployeDetailsWrapper getMaxEmploye(List<EmployeDetailsWrapper> list) {
        EmployeDetailsWrapper employeDetailsWrapper = list.get(0);
        int maxSize = employeDetailsWrapper.getAppointments().size();
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).getAppointments().size() > maxSize) {
                employeDetailsWrapper = list.get(i);
                maxSize = list.get(i).getAppointments().size();
            }
        }
        return employeDetailsWrapper;
    }

    public static ManagerFragment newInstance() {
        return new ManagerFragment();
    }

    @Override
    public void onEmployeClicked(EmployeDetailsWrapper myAppointmentWrapper) {
        Intent intent = new Intent(getActivity(), DetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(DetailsActivity.WRAPPER_KEY, TextUtility.toJson(myAppointmentWrapper));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void shareCsvData() {
        if (mEmployeDetailsWrappers != null) {
            try {
                File file = new File(requireContext().getFilesDir(), "data.csv");
                if (!file.exists()) {
                    file.createNewFile();
                }
                StringBuilder sb = new StringBuilder();
                sb.append("Name of employe").append(",").append("date").append(",").append("branch").append(",").append("time").append(",").append("name of client").append("\n");
                for (EmployeDetailsWrapper e1 : mEmployeDetailsWrappers) {
                    for (Appointment appointment : e1.getAppointments()) {
                        sb.append(e1.getEmployeName()).append(",").append(appointment.getDate()).append(",").append(appointment.branchName).append(",").append(appointment.hour).append(",").append(appointment.nameOfClient).append("\n");
                    }
                }
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(sb.toString());
                bw.close();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.putExtra(
                        Intent.EXTRA_STREAM,
                        FileProvider.getUriForFile(
                                requireActivity(),
                                requireActivity().getBaseContext().getPackageName() + ".fileprovider",
                                file
                        )
                );
                requireActivity().startActivity(Intent.createChooser(intent, "share file with"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

}