package com.example.hairmosa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hairmosa.R;
import com.example.hairmosa.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ActivityLoginBinding binding;

    //Attach the view to the screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.loginButton.setOnClickListener(view -> login());
        binding.registerTextView.setOnClickListener(view -> moveToRegisterActivity());
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            moveToMainActivity();
        }
    }

    private void moveToMainActivity() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void moveToRegisterActivity() {
        Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(i);
    }

    private void login() {
        binding.progressBar.setVisibility(View.VISIBLE);
        String email = binding.emailEditText.getText().toString();
        String password = binding.passwordEditText.getText().toString();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            onLoginFailed();
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            moveToMainActivity();
                        } else {
                            onLoginFailed();
                        }
                    });
        }
    }

    private void onLoginFailed() {
        binding.progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(this, getString(R.string.login_failed),
                Toast.LENGTH_SHORT).show();
    }
}