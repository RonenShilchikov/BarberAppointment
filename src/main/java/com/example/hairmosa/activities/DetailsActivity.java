package com.example.hairmosa.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.hairmosa.R;
import com.example.hairmosa.databinding.ActivityDetailsBinding;
import com.example.hairmosa.db.Consts;
import com.example.hairmosa.models.Appointment;
import com.example.hairmosa.models.EmployeDetailsWrapper;
import com.example.hairmosa.recycles.WorkerAdapter;
import com.example.hairmosa.utilities.TextUtility;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailsActivity extends AppCompatActivity implements WorkerAdapter.AppointmentDeleteListener {


    private ActivityDetailsBinding binding;
    public static String WRAPPER_KEY = "WRAPPER";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EmployeDetailsWrapper wrapper = TextUtility.fromJson(getIntent().getExtras().getString(WRAPPER_KEY, ""), EmployeDetailsWrapper.class);
        WorkerAdapter adapter = new WorkerAdapter(wrapper.getAppointments(), this);
        binding.recycleView.setHasFixedSize(true);
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        binding.recycleView.setAdapter(adapter);

    }

    @Override
    public void onAppointmentDeleteClickedListener(Appointment appointment) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(getResources().getString(R.string.warning))
                .setMessage(getResources().getString(R.string.are_you_sure))
                .setNegativeButton(getResources().getString(R.string.decline), (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton(getResources().getString(R.string.accept), (dialogInterface, i) -> {
                    db.collection(Consts.APPOINTEMNTS_DB).document(appointment.uid)
                            .delete().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(this, getResources().getString(R.string.item_deleted), Toast.LENGTH_SHORT).show();
                                    dialogInterface.dismiss();
                                    this.onBackPressed();
                                } else {
                                    Toast.makeText(this, getResources().getString(R.string.item_not_deleted), Toast.LENGTH_SHORT).show();
                                    dialogInterface.dismiss();
                                }
                            });
                }).
                show();
    }


}