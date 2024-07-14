package com.example.hairmosa.fragments;


import android.content.Intent;
import android.net.Uri;
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
import com.example.hairmosa.databinding.FragmentBranchesBinding;
import com.example.hairmosa.models.Branch;

public class NavigationFragment extends Fragment {

    private ArrayAdapter branchesAdapter;
    private FragmentBranchesBinding binding;
    private MyViewModel viewModel;
    private Branch choosenBranch;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBranchesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
        viewModel.getAllBranches().observe(getViewLifecycleOwner(), branches -> {
                    branchesAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, branches);
                    binding.branchChoose.setAdapter(branchesAdapter);
                    binding.branchChoose.setOnItemClickListener((parent, view13, position, l) -> {
                        Object item = parent.getItemAtPosition(position);
                        if (item instanceof Branch) {
                            choosenBranch = (Branch) item;
                        }
                    });
                }
        );
        binding.navigateButton.setOnClickListener(view1 -> {
            if (choosenBranch == null) {
                Toast.makeText(requireContext(), getString(R.string.choose_branch), Toast.LENGTH_SHORT).show();
            } else {
                String uri = String.format("geo:%s?q=%s", choosenBranch.location, choosenBranch.location);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
            }
        });

    }


    public static NavigationFragment newInstance() {
        return new NavigationFragment();
    }


}