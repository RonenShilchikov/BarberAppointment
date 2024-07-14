package com.example.hairmosa.recycles;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hairmosa.databinding.ManagerRowBinding;
import com.example.hairmosa.models.EmployeDetailsWrapper;

import java.util.List;

public class ManagerAdapter extends RecyclerView.Adapter<ManagerAdapter.ViewHolder> {

    private final List<EmployeDetailsWrapper> list;
    private final Listener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ManagerRowBinding binding;

        public ViewHolder(ManagerRowBinding view) {
            super(view.getRoot());
            binding = view;
        }

        public void bind(EmployeDetailsWrapper appointment) {
            binding.nameTV.setText(appointment.getEmployeName());
        }

    }

    public ManagerAdapter(List<EmployeDetailsWrapper> appointmentList, Listener listener) {
        list = appointmentList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ManagerRowBinding itemBinding = ManagerRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final EmployeDetailsWrapper wrapper = list.get(position);
        viewHolder.bind(wrapper);
        viewHolder.binding.getRoot().setOnClickListener(view -> listener.onEmployeClicked(wrapper));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface Listener {
        void onEmployeClicked(EmployeDetailsWrapper myAppointmentWrapper);
    }
}

