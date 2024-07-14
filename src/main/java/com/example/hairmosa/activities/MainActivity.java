package com.example.hairmosa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.hairmosa.MyViewModel;
import com.example.hairmosa.R;
import com.example.hairmosa.databinding.ActivityMainBinding;
import com.example.hairmosa.fragments.HomeFragment;
import com.example.hairmosa.fragments.ManagerFragment;
import com.example.hairmosa.models.UserType;
import com.example.hairmosa.utilities.FragmentUtility;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private MyViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initDrawer(); // --> hamburger
        viewModel = new ViewModelProvider(this).get(MyViewModel.class);
        //observe ==  להזאין
        viewModel.getUser().observe(this, user -> setFragment(user.getUserType()));
    }

    private void setFragment(UserType userType) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (userType) {
            case Client: {
                ft.replace(R.id.container, HomeFragment.newInstance());
                break;
            }
            case Manager: {
                ft.replace(R.id.container, ManagerFragment.newInstance());
                break;
            }
        }
        ft.commit();
    }

    private void initDrawer() {
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.myDrawerLayout, R.string.nav_open, R.string.nav_close);

        binding.myDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.navView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout: {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                return true;
            }
        }
        return false;
    }

}