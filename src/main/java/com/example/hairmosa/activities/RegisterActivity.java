package com.example.hairmosa.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hairmosa.R;
import com.example.hairmosa.databinding.ActivityRegisterBinding;
import com.example.hairmosa.db.Consts;
import com.example.hairmosa.models.User;
import com.example.hairmosa.models.UserType;
import com.example.hairmosa.utilities.TextUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ActivityRegisterBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.registerButton.setOnClickListener(view -> tryRegister());
    }


    private void tryRegister() {
        binding.progressBar.setVisibility(View.VISIBLE);
        if (!TextUtility.isValid(binding.emailEditText, binding.passwordEditTextVerify, binding.passwordEditText, binding.fullNameEditText)) {
            Toast.makeText(this, getString(R.string.error_text), Toast.LENGTH_SHORT).show();
            binding.progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        if (!binding.passwordEditText.getText().toString().equals(binding.passwordEditTextVerify.getText().toString())) {
            Toast.makeText(this, getString(R.string.error_password), Toast.LENGTH_SHORT).show();
            binding.progressBar.setVisibility(View.INVISIBLE);
            return;
        }


        mAuth.createUserWithEmailAndPassword(binding.emailEditText.getText().toString().trim(), binding.passwordEditText.getText().toString().trim())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        db.collection(Consts.USERS_DB).document(task.getResult().getUser().getUid()).set(new User(binding.fullNameEditText.getText().toString(), UserType.Client)).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.register_sucsses), Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    registerFailed(task.getException().getLocalizedMessage());
                                }
                            }
                        }).addOnFailureListener(e -> registerFailed(e.getLocalizedMessage()));
                    } else {
                        registerFailed(task.getException().getLocalizedMessage());
                    }
                });

    }

    private void registerFailed(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        binding.progressBar.setVisibility(View.INVISIBLE);
    }


}