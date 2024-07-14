package com.example.hairmosa.recycles;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hairmosa.databinding.AppointmentRowBinding;
import com.example.hairmosa.fragments.MyAppoinmentsFragment;
import com.example.hairmosa.models.MyAppointmentWrapper;

import java.util.List;

public class MyAppointmentAdapter extends RecyclerView.Adapter<MyAppointmentAdapter.ViewHolder> {

    private final List<MyAppointmentWrapper> appointments;
    private final String type;
    private final Listener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final AppointmentRowBinding binding;

        public ViewHolder(AppointmentRowBinding view) {
            super(view.getRoot());
            binding = view;
        }

        public void bind(MyAppointmentWrapper appointment, String type, Listener listener) {
            binding.dateTV.setText(appointment.appointment.date);
            binding.hourTV.setText(appointment.appointment.hour);
            binding.nameTV.setText(appointment.nameOfWorker);
            binding.branchTV.setText(appointment.appointment.branchName);
            if (TextUtils.equals(type, MyAppoinmentsFragment.EDIT_TYPE)) {
                binding.editButton.setVisibility(View.VISIBLE);
                binding.editButton.setOnClickListener(view -> listener.onAppointmentClicked(appointment, type));
            } else if (TextUtils.equals(type, MyAppoinmentsFragment.REMOVE_TYPE)) {
                binding.removeButton.setVisibility(View.VISIBLE);
                binding.removeButton.setOnClickListener(view -> listener.onAppointmentClicked(appointment, type));
            }
        }

    }

    public MyAppointmentAdapter(List<MyAppointmentWrapper> appointmentList, String type, Listener listener) {
        appointments = appointmentList;
        this.type = type;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AppointmentRowBinding itemBinding = AppointmentRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        MyAppointmentWrapper appointment = appointments.get(position);
        viewHolder.bind(appointment, type, listener);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public interface Listener {
        void onAppointmentClicked(MyAppointmentWrapper myAppointmentWrapper, String type);
    }
}

