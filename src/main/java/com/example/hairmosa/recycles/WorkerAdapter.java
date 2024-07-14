package com.example.hairmosa.recycles;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hairmosa.R;
import com.example.hairmosa.activities.DetailsActivity;
import com.example.hairmosa.databinding.WorkerRowBinding;
import com.example.hairmosa.models.Appointment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class WorkerAdapter extends RecyclerView.Adapter<WorkerAdapter.ViewHolder> {

    public interface AppointmentDeleteListener {
        void onAppointmentDeleteClickedListener(Appointment appointment);
    }

    private final List<Appointment> appointments;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final WorkerRowBinding binding;

        public ViewHolder(WorkerRowBinding view) {
            super(view.getRoot());
            binding = view;
        }


        public void bind(Appointment appointment, AppointmentDeleteListener listener) {
            binding.dateTV.setText(appointment.date);
            binding.hourTV.setText(appointment.hour);
            binding.nameTV.setText(appointment.nameOfClient);
            binding.deleteAppointment.setOnClickListener(view -> listener.onAppointmentDeleteClickedListener(appointment));
        }

    }

    private AppointmentDeleteListener listener;

    public WorkerAdapter(List<Appointment> appointmentList, AppointmentDeleteListener listener) {
        appointments = appointmentList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        WorkerRowBinding itemBinding = WorkerRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.bind(appointments.get(position), listener);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return appointments.size();
    }

}

